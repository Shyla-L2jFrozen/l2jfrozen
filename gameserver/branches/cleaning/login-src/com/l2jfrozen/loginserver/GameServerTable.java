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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.l2jfrozen.common.CommonConfig;
import com.l2jfrozen.common.util.database.L2DatabaseFactory;
import com.l2jfrozen.common.util.random.Rnd;
import com.l2jfrozen.loginserver.network.gameserverpackets.ServerStatus;
import com.l2jfrozen.netcore.util.IPSubnet;

import javolution.io.UTF8StreamReader;
import javolution.xml.stream.XMLStreamConstants;
import javolution.xml.stream.XMLStreamException;
import javolution.xml.stream.XMLStreamReaderImpl;

/**
 * The Class GameServerTable loads the game server names and initialize the game server tables.
 * @author KenM, Zoey76
 */
public final class GameServerTable
{
	
	private static Logger LOGGER = Logger.getLogger(GameServerTable.class);
	
	// Server Names
	private static final Map<Integer, String> SERVER_NAMES = new HashMap<>();
	// Game Server Table
	private static final Map<Integer, GameServerInfo> GAME_SERVER_TABLE = new HashMap<>();
	// RSA Config
	private static final int KEYS_SIZE = 10;
	private KeyPair[] _keyPairs;
	
	/**
	 * Instantiates a new game server table.
	 */
	public GameServerTable()
	{
		try
		{
			loadServerNames();
		}
		catch (final IOException e)
		{
			LOGGER.error("Error loading Game Servers names from file", e);
		}
		
		loadRegisteredGameServers();
		LOGGER.info("{" + getClass().getSimpleName() + "}: Loaded {" + GAME_SERVER_TABLE.size() + "} registered Game Servers.");
		
		initRSAKeys();
		LOGGER.info("{" + getClass().getSimpleName() + "}: Cached {" + _keyPairs.length + "} RSA keys for Game Server communication.");
	}
	
