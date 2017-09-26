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
package com.l2jfrozen.loginserver.network.serverpackets;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.l2jfrozen.loginserver.GameServerInfo;
import com.l2jfrozen.loginserver.GameServerTable;
import com.l2jfrozen.loginserver.network.L2LoginClient;
import com.l2jfrozen.loginserver.network.gameserverpackets.ServerStatus;

/**
 * ServerList
 * 
 * <pre>
 * Format: cc [cddcchhcdc]
 * 
 * c: server list size (number of servers)
 * c: ?
 * [ (repeat for each servers)
 * c: server id (ignored by client?)
 * d: server ip
 * d: server port
 * c: age limit (used by client?)
 * c: pvp or not (used by client?)
 * h: current number of players
 * h: max number of players
 * c: 0 if server is down
 * d: 2nd bit: clock
 *    3rd bit: won't display server name
 *    4th bit: test server (used by client?)
 * c: 0 if you don't want to display brackets in front of sever name
 * ]
 * </pre>
 * 
 * Server will be considered as Good when the number of online players<br>
 * is less than half the maximum. as Normal between half and 4/5<br>
 * and Full when there's more than 4/5 of the maximum number of players.
 */
public final class ServerList extends L2LoginServerPacket
{
	protected static final Logger _log = Logger.getLogger(ServerList.class.getName());
	
	private final List<ServerData> _servers;
	private final int _lastServer;
	private final Map<Integer, Integer> _charsOnServers;
	private final Map<Integer, long[]> _charsToDelete;
	
	
	
	public ServerList(final L2LoginClient client)
	{
		_servers = new ArrayList<>(GameServerTable.getInstance().getRegisteredGameServers().size());
		_lastServer = client.getLastServer();
		for (final GameServerInfo gsi : GameServerTable.getInstance().getRegisteredGameServers().values())
		{
			_servers.add(new ServerData(client, gsi));
		}
		_charsOnServers = client.getCharsOnServ();
		_charsToDelete = client.getCharsWaitingDelOnServ();
	}
	
	@Override
	public void write()
	{
		C(0x04);
		C(_servers.size());
		C(_lastServer);
		for (final ServerData server : _servers)
		{
			C(server._serverId); // server id
			
			C(server._ip[0] & 0xff);
			C(server._ip[1] & 0xff);
			C(server._ip[2] & 0xff);
			C(server._ip[3] & 0xff);
			
			D(server._port);
			C(server._ageLimit); // Age Limit 0, 15, 18
			C(server._pvp ? 0x01 : 0x00);
			H(server._currentPlayers);
			H(server._maxPlayers);
			C(server._status == ServerStatus.STATUS_DOWN ? 0x00 : 0x01);
			D(server._serverType); // 1: Normal, 2: Relax, 4: Public Test, 8: No Label, 16: Character Creation Restricted, 32: Event, 64: Free
			C(server._brackets ? 0x01 : 0x00);
		}
		H(0x00); // unknown
		if (_charsOnServers != null)
		{
			C(_charsOnServers.size());
			for (final int servId : _charsOnServers.keySet())
			{
				C(servId);
				C(_charsOnServers.get(servId));
				if ((_charsToDelete == null) || !_charsToDelete.containsKey(servId))
				{
					C(0x00);
				}
				else
				{
					C(_charsToDelete.get(servId).length);
					for (final long deleteTime : _charsToDelete.get(servId))
					{
						D((int) ((deleteTime - System.currentTimeMillis()) / 1000));
					}
				}
			}
		}
		else
		{
			C(0x00);
		}
	}
}
