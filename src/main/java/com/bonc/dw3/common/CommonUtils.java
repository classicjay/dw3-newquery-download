package com.bonc.dw3.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 公共工具类
 */
public final class CommonUtils {
    /**
     * 工具类，不允许new对象
     */
    private CommonUtils() {
    }

    /**
     * 判定object对象为空
     *
     * @param object 对象
     * @return 判断结果
     */
    public static boolean isBlank(final Object object) {
        return null == object || "".equals(object);
    }

    /**
     * 判定object对象不为空
     *
     * @param object 对象
     * @return 判断结果
     */
    public static boolean isNotBlank(final Object object) {
        return null != object && !"".equals(object);
    }

    /**
     * 判定map中key对应的value为空，则赋给缺省值
     *
     * @param map   map对象
     * @param key   map对象的key
     * @param value map对象的value
     */
    public static void defaultValueOfMap(final Map map, final Object key, final Object value) {
        if (isBlank(map.get(key))) {
            map.put(key, value);
        }
    }

    /**
     * 判断字符串是否是double类型
     *
     * @param sContentValue 待校验的字符串
     * @return 结果
     */
    public static boolean isDoubleValue(String sContentValue) {
        try {
            Double dCheckValue = Double.parseDouble(sContentValue);
            return dCheckValue instanceof Double;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 处理净增指标
     *
     * @param paramMap     参数map对象
     * @param dataList 数据map对象
     * @param keyOfMap     数据map对象中的key
     * @return 处理结果
     */
    public static List<Map<String, Object>> convertMinusKpiData(Map<String, Object> paramMap, List<Map<String, Object>> dataList,
                                                                String keyOfMap) {
        //非净增指标直接返回
        if ("0".equals(paramMap.get("isMinus"))) {
            return dataList;
        }
        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
        df.setRoundingMode(RoundingMode.UP);
        double totalValue = 0.0d;
        //计算总数
        for (Map<String, Object> map : dataList) {
            Object value = map.get(keyOfMap);
            if (isDoubleValue(value.toString())) {
                totalValue += Math.abs(Double.parseDouble(value.toString()));
            }
        }
        //计算百分比
        if (0 < Double.compare(totalValue, 0.0d)) {
            for (Map<String, Object> map : dataList) {
                Object value = map.get(keyOfMap);
                if (isDoubleValue(value.toString())) {
                    map.put(keyOfMap, new BigDecimal(df.format(Double.parseDouble(value.toString()) / totalValue  * 100)));
                }
            }
        }
        return dataList;
    }

}
