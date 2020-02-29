package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.config.FeignConfig;
import com.clsaa.dop.server.permission.model.dto.UserDtoV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lzy
 */
@Component

@FeignClient(value = "user-server", configuration = FeignConfig.class)
public interface UserFeignService {
    /**
     * 通过邮箱、密码获取用户信息
     *
     * @param id  用户ID
     * @return {@link UserDtoV1}
     */
    @GetMapping("/v1/users/{id}")
     UserDtoV1 findUserByIdV1(@PathVariable("id") Long id);
}