package com.clsaa.dop.server.pipline.util;


/**
 * @author 任贵杰
 * @version v1
 * @summary Bean工具类
 * @since 2018-09-01
 */
public class BeanUtils {
    private BeanUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 从类型转换
     */
    public static <T, R> R convertType(T source, Class<R> targetClass) {
        if (source == null) {
            return null;
        }
        final R result;
        try {
            result = targetClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        org.springframework.beans.BeanUtils.copyProperties(source, result);
        return result;
    }

}
