package com.clsaa.dop.server.image.feign;

import com.clsaa.dop.server.image.config.FeignConfig;
import com.clsaa.dop.server.image.model.dto.UserCredentialDto;
import com.clsaa.dop.server.image.model.enumtype.UserCredentialType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务接口
 */
@Component
@FeignClient(value = "user-server",configuration = FeignConfig.class)
public interface UserFeign {

    @GetMapping(value = "/v1/users/{userId}/credential")
    UserCredentialDto getUserCredentialV1ByUserId(@PathVariable("userId") Long userId,
                                                         @RequestParam("type")UserCredentialType type);

    @PostMapping("/v1/users/{userId}/credential")
    void addUserCredential(@PathVariable("userId") Long userId,
                           @RequestParam("identifier") String identifier,
                           @RequestParam("credential") String credential,
                           @RequestParam("type") UserCredentialType type);
}
