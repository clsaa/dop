package com.clsaa.dop.server.login.feign;

import com.clsaa.dop.server.login.config.FeignConfig;
import com.clsaa.dop.server.login.model.dto.UserDtoV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author joyren
 */
@Component
@FeignClient(value = "user-server", configuration = FeignConfig.class)
public interface UserFeign {
    /**
     * 通过邮箱、密码获取用户信息
     *
     * @param email    电子邮箱
     * @param password 密码
     * @return {@link UserDtoV1}
     */
    @GetMapping("/v1/users/byCredential")
    UserDtoV1 findUserByEmailAndPassword(@RequestParam("email") String email,
                                         @RequestParam("password") String password);
}