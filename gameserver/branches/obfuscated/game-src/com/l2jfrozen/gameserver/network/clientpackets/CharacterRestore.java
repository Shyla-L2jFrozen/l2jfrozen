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

import com.l2jfrozen.common.CommonConfig;
import com.l2jfrozen.gameserver.network.serverpackets.CharSelectInfo;

public final class CharacterRestore extends L2GameClientPacket
{
	private int _charSlot;
	
	@Override
	protected void readImpl()
	{
		_charSlot = D();
	}
	
	@Override
	protected void runImpl()
	{
		if (!g().getFloodProtectors().getCharacterSelect().tryPerformAction("CharacterRestore"))
			return;
		
		try
		{
			g().markRestoredChar(_charSlot);
		}
		catch (final Exception e)
		{
			if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
				e.printStackTrace();
		}
		
		// Before the char selection, check shutdown status
		if (g().getConnection().getSelectorThread().isShutdown())
		{
			g().closeNow();
			return;
		}
		
		final CharSelectInfo cl = new CharSelectInfo(g().getAccountName(), g().getSessionId().playOkID1, 0);
		sendPacket(cl);
		g().setCharSelection(cl.getCharInfo());
	}
	
	@Override
	public String getType()
	{
		return "[C] 62 CharacterRestore";
	}
}