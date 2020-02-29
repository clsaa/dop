package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.model.bo.tag.TagBo;
import com.clsaa.dop.server.code.model.vo.tag.TagVo;
import com.clsaa.dop.server.code.service.TagService;
import com.clsaa.dop.server.code.util.BeanUtils;
import com.clsaa.dop.server.code.util.RequestUtil;
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
public class TagController {

    @Autowired
    private TagService tagService;

    @ApiOperation(value = "获得项目的标签列表",notes = "根据项目的id获得标签列表")
    @GetMapping("/projects/{username}/{projectname}/repository/tags")
    public List<TagVo> findTagList(@ApiParam(value = "用户名") @PathVariable("username") String username,
                                      @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                                      @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        List<TagVo> tagVos=new ArrayList<>();
        List<TagBo> tagBos=tagService.findTagList(id,userId);
        for(TagBo tagBo:tagBos){
            tagVos.add(BeanUtils.convertType(tagBo,TagVo.class));
        }
        return tagVos;
    }

    @ApiOperation(value = "删除一个标签",notes = "根据项目的id，标签名删除一个标签")
    @DeleteMapping("/projects/{username}/{projectname}/repository/tags")
    public void deleteTag(@ApiParam(value = "用户名") @PathVariable("username") String username,
                             @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                             @ApiParam(value = "标签名") @RequestParam("tag_name") String tag_name,
                             @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        tagService.deleteTag(id,tag_name,userId);
    }

    @ApiOperation(value = "新建一个标签",notes = "根据项目id、标签名、ref和message创建一个标签")
    @PostMapping("/projects/{username}/{projectname}/repository/tags")
    public void addTag(@ApiParam(value = "用户名") @PathVariable("username") String username,
                       @ApiParam(value = "项目名") @PathVariable("projectname") String projectname,
                       @ApiParam(value = "标签名") @RequestParam("tag_name") String tag_name,
                       @ApiParam(value = "创建自分支或标签") @RequestParam("ref") String ref,
                       @ApiParam(value = "标签信息") @RequestParam("message") String message,
                       @ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){
        String id=username+"/"+projectname;
        tagService.addTag(id,tag_name,ref,message,userId);
    }




}
