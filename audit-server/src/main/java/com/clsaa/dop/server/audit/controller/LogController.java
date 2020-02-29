package com.clsaa.dop.server.audit.controller;

import com.clsaa.dop.server.audit.config.AuditProperties;
import com.clsaa.dop.server.audit.config.BizCodes;
import com.clsaa.dop.server.audit.model.dto.LogDtoV1;
import com.clsaa.dop.server.audit.model.vo.LogV1;
import com.clsaa.dop.server.audit.service.LogService;
import com.clsaa.rest.result.Pagination;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志接口实现类
 *
 * @author joyren
 */
@RestController
@CrossOrigin
public class LogController {

    @Autowired
    private LogService logService;
    @Autowired
    private AuditProperties auditProperties;

    @ApiOperation(value = "添加操作日志", notes = "添加操作日志")
    @PostMapping("/v1/logs")
    public void addLogV1(@RequestBody LogDtoV1 logDtoV1) {
        BizAssert.validParam(StringUtils.isNotEmpty(logDtoV1.getServiceId()), BizCodes.INVALID_PARAM);
        BizAssert.validParam(this.auditProperties.getSupportedServices().contains(logDtoV1.getServiceId()),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "服务id错误，服务必须经过配置后才能接入审计服务"));
        BizAssert.validParam(StringUtils.isNotEmpty(logDtoV1.getFunctionId()), BizCodes.INVALID_PARAM);
        BizAssert.validParam(StringUtils.isNotEmpty(logDtoV1.getUserId()), BizCodes.INVALID_PARAM);
        this.logService.addLog(logDtoV1.getServiceId(), logDtoV1.getFunctionId(), logDtoV1.getUserId(),
                logDtoV1.getValues(), logDtoV1.getOtime());
    }

    @ApiOperation(value = "查询操作日志", notes = "根据服务id、功能id，用户id分页查询操作日志。用户id不传入，则查询此功能全部用户的操作日志")
    @GetMapping("/v1/logs")
    public Pagination<LogV1> findLogV1(@ApiParam(value = "服务id") @RequestParam("serviceId") String serviceId,
                                       @ApiParam(value = "功能id") @RequestParam("functionId") String functionId,
                                       @ApiParam(value = "用户id") @RequestParam(value = "userId", required = false, defaultValue = "") String userId,
                                       @ApiParam(value = "页号") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                       @ApiParam(value = "页大小") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        BizAssert.validParam(StringUtils.isNotEmpty(serviceId), BizCodes.INVALID_PARAM);
        BizAssert.validParam(this.auditProperties.getSupportedServices().contains(serviceId),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "服务id错误，服务必须经过配置后才能接入审计服务"));
        BizAssert.validParam(StringUtils.isNotEmpty(functionId), BizCodes.INVALID_PARAM);
        return this.logService.findLog(serviceId, functionId, userId, pageNo, pageSize);
    }
}
