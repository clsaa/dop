package com.clsaa.dop.server.image.service;

import com.clsaa.dop.server.image.feign.UserFeign;
import com.clsaa.dop.server.image.feign.harborfeign.HarborStatisticFeign;
import com.clsaa.dop.server.image.model.bo.StatisticsBO;
import com.clsaa.dop.server.image.model.dto.UserCredentialDto;
import com.clsaa.dop.server.image.model.enumtype.UserCredentialType;
import com.clsaa.dop.server.image.util.BasicAuthUtil;
import com.clsaa.dop.server.image.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 统计信息的服务类
 * @author xzt
 * @since 2019-4-5
 */
@Service
public class StatisticsService {

    private final UserFeign userFeign;
    private final HarborStatisticFeign harborStatisticFeign;

    @Autowired
    public StatisticsService(UserFeign userFeign, HarborStatisticFeign harborStatisticFeign) {
        this.userFeign = userFeign;
        this.harborStatisticFeign = harborStatisticFeign;
    }


    /**
     *
     * @param userId 用户id
     * @return {@link StatisticsBO} 统计数据的逻辑层类
     */
    public StatisticsBO getStatistics(Long userId){
        UserCredentialDto userCredentialDto = userFeign.getUserCredentialV1ByUserId(userId,
                UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
        String auth = BasicAuthUtil.createAuth(userCredentialDto);
        return BeanUtils.convertType(harborStatisticFeign.statisticsGet(auth),StatisticsBO.class);
    }
}
