package com.clsaa.dop.server.test.dao;

import com.clsaa.dop.server.test.model.po.InterfaceCase;
import com.clsaa.dop.server.test.model.vo.SimpleCaseVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
public interface InterfaceCaseRepository extends JpaRepository<InterfaceCase, Long> {

    @Query(value = "select new com.clsaa.dop.server.test.model.vo.SimpleCaseVo(i.id, i.caseName, i.caseDesc, i.applicationId)" +
            " from InterfaceCase i where i.applicationId=?1 and i.caseName like concat('%',?2,'%')  and i.deleted = false ")
    List<SimpleCaseVo> findSimpleCase(Long appId, String key);

    @Query(value = "select new com.clsaa.dop.server.test.model.vo.SimpleCaseVo(i.id, i.caseName, i.caseDesc, i.applicationId)" +
            " from InterfaceCase i where i.caseName like concat('%',?1,'%')  and i.deleted = false ")
    List<SimpleCaseVo> findSimpleCaseWithoutAppId(String key);
}
