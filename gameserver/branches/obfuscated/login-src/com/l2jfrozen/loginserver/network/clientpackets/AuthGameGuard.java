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

import com.l2jfrozen.loginserver.network.LoginClientState;
import com.l2jfrozen.loginserver.network.serverpackets.GGAuth;
import com.l2jfrozen.loginserver.network.serverpackets.LoginFailReason;

/**
 * Format: ddddd
 * @author -Wooden-
 */
public class AuthGameGuard extends L2LoginClientPacket
{
	private int _sessionId;
	private int _data1;
	private int _data2;
	private int _data3;
	private int _data4;
	
	public int getSessionId()
	{
		return _sessionId;
	}
	
	public int getData1()
	{
		return _data1;
	}
	
	public int getData2()
	{
		return _data2;
	}
	
	public int getData3()
	{
		return _data3;
	}
	
	public int getData4()
	{
		return _data4;
	}
	
	@Override
	protected boolean readImpl()
	{
		if (super._b.remaining() >= 20)
		{
			_sessionId = D();
			_data1 = D();
			_data2 = D();
			_data3 = D();
			_data4 = D();
			return true;
		}
		return false;
	}
	
	@Override
	public void run()
	{
		if (_sessionId == g().getSessionId())
		{
			g().setState(LoginClientState.AUTHED_GG);
			g().sendPacket(new GGAuth(g().getSessionId()));
		}
		else
		{
			g().close(LoginFailReason.REASON_ACCESS_FAILED);
		}
	}
}
