/**
 * Copyright (c) RegExLab.com 2013.05.15
 */

package com.regexlab.j2e.tomcat7;

import javax.naming.directory.DirContext;

import org.apache.catalina.Context;
import org.apache.catalina.core.StandardContext;

/**
 * @author: <a href="mailto:sswater@gmail.com">Shi Shouwei</a>
 */
public class Jar2ExeStandardContext extends StandardContext implements Context {
    
    /**
     * Treat exe as a war
     */
    public synchronized void setResources(DirContext resources) {
        
        if ((getDocBase() != null) && (getDocBase().toLowerCase().endsWith(".exe")))
            super.setResources(new Jar2ExeWARDirContext());
        else
            super.setResources(resources);
        
    }

}
