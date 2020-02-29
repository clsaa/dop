package com.clsaa.dop.server.image.service;

import com.clsaa.dop.server.image.feign.UserFeign;
import com.clsaa.dop.server.image.feign.harborfeign.HarborRepoFeign;
import com.clsaa.dop.server.image.model.bo.ImageInfoBO;
import com.clsaa.dop.server.image.model.dto.UserCredentialDto;
import com.clsaa.dop.server.image.model.enumtype.UserCredentialType;
import com.clsaa.dop.server.image.model.po.DetailedTag;
import com.clsaa.dop.server.image.util.BasicAuthUtil;
import com.clsaa.dop.server.image.util.BeanUtils;
import com.clsaa.dop.server.image.util.SizeConvertUtil;
import com.clsaa.dop.server.image.util.TimeConvertUtil;
import com.clsaa.rest.result.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ImageService {
    private final HarborRepoFeign harborRepoFeign;

    private final UserFeign userFeign;

    @Autowired
    public ImageService(HarborRepoFeign harborRepoFeign, UserFeign userFeign) {
        this.harborRepoFeign = harborRepoFeign;
        this.userFeign = userFeign;
    }


    /**
     * 获取镜像仓库的镜像信息
     * @param repoName 镜像仓库名称
     * @param labels 标签
     * @param userId 访问用户名
     * @return {@link List<ImageInfoBO>} 镜像信息的list
     */
    public Pagination<ImageInfoBO> getImages(int pageNo,int pageSize,String tag,String projectName, String repoName, String labels, Long userId){
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        String repo = projectName+"/"+repoName;

        ResponseEntity<List<DetailedTag>> responseEntity = harborRepoFeign.repositoriesRepoNameTagsGet(repo,labels,auth);
        List<DetailedTag> detailedTags = responseEntity.getBody();

        int count = 0;
        if(tag==null){
            if (detailedTags!=null){
                count = detailedTags.size();
            }
        }else {
            for (DetailedTag detailedTag:detailedTags){
                if(detailedTag.getName().startsWith(tag)){
                    count++;
                }
            }
        }

        Pagination<ImageInfoBO> pagination = new Pagination<>();
        pagination.setTotalCount(count);
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        int beginIndex = (pageNo-1)*pageSize;
        int endIndex;
        if (pagination.isLastPage()){
            endIndex = count;
        }else {
            endIndex = pageNo*pageSize;
        }

        if (count==0){
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }else {
            if (tag==null){
                List<ImageInfoBO> imageInfoBOS = new ArrayList<>();
                for (DetailedTag detailedTag:detailedTags){
                    ImageInfoBO imageInfo = BeanUtils.convertType(detailedTag,ImageInfoBO.class);
                    imageInfo.setSize(SizeConvertUtil.convertSize(detailedTag.getSize()));
                    imageInfo.setCreated(TimeConvertUtil.convertTime(detailedTag.getCreated()));
                    imageInfoBOS.add(imageInfo);
                }
                pagination.setPageList(imageInfoBOS.subList(beginIndex,endIndex));
                return pagination;
            }else {
                List<ImageInfoBO> imageInfoBOS = new ArrayList<>();
                for (DetailedTag detailedTag:detailedTags){
                    if (detailedTag.getName().startsWith(tag)){
                        ImageInfoBO imageInfo = BeanUtils.convertType(detailedTag,ImageInfoBO.class);
                        imageInfo.setSize(SizeConvertUtil.convertSize(detailedTag.getSize()));
                        imageInfo.setCreated(TimeConvertUtil.convertTime(detailedTag.getCreated()));
                        imageInfoBOS.add(imageInfo);
                    }
                }
                pagination.setPageList(imageInfoBOS.subList(beginIndex,endIndex));
                return pagination;
            }

        }
    }

    /**
     * 通过镜像仓库名和tag名来删除镜像
     * @param projectName 项目名称
     * @param repoName  镜像仓库名称
     * @param imageName 镜像的tag名
     * @param userId   用户id
     */
    public void deleteImage(String projectName,String repoName,String imageName,Long userId){
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        String repo = projectName+"/"+repoName;
        harborRepoFeign.repositoriesRepoNameTagsTagDelete(repo,imageName,auth);
    }

    /**
     * 通过镜像仓库名和tag名来获取镜像
     * @param projectName 项目名称
     * @param repoName 镜像名称
     * @param imageName 镜像版本
     * @param userId 用户id
     * @return {@link ImageInfoBO} 镜像的信息
     */
    public ImageInfoBO getImage(String projectName,String repoName,String imageName,Long userId){
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        String repo = projectName+"/"+repoName;
        return BeanUtils.convertType(harborRepoFeign.repositoriesRepoNameTagsTagGet(repo,imageName,auth),ImageInfoBO.class);
    }
}
