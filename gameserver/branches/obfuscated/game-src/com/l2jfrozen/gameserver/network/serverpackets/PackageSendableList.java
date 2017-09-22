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
 * @author -Wooden-
 */
public class PackageSendableList extends L2GameServerPacket
{
	private static final String _S__C3_PACKAGESENDABLELIST = "[S] C3 PackageSendableList";
	private final L2ItemInstance[] _items;
	private final int _playerObjId;
	
	public PackageSendableList(final L2ItemInstance[] items, final int playerObjId)
	{
		_items = items;
		_playerObjId = playerObjId;
	}
	
	@Override
	protected void writeImpl()
	{
		C(0xC3);
		
		D(_playerObjId);
		D(g().getActiveChar().getAdena());
		D(_items.length);
		for (final L2ItemInstance item : _items) // format inside the for taken from SellList part use should be about the same
		{
			H(item.getItem().getType1());
			D(item.getObjectId());
			D(item.getItemId());
			D(item.getCount());
			H(item.getItem().getType2());
			H(0x00);
			D(item.getItem().getBodyPart());
			H(item.getEnchantLevel());
			H(0x00);
			H(0x00);
			D(item.getObjectId()); // some item identifier later used by client to answer (see RequestPackageSend) not item id nor object id maybe some freight system id??
		}
		
	}
	
	@Override
	public String getType()
	{
		return _S__C3_PACKAGESENDABLELIST;
	}
}
