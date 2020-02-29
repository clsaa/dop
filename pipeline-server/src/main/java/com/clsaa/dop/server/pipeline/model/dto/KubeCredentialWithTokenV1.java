package com.clsaa.dop.server.pipeline.model.dto;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KubeCredentialWithTokenV1 {
    /**
     * 目标集群Url
     */

    private String targetClusterUrl;

    private String targetClusterToken;
}
