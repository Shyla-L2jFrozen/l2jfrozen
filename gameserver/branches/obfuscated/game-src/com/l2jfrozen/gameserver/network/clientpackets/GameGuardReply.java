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

import com.l2jfrozen.gameserver.config.Config;

/**
 * @author zabbix Lets drink to code! Unknown Packet: ca 0000: 45 00 01 00 1e 37 a2 f5 00 00 00 00 00 00 00 00 E....7..........
 */
public class GameGuardReply extends L2GameClientPacket
{
	private final int[] _reply = new int[4];
	private static final Logger LOGGER = Logger.getLogger(GameGuardReply.class);
	
	@Override
	protected void readImpl()
	{
		_reply[0] = D();
		_reply[1] = D();
		_reply[2] = D();
		_reply[3] = D();
	}
	
	@Override
	protected void runImpl()
	{
		
		// L2jFrozen cannot be reached with GameGuard: L2Net notification --> Close Client connection
		if (Config.GAMEGUARD_L2NET_CHECK)
		{
			g().closeNow();
			LOGGER.warn("Player with account name " + g().accountName + " kicked to use L2Net ");
			return;
		}
		
		g().setGameGuardOk(true);
	}
	
	@Override
	public String getType()
	{
		return "[C] CA GameGuardReply";
	}
}