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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.l2jfrozen.loginserver.network.gameserverpackets.ServerStatus;

/**
 * @author Shyla
 *
 */
/**
 * The Class GameServerInfo.
 */
public class GameServerInfo
{
	// auth
	private int _id;
	private final byte[] _hexId;
	private boolean _isAuthed;
	// status
	private GameServerThread _gst;
	private int _status;
	// network
	private final ArrayList<GameServerAddress> _addrs = new ArrayList<>(5);
	private int _port;
	// config
	private final boolean _isPvp = true;
	private int _serverType;
	private int _ageLimit;
	private boolean _isShowingBrackets;
	private int _maxPlayers;
	
	/**
	 * Instantiates a new game server info.
	 * @param id the id
	 * @param hexId the hex id
	 * @param gst the gst
	 */
	public GameServerInfo(final int id, final byte[] hexId, final GameServerThread gst)
	{
		_id = id;
		_hexId = hexId;
		_gst = gst;
		_status = ServerStatus.STATUS_DOWN;
	}
	
	/**
	 * Instantiates a new game server info.
	 * @param id the id
	 * @param hexId the hex id
	 */
	public GameServerInfo(final int id, final byte[] hexId)
	{
		this(id, hexId, null);
	}
	
	/**
	 * Sets the id.
	 * @param id the new id
	 */
	public void setId(final int id)
	{
		_id = id;
	}
	
	/**
	 * Gets the id.
	 * @return the id
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Gets the hex id.
	 * @return the hex id
	 */
	public byte[] getHexId()
	{
		return _hexId;
	}
	
	public String getName()
	{
		// this value can't be stored in a private variable because the ID can be changed by setId()
		return GameServerTable.getInstance().getServerNameById(_id);
	}
	
	/**
	 * Sets the authed.
	 * @param isAuthed the new authed
	 */
	public void setAuthed(final boolean isAuthed)
	{
		_isAuthed = isAuthed;
	}
	
	/**
	 * Checks if is authed.
	 * @return true, if is authed
	 */
	public boolean isAuthed()
	{
		return _isAuthed;
	}
	
	/**
	 * Sets the game server thread.
	 * @param gst the new game server thread
	 */
	public void setGameServerThread(final GameServerThread gst)
	{
		_gst = gst;
	}
	
	/**
	 * Gets the game server thread.
	 * @return the game server thread
	 */
	public GameServerThread getGameServerThread()
	{
		return _gst;
	}
	
	/**
	 * Sets the status.
	 * @param status the new status
	 */
	public void setStatus(final int status)
	{
		_status = status;
	}
	
	/**
	 * Gets the status.
	 * @return the status
	 */
	public int getStatus()
	{
		return _status;
	}
	
	public String getStatusName()
	{
		switch (_status)
		{
			case 0:
				return "Auto";
			case 1:
				return "Good";
			case 2:
				return "Normal";
			case 3:
				return "Full";
			case 4:
				return "Down";
			case 5:
				return "GM Only";
			default:
				return "Unknown";
		}
	}
	
	/**
	 * Gets the current player count.
	 * @return the current player count
	 */
	public int getCurrentPlayerCount()
	{
		if (_gst == null)
		{
			return 0;
		}
		return _gst.getPlayerCount();
	}
	
	/**
	 * Gets the external host.
	 * @return the external host
	 */
	public String getExternalHost()
	{
		try
		{
			return getServerAddress(InetAddress.getByName("0.0.0.0"));
		}
		catch (final Exception e)
		{
			
		}
		return null;
	}
	
	/**
	 * Gets the port.
	 * @return the port
	 */
	public int getPort()
	{
		return _port;
	}
	
	/**
	 * Sets the port.
	 * @param port the new port
	 */
	public void setPort(final int port)
	{
		_port = port;
	}
	
	/**
	 * Sets the max players.
	 * @param maxPlayers the new max players
	 */
	public void setMaxPlayers(final int maxPlayers)
	{
		_maxPlayers = maxPlayers;
	}
	
	/**
	 * Gets the max players.
	 * @return the max players
	 */
	public int getMaxPlayers()
	{
		return _maxPlayers;
	}
	
	/**
	 * Checks if is pvp.
	 * @return true, if is pvp
	 */
	public boolean isPvp()
	{
		return _isPvp;
	}
	
	/**
	 * Sets the age limit.
	 * @param val the new age limit
	 */
	public void setAgeLimit(final int val)
	{
		_ageLimit = val;
	}
	
	/**
	 * Gets the age limit.
	 * @return the age limit
	 */
	public int getAgeLimit()
	{
		return _ageLimit;
	}
	
	/**
	 * Sets the server type.
	 * @param val the new server type
	 */
	public void setServerType(final int val)
	{
		_serverType = val;
	}
	
	/**
	 * Gets the server type.
	 * @return the server type
	 */
	public int getServerType()
	{
		return _serverType;
	}
	
	/**
	 * Sets the showing brackets.
	 * @param val the new showing brackets
	 */
	public void setShowingBrackets(final boolean val)
	{
		_isShowingBrackets = val;
	}
	
	/**
	 * Checks if is showing brackets.
	 * @return true, if is showing brackets
	 */
	public boolean isShowingBrackets()
	{
		return _isShowingBrackets;
	}
	
	/**
	 * Sets the down.
	 */
	public void setDown()
	{
		setAuthed(false);
		setPort(0);
		setGameServerThread(null);
		setStatus(ServerStatus.STATUS_DOWN);
	}
	
	/**
	 * Adds the server address.
	 * @param subnet the subnet
	 * @param addr the addr
	 * @throws UnknownHostException the unknown host exception
	 */
	public void addServerAddress(final String subnet, final String addr) throws UnknownHostException
	{
		_addrs.add(new GameServerAddress(subnet, addr));
	}
	
	/**
	 * Gets the server address.
	 * @param addr the addr
	 * @return the server address
	 */
	public String getServerAddress(final InetAddress addr)
	{
		for (final GameServerAddress a : _addrs)
		{
			if (a.equals(addr))
			{
				return a.getServerAddress();
			}
		}
		return null; // should not happens
	}
	
	/**
	 * Gets the server addresses.
	 * @return the server addresses
	 */
	public String[] getServerAddresses()
	{
		final String[] result = new String[_addrs.size()];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = _addrs.get(i).toString();
		}
		
		return result;
	}
	
	/**
	 * Clear server addresses.
	 */
	public void clearServerAddresses()
	{
		_addrs.clear();
	}
	
	
}

