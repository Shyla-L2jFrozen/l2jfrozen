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

import com.l2jfrozen.gameserver.model.TradeList;

/**
 * This class ...
 * @author Yme
 * @version $Revision: 1.2.2.1.2.3 $ $Date: 2005/03/27 15:29:39 $
 */
public class TradeOwnAdd extends L2GameServerPacket
{
	private static final String _S__30_TRADEOWNADD = "[S] 20 TradeOwnAdd";
	private final TradeList.TradeItem _item;
	
	public TradeOwnAdd(final TradeList.TradeItem item)
	{
		_item = item;
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0x20);
		
		H(1); // item count
		
		H(_item.getItem().getType1()); // item type1
		D(_item.getObjectId());
		D(_item.getItem().getItemId());
		D(_item.getCount());
		H(_item.getItem().getType2()); // item type2
		H(0x00); // ?
		
		D(_item.getItem().getBodyPart()); // rev 415 slot 0006-lr.ear 0008-neck 0030-lr.finger 0040-head 0080-?? 0100-l.hand 0200-gloves 0400-chest 0800-pants 1000-feet 2000-?? 4000-r.hand 8000-r.hand
		H(_item.getEnchant()); // enchant level
		H(0x00); // ?
		H(0x00);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__30_TRADEOWNADD;
	}
	
}
