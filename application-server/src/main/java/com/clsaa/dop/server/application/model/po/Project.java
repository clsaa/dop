package com.clsaa.dop.server.application.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 项目实体类
 *
 * @author Bowen
 * @version v2
 * @summary 项目实体类
 * @since 2019-3-1
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update project set is_deleted = true where id = ?")
@Where(clause = "is_deleted =false")
public class Project implements Serializable {

    private static final long serialVersionUID = 6906097418517275444L;
    /**
     * ID
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 项目名称
     */
    @Column(nullable = false, name = "title")
    private String title;

    /**
     * 项目创建者
     */
    @Column(nullable = false, name = "cuser")
    private Long cuser;

    /**
     * 项目修改者
     */
    @Column(nullable = false, name = "muser")
    private Long muser;

    /**
     * 创建时间
     */
    @Column(nullable = false, name = "ctime")
    private LocalDateTime ctime;

    /**
     * 修改时间
     */
    @Column(nullable = false, name = "mtime")
    private LocalDateTime mtime;

    /**
     * 组织ID
     */
    @Column(nullable = false, name = "organization_id")
    private Long organizationId;

    //public enum PrivateStatus {
    //
    //    /**
    //     * 正常的
    //     */
    //    NORMAL("NORMAL"),
    //    /**
    //     * 结束的
    //     */
    //    FINISHED("FINISHED"),
    //    ;
    //
    //    private String code;
    //
    //    Status(String code) {
    //    }
    //};


    /**
     * 项目状态
     */
    @Column(nullable = false, name = "private_status")
    @Enumerated(EnumType.STRING)
    private PrivateStatus privateStatus;
    ;

    public enum Status {

        /**
         * 公开的
         */
        NORMAL("NORMAL"),
        /**
         * 私人的
         */
        FINISHED("NORMAL"),
        ;

        private String code;

        Status(String code) {
        }
    }

    ;
    /**
     * 是否删除
     */
    @Column(nullable = false, name = "is_deleted")
    private boolean is_deleted;

    /**
     * 项目状态
     */
    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum PrivateStatus {

        /**
         * 公开的
         */
        PUBLIC("PUBLIC"),
        /**
         * 私人的
         */
        PRIVATE("PRIVATE"),
        ;

        private String code;

        PrivateStatus(String code) {
        }
    }

    /**
     * 项目描述
     */
    @Column(name = "description")
    private String description;

}
