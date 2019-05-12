/**
 * Copyright (c) RegExLab.com 2013.05.15
 */

package com.regexlab.j2e.tomcat8_5;

import java.util.logging.*;

import org.apache.catalina.*;
import org.apache.catalina.startup.*;

/**
 * @author: <a href="mailto:sswater@gmail.com">Shi Shouwei</a>
 */
public class Jar2ExeTomcat extends Tomcat {

    /**
     * Copied from parent and modified
     */
	@Override
    public Context addContext(Host host, String contextPath, String contextName,
            String dir) {
        silence(host, contextPath);
        Context ctx = new Jar2ExeStandardContext(); // modified // createContext(host, contextPath);
        ctx.setName(contextName);
        ctx.setPath(contextPath);
        ctx.setDocBase(dir);
        ctx.addLifecycleListener(new FixContextListener());

        if (host == null) {
            getHost().addChild(ctx);
        } else {
            host.addChild(ctx);
        }
        return ctx;
    }
    
    /**
     * Copied from parent and modified
     */
	@Override
    public Context addWebapp(Host host, String contextPath, String docBase,
            LifecycleListener config) {

        silence(host, contextPath);

        Context ctx = new Jar2ExeStandardContext(); // modified // createContext(host, contextPath);
        ctx.setPath(contextPath);
        ctx.setDocBase(docBase);
        ctx.addLifecycleListener(getDefaultWebXmlListener());
        ctx.setConfigFile(getWebappConfigFile(docBase, contextPath));

        ctx.addLifecycleListener(config);

        if (config instanceof ContextConfig) {
            // prevent it from looking ( if it finds one - it'll have dup error )
            ((ContextConfig) config).setDefaultWebXml(noDefaultWebXmlPath());
        }

        if (host == null) {
            getHost().addChild(ctx);
        } else {
            host.addChild(ctx);
        }

        return ctx;
    }
    
    private void silence(Host host, String ctx) {
        Logger.getLogger(getLoggerName(host, ctx)).setLevel(Level.WARNING);
    }
    
    private String getLoggerName(Host host, String ctx) {
        String loggerName = "org.apache.catalina.core.ContainerBase.[default].[";
        if (host == null) {
            loggerName += getHost().getName();
        } else {
            loggerName += host.getName();
        }
        loggerName += "].[";
        loggerName += ctx;
        loggerName += "]";
        return loggerName;
    }

}
