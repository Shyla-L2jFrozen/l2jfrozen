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
package com.l2jfrozen.loginserver.network.clientpackets;

import com.l2jfrozen.loginserver.network.serverpackets.LoginFailReason;
import com.l2jfrozen.loginserver.network.serverpackets.ServerList;

/**
 * <pre>
 * Format: ddc
 * d: fist part of session id
 * d: second part of session id
 * c: ?
 * </pre>
 */
public class RequestServerList extends L2LoginClientPacket
{
	private int _skey1;
	private int _skey2;
	private int _data3;
	
	/**
	 * @return
	 */
	public int getSessionKey1()
	{
		return _skey1;
	}
	
	/**
	 * @return
	 */
	public int getSessionKey2()
	{
		return _skey2;
	}
	
	/**
	 * @return
	 */
	public int getData3()
	{
		return _data3;
	}
	
	@Override
	public boolean readImpl()
	{
		if (super._b.remaining() >= 8)
		{
			_skey1 = D(); // loginOk 1
			_skey2 = D(); // loginOk 2
			return true;
		}
		return false;
	}
	
	@Override
	public void run()
	{
		if (g().getSessionKey().f(_skey1, _skey2))
		{
			g().sendPacket(new ServerList(g()));
		}
		else
		{
			g().close(LoginFailReason.REASON_ACCESS_FAILED);
		}
	}
}
