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

import com.l2jfrozen.gameserver.managers.CastleManager;
import com.l2jfrozen.gameserver.managers.CastleManorManager;
import com.l2jfrozen.gameserver.managers.CastleManorManager.CropProcure;
import com.l2jfrozen.gameserver.model.L2Manor;
import com.l2jfrozen.gameserver.model.entity.siege.Castle;

import javolution.util.FastList;

/**
 * format(packet 0xFE) ch dd [ddcdcdddddddcddc] c - id h - sub id d - manor id d - size [ d - crop id d - seed level c d - reward 1 id c d - reward 2 id d - next sale limit d d - min crop price d - max crop price d - today buy d - today price c - today reward d - next buy d - next price c - next
 * reward ]
 * @author l3x
 */
public class ExShowCropSetting extends L2GameServerPacket
{
	private static final String _S__FE_20_EXSHOWCROPSETTING = "[S] FE:20 ExShowCropSetting";
	
	private final int _manorId;
	private final int _count;
	private final int[] _cropData; // data to send, size:_count*14
	
	@Override
	public void runImpl()
	{
	}
	
	public ExShowCropSetting(final int manorId)
	{
		_manorId = manorId;
		final Castle c = CastleManager.getInstance().getCastleById(_manorId);
		final FastList<Integer> crops = L2Manor.getInstance().getCropsForCastle(_manorId);
		_count = crops.size();
		_cropData = new int[_count * 14];
		int i = 0;
		for (final int cr : crops)
		{
			_cropData[i * 14 + 0] = cr;
			_cropData[i * 14 + 1] = L2Manor.getInstance().getSeedLevelByCrop(cr);
			_cropData[i * 14 + 2] = L2Manor.getInstance().getRewardItem(cr, 1);
			_cropData[i * 14 + 3] = L2Manor.getInstance().getRewardItem(cr, 2);
			_cropData[i * 14 + 4] = L2Manor.getInstance().getCropPuchaseLimit(cr);
			_cropData[i * 14 + 5] = 0; // Looks like not used
			_cropData[i * 14 + 6] = L2Manor.getInstance().getCropBasicPrice(cr) * 60 / 100;
			_cropData[i * 14 + 7] = L2Manor.getInstance().getCropBasicPrice(cr) * 10;
			CropProcure cropPr = c.getCrop(cr, CastleManorManager.PERIOD_CURRENT);
			if (cropPr != null)
			{
				_cropData[i * 14 + 8] = cropPr.getStartAmount();
				_cropData[i * 14 + 9] = cropPr.getPrice();
				_cropData[i * 14 + 10] = cropPr.getReward();
			}
			else
			{
				_cropData[i * 14 + 8] = 0;
				_cropData[i * 14 + 9] = 0;
				_cropData[i * 14 + 10] = 0;
			}
			cropPr = c.getCrop(cr, CastleManorManager.PERIOD_NEXT);
			if (cropPr != null)
			{
				_cropData[i * 14 + 11] = cropPr.getStartAmount();
				_cropData[i * 14 + 12] = cropPr.getPrice();
				_cropData[i * 14 + 13] = cropPr.getReward();
			}
			else
			{
				_cropData[i * 14 + 11] = 0;
				_cropData[i * 14 + 12] = 0;
				_cropData[i * 14 + 13] = 0;
			}
			i++;
		}
	}
	
	@Override
	public void writeImpl()
	{
		C(0xFE); // Id
		H(0x20); // SubId
		
		D(_manorId); // manor id
		D(_count); // size
		
		for (int i = 0; i < _count; i++)
		{
			D(_cropData[i * 14 + 0]); // crop id
			D(_cropData[i * 14 + 1]); // seed level
			C(1);
			D(_cropData[i * 14 + 2]); // reward 1 id
			C(1);
			D(_cropData[i * 14 + 3]); // reward 2 id
			
			D(_cropData[i * 14 + 4]); // next sale limit
			D(_cropData[i * 14 + 5]); // ???
			D(_cropData[i * 14 + 6]); // min crop price
			D(_cropData[i * 14 + 7]); // max crop price
			
			D(_cropData[i * 14 + 8]); // today buy
			D(_cropData[i * 14 + 9]); // today price
			C(_cropData[i * 14 + 10]); // today reward
			
			D(_cropData[i * 14 + 11]); // next buy
			D(_cropData[i * 14 + 12]); // next price
			C(_cropData[i * 14 + 13]); // next reward
		}
	}
	
	@Override
	public String getType()
	{
		return _S__FE_20_EXSHOWCROPSETTING;
	}
}
