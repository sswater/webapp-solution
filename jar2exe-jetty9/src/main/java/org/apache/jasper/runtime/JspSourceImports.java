package org.apache.jasper.runtime;

import java.util.Set;

public abstract interface JspSourceImports
{
	public abstract Set<String> getPackageImports();
	public abstract Set<String> getClassImports();
}
