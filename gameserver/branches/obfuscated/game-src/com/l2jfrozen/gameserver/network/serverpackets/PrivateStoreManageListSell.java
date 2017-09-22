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

import com.l2jfrozen.gameserver.config.Config;
import com.l2jfrozen.gameserver.model.TradeList;
import com.l2jfrozen.gameserver.model.TradeList.TradeItem;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;

/**
 * 3 section to this packet 1)playerinfo which is always sent dd 2)list of items which can be added to sell d(hhddddhhhd) 3)list of items which have already been setup for sell in previous sell private store sell manageent d(hhddddhhhdd) *
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:39 $
 */

/*
 * In memory of our friend Vadim 03/11/2014
 */
public class PrivateStoreManageListSell extends L2GameServerPacket
{
	private static final String _S__B3_PRIVATESELLLISTSELL = "[S] 9a PrivateSellListSell";
	private final int _objId;
	private int _playerAdena;
	private final boolean _packageSale;
	private final TradeList.TradeItem[] _itemList;
	private final TradeList.TradeItem[] _sellList;
	
	public PrivateStoreManageListSell(final L2PcInstance player)
	{
		_objId = player.getObjectId();
		
		if (Config.SELL_BY_ITEM)
		{
			_playerAdena = player.getItemCount(Config.SELL_ITEM, -1);
		}
		else
		{
			_playerAdena = player.getAdena();
		}
		
		player.getSellList().updateItems();
		_packageSale = player.getSellList().isPackaged();
		_itemList = player.getInventory().getAvailableItems(player.getSellList());
		_sellList = player.getSellList().getItems();
	}
	
	/**
	 * During store set no packets will be received from client just when store definition is finished.
	 */
	@Override
	protected final void writeImpl()
	{
		C(0x9a);
		D(_objId);
		D(_packageSale ? 1 : 0); // Package sell
		D(_playerAdena);
		
		D(_itemList.length /*- _sellList.length*/); // for potential sells
		for (final TradeList.TradeItem item : _itemList)
		{
			if (isItemInSelling(item) == false)
			{
				D(item.getItem().getType2());
				D(item.getObjectId());
				D(item.getItem().getItemId());
				D(item.getCount());
				H(0x00);
				H(item.getEnchant());// enchant lvl
				H(0x00);
				D(item.getItem().getBodyPart());
				D(item.getPrice()); // store price
			}
		}
		D(_sellList.length); // count for any items already added for sell
		for (final TradeList.TradeItem item : _sellList)
		{
			D(item.getItem().getType2());
			D(item.getObjectId());
			D(item.getItem().getItemId());
			D(item.getCount());
			H(0x00);
			H(item.getEnchant());// enchant lvl
			H(0x00);
			D(item.getItem().getBodyPart());
			D(item.getPrice());// your price
			D(item.getItem().getReferencePrice()); // store price
		}
	}
	
	private boolean isItemInSelling(final TradeItem item_)
	{
		for (final TradeList.TradeItem itemSell : _sellList)
		{
			if (itemSell.getObjectId() == item_.getObjectId())
			{
				return true;
			}
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__B3_PRIVATESELLLISTSELL;
	}
}