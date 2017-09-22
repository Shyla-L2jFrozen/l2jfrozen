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

/**
 * Format: (ch) dd
 * @author -Wooden-
 */
public class PledgeSkillListAdd extends L2GameServerPacket
{
	private static final String _S__FE_3A_PLEDGESKILLLISTADD = "[S] FE:3A PledgeSkillListAdd";
	private final int _id;
	private final int _lvl;
	
	public PledgeSkillListAdd(final int id, final int lvl)
	{
		_id = id;
		_lvl = lvl;
	}
	
	@Override
	protected void writeImpl()
	{
		C(0xfe);
		H(0x3a);
		
		D(_id);
		D(_lvl);
	}
	
	@Override
	public String getType()
	{
		return _S__FE_3A_PLEDGESKILLLISTADD;
	}
}
