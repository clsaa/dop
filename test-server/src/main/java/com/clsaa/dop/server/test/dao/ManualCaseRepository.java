package com.clsaa.dop.server.test.dao;

import com.clsaa.dop.server.test.model.po.ManualCase;
import com.clsaa.dop.server.test.model.vo.SimpleCaseVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
public interface ManualCaseRepository extends JpaRepository<ManualCase, Long> {

    @Query(value = "select new com.clsaa.dop.server.test.model.vo.SimpleCaseVo(m.id, m.caseName, m.caseDesc, m.applicationId)" +
            " from ManualCase m where m.applicationId=?1 and m.caseName like concat('%',?2,'%') and m.deleted = false ")
    List<SimpleCaseVo> findSimpleCase(Long appId, String key);

    @Query(value = "select new com.clsaa.dop.server.test.model.vo.SimpleCaseVo(m.id, m.caseName, m.caseDesc, m.applicationId)" +
            " from ManualCase m where m.caseName like concat('%',?1,'%') and m.deleted = false ")
    List<SimpleCaseVo> findSimpleCaseWithoutAppId(String key);
}