	private void loadServerNames() throws IOException
	{
		SERVER_NAMES.clear();
		
		final XMLStreamReaderImpl xpp = new XMLStreamReaderImpl();
		final UTF8StreamReader reader = new UTF8StreamReader();
		
		InputStream in = null;
		try
		{
			final File conf_file = new File(LoginConfigFiles.SERVER_NAME_FILE);
			if (!conf_file.exists())
			{
				throw new IOException("Configuration file " + LoginConfigFiles.SERVER_NAME_FILE + " is missing");
			}
			
			in = new FileInputStream(conf_file);
			xpp.setInput(reader.setInput(in));
			
			for (int e = xpp.getEventType(); e != XMLStreamConstants.END_DOCUMENT; e = xpp.next())
			{
				if (e == XMLStreamConstants.START_ELEMENT)
				{
					if (xpp.getLocalName().toString().equals("server"))
					{
						Integer id = new Integer(xpp.getAttributeValue(null, "id").toString());
						String name = xpp.getAttributeValue(null, "name").toString();
						SERVER_NAMES.put(id, name);
						
						id = null;
						name = null;
					}
				}
			}
			
		}
		catch (final FileNotFoundException e)
		{
			if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
				e.printStackTrace();
			
			LOGGER.warn("servername.xml could not be loaded: file not found");
		}
		catch (final XMLStreamException xppe)
		{
			xppe.printStackTrace();
		}
		finally
		{
			try
			{
				xpp.close();
			}
			catch (final XMLStreamException e)
			{
				if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
					e.printStackTrace();
			}
			
			try
			{
				reader.close();
			}
			catch (final IOException e)
			{
				if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
					e.printStackTrace();
			}
			
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (final IOException e)
				{
					if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
						e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Inits the RSA keys.
	 */
	private void initRSAKeys()
	{
		try
		{
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(new RSAKeyGenParameterSpec(512, RSAKeyGenParameterSpec.F4));
			_keyPairs = new KeyPair[KEYS_SIZE];
			for (int i = 0; i < KEYS_SIZE; i++)
			{
				_keyPairs[i] = keyGen.genKeyPair();
			}
		}
		catch (final Exception e)
		{
			LOGGER.error("{}: Error loading RSA keys for Game Server communication!", e);
		}
	}
	
	/**
	 * Load registered game servers.
	 */
	private void loadRegisteredGameServers()
	{
		try (
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			Statement ps = con.createStatement();
			ResultSet rs = ps.executeQuery("SELECT * FROM gameservers"))
		{
			int id;
			while (rs.next())
			{
				id = rs.getInt("server_id");
				GAME_SERVER_TABLE.put(id, new GameServerInfo(id, stringToHex(rs.getString("hexid"))));
			}
		}
		catch (final Exception e)
		{
			LOGGER.error("{}: Error loading registered game servers!", e);
		}
	}
	
	/**
	 * Gets the registered game servers.
	 * @return the registered game servers
	 */
	public Map<Integer, GameServerInfo> getRegisteredGameServers()
	{
		return GAME_SERVER_TABLE;
	}
	
	/**
	 * Gets the registered game server by id.
	 * @param id the game server Id
	 * @return the registered game server by id
	 */
	public GameServerInfo getRegisteredGameServerById(final int id)
	{
		return GAME_SERVER_TABLE.get(id);
	}
	
	/**
	 * Checks for registered game server on id.
	 * @param id the id
	 * @return true, if successful
	 */
	public boolean hasRegisteredGameServerOnId(final int id)
	{
		return GAME_SERVER_TABLE.containsKey(id);
	}
	
	/**
	 * Register with first available id.
	 * @param gsi the game server information DTO
	 * @return true, if successful
	 */
	public boolean registerWithFirstAvailableId(final GameServerInfo gsi)
	{
		// avoid two servers registering with the same "free" id
		synchronized (GAME_SERVER_TABLE)
		{
			for (final Integer serverId : SERVER_NAMES.keySet())
			{
				if (!GAME_SERVER_TABLE.containsKey(serverId))
				{
					GAME_SERVER_TABLE.put(serverId, gsi);
					gsi.setId(serverId);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Register a game server.
	 * @param id the id
	 * @param gsi the gsi
	 * @return true, if successful
	 */
	public boolean register(final int id, final GameServerInfo gsi)
	{
		// avoid two servers registering with the same id
		synchronized (GAME_SERVER_TABLE)
		{
			if (!GAME_SERVER_TABLE.containsKey(id))
			{
				GAME_SERVER_TABLE.put(id, gsi);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Wrapper method.
	 * @param gsi the game server info DTO.
	 */
	public void registerServerOnDB(final GameServerInfo gsi)
	{
		registerServerOnDB(gsi.getHexId(), gsi.getId(), gsi.getExternalHost());
	}
	
	/**
	 * Register server on db.
	 * @param hexId the hex id
	 * @param id the id
	 * @param externalHost the external host
	 */
	public void registerServerOnDB(final byte[] hexId, final int id, final String externalHost)
	{
		register(id, new GameServerInfo(id, hexId));
		try (
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO gameservers (hexid,server_id,host) values (?,?,?)"))
		{
			ps.setString(1, hexToString(hexId));
			ps.setInt(2, id);
			ps.setString(3, externalHost);
			ps.executeUpdate();
		}
		catch (final Exception e)
		{
			LOGGER.error("{}: Error while saving gameserver!", e);
		}
	}
	
	/**
	 * Gets the server name by id.
	 * @param id the id
	 * @return the server name by id
	 */
	public String getServerNameById(final int id)
	{
		return SERVER_NAMES.get(id);
	}
	
	/**
	 * Gets the server names.
	 * @return the game server names map.
	 */
	public Map<Integer, String> getServerNames()
	{
		return SERVER_NAMES;
	}
	
	/**
	 * Gets the key pair.
	 * @return a random key pair.
	 */
	public KeyPair getKeyPair()
	{
		return _keyPairs[Rnd.nextInt(10)];
	}
	
	/**
	 * String to hex.
	 * @param string the string to convert.
	 * @return return the hex representation.
	 */
	private byte[] stringToHex(final String string)
	{
		return new BigInteger(string, 16).toByteArray();
	}
	
	/**
	 * Hex to string.
	 * @param hex the hex value to convert.
	 * @return the string representation.
	 */
	private String hexToString(final byte[] hex)
	{
		if (hex == null)
		{
			return "null";
		}
		return new BigInteger(hex).toString(16);
	}
	
	
	/**
	 * Gets the single instance of GameServerTable.
	 * @return single instance of GameServerTable
	 */
	public static GameServerTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	/**
	 * The Class SingletonHolder.
	 */
	private static class SingletonHolder
	{
		protected static final GameServerTable _instance = new GameServerTable();
	}
}
