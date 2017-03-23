/* L2jFrozen Project - www.l2jfrozen.com 
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jfrozen.loginserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.logging.LogManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.l2jfrozen.CommonConfig;
import com.l2jfrozen.CommonConfigFiles;
import com.l2jfrozen.L2Frozen;
import com.l2jfrozen.ServerType;
import com.l2jfrozen.loginserver.datatables.GameServerTable;
import com.l2jfrozen.loginserver.util.LoginServerFloodProtectorActions;
import com.l2jfrozen.netcore.NetcoreConfig;
import com.l2jfrozen.netcore.SelectorConfig;
import com.l2jfrozen.netcore.SelectorThread;
import com.l2jfrozen.netcore.util.PacketsFloodProtector;
import com.l2jfrozen.util.Util;
import com.l2jfrozen.util.database.L2DatabaseFactory;
import com.l2jfrozen.util.database.SqlUtils;

public class LoginServer
{
	public static final int PROTOCOL_REV = 0x0102;
	
	private static LoginServer _instance;
	private final Logger LOGGER = Logger.getLogger(LoginServer.class);
	private GameServerListener _gameServerListener;
	// public static SelectorThread<LoginClient> _selectorThread;
	
	public static void main(final String[] args) throws Exception
	{
		_instance = new LoginServer();
	}
	
	public static LoginServer getInstance()
	{
		return _instance;
	}
	
	public LoginServer() throws Exception
	{
		
		// Create Loggers
		final File log_conf_file = new File(CommonConfigFiles.LOG_CONF_FILE);
		if (!log_conf_file.exists())
		{
			throw new IOException("Configuration file " + CommonConfigFiles.LOG_CONF_FILE + " is missing");
		}
		
		// Local Constants
		final String LOG_FOLDER = "log/login";
		
		// Create LOGGER folder
		File logFolder = new File(LOG_FOLDER);
		logFolder.mkdir();
		
		InputStream is = new FileInputStream(log_conf_file);
		LogManager.getLogManager().readConfiguration(is);
		is.close();
		is = null;
		logFolder = null;
		
		// final String LOG_FOLDER_BASE = "log"; // Name of folder for LOGGER base file
		// final File logFolderBase = new File(LOG_FOLDER_BASE);
		// logFolderBase.mkdir();
		
		final File log4j_conf_file = new File(CommonConfigFiles.LOG4J_CONF_FILE);
		if (!log4j_conf_file.exists())
		{
			throw new IOException("Configuration file " + CommonConfigFiles.LOG4J_CONF_FILE + " is missing");
		}
		
		PropertyConfigurator.configure(CommonConfigFiles.LOG4J_CONF_FILE);
		ServerType.serverMode = ServerType.MODE_LOGINSERVER;
		
		// Team info
		Util.printSection("Team");
		L2Frozen.info();
		
		// Load Configs
		NetcoreConfig.getInstance();
		CommonConfig.load();
		LoginConfig.load();
		
		Util.printSection("Database");
		// Prepare Database
		try
		{
			L2DatabaseFactory.getInstance();
		}
		catch (final SQLException e)
		{
			LOGGER.fatal("Failed initializing database", e);
			System.exit(1);
		}
		
		try
		{
			LoginController.load();
		}
		catch (final GeneralSecurityException e)
		{
			LOGGER.fatal("Failed initializing LoginController", e);
			System.exit(1);
		}
		
		try
		{
			GameServerTable.load();
		}
		catch (final GeneralSecurityException e)
		{
			LOGGER.fatal("Failed to load GameServerTable", e);
			System.exit(1);
		}
		catch (final Exception e)
		{
			LOGGER.fatal("Failed to load GameServerTable", e);
			
			if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
				e.printStackTrace();
			
			System.exit(1);
		}
		
		InetAddress bindAddress = null;
		if (!LoginConfig.LOGIN_BIND_ADDRESS.equals("*"))
		{
			try
			{
				bindAddress = InetAddress.getByName(LoginConfig.LOGIN_BIND_ADDRESS);
			}
			catch (final UnknownHostException e1)
			{
				LOGGER.warn("WARNING: The LoginServer bind address is invalid, using all avaliable IPs", e1);
			}
		}
		
		final SelectorConfig sc = new SelectorConfig();
		sc.setMaxReadPerPass(NetcoreConfig.getInstance().MMO_MAX_READ_PER_PASS);
		sc.setMaxSendPerPass(NetcoreConfig.getInstance().MMO_MAX_SEND_PER_PASS);
		sc.setSleepTime(NetcoreConfig.getInstance().MMO_SELECTOR_SLEEP_TIME);
		sc.setHelperBufferCount(NetcoreConfig.getInstance().MMO_HELPER_BUFFER_COUNT);
		
		final LoginPacketHandler lph = new LoginPacketHandler();
		final SelectorHelper sh = new SelectorHelper();
		SelectorThread<LoginClient> _selectorThread = null;
		try
		{
			_selectorThread = new SelectorThread<>(sc, sh, lph, sh, sh);
		}
		catch (final IOException e)
		{
			LOGGER.fatal("Failed to open Selector", e);
			System.exit(1);
		}
		
		// Packets flood instance
		PacketsFloodProtector.setProtectedServer(new LoginServerFloodProtectorActions());
		
		try
		{
			_gameServerListener = new GameServerListener();
			_gameServerListener.start();
			LOGGER.info("Listening for GameServers on " + LoginConfig.GAME_SERVER_LOGIN_HOST + ":" + LoginConfig.GAME_SERVER_LOGIN_PORT);
		}
		catch (final IOException e)
		{
			LOGGER.fatal("Failed to start the Game Server Listener" + e);
			System.exit(1);
		}
		
		try
		{
			_selectorThread.openServerSocket(bindAddress, LoginConfig.PORT_LOGIN);
			_selectorThread.start();
			LOGGER.info("Login Server ready on " + (bindAddress == null ? "*" : bindAddress.getHostAddress()) + ":" + LoginConfig.PORT_LOGIN);
			
		}
		catch (final IOException e)
		{
			LOGGER.error("Failed to open server socket", e);
			System.exit(1);
		}
		
		// load bannedIps
		try
		{
			LoginController.loadBanFile();
		}
		catch (final IOException e)
		{
			LOGGER.error("Failed to load ban ip file", e);
			
		}
		
		bindAddress = null;
	}
	
	public GameServerListener getGameServerListener()
	{
		return _gameServerListener;
	}
	
	public void shutdown(final boolean restart)
	{
		LoginController.getInstance().shutdown();
		SqlUtils.OpzLogin();
		System.gc();
		Runtime.getRuntime().exit(restart ? 2 : 0);
	}
}
