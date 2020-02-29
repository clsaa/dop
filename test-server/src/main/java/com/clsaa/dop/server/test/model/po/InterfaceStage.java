package com.clsaa.dop.server.test.model.po;
import com.clsaa.dop.server.test.enums.Stage;
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
 * Attention!
 * 一个该对象代表的是一个接口测试用例的一个阶段
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "interface_stage", schema = "db_dop_test",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"case_id", "stage"})},
        indexes = {@Index(columnList = "case_id,stage", unique = true)})
public class InterfaceStage implements Po {
    // ----------- main property ---------
    @Enumerated(value = EnumType.STRING)
    private Stage stage;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "stage_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private List<RequestScript> requestScripts;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "stage_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private List<WaitOperation> waitOperations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private InterfaceCase interfaceCase;

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
