package com.clsaa.dop.server.test.controller;

import com.clsaa.dop.server.test.model.dto.ManualCaseDto;
import com.clsaa.dop.server.test.model.param.ManualCaseParam;
import com.clsaa.dop.server.test.service.create.ManualCaseCreateService;
import com.clsaa.dop.server.test.service.query.ManualCaseQueryService;
import com.clsaa.rest.result.Pagination;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author xihao
 * @version 1.0
 * @since 27/03/2019
 */
@RestController
@CrossOrigin
@RequestMapping("/manualCases")
public class ManualCaseController {

    @Autowired
    private ManualCaseCreateService manualCaseCreateService;

    @Autowired
    private ManualCaseQueryService manualCaseQueryService;

    @ApiOperation(value = "新增手工测试用例", notes = "创建失败返回null")
    @PostMapping
    public Long createCase(@RequestBody @Valid ManualCaseParam manualCaseParam) {
        return manualCaseCreateService.create(manualCaseParam).orElse(null);
    }

    @ApiOperation(value = "分页获取手工测试用例数据")
    @GetMapping("/page")
    public Pagination<ManualCaseDto> queryCase(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return manualCaseQueryService.selectByPage(pageNo, pageSize);
    }
}
