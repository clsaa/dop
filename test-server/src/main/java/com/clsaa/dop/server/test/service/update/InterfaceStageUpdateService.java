package com.clsaa.dop.server.test.service.update;

import com.clsaa.dop.server.test.dao.InterfaceStageRepository;
import com.clsaa.dop.server.test.mapper.po2dto.InterfaceStageDtoMapper;
import com.clsaa.dop.server.test.model.dto.InterfaceStageDto;
import com.clsaa.dop.server.test.model.po.InterfaceStage;
import com.clsaa.dop.server.test.service.common.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 19/04/2019
 */
@Component
public class InterfaceStageUpdateService implements UpdateService<InterfaceStageDto> {

    @Autowired
    private InterfaceStageRepository stageRepository;

    @Autowired
    private InterfaceStageDtoMapper stageDtoMapper;

    @Override
    public void update(InterfaceStageDto interfaceStageDto) {

    }

    @Override
    public void batchUpdate(List<InterfaceStageDto> interfaceStageDtos) {
        List<InterfaceStage> stages = stageDtoMapper.inverseConvert(interfaceStageDtos);
        stages.forEach(stage -> stageRepository.saveAndFlush(stage));
    }
}
