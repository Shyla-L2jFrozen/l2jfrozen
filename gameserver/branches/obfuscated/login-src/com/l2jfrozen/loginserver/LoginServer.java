/*
 * Copyright (C) 2004-2016 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jfrozen.loginserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.l2jfrozen.common.CommonConfig;
import com.l2jfrozen.common.CommonConfigFiles;
import com.l2jfrozen.common.L2Frozen;
import com.l2jfrozen.common.ServerType;
import com.l2jfrozen.common.util.Util;
import com.l2jfrozen.common.util.database.L2DatabaseFactory;
import com.l2jfrozen.loginserver.gsregistering.GameServerRegister;
import com.l2jfrozen.loginserver.network.L2LoginClient;
import com.l2jfrozen.loginserver.network.L2LoginPacketHandler;
import com.l2jfrozen.loginserver.util.LoginServerFloodProtectorActions;
import com.l2jfrozen.netcore.NetcoreConfig;
import com.l2jfrozen.netcore.SelectorConfig;
import com.l2jfrozen.netcore.SelectorThread;

import a.a.o;

/**
 * @author KenM
 */
public final class LoginServer
{
	private static final Logger LOGGER = Logger.getLogger(GameServerRegister.class);
	
	public static final int PROTOCOL_REV = 0x0102;
	private static LoginServer _instance;
	private GameServerListener _gameServerListener;
	private SelectorThread<L2LoginClient> _selectorThread;
	private Thread _restartLoginServer;
	
	public static void main(final String[] args) throws Exception
	{
		new LoginServer();
	}
	
	public static LoginServer getInstance()
	{
		return _instance;
	}
	
	private LoginServer() throws Exception
	{
		_instance = this;
		ServerType.serverMode = ServerType.MODE_LOGINSERVER;
		// Create Loggers
		
		final File log4j_conf_file = new File(CommonConfigFiles.LOG4J_CONF_FILE);
		if (!log4j_conf_file.exists())
		{
			throw new IOException("Configuration file " + CommonConfigFiles.LOG4J_CONF_FILE + " is missing");
		}
		
		PropertyConfigurator.configure(CommonConfigFiles.LOG4J_CONF_FILE);
		Logger.getLogger("com.mchange.v2.log.MLog").setLevel(org.apache.log4j.Level.WARN);
		
		Util.printSection("Team");
		
		// Print L2jfrozen's Logo
		L2Frozen.info();
		
		Util.printSection("Login Server");
		
		// Load Config
		NetcoreConfig.getInstance();
		CommonConfig.load();
		LoginConfig.load();
		
		// Prepare Database
		L2DatabaseFactory.getInstance();
		
		try
		{
			LoginController.load();
		}
		catch (final GeneralSecurityException e)
		{
			LOGGER.error("FATAL: Failed initializing LoginController. Reason: " + e.getMessage(), e);
			System.exit(1);
		}
		
		GameServerTable.getInstance();
		
		loadBanFile();
		
		InetAddress bindAddress = null;
		if (!LoginConfig.LOGIN_BIND_ADDRESS.equals("*"))
		{
			try
			{
				bindAddress = InetAddress.getByName(LoginConfig.LOGIN_BIND_ADDRESS);
			}
			catch (final UnknownHostException e)
			{
				LOGGER.warn("WARNING: The LoginServer bind address is invalid, using all avaliable IPs. Reason: " + e.getMessage(), e);
			}
		}
		
		final SelectorConfig sc = new SelectorConfig();
		sc.setMaxReadPerPass(NetcoreConfig.getInstance().MMO_MAX_READ_PER_PASS);
		sc.setMaxSendPerPass(NetcoreConfig.getInstance().MMO_MAX_SEND_PER_PASS);
		sc.setSleepTime(NetcoreConfig.getInstance().MMO_SELECTOR_SLEEP_TIME);
		sc.setHelperBufferCount(NetcoreConfig.getInstance().MMO_HELPER_BUFFER_COUNT);
		
		final L2LoginPacketHandler lph = new L2LoginPacketHandler();
		final SelectorHelper sh = new SelectorHelper();
		try
		{
			_selectorThread = new SelectorThread<>(sc, sh, lph, sh, sh);
		}
		catch (final Exception e)
		{
			LOGGER.error("FATAL: Failed to open Selector. Reason: " + e.getMessage(), e);
			System.exit(1);
		}
		
		// Packets flood instance
		o.a(new LoginServerFloodProtectorActions());
		
		try
		{
			_gameServerListener = new GameServerListener();
			_gameServerListener.start();
			LOGGER.info("Listening for GameServers on " + LoginConfig.GAME_SERVER_LOGIN_HOST + ":" + LoginConfig.GAME_SERVER_LOGIN_PORT);
		}
		catch (final IOException e)
		{
			LOGGER.error("FATAL: Failed to start the Game Server Listener. Reason: " + e.getMessage(), e);
			System.exit(1);
		}
		
		try
		{
			_selectorThread.openServerSocket(bindAddress, LoginConfig.PORT_LOGIN);
			_selectorThread.start();
			LOGGER.info(getClass().getSimpleName() + ": is now listening on: " + LoginConfig.LOGIN_BIND_ADDRESS + ":" + LoginConfig.PORT_LOGIN);
		}
		catch (final Exception e)
		{
			LOGGER.error("FATAL: Failed to open server socket. Reason: " + e.getMessage(), e);
			System.exit(1);
		}
		
	}
	
