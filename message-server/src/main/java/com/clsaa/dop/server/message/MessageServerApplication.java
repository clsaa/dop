package com.clsaa.dop.server.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 应用启动类
 *
 * @author 任贵杰
 */
@SpringBootApplication
@EnableAsync
@EnableFeignClients
@EnableScheduling
@ComponentScan("com.clsaa")
public class MessageServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageServerApplication.class, args);
    }
}
