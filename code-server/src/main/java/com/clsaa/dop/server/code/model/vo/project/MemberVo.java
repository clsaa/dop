package com.clsaa.dop.server.code.model.vo.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberVo {
    private int id;
    private String username;
    private int access_level;
}
