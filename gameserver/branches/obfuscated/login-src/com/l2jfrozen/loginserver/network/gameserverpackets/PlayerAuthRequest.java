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
package com.l2jfrozen.loginserver.network.gameserverpackets;

import java.util.logging.Logger;

import com.l2jfrozen.common.CommonConfig;
import com.l2jfrozen.loginserver.GameServerThread;
import com.l2jfrozen.loginserver.LoginController;
import com.l2jfrozen.loginserver.network.loginserverpackets.PlayerAuthResponse;
import com.l2jfrozen.netcore.SessionKey;
import com.l2jfrozen.netcore.util.network.BaseRecievePacket;

/**
 * @author -Wooden-
 */
public class PlayerAuthRequest extends BaseRecievePacket
{
	private static Logger _log = Logger.getLogger(PlayerAuthRequest.class.getName());
	
	/**
	 * @param decrypt
	 * @param server
	 */
	public PlayerAuthRequest(final byte[] decrypt, final GameServerThread server)
	{
		super(decrypt);
		final String account = readS();
		final int playKey1 = readD();
		final int playKey2 = readD();
		final int loginKey1 = readD();
		final int loginKey2 = readD();
		final SessionKey sessionKey = new SessionKey(loginKey1, loginKey2, playKey1, playKey2);
		
		PlayerAuthResponse authResponse;
		if (CommonConfig.DEBUG)
		{
			_log.info("auth request received for Player " + account);
		}
		final SessionKey key = LoginController.getInstance().getKeyForAccount(account);
		if ((key != null) && key.equals(sessionKey))
		{
			if (CommonConfig.DEBUG)
			{
				_log.info("auth request: OK");
			}
			LoginController.getInstance().removeAuthedLoginClient(account);
			authResponse = new PlayerAuthResponse(account, true);
		}
		else
		{
			if (CommonConfig.DEBUG)
			{
				_log.info("auth request: NO");
				_log.info("session key from self: " + key);
				_log.info("session key sent: " + sessionKey);
			}
			authResponse = new PlayerAuthResponse(account, false);
		}
		server.sendPacket(authResponse);
	}
}
