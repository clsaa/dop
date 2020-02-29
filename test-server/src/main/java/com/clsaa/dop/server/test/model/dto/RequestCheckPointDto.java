package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.enums.CheckPointOperation;
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
public class RequestCheckPointDto {

    private Long id;

    // ----------- main property ---------
    private String property;

    private CheckPointOperation operation;

    private String value;

    private String message;

    private boolean success = true;

    // ----------- common property ---------
    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;

    public void fail(String message) {
        this.message = message;
        this.success = false;
    }
}
