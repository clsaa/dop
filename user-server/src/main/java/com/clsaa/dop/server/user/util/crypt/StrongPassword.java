package com.clsaa.dop.server.user.util.crypt;

import com.google.common.io.BaseEncoding;

import java.util.Arrays;

/**
 * @author 任贵杰 812022339@qq.com
 */
public final class StrongPassword {
    /**
     * 加密算法
     */
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    /**
     * 盐的长度
     */
    private static final int SALT_SIZE = 128;

    /**
     * 生成密文的长度
     */
    private static final int HASH_SIZE = 256;

    /**
     * 迭代次数
     */
    private static final int PBKDF2_ITERATIONS = 1000;

    private StrongPassword() {
        throw new UnsupportedOperationException();
    }

    /**
     * PBKDF2摘要，返回BASE64URL编码的结果
     *
     * @param password 要加密的密码
     * @return {@link CryptoResult}
     */
    public static CryptoResult encrypt(String password) {
        return PBKDF2.hash(PBKDF2_ALGORITHM, password, Salt.newSalt(SALT_SIZE), PBKDF2_ITERATIONS, HASH_SIZE);
    }

    /**
     * 验证密码
     *
     * @param password       待验证的密码
     * @param storedPassword 数据库中的密码
     * @return 是否验证通过，true验证通过，false验证不通过
     */
    public static boolean verify(String password, String storedPassword) {
        byte[] byteResult = BaseEncoding.base64Url().decode(storedPassword);
        byte[] salt = Arrays.copyOfRange(byteResult, byteResult.length - SALT_SIZE, byteResult.length);
        return PBKDF2.hash(PBKDF2_ALGORITHM, password, salt, PBKDF2_ITERATIONS, HASH_SIZE).getContent().equals(storedPassword);
    }

    public static void main(String[] args) {
        String password = "RENguijie1996.";
        CryptoResult cryptoResult = encrypt(password);
        System.out.println(cryptoResult.getContent());
        System.out.println(verify(password, "ZX28oxmkph1g8gPrl4YD19SJf47iBSwNhveF_ES-DVbyZw1FZB2jykj3TB-B7HZ7NOpJ-8j8Yq14lOCkgqeKgLtkN6TP6ti-VLRy5ymD7pLwqdVp-CNe7frPyEdn99Fk44eHETdpTCWQZjK6_A1SN0CuhmsKGbR8l3ne3UiR-Urgd8pxjl8TnnjjJ904IDOb1JFnuRbCLD9ogroAMSb7Yw=="));
    }
}
