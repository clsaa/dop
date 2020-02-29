package com.clsaa.dop.server.login.service;

import com.clsaa.dop.server.login.feign.UserFeign;
import com.clsaa.dop.server.login.model.bo.UserBoV1;
import com.clsaa.dop.server.login.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户业务实现类
 *
 * @author joyren
 */
@Service
public class UserService {
    @Autowired
    private UserFeign userFeign;

    /**
     * 查询用户信息
     *
     * @param email    邮箱
     * @param password 密码
     * @return {@link UserBoV1}
     */
    public UserBoV1 findUserByEmailAndPassword(String email, String password) {
        return BeanUtils.convertType(this.userFeign.findUserByEmailAndPassword(email, password), UserBoV1.class);
    }
}
