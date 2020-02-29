package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.WaitOperationDto;
import com.clsaa.dop.server.test.model.po.WaitOperation;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class WaitOperationDtoMapper extends AbstractCommonServiceMapper<WaitOperation, WaitOperationDto> {

    @Override
    public Class<WaitOperation> getSourceClass() {
        return WaitOperation.class;
    }

    @Override
    public Class<WaitOperationDto> getTargetClass() {
        return WaitOperationDto.class;
    }

    @Override
    public Optional<WaitOperation> inverseConvert(WaitOperationDto waitOperationDto) {
        if (waitOperationDto.getOrder() < 0) {
            // 无效的请求脚本 【前端参数有order<0的无效数据】
            return Optional.empty();
        }
        return super.inverseConvert(waitOperationDto)
                .map(UserManager.newInfoIfNotExists())
                ;
    }
}
