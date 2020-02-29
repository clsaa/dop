package com.clsaa.dop.server.application.controller;

import com.clsaa.dop.server.application.config.BizCodes;
import com.clsaa.dop.server.application.config.HttpHeadersConfig;
import com.clsaa.dop.server.application.service.AppUrlInfoService;
import com.clsaa.dop.server.application.util.Validator;
import com.clsaa.rest.result.bizassert.BizAssert;
import com.clsaa.rest.result.bizassert.BizCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

/**
 * <p>
 * 应用基本API接口实现类
 * </p>
 *
 * @author Bowen
 * @since 2019-3-7
 */
@RestController
@CrossOrigin
@EnableSwagger2WebFlux
public class AppUrlInfoController {
    @Autowired
    private AppUrlInfoService appUrlInfoService;

    //@ApiOperation(value = "查询应用基本Url", notes = "根据应用ID查询应用基本Url")
    //@GetMapping(value = "/appDetail")
    //public AppUrlInfoV1 findAppUrlInfoByAppId(@ApiParam(name = "appId", value = "appId", required = true) @RequestParam(value = "appId") Long appId) {
    //    return BeanUtils.convertType(this.appUrlInfoService.findAppUrlInfoByAppId(appId), AppUrlInfoV1.class);
    //}

    @ApiOperation(value = "修改应用基本Url", notes = "根据应用ID修改应用基本Url")
    @PutMapping(value = "/app/{appId}/urlInfo")
    public void updateAppUrlInfoByAppId(
            @RequestHeader(HttpHeadersConfig.HttpHeaders.X_LOGIN_USER) Long loginUser,
            @ApiParam(name = "appId", value = "appId", required = true) @PathVariable(value = "appId") Long appId,
            @ApiParam(name = "warehouseUrl", value = "Git仓库Url", defaultValue = "") @RequestParam(value = "warehouseUrl", required = false, defaultValue = "") String warehouseUrl,
            @ApiParam(name = "productionDbUrl", value = "开发数据库Url", defaultValue = "") @RequestParam(value = "productionDbUrl", required = false, defaultValue = "") String productionDbUrl,
            @ApiParam(name = "imageUrl", value = "镜像仓库Url", defaultValue = "") @RequestParam(value = "imageUrl", required = false, defaultValue = "") String imageUrl,
            @ApiParam(name = "testDbUrl", value = "测试数据库Url", defaultValue = "") @RequestParam(value = "testDbUrl", required = false, defaultValue = "") String testDbUrl,
            @ApiParam(name = "productionDomain", value = "开发域名", defaultValue = "") @RequestParam(value = "productionDomain", required = false, defaultValue = "") String productionDomain,
            @ApiParam(name = "testDomain", value = "测试域名", defaultValue = "") @RequestParam(value = "testDomain", required = false, defaultValue = "") String testDomain) {

        BizAssert.validParam(Validator.isUrl(warehouseUrl),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "Git仓库地址格式错误"));
        BizAssert.validParam(Validator.isImageUrl(imageUrl),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "镜像仓库地址格式错误"));
        BizAssert.validParam(productionDbUrl.equals("") || Validator.isUrl(productionDbUrl),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "开发数据库Url格式错误"));
        BizAssert.validParam(testDbUrl.equals("") || Validator.isUrl(testDbUrl),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "测试数据库Url格式错误"));
        BizAssert.validParam(productionDomain.equals("") || Validator.isDomain(productionDomain),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "开发域名格式错误"));
        BizAssert.validParam(testDomain.equals("") || Validator.isDomain(testDomain),
                new BizCode(BizCodes.INVALID_PARAM.getCode(), "测试域名格式错误"));
        this.appUrlInfoService.updateAppUrlInfoByAppId(appId, loginUser, warehouseUrl, imageUrl, productionDbUrl, testDbUrl, productionDomain, testDomain);
    }


    //@ApiOperation(value = "删除应用", notes = "删除应用")
    //@DeleteMapping(value = "/app")
    //public void deleteApp(
    //        @ApiParam(name = "id", value = "项目Id", required = true) @RequestParam(value = "id") String sId) {
    //
    //
    //    this.applicationService.deleteApp(sId);
    //
    //}
}
