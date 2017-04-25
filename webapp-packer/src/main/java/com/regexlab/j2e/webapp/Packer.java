package com.regexlab.j2e.webapp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Packer {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		// config
		Properties prop = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
		if(in != null) prop.load(in);
		
		// verbose
		boolean verbose = "TRUE".equalsIgnoreCase(prop.getProperty("verbose", "false"));
		
		// j2ewiz
		String j2ewiz_path = prop.getProperty("j2ewiz.path", "j2ewiz");
		
		// check j2ewiz
		try {
			if( ! "false".equalsIgnoreCase(prop.getProperty("check.j2ewiz", "true")) ) {
				Runtime.getRuntime().exec(new String[]{j2ewiz_path, "/help"});
			}
		}
		catch(Exception e) {
			System.err.println("Jar2Exe not installed. Please add 'j2ewiz.path=<path to j2ewiz>' in config file.");
			return;
		}
		
		// parse arguments
		String war = null;
		String jar = null;
		List<String> other_args = new ArrayList<String>();
		for(String a : args) {
			if(a.toLowerCase().endsWith(".war")) war = a;
			else if(a.toLowerCase().endsWith(".jar")) jar = a;
			else other_args.add(a);
		}
		
		if(war == null || jar == null) {
			System.err.println("Please specify a .war file and a .jar file with the starter program.");
			return;
		}
		
		// create a temp directory
		File t = new File(System.getProperty("user.dir") + "/temp/webapp-" + System.currentTimeMillis());
		t.mkdirs();
		if(verbose) System.out.println("Temp directory: " + t);
		
		// lib sub directory
		File lib = new File(t + "/lib");
		lib.mkdirs();
		
		try {
			// unpack war
			unpackWar(war, t, lib, verbose);
			
			// pack exe
			if(verbose) System.out.println("\nPreparing arguments ...");
			List<String> j2ewiz_args = new ArrayList<String>();
			
			j2ewiz_args.add(j2ewiz_path);
			j2ewiz_args.add("/encrypt");
			
			j2ewiz_args.add("/jar");
			j2ewiz_args.add(jar);
			if(verbose) System.out.println("Jar file : " + jar);
			
			j2ewiz_args.add("/keepflat");
			j2ewiz_args.add(t + "/web.zip");
			if(verbose) System.out.println("Web resource file : " + t + "/web.zip");
			
			j2ewiz_args.add("/embed");
			j2ewiz_args.add(t + "/classes.zip" + ", protect");
			if(verbose) System.out.println("Classes file : " + t + "/classes.zip");
			
			for(File j : lib.listFiles()) {
				j2ewiz_args.add("/embed");
				j2ewiz_args.add(j + ", protect");
				if(verbose) System.out.println("Lib file : " + j);
			}

			j2ewiz_args.addAll(other_args);
			
			// execute
			if(verbose) System.out.println("\nExecuting 'j2ewiz' ...");
			Process j2ewiz_process = Runtime.getRuntime().exec(j2ewiz_args.toArray(new String[0]));
			BufferedReader reader = new BufferedReader(new InputStreamReader(j2ewiz_process.getInputStream()));
			String line = null;
			while((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			
			// wait
			j2ewiz_process.waitFor();
			System.out.println("Create finished.");
		}
		finally {
			if(!verbose) {
				for(File f : lib.listFiles()) {
					f.delete();
				}
				lib.delete();
				for(File f : t.listFiles()) {
					f.delete();
				}
				t.delete();
			}
		}
	}

	private static void unpackWar(String war, File t, File lib, boolean verbose) {
		
		if(verbose) System.out.println("\nUnpacking : '" + war + "' to '" + t + "' ...");
		
		try {
			ZipInputStream warin = new ZipInputStream(new FileInputStream(war));
			ZipOutputStream webzip = new ZipOutputStream(new FileOutputStream(t + "/web.zip"));
			ZipOutputStream classeszip = new ZipOutputStream(new FileOutputStream(t + "/classes.zip"));
			
			ZipEntry e = null;
			while((e = warin.getNextEntry()) != null) {
				
				if(e.getName().startsWith("WEB-INF/classes/")) {
					String name = e.getName().substring("WEB-INF/classes/".length());
					classeszip.putNextEntry(new ZipEntry(name));
					copyStream(warin, classeszip);
				}
				else if(e.getName().startsWith("WEB-INF/lib/")) {
					if(verbose) System.out.println(e.getName());
					if(e.isDirectory()) continue;
					String name = e.getName().substring("WEB-INF/lib/".length());
					OutputStream out = new BufferedOutputStream(new FileOutputStream(lib + "/" + name));
					copyStream(warin, out);
					out.close();
				}
				else {
					webzip.putNextEntry(new ZipEntry(e.getName()));
					copyStream(warin, webzip);
				}
			}
		
			classeszip.close();
			webzip.close();
			warin.close();
		}
		catch(Exception e) {
			System.err.println("The .war file is not valid.");
			e.printStackTrace();
			System.exit(1);
		}
		
	}

	private static void copyStream(InputStream in, OutputStream out) throws IOException {
		byte [] buf = new byte[1024];
		int red = 0;
		while((red = in.read(buf)) > 0) {
			out.write(buf, 0, red);
		}
	}

}
