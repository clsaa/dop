package com.clsaa.dop.server.code.util.crypt;

import com.google.common.io.BaseEncoding;

import javax.crypto.Cipher;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


/**
 * 非对称加密算法RSA算法组件
 * 非对称算法一般是用来传送对称加密算法的密钥来使用的，相对于DH算法，RSA算法只需要一方构造密钥，不需要
 * 大费周章的构造各自本地的密钥对了。DH算法只能算法非对称算法的底层实现。而RSA算法算法实现起来较为简单
 *
 * @author kongqz
 */
public class RSA {
    //非对称密钥算法
    private static final String KEY_ALGORITHM = "RSA";


    /**
     * 密钥长度，DH算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE = 2048;
    /**
     * 公钥名
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 私钥名
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 初始化密钥对
     *
     * @return Map 甲方密钥的Map
     */
    private static Map<String, Object> initKey() throws Exception {
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        //初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE);
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //甲方公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //甲方私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //将密钥存储在map中
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;

    }


    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密数据
     */
    private static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {

        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @param key  Base64URL编码过的密钥
     * @return {@link CryptoResult} 加密数据
     */
    public static CryptoResult encryptByPrivateKey(String data, String key) {
        byte[] byteKey = BaseEncoding.base64Url().decode(key);
        byte[] byteData = data.getBytes(StandardCharsets.UTF_8);

        try {
            byte[] byteResult = RSA.encryptByPrivateKey(byteData, byteKey);
            String base64URLResult = BaseEncoding.base64Url().encode(byteResult);
            return new CryptoResult(CryptoResult.Status.OK, base64URLResult);
        } catch (Exception e) {
            return new CryptoResult(CryptoResult.Status.Exception);
        }
    }

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密数据
     */
    private static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {

        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key  Base64URL编码过的密钥
     * @return {@link CryptoResult} 加密数据
     */
    private static CryptoResult encryptByPublicKey(String data, String key) {
        byte[] byteKey = BaseEncoding.base64Url().decode(key);
        byte[] byteData = data.getBytes(StandardCharsets.UTF_8);
        try {
            byte[] byteResult = RSA.encryptByPublicKey(byteData, byteKey);
            String base64URLResult = BaseEncoding.base64Url().encode(byteResult);
            return new CryptoResult(CryptoResult.Status.OK, base64URLResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new CryptoResult(CryptoResult.Status.Exception);
        }
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    private static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  Base64URL编码过的密钥
     * @return {@link CryptoResult} 解密数据
     */
    public static CryptoResult decryptByPrivateKey(String data, String key) {
        byte[] byteKey = BaseEncoding.base64Url().decode(key);
        byte[] byteData = BaseEncoding.base64Url().decode(data);

        try {
            byte[] byteResult = RSA.decryptByPrivateKey(byteData, byteKey);
            String base64URLResult = BaseEncoding.base64Url().encode(byteResult);
            return new CryptoResult(CryptoResult.Status.OK, new String(byteResult, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
            return new CryptoResult(CryptoResult.Status.Exception);
        }
    }

    /**
     * 公钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    private static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {

        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data 待解密数据
     * @param key  Base64URL编码过的密钥
     * @return {@link CryptoResult} 解密数据
     */
    public static CryptoResult decryptByPublicKey(String data, String key) {
        byte[] byteKey = BaseEncoding.base64Url().decode(key);
        byte[] byteData = BaseEncoding.base64Url().decode(data);


        try {
            byte[] byteResult = RSA.decryptByPublicKey(byteData, byteKey);
            String base64URLResult = BaseEncoding.base64Url().encode(byteResult);
            return new CryptoResult(CryptoResult.Status.OK, new String(byteResult, StandardCharsets.UTF_8));
        } catch (Exception e) {
            return new CryptoResult(CryptoResult.Status.Exception);
        }
    }

    /**
     * 取得私钥
     *
     * @param keyMap 密钥map
     * @return byte[] 私钥
     */
    private static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 取得Base64URL编码过的私钥
     *
     * @param keyMap 密钥map
     * @return {@link String} 私钥
     */
    private static String getBase64URLPrivateKey(Map<String, Object> keyMap) {
        return BaseEncoding.base64Url().encode(RSA.getPrivateKey(keyMap));
    }

    /**
     * 取得公钥
     *
     * @param keyMap 密钥map
     * @return byte[] 公钥
     */
    private static byte[] getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

    /**
     * 取得Base64URL编码过的公钥
     *
     * @param keyMap 密钥map
     * @return {@link String} 公钥
     */
    private static String getBase64URLPublicKey(Map<String, Object> keyMap) {
        return BaseEncoding.base64Url().encode(RSA.getPublicKey(keyMap));
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Charset.defaultCharset().displayName());
        //初始化密钥
        //生成密钥对
        Map<String, Object> keyMap = RSA.initKey();
        //公钥
        byte[] publicKey = RSA.getPublicKey(keyMap);
        //私钥
        byte[] privateKey = RSA.getPrivateKey(keyMap);
        String publicKeyStr = RSA.getBase64URLPublicKey(keyMap);
        String privateKeyStr = RSA.getBase64URLPrivateKey(keyMap);
        System.out.println("公钥 str：\n" + publicKeyStr);
        System.out.println("私钥 str：\n" + privateKeyStr);
        String data = "12343123210385438958345";
        CryptoResult cryptoResult = RSA.encryptByPublicKey(data, publicKeyStr);
        System.out.println("加密结果：" + cryptoResult.getContent());
        CryptoResult deResult = RSA.decryptByPrivateKey(cryptoResult.getContent(), privateKeyStr);
        System.out.println("解密结果：" + deResult.getContent());
        System.out.println("assert : " + deResult.getContent().equals(data));

        System.out.println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");

        String str = "RSA密码交换算法";
        System.out.println("\n===========甲方向乙方发送加密数据==============");
        System.out.println("原文:" + str);
        //甲方进行数据的加密
        byte[] code1 = RSA.encryptByPrivateKey(str.getBytes(), privateKey);
        System.out.println("加密后的数据：" + BaseEncoding.base64Url().encode(code1));
        System.out.println("===========乙方使用甲方提供的公钥对数据进行解密==============");
        //乙方进行数据的解密
        byte[] decode1 = RSA.decryptByPublicKey(code1, publicKey);
        System.out.println("乙方解密后的数据：" + new String(decode1) + "/n/n");

        System.out.println("===========反向进行操作，乙方向甲方发送数据==============/n/n");

        str = "h";

        System.out.println("原文:" + str);

        //乙方使用公钥对数据进行加密
        byte[] code2 = RSA.encryptByPublicKey(str.getBytes(), publicKey);
        System.out.println("===========乙方使用公钥对数据进行加密==============");
        System.out.println("加密后的数据：" + BaseEncoding.base64Url().encode(code2));

        System.out.println("=============乙方将数据传送给甲方======================");
        System.out.println("===========甲方使用私钥对数据进行解密==============");

        //甲方使用私钥对数据进行解密
        byte[] decode2 = RSA.decryptByPrivateKey(code2, privateKey);

        System.out.println("甲方解密后的数据：" + new String(decode2));

        publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA50CR3EdT0EpCs7YbakOfoRe2Q" +
                "QMQ1qvDWwS8ts1E98NCunPI8ozigtuNl7ZqtpP_VP_MR66B3TnDwGvDfbj7VoYIDURwyu_KzfsfuTq" +
                "5jH3D1hSxzqsXp6zyp8s9pkhn0zwIr2AK90lX3lUY4DsFQTUOpqtfJ9PFFWsHUBtK7CfT2mJhnf3JtVLG" +
                "6rEJk8JQvEg3M0loqHQBo8TP35jXWYeEi-oULNed1eLEKk1Gzg-DLB8C5I_stsHhgzYmcFr4ZMwsmEb1hPsgs" +
                "ZHLEPDXnSvmFPMuT0hi3Qx6lPrle-6dUrKBp48Wb7HZqIt5S2g45HvLnFh3FZKZM3mjqNm09QIDAQAB";
        privateKeyStr = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDnQJHcR1PQSkKzthtqQ5-hF7ZBAxDWq8NbBLy2zUT3w0K6c8jyjOKC242Xtmq2k_9U_8xHroHdOcPAa8N9uPtWhggNRHDK78rN-x-5OrmMfcPWFLHOqxenrPKnyz2mSGfTPAivYAr3SVfeVRjgOwVBNQ6mq18n08UVawdQG0rsJ9PaYmGd_cm1UsbqsQmTwlC8SDczSWiodAGjxM_fmNdZh4SL6hQs153V4sQqTUbOD4MsHwLkj-y2weGDNiZwWvhkzCyYRvWE-yCxkcsQ8NedK-YU8y5PSGLdDHqU-uV77p1SsoGnjxZvsdmoi3lLaDjke8ucWHcVkpkzeaOo2bT1AgMBAAECggEAZK5LEuGHDxdVxRJTC0fX1vYQ5mskSKJNknIUi24BSfGcOrswUNGfyKM4GUZVXZo_v00DLm4-ogeFP2Bro3AHFVuTY9tZVDFkVZuw6x8zK4SR38Hwy-7XhKwalY06EQxTD7eCfIBPJeyrsePQycdIeWRVNaFE830SwElxzKGlU_0LFmtoPfZqiRKriLpII2hAiRedNvrvlNpAUGtgGCpahRCGQmpFTM2FRD-F7ChLnY-wAVa88zsMcLpwVTchx6t6inxsnI6QEHfhUsFrio_a-nvLE_Lh7I_rSWIzPXiwdbuUVo1LbQl9TphtGekvHn8SUlSJJ7Bg8NkjACoXHqlO4QKBgQD7mn_ZiS_yMbrRQ-EJad62GyogmM6y-rNA9cjh9uNk_QCJdLOehjRJ8HOk8b-YMdyVbvHJLHs8gAqL9u4o5c75CZgcTYQRZOdef5Usk15E933tHowKNAHLZC5ubvFNrUdihMcIFEwfFFAyTwmXsWSqqekhAFALFcK7_JfyIl32zQKBgQDrSwhg9_0lqAb-EFf27Nk95mO3vtLmarlgSWReWeDf1bi4FC80J3gyyEtwQm4DQQEOjLGkQPDUdn_pxQZ-jWXNL02o-pXYg-IJiRJtcwkpKNgi3UgN0OF6VAF4-UbGLhkFlLVPjA7KY2DCXIvboQvY74ONmc2T0LU6N1RTtzKmyQKBgHEH7ZvKt9F0adsNRZG3ECh7d6eBPorFebYbNQ3enjWGO7GEzhTPcbot3aYiPNKgw9oWJ2UbKJn7Y8GRQ1j4NM1IdB9y63vs-sxHQLrv0Y_mJ5pY-My0pIG9FebizGD7EXC--bM1eyUTORwAyext8y2Ae40p2MvCEOPnDFPpAJGZAoGBAKK3bwHwPcau3rcQgmSnAs1F6KwHcT4bjDtERf7h1Ru2Z1oVy1cb_InR7imiJwZlC-AN-EOJDYNAt9Doikc2bbJqBiDXuozTF5xUiMPcC_fE4UOrF8tGKsg4FBSe_wzKkE9FueVbDFd2RlHjQm0OgGzzplxkgsC4UrwBy3VhecvRAoGBAIJsHSZzoALIis_rIGxsq8ENiCP52C6SiQ4hS90BcUcXmhZ2AeOFpyB0yyVaCRMm3B8uIDjymfEIl6-EUMX1Qj-CY_CncD2ZCscSJp3kWVQdiYkeVEduHZZZ4JUcgg6cgp00QSKXO_nfm17ciIFBcOmsO_JciwNS8TxEvs-Qz8cF";
        CryptoResult en = RSA.encryptByPublicKey("Ren19960225.", publicKeyStr);
        System.out.println(en.getContent());
        CryptoResult de = RSA.decryptByPrivateKey(en.getContent(), privateKeyStr);
        System.out.println(de.getContent());

        CryptoResult en2 = RSA.encryptByPrivateKey("Ren19960225.", privateKeyStr);
        System.out.println(en2.getContent());
        CryptoResult de2 = RSA.decryptByPublicKey(en2.getContent(), publicKeyStr);
        System.out.println(de2.getContent());

    }
}
