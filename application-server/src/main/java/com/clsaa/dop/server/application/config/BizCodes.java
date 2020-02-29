package com.clsaa.dop.server.application.config;

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
     * 没有操作权限
     */
    BizCode NO_PERMISSION = new BizCode(1013, "没有权限");

    /**
     * 数据库更新失败
     */
    BizCode ERROR_ACCESS = new BizCode(1014, "连接失败");
}