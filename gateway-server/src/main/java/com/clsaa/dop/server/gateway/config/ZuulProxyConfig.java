package com.clsaa.dop.server.gateway.config;

import com.clsaa.dop.server.gateway.zuul.filter.pre.AccessTokenZuulFilter;
import com.clsaa.dop.server.gateway.zuul.filter.pre.UserLoginZuulFilter;
import org.springframework.cloud.netflix.zuul.ZuulProxyAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Zuul网关扩展配置
 *
 * @author 任贵杰
 */
@Configuration
public class ZuulProxyConfig extends ZuulProxyAutoConfiguration {
    /**
     * Access-token 头部校验 优先级必须最高（高于所有其他preFilter），
     */
    @Bean
    public AccessTokenZuulFilter accessTokenZuulFilter() {
        return new AccessTokenZuulFilter();
    }

    /**
     * 用户登录 X-Login-Token校验，
     */
    @Bean
    public UserLoginZuulFilter userLoginZuulFilter() {
        return new UserLoginZuulFilter();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
