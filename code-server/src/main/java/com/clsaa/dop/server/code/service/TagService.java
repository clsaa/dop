package com.clsaa.dop.server.code.service;

import com.clsaa.dop.server.code.model.bo.tag.CommitBo;
import com.clsaa.dop.server.code.model.bo.tag.TagBo;
import com.clsaa.dop.server.code.util.RequestUtil;
import com.clsaa.dop.server.code.util.TimeUtil;
import com.clsaa.dop.server.code.util.URLUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签服务类
 * @author wsy
 */
@Service
public class TagService {

    /**
     * 查询项目的标签列表
     * @param id 项目id
     * @param userId 用户id
     * @return 标签列表
     */
    public List<TagBo> findTagList(String id,Long userId){

        id = URLUtil.encodeURIComponent(id);
        String path = "/projects/" + id + "/repository/tags";

        List<TagBo> tagBos=RequestUtil.getList(path,userId,TagBo.class);
        for (TagBo tagBo : tagBos) {
            CommitBo commitBo = tagBo.getCommit();
            tagBo.setCommit_id(commitBo.getId());
            tagBo.setCommit_short_id(commitBo.getId().substring(0,8));
            tagBo.setCommit_msg(commitBo.getMessage());
            tagBo.setCommit_time(TimeUtil.natureTime(commitBo.getAuthored_date()).get(1));
        }

        return tagBos;


    }

    /**
     * 删除一个分支
     * @param id 项目id
     * @param tag_name 标签名称
     * @param userId 用户id
     */
    public void deleteTag(String id,String tag_name,Long userId){

        id = URLUtil.encodeURIComponent(id);
        tag_name = URLUtil.encodeURIComponent(tag_name);
        String path="/projects/"+id+"/repository/tags/"+tag_name;

        RequestUtil.delete(path,userId);

    }

    /**
     * 新建一个标签
     * @param id 项目id
     * @param tag_name 标签名
     * @param ref 创建自分支、标签
     * @param message 标签信息
     * @param userId 用户id
     */
    public void addTag(String id,String tag_name,String ref,String message,Long userId){

        id = URLUtil.encodeURIComponent(id);
        String path="/projects/"+id+"/repository/tags";

        List<NameValuePair> params=new ArrayList<>();
        params.add(new BasicNameValuePair("tag_name",tag_name));
        params.add(new BasicNameValuePair("ref",ref));
        params.add(new BasicNameValuePair("message",message));
        RequestUtil.post(path,userId,params);
    }
}
