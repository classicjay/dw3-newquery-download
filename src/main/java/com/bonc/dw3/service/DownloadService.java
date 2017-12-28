package com.bonc.dw3.service;

import com.bonc.dw3.common.CommonUtils;
import com.bonc.dw3.entity.DownloadFile;
import com.bonc.dw3.mapper.DownloadFileMapper;
import com.bonc.dw3.mapper.DownloadMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>Title: BONC -  DownloadService</p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright BONC(c) 2013 - 2025 </p>
 * <p>Company: 北京东方国信科技股份有限公司 </p>
 *
 * @author zhaojie
 * @version 1.0.0
 */
@Service
public class DownloadService {
    private final Logger log = LoggerFactory.getLogger(DownloadService.class);

    @Autowired
    DownloadMapper downloadMapper;

    @Autowired
    DownloadFileMapper downloadFileMapper;

    @Autowired
    Environment environment;
    /**
     * 指标编码-名称映射
     */
    private static HashMap<String,String> KPI_CODE_MAP = new HashMap<>();
    /**
     * 省分地市联合编码的list
     */
    private static List<String> PRO_AREA_CODE_UNION_LIST = new ArrayList<>();
    /**
     * 省分地市联合编码-名称的映射
     */
    private static HashMap<String,String> PRO_AREA_NAME_UNION_MAP = new HashMap<>();
    /**
     * 维度编码-名称映射
     */
    private static final HashMap<String,String> DIMENSION_CODE_MAP = new HashMap(){{
        put("20AAAAAA","2G业务");
        put("30AAAAAA","3G业务");
        put("40AAAAAA","4G业务");
        put("90AAAAAA","其他业务");

        put("10AA","电子渠道");
        put("20AA","集团渠道");
        put("30AA","实体渠道");
        put("99AA","其他渠道");

        put("01","2I2C产品");
        put("02","冰激凌套餐");
        put("03","流量王A套餐");
        put("04","日租卡套餐");
        put("99","其他套餐");

        put("**","不区分");

        put("999","全部");
    }};
    /**
     * 业务类型
     */
    private static final List<String> SERVICE_TYPE_LIST = Arrays.asList(new String[]{"20AAAAAA","30AAAAAA","40AAAAAA", "90AAAAAA", "**","999"});
    /**
     * 渠道类型
     */
    private static final List<String> CHANNEL_TYPE_LIST = Arrays.asList(new String[]{"10AA","20AA","30AA","99AA","**","999"});
    /**
     *产品类型
     */
    private static final List<String> PRODUCT_ID_LIST = Arrays.asList(new String[]{"01","02","03","04","99","**","999"});

    private static final List<String> DIMENSION_NAME_LIST = Arrays.asList(new String[]{"2G业务","3G业务","4G业务","其他业务", "2I2C产品","冰激凌套餐","流量王A","日租卡","其他","实体渠道","电子渠道","集团渠道","其他渠道"});
    private static final String DIMENSION_NAME_STR = "2G业务,3G业务,4G业务,其他业务,2I2C产品,冰激凌套餐,流量王A,日租卡,其他,实体渠道,电子渠道,集团渠道,其他渠道";
    private static final String KPI_CODE_FIELD = "KPI_CODE";
    private static final String KPI_FULL_NAME_FIELD = "KPI_FULL_NAME";

    private static final String AREA_ID_FIELD = "AREA_ID";
    private static final String AREA_SHORT_DESC_FIELD = "AREA_SHORT_DESC";
    private static final String PROV_ID_FIELD = "PROV_ID";
    private static final String PRO_NAME_FIELD = "PRO_NAME";

    //模板文件路径
    private static String TEMPLATE_FILE_PATH;
    //源数据文件路径
    private static String SOURCEDATA_FILE_PATH;
    //导出文件路径
    private static String EXPORT_FILE_PATH;

    private static final String KPI_VALUE_NAME = "当日值";
    private static final String M_TM_VALUE_NAME = "本月累计";
    private static final String YOY_NAME = "累计同比";
    private static final String MOM_NAME = "累计环比";

