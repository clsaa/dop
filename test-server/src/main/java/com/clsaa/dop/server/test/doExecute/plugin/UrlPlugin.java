package com.clsaa.dop.server.test.doExecute.plugin;

import com.clsaa.dop.server.test.doExecute.Version;
import com.clsaa.dop.server.test.doExecute.context.RequestContext;
import org.springframework.plugin.core.Plugin;

/**
 * @author xihao
 * @version 1.0
 * @since 12/04/2019
 */
public interface UrlPlugin extends Plugin<Version> {

    void apply(RequestContext requestContext);
}
