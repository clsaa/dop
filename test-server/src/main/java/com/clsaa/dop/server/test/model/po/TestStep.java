package com.clsaa.dop.server.test.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test_step", schema = "db_dop_test",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"case_id", "step_index"})},
        indexes = {@Index(columnList = "case_id,step_index", unique = true)})
public class TestStep implements Po {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step_index")
    private int stepIndex;

    //todo 图片上传支持
    @Column(name = "step_desc")
    private String stepDesc;

    @Column(name = "expect_result")
    private String expectResult;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private ManualCase manualCase;

    // ------ common property ------------
    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    @Column(name = "is_deleted")
    private boolean deleted;
}
