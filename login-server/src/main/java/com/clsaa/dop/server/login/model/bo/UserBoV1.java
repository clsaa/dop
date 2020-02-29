package com.clsaa.dop.server.login.model.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 用户业务层对象
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-12-23
 */
@Getter
@Setter
@ToString
public class UserBoV1 {
    /**
     * 用户类型枚举
     *
     * @author 任贵杰
     */
    public enum Status {

        /**
         * 正常的
         */
        NORMAL("NORMAL"),
        /**
         * 禁止的
         */
        FORBIDDEN("FORBIDDEN"),
        ;

        private String code;

        Status(String code) {
        }
    }
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户email地址
     */
    private String email;
    /**
     * 用户头像URL
     */
    private String avatarURL;
    /**
     * 用户状态
     */
    private Status status;
}