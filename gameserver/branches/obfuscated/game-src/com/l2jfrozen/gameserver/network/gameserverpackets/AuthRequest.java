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

import java.util.List;

import a.a.x;

public class AuthRequest extends x
{
	/**
	 * Format: cccSddb c desired ID c accept alternative ID c reserve Host s ExternalHostName s InetranlHostName d max players d hexid size b hexid
	 * @param id
	 * @param acceptAlternate
	 * @param hexid
	 * @param port
	 * @param reserveHost
	 * @param maxplayer
	 * @param subnets the subnets lists
	 * @param hosts the hosts list
	 */
	public AuthRequest(int id, boolean acceptAlternate, byte[] hexid, int port, boolean reserveHost, int maxplayer, List<String> subnets, List<String> hosts)
	{
		C(0x01);
		C(id);
		C(acceptAlternate ? 0x01 : 0x00);
		C(reserveHost ? 0x01 : 0x00);
		H(port);
		D(maxplayer);
		D(hexid.length);
		B(hexid);
		D(subnets.size());
		for (int i = 0; i < subnets.size(); i++)
		{
			S(subnets.get(i));
			S(hosts.get(i));
		}
	}
	
	@Override
	public byte[] gC()
	{
		return gB();
	}
	
}