package com.clsaa.dop.server.user.dao;


import com.clsaa.dop.server.user.model.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * @author joyren
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return {@link User}
     */
    User findUsersById(Long id);

    /**
     * 根据email查询用户
     *
     * @param email 用户email
     * @return {@link User}
     */
    User findUserByEmail(String email);

    /**
     * 根据用户名查询用户
     *
     * @param name 用户名
     * @return {@link User}
     */
    User findUserByName(String name);

    /**
     * 根据关键字查询用户
     *
     * @param key 关键字
     * @return {@link List<User>}
     */
    @Query(value = "select count(*) from t_user  where email like concat(:key,'%') or name like concat(:key,'%')",
            nativeQuery = true)
    int selectCountByEmailOrPassword(@Param("key") String key);

    /**
     * 根据关键字查询用户
     *
     * @param key       关键字
     * @param rowOffset 页偏移量
     * @param pageSize  页大小
     * @return {@link List<User>}
     */
    @Query(value = "select * from t_user  where email like concat(:key,'%') or name like concat(:key,'%')" +
            " order by name" +
            " limit :rowOffset,:pageSize",
            nativeQuery = true)
    List<User> searchUserByEmailOrPassword(@Param("key") String key,
                                           @Param("rowOffset") Integer rowOffset,
                                           @Param("pageSize") Integer pageSize);

    /**
     * 根据关键字查询用户
     *
     * @param ids id列表
     * @param key 关键字
     * @return {@link List<User>}
     */
    @Query(value = "select count(*) from t_user  where id in :ids" +
            " and (email like concat(:key,'%') or name like concat(:key,'%'))",
            nativeQuery = true)
    int selectCountByIdsAndEmailOrPassword(@Param("ids") List<Long> ids,
                                           @Param("key") String key);

    /**
     * 根据关键字查询用户
     *
     * @param ids       id列表
     * @param key       关键字
     * @param rowOffset 页偏移量
     * @param pageSize  页大小
     * @return {@link List<User>}
     */
    @Query(value = "select * from t_user  where id in :ids" +
            " and (email like concat(:key,'%') or name like concat(:key,'%'))" +
            " order by name" +
            " limit :rowOffset,:pageSize",
            nativeQuery = true)
    List<User> searchUserByIdsAndEmailOrPassword(@Param("ids") List<Long> ids,
                                                 @Param("key") String key,
                                                 @Param("rowOffset") Integer rowOffset,
                                                 @Param("pageSize") Integer pageSize);

}
