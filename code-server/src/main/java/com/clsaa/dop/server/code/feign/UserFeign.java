package com.clsaa.dop.server.code.feign;

import com.clsaa.dop.server.code.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wsy
 */
@Component
@FeignClient(value = "user-server",configuration = FeignConfig.class)
public interface UserFeign {

    @GetMapping("/v1/account/RSAPublicKey")
    String getAccountRSAPublicKey();


    @PostMapping("/v1/users/{userId}/credential")
    void addUserCredential(@PathVariable("userId") Long userId,
                                  @RequestParam("identifier") String identifier,
                                  @RequestParam("credential") String credential,
                                  @RequestParam("type") UserCredentialType type);


    @GetMapping("/v1/users/{userId}/credential")
    UserCredentialV1 getUserCredentialV1ByUserId(@PathVariable("userId") Long userId, @RequestParam("type") UserCredentialType type);

}
