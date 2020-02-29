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
public class ProjectAccessLevelVo {
    private String visibility;
    private int access_level;
}
