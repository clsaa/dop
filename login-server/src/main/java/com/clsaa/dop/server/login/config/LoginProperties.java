package com.clsaa.dop.server.login.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * 用户服务配置类
 *
 * @author joyren
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "login")
@Validated
public class LoginProperties {
    @NotNull
    @Valid
    private Jwt jwt;

    @Getter
    @Setter
    public static class Jwt {
        public static final String TYP_KEY = "typ";
        public static final String ALG_KEY = "alg";
        public static final String ISS_KEY = "iss";
        public static final String SUB_KEY = "sub";
        public static final String EXP_KEY = "exp";
        @NotNull
        @Valid
        private String typ;
        @NotNull
        @Valid
        private String alg;
        @NotNull
        @Valid
        private String iss;
        @NotNull
        @Valid
        private String aud;
        @NotNull
        @Valid
        private String sub;
        @NotNull
        @Valid
        private int exp;
        @NotNull
        @Valid
        private String secret;
    }
}
