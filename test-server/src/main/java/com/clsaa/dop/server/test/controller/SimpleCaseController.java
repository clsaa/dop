package com.clsaa.dop.server.test.controller;

import com.clsaa.dop.server.test.model.vo.SimpleCaseVo;
import com.clsaa.dop.server.test.service.core.SimpleCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 07/05/2019
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/simpleCases")
public class SimpleCaseController {

    @Autowired
    private SimpleCaseService simpleCaseService;

    @GetMapping
    public List<SimpleCaseVo> queryCases(@RequestParam(value = "appId", required = false) Long appId,
                                         @RequestParam(name = "key", required = false) String key) {
        if (key == null || key.length() == 0) {
            key = "";
        }
        return simpleCaseService.getAllCases(appId, key);
    }
}
