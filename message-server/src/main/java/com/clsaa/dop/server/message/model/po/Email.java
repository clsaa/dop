package com.clsaa.dop.server.message.model.po;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 电子邮件持久层类
 *
 * @author joyren
 */
@Data
@Builder
public class Email implements Serializable {
    public static final int MAX_RETRY = 5;

    public enum Status {
        /**
         * 发送中
         */
        SENDING("SENDING"),
        /**
         * 已发送
         */
        SENT("SENT"),
        /**
         * 已取消
         */
        CANCELED("CANCELED");
        private String status;

        Status(String status) {
            this.status = status;
        }
    }

    @Id
    @Field("id")
    private String id;
    @Field("from")
    private String from;
    @Field("to")
    private String to;
    @Field("subject")
    private String subject;
    @Field("text")
    private String text;
    @Field("retry")
    private Integer retry;
    @Field("ctime")
    private LocalDateTime ctime;
    @Field("mtime")
    private LocalDateTime mtime;
    @Field("status")
    private Status status;
}
