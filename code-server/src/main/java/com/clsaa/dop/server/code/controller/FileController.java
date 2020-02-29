package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.model.bo.file.BranchAndTagBo;
import com.clsaa.dop.server.code.model.bo.file.ChildrenBo;
import com.clsaa.dop.server.code.model.bo.file.TreeCommitBo;
import com.clsaa.dop.server.code.model.bo.file.TreeNodeBo;
import com.clsaa.dop.server.code.model.dto.file.FileUpdateDto;
import com.clsaa.dop.server.code.model.vo.file.*;
import com.clsaa.dop.server.code.service.FileService;
import com.clsaa.dop.server.code.util.BeanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wsy
 */
@CrossOrigin
@RestController
public class FileController {

    @Autowired
    private FileService fileService;


    @ApiOperation(value = "查询项目文件结构",notes = "根据项目的id、分支或tag和查找指定路径下的node内容")
    @GetMapping("/projects/{username}/{projectname}/repository/tree/nodes")
    public List<TreeNodeVo> findTreeNodes(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                          @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                          @ApiParam(value = "分支或tag名") @RequestParam("ref")String ref,
                                          @ApiParam(value = "路径") @RequestParam("path")String path,
                                          @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        List<TreeNodeVo> treeNodeVos=new ArrayList<>();

        List<TreeNodeBo> treeNodeBos = fileService.findTreeNodes(id,ref,path,userId);
        for(TreeNodeBo treeNodeBo:treeNodeBos){
            treeNodeVos.add(BeanUtils.convertType(treeNodeBo,TreeNodeVo.class));
        }

        return treeNodeVos;
    }


    @ApiOperation(value = "查询项目文件结构",notes = "根据项目的id、分支或tag和查找指定路径下的commit内容")
    @GetMapping("/projects/{username}/{projectname}/repository/tree/commits")
    public List<TreeCommitVo> findTreeCommits(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                              @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                              @ApiParam(value = "分支或tag名") @RequestParam("ref")String ref,
                                              @ApiParam(value = "路径") @RequestParam("path")String path,
                                              @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        List<TreeCommitVo> treeCommitVos=new ArrayList<>();

        List<TreeCommitBo> treeCommitBos = fileService.findTreeCommits(id,ref,path,userId);
        for(TreeCommitBo treeCommitBo:treeCommitBos){
            treeCommitVos.add(BeanUtils.convertType(treeCommitBo,TreeCommitVo.class));
        }

        return treeCommitVos;
    }

    @ApiOperation(value = "查找项目的分支名和tag名",notes = "根据项目id查找项目的分支名和tag名")
    @GetMapping("/projects/{username}/{projectname}/repository/branchandtag")
    public List<BranchAndTagVo> findBranchAndTag(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                                 @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                                 @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
//        long t1=System.currentTimeMillis();

        List<BranchAndTagBo> bos=fileService.findBranchAndTag(id,userId);
        List<BranchAndTagVo> vos=new ArrayList<>();

        List<ChildrenVo> branches=new ArrayList<>();
        List<ChildrenVo> tags=new ArrayList<>();

        for(ChildrenBo childrenBo:bos.get(0).getChildren()){
            branches.add(BeanUtils.convertType(childrenBo,ChildrenVo.class));
        }

        for(ChildrenBo childrenBo:bos.get(1).getChildren()){
            tags.add(BeanUtils.convertType(childrenBo,ChildrenVo.class));
        }

        BranchAndTagVo vo1=new BranchAndTagVo("branch","branch",branches);
        BranchAndTagVo vo2=new BranchAndTagVo("tag","tag",tags);

        vos.add(vo1);
        vos.add(vo2);

//        long t2=System.currentTimeMillis();
//
//        System.out.println("total:"+(t2-t1));

        return vos;

    }

    @ApiOperation(value = "查找文件内容",notes = "根据项目id，文件路径，ref查找文件内容")
    @GetMapping("/projects/{username}/{projectname}/repository/blob")
    public BlobVo findFileContent(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                  @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                  @ApiParam(value = "文件路径")@RequestParam("file_path") String file_path,
                                  @ApiParam(value = "branch,tag or commit")@RequestParam("ref") String ref,
                                  @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){

        String id=username+"/"+projectname;
        return BeanUtils.convertType(fileService.findFileContent(id,file_path,ref,userId),BlobVo.class) ;

    }

    @ApiOperation(value = "更新文件内容",notes = "更新文件内容，并作为一次提交")
    @PutMapping("/projects/{username}/{projectname}/repository/blob")
    public void updateFile(@ApiParam(value = "用户名") @PathVariable("username") String username,
                           @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                           @ApiParam(value = "文件更新内容") @RequestBody FileUpdateDto fileUpdateDto){
        String id=username+"/"+projectname;
        fileService.updateFile(
                id,
                fileUpdateDto.getFile_path(),
                fileUpdateDto.getBranch(),
                fileUpdateDto.getContent(),
                fileUpdateDto.getCommit_message(),
                fileUpdateDto.getUserId());

    }


    @ApiOperation(value = "删除文件",notes = "删除文件，并作为一次提交")
    @DeleteMapping("/projects/{username}/{projectname}/repository/blob")
    public void deleteFile(@ApiParam(value = "用户名") @PathVariable("username") String username,
                           @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                           @ApiParam(value = "文件路径")@RequestParam("file_path") String file_path,
                           @ApiParam(value = "分支")@RequestParam("branch") String branch,
                           @ApiParam(value = "提交信息")@RequestParam("commit_message") String commit_message,
                           @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){


        String id=username+"/"+projectname;
        fileService.deleteFile(id,file_path,branch,commit_message,userId);

    }

    @ApiOperation(value = "查找项目所有的文件路径",notes = "根据项目id，分支或tag查找项目所有的文件的路径")
    @GetMapping("/projects/{username}/{projectname}/repository/filepathlist")
    public List<String> findAllFilePath(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                        @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                        @ApiParam(value = "分支或tag")@RequestParam("ref") String ref,
                                        @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        return fileService.findAllFilePath(id,ref,userId);
    }



}
