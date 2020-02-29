package com.clsaa.dop.server.image.controller;

import com.clsaa.dop.server.image.model.bo.ImageInfoBO;
import com.clsaa.dop.server.image.model.vo.ImageInfoVO;
import com.clsaa.dop.server.image.service.ImageService;
import com.clsaa.dop.server.image.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 镜像信息的控制器
 * @author xzt
 * @since 2019-4-6
 */
@RestController
@CrossOrigin
public class ImageInfoController {
    private final ImageService imageService;

    @Autowired
    public ImageInfoController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/v1/repositories/{projectName}/{repoName}/images")
    @ApiOperation(value = "获取镜像信息",notes = "通过项目名称，镜像名，注意是没有/的，不存在返回null")
    public Pagination<ImageInfoVO> getImages(@ApiParam(value = "项目名称",required = true) @PathVariable(value = "projectName") String projectName,
                                             @ApiParam(value = "镜像仓库名称",required = true) @PathVariable(value = "repoName") String repoName,
                                             @ApiParam(value = "版本名称") @RequestParam(value = "tag",required = false) String tag,
                                             @ApiParam(value = "标签") @RequestParam(value = "labels",required = false) String labels,
                                             @ApiParam(value = "页号",required = true) @RequestParam(value = "pageNo")Integer pageNo,
                                             @ApiParam(value = "页大小",required = true) @RequestParam(value = "pageSize") Integer pageSize,
                                             @ApiParam(value = "用户id",required = true) @RequestHeader(value = "x-login-user") Long userId){
        Pagination<ImageInfoBO> pagination = imageService.getImages(pageNo,pageSize,tag,projectName,repoName,labels,userId);
        Pagination<ImageInfoVO> pagination1 = new Pagination<>();
        pagination1.setTotalCount(pagination.getTotalCount());
        pagination1.setPageNo(pageNo);
        pagination1.setPageSize(pageSize);
        pagination1.setPageList(BeanUtils.convertList(pagination.getPageList(),ImageInfoVO.class));
        return pagination1;
    }

    @ApiOperation(value = "获取镜像信息")
    @GetMapping(value = "/v1/repositories/{projectName}/{repoName}/images/{imageName}")
    public ImageInfoVO getImage(@ApiParam(value = "项目名称",required = true) @PathVariable(value = "projectName")String projectName,
                                @ApiParam(value = "镜像仓库名称",required = true) @PathVariable(value = "repoName") String repoName,
                                @ApiParam(value = "镜像名称",required = true) @PathVariable(value = "imageName") String imageName,
                                @ApiParam(value = "用户id",required = true) @RequestHeader(value = "x-login-user") Long userId){
        return BeanUtils.convertType(imageService.getImage(projectName,repoName,imageName,userId),ImageInfoVO.class);
    }


    @ApiOperation(value = "删除对应的镜像")
    @DeleteMapping(value = "/v1/repositories/{projectName}/{repoName}/images/{imageName}")
    public void deleteImage(@ApiParam(value = "项目名称",required = true) @PathVariable(value = "projectName")String projectName,
                            @ApiParam(value = "镜像仓库名称",required = true) @PathVariable(value = "repoName") String repoName,
                            @ApiParam(value = "镜像名称",required = true) @PathVariable(value = "imageName") String imageName,
                            @ApiParam(value = "用户id",required = true) @RequestHeader(value = "x-login-user") Long userId){
        imageService.deleteImage(projectName,repoName,imageName,userId);
    }
}
