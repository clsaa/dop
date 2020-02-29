package com.clsaa.dop.server.code.service;

import com.clsaa.dop.server.code.model.bo.mergeRequest.MergeRequestBo;
import com.clsaa.dop.server.code.util.RequestUtil;
import com.clsaa.dop.server.code.util.TimeUtil;
import com.clsaa.dop.server.code.util.URLUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 合并请求服务类
 * @author wsy
 */
@Service
public class MergeRequestService {

    /**
     * 查询项目的合并请求列表
     * @param id 项目id
     * @param userId 用户id
     * @param state 合并请求状态
     * @return 合并请求列表
     */
    public List<MergeRequestBo> findMRList(String id,String state,Long userId){
        id = URLUtil.encodeURIComponent(id);
        String path="/projects/"+id+"/merge_requests?state="+state+"&order_by=updated_at";
        List<MergeRequestBo> mergeRequestBos=RequestUtil.getList(path,userId,MergeRequestBo.class);
        for(MergeRequestBo mergeRequestBo:mergeRequestBos){
            mergeRequestBo.setCreated_by(mergeRequestBo.getAuthor().getUsername());
            mergeRequestBo.setCreated_at(TimeUtil.natureTime(mergeRequestBo.getCreated_at()).get(1));
            mergeRequestBo.setUpdated_at(TimeUtil.natureTime(mergeRequestBo.getUpdated_at()).get(1));
            if(mergeRequestBo.getMergedBy()!=null) {
                mergeRequestBo.setMerged_by(mergeRequestBo.getMergedBy().getUsername());
                mergeRequestBo.setMerged_at(TimeUtil.natureTime(mergeRequestBo.getMerged_at()).get(1));
            }
            if(mergeRequestBo.getClosedBy()!=null){
                mergeRequestBo.setClosed_by(mergeRequestBo.getClosedBy().getUsername());
                mergeRequestBo.setClosed_at(TimeUtil.natureTime(mergeRequestBo.getClosed_at()).get(1));
            }
        }

        return mergeRequestBos;
    }

    /**
     * 查找项目的某个合并请求
     * @param id 项目id
     * @param iid 合并请求id
     * @param userId 用户id
     * @return 合并请求
     */
    public MergeRequestBo findSingleMR(String id,int iid,Long userId){
        id = URLUtil.encodeURIComponent(id);
        String path="/projects/"+id+"/merge_requests/"+iid;
        MergeRequestBo mergeRequestBo=RequestUtil.get(path,userId,MergeRequestBo.class);
        mergeRequestBo.setCreated_by(mergeRequestBo.getAuthor().getUsername());
        mergeRequestBo.setCreated_at(TimeUtil.natureTime(mergeRequestBo.getCreated_at()).get(1));
        mergeRequestBo.setUpdated_at(TimeUtil.natureTime(mergeRequestBo.getUpdated_at()).get(1));
        if(mergeRequestBo.getMergedBy()!=null) {
            mergeRequestBo.setMerged_by(mergeRequestBo.getMergedBy().getUsername());
            mergeRequestBo.setMerged_at(TimeUtil.natureTime(mergeRequestBo.getMerged_at()).get(1));
        }
        if(mergeRequestBo.getClosedBy()!=null){
            mergeRequestBo.setClosed_by(mergeRequestBo.getClosedBy().getUsername());
            mergeRequestBo.setClosed_at(TimeUtil.natureTime(mergeRequestBo.getClosed_at()).get(1));
        }

        return mergeRequestBo;
    }

    /**
     * 创建一个合并请求
     * @param id 项目id
     * @param source_branch 源分支
     * @param target_branch 目标分支
     * @param title 标题
     * @param description 描述
     * @param userId 用户id
     */
    public void createMR(String id,String source_branch,String target_branch,String title,String description,Long userId){
        id = URLUtil.encodeURIComponent(id);
        String path="/projects/"+id+"/merge_requests";
        List<NameValuePair> params=new ArrayList<>();
        params.add(new BasicNameValuePair("source_branch",source_branch));
        params.add(new BasicNameValuePair("target_branch",target_branch));
        params.add(new BasicNameValuePair("title",title));
        params.add(new BasicNameValuePair("description",description));
        RequestUtil.post(path,userId,params);
    }

    /**
     * close或reopen一个合并请求
     * @param id 项目id
     * @param iid 合并请求id
     * @param state_event close or reopen
     * @param userId 用户id
     */
    public void updateMR(String id,int iid,String state_event,Long userId){
        id = URLUtil.encodeURIComponent(id);
        String path="/projects/"+id+"/merge_requests/"+iid;
        List<NameValuePair> params=new ArrayList<>();
        params.add(new BasicNameValuePair("state_event",state_event));
        RequestUtil.put(path,userId,params);
    }

    /**
     * 通过一个合并请求
     * @param id 项目id
     * @param iid 合并请求id
     * @param userId 用户id
     */
    public void acceptMR(String id,int iid,Long userId){
        id = URLUtil.encodeURIComponent(id);
        String path="/projects/"+id+"/merge_requests/"+iid+"/merge";
        List<NameValuePair> params=new ArrayList<>();
        RequestUtil.put(path,userId,params);
    }

}
