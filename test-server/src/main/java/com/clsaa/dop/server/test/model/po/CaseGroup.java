package com.clsaa.dop.server.test.model.po;

import com.clsaa.dop.server.test.enums.ExecuteWay;
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
 * @since 06/05/2019
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "case_group", schema = "db_dop_test"
        ,
        uniqueConstraints = {@UniqueConstraint(columnNames = {"app_id", "group_name"})},
        indexes = {@Index(columnList = "app_id,group_name", unique = true)}
        )
public class CaseGroup implements Po{

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "app_id")
    private Long appId;

    @Enumerated(EnumType.STRING)
    @Column(name = "execute_way")
    private ExecuteWay executeWay;

    private String comment;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "case_group_id", referencedColumnName = "id"
            ,foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @Fetch(FetchMode.SUBSELECT)
    private List<CaseUnit> caseUnits;

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
