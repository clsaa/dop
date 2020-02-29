package com.clsaa.dop.server.code.model.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SSHKeyVo {

    private int id;
    private String title;
    private String key;
    private String created_at;

}
