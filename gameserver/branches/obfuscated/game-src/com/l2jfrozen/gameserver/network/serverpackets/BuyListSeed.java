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

import java.util.List;

import com.l2jfrozen.gameserver.model.L2TradeList;
import com.l2jfrozen.gameserver.model.actor.instance.L2ItemInstance;

import javolution.util.FastList;

/**
 * Format: c ddh[hdddhhd] c - id (0xE8) d - money d - manor id h - size [ h - item type 1 d - object id d - item id d - count h - item type 2 h d - price ]
 * @author l3x
 */

public final class BuyListSeed extends L2GameServerPacket
{
	private static final String _S__E8_BUYLISTSEED = "[S] E8 BuyListSeed";
	
	private final int _manorId;
	private List<L2ItemInstance> _list = new FastList<>();
	private final int _money;
	
	public BuyListSeed(final L2TradeList list, final int manorId, final int currentMoney)
	{
		_money = currentMoney;
		_manorId = manorId;
		_list = list.getItems();
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0xE8);
		
		D(_money); // current money
		D(_manorId); // manor id
		
		H(_list.size()); // list length
		
		for (final L2ItemInstance item : _list)
		{
			H(0x04); // item->type1
			D(0x00); // objectId
			D(item.getItemId()); // item id
			D(item.getCount()); // item count
			H(0x04); // item->type2
			H(0x00); // unknown :)
			D(item.getPriceToSell()); // price
		}
	}
	
	@Override
	public String getType()
	{
		return _S__E8_BUYLISTSEED;
	}
}
