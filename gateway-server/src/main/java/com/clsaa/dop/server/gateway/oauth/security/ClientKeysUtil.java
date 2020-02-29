package com.clsaa.dop.server.gateway.oauth.security;

import com.clsaa.dop.server.gateway.config.BizCodes;
import com.clsaa.dop.server.gateway.model.po.Client;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import com.google.common.io.BaseEncoding;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * 客户端key secret 生成、转换工具类
 *
 * @author 任贵杰 812022339@qq.com
 */
public class ClientKeysUtil {
    /**
     * 通过clientName构造一个clientId(client_id)
     *
     * @param clientName 客户端名称
     * @return client_id
     */
    public static String newClientId(String clientName) {
        // 若为空使用默认前缀
        clientName = StringUtils.hasText(clientName) ? clientName : Client.DEFAULT_CLIENT_ID_PREFIX;
        // 若长度大于18则截取前面18个字符
        clientName = clientName.length() > 18 ? clientName.substring(0, 18) : clientName;
        // 构造AppKey
        StringBuilder stringBuilder = new StringBuilder(clientName)
                //32或36位字符串
                .append(BaseEncoding.base64Url().encode(new BigInteger(196, new SecureRandom()).toByteArray()));
        //截取前30位
        return stringBuilder.substring(0, Client.DEFAULT_CLIENT_ID_PREFIX_LEN);
    }

    /**
     * 生成 Base64url 编码的 客户端 明文密码
     *
     * @return 客户端 明文密码（client_secret）
     */
    public static String newPlainClientSecret() {
        return BaseEncoding.base64Url().encode(new BigInteger(384, new SecureRandom()).toByteArray());
    }

    /**
     * 生成客户端密匙的密文(存在数据库中)
     *
     * @param clientSecretPlainText 客户端密匙明文
     * @param BASE64URLAesKey       BASE64URL编码的加密密匙
     * @return 客户端密匙的密文
     */
    public static String getAesChipperSecret(String clientSecretPlainText, String BASE64URLAesKey) {
        // AppSecret密文，存在数据库中
        CryptoResult result = SlowlyAes.encrypt(BaseEncoding.base64Url().decode(BASE64URLAesKey), clientSecretPlainText);
        BizAssert.validParam(result.isOK(), new BizCode(BizCodes.INNER_ERROR.getCode(), "生成client_secret时加密失败"));
        return result.getContent();
    }

    /**
     * 解密客户端密匙
     *
     * @param clientSecretChipperText 客户端密匙密文
     * @param BASE64URLAesKey         BASE64URL编码的加密密匙
     * @return 客户端密匙的密文
     */
    public static String getAesPlainSecret(String clientSecretChipperText, String BASE64URLAesKey) {
        // AppSecret密文，存在数据库中
        CryptoResult result = SlowlyAes.decrypt(BaseEncoding.base64Url().decode(BASE64URLAesKey), clientSecretChipperText);
        BizAssert.validParam(result.isOK(), new BizCode(BizCodes.INNER_ERROR.getCode(), "生成client_secret时解密失败"));
        return result.getContent();
    }
}
