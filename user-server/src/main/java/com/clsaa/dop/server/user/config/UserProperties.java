package com.clsaa.dop.server.user.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 用户服务配置类
 *
 * @author joyren
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "user")
@Validated
public class UserProperties {
    @NotNull
    @Valid
    private Account account;

    @Getter
    @Setter
    public static class Account {
        @NotNull
        @Valid
        private Secret secret;

        @Getter
        @Setter
        public static class Secret {
            @NotBlank
            @Valid
            private String RSAPublicKey;
            @NotBlank
            @Valid
            private String RSAPrivateKey;
        }
    }
}
