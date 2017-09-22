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

import com.l2jfrozen.gameserver.model.actor.instance.L2ItemInstance;

/**
 * 16 d6 6d c0 4b player id who dropped it ee cc 11 43 object id 39 00 00 00 item id 8f 14 00 00 x b7 f1 00 00 y 60 f2 ff ff z 01 00 00 00 show item-count 1=yes 7a 00 00 00 count . format dddddddd rev 377 ddddddddd rev 417
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:39 $
 */
public class DropItem extends L2GameServerPacket
{
	private static final String _S__16_DROPITEM = "[S] 0c DropItem";
	private final L2ItemInstance _item;
	private final int _charObjId;
	
	/**
	 * Constructor of the DropItem server packet
	 * @param item : L2ItemInstance designating the item
	 * @param playerObjId : int designating the player ID who dropped the item
	 */
	public DropItem(final L2ItemInstance item, final int playerObjId)
	{
		_item = item;
		_charObjId = playerObjId;
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0x0c);
		D(_charObjId);
		D(_item.getObjectId());
		D(_item.getItemId());
		
		D(_item.getX());
		D(_item.getY());
		D(_item.getZ());
		// only show item count if it is a stackable item
		if (_item.isStackable())
		{
			D(0x01);
		}
		else
		{
			D(0x00);
		}
		D(_item.getCount());
		
		D(1); // unknown
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__16_DROPITEM;
	}
	
}
