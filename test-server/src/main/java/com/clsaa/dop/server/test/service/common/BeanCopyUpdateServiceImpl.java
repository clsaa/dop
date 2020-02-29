package com.clsaa.dop.server.test.service.common;

import com.clsaa.dop.server.test.model.param.UpdateParam;
import com.clsaa.dop.server.test.model.po.Po;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xihao
 * @version 1.0
 * @since 16/04/2019
 */
public class BeanCopyUpdateServiceImpl<ID, PARAM extends UpdateParam<ID>, PO extends Po> extends CommonUpdateServiceImpl<ID, PARAM, PO> {

    public BeanCopyUpdateServiceImpl(JpaRepository<PO, ID> repository) {
        super(repository);
    }

    @Override
    protected PO doUpdate(PO old, PARAM param) {
        BeanUtils.copyProperties(param, old);
        return old;
    }
}
