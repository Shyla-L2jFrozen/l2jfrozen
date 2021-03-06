package com.l2jfrozen;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class Config {

	private static Logger logger = Logger.getGlobal();
	private static String CONFIG_FILE="./config/manager.properties";
	
	public static int THREAD_P_EFFECTS = 10;
	public static int THREAD_P_GENERAL = 13;
	public static int IO_PACKET_THREAD_CORE_SIZE = 2;
	public static int GENERAL_PACKET_THREAD_CORE_SIZE = 4;
	public static int GENERAL_THREAD_CORE_SIZE = 4;
	public static int AI_MAX_THREAD = 4;
	
	public static String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	public static String DATABASE_URL = "jdbc:mysql://localhost/managerdb";
	public static String DATABASE_LOGIN = "root";
	public static String DATABASE_PASSWORD = "root";
	public static long DATABASE_CONNECTION_TIMEOUT = 200000;
	public static int DATABASE_MAX_CONNECTIONS = 100;
	public static int DATABASE_MAX_IDLE_TIME = 0;
	
	public static String NETCORE_FILE = "l2jfrozen-netcore.jar";
	public static String NETCORE_VERSION = "0.0.1-SNAPSHOT-obf";
	
	public static void loadConfig()
	{
		
		logger.info("Loading Configuration..");
		
		String managerConfigfile = System.getProperty("MANAGER_CONFIG_FILE");
		if(managerConfigfile!=null 
				&& !managerConfigfile.isEmpty()){
			logger.info("Loading file from System Property configuration \n\t"+managerConfigfile);
			CONFIG_FILE = managerConfigfile;
		}
		
		try
		{
			Properties serverSettings = new Properties();
			
			File file = new File(CONFIG_FILE);
			if(file.exists()){
				logger.info("Loading configuration file..");
				InputStream is = new FileInputStream(file);
				serverSettings.load(is);
				is.close();
			}else{
				
				InputStream is = Config.class.getClassLoader().getResourceAsStream("manager.properties");
				if(is!=null){
					
					logger.info("Loading configuration resource..");
					serverSettings.load(is);
					is.close();
				}else{
					logger.info("Loading default configuration..");
				}
				
			}
			
			DATABASE_DRIVER = serverSettings.getProperty("Driver", "com.mysql.jdbc.Driver");
			DATABASE_URL = serverSettings.getProperty("URL", "jdbc:mysql://localhost/managerdb");
			DATABASE_LOGIN = serverSettings.getProperty("Login", "root");
			DATABASE_PASSWORD = serverSettings.getProperty("Password", "dsadj32rWRwwrwq");
			DATABASE_MAX_CONNECTIONS = Integer.parseInt(serverSettings.getProperty("MaximumDbConnections", "10"));
			DATABASE_MAX_IDLE_TIME = Integer.parseInt(serverSettings.getProperty("MaximumDbIdleTime", "0"));
			DATABASE_CONNECTION_TIMEOUT = Integer.parseInt(serverSettings.getProperty("SingleConnectionTimeOutDb", "120000"));
			
			THREAD_P_EFFECTS = Integer.parseInt(serverSettings.getProperty("ThreadPoolSizeEffects", "6"));
			THREAD_P_GENERAL = Integer.parseInt(serverSettings.getProperty("ThreadPoolSizeGeneral", "15"));
			GENERAL_PACKET_THREAD_CORE_SIZE = Integer.parseInt(serverSettings.getProperty("GeneralPacketThreadCoreSize", "4"));
			IO_PACKET_THREAD_CORE_SIZE = Integer.parseInt(serverSettings.getProperty("UrgentPacketThreadCoreSize", "2"));
			AI_MAX_THREAD = Integer.parseInt(serverSettings.getProperty("AiMaxThread", "10"));
			GENERAL_THREAD_CORE_SIZE = Integer.parseInt(serverSettings.getProperty("GeneralThreadCoreSize", "4"));

			NETCORE_FILE = serverSettings.getProperty("NetcoreFile", "l2jfrozen-netcore.jar");
			NETCORE_VERSION = serverSettings.getProperty("NetcoreVersion", "0.0.1-SNAPSHOT");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
