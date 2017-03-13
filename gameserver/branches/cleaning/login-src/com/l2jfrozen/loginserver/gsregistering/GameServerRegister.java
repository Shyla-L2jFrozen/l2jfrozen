/*
 * L2jFrozen Project - www.l2jfrozen.com 
 * 
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
package com.l2jfrozen.loginserver.gsregistering;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.LogManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.l2jfrozen.CommonConfig;
import com.l2jfrozen.CommonConfigFiles;
import com.l2jfrozen.ServerType;
import com.l2jfrozen.loginserver.LoginConfig;
import com.l2jfrozen.loginserver.datatables.GameServerTable;
import com.l2jfrozen.util.CloseUtil;
import com.l2jfrozen.util.database.DatabaseUtils;
import com.l2jfrozen.util.database.L2DatabaseFactory;
import com.l2jfrozen.util.random.Rnd;

public class GameServerRegister
{
	private static final Logger LOGGER = Logger.getLogger(GameServerRegister.class);
	private static String _choice;
	private static boolean _choiceOk;
	
	public static void main(final String[] args) throws IOException
	{
		// Create Loggers
		final File log_conf_file = new File(CommonConfigFiles.LOG_CONF_FILE);
		if (!log_conf_file.exists())
		{
			throw new IOException("Configuration file " + CommonConfigFiles.LOG_CONF_FILE + " is missing");
		}
		
		// Local Constants
		final String LOG_FOLDER = "log/gsregister";
		
		// Create LOGGER folder
		File logFolder = new File(LOG_FOLDER);
		logFolder.mkdir();
		
		InputStream is = new FileInputStream(log_conf_file);
		LogManager.getLogManager().readConfiguration(is);
		is.close();
		is = null;
		logFolder = null;
		
//		final String LOG_FOLDER_BASE = "log"; // Name of folder for LOGGER base file
//		final File logFolderBase = new File(LOG_FOLDER_BASE);
//		logFolderBase.mkdir();
		
		final File log4j_conf_file = new File(CommonConfigFiles.LOG4J_CONF_FILE);
		if (!log4j_conf_file.exists())
		{
			throw new IOException("Configuration file " + CommonConfigFiles.LOG4J_CONF_FILE + " is missing");
		}
		
		PropertyConfigurator.configure(CommonConfigFiles.LOG4J_CONF_FILE);
		
		ServerType.serverMode = ServerType.MODE_LOGINSERVER;
		LoginConfig.load();
		final LineNumberReader _in = new LineNumberReader(new InputStreamReader(System.in));
		try
		{
			GameServerTable.load();
		}
		catch (final Exception e)
		{
			LOGGER.info("FATAL: Failed loading GameServerTable. Reason: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		final GameServerTable gameServerTable = GameServerTable.getInstance();
		LOGGER.info("Welcome to L2JFrozen GameServer Regitering");
		LOGGER.info("Enter The id of the server you want to register");
		LOGGER.info("Type 'help' to get a list of ids.");
		LOGGER.info("Type 'clean' to unregister all currently registered gameservers on this LoginServer.");
		while (!_choiceOk)
		{
			LOGGER.info("Your choice:");
			_choice = _in.readLine();
			if (_choice.equalsIgnoreCase("help"))
			{
				for (final Map.Entry<Integer, String> entry : gameServerTable.getServerNames().entrySet())
				{
					LOGGER.info("Server: ID: " + entry.getKey() + "\t- " + entry.getValue() + " - In Use: " + (gameServerTable.hasRegisteredGameServerOnId(entry.getKey()) ? "YES" : "NO"));
				}
				LOGGER.info("You can also see servername.xml");
			}
			else if (_choice.equalsIgnoreCase("clean"))
			{
				System.out.print("This is going to UNREGISTER ALL servers from this LoginServer. Are you sure? (y/n) ");
				_choice = _in.readLine();
				if (_choice.equals("y"))
				{
					GameServerRegister.cleanRegisteredGameServersFromDB();
					gameServerTable.getRegisteredGameServers().clear();
				}
				else
				{
					LOGGER.info("ABORTED");
				}
			}
			else
			{
				try
				{
					final int id = Integer.parseInt(_choice);
					final int size = gameServerTable.getServerNames().size();
					if (size == 0)
					{
						LOGGER.info("No server names avalible, please make sure that servername.xml is in the LoginServer directory.");
						System.exit(1);
					}
					
					_choice = "";
					
					while (!_choice.equalsIgnoreCase(""))
					{
						LOGGER.info("External Server Ip:");
						_choice = _in.readLine();
					}
					
					final String ip = _choice;
					
					final String name = gameServerTable.getServerNameById(id);
					if (name == null)
					{
						LOGGER.info("No name for id: " + id);
						continue;
					}
					
					if (gameServerTable.hasRegisteredGameServerOnId(id))
					{
						LOGGER.info("This id is not free");
					}
					else
					{
						final byte[] hexId = generateHex(16);
						gameServerTable.registerServerOnDB(hexId, id, ip);
						saveHexid(id, new BigInteger(hexId).toString(16), "hexid.txt");
						LOGGER.info("Server Registered hexid saved to 'hexid.txt'");
						LOGGER.info("Put this file in the /config folder of your gameserver.");
						return;
					}
				}
				catch (final NumberFormatException nfe)
				{
					if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
						nfe.printStackTrace();
					
					LOGGER.info("Please, type a number or 'help'");
				}
			}
		}
	}
	
	public static void cleanRegisteredGameServersFromDB()
	{
		java.sql.Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection(false);
			statement = con.prepareStatement("DELETE FROM gameservers");
			statement.executeUpdate();
			DatabaseUtils.close(statement);
		}
		catch (final SQLException e)
		{
			if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
				e.printStackTrace();
			
			LOGGER.info("SQL error while cleaning registered servers: " + e);
		}
		finally
		{
			CloseUtil.close(con);
			con = null;
		}
	}
	
	public static byte[] generateHex(final int size)
	{
		final byte[] array = new byte[size];
		Rnd.nextBytes(array);
		if (CommonConfig.DEBUG)
		{
			LOGGER.debug("Generated random String:  \"" + array + "\"");
		}
		return array;
	}
	
	private static void saveHexid(final int serverId, final String hexId, final String fileName)
	{
		OutputStream out = null;
		try
		{
			final Properties hexSetting = new Properties();
			final File file = new File(fileName);
			if (file.createNewFile())
			{
				out = new FileOutputStream(file);
				hexSetting.setProperty("ServerID", String.valueOf(serverId));
				hexSetting.setProperty("HexID", hexId);
				hexSetting.store(out, "the hexID to auth into login");
			}
		}
		catch (final Exception e)
		{
			LOGGER.warn("Failed to save hex id to " + fileName + " File.");
			e.printStackTrace();
		}
		finally
		{
			
			if (out != null)
				try
				{
					out.close();
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
			
		}
	}
}