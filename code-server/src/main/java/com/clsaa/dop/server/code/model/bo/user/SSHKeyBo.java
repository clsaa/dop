package com.clsaa.dop.server.code.model.bo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SSHKeyBo {

    private int id;
    private String title;
    private String key;
    private String created_at;

}
