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
 * sample 0000: 5a d8 a8 10 48 d8 a8 10 48 10 04 00 00 01 00 00 Z...H...H....... 0010: 00 f0 1a 00 00 68 28 00 00 .....h(.. format dddddd dddh (h)
 * @version $Revision: 1.4.2.1.2.4 $ $Date: 2005/03/27 15:29:39 $
 */
public class MagicSkillUser extends L2GameServerPacket
{
	private static final String _S__5A_MAGICSKILLUSER = "[S] 5A MagicSkillUser";
	private int _targetId;
	private final int _skillId;
	private final int _skillLevel;
	private final int _hitTime;
	private final int _reuseDelay;
	private final int _charObjId, _x, _y, _z;
	
	public MagicSkillUser(final L2Character cha, final L2Character target, final int skillId, final int skillLevel, final int hitTime, final int reuseDelay)
	{
		_charObjId = cha.getObjectId();
		if (target != null)
		{
			_targetId = target.getObjectId();
		}
		else
		{
			_targetId = cha.getTargetId();
		}
		_skillId = skillId;
		_skillLevel = skillLevel;
		_hitTime = hitTime;
		_reuseDelay = reuseDelay;
		_x = cha.getX();
		_y = cha.getY();
		_z = cha.getZ();
	}
	
	public MagicSkillUser(final L2Character cha, final int skillId, final int skillLevel, final int hitTime, final int reuseDelay)
	{
		_charObjId = cha.getObjectId();
		_targetId = cha.getTargetId();
		_skillId = skillId;
		_skillLevel = skillLevel;
		_hitTime = hitTime;
		_reuseDelay = reuseDelay;
		_x = cha.getX();
		_y = cha.getY();
		_z = cha.getZ();
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0x48);
		D(_charObjId);
		D(_targetId);
		D(_skillId);
		D(_skillLevel);
		D(_hitTime);
		D(_reuseDelay);
		D(_x);
		D(_y);
		D(_z);
		H(0x00); // unknown loop but not AoE
		// for()
		// {
		H(0x00);
		H(0x00);
		H(0x00);
		// }
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__5A_MAGICSKILLUSER;
	}
}