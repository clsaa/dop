package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.feign.CodeFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "CodeService")
public class CodeService {
    @Autowired
    private CodeFeign codeFeign;

    public List<String> findProjectUrlList(Long loginUser) {
        return this.codeFeign.findProjectUrlList(loginUser);

    }
}
