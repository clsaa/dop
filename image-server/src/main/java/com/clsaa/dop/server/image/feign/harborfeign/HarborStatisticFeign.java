package com.clsaa.dop.server.image.feign.harborfeign;

import com.clsaa.dop.server.image.config.FeignConfig;
import com.clsaa.dop.server.image.model.po.StatisticMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 和harbor对应的统计信息接口
 * @author xzt
 * @since 2019-4-5
 */
@Component
@FeignClient(url = "${feign.url}", value = "statistics",configuration = FeignConfig.class)
public interface HarborStatisticFeign {
    /**
     * 根据登录用户获取统计信息
     * @param auth 用户信息
     * @return {@link StatisticMap} 统计信息
     */
    @GetMapping(value = "/statistics")
    StatisticMap statisticsGet(@RequestHeader("Authorization")String auth);
}
