/**
 * Copyright (c) RegExLab.com 2013.05.15
 */

package com.regexlab.j2e.tomcat8_5;

import java.io.File;
import org.apache.catalina.Host;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.JarResourceSet;
import org.apache.catalina.webresources.StandardRoot;

/**
 * @author: <a href="mailto:sswater@gmail.com">Shi Shouwei</a>
 */
public class Jar2ExeWARDirContext extends StandardRoot implements WebResourceRoot {
    
    /**
     * Copied from parent and modified
     */
	@Override
    protected WebResourceSet createMainResourceSet() {
        String docBase = getContext().getDocBase();

        WebResourceSet mainResourceSet;
        if (docBase == null) {
            mainResourceSet = new EmptyResourceSet(this);
        } else {
            File f = new File(docBase);
            if (!f.isAbsolute()) {
                f = new File(((Host)getContext().getParent()).getAppBaseFile(), f.getPath());
            }
            if (f.isDirectory()) {
                mainResourceSet = new DirResourceSet(this, "/", f.getAbsolutePath(), "/");
            } else if(f.isFile() /* && docBase.endsWith(".war") */) { // modified
                mainResourceSet = new JarResourceSet(this, "/", f.getAbsolutePath(), "/");
            } else {
                throw new IllegalArgumentException(
                        sm.getString("standardRoot.startInvalidMain",
                                new Object[]{ f.getAbsolutePath() }));
            }
        }

        return mainResourceSet;
    }

}
