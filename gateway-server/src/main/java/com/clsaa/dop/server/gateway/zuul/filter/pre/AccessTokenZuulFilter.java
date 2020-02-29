package com.clsaa.dop.server.gateway.zuul.filter.pre;

import com.alibaba.fastjson.JSON;
import com.clsaa.dop.server.gateway.config.BizCodes;
import com.clsaa.dop.server.gateway.config.GatewayProperties;
import com.clsaa.dop.server.gateway.model.bo.AccessTokenBoV1;
import com.clsaa.dop.server.gateway.model.vo.ErrorResult;
import com.clsaa.dop.server.gateway.oauth.security.CryptoResult;
import com.clsaa.dop.server.gateway.oauth.security.FastAes;
import com.clsaa.dop.server.gateway.service.AccessTokenService;
import com.google.common.io.BaseEncoding;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * 验证AccessToken的过滤器
 *
 * @author 任贵杰 812022339@qq.com
 * @summary 验证AccessToken
 * @since 2018-12-29
 */
public class AccessTokenZuulFilter extends ZuulFilter {
    /**
     * http 授权头
     */
    private static final String HTTP_AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private GatewayProperties properties;
    @Autowired
    private AccessTokenService accessTokenService;
    private byte[] tokenSecret = null;

    @PostConstruct
    void init() {
        this.tokenSecret = BaseEncoding.base64Url()
                .decode(this.properties.getOauth().getAES().getTokenKey());
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        System.out.println("should filter");
        if (ctx.getRequest().getRequestURI().contains("swagger")
                || ctx.getRequest().getRequestURI().contains("api-docs")) {
            System.out.println("swagger 请求放行");
            return false;
        }
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * order越小优先级越高
     */
    @Override
    public int filterOrder() {
        return Integer.MIN_VALUE;
    }

    /**
     * 注意：AccessToken优先级最高，如果可能的话，只使用RequestHeader来进行业务判断。
     * 没必要为了非法请求浪费内存，耗费CPU去解析请求参数（request.getParameter会触发解析请求参数)
     *
     * @see com.netflix.zuul.IZuulFilter#run()
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        // 检查是否有header
        String bearToken = ctx.getRequest().getHeader(HTTP_AUTHORIZATION_HEADER);
        if (StringUtils.isEmpty(bearToken)) {
            returnForError();
        }
        // bearer和token之间用一个空格分隔
        // 参考 http://self-issued.info/docs/draft-ietf-oauth-v2-bearer.html#authz-header
        String[] tokens = StringUtils.split(bearToken, " ");
        if (tokens == null || tokens.length != 2) {
            returnForError();
        }
        // token自校验解密
        CryptoResult cryptoResult = FastAes.decrypt(this.tokenSecret, tokens[1]);
        // 是否通过自校验
        if(!cryptoResult.isOK()){
            returnForError();
        }
        // 查找token
        AccessTokenBoV1 token = this.accessTokenService.findAccessTokenByToken(cryptoResult.getContent());
        // 是否失效
        if(token==null||token.isExpired()){
            returnForError();
        }
        return null;
    }

    private void returnForError() {
        RequestContext ctx = RequestContext.getCurrentContext();
        // 过滤该请求，不对其进行路由
        ErrorResult errorResult = ErrorResult.builder()
                .path(ctx.getRequest().getRequestURI())
                .bizCode(BizCodes.INVALID_ACCESS_TOKEN)
                .error(BizCodes.INVALID_ACCESS_TOKEN.getMessage())
                .trace(this.getClass().getName())
                .build();
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(403);
        ctx.setResponseBody(JSON.toJSONString(errorResult));
        ctx.getResponse().setContentType("application/json;charset=UTF-8");
    }
}
