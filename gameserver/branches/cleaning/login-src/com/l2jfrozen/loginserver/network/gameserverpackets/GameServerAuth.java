/*
 * L2jFrozen Project - www.l2jfrozen.com 
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
package com.l2jfrozen.loginserver.network.gameserverpackets;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.l2jfrozen.CommonConfig;
import com.l2jfrozen.loginserver.GameServerTable;
import com.l2jfrozen.loginserver.GameServerTable.GameServerInfo;
import com.l2jfrozen.loginserver.GameServerThread;
import com.l2jfrozen.loginserver.LoginConfig;
import com.l2jfrozen.loginserver.network.GameServerState;
import com.l2jfrozen.loginserver.network.loginserverpackets.AuthResponse;
import com.l2jfrozen.loginserver.network.loginserverpackets.LoginServerFail;
import com.l2jfrozen.util.network.BaseRecievePacket;

/**
 * Format: cccddb c desired ID c accept alternative ID c reserve Host s ExternalHostName s InetranlHostName d max players d hexid size b hexid
 * @author -Wooden-
 */
public class GameServerAuth extends BaseRecievePacket
{
	protected static Logger LOGGER = Logger.getLogger(GameServerAuth.class);
	private final byte[] _hexId;
	private final int _desiredId;
	private final boolean _hostReserved;
	private final boolean _acceptAlternativeId;
	private final int _maxPlayers;
	private final int _port;
	// private final String _externalHost;
	// private final String _internalHost;
	private final GameServerThread _server;
	private final String[] _hosts;
	
	/**
	 * @param decrypt
	 * @param server
	 */
	public GameServerAuth(final byte[] decrypt, final GameServerThread server)
	{
		super(decrypt);
		
		_server = server;
		_desiredId = readC();
		_acceptAlternativeId = readC() == 0 ? false : true;
		_hostReserved = readC() == 0 ? false : true;
		
		_hosts = new String[2];
		for (int i = 0; i < 2; i++)
		{
			_hosts[i] = readS();
		}
		
		// _externalHost = readS();
		// _internalHost = readS();
		_port = readH();
		_maxPlayers = readD();
		
		final int size = readD();
		
		_hexId = readB(size);
		
		if (CommonConfig.DEBUG)
		{
			LOGGER.info("Auth request received");
		}
		
		if (handleRegProcess())
		{
			final AuthResponse ar = new AuthResponse(server.getGameServerInfo().getId());
			server.sendPacket(ar);
			if (CommonConfig.DEBUG)
			{
				LOGGER.info("Authed: id: " + server.getGameServerInfo().getId());
			}
			server.setLoginConnectionState(GameServerState.AUTHED);
		}
	}
	
	/*
	 * public byte[] getHexID() { return _hexId; } public boolean getHostReserved() { return _hostReserved; } public int getDesiredID() { return _desiredId; } public boolean acceptAlternateID() { return _acceptAlternativeId; } public int getMaxPlayers() { return _maxPlayers; } public String
	 * getExternalHost() { return _externalHost; } public String getInternalHost() { return _internalHost; } public int getPort() { return _port; }
	 */
	
	private boolean handleRegProcess()
	{
		final GameServerTable gameServerTable = GameServerTable.getInstance();
		
		final int id = _desiredId;
		final byte[] hexId = _hexId;
		
		GameServerInfo gsi = gameServerTable.getRegisteredGameServerById(id);
		// is there a gameserver registered with this id?
		if (gsi != null)
		{
			// does the hex id match?
			if (Arrays.equals(gsi.getHexId(), hexId))
			{
				// check to see if this GS is already connected
				synchronized (gsi)
				{
					if (gsi.isAuthed())
					{
						_server.forceClose(LoginServerFail.REASON_ALREADY_LOGGED8IN);
						return false;
					}
					_server.attachGameServerInfo(gsi, _port, _hosts, _maxPlayers);
				}
			}
			else
			{
				// there is already a server registered with the desired id and different hex id
				// try to register this one with an alternative id
				if (LoginConfig.ACCEPT_NEW_GAMESERVER && _acceptAlternativeId)
				{
					gsi = new GameServerInfo(id, hexId, _server);
					if (gameServerTable.registerWithFirstAvailableId(gsi))
					{
						_server.attachGameServerInfo(gsi, _port, _hosts, _maxPlayers);
						gameServerTable.registerServerOnDB(gsi);
					}
					else
					{
						_server.forceClose(LoginServerFail.REASON_NO_FREE_ID);
						return false;
					}
				}
				else
				{
					// server id is already taken, and we cant get a new one for you
					_server.forceClose(LoginServerFail.REASON_WRONG_HEXID);
					return false;
				}
			}
		}
		else
		{
			// can we register on this id?
			if (LoginConfig.ACCEPT_NEW_GAMESERVER)
			{
				gsi = new GameServerInfo(id, hexId, _server);
				if (gameServerTable.register(id, gsi))
				{
					_server.attachGameServerInfo(gsi, _port, _hosts, _maxPlayers);
					gameServerTable.registerServerOnDB(gsi);
				}
				else
				{
					// some one took this ID meanwhile
					_server.forceClose(LoginServerFail.REASON_ID_RESERVED);
					return false;
				}
			}
			else
			{
				_server.forceClose(LoginServerFail.REASON_WRONG_HEXID);
				return false;
			}
		}
		
		return true;
	}
	
}
