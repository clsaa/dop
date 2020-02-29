package com.clsaa.dop.server.application.model.vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户名称视图层对象
 * </p>
 *
 * @author 郑博文
 * @since 2019-4-13
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserNameV1 {

    /**
     * 用户姓名
     */
    private String name;

}
