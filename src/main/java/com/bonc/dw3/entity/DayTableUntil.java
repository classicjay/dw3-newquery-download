package com.bonc.dw3.entity;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by zhaoc on 2016/11/29.
 */
public class DayTableUntil implements Serializable {
    private String MONTH_ID;
    private String DAY_ID;
    private String ACCT_DATE;
    private String PROV_ID;
     String AREA_NO;
    private String CITY_NO;
    private String SERVICE_TYPE;
    private String CHANNEL_TYPE;
    private String PRODUCT_ID;
    private String KPI_CODE;
    private  String P_A_C;
    private BigDecimal KPI_VALUE;
    private BigDecimal D_LD_VALUE;
    private BigDecimal M_TM_VALUE;
    private BigDecimal D_LM_VALUE;
    private BigDecimal M_LM_VALUE;
    private BigDecimal M_LY_VALUE;
    private BigDecimal M_DA_VALUE;
    private BigDecimal M_LDA_VALUE;


    private void setP_A_C(){
        this.P_A_C=this.PROV_ID+"|"+this.AREA_NO+"|"+this.CITY_NO;

    }
    public  String getP_A_C(){
        return  this.P_A_C;
    }
    public String getMONTH_ID() {
        return MONTH_ID;
    }

    public void setMONTH_ID(String MONTH_ID) {
        this.MONTH_ID = MONTH_ID;
    }

    public String getDAY_ID() {
        return DAY_ID;
    }

    public void setDAY_ID(String DAY_ID) {
        this.DAY_ID = DAY_ID;
    }

    public String getACCT_DATE() {
        return ACCT_DATE;
    }

    public void setACCT_DATE(String ACCT_DATE) {
        this.ACCT_DATE = ACCT_DATE;
    }

    public String getPROV_ID() {
        return PROV_ID;
    }

    public void setPROV_ID(String PROV_ID) {
        this.PROV_ID = PROV_ID;
    }

    public String getAREA_NO() {
        return AREA_NO;
    }

    public void setAREA_NO(String AREA_NO) {
        this.AREA_NO = AREA_NO;
    }

    public String getCITY_NO() {
        return CITY_NO;
    }

    public void setCITY_NO(String CITY_NO) {
        this.CITY_NO = CITY_NO;
    }

    public String getSERVICE_TYPE() {
        return SERVICE_TYPE;
    }

    public void setSERVICE_TYPE(String SERVICE_TYPE) {
        this.SERVICE_TYPE = SERVICE_TYPE;
    }

    public String getCHANNEL_TYPE() {
        return CHANNEL_TYPE;
    }

    public void setCHANNEL_TYPE(String CHANNEL_TYPE) {
        this.CHANNEL_TYPE = CHANNEL_TYPE;
    }

    public String getPRODUCT_ID() {
        return PRODUCT_ID;
    }

    public void setPRODUCT_ID(String PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
    }

    public String getKPI_CODE() {
        return KPI_CODE;
    }

    public void setKPI_CODE(String KPI_CODE) {
        this.KPI_CODE = KPI_CODE;
    }

    public BigDecimal getKPI_VALUE() {
        return KPI_VALUE;
    }

    public void setKPI_VALUE(BigDecimal KPI_VALUE) {
        this.KPI_VALUE = KPI_VALUE;
    }

    public BigDecimal getD_LD_VALUE() {
        return D_LD_VALUE;
    }

    public void setD_LD_VALUE(BigDecimal d_LD_VALUE) {
        D_LD_VALUE = d_LD_VALUE;
    }

    public BigDecimal getM_TM_VALUE() {
        return M_TM_VALUE;
    }

    public void setM_TM_VALUE(BigDecimal m_TM_VALUE) {
        M_TM_VALUE = m_TM_VALUE;
    }

    public BigDecimal getD_LM_VALUE() {
        return D_LM_VALUE;
    }

    public void setD_LM_VALUE(BigDecimal d_LM_VALUE) {
        D_LM_VALUE = d_LM_VALUE;
    }

    public BigDecimal getM_LM_VALUE() {
        return M_LM_VALUE;
    }

    public void setM_LM_VALUE(BigDecimal m_LM_VALUE) {
        M_LM_VALUE = m_LM_VALUE;
    }

    public BigDecimal getM_LY_VALUE() {
        return M_LY_VALUE;
    }

    public void setM_LY_VALUE(BigDecimal m_LY_VALUE) {
        M_LY_VALUE = m_LY_VALUE;
    }

    public BigDecimal getM_DA_VALUE() {
        return M_DA_VALUE;
    }

