package com.clsaa.dop.server.test.model.po;

import com.clsaa.dop.server.test.enums.CaseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 30/03/2019
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "interface_execute_log", schema = "db_dop_test",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"case_type", "case_id", "begin"})},
        indexes = {@Index(columnList = "case_type,case_id,begin", unique = true)})
public class InterfaceExecuteLog implements Po {

    @Enumerated(EnumType.STRING)
    @Column(name = "case_type")
    private CaseType caseType;

    @Column(name = "jenkins_info")
    private String jenkinsInfo;

    @Column(name = "execute_info")
    private String executeInfo;

    private Boolean success;

    private LocalDateTime begin;

    private LocalDateTime end;

    @Column(name = "case_id")
    private Long caseId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "case_log_id", referencedColumnName = "id"
            ,foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @Fetch(FetchMode.SUBSELECT)
    private List<OperationExecuteLog> operationExecuteLogs;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_log_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private GroupExecuteLog groupExecuteLog;

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
