package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.dao.AppEnvRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/*
 * 暂未开始使用
 */
@Service(value = "KubernetesService")
public class KubernetesService {
    @Autowired
    AppEnvRepository appEnvRepository;


}


