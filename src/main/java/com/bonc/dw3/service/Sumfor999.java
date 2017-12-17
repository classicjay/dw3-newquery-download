package com.bonc.dw3.service;


import com.bonc.dw3.entity.DayTableUntil;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


/**
 * Created by chou on 2017/11/20.
 */
public class Sumfor999 {


    public static void excute999(HashMap<String, DayTableUntil> dataHm, String path) throws IOException {

        long time=System.currentTimeMillis();

       HashMap <String,DayTableUntil> result999=new HashMap<>();
        DayTableUntil sumRes999;

        DayTableUntil demo=new DayTableUntil();
        //开始计算999|999|999从dataHm取值
        Iterator it= dataHm.entrySet().iterator();

        if (dataHm.isEmpty()){
            return;
        }
        //先随意拿一个做模版
        Map.Entry m1 = (Map.Entry) it.next();
        DayTableUntil value1= (DayTableUntil) m1.getValue();
        demo.setMONTH_ID(value1.getMONTH_ID());
        demo.setDAY_ID(value1.getDAY_ID());
        demo.setACCT_DATE(value1.getACCT_DATE());
        demo.setPROV_ID(value1.getPROV_ID());
        demo.setAREA_NO(value1.getAREA_NO());
        demo.setCITY_NO(value1.getCITY_NO());
        demo.setKPI_CODE(value1.getKPI_CODE());

        demo.setKPI_VALUE(new BigDecimal("0"));
        demo.setD_LD_VALUE(new BigDecimal("0"));
        demo.setM_TM_VALUE(new BigDecimal("0"));
        demo.setD_LM_VALUE(new BigDecimal("0"));
        demo.setM_LM_VALUE(new BigDecimal("0"));
        demo.setM_LY_VALUE(new BigDecimal("0"));
        demo.setM_DA_VALUE(new BigDecimal("0"));
        demo.setM_LDA_VALUE(new BigDecimal("0"));
        //如果没有只有一个，怎么办？
        sumRes999=demo;

        //从第二个开始轮询?

        Iterator it2= dataHm.entrySet().iterator();
        while (it2.hasNext())
        {
            Map.Entry m = (Map.Entry) it2.next();
            String key= (String) m.getKey();
            DayTableUntil value= (DayTableUntil) m.getValue();

            sumRes999 = sumRes999.add(value);
        }

        dataHm.put("999:999:999",sumRes999);

        BufferedOutputStream buff = new BufferedOutputStream(new FileOutputStream(path,true));
        Iterator data_it=dataHm.entrySet().iterator();
        while (data_it.hasNext())
        {
            Map.Entry m = (Map.Entry) data_it.next();
            String key= (String) m.getKey();
            DayTableUntil value= (DayTableUntil) m.getValue();
          //  201709|18|20170918|091|V0210500|-1|999|999|999|CKP_24103|0|0|0|0|0|0|0|0

            String KPI_VALUE  =value.getKPI_VALUE()==null?"":value.getKPI_VALUE().stripTrailingZeros().toPlainString();
            String D_LD_VALUE =value.getD_LD_VALUE()==null?"":value.getD_LD_VALUE().stripTrailingZeros().toPlainString();
            String M_TM_VALUE =value.getM_TM_VALUE()==null?"":value.getM_TM_VALUE().stripTrailingZeros().toPlainString();
            String D_LM_VALUE =value.getD_LM_VALUE()==null?"":value.getD_LM_VALUE().stripTrailingZeros().toPlainString();
            String M_LM_VALUE =value.getM_LM_VALUE()==null?"":value.getM_LM_VALUE().stripTrailingZeros().toPlainString();
            String M_LY_VALUE =value.getM_LY_VALUE()==null?"":value.getM_LY_VALUE().stripTrailingZeros().toPlainString();
            String M_DA_VALUE =value.getM_DA_VALUE()==null?"":value.getM_DA_VALUE().stripTrailingZeros().toPlainString();
            String M_LDA_VALUE=value.getM_LDA_VALUE()==null?"":value.getM_LDA_VALUE().stripTrailingZeros().toPlainString();
            buff.write((value.getMONTH_ID()+"|"+value.getDAY_ID()+"|"+value.getACCT_DATE()+"|"+value.getPROV_ID()+"|"+value.getAREA_NO()+"|"+value.getCITY_NO()+"|"
                    +key.split(":",-1)[0]+"|"+key.split(":",-1)[1]+"|"+key.split(":",-1)[2]+"|"+value.getKPI_CODE()+"|"+
                    KPI_VALUE+"|"+
                    D_LD_VALUE+"|"+
                    M_TM_VALUE+"|"+
                    D_LM_VALUE+"|"+
                    M_LM_VALUE+"|"+
                    M_LY_VALUE+"|"+
                    M_DA_VALUE+"|"+
                    M_LDA_VALUE
                    +"\n").getBytes());
        }
        buff.close();
          // System.out.println("写完成！");

   }
    public static List<String> excutefor999(HashMap<String, DayTableUntil> dataHm) throws IOException {

        long time=System.currentTimeMillis();

        HashMap <String,DayTableUntil> result999=new HashMap<>();
        DayTableUntil sumRes999;

        DayTableUntil demo=new DayTableUntil();
        //开始计算999|999|999从dataHm取值
        Iterator it= dataHm.entrySet().iterator();

        if (dataHm.isEmpty()){
            return null;
        }
        //先随意拿一个做模版
        Map.Entry m1 = (Map.Entry) it.next();
        DayTableUntil value1= (DayTableUntil) m1.getValue();
        demo.setMONTH_ID(value1.getMONTH_ID());
        demo.setDAY_ID(value1.getDAY_ID());
        demo.setACCT_DATE(value1.getACCT_DATE());
        demo.setPROV_ID(value1.getPROV_ID());
        demo.setAREA_NO(value1.getAREA_NO());
        demo.setCITY_NO(value1.getCITY_NO());
        demo.setKPI_CODE(value1.getKPI_CODE());

        demo.setKPI_VALUE(new BigDecimal("0"));
        demo.setD_LD_VALUE(new BigDecimal("0"));
        demo.setM_TM_VALUE(new BigDecimal("0"));
        demo.setD_LM_VALUE(new BigDecimal("0"));
        demo.setM_LM_VALUE(new BigDecimal("0"));
        demo.setM_LY_VALUE(new BigDecimal("0"));
        demo.setM_DA_VALUE(new BigDecimal("0"));
        demo.setM_LDA_VALUE(new BigDecimal("0"));
        //如果没有只有一个，怎么办？
        sumRes999=demo;

        //从第二个开始轮询?

        Iterator it2= dataHm.entrySet().iterator();
        while (it2.hasNext())
        {
            Map.Entry m = (Map.Entry) it2.next();
            String key= (String) m.getKey();
            DayTableUntil value= (DayTableUntil) m.getValue();

            sumRes999 = sumRes999.add(value);
        }

        dataHm.put("999:999:999",sumRes999);

        List<String> resultList=new ArrayList<>();
        Iterator data_it=dataHm.entrySet().iterator();

        while (data_it.hasNext())
        {
            Map.Entry m = (Map.Entry) data_it.next();
            String key= (String) m.getKey();
            DayTableUntil value= (DayTableUntil) m.getValue();
            //  201709|18|20170918|091|V0210500|-1|999|999|999|CKP_24103|0|0|0|0|0|0|0|0

            String KPI_VALUE  =value.getKPI_VALUE()==null?"":value.getKPI_VALUE().stripTrailingZeros().toPlainString();
            String D_LD_VALUE =value.getD_LD_VALUE()==null?"":value.getD_LD_VALUE().stripTrailingZeros().toPlainString();
            String M_TM_VALUE =value.getM_TM_VALUE()==null?"":value.getM_TM_VALUE().stripTrailingZeros().toPlainString();
            String D_LM_VALUE =value.getD_LM_VALUE()==null?"":value.getD_LM_VALUE().stripTrailingZeros().toPlainString();
            String M_LM_VALUE =value.getM_LM_VALUE()==null?"":value.getM_LM_VALUE().stripTrailingZeros().toPlainString();
            String M_LY_VALUE =value.getM_LY_VALUE()==null?"":value.getM_LY_VALUE().stripTrailingZeros().toPlainString();
            String M_DA_VALUE =value.getM_DA_VALUE()==null?"":value.getM_DA_VALUE().stripTrailingZeros().toPlainString();
            String M_LDA_VALUE=value.getM_LDA_VALUE()==null?"":value.getM_LDA_VALUE().stripTrailingZeros().toPlainString();
            resultList.add(value.getMONTH_ID()+"|"+value.getDAY_ID()+"|"+value.getACCT_DATE()+"|"+value.getPROV_ID()+"|"+value.getAREA_NO()+"|"+value.getCITY_NO()+"|"
                    +key.split(":",-1)[0]+"|"+key.split(":",-1)[1]+"|"+key.split(":",-1)[2]+"|"+value.getKPI_CODE()+"|"+
                    KPI_VALUE+"|"+
                    D_LD_VALUE+"|"+
                    M_TM_VALUE+"|"+
                    D_LM_VALUE+"|"+
                    M_LM_VALUE+"|"+
                    M_LY_VALUE+"|"+
                    M_DA_VALUE+"|"+
                    M_LDA_VALUE
                    +"\n");
        }

        // System.out.println("写完成！");
        return resultList;
    }


}
