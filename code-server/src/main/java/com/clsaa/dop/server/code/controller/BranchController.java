package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.model.bo.branch.BranchBo;
import com.clsaa.dop.server.code.model.vo.branch.BranchVo;
import com.clsaa.dop.server.code.service.BranchService;
import com.clsaa.dop.server.code.util.BeanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wsy
 */
@RestController
@CrossOrigin
public class BranchController {

    @Autowired
    private BranchService branchService;


    @ApiOperation(value = "获得项目的分支列表",notes = "根据项目的id获得分支列表")
    @GetMapping("/projects/{username}/{projectname}/repository/branches")
    public List<BranchVo> findBranchList(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                         @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                         @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        List<BranchVo> branchVos=new ArrayList<>();
        List<BranchBo> branchBos=branchService.findBranchList(id,userId);
        for(BranchBo branchBo:branchBos){
            branchVos.add(BeanUtils.convertType(branchBo,BranchVo.class));
        }
        return branchVos;
    }

    @ApiOperation(value = "获得项目的一个分支",notes = "根据项目的id和分支获得一个分支")
    @GetMapping("/projects/{username}/{projectname}/repository/branch")
    public BranchVo findSingleBranch(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                         @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                         @ApiParam(value = "分支名") @RequestParam("branch") String branch,
                                         @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        BranchBo branchBo=branchService.findSingleBranch(id,branch,userId);
        return BeanUtils.convertType(branchBo,BranchVo.class);
    }

    @ApiOperation(value = "创建一个分支",notes = "根据项目的id，新的分支名，源分支创建一个新的分支")
    @PostMapping("/projects/{username}/{projectname}/repository/branches")
    public void addBranch(@ApiParam(value = "用户名") @PathVariable("username") String username,
                          @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                          @ApiParam(value = "新的分支名") @RequestParam("branch") String branch,
                          @ApiParam(value = "创建自分支、标签或者commit SHA") @RequestParam("ref") String ref,
                          @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        branchService.addBranch(id,branch,ref,userId);
    }

    @ApiOperation(value = "删除一个分支",notes = "根据项目的id，分支名删除一个分支")
    @DeleteMapping("/projects/{username}/{projectname}/repository/branches")
    public void deleteBranch(@ApiParam(value = "用户名") @PathVariable("username") String username,
                          @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                          @ApiParam(value = "分支名") @RequestParam("branch") String branch,
                          @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        branchService.deleteBranch(id,branch,userId);
    }

    @ApiOperation(value = "删除所有已经合并到主分支的分支",notes = "根据项目的id删除所有merged分支")
    @DeleteMapping("/projects/{username}/{projectname}/repository/merged_branches")
    public void deleteMergedBranches(@ApiParam(value = "用户名") @PathVariable("username") String username,
                             @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                             @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        branchService.deleteMergedBranches(id,userId);
    }

    @ApiOperation(value = "保护一个分支",notes = "根据项目的id和分支名保护分支，可以设置开发者是否可以push,merge选项")
    @PutMapping("/projects/{username}/{projectname}/repository/branches/protect")
    public void protectBranch(@ApiParam(value = "用户名") @PathVariable("username") String username,
                              @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                              @ApiParam(value = "分支名") @RequestParam("branch") String branch,
                              @ApiParam(value = "开发者是否可以push") @RequestParam("developers_can_push") String developers_can_push,
                              @ApiParam(value = "开发者是否可以merge") @RequestParam("developers_can_merge") String developers_can_merge,
                              @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        branchService.protectBranch(id,branch,developers_can_push,developers_can_merge,userId);
    }

    @ApiOperation(value = "取消保护一个分支",notes = "根据项目的id和分支名取消保护分支")
    @PutMapping("/projects/{username}/{projectname}/repository/branches/unprotect")
    public void unprotectBranch(@ApiParam(value = "用户名") @PathVariable("username") String username,
                              @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                              @ApiParam(value = "分支名") @RequestParam("branch") String branch,
                              @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        branchService.unprotectBranch(id,branch,userId);
    }





}
