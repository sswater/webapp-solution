/**
 * Copyright (c) RegExLab.com 2013.05.15
 */

package com.regexlab.j2e.tomcat7;

import java.io.File;
import java.util.zip.ZipFile;

import javax.naming.directory.DirContext;

import org.apache.naming.resources.WARDirContext;

/**
 * @author: <a href="mailto:sswater@gmail.com">Shi Shouwei</a>
 */
public class Jar2ExeWARDirContext extends WARDirContext implements DirContext {
    
    /**
     * Copied from parent and modified
     */
	@Override
    public void setDocBase(String docBase) {

        // Validate the format of the proposed document root
        if (docBase == null)
            throw new IllegalArgumentException
                (sm.getString("resources.null"));

        // Calculate a File object referencing this document base directory
        File base = new File(docBase);

        // Validate that the document base is an existing directory
        if (!base.exists() || !base.canRead() || base.isDirectory())
            throw new IllegalArgumentException
                (sm.getString("warResources.invalidWar", new Object[]{docBase}));
        try {
            this.base = new ZipFile(base);
        } catch (Exception e) {
            throw new IllegalArgumentException
                (sm.getString("warResources.invalidWar", new Object[]{e.getMessage()}));
        }
        
        // BaseDirContext.setDocBase(docBase);
        this.docBase = docBase;

        loadEntries();

    }

}
