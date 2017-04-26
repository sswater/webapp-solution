package com.regexlab.j2e.startjetty9;

import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class HelloJettyWebapp {

	public static void main(String[] args) throws Exception {

		Server server = new Server(8080);

		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		webapp.setWar(System.getProperty("j2e.app.path"));
		webapp.setExtractWAR(false);
		URL webxml = Thread.currentThread().getContextClassLoader().getResource("WEB-INF/web.xml");
		if (webxml != null) {
			webapp.setDescriptor(webxml.toString());
		}
		server.setHandler(webapp);

		server.start();
		server.dumpStdErr();
		server.join();

	}

}
