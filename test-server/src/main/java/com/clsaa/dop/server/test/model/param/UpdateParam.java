package com.clsaa.dop.server.test.model.param;

/**
 * @author xihao
 * @version 1.0
 * @since 16/04/2019
 */
public interface UpdateParam<ID> {

    void setId(ID id);

    ID getId();
}
