package com.clsaa.dop.server.test.exception;

import com.clsaa.dop.server.test.config.BizCodes;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

/**
 * @author xihao
 * @version 1.0
 * @since 16/03/2019
 */
@RestControllerAdvice(basePackages = "com.clsaa.dop.server.test.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public void handle(WebExchangeBindException exception) {
        String message = exception.getMessage();
        BizAssert.justInvalidParam(BizCodes.INVALID_PARAM.getCode(), message);
    }
}
