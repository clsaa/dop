package com.clsaa.dop.server.message.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 电子邮件传输层类
 *
 * @author joyren
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDtoV1 implements Serializable {
    private String from;
    private String to;
    private String subject;
    private String text;
}
