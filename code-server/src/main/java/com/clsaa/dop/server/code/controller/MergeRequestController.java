package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.model.bo.mergeRequest.MergeRequestBo;
import com.clsaa.dop.server.code.model.vo.mergeRequest.MergeRequestVo;
import com.clsaa.dop.server.code.service.MergeRequestService;
import com.clsaa.dop.server.code.util.BeanUtils;
import com.clsaa.dop.server.code.util.RequestUtil;
import com.clsaa.dop.server.code.util.URLUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wsy
 */
@RestController
@CrossOrigin
public class MergeRequestController {

    @Autowired
    private MergeRequestService mergeRequestService;


    @ApiOperation(value = "获得项目的合并请求列表",notes = "根据项目id，合并请求的状态获得项目的合并请求列表")
    @GetMapping("/projects/{username}/{projectname}/merge_requests")
    public List<MergeRequestVo> findMRList(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                           @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                           @ApiParam(value = "状态") @RequestParam("state") String state,
                                           @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        List<MergeRequestBo> mergeRequestBos = mergeRequestService.findMRList(id,state,userId);
        List<MergeRequestVo> mergeRequestVos = new ArrayList<>();
        for(MergeRequestBo mergeRequestBo:mergeRequestBos){
            mergeRequestVos.add(BeanUtils.convertType(mergeRequestBo,MergeRequestVo.class));
        }
        return mergeRequestVos;
    }

    @ApiOperation(value = "获得项目的某个合并请求",notes = "根据项目id，合并请求id获得项目的合并请求")
    @GetMapping("/projects/{username}/{projectname}/merge_requests/{iid}")
    public MergeRequestVo findSingleMR(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                       @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                       @ApiParam(value = "请求id") @PathVariable("iid") int iid,
                                       @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        MergeRequestBo mergeRequestBo=mergeRequestService.findSingleMR(id,iid,userId);
        return BeanUtils.convertType(mergeRequestBo,MergeRequestVo.class);
    }

    @ApiOperation(value = "创建一个合并请求",notes = "创建合并请求，包括源分支、目标分支、标题和描述信息")
    @PostMapping("/projects/{username}/{projectname}/merge_requests")
    public void createMR(@ApiParam(value = "用户名") @PathVariable("username") String username,
                         @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                         @ApiParam(value = "源分支") @RequestParam("source_branch") String source_branch,
                         @ApiParam(value = "目标分支") @RequestParam("target_branch") String target_branch,
                         @ApiParam(value = "标题") @RequestParam("title") String title,
                         @ApiParam(value = "描述") @RequestParam("description") String description,
                         @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        mergeRequestService.createMR(id,source_branch,target_branch,title,description,userId);
    }

    @ApiOperation(value = "修改一个合并请求",notes = "close或reopen一个合并请求")
    @PutMapping("/projects/{username}/{projectname}/merge_requests/{iid}")
    public void updateMR(@ApiParam(value = "用户名") @PathVariable("username") String username,
                         @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                         @ApiParam(value = "请求id") @PathVariable("iid") int iid,
                         @ApiParam(value = "改变的状态") @RequestParam("state_event") String state_event,
                         @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        mergeRequestService.updateMR(id,iid,state_event,userId);
    }

    @ApiOperation(value = "通过一个合并请求",notes = "通过一个合并请求")
    @PutMapping("/projects/{username}/{projectname}/merge_requests/{iid}/merge")
    public void acceptMR(@ApiParam(value = "用户名") @PathVariable("username") String username,
                         @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                         @ApiParam(value = "请求id") @PathVariable("iid") int iid,
                         @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        mergeRequestService.acceptMR(id,iid,userId);
    }







}
