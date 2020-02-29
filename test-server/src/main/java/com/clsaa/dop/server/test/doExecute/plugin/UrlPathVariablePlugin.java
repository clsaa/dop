package com.clsaa.dop.server.test.doExecute.plugin;

import com.clsaa.dop.server.test.doExecute.Version;
import com.clsaa.dop.server.test.doExecute.context.RequestContext;
import com.clsaa.dop.server.test.enums.ParamClass;
import com.clsaa.dop.server.test.model.dto.RequestParamDto;
import com.clsaa.dop.server.test.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xihao
 * @version 1.0
 * @since 05/05/2019
 */
@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 1000)
public class UrlPathVariablePlugin implements UrlPlugin{

    @Override
    public void apply(RequestContext requestContext) {
        String url = requestContext.getUrl();
        Map<String, String> data = requestContext.getRequestParams()
                .getOrDefault(ParamClass.PATH_PARAM,new ArrayList<>())
                .stream()
                .collect(Collectors.toMap(RequestParamDto::getName, RequestParamDto::getValue));
        requestContext.setUrl(StringUtils.tryToResolveBrace(url, data));
    }

    @Override
    public boolean supports(Version delimiter) {
        return delimiter == Version.V1;
    }
}
