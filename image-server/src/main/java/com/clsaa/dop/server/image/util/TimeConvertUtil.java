package com.clsaa.dop.server.image.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 将harbor的国际标准时间转化为北京时间
 * @author xzt
 * @since 2019-4-9
 */
public class TimeConvertUtil {
    public static String convertTime(String UTCTime){
        String utcFormat = "yyyy-MM-dd'T'HH:mm:ss";
        String localFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat UTCDateFormat = new SimpleDateFormat(utcFormat);
        SimpleDateFormat localDateFormat = new SimpleDateFormat(localFormat);
        UTCDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = UTCDateFormat.parse(UTCTime);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("时间转化失败");
        }
        localDateFormat.setTimeZone(TimeZone.getDefault());
        return localDateFormat.format(date.getTime());
    }

}
