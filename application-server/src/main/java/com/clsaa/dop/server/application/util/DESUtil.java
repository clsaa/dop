package com.clsaa.dop.server.application.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;


/**
 * 用DES对称算法加密数据库连接的信息</br>
 * 所谓对称算法就是加密和解密使用相同的key
 *
 * @author zhu
 */
public class DESUtil {
    private static Key key;
    // 设置密钥key
    private static String KEY_STR = "nju-dop-sha-key@#$*^%";
    private static String CHARSETNAME = "UTF-8";
    private static String ALGORITHM = "DES";

    static {
        try {
            // 生成des算法对象
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            // 运用SHA1安全策略
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            // 设置密钥种子
            secureRandom.setSeed(KEY_STR.getBytes());
            // 初始化基于SHA1的算法对象
            generator.init(secureRandom);
            // 生成密钥对象
            key = generator.generateKey();
            generator = null;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 获取加密后的信息
     *
     * @param str
     * @return
     */
    public static String getEncryptString(String str) {
        // 基于BASE64编码，接收byte[]并转换层String
        BASE64Encoder base64encoder = new BASE64Encoder();
        try {
            // utf-8编码
            byte[] bytes = str.getBytes(CHARSETNAME);
            // 获取加密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化密码信息
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            byte[] doFinal = cipher.doFinal(bytes);
            // 返回
            return base64encoder.encode(doFinal);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 获取解密之后的信息
     *
     * @param str
     * @return
     */
    public static String getDecryptString(String str) {
        //基于BASE64编码，接收byte[]并转换成String
        BASE64Decoder base64decoder = new BASE64Decoder();
        try {
            //将字符串decode成byte[]
            byte[] bytes = base64decoder.decodeBuffer(str);
            //获取解密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            //初始化解密信息
            cipher.init(Cipher.DECRYPT_MODE, key);
            //解密
            byte[] doFinal = cipher.doFinal(bytes);
            //返回解密之后的信息
            return new String(doFinal, CHARSETNAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

