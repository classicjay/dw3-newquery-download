package com.bonc.dw3.service;

import com.bonc.dw3.mapper.DownloadMapper;
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
import java.io.*;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
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

    private static final String KPI_CODE_FIELD = "KPI_CODE";
    private static final String KPI_FULL_NAME_FIELD = "KPI_FULL_NAME";

    private static final String AREA_ID_FIELD = "AREA_ID";
    private static final String AREA_SHORT_DESC_FIELD = "AREA_SHORT_DESC";
    private static final String PROV_ID_FIELD = "PROV_ID";
    private static final String PRO_NAME_FIELD = "PRO_NAME";

    private static String TEMPLATE_FILE_PATH;
    private static String DATA_FILE_PATH;
    private static String EXPORT_PATH;

    private static final String KPI_VALUE_NAME = "当日值";
    private static final String M_TM_VALUE_NAME = "本月累计";
    private static final String YOY_NAME = "累计同比";
    private static final String MOM_NAME = "累计环比";

    /**
     * 初始化执行方法
     */
    @PostConstruct
    public void init(){
        TEMPLATE_FILE_PATH = environment.getProperty("template.file.path");
        DATA_FILE_PATH = environment.getProperty("data.file.path");
        EXPORT_PATH = environment.getProperty("export.file.path");

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

    public void generateExcel(HashMap<String,String> param) throws IOException {

        String moduleId = param.get("MODULE_CODE");
        String date = param.get("date");
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
        //笛卡尔积运算的元素
        List<List<String>> dimValue = new ArrayList<List<String>>();
        //笛卡尔积远算结果
        List<List<String>> recursiveResult = new ArrayList<List<String>>();

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
        String kpiCode = new String();
        //当前指标对应的列数
        int colNum = 0;
        //每新增一个指标，列数都要+4，即一个指标占4列（当日，本月，同比，环比）
        int count = 0;
        //读取数据文件
        List<String> lines = Files.readAllLines(Paths.get(DATA_FILE_PATH), StandardCharsets.UTF_8);
        for (String line:lines){
            String items[] = line.split("\\|",-1);
            String drz = items[10];
            String bylj = items[12];
            String sylj = items[13];
            String qntq = items[15];
            //指标名行
            XSSFRow headRow = sheet.getRow(0);
            //字段名行
            XSSFRow fieldRow = sheet.getRow(1);
            //当前数据中的kpiCode
            String currentKpi = items[9];
            //当前currentKpi与kpiCode不匹配
            if (!kpiCode.equals(currentKpi)){
                //定位到列数
                colNum =  6 + count*4;
                //显示指标名称的区域合并并设置居中
                CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, colNum, colNum+3);
                sheet.addMergedRegion(cellRangeAddress);

                XSSFCell kpiNameCell = headRow.createCell(colNum);
                if(null == KPI_CODE_MAP.get(currentKpi) || "".equals(KPI_CODE_MAP.get(currentKpi))){
                    log.info("kpiCode："+currentKpi+"在码表中查不到");
                }
                kpiNameCell.setCellValue(KPI_CODE_MAP.get(currentKpi));
                //指标名称单元格设置居中
                kpiNameCell.setCellStyle(styleCenter);
                fieldRow.createCell(colNum).setCellValue(KPI_VALUE_NAME);
                fieldRow.createCell(colNum+1).setCellValue(M_TM_VALUE_NAME);
                fieldRow.createCell(colNum+2).setCellValue(MOM_NAME);
                fieldRow.createCell(colNum+3).setCellValue(YOY_NAME);
                count++;
                kpiCode = currentKpi;
                log.info("kpiCode:"+kpiCode);
            }
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
            //TODO 此处指标数据的单位以及格式（保留几位）需要码表，码表从库里拿
            //TODO 账期作为参数
            //TODO 文件路径
            //定位到行数
            String rowNum = combineMap.get(stringBuffer.toString());
            if (null == rowNum || "".equals(rowNum)){
                //数据中字符拼接在维度组合映射中找不到的情况
                log.info("未找到匹配的数据combine："+stringBuffer.toString());
                log.info("rowNum："+rowNum);
            }else {
                //填入数据
                XSSFRow row = sheet.getRow(Integer.parseInt(rowNum));
                row.createCell(colNum).setCellValue(drz);
                row.createCell(colNum+1).setCellValue(bylj);
//                row.createCell(colNum+2).setCellValue(sylj);
//                row.createCell(colNum+3).setCellValue(qntq);
                row.createCell(colNum+2).setCellValue(calculateMoM(bylj,sylj));
                row.createCell(colNum+3).setCellValue(calculateYoY(bylj,qntq));
            }
        }

        //导出数据
        FileOutputStream out = new FileOutputStream(EXPORT_PATH);
        wb.write(out);
        out.close();
        in.close();
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
        if (M_TM_VAL.equals("0")||M_LM_VAL.equals("0")||M_TM_VAL.equals("")||M_LM_VAL.equals("")){
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
        if (M_TM_VAL.equals("0")||M_LY_VAL.equals("0")||M_TM_VAL.equals("")||M_LY_VAL.equals("")){
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
}
