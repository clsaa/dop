package com.clsaa.dop.server.test.mapper.po2dto;

import com.clsaa.dop.server.test.doExecute.Operation;
import com.clsaa.dop.server.test.manager.UserManager;
import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.dto.InterfaceStageDto;
import com.clsaa.dop.server.test.model.dto.RequestScriptDto;
import com.clsaa.dop.server.test.model.dto.WaitOperationDto;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import com.clsaa.dop.server.test.model.po.InterfaceStage;
import com.clsaa.dop.server.test.model.po.RequestScript;
import com.clsaa.dop.server.test.model.po.WaitOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class InterfaceStageDtoMapper extends AbstractCommonServiceMapper<InterfaceStage, InterfaceStageDto> {

    @Autowired
    private RequestScriptDtoMapper requestScriptDtoMapper;

    @Autowired
    private WaitOperationDtoMapper waitOperationDtoMapper;

    @Override
    public Class<InterfaceStage> getSourceClass() {
        return InterfaceStage.class;
    }

    @Override
    public Class<InterfaceStageDto> getTargetClass() {
        return InterfaceStageDto.class;
    }

    @Override
    public Optional<InterfaceStageDto> convert(InterfaceStage interfaceStage) {
        return super.convert(interfaceStage).map(interfaceStageDto -> {
            interfaceStageDto.setCaseId(interfaceStage.getInterfaceCase().getId());

            List<RequestScriptDto> requestScriptDtos = requestScriptDtoMapper.convert(interfaceStage.getRequestScripts());
            List<WaitOperationDto> waitOperationDtos = waitOperationDtoMapper.convert(interfaceStage.getWaitOperations());

            //sort
            requestScriptDtos.sort(Operation.operationSorter());
            waitOperationDtos.sort(Operation.operationSorter());

            List<Operation> operations = new ArrayList<>();
            operations.addAll(requireNonNull(requestScriptDtos));
            operations.addAll(requireNonNull(waitOperationDtos));
            operations.sort(Operation.operationSorter());

            interfaceStageDto.setRequestScripts(requestScriptDtos);
            interfaceStageDto.setWaitOperations(waitOperationDtos);
            interfaceStageDto.setOperations(operations);

            int total = requestScriptDtos.size() + waitOperationDtos.size();
            RequestScriptDto[] scriptDtos = new RequestScriptDto[total];
            requestScriptDtos.forEach(script -> scriptDtos[script.getOrder()] = script);
            for (int i = 0; i < total; i++) {
                if (scriptDtos[i] == null) {
                    scriptDtos[i] = RequestScriptDto.emptyScript();
                }
            }
            // fill array with empty scripts in correct order
            interfaceStageDto.setRequestScripts(Arrays.asList(scriptDtos));

            WaitOperationDto[] waitDtos = new WaitOperationDto[total];
            waitOperationDtos.forEach(wait -> waitDtos[wait.getOrder()] = wait);
            for (int i = 0; i < total; i++) {
                if (waitDtos[i] == null) {
                    waitDtos[i] = WaitOperationDto.emptyWait();
                }
            }
            // fill array with empty wait in correct order
            interfaceStageDto.setWaitOperations(Arrays.asList(waitDtos));

            return interfaceStageDto;
        });
    }

    @Override
    public Optional<InterfaceStage> inverseConvert(InterfaceStageDto interfaceStageDto) {
        return super.inverseConvert(interfaceStageDto)
                .map(UserManager.newInfoIfNotExists())
                .map(fillProperties(interfaceStageDto))
                ;
    }

    private Function<InterfaceStage,InterfaceStage> fillProperties(InterfaceStageDto interfaceStageDto) {
        return interfaceStage -> {
            List<RequestScript> requestScripts = requestScriptDtoMapper.inverseConvert(interfaceStageDto.getRequestScripts());
            List<WaitOperation> waitOperations = waitOperationDtoMapper.inverseConvert(interfaceStageDto.getWaitOperations());

            interfaceStage.setRequestScripts(requestScripts);
            interfaceStage.setWaitOperations(waitOperations);

            requestScripts.forEach(requestScript -> requestScript.setInterfaceStage(interfaceStage));
            waitOperations.forEach(waitOperation -> waitOperation.setInterfaceStage(interfaceStage));

            InterfaceCase interfaceCase = new InterfaceCase();
            interfaceCase.setId(interfaceStageDto.getCaseId());

            interfaceStage.setInterfaceCase(interfaceCase);
            return interfaceStage;
        };
    }

}
