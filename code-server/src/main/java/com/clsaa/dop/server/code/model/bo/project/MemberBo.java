package com.clsaa.dop.server.code.model.bo.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberBo {
    private int id;
    private String username;
    private int access_level;
}
