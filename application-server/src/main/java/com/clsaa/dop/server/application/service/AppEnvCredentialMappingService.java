package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.AppEnvCredentialMappingRepository;
import com.clsaa.dop.server.application.model.po.AppEnvCredentialMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service(value = "AppEnvCredentialMappingService")
public class AppEnvCredentialMappingService {
    @Autowired
    AppEnvCredentialMappingRepository appEnvCredentialMappingRepository;

    public void createMapping(Long cuser, Long appEnvId, Long credentialId) {
        AppEnvCredentialMapping appEnvCredentialMapping = AppEnvCredentialMapping.builder()
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .cuser(cuser)
                .muser(cuser)
                .is_deleted(false)
                .appEnvId(appEnvId)
                .credentialId(credentialId)
                .build();
        this.appEnvCredentialMappingRepository.saveAndFlush(appEnvCredentialMapping);
    }


    public Long findCredentialIdByAppEnvId(Long appEnvId) {


        return this.appEnvCredentialMappingRepository.findByAppEnvId(appEnvId).orElse(null).getCredentialId();

    }
}
