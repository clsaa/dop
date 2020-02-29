package com.clsaa.dop.server.test.service.common;

import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.model.param.UpdateParam;
import com.clsaa.dop.server.test.model.po.Po;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.function.Function;

/**
 * @author xihao
 * @version 1.0
 * @since 16/04/2019
 */
public abstract class CommonUpdateServiceImpl<ID, PARAM extends UpdateParam<ID>, PO extends Po> implements UpdateService<PARAM> {

    private JpaRepository<PO, ID> repository;

    public CommonUpdateServiceImpl(JpaRepository<PO, ID> repository) {
        this.repository = repository;
    }

    @Override
    public void update(PARAM param) {
        ID id = param.getId();
        repository.findById(id)
                .map(doUpdateFunction(param))
                .map(UserManager.updateUserAndTime())
                .map(repository::save);
    }

    private Function<PO, PO> doUpdateFunction(PARAM param) {
        return po -> doUpdate(po, param);
    }

    /**
     * 更新的核心逻辑
     *
     * @param old   旧数据实体
     * @param param 需要更新的新实体参数
     * @return 更新后的数据
     */
    protected abstract PO doUpdate(PO old, PARAM param);

    @Override
    public void batchUpdate(List<PARAM> params) {
        params.forEach(this::update);
    }
}
