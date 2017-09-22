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

import javolution.util.FastList;

public class ExEnchantSkillInfo extends L2GameServerPacket
{
	private static final String _S__FE_18_EXENCHANTSKILLINFO = "[S] FE:18 ExEnchantSkillInfo";
	private final FastList<Req> _reqs;
	private final int _id;
	private final int _level;
	private final int _spCost;
	private final int _xpCost;
	private final int _rate;
	
	class Req
	{
		public int id;
		public int count;
		public int type;
		public int unk;
		
		Req(final int pType, final int pId, final int pCount, final int pUnk)
		{
			id = pId;
			type = pType;
			count = pCount;
			unk = pUnk;
		}
	}
	
	public ExEnchantSkillInfo(final int id, final int level, final int spCost, final int xpCost, final int rate)
	{
		_reqs = new FastList<>();
		_id = id;
		_level = level;
		_spCost = spCost;
		_xpCost = xpCost;
		_rate = rate;
	}
	
	public void addRequirement(final int type, final int id, final int count, final int unk)
	{
		_reqs.add(new Req(type, id, count, unk));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#writeImpl()
	 */
	@Override
	protected void writeImpl()
	{
		C(0xfe);
		H(0x18);
		
		D(_id);
		D(_level);
		D(_spCost);
		Q(_xpCost);
		D(_rate);
		
		D(_reqs.size());
		
		for (final Req temp : _reqs)
		{
			D(temp.type);
			D(temp.id);
			D(temp.count);
			D(temp.unk);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__FE_18_EXENCHANTSKILLINFO;
	}
	
}
