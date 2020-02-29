package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author Bowen
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * 根据status和关键词查询Project
     *
     * @param status 项目状态
     * @param title  搜索字符串
     * @return {@link Page<Project>} 项目持久层对象
     */

    Page<Project> findAllByStatusAndTitleStartingWithAndIdIn(Project.Status status, String title, Pageable pageable, Collection<Long> c);

    /**
     * 根据status和关键词查询Project
     *
     * @param status 项目状态
     * @param title  搜索字符串
     * @return {@link Integer} 项目持久层对象
     */

    Integer countAllByStatusAndTitleStartingWithAndIdIn(Project.Status status, String title, Collection<Long> c);

    /**
     * 根据status和关键词查询Project
     *
     * @param status 项目状态
     * @return {@link Page<Project>} 项目持久层对象
     */

    Page<Project> findAllByStatusAndIdIn(Project.Status status, Pageable pageable, Collection<Long> c);

    /**
     * 根据status和关键词查询Project
     *
     * @param status 项目状态
     * @return {@link Integer} 项目持久层对象
     */

    Integer countAllByStatusAndIdIn(Project.Status status, Collection<Long> c);

    /**
     * 根据status和关键词查询Project
     *
     * @return {@link Integer} 项目持久层对象
     */
    Integer countAllByIdIn(Collection<Long> c);

    /**
     * 根据status和关键词查询Project
     *
     * @return {@link Integer} 项目持久层对象
     */
    Page<Project> findAllByIdIn(Pageable pageable, Collection<Long> c);
    /**
     * 根据status和关键词查询Project
     *
     * @param status 项目状态
     * @return {@link Page<Project>} 项目持久层对象
     */

    List<Project> findAllByStatusAndIdIn(Project.Status status, Collection<Long> c);



    /**
     * 根据关键词查询Project
     *
     * @param Title 项目名称
     * @return {@link Integer} 项目持久层对象
     */

    Integer countAllByTitleStartingWithAndIdIn(String Title, Collection<Long> c);

    /**
     * 根据关键词查询Project
     *
     * @param Title 项目名称
     * @return {@link Page<Project>} 项目持久层对象
     */
    Page<Project> findAllByTitleStartingWithAndIdIn(String Title, Pageable pageable, Collection<Long> c);
}
