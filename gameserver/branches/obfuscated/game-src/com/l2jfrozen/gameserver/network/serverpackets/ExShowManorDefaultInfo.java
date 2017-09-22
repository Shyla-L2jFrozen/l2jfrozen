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

import com.l2jfrozen.gameserver.model.L2Manor;

import javolution.util.FastList;

/**
 * format(packet 0xFE) ch cd [ddddcdcd] c - id h - sub id c d - size [ d - level d - seed price d - seed level d - crop price c d - reward 1 id c d - reward 2 id ]
 * @author l3x
 */
public class ExShowManorDefaultInfo extends L2GameServerPacket
{
	private static final String _S__FE_1C_EXSHOWSEEDINFO = "[S] FE:1E ExShowManorDefaultInfo";
	
	private FastList<Integer> _crops = null;
	
	public ExShowManorDefaultInfo()
	{
		_crops = L2Manor.getInstance().getAllCrops();
	}
	
	@Override
	protected void writeImpl()
	{
		C(0xFE);
		H(0x1E);
		C(0);
		D(_crops.size());
		for (final int cropId : _crops)
		{
			D(cropId); // crop Id
			D(L2Manor.getInstance().getSeedLevelByCrop(cropId)); // level
			D(L2Manor.getInstance().getSeedBasicPriceByCrop(cropId)); // seed price
			D(L2Manor.getInstance().getCropBasicPrice(cropId)); // crop price
			C(1); // rewrad 1 Type
			D(L2Manor.getInstance().getRewardItem(cropId, 1)); // Rewrad 1 Type Item Id
			C(1); // rewrad 2 Type
			D(L2Manor.getInstance().getRewardItem(cropId, 2)); // Rewrad 2 Type Item Id
		}
	}
	
	@Override
	public String getType()
	{
		return _S__FE_1C_EXSHOWSEEDINFO;
	}
}
