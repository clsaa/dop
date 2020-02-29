package com.clsaa.dop.server.gateway.model.vo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessTokenV1 {
    /**
     * 颁发的AccessToken
     */
    private String access_token;
    /**
     * 授权类型
     */
    private String token_type;
    /**
     * AccessToken的有效期,以秒为单位
     */
    private Integer expires_in;
}
