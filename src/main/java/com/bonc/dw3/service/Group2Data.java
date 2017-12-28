package com.bonc.dw3.service;


import com.bonc.dw3.entity.DayTableUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by chou on 2017/11/22.
 */
public class Group2Data {

    private final Logger log = LoggerFactory.getLogger(Group2Data.class);

    List<String> lines;
    public Group2Data(String filePath ){
        readFile(filePath);

    }
    /**
     *
     * @param filePath
     * @throws IOException
     */
    private void readFile(String filePath)  {
        long time=System.currentTimeMillis();

        try {
            lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("读取文件，耗时："+(System.currentTimeMillis()-time));
    }

    /**
     * group sum
     * @param code_List
     * @return result list
     * @throws IOException
     */
    public  List<String> groupbySth (List <String> code_List) throws IOException {
        long time=System.currentTimeMillis();
        List <DayTableUntil> datalist=new ArrayList<>();
        for (String line : lines)
        {
            String kpi_code=line.split("\\|",-1)[9];

            if(code_List.contains(kpi_code))
            {
                DayTableUntil dayTableUntil = new DayTableUntil();
                dayTableUntil.parse(line);
                datalist.add(dayTableUntil);
            }

        }
        Map <Object,Map<Object,List<DayTableUntil>>> groupBy2 = datalist.stream()
                .collect( Collectors.groupingBy(DayTableUntil::getKPI_CODE,Collectors.groupingBy(DayTableUntil::getP_A_C)));

        log.info("groupBy，耗时："+(System.currentTimeMillis()-time));

        List <String> resultList=new ArrayList<>();
        groupBy2.forEach((key,value)->{
            log.info(key.toString());
            value.forEach((key2,value2)->{
//                System.out.println(key2+":"+value2.size());
//               System.out.println(value2);
                //value2不是DayTableUntil对象，而是LIST对象
                HashMap<String, DayTableUntil> dataHm=new HashMap<>();
                Iterator it=value2.iterator();


                while (it.hasNext())
                {
                    DayTableUntil oneDayValue= (DayTableUntil) it.next();
                    StringBuilder keyhm=new StringBuilder (oneDayValue.getSERVICE_TYPE()+":"+oneDayValue.getCHANNEL_TYPE()+":"+oneDayValue.getPRODUCT_ID());
                    dataHm.put(keyhm.toString(),oneDayValue);
                }

                try {
                    resultList.addAll(Sumfor999.excutefor999(dataHm));
                    //Sumfor999.excute999(dataHm,"./result.txt");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        });//遍历结束
        log.info("整体时间："+(System.currentTimeMillis()-time));
        log.info("======================结束======================");
        return resultList;
    }
//    public static void main(String[] args) throws IOException {
//        long time=System.currentTimeMillis();
////        HashMap<String,DayTableUntil> dataHm=new HashMap<>();
//        List <DayTableUntil> datalist=new ArrayList<>();
//        // List<String> lines = Files.readAllLines(Paths.get("C:\\Users\\chou\\Desktop\\压缩包\\20170918.txt"), StandardCharsets.UTF_8);
//        List<String> lines = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8);
////        List<String> lines = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8);
//        for (String line : lines)
//        {
//            DayTableUntil dayTableUntil = new DayTableUntil();
//            dayTableUntil.parse(line);
//            datalist.add(dayTableUntil);
//        }
//
//        System.out.println("读取文件，耗时："+(System.currentTimeMillis()-time));
//        Map <Object,Map<Object,List<DayTableUntil>>> groupBy2 = datalist.stream()
//                .collect( Collectors.groupingBy(DayTableUntil::getKPI_CODE,Collectors.groupingBy(DayTableUntil::getP_A_C)));
////         Map <Object,List<DayTableUntil>> rmp=datalist.stream()
////                 .collect(Collectors.groupingBy(DayTableUntil::getP_A_C));
//
//        System.out.println("groupBy，耗时："+(System.currentTimeMillis()-time));
////        System.out.println(rmp);
//
//        groupBy2.forEach((key,value)->{
//            System.out.println(key);
//            value.forEach((key2,value2)->{
////                System.out.println(key2+":"+value2.size());
////               System.out.println(value2);
//                //value2不是DayTableUntil对象，而是LIST对象
//                HashMap<String, DayTableUntil> dataHm=new HashMap<>();
//                Iterator it=value2.iterator();
//
//
//                while (it.hasNext())
//                {
//                    DayTableUntil oneDayValue= (DayTableUntil) it.next();
//                    StringBuilder keyhm=new StringBuilder (oneDayValue.getSERVICE_TYPE()+":"+oneDayValue.getCHANNEL_TYPE()+":"+oneDayValue.getPRODUCT_ID());
//                    dataHm.put(keyhm.toString(),oneDayValue);
//                }
//
//                try {
//                    Sumfor999.excutefor999(dataHm);
//                    //Sumfor999.excute999(dataHm,"./result.txt");
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            });
//        });//遍历结束
//        System.out.println("整体时间："+(System.currentTimeMillis()-time));
//        System.out.println("======================结束======================");
//    }
    public static void main(String[] args) throws IOException {

        Group2Data gd=new Group2Data("C:\\Users\\chou\\Desktop\\压缩包\\20170918.txt");
        List <String> code_List=new ArrayList<>();
        code_List.add("CKP_24463");
        code_List.add("CKP_24453");
        code_List.add("CKP_23373");
        code_List.add("CKP_05806");
        code_List.add("CKP_05986");
        List<String> res=gd.groupbySth(code_List);
}
}
