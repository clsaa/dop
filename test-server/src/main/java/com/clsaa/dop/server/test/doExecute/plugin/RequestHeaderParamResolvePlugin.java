package com.clsaa.dop.server.test.doExecute.plugin;

import com.clsaa.dop.server.test.doExecute.Version;
import com.clsaa.dop.server.test.doExecute.context.RequestContext;
import com.clsaa.dop.server.test.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xihao
 * @version 1.0
 * @since 12/04/2019
 */
@Component
public class RequestHeaderParamResolvePlugin implements RequestHeaderPlugin {

    @Override
    public boolean supports(Version delimiter) {
        return delimiter == Version.V1;
    }

    @Override
    public void apply(RequestContext requestContext) {
        Map<String, String> headers = requestContext.getRequestHeaders();
        Map<String, String> params = requestContext.getCaseParams();
        headers.forEach((name,value) -> {
            String valueResolved = StringUtils.tryToResolveDollar(value, params);
            if (!value.equals(valueResolved)) {
                headers.put(name, valueResolved);
            }
        });
    }
}
