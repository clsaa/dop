package com.clsaa.dop.server.code.model.bo.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目统计数据
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsBo {

    private int commit_count;
    private int storage_size;
    private int repository_size;
}
