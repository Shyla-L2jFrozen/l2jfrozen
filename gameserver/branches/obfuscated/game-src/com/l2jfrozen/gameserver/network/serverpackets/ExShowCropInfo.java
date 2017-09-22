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

import javolution.util.FastList;

/**
 * Format: ch cddd[ddddcdcdcd] c - id (0xFE) h - sub id (0x1D) c d - manor id d d - size [ d - crop id d - residual buy d - start buy d - buy price c - reward type d - seed level c - reward 1 items d - reward 1 item id c - reward 2 items d - reward 2 item id ]
 * @author l3x
 */

public class ExShowCropInfo extends L2GameServerPacket
{
	private static final String _S__FE_1C_EXSHOWSEEDINFO = "[S] FE:1D ExShowCropInfo";
	private FastList<CropProcure> _crops;
	private final int _manorId;
	
	public ExShowCropInfo(final int manorId, final FastList<CropProcure> crops)
	{
		_manorId = manorId;
		_crops = crops;
		if (_crops == null)
		{
			_crops = new FastList<>();
		}
	}
	
	@Override
	protected void writeImpl()
	{
		C(0xFE); // Id
		H(0x1D); // SubId
		C(0);
		D(_manorId); // Manor ID
		D(0);
		D(_crops.size());
		for (final CropProcure crop : _crops)
		{
			D(crop.getId()); // Crop id
			D(crop.getAmount()); // Buy residual
			D(crop.getStartAmount()); // Buy
			D(crop.getPrice()); // Buy price
			C(crop.getReward()); // Reward
			D(L2Manor.getInstance().getSeedLevelByCrop(crop.getId())); // Seed Level
			C(1); // rewrad 1 Type
			D(L2Manor.getInstance().getRewardItem(crop.getId(), 1)); // Rewrad 1 Type Item Id
			C(1); // rewrad 2 Type
			D(L2Manor.getInstance().getRewardItem(crop.getId(), 2)); // Rewrad 2 Type Item Id
		}
	}
	
	@Override
	public String getType()
	{
		return _S__FE_1C_EXSHOWSEEDINFO;
	}
	
}
