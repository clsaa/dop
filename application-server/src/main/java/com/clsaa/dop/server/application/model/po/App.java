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
 * 应用实体类
 *
 * @author ZhengBowen
 * @version v1
 * @summary 应用实体类
 * @since 2019-3-7
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update app set is_deleted = true where id = ?")
@Where(clause = "is_deleted =false")
public class App implements Serializable {

    private static final long serialVersionUID = 6906097418517275446L;
    /**
     * ID
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 应用所属的项目ID
     */
    @Column(nullable = false, name = "project_id")
    private Long projectId;

    /**
     * 创建人
     */
    @Column(nullable = false, name = "cuser")
    private Long cuser;

    /**
     * 修改人
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
     * 名称
     */
    @Column(nullable = false, name = "title")
    private String title;

    //拥有者
    @Column(nullable = false, name = "ouser")
    private Long ouser;


    //应用描述
    @Column(nullable = true, name = "description")
    private String description;

    /**
     * 是否删除
     */
    @Column(nullable = false, name = "is_deleted")
    private boolean is_deleted;

    /**
     * 开发模式
     */
    @Column(name = "product_mode")
    @Enumerated(EnumType.STRING)
    private App.ProductMode productMode;


    public enum ProductMode {

        /**
         * 分支模式
         */
        BRANCH("BRANCH"),
        /**
         * 自由模式
         */
        FREE("FREE"),
        ;

        private String code;

        ProductMode(String code) {
            this.code = code;
        }
    }

    ;

}
