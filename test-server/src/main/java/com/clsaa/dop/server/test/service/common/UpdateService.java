package com.clsaa.dop.server.test.service.common;

import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 16/04/2019
 */
public interface UpdateService<PARAM> {

    void update(PARAM param);

    void batchUpdate(List<PARAM> params);
    
}
