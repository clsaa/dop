package com.clsaa.dop.server.application.model.bo;

import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;


/**
 * K8s集群认证信息业务层对象
 *
 * @author Bowen
 * @since 2019-3-23
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KubeCredentialBoV1 {

    private Long id;

    /**
     * 创建者
     */
    private Long cuser;

    /**
     * 修改者
     */
    private Long muser;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 修改时间
     */
    private LocalDateTime mtime;

    /**
     * 目标集群Url
     */
    private String targetClusterUrl;

    /**
     * 目标集群Token
     */
    private String targetClusterToken;
}
