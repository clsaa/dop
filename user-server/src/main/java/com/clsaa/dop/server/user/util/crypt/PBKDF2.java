package com.clsaa.dop.server.user.util.crypt;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Bytes;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * @author 任贵杰 812022339@qq.com
 */
public final class PBKDF2 {

    private PBKDF2() {
        throw new UnsupportedOperationException();
    }

    /**
     * PBKDF2摘要，返回BASE64URL编码的结果,salt会被拼接在加密结果尾部
     * byte[] byteCipher = secretKey.getEncoded();
     * byte[] byteResult = Bytes.concat(byteCipher, salt);
     *
     * @param data       需要进行摘要的数据
     * @param salt       盐
     * @param iterations 迭代次数
     * @param len        生成密文长度
     * @return {@link CryptoResult}
     */
    public static CryptoResult hash(String algorithm, String data, byte[] salt, int iterations, int len) {
        try {
            PBEKeySpec pbeKeySpec = new PBEKeySpec(data.toCharArray(), salt, iterations, len);
            SecretKeyFactory kFactory = SecretKeyFactory.getInstance(algorithm);
            SecretKey secretKey = kFactory.generateSecret(pbeKeySpec);
            byte[] byteCipher = secretKey.getEncoded();
            byte[] byteResult = Bytes.concat(byteCipher, salt);
            String base64URLResult = BaseEncoding.base64Url()
                    .encode(byteResult);
            return new CryptoResult(CryptoResult.Status.OK, base64URLResult);
        } catch (Exception ignored) {
            return new CryptoResult(CryptoResult.Status.Exception);
        }
    }
}
