package com.clsaa.dop.server.code.service;

import com.clsaa.dop.server.code.model.bo.file.*;
import com.clsaa.dop.server.code.util.RequestUtil;
import com.clsaa.dop.server.code.util.TimeUtil;
import com.clsaa.dop.server.code.util.URLUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件服务类
 *
 * @author wsy
 */
@Service
public class FileService {


    /**
     * 查找路径下的tree node 节点
     *
     * @param id     项目id
     * @param ref    分支名或tag名
     * @param path   文件路径
     * @param userId 用户id
     * @return path下的所有文件和文件夹节点
     */
    public List<TreeNodeBo> findTreeNodes(String id, String ref, String path, Long userId) {

        id = URLUtil.encodeURIComponent(id);

        long t1 = System.currentTimeMillis();
        List<TreeNodeBo> treeNodeBos = RequestUtil.getList("/projects/" + id + "/repository/tree?ref=" + ref + "&path=" + path, userId, TreeNodeBo.class);
        long t2 = System.currentTimeMillis();
        System.out.println("tree nodes:" + (t2 - t1));

        return treeNodeBos;
    }


    /**
     * 查找路径下的tree node节点对应的commit data
     *
     * @param id     项目id
     * @param ref    分支名或tag名
     * @param path   文件路径
     * @param userId 用户id
     * @return path下的提交信息
     */
    public List<TreeCommitBo> findTreeCommits(String id, String ref, String path, Long userId) {

        id = URLUtil.encodeURIComponent(id);

        long t1 = System.currentTimeMillis();
        List<TreeNodeBo> treeNodeBos = RequestUtil.getList("/projects/" + id + "/repository/tree?ref=" + ref + "&path=" + path, userId, TreeNodeBo.class);
        long t2 = System.currentTimeMillis();
        System.out.println("tree nodes:" + (t2 - t1));

        t1 = System.currentTimeMillis();
        List<TreeCommitBo> treeCommitBos = new ArrayList<>();
        for (TreeNodeBo treeNode : treeNodeBos) {
            //获得最近提交的一次
            TreeCommitBo treeCommitBo = new TreeCommitBo();
            CommitBo commit = RequestUtil.getList("/projects/" + id + "/repository/commits?ref_name=" + ref + "&path=" + treeNode.getPath(), userId, CommitBo.class).get(0);
            treeCommitBo.setCommit_id(commit.getId());
            treeCommitBo.setCommit_msg(commit.getMessage());
            List<String> res = TimeUtil.natureTime(commit.getCommitted_date());
            treeCommitBo.setCommit_date(res.get(0));
            treeCommitBo.setCommit_time(res.get(1));
            treeCommitBos.add(treeCommitBo);
        }
        t2 = System.currentTimeMillis();
        System.out.println("tree commits:" + (t2 - t1));

        return treeCommitBos;
    }


    /**
     * 查找项目所有的分支名和tag名
     *
     * @param id     项目id
     * @param userId 用户id
     * @return 分支名和tag名
     */
    public List<BranchAndTagBo> findBranchAndTag(String id, Long userId) {

        id = URLUtil.encodeURIComponent(id);

        List<BranchBo> branchBos = RequestUtil.getList("/projects/" + id + "/repository/branches", userId, BranchBo.class);
        List<TagBo> tagBos = RequestUtil.getList("/projects/" + id + "/repository/tags", userId, TagBo.class);

        List<ChildrenBo> branches = new ArrayList<>();
        List<ChildrenBo> tags = new ArrayList<>();

        for (BranchBo branchBo : branchBos) {
            branches.add(new ChildrenBo(branchBo.getName(), branchBo.getName(),branchBo.isDefault_(),branchBo.isProtected_()));
        }

        for (TagBo tagBo : tagBos) {
            tags.add(new ChildrenBo(tagBo.getName(), tagBo.getName()));
        }

        List<BranchAndTagBo> res = new ArrayList<>();
        res.add(new BranchAndTagBo("branch", "branch", branches));
        res.add(new BranchAndTagBo("tag", "tag", tags));

        return res;

    }

    /**
     * 根据分支和路径获得文件的内容
     *
     * @param id        项目id
     * @param file_path 文件路径(原始路径，需要urlencode)
     * @param ref       branch,tag or commit
     * @param userId    用户id
     * @return 文件内容(raw)
     */
    public BlobBo findFileContent(String id, String file_path, String ref, Long userId) {

        id = URLUtil.encodeURIComponent(id);
        file_path = URLUtil.encodeURIComponent(file_path);

        String content = RequestUtil.getString("/projects/" + id + "/repository/files/" + file_path + "/raw?ref=" + ref, userId);
        BlobBo blobBo = RequestUtil.get("/projects/" + id + "/repository/files/" + file_path + "?ref=" + ref, userId, BlobBo.class);
        blobBo.setFile_content(content);

        int size = blobBo.getSize();
        if (size < 1024) {
            blobBo.setFile_size(size + "B");
        } else if (size < (1024 * 1024)) {
            blobBo.setFile_size(size / 1024 + "KB");
        } else if (size < (1024 * 1024 * 1024)) {
            blobBo.setFile_size(size / (1024 * 1024) + "MB");
        } else {
            blobBo.setFile_size(size / (1024 * 1024 * 1024) + "GB");
        }

        return blobBo;


    }


    /**
     * 更新文件并作为一次提交
     *
     * @param id             项目id
     * @param file_path      文件路径
     * @param branch         分支
     * @param content        更新的文件内容
     * @param commit_message 提交信息
     * @param userId         用户id
     */
    public void updateFile(String id, String file_path, String branch, String content, String commit_message, Long userId) {

        id = URLUtil.encodeURIComponent(id);
        file_path = URLUtil.encodeURIComponent(file_path);

        NameValuePair p1 = new BasicNameValuePair("branch", branch);
        NameValuePair p2 = new BasicNameValuePair("content", content);
        NameValuePair p3 = new BasicNameValuePair("commit_message", commit_message);

        List<NameValuePair> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);

        String path = "/projects/" + id + "/repository/files/" + file_path;

        RequestUtil.put(path, userId, list);
    }


    /**
     * 删除一个文件
     *
     * @param id             项目id
     * @param file_path      文件路径
     * @param branch         分支
     * @param commit_message 提交信息
     * @param userId         用户id
     */
    public void deleteFile(String id, String file_path, String branch, String commit_message, Long userId) {

        id = URLUtil.encodeURIComponent(id);
        file_path = URLUtil.encodeURIComponent(file_path);

        String path = "/projects/" + id + "/repository/files/" + file_path + "?branch=" + branch + "&commit_message=" + commit_message;
        RequestUtil.delete(path, userId);
    }


    /**
     * 查找项目所有的文件路径
     *
     * @param id     项目id
     * @param ref    分支或tag
     * @param userId 用户id
     * @return 所有文件路径
     */
    public List<String> findAllFilePath(String id, String ref, Long userId) {

        id = URLUtil.encodeURIComponent(id);

        String path = "/projects/" + id + "/repository/tree?ref=" + ref + "&recursive=true";
        List<FilePathBo> filePathBos = RequestUtil.getList(path, userId, FilePathBo.class);
        List<String> res = new ArrayList<>();
        for (FilePathBo filePathBo : filePathBos) {
            if (filePathBo.getType().equals("blob")) {
                res.add(filePathBo.getPath());
            }
        }

        return res;


    }
}
