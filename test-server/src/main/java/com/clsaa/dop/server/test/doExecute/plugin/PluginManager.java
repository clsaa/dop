package com.clsaa.dop.server.test.doExecute.plugin;

import com.clsaa.dop.server.test.doExecute.Version;
import com.clsaa.dop.server.test.doExecute.context.RequestContext;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author xihao
 * @version 1.0
 * @since 12/04/2019
 */
@Component
public class PluginManager {

    @Autowired
    @Qualifier("urlPluginRegistry")
    private PluginRegistry<UrlPlugin, Version> urlPlugins;

    @Autowired
    @Qualifier("requestHeaderPluginRegistry")
    private PluginRegistry<RequestHeaderPlugin, Version> headerPlugins;

    public String url(RequestContext requestContext) {
        Version version = requestContext.getVersion();
        for (UrlPlugin urlPlugin : urlPlugins.getPluginsFor(version)) {
            urlPlugin.apply(requestContext);
        }
        return requestContext.getUrl();
    }

    public Map<String, String> headers(RequestContext requestContext) {
        Version version = requestContext.getVersion();
        for (RequestHeaderPlugin headerPlugin : headerPlugins.getPluginsFor(version)) {
            headerPlugin.apply(requestContext);
        }
        return requestContext.getRequestHeaders();
    }

}
