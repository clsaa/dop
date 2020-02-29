package com.clsaa.dop.server.pipeline.feign;


import com.clsaa.dop.server.pipeline.config.FeignConfig;
import com.clsaa.dop.server.pipeline.model.dto.UserCredential;
import com.clsaa.dop.server.pipeline.model.dto.UserCredentialV1;
import com.clsaa.dop.server.pipeline.model.dto.UserV1;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "user-server", configuration = FeignConfig.class)
public interface UserFeign {

    @GetMapping("/v1/users/{userId}/credential")
    UserCredentialV1 getUserCredentialV1ByUserId(
            @ApiParam(value = "用户id") @PathVariable("userId") Long userId,
            @ApiParam(value = "凭证类型，用于标识凭证的用途") @RequestParam("type") UserCredential.Type type);

    @ApiOperation(value = "根据id查询用户信息", notes = "根据id查询用户信息，若用户不存在返回null")
    @GetMapping("/v1/users/{id}")
    UserV1 findUserByIdV1(@ApiParam(value = "用户id") @PathVariable("id") Long id);
}
