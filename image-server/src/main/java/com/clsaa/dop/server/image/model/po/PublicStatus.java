package com.clsaa.dop.server.image.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 修改项目公开状态的类
 * @author xzt
 * @since 2019-4-5
 */
@Setter
@Getter
public class PublicStatus {
    @JsonProperty("public")
    private String publicStatus;
}
