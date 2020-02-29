package com.clsaa.dop.server.test.model.dto;

import com.clsaa.dop.server.test.doExecute.Operation;
import com.clsaa.dop.server.test.doExecute.context.ExecuteContext;
import com.clsaa.dop.server.test.doExecute.context.RequestContext;
import com.clsaa.dop.server.test.doExecute.matcher.ToStringMatcher;
import com.clsaa.dop.server.test.enums.HttpMethod;
import com.clsaa.dop.server.test.enums.OperationType;
import com.clsaa.dop.server.test.enums.ParamClass;
import com.clsaa.dop.server.test.model.po.OperationExecuteLog;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.clsaa.dop.server.test.doExecute.TestManager.*;
import static com.clsaa.dop.server.test.doExecute.Version.currentVersion;
import static com.clsaa.dop.server.test.enums.OperationType.REQUEST;

/**
 * @author xihao
 * @version 1.0
 * @since 18/03/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestScriptDto implements Operation {

    private static final Logger log = LoggerFactory.getLogger(RequestScriptDto.class);

    // ----------- main property ---------
    private String rawUrl;

    private HttpMethod httpMethod;

    private List<RequestHeaderDto> requestHeaders;

    private List<RequestParamDto> requestParams;

    private Map<String, String> headersMap;

    private String requestBody;

    // check point concerned
    private List<RequestCheckPointDto> requestCheckPoints;

    private int retryTimes;

    private Long retryInterval;

    private List<UrlResultParamDto> resultParams;

    private OperationType operationType;

    private int order;

    private String result = SUCCESS_RESULT;

    // ----------- common property ---------
    private Long id;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    private Long cuser;

    private Long muser;

    private boolean deleted;

    public static RequestScriptDto emptyScript() {
        return RequestScriptDto.builder()
                .requestHeaders(new ArrayList<>())
                .requestCheckPoints(new ArrayList<>())
                .resultParams(new ArrayList<>())
                .order(-1)
                .build();
    }

    @Override
    public void run(ExecuteContext executeContext) {
        // 1, execute log
        OperationExecuteLog operationExecuteLog = initOperationLog(executeContext);
        try {
            // 2, plugins to handle request info
            RequestContext requestContext = RequestContext.builder()
                    .url(rawUrl)
                    .requestHeaders(headersMap)
                    .requestBody(requestBody)
                    .caseParams(executeContext.getCaseParams())
                    .version(currentVersion())
                    .build();
            requestContext.fillRequestParams(this.requestParams);

            String url = urlHandled(requestContext);
            Map<String, String> headers = requestHeadersHandled(requestContext);

            StringBuilder executionLog = requestContext.logRequestInfo(headers, requestBody, httpMethod, url);

            // 3, http call and log response
            //string writer to store response info
            StringWriter writer = new StringWriter();
            PrintStream captor = new PrintStream(new WriterOutputStream(writer), true);
            RequestSpecification requestSpecification = RestAssured.given()
                    .config(RestAssured.config()
                            .logConfig(new LogConfig(captor, true)))
                    .headers(headers)
                    .queryParams(getQueryParams(this.requestParams))
                    .body(requestBody);

            URL urlObj = new URL(url);
            ValidatableResponse response = httpCall(requestSpecification, urlObj, httpMethod)
                    .then()
                    .log()
                    .everything(true);
            executionLog = requestContext.logResponseInfo(executionLog, writer);

            // 4, auto test
            for (RequestCheckPointDto checkPoint : requestCheckPoints) {
                Matcher matcher = getMatcher(checkPoint);
                try {
                    response.body(checkPoint.getProperty(), matcher);
                } catch (AssertionError error) {
                    setResultFail();
                    checkPoint.fail(error.getMessage());
                }
            }

            // 5, update interface case param
            try {
                response.contentType(ContentType.JSON);
            } catch (AssertionError assertionError) {
                executionLog.append("[Unsupported Content Type]Only JSON content as next request's param supported!");
                throw new UnsupportedOperationException(executionLog.toString());
            }

            ExtractableResponse extractableResponse = response.extract();
            for (UrlResultParamDto resultParam : resultParams) {
                String paramName = resultParam.getName();
                String paramRef = resultParam.getRawValue();
                String value = extractableResponse.path(paramRef);
                if (StringUtils.isNotEmpty(value)) {
                    executeContext.addParam(paramName, value);
                }
            }
            operationExecuteLog.setExecuteInfo(executionLog.toString());
        } catch (Exception e) {
            String message = e.getMessage();
            operationExecuteLog.setExecuteInfo(message);
            setResultFail();
        }  finally {
            executeContext.addOperationLog(
                    endOperationLog(operationExecuteLog)
            );
        }
    }

    private Map<String, String> getQueryParams(List<RequestParamDto> requestParams) {
        Map<String, String> result = new HashMap<>();
        if (!CollectionUtils.isEmpty(requestParams)) {
            List<RequestParamDto> queryParamDtos = requestParams.stream()
                    .filter(script -> script.getParamClass() == ParamClass.GET_PARAM)
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(queryParamDtos)) {
                queryParamDtos.forEach(queryParam -> result.put(queryParam.getName(), queryParam.getValue()));
            }
        }
        return result;
    }

    private void setResultFail() {
        if (!FAIL_RESULT.equals(this.result)) {
            this.result = FAIL_RESULT;
        }
    }

    private void setResultSuccess() {
        this.result = SUCCESS_RESULT;
    }

    private Matcher getMatcher(RequestCheckPointDto checkPoint) {
        switch (checkPoint.getOperation()) {
            case EQUALS:
                return ToStringMatcher.hasToString(checkPoint.getValue());
            case NOTEQUALS:
                return ToStringMatcher.notToString(checkPoint.getValue());
            default:
                throw new UnsupportedOperationException(String.format("不支持的校验操作: %s", checkPoint.getOperation()));
        }
    }

    private Response httpCall(RequestSpecification requestSpecification, URL url, HttpMethod httpMethod) {
        Response response;
        switch (httpMethod) {
            case GET:
                response = requestSpecification.get(url);
                break;
            case POST:
                response = requestSpecification.post(url);
                break;
            case PUT:
                response = requestSpecification.put(url);
                break;
            case DELETE:
                response = requestSpecification.delete(url);
                break;
            default:
                throw new UnsupportedOperationException(String.format("不支持的请求方法: %s", httpMethod.toString()));
        }
        return response;
    }

    @Override
    public OperationType type() {
        return REQUEST;
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public String result() {
        return result;
    }
}
