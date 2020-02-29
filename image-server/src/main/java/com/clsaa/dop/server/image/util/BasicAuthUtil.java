package com.clsaa.dop.server.image.util;

import com.clsaa.dop.server.image.model.dto.UserCredentialDto;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * <p>
 *    Basic Auth工具类
 * </p>
 * @author xzt
 * @since 2019-3-28
 */
@Component
public class BasicAuthUtil {
    /**
     * 通过用户信息产生basic auth
     * @param userCredentialDto 用户信息
     * @return {@link String} basic auth
     */
    public static String createAuth(UserCredentialDto userCredentialDto){
        String userNamePassword = userCredentialDto.getIdentifier() +":"+userCredentialDto.getCredential();
        String auth = "Basic "+Base64.getEncoder().encodeToString(userNamePassword.getBytes());
        return auth;
    }
}
