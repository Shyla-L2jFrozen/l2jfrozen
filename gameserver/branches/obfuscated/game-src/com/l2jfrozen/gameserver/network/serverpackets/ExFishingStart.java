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

import com.l2jfrozen.gameserver.model.L2Character;

/**
 * Format (ch)ddddd
 * @author -Wooden-
 */
public class ExFishingStart extends L2GameServerPacket
{
	private static final String _S__FE_13_EXFISHINGSTART = "[S] FE:13 ExFishingStart";
	private final L2Character _activeChar;
	private final int _x, _y, _z, _fishType;
	
	public ExFishingStart(final L2Character character, final int fishType, final int x, final int y, final int z, final boolean isNightLure)
	{
		_activeChar = character;
		_fishType = fishType;
		_x = x;
		_y = y;
		_z = z;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#writeImpl()
	 */
	@Override
	protected void writeImpl()
	{
		C(0xfe);
		H(0x13);
		D(_activeChar.getObjectId());
		D(_fishType); // fish type
		D(_x); // x poisson
		D(_y); // y poisson
		D(_z); // z poisson
		C(0x00); // night lure
		C(0x00); // ??
		C(_fishType >= 7 && _fishType <= 9 ? 0x01 : 0x00); // 0 = day lure 1 = night lure
		C(0x00);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__FE_13_EXFISHINGSTART;
	}
	
}
