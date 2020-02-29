package com.clsaa.dop.server.login.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.clsaa.rest.result.exception.*;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 讲FeignException转换为标准业务异常
 *
 * @author joyren
 */
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String body = Util.toString(response.body().asReader());
            JSONObject jsonObject = JSON.parseObject(body);
            int code = Integer.valueOf(String.valueOf(jsonObject.get("code")));
            String msg = String.valueOf(jsonObject.get("message"));
            switch (response.status()) {
                case 400:
                    return new InvalidParameterException(code, msg);
                case 403:
                    return new AccessDeniedException(code, msg);
                case 404:
                    return new NotFoundException(code, msg);
                case 417:
                    return new StandardBusinessException(code, msg);
                case 401:
                    return new UnauthorizedException(code, msg);
                default:
                    return new RuntimeException(body);
            }
        } catch (IOException ignored) {
        }
        return decode(methodKey, response);
    }
}