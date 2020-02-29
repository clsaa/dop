package com.clsaa.dop.server.application.model.vo;

import lombok.*;

import javax.persistence.Column;

/**
 * 变量视图对象
 *
 * @author Bowen
 * @since 2019-3-12
 **/
@Getter
@Setter
public class AppVarV1 {

    private Long id;
    /**
     * 键
     */
    private String varKey;
}
