package com.clsaa.dop.server.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 网关配置类
 *
 * @author 任贵杰 812022339@qq.com
 * @version v1
 * @summary 网关配置类
 * @since 2018-12-29
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "gateway")
@Validated
public class GatewayProperties {

	@NotNull
	@Valid
	private Oauth oauth;
	@NotNull
	@Valid
	private Jwt jwt;


	@Getter
	@Setter
	public static class Oauth {
		@NotNull
		@Valid
		private AES AES;
		@Getter
		@Setter
		public static class AES{
			@NotBlank
			private String clientKey;
			@NotBlank
			private String tokenKey;
		}
	}

	@Getter
	@Setter
	public static class Jwt {
		@NotNull
		@Valid
		private String secret;
	}
}
