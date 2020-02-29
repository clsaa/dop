package com.clsaa.dop.server.user.util.crypt;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

import java.nio.charset.StandardCharsets;

/**
 * @author 任贵杰 812022339@qq.com
 */
public final class HMAC {

    private HMAC() {
        throw new UnsupportedOperationException();
    }

    /**
     * HMAC-SHA256摘要，返回BASE64URL编码的结果
     *
     * @param secret 密匙
     * @param data   明文
     * @return {@link CryptoResult}
     */
    public static CryptoResult SHA256(String secret, String data) {
        try {
            String base64URLResult = BaseEncoding.base64Url()
                    .encode(Hashing.hmacSha256(secret.getBytes(StandardCharsets.UTF_8)).hashString(data, StandardCharsets.UTF_8).asBytes());
            return new CryptoResult(CryptoResult.Status.OK, base64URLResult);
        } catch (Exception ignored) {
            return new CryptoResult(CryptoResult.Status.Exception);
        }
    }
}
