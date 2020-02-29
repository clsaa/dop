package com.clsaa.dop.server.code.model.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUpdateDto {
    private String file_path;
    private String branch;
    private String content;
    private String commit_message;
    private Long userId;
}
