package com.l2jfrozen.manager.service.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import a.a.g;
import a.a.i;

public class JarUpdateTest {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		String newFile = "l2jfrozen-netcore-0.0.2-SNAPSHOT.jar";
		String original = "l2jfrozen-netcore-0.0.1-SNAPSHOT-obf.jar";
		//String[] command = new String[]{"cmd","/c","java","-cp","lib/l2jfrozen-common*.jar","com.l2jfrozen.common.util.updater.FilesUpdater",newFile,original};
		String command = "cmd /c java -cp lib/l2jfrozen-common-0.0.1-SNAPSHOT.jar com.l2jfrozen.common.util.updater.FilesUpdater "+newFile+" "+original;
		
		Runtime.getRuntime().exec(command);
		
//		ProcessBuilder pb = new ProcessBuilder("java","-cp","lib/l2jfrozen-common*.jar","com.l2jfrozen.common.util.updater.FilesUpdater",newFile,original);
//		Process p = pb.start();
		
		Thread.sleep(30000);
	}
	
	

}
