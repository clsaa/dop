package com.clsaa.dop.server.permission.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.time.LocalDateTime;
/**
 * 角色DTO对象
 *
 * @author lzy
 *

 *
 * @since :2019.4.6
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDtoV1 {

    /**
     * 角色ID
     */
    private Long id;
    /**
     * 角色名称
     */
    private String name;

    /* 表里都要有的字段*/

    /**
     * 修改时间
     */
    private LocalDateTime mtime;
    /**
     * 修改者
     */
    private Long muser;

    /* 表里都要有的字段*/

}