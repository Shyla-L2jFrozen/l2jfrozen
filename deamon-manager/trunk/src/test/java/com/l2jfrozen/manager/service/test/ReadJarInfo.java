package com.l2jfrozen.manager.service.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.jar.Attributes.Name;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReadJarInfo {

	public static void main(String[] args) throws IOException {
		
		String jarFilePath = "lib/l2jfrozen-netcore-0.0.2-SNAPSHOT-obf.jar";
		JarFile jarFile = new JarFile(jarFilePath);
		JarEntry pomPropertiesEntry = jarFile.getJarEntry("META-INF/maven/com.l2jfrozen/l2jfrozen-netcore/pom.properties");
		InputStream pomPropertiesIs = jarFile.getInputStream( pomPropertiesEntry );
		Properties pomProperties = new Properties();
		pomProperties.load(pomPropertiesIs);
		System.out.println(pomProperties.getProperty("version"));
		
		for(Object current:jarFile.getManifest().getMainAttributes().keySet()){
			System.out.println(current+"="+jarFile.getManifest().getMainAttributes().getValue((Name) current));
		}
		
	}

}
