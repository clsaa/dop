package com.clsaa.dop.server.gateway.dao;

import com.clsaa.dop.server.gateway.model.po.Client;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 客户端持久层类
 *
 * @author 任贵杰 812022339@qq.com
 */
public interface ClientDao extends PagingAndSortingRepository<Client, Long> {
    /**
     * 根据clientId查找客户端
     *
     * @param clientId clientId
     * @return {@link Client}
     */
    Client findClientByClientId(String clientId);
}
