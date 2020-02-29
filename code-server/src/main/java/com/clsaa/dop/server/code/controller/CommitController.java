package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.model.bo.commit.CommitBo;
import com.clsaa.dop.server.code.model.bo.commit.CommitInfoBo;
import com.clsaa.dop.server.code.model.bo.commit.DiffBo;
import com.clsaa.dop.server.code.model.vo.commit.CommitInfoVo;
import com.clsaa.dop.server.code.model.vo.commit.CommitVo;
import com.clsaa.dop.server.code.model.vo.commit.DiffVo;
import com.clsaa.dop.server.code.service.CommitService;
import com.clsaa.dop.server.code.util.BeanUtils;
import com.clsaa.dop.server.code.util.RequestUtil;
import com.clsaa.dop.server.code.util.URLUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wsy
 */
@RestController
@CrossOrigin
public class CommitController {

    @Autowired
    private CommitService commitService;

    @ApiOperation(value = "查看项目的提交列表",notes = "根据项目的id查看项目的提交列表")
    @GetMapping("/projects/{username}/{projectname}/repository/commits")
    public List<CommitVo> findCommitList(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                         @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                         @ApiParam(value = "路径") @RequestParam("path")String path,
                                         @ApiParam(value = "分支名或tag名")@RequestParam("ref_name")String ref_name,
                                         @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        List<CommitVo> commitVos=new ArrayList<>();
        List<CommitBo> commitBos = commitService.findCommitList(id,path,ref_name,userId);
        for(CommitBo commitBo:commitBos){
            commitVos.add(BeanUtils.convertType(commitBo,CommitVo.class));
        }
        return commitVos;
    }


    @ApiOperation(value = "查看项目的某一次具体提交git diff",notes = "根据项目的id和提交的commit sha查看commit的git diff内容")
    @GetMapping("/projects/{username}/{projectname}/repository/commit/diff")
    public List<DiffVo> findDiff(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                 @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                 @ApiParam(value = "commit sha") @RequestParam("sha") String sha,
                                 @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        List<DiffBo> diffBos=commitService.findDiff(id,sha,userId);
        List<DiffVo> diffVos=new ArrayList<>();
        for(DiffBo diffBo:diffBos){
            diffVos.add(BeanUtils.convertType(diffBo,DiffVo.class));
        }
        return diffVos;
    }

    @ApiOperation(value = "查看项目的某一次具体提交统计信息",notes = "根据项目的id和提交的commit sha查看commit的统计信息")
    @GetMapping("/projects/{username}/{projectname}/repository/commit")
    public CommitInfoVo findCommitInfo(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                       @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                       @ApiParam(value = "commit sha") @RequestParam("sha") String sha,
                                       @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        return BeanUtils.convertType(commitService.findCommitInfo(id,sha,userId),CommitInfoVo.class);

    }


}
