package com.clsaa.dop.server.code.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SSHKeyDto {
    private String key;
    private String title;
    private Long userId;
}
