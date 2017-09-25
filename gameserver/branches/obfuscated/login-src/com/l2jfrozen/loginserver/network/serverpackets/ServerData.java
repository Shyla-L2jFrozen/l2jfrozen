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
package com.l2jfrozen.loginserver.network.serverpackets;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import com.l2jfrozen.loginserver.GameServerInfo;
import com.l2jfrozen.loginserver.network.L2LoginClient;
import com.l2jfrozen.loginserver.network.gameserverpackets.ServerStatus;

/**
 * @author Shyla
 *
 */
public class ServerData
{
	protected static final Logger _log = Logger.getLogger(ServerData.class.getName());
	
	protected byte[] _ip;
	protected int _port;
	protected int _ageLimit;
	protected boolean _pvp;
	protected int _currentPlayers;
	protected int _maxPlayers;
	protected boolean _brackets;
	protected boolean _clock;
	protected int _status;
	protected int _serverId;
	protected int _serverType;
	
	public ServerData(final L2LoginClient client, final GameServerInfo gsi)
	{
		try
		{
			_ip = InetAddress.getByName(gsi.getServerAddress(client.gco().getInetAddress())).getAddress();
		}
		catch (final UnknownHostException e)
		{
			_log.warning(getClass().getSimpleName() + ": " + e.getMessage());
			_ip = new byte[4];
			_ip[0] = 127;
			_ip[1] = 0;
			_ip[2] = 0;
			_ip[3] = 1;
		}
		
		_port = gsi.getPort();
		_pvp = gsi.isPvp();
		_serverType = gsi.getServerType();
		_currentPlayers = gsi.getCurrentPlayerCount();
		_maxPlayers = gsi.getMaxPlayers();
		_ageLimit = 0;
		_brackets = gsi.isShowingBrackets();
		// If server GM-only - show status only to GMs
		_status = gsi.getStatus() != ServerStatus.STATUS_GM_ONLY ? gsi.getStatus() : client.getAccessLevel() > 0 ? gsi.getStatus() : ServerStatus.STATUS_DOWN;
		_serverId = gsi.getId();
	}
}