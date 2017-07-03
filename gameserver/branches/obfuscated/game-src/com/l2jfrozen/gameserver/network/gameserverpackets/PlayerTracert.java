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
package com.l2jfrozen.gameserver.network.gameserverpackets;

import a.a.L;


/**
 * @author mrTJO
 */
public class PlayerTracert extends L
{
	public PlayerTracert(final String account, final String pcIp, final String hop1, final String hop2, final String hop3, final String hop4)
	{
		writeC(0x07);
		writeS(account);
		writeS(pcIp);
		writeS(hop1);
		writeS(hop2);
		writeS(hop3);
		writeS(hop4);
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}