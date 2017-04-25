package com.regexlab.j2e.starter7;

import org.apache.catalina.Context;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import com.regexlab.j2e.tomcat7.Jar2ExeTomcat;

/**
 * demo starter for Jar2Exe
 * 
 * @author sswater
 */
public class HelloWebapp {

    public static void main(String[] args) throws Exception {
        
        // Tomcat instance
        Tomcat tomcat = new Jar2ExeTomcat();
        tomcat.setPort(80);
        tomcat.setBaseDir(System.getProperty("java.io.tmpdir", "."));

        // deploy exe file self as a war file
        Context context = tomcat.addWebapp("", System.getProperty("j2e.app.path"));
        
        // do not extract war file
        ((StandardContext)context).setUnpackWAR(false);
        
        // Start server
        tomcat.start();
        tomcat.getServer().await();
        
    }
}
