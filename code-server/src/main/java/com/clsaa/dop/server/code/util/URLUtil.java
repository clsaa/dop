package com.clsaa.dop.server.code.util;

import org.apache.http.client.utils.URLEncodedUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author wsy
 */
public class URLUtil {

    /**
     * 与js中encodeURIComponent功能相同
     *
     * @param s 任意字符串
     * @return 编码后字符串
     */
    public static String encodeURIComponent(String s) {

        String result = null;

        try {
            result = URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            result = s;
        }

        return result;

    }

    /**
     * 对query string 的参数进行编码
     *
     * @param s get或delete请求的完整url
     * @return 参数编码后的url
     */
    public static String encodeURI(String s) {

        String[] strs = s.split("\\?");

        String result = null;

        try {
            result = URLEncoder.encode(strs[1], "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("%3D", "=")
                    .replaceAll("%26", "&");
        } catch (UnsupportedEncodingException e) {
            result = s;
        }

        return strs[0] + "?" + result;
    }

    public static void main(String[] args) {
        String url = "http://localhost:8888/code-server/projects/Wsy123/swaggg/repository/blob?file_path=.DS_Store&branch=master&commit_message=删除 store 111 111.fd&userId=20";
        String component = "file_path=.DS_Store&branch=master&commit_message=删除 store 111 111.fd&userId=20";
        System.out.println(encodeURIComponent(component));
        System.out.println(encodeURI(url));
    }
}
