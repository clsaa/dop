package com.clsaa.dop.server.code.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author wsy
 */
public class TimeUtil {

    private final static long YEAR = 1000 * 60 * 60 * 24 * 365L;
    private final static long MONTH = 1000 * 60 * 60 * 24 * 30L;
    private final static long DAY = 1000 * 60 * 60 * 24L;
    private final static long HOUR = 1000 * 60 * 60L;
    private final static long MINUTE = 1000 * 60L;

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static void main(String[] args) {

        String lastTime = "2019-02-26T07:57:50.000Z";
        List<String> res=natureTime(lastTime);
        System.out.println(res.get(0));
        System.out.println(res.get(1));

    }

    /**
     * 将gitlab返回的时间(例如2019-02-26T07:57:50.000Z)加上8小时的时差转换成距现在的时间
     * @param lastTime gitlab返回的时间
     * @return 加上8小时的时差后的时间以及距现在的时间
     */
    public static List<String> natureTime(String lastTime) {

        if(lastTime.contains("+")){
            lastTime=lastTime.substring(0, lastTime.indexOf("+")) + "Z";
        }

        LocalDateTime dt_commit=LocalDateTime.parse(lastTime,dtf);
        Long ts_commit=dt_commit.toInstant(ZoneOffset.of("+0")).toEpochMilli();
        String commit_time=ts_commit.toString();
//        dt_commit=dt_commit.plusHours(8);
//
//        //计算距现在的时长
//        LocalDateTime dt_now=LocalDateTime.now();
//        long between = dt_now.toInstant(ZoneOffset.UTC).toEpochMilli() - dt_commit.toInstant(ZoneOffset.UTC).toEpochMilli();
//        String commit_time;
//        if (between > YEAR) {
//            commit_time = ((between - YEAR) / YEAR + 1) + "年前";
//        }else if (between > MONTH) {
//            commit_time = ((between - MONTH) / MONTH + 1) + "月前";
//        }else if (between > DAY) {
//            commit_time = ((between - DAY) / DAY + 1) + "天前";
//        }else if (between > HOUR) {
//            commit_time = ((between - HOUR) / HOUR + 1) + "小时前";
//        }else if (between > MINUTE) {
//            commit_time = ((between - MINUTE) / MINUTE + 1) + "分钟前";
//        }else {
//            commit_time = "刚刚";
//        }

        List<String> res = new ArrayList<>();
        String commit_date=dt_commit.toString();
        res.add(commit_date);
        res.add(commit_time);

        return res;
    }

//    public static void main(String[] args) {
//        List<String> res= TimeUtil.natureTime("2019-03-20T13:03:46.000Z");
//        System.out.println(res.get(0));
//        System.out.println(res.get(1));
//
//    }
}
