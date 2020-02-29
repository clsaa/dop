package com.clsaa.dop.server.image.controller;

import com.clsaa.dop.server.image.model.vo.StatisticVO;
import com.clsaa.dop.server.image.service.StatisticsService;
import com.clsaa.dop.server.image.util.BeanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于获取仓库数据的控制类
 * @author xzt
 * @since 2019-4-5
 */
@RestController
@CrossOrigin

public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @ApiOperation(value = "获取仓库的基本统计数据",notes = "根据不同的登录用户返回不同的数据")
    @GetMapping(value = "/v1/statistics")
    public StatisticVO getStatistics(@ApiParam(value = "用户id",required = true) @RequestHeader(value = "x-login-user")Long userId){
        return BeanUtils.convertType(statisticsService.getStatistics(userId),StatisticVO.class);
    }
}
