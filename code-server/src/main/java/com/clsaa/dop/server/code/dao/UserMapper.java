package com.clsaa.dop.server.code.dao;

import com.clsaa.dop.server.code.model.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wsy
 */

@Mapper
@Repository
public interface UserMapper {

    public String findUserAccessToken(@Param("username") String username);

    int addUser(User user);

}
