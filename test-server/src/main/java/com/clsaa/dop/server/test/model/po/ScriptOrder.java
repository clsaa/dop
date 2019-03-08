package com.clsaa.dop.server.test.model.po;

import com.clsaa.dop.server.test.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 08/03/2019
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "script_order", schema = "db_dop_test",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"script_id", "operation_order"})},
        indexes = {@Index(columnList = "script_id,operation_order", unique = true)})
public class ScriptOrder {

    @Column(name = "script_id")
    private Long scriptId;

    @Column(name = "operation_order")
    private int order;

    @Column(name = "operation_id")
    private Long operationId;

    @Enumerated(value = EnumType.STRING)
    private OperationType type;

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