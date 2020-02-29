package com.clsaa.dop.server.pipeline.model.vo;

import com.clsaa.dop.server.pipeline.model.po.Step;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepVoV1 {

    private int taskName;

    private String gitUrl;

    private String dockerUserName;

    private String dockerPassword;

    private String repositoryVersion;

    private String repository;

    private String deploy;

    private String token;

    private String ip;

    private String shell;
}
