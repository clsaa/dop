package com.clsaa.dop.server.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger配置类
 *
 * @author 任贵杰 812022339@qq.com
 */
@Primary
@Configuration
public class SwaggerConfig implements SwaggerResourcesProvider {

    private static final String API_PACKAGE_NAME = SwaggerConfig.class.getPackage().getName().replace("config", "controller");
    @Autowired
    RouteLocator routeLocator;
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

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
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

    @Override
    public List<SwaggerResource> get() {
        //利用routeLocator动态引入微服务
        List<SwaggerResource> resources = new ArrayList<>();
        resources.add(swaggerResource("gateway-server","/v2/api-docs","1.0"));
        //循环 使用Lambda表达式简化代码
        routeLocator.getRoutes().forEach(route -> {
            //动态获取
            resources.add(swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs"), "1.0"));
        });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
