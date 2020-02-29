package com.clsaa.dop.server.gateway.zuul.filter.pre;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.clsaa.dop.server.gateway.config.BizCodes;
import com.clsaa.dop.server.gateway.config.GatewayProperties;
import com.clsaa.dop.server.gateway.config.HttpHeaders;
import com.clsaa.dop.server.gateway.feign.LoginFeign;
import com.clsaa.dop.server.gateway.model.vo.ErrorResult;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 用户认证流程过滤器
 *
 * @author joyren
 */
public class UserLoginZuulFilter extends ZuulFilter {

    @Autowired
    private LoginFeign loginFeign;
    @Autowired
    private GatewayProperties gatewayProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return Integer.MIN_VALUE + 2;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        //如果携带了登录头则进行校验
        return StringUtils.isNotEmpty(ctx.getRequest().getHeader(HttpHeaders.X_LOGIN_TOKEN));
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String token = ctx.getRequest().getHeader(HttpHeaders.X_LOGIN_TOKEN);

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(this.gatewayProperties.getJwt().getSecret())).build();
            DecodedJWT jwt = verifier.verify(token);
            Long userId = jwt.getClaim("userId").asLong();
            Date expiresAt = jwt.getExpiresAt();
            if (expiresAt.getTime() < System.currentTimeMillis()) {
                //token已过期
                returnForError();
            }
            boolean verifyResult = this.loginFeign.verifyToken(token);
            if (verifyResult) {
                //token校验通过，将对应userId置于请求头中
                ctx.addZuulRequestHeader(HttpHeaders.X_LOGIN_USER, String.valueOf(userId));
            } else {
                //该token已被删除
                returnForError();
            }
        } catch (Exception e) {
            returnForError();
        }

        return null;
    }

    private void returnForError() {
        RequestContext ctx = RequestContext.getCurrentContext();
        // 过滤该请求，不对其进行路由
        ErrorResult errorResult = ErrorResult.builder()
                .path(ctx.getRequest().getRequestURI())
                .bizCode(BizCodes.INVALID_LOGIN_TOKEN)
                .error(BizCodes.INVALID_LOGIN_TOKEN.getMessage())
                .trace(this.getClass().getName())
                .build();
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(403);
        ctx.setResponseBody(JSON.toJSONString(errorResult));
        ctx.getResponse().setContentType("application/json;charset=UTF-8");
    }
}
