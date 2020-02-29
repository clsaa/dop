package com.clsaa.dop.server.image.feign.harborfeign;

import com.clsaa.dop.server.image.config.FeignConfig;
import com.clsaa.dop.server.image.model.po.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * harbor用户管理类
 * @author  xzt
 * @since 2019-4-5
 */
@Component
@FeignClient(value = "user",url = "${feign.url}",configuration = FeignConfig.class)
public interface HarborUserFeign {

    /**
     * 注册harbor用户
     * @param user 用户信息
     */
    @PostMapping(value = "/users")
    void usersPost(@RequestBody User user);

}
