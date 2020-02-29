package com.clsaa.dop.server.gateway.oauth.security;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Bytes;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

/**
 * 慢速AES算法类,内部会使用PBEKey加密key
 *
 * @author 任贵杰 812022339@qq.com
 * @summary AES算法类
 * @since 2018-12-29
 */
public final class SlowlyAes {

    private static final String AES = "AES";
    private static final int IV_LEN = 16;
    private static final int SLAT_LEN = 8;

    private SlowlyAes() {
        throw new UnsupportedOperationException();
    }

    /**
     * 慢速AES128位加密,加密后内容会被BASE64URL编码
     *
     * @param AESKey  加密密匙,内部会使用PBEKey加密key,迭代30720次,生成128位,i7-5500U CPU @ 2.40GHz 执行200-300ms
     * @param content 加密内容
     * @return {@link CryptoResult}对象,加密后内容会被BASE64URL编码
     */
    public static CryptoResult encrypt(byte[] AESKey, String content) {
        try {
            //生成slat
            SecureRandom saltRandom = new SecureRandom();
            byte[] salt = new byte[SLAT_LEN];
            saltRandom.nextBytes(salt);

            //生成iv
            byte[] bytesIv = new byte[IV_LEN];
            SecureRandom ivRandom = new SecureRandom();
            ivRandom.nextBytes(bytesIv);

            //使用PBEKey加密key,迭代30720次,生成128位
            //i7-5500U CPU @ 2.40GHz 内存ddr3 1600MHZ 执行200-300ms
            PBEKeySpec keySpec = new PBEKeySpec(charsOf(AESKey), salt, 30720, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey key = factory.generateSecret(keySpec);
            key = new SecretKeySpec(key.getEncoded(), AES);

            //根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //初始化密码器
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(bytesIv));
            //获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byteEncode = content.getBytes(StandardCharsets.UTF_8);
            //根据密码器的初始化方式--加密
            byte[] byteAES = cipher.doFinal(byteEncode);
            byte[] byteResult = Bytes.concat(bytesIv, byteAES, salt);
            //将加密后的数据转换为字符串
            String AESEncode = BaseEncoding.base64Url().encode(byteResult);
            //将字符串返回
            return new CryptoResult(CryptoResult.Status.OK, AESEncode);
        } catch (NoSuchAlgorithmException ignored) {
            return new CryptoResult(CryptoResult.Status.NoSuchAlgorithmException);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ignored) {
            return new CryptoResult(CryptoResult.Status.Exception);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | InvalidKeySpecException ignored) {
            return new CryptoResult(CryptoResult.Status.InvalidKeyException);
        }
    }

    /**
     * AES128位解密,解密内容必须为BASE64URL编码
     *
     * @param AESKey  加密密匙,内部会使用PBEKey加密key,迭代30720次,生成128位,i7-5500U CPU @ 2.40GHz 执行200-300ms
     * @param content 加密内容
     * @return {@link CryptoResult}
     */
    public static CryptoResult decrypt(byte[] AESKey, String content) {
        try {
            //将加密并编码后的内容解码成字节数组
            byte[] byteContent = BaseEncoding.base64Url().decode(content);
            //获取iv/密文/salt
            byte[] byteIv = Arrays.copyOfRange(byteContent, 0, IV_LEN);
            byte[] byteChiper = Arrays.copyOfRange(byteContent, IV_LEN, byteContent.length - SLAT_LEN);
            byte[] salt = Arrays.copyOfRange(byteContent, byteContent.length - SLAT_LEN, byteContent.length);

            //使用PBEKey加密key,迭代30720次,生成128位
            //i7-5500U CPU @ 2.40GHz 内存ddr3 1600MHZ 执行200-300ms
            PBEKeySpec keySpec = new PBEKeySpec(charsOf(AESKey), salt, 30720, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey key = factory.generateSecret(keySpec);
            key = new SecretKeySpec(key.getEncoded(), AES);

            //根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //初始化密码器
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(byteIv));
            //解密
            byte[] byteDecode = cipher.doFinal(byteChiper);
            String AESDecode = new String(byteDecode, StandardCharsets.UTF_8);
            //将解密结果返回
            return new CryptoResult(CryptoResult.Status.OK, AESDecode);
        } catch (NoSuchAlgorithmException ignored) {
            return new CryptoResult(CryptoResult.Status.NoSuchAlgorithmException);
        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException ignored) {
            return new CryptoResult(CryptoResult.Status.Exception);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | InvalidKeySpecException ignored) {
            return new CryptoResult(CryptoResult.Status.InvalidKeyException);
        }
    }

    /**
     * byte数组转char数组
     *
     * @param bytes
     * @return
     */
    private static char[] charsOf(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes is null");
        }
        char[] result = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = (char) bytes[i];
        }
        return result;
    }
}
