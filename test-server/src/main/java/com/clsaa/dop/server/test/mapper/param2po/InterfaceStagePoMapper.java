package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.InterfaceStageParam;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import com.clsaa.dop.server.test.model.po.InterfaceStage;
import com.clsaa.dop.server.test.model.po.RequestScript;
import com.clsaa.dop.server.test.model.po.WaitOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.clsaa.dop.server.test.manager.UserManager.dateAndUser;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Component
public class InterfaceStagePoMapper extends AbstractCommonServiceMapper<InterfaceStageParam, InterfaceStage> {

    @Autowired
    private RequestScriptPoMapper requestScriptPoMapper;

    @Autowired
    private WaitOperationPoMapper waitOperationPoMapper;

    @Override
    public Class<InterfaceStageParam> getSourceClass() {
        return InterfaceStageParam.class;
    }

    @Override
    public Class<InterfaceStage> getTargetClass() {
        return InterfaceStage.class;
    }

    @Override
    public Optional<InterfaceStage> convert(InterfaceStageParam interfaceStageParam) {
        List<RequestScript> requestScripts = requestScriptPoMapper.convert(interfaceStageParam.getRequestScripts());
        List<WaitOperation> waitOperations = waitOperationPoMapper.convert(interfaceStageParam.getWaitOperations());
        return super.convert(interfaceStageParam).map(dateAndUser())
                .map(interfaceStage -> {
                        interfaceStage.setRequestScripts(requestScripts);
                        interfaceStage.setWaitOperations(waitOperations);

                        requestScripts.forEach(requestScript -> requestScript.setInterfaceStage(interfaceStage));
                        waitOperations.forEach(waitOperation -> waitOperation.setInterfaceStage(interfaceStage));

                        InterfaceCase interfaceCase = new InterfaceCase();
                        interfaceCase.setId(interfaceStageParam.getCaseId());
                        interfaceCase.setStages(Collections.singletonList(interfaceStage));

                        interfaceStage.setInterfaceCase(interfaceCase);
                        return interfaceStage;
                });
    }
}
