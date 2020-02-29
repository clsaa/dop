package com.clsaa.dop.server.test.model.po;

import com.clsaa.dop.server.test.enums.HttpMethod;
import com.clsaa.dop.server.test.enums.OperationType;
import lombok.AllArgsConstructor;
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
 * @since 09/03/2019
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "request_script", schema = "db_dop_test")
public class RequestScript implements Po {

    // ----------- main property ---------
    @Column(name = "raw_url")
    private String rawUrl;

    @Enumerated(value = EnumType.STRING)
    private HttpMethod httpMethod;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @Fetch(FetchMode.SUBSELECT)
    private List<RequestHeader> requestHeaders;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @Fetch(FetchMode.SUBSELECT)
    private List<RequestParam> requestParams;

    @Lob
    @Column(name = "request_body")
    private String requestBody;

    // check point concerned
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id", referencedColumnName = "id"
            ,foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @Fetch(FetchMode.SUBSELECT)
    private List<RequestCheckPoint> requestCheckPoints;

    @Column(name = "retry_times")
    private int retryTimes;

    @Column(name = "retry_interval")
    private Long retryInterval;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id", referencedColumnName = "id"
            ,foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @Fetch(FetchMode.SUBSELECT)
    private List<UrlResultParam> resultParams;

    @Enumerated(value = EnumType.STRING)
    private OperationType operationType;

    @Column(name = "operation_order")
    private int order;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private InterfaceStage interfaceStage;

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
