package com.clsaa.dop.server.code.service;

import com.alibaba.fastjson.JSON;
import com.clsaa.dop.server.code.dao.UserMapper;
import com.clsaa.dop.server.code.feign.UserCredentialType;
import com.clsaa.dop.server.code.feign.UserFeign;
import com.clsaa.dop.server.code.model.bo.user.SSHKeyBo;
import com.clsaa.dop.server.code.model.bo.user.TokenBo;
import com.clsaa.dop.server.code.model.bo.user.UserIdBo;
import com.clsaa.dop.server.code.model.po.User;
import com.clsaa.dop.server.code.util.RequestUtil;
import com.clsaa.dop.server.code.util.TimeUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * user服务类
 *
 * @author wsy
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserFeign userFeign;

    /**
     * 查询用户的gitlab的access_token
     *
     * @param username 用户名
     * @return access_token
     */
    public String findUserAccessToken(String username) {
        return userMapper.findUserAccessToken(username);
    }


    /**
     * 注册一个用户，并且获得access_token插入数据库,并创建用户凭证
     *
     * @param username 用户名
     * @param password 密码
     * @param email    邮箱
     */
    public void addUser(Long id, String username, String password, String email) {

        //保证唯一性，查询用户名是否已经存在，如存在则先删除用户
        String path = "/users?username=" + username;
        List<UserIdBo> userIdBos = RequestUtil.getList(path, UserIdBo.class);
//        System.out.println("userId size:"+userIdBos.size());
        if (userIdBos.size() != 0) {

            int gitlabId = userIdBos.get(0).getId();
            path = "/users/" + gitlabId + "?hard_delete=true";
            RequestUtil.delete(path);

        }

        //首先创建gitlab 用户

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        //name和username相同，因为dop系统注册不需要填name
        params.add(new BasicNameValuePair("name", username));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("skip_confirmation", "true"));

        RequestUtil.post("/users", params);

        //获得用户的access_token
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "password"));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        String access_token = JSON.parseObject(RequestUtil.httpPost1("http://gitlab.dop.clsaa.com/oauth/token", params), TokenBo.class).getAccess_token();


        //将access_token插入数据库
        userMapper.addUser(new User(username, access_token));
        //创建gitlab_token用户凭证
        userFeign.addUserCredential(id, email, access_token, UserCredentialType.DOP_INNER_GITLAB_TOKEN);

    }


    /**
     * 根据用户名获得用户id，然后修改用户密码
     *
     * @param username 用户名
     * @param password 密码
     */
    public void updateUserPassword(String username, String password) {

        String path = "/users?username=" + username;
        int id = RequestUtil.getList(path, UserIdBo.class).get(0).getId();

        path = "/users/" + id;
        NameValuePair p1 = new BasicNameValuePair("password", password);
        NameValuePair p2 = new BasicNameValuePair("skip_reconfirmation", "true");
        List<NameValuePair> params = new ArrayList<>();
        params.add(p1);
        params.add(p2);
        RequestUtil.put(path, params);

    }


    /**
     * 查询用户的sshkey列表
     *
     * @param userId 用户id
     * @return sshkey列表
     */
    public List<SSHKeyBo> findSSHKeys(Long userId) {

        String path = "/user/keys";
        List<SSHKeyBo> sshKeyBos = RequestUtil.getList(path, userId, SSHKeyBo.class);
        for (SSHKeyBo sshKeyBo : sshKeyBos) {
            sshKeyBo.setCreated_at(TimeUtil.natureTime(sshKeyBo.getCreated_at()).get(1));
        }
        return sshKeyBos;
    }


    /**
     * 用户添加一个ssh key
     *
     * @param key    ssh key内容
     * @param title  标题
     * @param userId 用户id
     */
    public void addSSHKey(String key, String title, Long userId) {

        String path = "/user/keys";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("key", key));
        params.add(new BasicNameValuePair("title", title));
        RequestUtil.post(path, userId, params);

    }

    /**
     * 用户删除一个ssh key
     *
     * @param id     ssh key的id
     * @param userId 用户id
     */
    public void deleteSSHkey(int id, Long userId) {

        String path = "/user/keys/" + id;
        RequestUtil.delete(path, userId);

    }


}
