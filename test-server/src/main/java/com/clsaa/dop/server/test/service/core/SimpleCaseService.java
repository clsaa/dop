package com.clsaa.dop.server.test.service.core;

import com.clsaa.dop.server.test.dao.InterfaceCaseRepository;
import com.clsaa.dop.server.test.dao.ManualCaseRepository;
import com.clsaa.dop.server.test.enums.CaseType;
import com.clsaa.dop.server.test.model.vo.SimpleCaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 07/05/2019
 */
@Component
public class SimpleCaseService {

    @Autowired
    private ManualCaseRepository manualCaseRepository;

    @Autowired
    private InterfaceCaseRepository interfaceCaseRepository;

    public List<SimpleCaseVo> getAllCases(Long appId, String key) {
        List<SimpleCaseVo> result = new ArrayList<>();
        boolean appIdExists = appId != null;
        List<SimpleCaseVo> manualCases = appIdExists ?
                manualCaseRepository.findSimpleCase(appId, key) :
                manualCaseRepository.findSimpleCaseWithoutAppId(key);
        if (!CollectionUtils.isEmpty(manualCases)) {
            manualCases.forEach(manualCase -> manualCase.setCaseType(CaseType.MANUAL));
            result.addAll(manualCases);
        }
        List<SimpleCaseVo> interfaceCases = appIdExists ?
                interfaceCaseRepository.findSimpleCase(appId, key) :
                interfaceCaseRepository.findSimpleCaseWithoutAppId(key);
        if (!CollectionUtils.isEmpty(interfaceCases)) {
            interfaceCases.forEach(interfaceCase -> interfaceCase.setCaseType(CaseType.INTERFACE));
            result.addAll(interfaceCases);
        }
        return result;
    }
}
