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

import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jfrozen.common.CommonConfig;
import com.l2jfrozen.loginserver.GameServerThread;
import com.l2jfrozen.loginserver.network.GameServerState;
import a.a.u;
import a.a.w;

/**
 * @author -Wooden-
 */
public class BlowFishKey extends w
{
	protected static final Logger _log = Logger.getLogger(BlowFishKey.class.getName());
	
	/**
	 * @param decrypt
	 * @param server
	 */
	public BlowFishKey(final byte[] decrypt, final GameServerThread server)
	{
		super(decrypt);
		final int size = D();
		final byte[] tempKey = B(size);
		try
		{
			byte[] tempDecryptKey;
			final Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.DECRYPT_MODE, server.getPrivateKey());
			tempDecryptKey = rsaCipher.doFinal(tempKey);
			// there are nulls before the key we must remove them
			int i = 0;
			final int len = tempDecryptKey.length;
			for (; i < len; i++)
			{
				if (tempDecryptKey[i] != 0)
				{
					break;
				}
			}
			final byte[] key = new byte[len - i];
			System.arraycopy(tempDecryptKey, i, key, 0, len - i);
			
			server.SetBlowFish(new u(key));
			if (CommonConfig.DEBUG)
			{
				_log.info("New BlowFish key received, Blowfih Engine initialized:");
			}
			server.setLoginConnectionState(GameServerState.BF_CONNECTED);
		}
		catch (final GeneralSecurityException e)
		{
			_log.log(Level.SEVERE, "Error While decrypting blowfish key (RSA): " + e.getMessage(), e);
		}
	}
}
