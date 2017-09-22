/* L2jFrozen Project - www.l2jfrozen.com 
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

import com.l2jfrozen.gameserver.managers.CastleManorManager.CropProcure;
import com.l2jfrozen.gameserver.model.L2Manor;
import com.l2jfrozen.gameserver.model.actor.instance.L2ItemInstance;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * format(packet 0xFE) ch dd [ddddcdcdddc] c - id h - sub id d - manor id d - size [ d - Object id d - crop id d - seed level c d - reward 1 id c d - reward 2 id d - manor d - buy residual d - buy price d - reward ]
 * @author l3x
 */

public class ExShowSellCropList extends L2GameServerPacket
{
	private static final String _S__FE_21_EXSHOWSELLCROPLIST = "[S] FE:21 ExShowSellCropList";
	
	private int _manorId = 1;
	private final FastMap<Integer, L2ItemInstance> _cropsItems;
	private final FastMap<Integer, CropProcure> _castleCrops;
	
	public ExShowSellCropList(final L2PcInstance player, final int manorId, final FastList<CropProcure> crops)
	{
		_manorId = manorId;
		_castleCrops = new FastMap<>();
		_cropsItems = new FastMap<>();
		
		final FastList<Integer> allCrops = L2Manor.getInstance().getAllCrops();
		for (final int cropId : allCrops)
		{
			final L2ItemInstance item = player.getInventory().getItemByItemId(cropId);
			if (item != null)
			{
				_cropsItems.put(cropId, item);
			}
		}
		
		for (final CropProcure crop : crops)
		{
			if (_cropsItems.containsKey(crop.getId()) && crop.getAmount() > 0)
			{
				_castleCrops.put(crop.getId(), crop);
			}
		}
	}
	
	@Override
	public void runImpl()
	{
		// no long running
	}
	
	@Override
	public void writeImpl()
	{
		C(0xFE);
		H(0x21);
		
		D(_manorId); // manor id
		D(_cropsItems.size()); // size
		
		for (final L2ItemInstance item : _cropsItems.values())
		{
			D(item.getObjectId()); // Object id
			D(item.getItemId()); // crop id
			D(L2Manor.getInstance().getSeedLevelByCrop(item.getItemId())); // seed level
			C(1);
			D(L2Manor.getInstance().getRewardItem(item.getItemId(), 1)); // reward 1 id
			C(1);
			D(L2Manor.getInstance().getRewardItem(item.getItemId(), 2)); // reward 2 id
			
			if (_castleCrops.containsKey(item.getItemId()))
			{
				final CropProcure crop = _castleCrops.get(item.getItemId());
				D(_manorId); // manor
				D(crop.getAmount()); // buy residual
				D(crop.getPrice()); // buy price
				C(crop.getReward()); // reward
			}
			else
			{
				D(0xFFFFFFFF); // manor
				D(0); // buy residual
				D(0); // buy price
				C(0); // reward
			}
			D(item.getCount()); // my crops
		}
	}
	
	@Override
	public String getType()
	{
		return _S__FE_21_EXSHOWSELLCROPLIST;
	}
}
