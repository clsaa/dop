package com.clsaa.dop.server.test.manager.feign;

import com.clsaa.dop.server.test.config.FeignConfig;
import com.clsaa.dop.server.test.model.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author xihao
 * @version 1.0
 * @since 02/04/2019
 */
@Component
@FeignClient(value = "user-server", configuration = FeignConfig.class)
public interface UserInterface {

    @GetMapping("/v1/users/{id}")
    User getUserById(@PathVariable("id") Long userId);

}
