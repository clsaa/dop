package com.clsaa.dop.server.test;

import com.clsaa.dop.server.test.doExecute.plugin.RequestHeaderPlugin;
import com.clsaa.dop.server.test.doExecute.plugin.UrlPlugin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 应用启动类
 *
 * @author 任贵杰
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableFeignClients
@EnablePluginRegistries({
        UrlPlugin.class, RequestHeaderPlugin.class
})
@ComponentScan("com.clsaa")
public class TestServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestServerApplication.class, args);
    }
}
