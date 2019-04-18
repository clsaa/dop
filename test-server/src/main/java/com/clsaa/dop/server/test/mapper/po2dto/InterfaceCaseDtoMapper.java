package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.enums.Stage;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.CaseParamDto;
import com.clsaa.dop.server.test.model.dto.InterfaceCaseDto;
import com.clsaa.dop.server.test.model.dto.InterfaceStageDto;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import com.clsaa.dop.server.test.manager.UserManager;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
@Component
public class InterfaceCaseDtoMapper extends AbstractCommonServiceMapper<InterfaceCase, InterfaceCaseDto> {

    @Autowired
    private InterfaceStageDtoMapper interfaceStageDtoMapper;

    @Autowired
    private CaseParamDtoMapper caseParamDtoMapper;

    @Override
    public Class<InterfaceCase> getSourceClass() {
        return InterfaceCase.class;
    }

    @Override
    public Class<InterfaceCaseDto> getTargetClass() {
        return InterfaceCaseDto.class;
    }

    @Override
    public Optional<InterfaceCaseDto> convert(InterfaceCase source) {
        return super.convert(source)
                .map(fillStages(source))
                .map(fillUser())
                .map(fillParams(source))
                .map(fillEmptyStagesIfNeeded())
                ;
    }

    private Function<InterfaceCaseDto, InterfaceCaseDto> fillStages(InterfaceCase interfaceCase) {
        return dto -> {
            List<InterfaceStageDto> interfaceStageDtos = interfaceStageDtoMapper.convert(interfaceCase.getStages());
            dto.setStages(interfaceStageDtos);
            return dto;
        };
    }

    private Function<InterfaceCaseDto, InterfaceCaseDto> fillUser() {
        return interfaceCaseDto -> {
            Long cuserId = interfaceCaseDto.getCuser();
            interfaceCaseDto.setCreateUserName(UserManager.getUserName(cuserId));
            return interfaceCaseDto;
        };
    }

    private Function<InterfaceCaseDto, InterfaceCaseDto> fillEmptyStagesIfNeeded() {
        return interfaceCaseDto -> {
            if (CollectionUtils.isEmpty(interfaceCaseDto.getStages())) {
                // 填充空数据 方便展示
                List<InterfaceStageDto> stageDtos = Arrays.asList(
                        InterfaceStageDto.builder().stage(Stage.PREPARE)
                                .operations(new ArrayList<>())
                                .requestScripts(new ArrayList<>())
                                .waitOperations(new ArrayList<>()).build(),
                        InterfaceStageDto.builder().stage(Stage.TEST)
                                .operations(new ArrayList<>())
                                .requestScripts(new ArrayList<>())
                                .waitOperations(new ArrayList<>()).build(),
                        InterfaceStageDto.builder().stage(Stage.DESTROY)
                                .operations(new ArrayList<>())
                                .requestScripts(new ArrayList<>())
                                .waitOperations(new ArrayList<>()).build()
                );
                interfaceCaseDto.setStages(stageDtos);
            }
            return interfaceCaseDto;
        };
    }

    private Function<InterfaceCaseDto,InterfaceCaseDto> fillParams(InterfaceCase interfaceCase) {
        return interfaceCaseDto -> {
            List<CaseParamDto> caseParamDtos = caseParamDtoMapper.convert(interfaceCase.getCaseParams());
            interfaceCaseDto.setCaseParams(caseParamDtos);
            interfaceCaseDto.setParamsMap(
                    isEmpty(caseParamDtos) ?
                            Maps.newHashMap() :
                            caseParamDtos.stream().collect(Collectors.toMap(CaseParamDto::getRef, CaseParamDto::getValue))
            );
            return interfaceCaseDto;
        };
    }
}

