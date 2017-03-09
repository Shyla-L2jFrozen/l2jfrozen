/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jfrozen;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Shyla
 */
public class CommonConfig
{
	
	private static final Logger LOGGER = Logger.getLogger(CommonConfig.class);
	
	public static boolean ENABLE_ALL_EXCEPTIONS;
	public static boolean ASSERT;
	public static boolean DEBUG;
	
	// ThreadPoolManager
	public static int THREAD_P_EFFECTS;
	public static int THREAD_P_GENERAL;
	public static int IO_PACKET_THREAD_CORE_SIZE;
	public static int GENERAL_PACKET_THREAD_CORE_SIZE;
	public static int GENERAL_THREAD_CORE_SIZE;
	public static int AI_MAX_THREAD;
	
	// DatabasePool
	public static Object DATABASE_POOL_TYPE;
	public static String DATABASE_DRIVER;
	public static String DATABASE_URL;
	public static int DATABASE_MAX_CONNECTIONS;
	public static String DATABASE_LOGIN;
	public static String DATABASE_PASSWORD;
	public static int DATABASE_PARTITION_COUNT;
	public static long DATABASE_CONNECTION_TIMEOUT;
	public static long DATABASE_TIMEOUT;
	public static int DATABASE_MAX_IDLE_TIME;
	
	// load statically the configuration files
	static
	{
		load();
	}
	
	public static void load()
	{
		load(CommonConfigFiles.COMMON_CONFIGURATION_FILE);
	}
	
	public static void load(final String configurationFile)
	{
		
		try
		{
			final Properties serverSettings = new Properties();
			final InputStream is = new FileInputStream(new File(configurationFile));
			serverSettings.load(is);
			is.close();
			
			DEBUG = Boolean.parseBoolean(serverSettings.getProperty("Debug", "false"));
			ASSERT = Boolean.parseBoolean(serverSettings.getProperty("Assert", "false"));
			ENABLE_ALL_EXCEPTIONS = Boolean.parseBoolean(serverSettings.getProperty("EnableAllExceptionsLog", "false"));
			
			DATABASE_POOL_TYPE = serverSettings.getProperty("DatabasePoolType", "c3p0");
			DATABASE_DRIVER = serverSettings.getProperty("Driver", "com.mysql.jdbc.Driver");
			DATABASE_URL = serverSettings.getProperty("URL", "jdbc:mysql://localhost/l2jdb");
			DATABASE_LOGIN = serverSettings.getProperty("Login", "root");
			DATABASE_PASSWORD = serverSettings.getProperty("Password", "");
			DATABASE_MAX_CONNECTIONS = Integer.parseInt(serverSettings.getProperty("MaximumDbConnections", "10"));
			DATABASE_MAX_IDLE_TIME = Integer.parseInt(serverSettings.getProperty("MaximumDbIdleTime", "0"));
			
			DATABASE_TIMEOUT = Integer.parseInt(serverSettings.getProperty("TimeOutConDb", "0"));
			DATABASE_CONNECTION_TIMEOUT = Integer.parseInt(serverSettings.getProperty("SingleConnectionTimeOutDb", "120000"));
			DATABASE_PARTITION_COUNT = Integer.parseInt(serverSettings.getProperty("PartitionCount", "4"));
			
			THREAD_P_EFFECTS = Integer.parseInt(serverSettings.getProperty("ThreadPoolSizeEffects", "6"));
			THREAD_P_GENERAL = Integer.parseInt(serverSettings.getProperty("ThreadPoolSizeGeneral", "15"));
			GENERAL_PACKET_THREAD_CORE_SIZE = Integer.parseInt(serverSettings.getProperty("GeneralPacketThreadCoreSize", "4"));
			IO_PACKET_THREAD_CORE_SIZE = Integer.parseInt(serverSettings.getProperty("UrgentPacketThreadCoreSize", "2"));
			AI_MAX_THREAD = Integer.parseInt(serverSettings.getProperty("AiMaxThread", "10"));
			GENERAL_THREAD_CORE_SIZE = Integer.parseInt(serverSettings.getProperty("GeneralThreadCoreSize", "4"));
			
		}
		catch (final Exception e)
		{
			LOGGER.error("Failed to Load " + configurationFile + " File.", e);
		}
	}
	
}
