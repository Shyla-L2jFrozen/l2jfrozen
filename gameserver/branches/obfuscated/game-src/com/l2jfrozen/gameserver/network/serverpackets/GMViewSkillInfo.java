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

import com.l2jfrozen.gameserver.model.L2Skill;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;

public class GMViewSkillInfo extends L2GameServerPacket
{
	private static final String _S__91_GMViewSkillInfo = "[S] 91 GMViewSkillInfo";
	private final L2PcInstance _activeChar;
	private L2Skill[] _skills;
	
	public GMViewSkillInfo(final L2PcInstance cha)
	{
		_activeChar = cha;
		_skills = _activeChar.getAllSkills();
		if (_skills.length == 0)
		{
			_skills = new L2Skill[0];
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0x91);
		S(_activeChar.getName());
		D(_skills.length);
		
		for (final L2Skill skill : _skills)
		{
			D(skill.isPassive() ? 1 : 0);
			D(skill.getLevel());
			D(skill.getId());
			C(0x00); // c5
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__91_GMViewSkillInfo;
	}
}
