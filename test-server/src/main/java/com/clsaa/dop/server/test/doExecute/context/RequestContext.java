package com.clsaa.dop.server.test.doExecute.context;

import com.clsaa.dop.server.test.doExecute.Version;
import com.clsaa.dop.server.test.enums.HttpMethod;
import com.clsaa.dop.server.test.enums.ParamClass;
import com.clsaa.dop.server.test.model.dto.RequestParamDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xihao
 * @version 1.0
 * @since 12/04/2019
 */
@Data
@Builder
public class RequestContext {

    public Version version;

    private String url;

    private Map<String, String> requestHeaders;

    private String requestBody;

    private Map<String, String> caseParams;

    private Map<ParamClass, List<RequestParamDto>> requestParams;

    public void fillRequestParams(List<RequestParamDto> requestParamDtos) {
        if (CollectionUtils.isEmpty(requestParamDtos)) {
            this.requestParams = new HashMap<>();
            return;
        }
        this.requestParams = requestParamDtos.stream()
                .collect(Collectors.groupingBy(RequestParamDto::getParamClass));
    }

    public RequestContext url(StringBuilder origin, HttpMethod method, String url) {
        char nextLine = '\n';
        origin.append(nextLine)
                .append(method)
                .append(" ")
                .append(url)
                .append(nextLine);
        return this;
    }

    public RequestContext headers(StringBuilder origin, Map<String, String> headers) {
        char nextLine = '\n';
        origin.append(nextLine)
                .append("Request Headers:")
                .append(nextLine);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            String name = header.getKey();
            String value = header.getValue();
            origin.append(' ').append(name).append(" : ").append(value).append(' ').append(nextLine);
        }
        return this;
    }

    public RequestContext body(StringBuilder origin, String body) {
        char nextLine = '\n';
        origin.append(nextLine)
                .append("Request Body:")
                .append(nextLine)
                .append(body);
        return this;
    }

    public StringBuilder get(StringBuilder s) {
        return s;
    }

    public StringBuilder logRequestInfo(Map<String, String> headers, String body, HttpMethod method, String url) {
        StringBuilder origin = new StringBuilder();
        return url(origin, method, url).headers(origin, headers).body(origin, body).get(origin);
    }

    public StringBuilder logResponseInfo(StringBuilder origin, StringWriter writer) {
        char nextLine = '\n';
        origin.append(nextLine)
                .append(nextLine)
                .append("Response: ")
                .append(nextLine)
                .append(writer.toString().trim());
        return origin;
    }
}
