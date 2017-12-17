package com.bonc.dw3.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <p>Title: BONC -  DownloadMapper</p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright BONC(c) 2013 - 2025 </p>
 * <p>Company: 北京东方国信科技股份有限公司 </p>
 *
 * @author zhaojie
 * @version 1.0.0
 */
@Mapper
public interface DownloadMapper {
    /**
     * 获取指标编码名称映射
     * @return
     */
    public List<HashMap<String,String>> getKpiMapping();

    /**
     * 获取省份编码名称映射
     * @return
     */
    public List<HashMap<String,String>> getProvCode();

    /**
     *获取地市编码&省份编码-地市名称&省份名称映射
     * @return
     */
    public List<HashMap<String,String>> getAreaProvCode();

    /**
     * 指标信息
     * @param moduleId
     * @return
     */
    public List<HashMap<String,String>> getKpiInfo(@Param(value = "moduleId") String moduleId);
}
