package com.clsaa.dop.server.test.controller;

import com.clsaa.dop.server.test.doExecute.TestManager;
import com.clsaa.dop.server.test.enums.CaseType;
import com.clsaa.dop.server.test.model.dto.CaseGroupDto;
import com.clsaa.dop.server.test.model.dto.CaseUnitDto;
import com.clsaa.dop.server.test.model.dto.GroupExecuteLogDto;
import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.param.CaseGroupParam;
import com.clsaa.dop.server.test.service.create.CaseGroupCreateService;
import com.clsaa.dop.server.test.service.query.CaseGroupQueryService;
import com.clsaa.dop.server.test.service.query.GroupLogQueryService;
import com.clsaa.dop.server.test.service.query.InterfaceCaseQueryService;
import com.clsaa.dop.server.test.service.update.CaseGroupUpdateService;
import com.clsaa.rest.result.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 测试分组相关接口
 * @author xihao
 * @version 1.0
 * @since 06/05/2019
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private CaseGroupCreateService caseGroupCreateService;

    @Autowired
    private CaseGroupUpdateService caseGroupUpdateService;

    @Autowired
    private CaseGroupQueryService caseGroupQueryService;

    @Autowired
    private InterfaceCaseQueryService interfaceCaseQueryService;

    @Autowired
    private GroupLogQueryService groupLogQueryService;

    @PostMapping
    public Long createGroup(@RequestBody CaseGroupParam param) {
        return caseGroupCreateService.create(param).orElse(null);
    }

    @PutMapping
    public Boolean updateGroup(@RequestBody CaseGroupDto caseGroupDto) {
        caseGroupUpdateService.update(caseGroupDto);
        return Boolean.TRUE;
    }

    @GetMapping("/page")
    public Pagination<CaseGroupDto> queryGroup(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return caseGroupQueryService.selectByPage(pageNo, pageSize);
    }

    @GetMapping("/{groupId}")
    public CaseGroupDto queryGroupById(@PathVariable("groupId") Long groupId) {
        return caseGroupQueryService.selectByIds(groupId).orElse(null);
    }

    @GetMapping("/execute/{groupId}")
    public Boolean executeGroup(@PathVariable("groupId") Long groupId) {
        CaseGroupDto caseGroupDto = caseGroupQueryService.selectByIds(groupId).orElse(null);
        if (caseGroupDto == null || CollectionUtils.isEmpty(caseGroupDto.getCaseUnits())) {
            return Boolean.FALSE;
        }
        List<Long> interfaceCaseIds = caseGroupDto.getCaseUnits()
                .stream()
                .filter(unit -> unit.getCaseType() == CaseType.INTERFACE)
                .map(CaseUnitDto::getCaseId)
                .collect(Collectors.toList());
        List<InterfaceCaseDto> interfaceCaseDtos = interfaceCaseQueryService.selectByIds(interfaceCaseIds);
        return TestManager.execute(caseGroupDto, interfaceCaseDtos);
    }

    @GetMapping("/logs/page/{groupId}")
    public Pagination<GroupExecuteLogDto> getGroupLogByGroupId(@PathVariable("groupId") Long groupId,
                                                          @RequestParam("pageNo")int pageNo,
                                                          @RequestParam("pageSize")int pageSize) {

        return groupLogQueryService.getGroupLogs(groupId, pageNo, pageSize);
    }

    @GetMapping("/logs/{id}")
    public GroupExecuteLogDto getGroupLogById(@PathVariable("id") Long id) {
        return groupLogQueryService.selectByIds(id).orElse(null);
    }
}
