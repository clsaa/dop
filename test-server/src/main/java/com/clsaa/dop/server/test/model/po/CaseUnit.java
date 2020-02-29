package com.clsaa.dop.server.test.model.po;

import com.clsaa.dop.server.test.enums.CaseResultEnum;
import com.clsaa.dop.server.test.enums.CaseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 06/05/2019
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "case_unit", schema = "db_dop_test"
//        ,
//        uniqueConstraints = {@UniqueConstraint(columnNames = {"case_id", "case_type"})},
//        indexes = {@Index(columnList = "case_id,case_type", unique = true)}
        )
public class CaseUnit implements Po {

    // 类型
    @Enumerated(EnumType.STRING)
    @Column(name = "case_type")
    private CaseType caseType;

    @Column(name = "case_id")
    private Long caseId;

    private String caseName;

    // 执行结果
//    @Enumerated(EnumType.STRING)
//    private CaseResultEnum result;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_group_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private CaseGroup caseGroup;

    // ----------- common property ---------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    @Column(name = "is_deleted")
    private boolean deleted;
}
