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

import org.apache.log4j.Logger;

import com.l2jfrozen.common.CommonConfig;
import com.l2jfrozen.gameserver.model.actor.instance.L2ItemInstance;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;

/**
 * 0x42 WarehouseWithdrawalList dh (h dddhh dhhh d)
 * @version $Revision: 1.3.2.1.2.5 $ $Date: 2005/03/29 23:15:10 $
 */
public class WareHouseWithdrawalList extends L2GameServerPacket
{
	public static final int PRIVATE = 1;
	public static final int CLAN = 2;
	public static final int CASTLE = 3; // not sure
	public static final int FREIGHT = 4; // not sure
	private static Logger LOGGER = Logger.getLogger(WareHouseWithdrawalList.class);
	private static final String _S__54_WAREHOUSEWITHDRAWALLIST = "[S] 42 WareHouseWithdrawalList";
	private L2PcInstance _activeChar;
	private int _playerAdena;
	private L2ItemInstance[] _items;
	private int _whType;
	
	public WareHouseWithdrawalList(final L2PcInstance player, final int type)
	{
		_activeChar = player;
		_whType = type;
		
		_playerAdena = _activeChar.getAdena();
		if (_activeChar.getActiveWarehouse() == null)
		{
			// Something went wrong!
			LOGGER.warn("error while sending withdraw request to: " + _activeChar.getName());
			return;
		}
		_items = _activeChar.getActiveWarehouse().getItems();
		
		if (CommonConfig.DEBUG)
		{
			for (final L2ItemInstance item : _items)
			{
				LOGGER.debug("item:" + item.getItem().getName() + " type1:" + item.getItem().getType1() + " type2:" + item.getItem().getType2());
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0x42);
		/*
		 * 0x01-Private Warehouse 0x02-Clan Warehouse 0x03-Castle Warehouse 0x04-Warehouse
		 */
		H(_whType);
		D(_playerAdena);
		H(_items.length);
		
		for (final L2ItemInstance item : _items)
		{
			H(item.getItem().getType1()); // item type1 //unconfirmed, works
			D(0x00); // unconfirmed, works
			D(item.getItemId()); // unconfirmed, works
			D(item.getCount()); // unconfirmed, works
			H(item.getItem().getType2()); // item type2 //unconfirmed, works
			H(0x00); // ?
			D(item.getItem().getBodyPart()); // ?
			H(item.getEnchantLevel()); // enchant level -confirmed
			H(0x00); // ?
			H(0x00); // ?
			D(item.getObjectId()); // item id - confimed
			if (item.isAugmented())
			{
				D(0x0000FFFF & item.getAugmentation().getAugmentationId());
				D(item.getAugmentation().getAugmentationId() >> 16);
			}
			else
			{
				Q(0x00);
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
		return _S__54_WAREHOUSEWITHDRAWALLIST;
	}
}
