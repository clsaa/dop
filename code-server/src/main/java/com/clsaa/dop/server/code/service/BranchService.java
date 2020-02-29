package com.clsaa.dop.server.code.service;

import com.clsaa.dop.server.code.model.bo.branch.BranchBo;
import com.clsaa.dop.server.code.model.bo.branch.CommitBo;
import com.clsaa.dop.server.code.util.RequestUtil;
import com.clsaa.dop.server.code.util.TimeUtil;
import com.clsaa.dop.server.code.util.URLUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 分支服务类
 *
 * @author wsy
 */
@Service
public class BranchService {

    /**
     * 查询项目的分支列表
     *
     * @param id     项目id
     * @param userId 用户id
     * @return 项目分支信息的列表
     */
    public List<BranchBo> findBranchList(String id, Long userId) {

        id = URLUtil.encodeURIComponent(id);
        String path = "/projects/" + id + "/repository/branches";

        List<BranchBo> branchBos = RequestUtil.getList(path, userId, BranchBo.class);
        for (BranchBo branchBo : branchBos) {
            CommitBo commitBo = branchBo.getCommit();
            branchBo.setCommit_id(commitBo.getId());
            branchBo.setCommit_short_id(commitBo.getId().substring(0,8));
            branchBo.setCommit_msg(commitBo.getMessage());
            branchBo.setCommit_time(TimeUtil.natureTime(commitBo.getAuthored_date()).get(1));
        }

        return branchBos;

    }

    /**
     * 查询一个分支
     * @param id 项目id
     * @param branch 分支名
     * @param userId 用户id
     * @return 分支信息
     */
    public BranchBo findSingleBranch(String id,String branch,Long userId){

        id = URLUtil.encodeURIComponent(id);
        branch=URLUtil.encodeURIComponent(branch);
        String path = "/projects/" + id + "/repository/branches/"+branch;

        BranchBo branchBo=RequestUtil.get(path,userId,BranchBo.class);
        CommitBo commitBo = branchBo.getCommit();
        branchBo.setCommit_id(commitBo.getId());
        branchBo.setCommit_short_id(commitBo.getId().substring(0,8));
        branchBo.setCommit_msg(commitBo.getMessage());
        branchBo.setCommit_time(TimeUtil.natureTime(commitBo.getAuthored_date()).get(1));

        return branchBo;
    }

    /**
     * 创建一个分支
     * @param id 项目id
     * @param branch 新的分支名称
     * @param ref 创建自分支、标签或者commit SHA
     */
    public void addBranch(String id,String branch,String ref,Long userId){

        id = URLUtil.encodeURIComponent(id);
        String path="/projects/"+id+"/repository/branches";

        List<NameValuePair> params=new ArrayList<>();
        params.add(new BasicNameValuePair("branch",branch));
        params.add(new BasicNameValuePair("ref",ref));
        RequestUtil.post(path,userId,params);

    }

    /**
     * 删除一个分支
     * @param id 项目id
     * @param branch 分支名
     * @param userId 用户id
     */
    public void deleteBranch(String id,String branch,Long userId){

        id = URLUtil.encodeURIComponent(id);
        branch = URLUtil.encodeURIComponent(branch);
        String path="/projects/"+id+"/repository/branches/"+branch;

        RequestUtil.delete(path,userId);

    }

    /**
     * 删除所有已经合并到主分支的分支
     * @param id 项目id
     * @param userId 用户id
     */
    public void deleteMergedBranches(String id,Long userId){

        id = URLUtil.encodeURIComponent(id);
        String path="/projects/"+id+"/repository/merged_branches";

        RequestUtil.delete(path,userId);
    }


    /**
     * 保护一个分支
     * @param id 项目id
     * @param branch 分支名
     * @param developers_can_push 开发者是否可以push到该分支
     * @param developers_can_merge 开发者是否可以merge到该分支
     * @param userId 用户id
     */
    public void protectBranch(String id,String branch,String developers_can_push,String developers_can_merge,Long userId){

        id=URLUtil.encodeURIComponent(id);
        branch=URLUtil.encodeURIComponent(branch);
        String path="/projects/"+id+"/repository/branches/"+branch+"/protect";

        List<NameValuePair> params=new ArrayList<>();
        params.add(new BasicNameValuePair("developers_can_push",developers_can_push));
        params.add(new BasicNameValuePair("developers_can_merge",developers_can_merge));
        RequestUtil.put(path,userId,params);

    }

    /**
     * 取消保护一个分支
     * @param id 项目id
     * @param branch 分支名
     * @param userId 用户id
     */
    public void unprotectBranch(String id,String branch,Long userId){

        id=URLUtil.encodeURIComponent(id);
        branch=URLUtil.encodeURIComponent(branch);
        String path="/projects/"+id+"/repository/branches/"+branch+"/unprotect";

        List<NameValuePair> params=new ArrayList<>();
        RequestUtil.put(path,userId,params);
    }

}
