package com.clsaa.dop.server.test.service.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.clsaa.dop.server.test.enums.HttpMethod;
import com.clsaa.dop.server.test.enums.OperationType;
import com.clsaa.dop.server.test.enums.ParamClass;
import com.clsaa.dop.server.test.model.param.CaseParamRef;
import com.clsaa.dop.server.test.model.param.RequestParamCreateParam;
import com.clsaa.dop.server.test.model.param.RequestScriptParam;
import com.clsaa.dop.server.test.model.vo.ImportApiVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xihao
 * @version 1.0
 * @since 04/05/2019
 */
@Component
@Slf4j
public class ImportService {

    public static final String HOST_KEY = "host";
    public static final String BASE_PATH_KEY = "basePath";
    public static final String PATHS_KEY = "paths";
    public static final String PARAMS_KEY = "parameters";
    public static final String PARAM_TYPE_KEY = "in";

    public ImportApiVo doImport(String openApi) {
        JSONObject apiJson = null;
        try {
            apiJson = JSON.parseObject(openApi);
        } catch (Exception e) {
            log.error("Invalid Json File! Json Parse Error!");
        }
        if (apiJson == null) {
            return null;
        }
        List<CaseParamRef> caseParams = new ArrayList<>();
        String host = apiJson.getString(HOST_KEY);
        addCaseParam("host", host, caseParams);
        String basePath = apiJson.getString(BASE_PATH_KEY);
        addCaseParam("basePath", basePath, caseParams);
        List<RequestScriptParam> requestScriptParams = new ArrayList<>();
        AtomicReference<Integer> order = new AtomicReference<>(0);

        Optional.of(apiJson.getJSONObject(PATHS_KEY))
                .ifPresent(pathsJson -> {
                    pathsJson.getInnerMap().forEach(
                            (rawUrl, object) ->
                                    ensureJson(object)
                                            .getInnerMap()
                                            .forEach((method, apiInfo) -> {
                                                HttpMethod httpMethod = HttpMethod.from(method);
                                                List<RequestParamCreateParam> requestParams = new ArrayList<>();
                                                Optional.of(ensureJson(apiInfo))
                                                        .ifPresent(info -> {
                                                            JSONArray paramArray = info.getJSONArray(PARAMS_KEY);
                                                            if (paramArray == null) return;
                                                            paramArray.forEach(param -> {
                                                                JSONObject paramJson = ensureJson(param);
                                                                ParamClass paramClass = ParamClass.from(paramJson.getString(PARAM_TYPE_KEY));
                                                                // skip body param
                                                                if (paramClass == ParamClass.BODY_PARAM) return;
                                                                String name = paramJson.getString("name");
                                                                RequestParamCreateParam requestParam = RequestParamCreateParam.builder()
                                                                        .paramClass(paramClass)
                                                                        .name(name)
                                                                        .value("")
                                                                        .build();
                                                                requestParams.add(requestParam);
                                                            });
                                                        });

                                                String url = "http://" + "${host}" + "${basePath}";
                                                if (rawUrl.startsWith("/")) {
                                                    url += rawUrl.substring(1);
                                                }else {
                                                    url += rawUrl;
                                                }

                                                RequestScriptParam requestScript = RequestScriptParam.builder()
                                                        .rawUrl(url)
                                                        .httpMethod(httpMethod)
                                                        .requestParams(requestParams)
                                                        .requestHeaders(new ArrayList<>())
                                                        .requestCheckPoints(new ArrayList<>())
                                                        .resultParams(new ArrayList<>())
                                                        .requestBody("")
                                                        .retryTimes(0)
                                                        .retryInterval(0L)
                                                        .operationType(OperationType.REQUEST)
                                                        .order(order.getAndUpdate(i -> i + 1))
                                                        .build();

                                                requestScriptParams.add(requestScript);
                                            })
                    );
                });

        return ImportApiVo.builder()
                .caseParams(caseParams)
                .requestScripts(requestScriptParams).build();
    }

    private JSONObject ensureJson(Object value) {
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }

        if (value instanceof String) {
            return JSON.parseObject((String) value);
        }

        return (JSONObject) JSON.toJSON(value);
    }

    private void addCaseParam(String ref, String value, List<CaseParamRef> params) {
        CaseParamRef newParam = CaseParamRef.builder().ref(ref).value(value).build();
        params.add(newParam);
    }
}
