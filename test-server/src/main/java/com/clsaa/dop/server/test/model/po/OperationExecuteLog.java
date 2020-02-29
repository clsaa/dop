package com.clsaa.dop.server.test.model.po;

import com.clsaa.dop.server.test.enums.OperationType;
import com.clsaa.dop.server.test.enums.Stage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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
@Table(name = "operation_log", schema = "db_dop_test",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"case_log_id", "stage", "operation_order"})},
        indexes = {@Index(columnList = "case_log_id,stage,operation_order", unique = true)})
public class OperationExecuteLog implements Po{

    @Lob
    @Column(name = "execute_info")
    private String executeInfo;

    private Boolean success;

    @Column(name = "operation_order")
    private int order;

    @Enumerated(value = EnumType.STRING)
    private Stage stage;

    @Enumerated(value = EnumType.STRING)
    private OperationType operationType;

    @Column(name = "exe_begin")
    private LocalDateTime begin;

    @Column(name = "exe_end")
    private LocalDateTime end;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_log_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private InterfaceExecuteLog interfaceExecuteLog;

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
