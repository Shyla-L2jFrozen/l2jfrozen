/*
 * $Header$
 *
 *
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

import com.l2jfrozen.gameserver.model.actor.instance.L2HennaInstance;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;

public class HennaItemInfo extends L2GameServerPacket
{
	private static final String _S__E3_HennaItemInfo = "[S] E3 HennaItemInfo";
	
	private final L2PcInstance _activeChar;
	private final L2HennaInstance _henna;
	
	public HennaItemInfo(final L2HennaInstance henna, final L2PcInstance player)
	{
		_henna = henna;
		_activeChar = player;
	}
	
	@Override
	protected final void writeImpl()
	{
		
		C(0xe3);
		D(_henna.getSymbolId()); // symbol Id
		D(_henna.getItemIdDye()); // item id of dye
		D(_henna.getAmountDyeRequire()); // total amount of dye require
		D(_henna.getPrice()); // total amount of aden require to draw symbol
		D(1); // able to draw or not 0 is false and 1 is true
		D(_activeChar.getAdena());
		
		D(_activeChar.getINT()); // current INT
		C(_activeChar.getINT() + _henna.getStatINT()); // equip INT
		D(_activeChar.getSTR()); // current STR
		C(_activeChar.getSTR() + _henna.getStatSTR()); // equip STR
		D(_activeChar.getCON()); // current CON
		C(_activeChar.getCON() + _henna.getStatCON()); // equip CON
		D(_activeChar.getMEN()); // current MEM
		C(_activeChar.getMEN() + _henna.getStatMEM()); // equip MEM
		D(_activeChar.getDEX()); // current DEX
		C(_activeChar.getDEX() + _henna.getStatDEX()); // equip DEX
		D(_activeChar.getWIT()); // current WIT
		C(_activeChar.getWIT() + _henna.getStatWIT()); // equip WIT
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__E3_HennaItemInfo;
	}
}
