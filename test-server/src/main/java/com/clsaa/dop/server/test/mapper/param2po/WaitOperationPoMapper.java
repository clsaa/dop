package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.WaitOperationParam;
import com.clsaa.dop.server.test.model.po.WaitOperation;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.clsaa.dop.server.test.manager.UserManager.dateAndUser;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class WaitOperationPoMapper extends AbstractCommonServiceMapper<WaitOperationParam, WaitOperation> {

    @Override
    public Class<WaitOperationParam> getSourceClass() {
        return WaitOperationParam.class;
    }

    @Override
    public Class<WaitOperation> getTargetClass() {
        return WaitOperation.class;
    }

    @Override
    public Optional<WaitOperation> convert(WaitOperationParam waitOperationParam) {
        if (waitOperationParam.getOrder() < 0) {
            // 无效的请求脚本 【前端参数有order<0的无效数据】
            return Optional.empty();
        }
        return super.convert(waitOperationParam).map(dateAndUser());
    }

}
