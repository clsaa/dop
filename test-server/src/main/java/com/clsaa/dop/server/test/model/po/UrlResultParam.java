package com.clsaa.dop.server.test.model.po;

import com.clsaa.dop.server.test.enums.ParamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 04/03/2019
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "url_result_param", schema = "db_dop_test",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"request_id", "name"})},
        indexes = {@Index(columnList = "request_id,name", unique = true)})
public class UrlResultParam implements Po {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private ParamType paramType;

    private String name;

    @Column(name = "raw_value")
    private String rawValue;

    @Column(name = "param_desc")
    private String paramDesc;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private RequestScript requestScript;

    // ------ common property ------------
    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    @Column(name = "is_deleted")
    private boolean deleted;
}
