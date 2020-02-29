package com.clsaa.dop.server.test.model.po;

import com.clsaa.dop.server.test.enums.CaseResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 05/03/2019
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "case_result", schema = "db_dop_test",
        indexes = {@Index(columnList = "case_id,rtime", unique = false)})
public class CaseResult implements Po{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_id")
    private Long caseId;

    @Column(name = "ruser_id")
    private Long ruserId;

    private LocalDateTime rtime;

    @Enumerated(EnumType.STRING)
    private CaseResultEnum status;

    private String comment;

    // ----------- common property ---------
    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    @Column(name = "is_deleted")
    private boolean deleted;
}
