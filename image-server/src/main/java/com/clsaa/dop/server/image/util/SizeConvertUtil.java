package com.clsaa.dop.server.image.util;

/**
 * 将大小B转为其他单位的size
 * @author xzt
 * @since 2019-4-9
 */
public class SizeConvertUtil {
    /**
     * 将单位为B的size转化为对应单位的size
     * @param size harbor的大小
     * @return 转换之后的size大小
     */
    public static String convertSize(Integer size){
        int remainder = 0;
        if (size < 1024){
            return size+"B";
        }else {
            //剩余B
            remainder = ((size%1024)*100)/1024;
            size = size/1024;
            if (size<1024){
                return size+"."+remainder+"KB";
            }else {
                remainder = ((size%1024)*100)/1024;
                size =size/1024;
                if (size < 1024){
                    return size+"."+remainder+"MB";
                }else {
                    remainder = ((size%1024)*100)/1024;
                    size =size/1024;
                    return size+"."+remainder+"GB";
                }
            }
        }
    }
}
