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
package com.l2jfrozen.gameserver.network.clientpackets;

import org.apache.log4j.Logger;

import com.l2jfrozen.common.CommonConfig;
import com.l2jfrozen.gameserver.LoginServerThread;
import com.l2jfrozen.gameserver.network.L2GameClient;
import a.a.aa;

public final class AuthLogin extends L2GameClientPacket
{
	private static Logger LOGGER = Logger.getLogger(AuthLogin.class);
	
	// loginName + keys must match what the loginserver used.
	private String _loginName;
	private int _playKey1;
	private int _playKey2;
	private int _loginKey1;
	private int _loginKey2;
	
	@Override
	protected void readImpl()
	{
		_loginName = S().toLowerCase();
		_playKey2 = D();
		_playKey1 = D();
		_loginKey1 = D();
		_loginKey2 = D();
	}
	
	@Override
	protected void runImpl()
	{
		final L2GameClient client = g();
		if (_loginName.isEmpty())
		{
			client.closeNow();
			return;
		}
		
		final aa key = new aa(_loginKey1, _loginKey2, _playKey1, _playKey2);
		if (CommonConfig.DEBUG)
		{
			LOGGER.info("user:" + _loginName);
			LOGGER.info("key:" + key);
		}
		
		// avoid potential exploits
		if (client.getAccountName() == null)
		{
			// Preventing duplicate login in case client login server socket was
			// disconnected or this packet was not sent yet
			if (LoginServerThread.getInstance().addGameServerLogin(_loginName, client))
			{
				client.setAccountName(_loginName);
				LoginServerThread.getInstance().addWaitingClientAndSendRequest(_loginName, client, key);
			}
			else
			{
				client.closeNow();
			}
			
		}
	}
	
	@Override
	public String getType()
	{
		return "[C] 08 AuthLogin";
	}
}