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

/**
 * @author KenM
 */
public final class AccountKicked extends L2LoginServerPacket
{
	private final AccountKickedReason _reason;
	
	/**
	 * @param reason
	 */
	public AccountKicked(final AccountKickedReason reason)
	{
		_reason = reason;
	}
	
	@Override
	public void write()
	{
		writeC(0x02);
		writeD(_reason.getCode());
	}
}
