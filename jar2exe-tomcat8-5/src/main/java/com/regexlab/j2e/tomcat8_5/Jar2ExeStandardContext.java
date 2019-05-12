/**
 * Copyright (c) RegExLab.com 2013.05.15
 */

package com.regexlab.j2e.tomcat8_5;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;

/**
 * @author: <a href="mailto:sswater@gmail.com">Shi Shouwei</a>
 */
public class Jar2ExeStandardContext extends StandardContext implements Context {
    
    /**
     * Treat exe as a war
     */
	@Override
    public synchronized void setResources(WebResourceRoot resources) {
        
        if (getDocBase() != null)
            super.setResources(new Jar2ExeWARDirContext());
        else
            super.setResources(resources);
        
    }

}
