package com.clsaa.dop.server.image.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

/**
 * swagger配置类
 *
 * @author 任贵杰 812022339@qq.com
 */
@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfig {

    private static final String API_PACKAGE_NAME = SwaggerConfig.class.getPackage().getName().replace("config", "controller");
    @Value("${project.host}")
    private String host;
    @Value("${project.groupId}")
    private String groupId;
    @Value("${project.artifactId}")
    private String artifactId;
    @Value("${project.version}")
    private String version;
    @Value("${project.name}")
    private String name;
    @Value("${project.description}")
    private String description;
    @Value("${project.url}")
    private String url;
    @Value("${swagger.enable}")
    private boolean enableShow;
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enableShow)
                .host(host)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(API_PACKAGE_NAME))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(name)
                .description(description)
                .termsOfServiceUrl(url)
                .version(version)
                .build();
    }
}
