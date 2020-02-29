package com.clsaa.dop.server.code.service;

import com.clsaa.dop.server.code.model.bo.commit.CommitBo;
import com.clsaa.dop.server.code.model.bo.commit.CommitInfoBo;
import com.clsaa.dop.server.code.model.bo.commit.DiffBo;
import com.clsaa.dop.server.code.util.RequestUtil;
import com.clsaa.dop.server.code.util.TimeUtil;
import com.clsaa.dop.server.code.util.URLUtil;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 提交服务类
 *
 * @author wsy
 */
@Service
public class CommitService {

    /**
     * 查看项目所有提交的列表
     *
     * @param id       项目id
     * @param userId   用户id
     * @param ref_name tag名或分支名
     * @param path     路径(需要urlencode)
     * @return 提交列表
     */
    public List<CommitBo> findCommitList(String id, String path, String ref_name, Long userId){

        id = URLUtil.encodeURIComponent(id);

        String url = "/projects/" + id + "/repository/commits?ref_name=" + ref_name;
        //若不为根目录
        if (!path.equals("/")) {
            url += "&path=" + URLUtil.encodeURIComponent(path);
        }

        List<CommitBo> commitBos = RequestUtil.getList(url, userId, CommitBo.class);


        for (CommitBo commitBo : commitBos) {
            List<String> strs = TimeUtil.natureTime(commitBo.getAuthored_date());
            commitBo.setAuthored_date(strs.get(0));
            commitBo.setAuthored_time(strs.get(1));
        }

        return commitBos;

    }

    /**
     * 查询某次commit的git diff内容
     * @param id 项目id
     * @param sha commit sha
     * @param userId 用户id
     * @return 每个改动文件的git diff信息
     */
    public List<DiffBo> findDiff(String id,String sha,Long userId){

        id=URLUtil.encodeURIComponent(id);
        sha=URLUtil.encodeURIComponent(sha);

        String path="/projects/"+id+"/repository/commits/"+sha+"/diff";

        return RequestUtil.getList(path,userId,DiffBo.class);

    }

    /**
     * 查询commit的统计信息
     * @param id 项目id
     * @param sha commit sha
     * @param userId 用户id
     * @return 统计信息
     */
    public CommitInfoBo findCommitInfo(String id,String sha,Long userId) {

        id = URLUtil.encodeURIComponent(id);
        sha = URLUtil.encodeURIComponent(sha);

        String path = "/projects/" + id + "/repository/commits/" + sha;
        CommitInfoBo commitInfoBo = RequestUtil.get(path, userId, CommitInfoBo.class);
        List<String> strs = TimeUtil.natureTime(commitInfoBo.getAuthored_date());
        commitInfoBo.setAuthored_date(strs.get(0));
        commitInfoBo.setAuthored_time(strs.get(1));
        commitInfoBo.setAdditions(commitInfoBo.getStats().getAdditions());
        commitInfoBo.setDeletions(commitInfoBo.getStats().getDeletions());

        return commitInfoBo;
    }

}
