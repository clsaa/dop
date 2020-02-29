package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.enums.OperationType;
import com.clsaa.dop.server.test.enums.Stage;
import com.clsaa.dop.server.test.model.po.InterfaceExecuteLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 31/03/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationExecuteLogDto {

    private String executeInfo;

    private Boolean success;

    private int order;

    private Stage stage;

    private OperationType operationType;

    private LocalDateTime begin;

    private LocalDateTime end;

    // ----------- common property ---------
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;
}
