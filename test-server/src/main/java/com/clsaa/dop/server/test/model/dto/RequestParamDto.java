package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.enums.ParamClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 04/05/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestParamDto {

    private Long id;

    // ----------- main property ---------
    private ParamClass paramClass;

    private String name;

    private String value;

    // ----------- common property ---------
    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;
}
