package com.clsaa.dop.server.gateway.feign;

import com.clsaa.dop.server.gateway.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author joyren
 */
@Component
@FeignClient(value = "login-server", configuration = FeignConfig.class)
public interface LoginFeign {
    /**
     * 校验token
     *
     * @param token token
     * @return 是否校验通过
     */
    @GetMapping("/v1/login/token")
    boolean verifyToken(@RequestParam("token") String token);
}