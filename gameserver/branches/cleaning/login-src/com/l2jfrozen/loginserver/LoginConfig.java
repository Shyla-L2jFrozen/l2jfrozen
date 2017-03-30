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
package com.l2jfrozen.loginserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Shyla
 */
public class LoginConfig
{
	private static final Logger LOGGER = Logger.getLogger(LoginConfig.class);
	
	public static int BRUT_LOGON_ATTEMPTS;
	public static int BRUT_AVG_TIME;
	public static int BRUT_BAN_IP_TIME;
	
	public static boolean FLOOD_PROTECTION;
	public static int FAST_CONNECTION_LIMIT;
	public static int MAX_CONNECTION_PER_IP;
	public static long FAST_CONNECTION_TIME;
	public static long NORMAL_CONNECTION_TIME;
	
	public static String GAME_SERVER_LOGIN_HOST;
	public static int GAME_SERVER_LOGIN_PORT;
	public static boolean ACCEPT_NEW_GAMESERVER;
	
	public static String NETWORK_IP_LIST;
	public static boolean DEBUG_PACKETS;
	public static String LOGIN_BIND_ADDRESS;
	public static int PORT_LOGIN;
	
	public static long SESSION_TTL;
	public static int MAX_LOGINSESSIONS;
	public static int LOGIN_TRY_BEFORE_BAN;
	public static long LOGIN_BLOCK_AFTER_BAN;
	public static boolean AUTO_CREATE_ACCOUNTS;
	
	public static boolean ENABLE_DDOS_PROTECTION_SYSTEM;
	public static String DDOS_COMMAND_BLOCK;
	public static boolean ENABLE_DEBUG_DDOS_PROTECTION_SYSTEM;

	public static long LOGIN_SERVER_SCHEDULE_RESTART_TIME;
	public static boolean LOGIN_SERVER_SCHEDULE_RESTART;
	
	public static void load()
	{
		load(LoginConfigFiles.LOGIN_CONFIGURATION_FILE);
	}
	
	public static void load(final String configurationFile)
	{
		try
		{
			final Properties serverSettings = new Properties();
			final InputStream is = new FileInputStream(new File(configurationFile));
			serverSettings.load(is);
			is.close();
			
			GAME_SERVER_LOGIN_HOST = serverSettings.getProperty("LoginHostname", "*");
			GAME_SERVER_LOGIN_PORT = Integer.parseInt(serverSettings.getProperty("LoginPort", "9013"));
			
			LOGIN_BIND_ADDRESS = serverSettings.getProperty("LoginserverHostname", "*");
			PORT_LOGIN = Integer.parseInt(serverSettings.getProperty("LoginserverPort", "2106"));
			
			ACCEPT_NEW_GAMESERVER = Boolean.parseBoolean(serverSettings.getProperty("AcceptNewGameServer", "True"));
			
			LOGIN_TRY_BEFORE_BAN = Integer.parseInt(serverSettings.getProperty("LoginTryBeforeBan", "10"));
			LOGIN_BLOCK_AFTER_BAN = Integer.parseInt(serverSettings.getProperty("LoginBlockAfterBan", "600"));
			
			ENABLE_DDOS_PROTECTION_SYSTEM = Boolean.parseBoolean(serverSettings.getProperty("EnableDdosProSystem", "false"));
			DDOS_COMMAND_BLOCK = serverSettings.getProperty("Deny_noallow_ip_ddos", "/sbin/iptables -I INPUT -p tcp --dport 7777 -s $IP -j ACCEPT");
			ENABLE_DEBUG_DDOS_PROTECTION_SYSTEM = Boolean.parseBoolean(serverSettings.getProperty("Fulllog_mode_print", "false"));
			
			// Anti Brute force attack on login
			BRUT_AVG_TIME = Integer.parseInt(serverSettings.getProperty("BrutAvgTime", "30")); // in Seconds
			BRUT_LOGON_ATTEMPTS = Integer.parseInt(serverSettings.getProperty("BrutLogonAttempts", "15"));
			BRUT_BAN_IP_TIME = Integer.parseInt(serverSettings.getProperty("BrutBanIpTime", "900")); // in Seconds
			
			AUTO_CREATE_ACCOUNTS = Boolean.parseBoolean(serverSettings.getProperty("AutoCreateAccounts", "True"));
			
			FLOOD_PROTECTION = Boolean.parseBoolean(serverSettings.getProperty("EnableFloodProtection", "True"));
			FAST_CONNECTION_LIMIT = Integer.parseInt(serverSettings.getProperty("FastConnectionLimit", "15"));
			NORMAL_CONNECTION_TIME = Integer.parseInt(serverSettings.getProperty("NormalConnectionTime", "700"));
			FAST_CONNECTION_TIME = Integer.parseInt(serverSettings.getProperty("FastConnectionTime", "350"));
			MAX_CONNECTION_PER_IP = Integer.parseInt(serverSettings.getProperty("MaxConnectionPerIP", "50"));
			
			NETWORK_IP_LIST = serverSettings.getProperty("NetworkList", "");
			SESSION_TTL = Long.parseLong(serverSettings.getProperty("SessionTTL", "25000"));
			MAX_LOGINSESSIONS = Integer.parseInt(serverSettings.getProperty("MaxSessions", "200"));
			
			DEBUG_PACKETS = Boolean.parseBoolean(serverSettings.getProperty("DebugPackets", "false"));
			
			LOGIN_SERVER_SCHEDULE_RESTART = Boolean.parseBoolean(serverSettings.getProperty("LoginRestartSchedule", "false"));
			LOGIN_SERVER_SCHEDULE_RESTART_TIME = Long.parseLong(serverSettings.getProperty("LoginRestartTime", "24"));
			
		}
		catch (final Exception e)
		{
			LOGGER.error("Failed to Load " + configurationFile + " File.", e);
		}
		
	}
	
}
