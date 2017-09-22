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

/**
 * Format: (ch)ddddd
 */
public class ExConfirmVariationRefiner extends L2GameServerPacket
{
	private static final String _S__FE_53_EXCONFIRMVARIATIONREFINER = "[S] FE:53 ExConfirmVariationRefiner";
	
	private final int _refinerItemObjId;
	private final int _lifestoneItemId;
	private final int _gemstoneItemId;
	private final int _gemstoneCount;
	private final int _unk2;
	
	public ExConfirmVariationRefiner(final int refinerItemObjId, final int lifeStoneId, final int gemstoneItemId, final int gemstoneCount)
	{
		_refinerItemObjId = refinerItemObjId;
		_lifestoneItemId = lifeStoneId;
		_gemstoneItemId = gemstoneItemId;
		_gemstoneCount = gemstoneCount;
		_unk2 = 1;
	}
	
	@Override
	protected void writeImpl()
	{
		C(0xfe);
		H(0x53);
		D(_refinerItemObjId);
		D(_lifestoneItemId);
		D(_gemstoneItemId);
		D(_gemstoneCount);
		D(_unk2);
	}
	
	@Override
	public String getType()
	{
		return _S__FE_53_EXCONFIRMVARIATIONREFINER;
	}
}
