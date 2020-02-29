package com.clsaa.dop.server.gateway.service;

import com.clsaa.dop.server.gateway.dao.ClientDao;
import com.clsaa.dop.server.gateway.model.bo.ClientBoV1;
import com.clsaa.dop.server.gateway.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 客户端业务逻辑
 *
 * @author 任贵杰
 */
@Service
public class ClientService {
    @Autowired
    private ClientDao clientDao;

    /**
     * 根据clientId查询客户端
     *
     * @param clientId clientId
     * @return {@link ClientBoV1}
     */
    public ClientBoV1 findClientByClientId(String clientId) {
        return BeanUtils.convertType(this.clientDao.findClientByClientId(clientId), ClientBoV1.class);
    }
}
