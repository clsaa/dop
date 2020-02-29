package com.clsaa.dop.server.user.util.crypt;

/**
 * 各种加解密算法结果model，通过Status判断加解密是否成功，text记录加密或者解密后的内容
 *
 * @author 任贵杰
 * @version v1
 * @summary 各种加解密算法结果model
 * @since 2018-12-29
 */
public class CryptoResult {

    /**
     * 加解密结果状态
     */
    private final Status status;
    /**
     * 加解密结果内容
     */
    private String content;

    /**
     * 构造函数，传入enum
     */
    public CryptoResult(Status status) {
        this.status = status;
    }

    /**
     * 构造函数，传入enum和加解密后的内容
     */
    public CryptoResult(Status status, String content) {
        this.status = status;
        this.content = content;
    }


    /**
     * 加解密后的状态
     *
     * @return status  加解密后的状态
     */
    public Status getStatus() {
        return status;
    }

    /**
     * 加解密后的内容
     *
     * @return content  加解密后的内容
     */
    public String getContent() {
        return content;
    }


    /**
     * 判断加解密结果是否有效
     *
     * @return boolean
     */
    public boolean isOK() {
        return status == Status.OK;
    }

    /**
     * 记录加解密结果的状态enum
     */
    public enum Status {
        //加解密成功
        OK,
        //加解密失败,非法算法
        NoSuchAlgorithmException,
        //加解密失败,非法密匙
        InvalidKeyException,
        //加解密失败,其他异常
        Exception
    }

}