    /**
     * 初始化执行方法
     */
    @PostConstruct
    public void init(){
//        TEMPLATE_FILE_PATH = environment.getProperty("template.file.path");
//        SOURCEDATA_FILE_PATH = environment.getProperty("sourcedata.file.path");
//        EXPORT_FILE_PATH = environment.getProperty("export.file.path");

        TEMPLATE_FILE_PATH = "C:/Users/毛/Desktop/template.xlsx";
        SOURCEDATA_FILE_PATH = "C:/Users/毛/Desktop/";
        EXPORT_FILE_PATH = "C:/Users/毛/Desktop/";

        List<HashMap<String,String>> kpiCodeList = downloadMapper.getKpiMapping();
        for (HashMap<String,String> map:kpiCodeList){
            KPI_CODE_MAP.put(map.get(KPI_CODE_FIELD),map.get(KPI_FULL_NAME_FIELD));
        }
        //省分地市list，不含各省-1以及全国北10南21情况
        List<HashMap<String,String>> areaProvList = downloadMapper.getAreaProvCode();
        //省份list，包含全国及北10南21
        List<HashMap<String,String>> provList = downloadMapper.getProvCode();
        //给areaProvList中添加每个省的-1，以及全国北10南21情况
        for (HashMap<String,String> provMap:provList){
            HashMap<String,String> extraMap = new HashMap<>();
            extraMap.put(AREA_ID_FIELD,"-1");
            extraMap.put(AREA_SHORT_DESC_FIELD,provMap.get(PRO_NAME_FIELD));
            extraMap.put(PROV_ID_FIELD,provMap.get(PROV_ID_FIELD));
            extraMap.put(PRO_NAME_FIELD,provMap.get(PRO_NAME_FIELD));
            areaProvList.add(extraMap);
        }
        //对省份地市list排序
        sortList(areaProvList,PROV_ID_FIELD);
        //全国map
        HashMap<String,String> allProMap = areaProvList.get(areaProvList.size()-3);
        //北10map
        HashMap<String,String> northMap = areaProvList.get(areaProvList.size()-2);
        //南21map
        HashMap<String,String> southMap = areaProvList.get(areaProvList.size()-1);
        areaProvList = areaProvList.subList(0,areaProvList.size()-3);
        areaProvList.add(0,allProMap);
        areaProvList.add(1,northMap);
        areaProvList.add(2,southMap);

        //生成省份地市联合编码list，并添加相应映射
        for (HashMap<String,String> map:areaProvList){
            PRO_AREA_CODE_UNION_LIST.add(map.get(PROV_ID_FIELD)+"|"+map.get(AREA_ID_FIELD));
            PRO_AREA_NAME_UNION_MAP.put(map.get(PROV_ID_FIELD)+"|"+map.get(AREA_ID_FIELD),map.get(PRO_NAME_FIELD)+"|"+map.get(AREA_SHORT_DESC_FIELD));
        }
    }

