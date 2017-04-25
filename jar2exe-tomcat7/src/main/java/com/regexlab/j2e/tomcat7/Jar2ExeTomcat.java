/**
 * Copyright (c) RegExLab.com 2013.05.15
 */

package com.regexlab.j2e.tomcat7;

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
    public Context addContext(Host host, String contextPath, String dir) {
        silence(contextPath);
        Context ctx = new Jar2ExeStandardContext(); // modified
        ctx.setPath( contextPath );
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
    public Context addWebapp(Host host, String url, String path) {
        silence(url);

        Context ctx = new Jar2ExeStandardContext(); // modified
        ctx.setPath( url );
        ctx.setDocBase(path);
        if (defaultRealm == null) {
            initSimpleAuth();
        }
        ctx.setRealm(defaultRealm);

        ctx.addLifecycleListener(new DefaultWebXmlListener());
        
        ContextConfig ctxCfg = new ContextConfig();
        ctx.addLifecycleListener(ctxCfg);
        
        // prevent it from looking ( if it finds one - it'll have dup error )
        ctxCfg.setDefaultWebXml("org/apache/catalin/startup/NO_DEFAULT_XML");
        
        if (host == null) {
            getHost().addChild(ctx);
        } else {
            host.addChild(ctx);
        }

        return ctx;
    }
    
    private void silence(String ctx) {
        String base = "org.apache.catalina.core.ContainerBase.[default].[";
        base += getHost().getName();
        base += "].[";
        base += ctx; 
        base += "]";
        Logger.getLogger(base).setLevel(Level.WARNING);
    }

}
