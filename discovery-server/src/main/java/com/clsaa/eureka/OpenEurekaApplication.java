package com.clsaa.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * <p>
 * eureka启动类
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-09-10
 */
@EnableEurekaServer
@SpringBootApplication
public class OpenEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenEurekaApplication.class, args);
    }
}