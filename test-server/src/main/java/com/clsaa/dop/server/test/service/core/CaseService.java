package com.clsaa.dop.server.test.service.core;

import com.clsaa.dop.server.test.dao.InterfaceCaseRepository;
import com.clsaa.dop.server.test.dao.ManualCaseRepository;
import com.clsaa.dop.server.test.enums.CaseType;
import com.clsaa.dop.server.test.model.po.InterfaceCase;
import com.clsaa.dop.server.test.model.po.ManualCase;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * @author xihao
 * @version 1.0
 * @since 08/05/2019
 */
@Component
@Slf4j
public class CaseService {

    public static final String INVALID_CASE_ID = "Invalid CaseId %d";

    @Autowired
    private InterfaceCaseRepository interfaceCaseRepository;

    @Autowired
    private ManualCaseRepository manualCaseRepository;

    private LoadingCache<Long,String> inCaseName = CacheBuilder.newBuilder().maximumSize(10000L).build(new CacheLoader<Long, String>() {
        @Override
        public String load(Long id) throws Exception {
            InterfaceCase interfaceCase = interfaceCaseRepository.findById(id).orElse(null);
            if (interfaceCase == null) {
                log.error("[Get Case Info]: error! invalid interface case id: {}", id);
                return String.format(INVALID_CASE_ID, id);
            }
            return interfaceCase.getCaseName();
        }
    });

    private LoadingCache<Long,String> manCaseName = CacheBuilder.newBuilder().maximumSize(10000L).build(new CacheLoader<Long, String>() {
        @Override
        public String load(Long id) throws Exception {
            ManualCase manualCase = manualCaseRepository.findById(id).orElse(null);
            if (manualCase == null) {
                log.error("[Get Case Info]: error! invalid manual case id: {}", id);
                return String.format(INVALID_CASE_ID, id);
            }
            return manualCase.getCaseName();
        }
    });

    public String getCaseName(CaseType caseType, Long id) throws ExecutionException {
        if (caseType == CaseType.INTERFACE) {
            return inCaseName.get(id);
        } else if (caseType == CaseType.MANUAL) {
            return manCaseName.get(id);
        }else {
            return null;
        }
    }
}
