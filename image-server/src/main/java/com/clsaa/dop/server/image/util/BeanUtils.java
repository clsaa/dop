package com.clsaa.dop.server.image.util;


import java.util.ArrayList;
import java.util.List;

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
    /**
     * List类之间的类型转化
     */
    public static<T, R> List<R> convertList(List<T> sourceList, Class<R> targetClass){
        if (sourceList.size()==0||sourceList==null){
            return null;
        }
        List<R> resultList = new ArrayList<>();
        for (T t:sourceList){
            R result = convertType(t,targetClass);
            resultList.add(result);
        }
        return resultList;
    }

}
