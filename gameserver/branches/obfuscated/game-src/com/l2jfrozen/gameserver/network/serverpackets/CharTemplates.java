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

import java.util.List;

import com.l2jfrozen.gameserver.templates.L2PcTemplate;

import javolution.util.FastList;

/**
 * This class ...
 * @version $Revision: 1.3.2.1.2.7 $ $Date: 2005/03/27 15:29:39 $
 */
public class CharTemplates extends L2GameServerPacket
{
	// dddddddddddddddddddd
	private static final String _S__23_CHARTEMPLATES = "[S] 23 CharTemplates";
	private final List<L2PcTemplate> _chars = new FastList<>();
	
	public void addChar(final L2PcTemplate template)
	{
		_chars.add(template);
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0x17);
		D(_chars.size());
		
		for (final L2PcTemplate temp : _chars)
		{
			D(temp.race.ordinal());
			D(temp.classId.getId());
			D(0x46);
			D(temp.baseSTR);
			D(0x0a);
			D(0x46);
			D(temp.baseDEX);
			D(0x0a);
			D(0x46);
			D(temp.baseCON);
			D(0x0a);
			D(0x46);
			D(temp.baseINT);
			D(0x0a);
			D(0x46);
			D(temp.baseWIT);
			D(0x0a);
			D(0x46);
			D(temp.baseMEN);
			D(0x0a);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__23_CHARTEMPLATES;
	}
}
