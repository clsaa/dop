package com.clsaa.dop.server.user.config;

import com.clsaa.rest.result.bizassert.BizCode;

/**
 * @author 任贵杰
 */
public interface BizCodes {
    /**
     * 非法请求
     */
    BizCode INVALID_PARAM = new BizCode(1000, "请求参数非法");
    /**
     * 非法请求
     */
    BizCode NOT_FOUND = new BizCode(1001, "数据不存在");
    /**
     * 数据库插入失败
     */
    BizCode ERROR_INSERT = new BizCode(1010, "新增失败");
    /**
     * 数据库删除失败
     */
    BizCode ERROR_DELETE = new BizCode(1011, "删除失败");
    /**
     * 数据库更新失败
     */
    BizCode ERROR_UPDATE = new BizCode(1012, "更新失败");
    /**
     * 用户邮箱已注册
     */
    BizCode REPETITIVE_USER_EMAIL = new BizCode(13100, "注册失败，此邮箱已注册");
    /**
     * 激活通道已失效
     */
    BizCode EXPIRED_REGISTER_CODE = new BizCode(13101, "注册失败，激活通道已失效");
    /**
     * 用户名密码错误
     */
    BizCode INVALID_PASSWORD = new BizCode(13102, "用户名密码错误");
    /**
     * 用户名已注册
     */
    BizCode REPETITIVE_USER_NAME = new BizCode(13103, "注册失败，此用户名已注册");
    /**
     * 该凭证不允许被支持此操作
     */
    BizCode INVALID_OPERATION_FOR_CREDENTIAL = new BizCode(13103, "该凭证不允许被支持此操作");
}