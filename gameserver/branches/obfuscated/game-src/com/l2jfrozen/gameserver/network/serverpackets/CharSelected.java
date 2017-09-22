/*
 * L2jFrozen Project - www.l2jfrozen.com 
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jfrozen.gameserver.network.serverpackets;

import com.l2jfrozen.gameserver.controllers.GameTimeController;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 * @version $Revision: 1.4.2.5.2.6 $ $Date: 2005/03/27 15:29:39 $
 */
public class CharSelected extends L2GameServerPacket
{
	// SdSddddddddddffddddddddddddddddddddddddddddddddddddddddd d
	private static final String _S__21_CHARSELECTED = "[S] 15 CharSelected";
	private final L2PcInstance _activeChar;
	private final int _sessionId;
	
	/**
	 * @param cha
	 * @param sessionId
	 */
	public CharSelected(final L2PcInstance cha, final int sessionId)
	{
		_activeChar = cha;
		_sessionId = sessionId;
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0x15);
		
		S(_activeChar.getName());
		D(_activeChar.getCharId()); // ??
		S(_activeChar.getTitle());
		D(_sessionId);
		D(_activeChar.getClanId());
		D(0x00); // ??
		D(_activeChar.getAppearance().getSex() ? 1 : 0);
		D(_activeChar.getRace().ordinal());
		D(_activeChar.getClassId().getId());
		D(0x01); // active ??
		D(_activeChar.getX());
		D(_activeChar.getY());
		D(_activeChar.getZ());
		
		F(_activeChar.getCurrentHp());
		F(_activeChar.getCurrentMp());
		D(_activeChar.getSp());
		Q(_activeChar.getExp());
		D(_activeChar.getLevel());
		D(_activeChar.getKarma()); // thx evill33t
		D(0x0); // ?
		D(_activeChar.getINT());
		D(_activeChar.getSTR());
		D(_activeChar.getCON());
		D(_activeChar.getMEN());
		D(_activeChar.getDEX());
		D(_activeChar.getWIT());
		for (int i = 0; i < 30; i++)
		{
			D(0x00);
		}
		// D(0); //c3
		// D(0); //c3
		// D(0); //c3
		
		D(0x00); // c3 work
		D(0x00); // c3 work
		
		// extra info
		D(GameTimeController.getInstance().getGameTime()); // in-game time
		
		D(0x00); //
		
		D(0x00); // c3
		
		D(0x00); // c3 InspectorBin
		D(0x00); // c3
		D(0x00); // c3
		D(0x00); // c3
		
		D(0x00); // c3 InspectorBin for 528 client
		D(0x00); // c3
		D(0x00); // c3
		D(0x00); // c3
		D(0x00); // c3
		D(0x00); // c3
		D(0x00); // c3
		D(0x00); // c3
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__21_CHARSELECTED;
	}
}