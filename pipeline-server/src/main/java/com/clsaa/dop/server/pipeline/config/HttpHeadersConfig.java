package com.clsaa.dop.server.pipeline.config;

public class HttpHeadersConfig {
    public interface HttpHeaders {
        /**
         * 用户登录Token请求头
         */
        String X_LOGIN_TOKEN = "x-login-token";
        /**
         * 登录用户id
         */
        String X_LOGIN_USER = "x-login-user";
    }
}