	public GameServerListener getGameServerListener()
	{
		return _gameServerListener;
	}
	
	private void loadBanFile()
	{
		final File bannedFile = new File(LoginConfigFiles.BANNED_IP);
		if (bannedFile.exists() && bannedFile.isFile())
		{
			try (
				FileInputStream fis = new FileInputStream(bannedFile);
				InputStreamReader is = new InputStreamReader(fis);
				LineNumberReader lnr = new LineNumberReader(is))
			{
				// @formatter:off
				lnr.lines()
						.map(String::trim)
						.filter(l -> !l.isEmpty() && (l.charAt(0) != '#'))
						.forEach(line -> {
							String[] parts = line.split("#", 2); // address[
																	// duration][
																	// #
																	// comments]
								line = parts[0];
								parts = line.split("\\s+"); // durations might
															// be aligned via
															// multiple spaces
								final String address = parts[0];
								long duration = 0;

								if (parts.length > 1) {
									try {
										duration = Long.parseLong(parts[1]);
									} catch (final NumberFormatException nfe) {
										LOGGER.warn("Skipped: Incorrect ban duration ("
												+ parts[1]
												+ ") on ("
												+ bannedFile.getName()
												+ "). Line: "
												+ lnr.getLineNumber());
										return;
									}
								}

								try {
									LoginController
											.getInstance()
											.addBanForAddress(address, duration);
								} catch (final UnknownHostException e) {
									LOGGER.warn("Skipped: Invalid address ("
											+ address + ") on ("
											+ bannedFile.getName()
											+ "). Line: " + lnr.getLineNumber());
								}
							});
				// @formatter:on
			}
			catch (final IOException e)
			{
				LOGGER.warn("Error while reading the bans file (" + bannedFile.getName() + "). Details: " + e.getMessage(), e);
			}
			LOGGER.info("Loaded " + LoginController.getInstance().getBannedIps().size() + " IP Bans.");
		}
		else
		{
			LOGGER.warn("IP Bans file (" + bannedFile.getName() + ") is missing or is a directory, skipped.");
		}
		
		if (LoginConfig.LOGIN_SERVER_SCHEDULE_RESTART)
		{
			LOGGER.info("Scheduled LS restart after " + LoginConfig.LOGIN_SERVER_SCHEDULE_RESTART_TIME + " hours");
			_restartLoginServer = new LoginServerRestart();
			_restartLoginServer.setDaemon(true);
			_restartLoginServer.start();
		}
	}
	
	class LoginServerRestart extends Thread
	{
		public LoginServerRestart()
		{
			setName("LoginServerRestart");
		}
		
		@Override
		public void run()
		{
			while (!isInterrupted())
			{
				try
				{
					Thread.sleep(LoginConfig.LOGIN_SERVER_SCHEDULE_RESTART_TIME * 3600000);
				}
				catch (final InterruptedException e)
				{
					return;
				}
				shutdown(true);
			}
		}
	}
	
	public void shutdown(final boolean restart)
	{
		Runtime.getRuntime().exit(restart ? 2 : 0);
	}
}
