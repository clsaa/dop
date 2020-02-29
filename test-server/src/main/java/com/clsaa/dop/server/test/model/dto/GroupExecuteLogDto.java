package com.clsaa.dop.server.test.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 08/05/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupExecuteLogDto {

    private Long groupId;

    private String executeInfo;

    private String jenkinsInfo;

    private List<InterfaceExecuteLogDto> logs;

    private String createUserName;

    // ----------- common property ---------
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;
}