    /**
     *
     * @param subjectCode 传入参数：专题code、账期、文件id
     * @param date
     * @param fileId
     * @return 生成文件路径
     * @throws IOException
     */
    public String generateExcel(String subjectCode,String date,String fileId) throws IOException {
        String sourceFilePath = SOURCEDATA_FILE_PATH+date+".txt";
        Group2Data gd = new Group2Data(sourceFilePath);
        List<String> subjectKpiList = downloadMapper.getSubjectKpi(subjectCode);
        log.info("subjectKpiList:"+subjectKpiList);
        List<String> lines = gd.groupbySth(subjectKpiList);

        //excel模板路径
        File file = new File(TEMPLATE_FILE_PATH);
        FileInputStream in = new FileInputStream(file);
        //读取excel模板
        XSSFWorkbook wb = new XSSFWorkbook(in);
        //读取了模板内所有sheet内容，读取第一个sheet
        XSSFSheet sheet = wb.getSheetAt(0);
        //设置居中
        CellStyle styleCenter = wb.createCellStyle();
        styleCenter.setAlignment(HorizontalAlignment.CENTER);
        //如果这行没有了，整个公式都不会有自动计算的效果
        sheet.setForceFormulaRecalculation(true);
        //参与笛卡尔积的元素
        List<List<String>> dimValue = new ArrayList<List<String>>();
        //笛卡尔积运算结果
        List<List<String>> recursiveResult = new ArrayList<List<String>>();
        //加入地市、三维度
        dimValue.add(PRO_AREA_CODE_UNION_LIST);
        dimValue.add(SERVICE_TYPE_LIST);
        dimValue.add(CHANNEL_TYPE_LIST);
        dimValue.add(PRODUCT_ID_LIST);

        recursive(dimValue, recursiveResult, 0, new ArrayList<String>());
        log.info("笛卡尔积长度："+recursiveResult.size());
        //编码拼接字符串和rowNum的映射
        Map<String,String> combineMap = new HashMap<>();
        //从第三行开始生成
        int index = 2;
        //填充模板左边各维度组合结果
        for (List<String> singleRecursive:recursiveResult){
            StringBuffer stringBuffer = new StringBuffer();
            //在index+1行生成一行row
            XSSFRow row = sheet.createRow(index);
            //在row行第一列插入数据账期
            row.createCell(0).setCellValue(date);
            for (int i=0;i<singleRecursive.size();i++){
                String dimsionValue = singleRecursive.get(i);
                if (i==0){
                    //省分地市名称联合字段
                    String proAreaNameUnion = PRO_AREA_NAME_UNION_MAP.get(dimsionValue);
                    String[] proAreaNameUnionArr = proAreaNameUnion.split("\\|",-1);
                    row.createCell(i+1).setCellValue(proAreaNameUnionArr[0]);
                    row.createCell(i+2).setCellValue(proAreaNameUnionArr[1]);
                }else {
                    //三维度字段
                    row.createCell(i+2).setCellValue(DIMENSION_CODE_MAP.get(dimsionValue));
                }
                if (stringBuffer.length() == 0){
                    stringBuffer.append(dimsionValue);
                }else {
                    stringBuffer.append("|");
                    stringBuffer.append(dimsionValue);
                }
            }
            //combineMap的key为维度组合，value为行数
            combineMap.put(stringBuffer.toString(),String.valueOf(index));
            index++;
        }

        //保存遍历过程中上一个kpiCode，如果currentKpi与其不匹配，则表示为新指标，另起一列
        String tempKpiCode = new String();
        //当前指标对应的列数
        int colNum = 0;
        //每新增一个指标，列数都要+4，即一个指标占4列（当日，本月，同比，环比）
        int count = 0;
        //读取数据文件
//        List<String> lines = Files.readAllLines(Paths.get(DATA_FILE_PATH), StandardCharsets.UTF_8);

        for (String line:lines){
            String items[] = line.split("\\|",-1);
            //当前数据中的kpiCode
            String currentKpi = items[9];
            if(null == KPI_CODE_MAP.get(currentKpi) || "".equals(KPI_CODE_MAP.get(currentKpi))){
                log.info("kpiCode："+currentKpi+"在码表中查不到");
                continue;
            }
            //指标名行
            XSSFRow headRow = sheet.getRow(0);
            //字段名行
            XSSFRow fieldRow = sheet.getRow(1);
            //当前kpi的指标信息，单位格式等
            Map<String,Object> kpiInfoMap = new HashedMap();
            //currentKpi与tempKpiCode不匹配
            if (!tempKpiCode.equals(currentKpi)){
                kpiInfoMap = downloadMapper.getKpiInfo(currentKpi);
                //指标单位为基础单位，已在sql中判断
                String kpiUnit = kpiInfoMap.get("UNIT").toString();
                //定位到列数
                colNum =  6 + count*4;
                //显示指标名称的区域合并并设置居中
                CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, colNum, colNum+3);
                sheet.addMergedRegion(cellRangeAddress);

                XSSFCell kpiNameCell = headRow.createCell(colNum);
                kpiNameCell.setCellValue(KPI_CODE_MAP.get(currentKpi)+"("+kpiUnit+")");
                //指标名称单元格设置居中
                kpiNameCell.setCellStyle(styleCenter);
                fieldRow.createCell(colNum).setCellValue(KPI_VALUE_NAME);
                fieldRow.createCell(colNum+1).setCellValue(M_TM_VALUE_NAME);
                fieldRow.createCell(colNum+2).setCellValue(MOM_NAME);
                fieldRow.createCell(colNum+3).setCellValue(YOY_NAME);
                count++;
                tempKpiCode = currentKpi;
                log.info("kpiCode："+tempKpiCode);
                //当前kpi的指标信息，单位格式等

            }

            String dayValue = items[10];
            String thisMonthValue = items[12];
            String lastMonthValue = items[14];
            String lastYearThisMonthValue = items[15];
            StringBuffer stringBuffer = new StringBuffer();
            for (int i=3;i<9;i++){
                if (i==5){
                    continue;
                }
                if (stringBuffer.length() == 0){
                    stringBuffer.append(items[i]);
                }else {
                    stringBuffer.append("|");
                    stringBuffer.append(items[i]);
                }
            }
            //定位到行数
            String rowNum = combineMap.get(stringBuffer.toString());
            if (null == rowNum || "".equals(rowNum)){
                //数据中字符拼接在维度组合映射中找不到的情况
                log.info("未找到匹配的数据combine："+stringBuffer.toString());
                log.info("rowNum："+rowNum);
            }else {
                //填入数据
                XSSFRow row = sheet.getRow(Integer.parseInt(rowNum));
                row.createCell(colNum).setCellValue(formatKpiValue(kpiInfoMap,dayValue));
                row.createCell(colNum+1).setCellValue(formatKpiValue(kpiInfoMap,thisMonthValue));
//                row.createCell(colNum+2).setCellValue(sylj);
//                row.createCell(colNum+3).setCellValue(qntq);
                row.createCell(colNum+2).setCellValue(calculateMoM(thisMonthValue,lastMonthValue));
                row.createCell(colNum+3).setCellValue(calculateYoY(thisMonthValue,lastYearThisMonthValue));
            }
        }
        String exportFilePath = EXPORT_FILE_PATH+fileId+".xlsx";
        //导出数据
        FileOutputStream out = new FileOutputStream(exportFilePath);
        wb.write(out);
        out.close();
        in.close();
        return exportFilePath;
    }

    /**
     * 下载列表
     * @param paramMap
     * @return
     */
    public List<DownloadFile> downloadTable(HashMap<String,String> paramMap){
        List<DownloadFile> fileList = new ArrayList<>();
        String userId = new String();
        if (CommonUtils.isNotBlank(paramMap.get("userId"))){
            userId = paramMap.get("userId");
        }
        fileList = downloadFileMapper.getFileByUserId(userId);
        for (DownloadFile downloadFile:fileList){
            ObjectMapper objectMapper = new ObjectMapper();
            String json = new String();
            try {
                json = objectMapper.writeValueAsString(downloadFile);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            log.info("json:"+json);
        }
        return fileList;
    }

    /**
     * 导出文件路径
     * @param paramMap
     * @return
     */
    public String allDataDownload(HashMap<String,String> paramMap){
        String userId = paramMap.get("userId");
        String subjectCode = paramMap.get("specialId");//011
        String date = paramMap.get("date");
        String fileId = UUID.randomUUID().toString();
        String subjectName = new String();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = simpleDateFormat.format(new Date());
        String exportFilePath = new String();
        try {
            exportFilePath = generateExcel(subjectCode,date,fileId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        subjectName = downloadFileMapper.getSubjectNameByCode(subjectCode);

        DownloadFile downloadFile = new DownloadFile(fileId,exportFilePath,date,subjectName,
                DIMENSION_NAME_STR,currentTime,String.valueOf(1),userId);
        downloadFileMapper.addFile(downloadFile);
        return exportFilePath;
    }

    public String downloadMaxDate(HashMap<String,String> paramMap){
        String dateType = paramMap.get("dateType");
        String markType = paramMap.get("markType");
        String maxDate = new String();
        maxDate = downloadFileMapper.getMaxDate(dateType,markType);
        return maxDate;

    }


    /**
     * 计算环比
     * @param M_TM_VAL
     * @param M_LM_VAL
     * @return
     */
    private static String calculateMoM(String M_TM_VAL,String M_LM_VAL){
        DecimalFormat df = new DecimalFormat("######0.00");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        String result = new String();
        if (M_TM_VAL.equals("0")||M_LM_VAL.equals("0")||M_TM_VAL.equals("")||M_LM_VAL.equals("")||M_LM_VAL.equals("-")||M_TM_VAL.equals("-")){
            return "-";
        }else {
            Double mtmVal = Double.parseDouble(M_TM_VAL);
            Double mlmVal = Double.parseDouble(M_LM_VAL);
            result = df.format((mtmVal-mlmVal)*100/Math.abs(mlmVal));
            return result+"%";
        }
    }

    /**
     * 计算同比
     * @param M_TM_VAL
     * @param M_LY_VAL
     * @return
     */
    private static String calculateYoY(String M_TM_VAL,String M_LY_VAL){
        DecimalFormat df = new DecimalFormat("######0.00");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        String result = new String();
        if (M_TM_VAL.equals("0")||M_LY_VAL.equals("0")||M_TM_VAL.equals("")||M_LY_VAL.equals("")||M_LY_VAL.equals("-")||M_TM_VAL.equals("-")){
            return "-";
        }else {
            Double mtmVal = Double.parseDouble(M_TM_VAL);
            Double mlyVal = Double.parseDouble(M_LY_VAL);
            result = df.format((mtmVal-mlyVal)*100/Math.abs(mlyVal));
            return result+"%";
        }
    }

    /**
     * 递归实现dimValue中的笛卡尔积，结果放在result中
     *
     * @param dimValue 原始数据
     * @param result   结果数据
     * @param layer    dimValue的层数
     * @param curList  每次笛卡尔积的结果
     */
    public static void recursive(List<List<String>> dimValue, List<List<String>> result, int layer, List<String> curList) {
        if (layer < dimValue.size() - 1) {
            if (dimValue.get(layer).size() == 0) {
                recursive(dimValue, result, layer + 1, curList);
            } else {
                for (int i = 0; i < dimValue.get(layer).size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimValue.get(layer).get(i));
                    recursive(dimValue, result, layer + 1, list);
                }
            }
        } else if (layer == dimValue.size() - 1) {
            if (dimValue.get(layer).size() == 0) {
                result.add(curList);
            } else {
                for (int i = 0; i < dimValue.get(layer).size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimValue.get(layer).get(i));
                    result.add(list);
                }
            }
        }
    }

    /**
     * 对List<Map<String,Object>进行排序
     * @param paramList 待排序List<Map<String,Object>
     * @param compareParam  比较依据
     * @return
     */
    public static List<HashMap<String,String>> sortList(List<HashMap<String,String>> paramList,String compareParam) {

        Collections.sort(paramList, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                String o1Code = o1.get(compareParam);
                String o2Code = o2.get(compareParam);
                return o1Code.compareTo(o2Code);
            }
        });
        return paramList;
    }

    /**
     * 规范指标数值，包括单位、保留位数、是否除以10000等
     * @param kpiInfoMap
     * @param value
     * @return
     */
    public static String formatKpiValue(Map<String, Object> kpiInfoMap, String value) {
        if (value.equals("0")||value.equals("0")||value.equals("")||value.equals("")){
            return "-";
        }
        Double uatio = 1.0;
        String unit = "";
        String format = "";
        if (null != kpiInfoMap.get("UATIO")) {
            //uatio为指标除数，一般为1或者10000（万元、万户类）
            uatio = ((BigDecimal) kpiInfoMap.get("UATIO")).doubleValue();
        }
        if (null != kpiInfoMap.get("FORMAT")) {
            //指标小数点后保留位数
            format = (String) kpiInfoMap.get("FORMAT");
        }
        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
        String result = "";
        if (null != value) {
            Double doubleValue = Double.parseDouble(value);
            if (doubleValue != 0) {
                if (!StringUtils.isEmpty(format)) {
                    df = new DecimalFormat(format);
                    df.setRoundingMode(RoundingMode.HALF_EVEN);
                }
                //下载全部的excel中不需要除以10000，展示基础单位即可，与指标名称后单位保持一致
                result = df.format(doubleValue);
            }
        }
        return result;
    }
}