    public void setM_DA_VALUE(BigDecimal m_DA_VALUE) {
        M_DA_VALUE = m_DA_VALUE;
    }

    public BigDecimal getM_LDA_VALUE() {
        return M_LDA_VALUE;
    }

    public void setM_LDA_VALUE(BigDecimal m_LDA_VALUE) {
        M_LDA_VALUE = m_LDA_VALUE;
    }
    //201709|19|20170919|019|V0142200|**|40AAAAAA|30AA|99|CKP_24083|19.0|35.0|2462.0|9.0|1924.0||129.578947368|101.263157895
    public void parse(String line) throws IOException {

            String[] strings = line.split("\\|", -1);
            setMONTH_ID(strings[0]);
            setDAY_ID(strings[1]);
            setACCT_DATE(strings[2]);
            setPROV_ID(strings[3]);
            setAREA_NO(strings[4]);
            setCITY_NO(strings[5]);
            setSERVICE_TYPE(strings[6]);
            setCHANNEL_TYPE(strings[7]);
            setPRODUCT_ID(strings[8]);
            setKPI_CODE(strings[9]);

            setKPI_VALUE (strings[10].equals("") ? null : new BigDecimal(strings[10].trim()));
            setD_LD_VALUE(strings[11].equals("") ? null : new BigDecimal(strings[11].trim()));
            setM_TM_VALUE(strings[12].equals("") ? null : new BigDecimal(strings[12].trim()));
            setD_LM_VALUE(strings[13].equals("") ? null : new BigDecimal(strings[13].trim()));
            setM_LM_VALUE(strings[14].equals("") ? null : new BigDecimal(strings[14].trim()));
            setM_LY_VALUE(strings[15].equals("") ? null : new BigDecimal(strings[15].trim()));
            setM_DA_VALUE(strings[16].equals("") ? null : new BigDecimal(strings[16].trim()));
            setM_LDA_VALUE(strings[17].equals("") ? null : new BigDecimal(strings[17].trim()));
            setP_A_C();
        }

