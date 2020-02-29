package com.clsaa.dop.server.user.util.crypt;

import java.security.SecureRandom;

/**
 * 盐生成工具类
 *
 * @author joyren
 */
public class Salt {
    private Salt() {
        throw new UnsupportedOperationException();
    }

    /**
     * 生成一个新的盐
     *
     * @param len 要生成盐的位数
     * @return {@link byte[]} 盐
     */
    public static byte[] newSalt(int len) {
        byte[] salt = new byte[len];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }
}
