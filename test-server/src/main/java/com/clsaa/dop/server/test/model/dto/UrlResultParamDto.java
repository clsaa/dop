package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.enums.ParamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlResultParamDto {

    private Long id;

    private ParamType paramType;

    private String name;

    private String rawValue;

    private String paramDesc;

    // ------ common property ------------
    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;
}