    public DayTableUntil add(DayTableUntil addValue)
    {

        //stripTrailingZeros().toPlainString()写文件 转 string 记得加 去零跟非科学计数
        DayTableUntil result=new DayTableUntil();
        result.setDAY_ID(this.DAY_ID);
        result.setMONTH_ID(this.MONTH_ID);
        result.setACCT_DATE(this.ACCT_DATE);
        result.setPROV_ID(this.PROV_ID);
        result.setAREA_NO(this.AREA_NO);
        result.setCITY_NO(this.CITY_NO);
        //这3个不用，用了就错了
//        result.setSERVICE_TYPE(this.SERVICE_TYPE);
//        result.setCHANNEL_TYPE(this.CHANNEL_TYPE);
//        result.setPRODUCT_ID(this.PRODUCT_ID);
        result.setKPI_CODE(this.KPI_CODE);


        if (this.getKPI_VALUE()==null && addValue.getKPI_VALUE()==null){

            result.setKPI_VALUE(null);
        }else {
            if(addValue.getKPI_VALUE()==null) {
                result.setKPI_VALUE(this.getKPI_VALUE().add(new BigDecimal("0")));
            }
            else if(this.getKPI_VALUE()==null) {
                result.setKPI_VALUE(addValue.getKPI_VALUE().add(new BigDecimal("0")));
            }else {

                result.setKPI_VALUE(this.getKPI_VALUE().add(addValue.getKPI_VALUE()));
            }

        }
        //result.setD_LD_VALUE(this.getD_LD_VALUE().add(addValue.getD_LD_VALUE()));
        if (this.getD_LD_VALUE()==null&&addValue.getD_LD_VALUE()==null){
            result.setD_LD_VALUE(null);
        }else {
            if(addValue.getD_LD_VALUE()==null) {
                result.setD_LD_VALUE(this.getD_LD_VALUE().add(new BigDecimal("0")));
            }
            else if(this.getD_LD_VALUE()==null) {
                result.setD_LD_VALUE(addValue.getD_LD_VALUE().add(new BigDecimal("0")));
            }else {

                result.setD_LD_VALUE(this.getD_LD_VALUE().add(addValue.getD_LD_VALUE()));
            }

        }
        //result.setM_TM_VALUE(this.getM_TM_VALUE().add(addValue.getM_TM_VALUE()));
        if (this.getM_TM_VALUE()==null&&addValue.getM_TM_VALUE()==null){
            result.setM_TM_VALUE(null);
        }else {
            if(addValue.getM_TM_VALUE()==null) {
                result.setM_TM_VALUE(this.getM_TM_VALUE().add(new BigDecimal("0")));
            }
            else if(this.getM_TM_VALUE()==null) {
                result.setM_TM_VALUE(addValue.getM_TM_VALUE().add(new BigDecimal("0")));
            }else {

                result.setM_TM_VALUE(this.getM_TM_VALUE().add(addValue.getM_TM_VALUE()));
            }

        }
        // result.setD_LM_VALUE(this.getD_LM_VALUE().add(addValue.getD_LM_VALUE()));
        if (this.getD_LM_VALUE()==null&&addValue.getD_LM_VALUE()==null){
            result.setD_LM_VALUE(null);
        }else {
            if(addValue.getD_LM_VALUE()==null) {
                result.setD_LM_VALUE(this.getD_LM_VALUE().add(new BigDecimal("0")));
            }
            else if(this.getD_LM_VALUE()==null) {
                result.setD_LM_VALUE(addValue.getD_LM_VALUE().add(new BigDecimal("0")));
            }else {

                result.setD_LM_VALUE(this.getD_LM_VALUE().add(addValue.getD_LM_VALUE()));
            }

        }
        // result.setM_LM_VALUE(this.getM_LM_VALUE().add(addValue.getM_LM_VALUE()));
        if (this.getM_LM_VALUE()==null&&addValue.getM_LM_VALUE()==null){
            result.setM_LM_VALUE(null);
        }else {
            if(addValue.getM_LM_VALUE()==null) {
                result.setM_LM_VALUE(this.getM_LM_VALUE().add(new BigDecimal("0")));
            }
            else if(this.getM_LM_VALUE()==null) {
                result.setM_LM_VALUE(addValue.getM_LM_VALUE().add(new BigDecimal("0")));
            }else {

                result.setM_LM_VALUE(this.getM_LM_VALUE().add(addValue.getM_LM_VALUE()));
            }

        }
        // result.setM_LY_VALUE(this.getM_LY_VALUE().add(addValue.getM_LY_VALUE()));
        if (this.getM_LY_VALUE()==null&&addValue.getM_LY_VALUE()==null){
            result.setM_LY_VALUE(null);
        }else {
            if(addValue.getM_LY_VALUE()==null) {
                result.setM_LY_VALUE(this.getM_LY_VALUE().add(new BigDecimal("0")));
            }
            else if(this.getM_LY_VALUE()==null) {
                result.setM_LY_VALUE(addValue.getM_LY_VALUE().add(new BigDecimal("0")));
            }else {

                result.setM_LY_VALUE(this.getM_LY_VALUE().add(addValue.getM_LY_VALUE()));
            }

        }
        // result.setM_DA_VALUE(this.getM_DA_VALUE().add(addValue.getM_DA_VALUE()));
        if (this.getM_DA_VALUE()==null&&addValue.getM_DA_VALUE()==null){
            result.setM_DA_VALUE(null);
        }else {
            if(addValue.getM_DA_VALUE()==null) {
                result.setM_DA_VALUE(this.getM_DA_VALUE().add(new BigDecimal("0")));
            }
            else if(this.getM_DA_VALUE()==null) {
                result.setM_DA_VALUE(addValue.getM_DA_VALUE().add(new BigDecimal("0")));
            }else {

                result.setM_DA_VALUE(this.getM_DA_VALUE().add(addValue.getM_DA_VALUE()));
            }

        }
        //result.setM_LDA_VALUE(this.getM_LDA_VALUE().add(addValue.getM_LDA_VALUE()));
        if (this.getM_LDA_VALUE()==null&&addValue.getM_LDA_VALUE()==null){
            result.setM_LDA_VALUE(null);
        }else {
            if(addValue.getM_LDA_VALUE()==null) {
                result.setM_LDA_VALUE(this.getM_LDA_VALUE().add(new BigDecimal("0")));
            }
            else if(this.getM_LDA_VALUE()==null) {
                result.setM_LDA_VALUE(addValue.getM_LDA_VALUE().add(new BigDecimal("0")));
            }else {

                result.setM_LDA_VALUE(this.getM_LDA_VALUE().add(addValue.getM_LDA_VALUE()));
            }

        }
        return result;
    }
    public boolean valueIsAllNull()
    {
        if(this.getKPI_VALUE()==null&&
                this.getD_LD_VALUE()==null&&
                this.getM_TM_VALUE()==null&&
                this.getD_LM_VALUE()==null&&
                this.getM_LM_VALUE()==null&&
                this.getM_LY_VALUE()==null&&
                this.getM_DA_VALUE()==null&&
                this.getM_LDA_VALUE()==null) {
            return true;
        }
        else return false;
    }
}
