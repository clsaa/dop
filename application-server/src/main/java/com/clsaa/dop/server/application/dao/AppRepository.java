package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.App;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AppRepository extends JpaRepository<App, Long> {


    /**
     * 根据projectId分页查询Application
     *
     * @param projectId 项目Id
     * @param pageable  分页
     * @return {@link Page<App>} 项目持久层对象
     */
    Page<App> findAllByProjectId(Long projectId, Pageable pageable);
    //Page<App> findAllByProjectIdAndIdIn(Long projectId, Pageable pageable, Collection<Long> c);

    /**
     * 根据projectId查询Application
     *
     * @param ouser 拥有者Id
     * @return {@link   List<App> } 项目持久层对象
     */
    List<App> findAllByOuser(Long ouser);

    /**
     * 根据projectId查询Application的数量
     *
     * @param projectId 项目Id
     * @return {@link Integer} 项目持久层对象
     */
    Integer countAllByProjectId(Long projectId);
    //Integer countAllByProjectIdAndIdIn(Long projectId, Collection<Long> c);

    /**
     * 根据projectId和应用名称查询Application的数量
     *
     * @param projectId 项目Id
     * @param title     应用名称
     * @return {@link Integer} 项目持久层对象
     */
    Integer countAllByProjectIdAndTitleStartingWith(Long projectId, String title);
    //Integer countAllByProjectIdAndTitleStartingWithAndIdIn(Long projectId, String title, Collection<Long> c);

    /**
     * 根据projectId和应用名称查询Application
     *
     * @param projectId 项目Id
     * @param title     应用名称
     * @param pageable  分页
     * @return {@link Page<App>} 项目持久层对象
     */
    Page<App> findAllByProjectIdAndTitleStartingWith(Long projectId, String title, Pageable pageable);
    //Page<App> findAllByProjectIdAndTitleStartingWithAndIdIn(Long projectId, String title, Pageable pageable, Collection<Long> c);
}
