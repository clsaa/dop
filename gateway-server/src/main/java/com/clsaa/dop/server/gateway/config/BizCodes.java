package com.clsaa.dop.server.gateway.config;

import com.clsaa.rest.result.bizassert.BizCode;

/**
 * @author 任贵杰
 */
public interface BizCodes {

    /**
     * 非法请求， 比如，请求接口时没通过Authorization请求头传递access_token，或者接口参数的值为空，比如申请token时 grant_type为空
     */
    BizCode INVALID_REQUEST = new BizCode(900000, "OAuth请求非法");

    /**
     * 使用伪造的AccessToken请求接口
     */
    BizCode INVALID_ACCESS_TOKEN = new BizCode(900001, "AccessToken非法");

    /**
     * 客户端AccessToken已失效，客户端应主动刷新Token或者重新申请AccessToken
     */
    BizCode ACCESS_TOKEN_EXPIRED = new BizCode(900002, "AccessToken已失效");

    /**
     * 使用伪造的OAuth客户端请求AccessToken
     */
    BizCode INVALID_CLIENT = new BizCode(900003, "OAuth客户端非法");

    /**
     * 授权类型(grant_type)不支持，目前仅支持refresh_token和client_credentials
     */
    BizCode INVALID_GRANT_TYPE = new BizCode(900004, "OAuth不支持此类授权");

    /**
     * OAuth客户端已被禁用
     */
    BizCode INVALID_CLIENT_STATUS = new BizCode(900005, "OAuth客户端不可用");

    /**
     * 使用伪造的RefreshToken刷新AccessToken，客户端IP可能被封
     */
    BizCode INVALID_REFRESH_TOKEN = new BizCode(900006, "RefreshToken非法");

    /**
     * 使用过期的RefreshToken刷新AccessToken，客户端应主动重新申请AccessToken
     */
    BizCode REFRESH_TOKEN_EXPIRED = new BizCode(900007, "RefreshToken已失效");

    /**
     * OAuth客户端无权访问特定服务，客户端应主动在janus-admin申请新权限
     */
    BizCode INVALID_SCOPE = new BizCode(900008, "客户端无权访问此服务");

    /**
     * 验证签名时，签名非法
     */
    BizCode INVALID_SIGNATURE = new BizCode(900009, "非法签名");

    /**
     * 验证签名时，客户端与服务端时钟不同步
     */
    BizCode INVALID_TIMESTAMP = new BizCode(900010, "时钟不同步");

    /**
     * 使用被禁用的RefreshToken刷新AccessToken，客户端应重新申请AccessToken
     */
    BizCode INVALID_REFRESH_TOKEN_STATUS = new BizCode(900011, "RefreshToken被禁用");

    /**
     * 使用被禁用的AccessToken，客户端应使用RefreshToken刷新AccessToken或重新申请AccessToken
     */
    BizCode INVALID_ACCESS_TOKEN_STATUS = new BizCode(900012, "AccessToken被禁用");

    /**
     * 内部错误，如数据库未成功插入；参数合法情况下，加解密失败等，客户端可改变参数后进行重试
     */
    BizCode INNER_ERROR = new BizCode(100250, "Oauth网关内部错误");

    /**
     * 登入token失效或错误
     */
    BizCode INVALID_LOGIN_TOKEN = new BizCode(100350, "登入token失效或错误，请重新登入");
}