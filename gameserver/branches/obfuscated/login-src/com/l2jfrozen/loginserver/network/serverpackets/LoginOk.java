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

import a.a.aa;

/**
 * <pre>
 * Format: dddddddd
 * f: the session key
 * d: ?
 * d: ?
 * d: ?
 * d: ?
 * d: ?
 * d: ?
 * b: 16 bytes - unknown
 * </pre>
 */
public final class LoginOk extends L2LoginServerPacket
{
	private final int _loginOk1, _loginOk2;
	
	public LoginOk(final aa sessionKey)
	{
		_loginOk1 = sessionKey.c;
		_loginOk2 = sessionKey.d;
	}
	
	@Override
	public void write()
	{
		C(0x03);
		D(_loginOk1);
		D(_loginOk2);
		D(0x00);
		D(0x00);
		D(0x000003ea);
		D(0x00);
		D(0x00);
		D(0x00);
		B(new byte[16]);
	}
}
