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

import com.l2jfrozen.gameserver.model.L2ShortCut;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;

/**
 * ShortCutInit format d *(1dddd)/(2ddddd)/(3dddd)
 * @version $Revision: 1.3.2.1.2.4 $ $Date: 2005/03/27 15:29:39 $
 */
public class ShortCutInit extends L2GameServerPacket
{
	private static final String _S__57_SHORTCUTINIT = "[S] 45 ShortCutInit";
	
	private L2ShortCut[] _shortCuts;
	private L2PcInstance _activeChar;
	
	public ShortCutInit(final L2PcInstance activeChar)
	{
		_activeChar = activeChar;
		
		if (_activeChar == null)
			return;
		
		_shortCuts = _activeChar.getAllShortCuts();
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0x45);
		D(_shortCuts.length);
		
		for (final L2ShortCut sc : _shortCuts)
		{
			D(sc.getType());
			D(sc.getSlot() + sc.getPage() * 12);
			
			switch (sc.getType())
			{
				case L2ShortCut.TYPE_ITEM: // 1
					D(sc.getId());
					D(0x01);
					D(-1);
					D(0x00);
					D(0x00);
					H(0x00);
					H(0x00);
					break;
				case L2ShortCut.TYPE_SKILL: // 2
					D(sc.getId());
					D(sc.getLevel());
					C(0x00); // C5
					D(0x01); // C6
					break;
				case L2ShortCut.TYPE_ACTION: // 3
					D(sc.getId());
					D(0x01); // C6
					break;
				case L2ShortCut.TYPE_MACRO: // 4
					D(sc.getId());
					D(0x01); // C6
					break;
				case L2ShortCut.TYPE_RECIPE: // 5
					D(sc.getId());
					D(0x01); // C6
					break;
				default:
					D(sc.getId());
					D(0x01); // C6
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__57_SHORTCUTINIT;
	}
}
