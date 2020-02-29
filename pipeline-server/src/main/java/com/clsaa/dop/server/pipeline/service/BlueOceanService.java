package com.clsaa.dop.server.pipeline.service;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;

/**
 *  调用blueocean接口业务实现类
 *
 * @author 张富利
 * @since 2019-03-09
 */
@Service
public class BlueOceanService {
    private RestTemplate restTemplate = new RestTemplate();

    // 获得authorization
    public String getAuthorization(){
        String uri = "http://jenkins.dop.clsaa.com/jwt-auth/token";
        HttpHeaders headers = restTemplate.getForEntity(uri, String.class).getHeaders();
        return headers.get("X-BLUEOCEAN-JWT").get(0);
    }
}
