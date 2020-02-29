package com.clsaa.dop.server.pipline.util;

import java.sql.Timestamp;

/**
 * @author 任贵杰
 * @version v1
 * @summary Timestamp工具类
 * @since 2018/4/29
 */
public class TimestampUtil {
    private TimestampUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * 生成当前Timestamp
     */
    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }
}
