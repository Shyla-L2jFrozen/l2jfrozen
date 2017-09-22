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
import com.l2jfrozen.gameserver.network.serverpackets.CharDeleteFail;
import com.l2jfrozen.gameserver.network.serverpackets.CharDeleteOk;
import com.l2jfrozen.gameserver.network.serverpackets.CharSelectInfo;

/**
 * @author eX1steam, l2jfrozen
 */
public final class CharacterDelete extends L2GameClientPacket
{
	private static Logger LOGGER = Logger.getLogger(CharacterDelete.class);
	private int _charSlot;
	
	@Override
	protected void readImpl()
	{
		_charSlot = D();
	}
	
	@Override
	protected void runImpl()
	{
		
		if (!g().getFloodProtectors().getCharacterSelect().tryPerformAction("CharacterDelete"))
			return;
		
		if (CommonConfig.DEBUG)
			LOGGER.debug("DEBUG " + getType() + ": deleting slot:" + _charSlot);
		
		try
		{
			final byte answer = g().markToDeleteChar(_charSlot);
			switch (answer)
			{
				default:
				case -1: // Error
					break;
				case 0: // Success!
					sendPacket(new CharDeleteOk());
					break;
				case 1:
					sendPacket(new CharDeleteFail(CharDeleteFail.REASON_YOU_MAY_NOT_DELETE_CLAN_MEMBER));
					break;
				case 2:
					sendPacket(new CharDeleteFail(CharDeleteFail.REASON_CLAN_LEADERS_MAY_NOT_BE_DELETED));
					break;
			}
		}
		catch (final Exception e)
		{
			if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
				e.printStackTrace();
			
			LOGGER.error("ERROR " + getType() + ":", e);
		}
		
		// Before the char selection, check shutdown status
		if (g().gco().x().isShutdown())
		{
			g().closeNow();
			return;
		}
		
		final CharSelectInfo cl = new CharSelectInfo(g().getAccountName(), g().getSessionId().a, 0);
		sendPacket(cl);
		g().setCharSelection(cl.getCharInfo());
	}
	
	@Override
	public String getType()
	{
		return "[C] 0C CharacterDelete";
	}
}
