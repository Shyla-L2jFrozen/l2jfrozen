/*
 * L2jFrozen Project - www.l2jfrozen.com 
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jfrozen.gameserver.network.serverpackets;

import com.l2jfrozen.gameserver.datatables.sql.CharTemplateTable;
import com.l2jfrozen.gameserver.model.actor.instance.L2NpcInstance;

public class CustomNpcInfo extends L2GameServerPacket
{
	
	private static final String _S__03_CUSTOMNPCINFO = "[S] 03 CustomNpcInfo [dddddsddd dddddddddddd dddddddd hhhh d hhhhhhhhhhhh d hhhh hhhhhhhhhhhhhhhh dddddd dddddddd ffff ddd s ddddd ccccccc h c d c h ddd cc d ccc ddddddddddd]";
	private final L2NpcInstance _activeChar;
	
	/**
	 * @param cha
	 */
	public CustomNpcInfo(final L2NpcInstance cha)
	{
		_activeChar = cha;
		_activeChar.setClientX(_activeChar.getPosition().getX());
		_activeChar.setClientY(_activeChar.getPosition().getY());
		_activeChar.setClientZ(_activeChar.getPosition().getZ());
		
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0x03);
		D(_activeChar.getX());
		D(_activeChar.getY());
		D(_activeChar.getZ());
		D(_activeChar.getHeading());
		D(_activeChar.getObjectId());
		S(_activeChar.getCustomNpcInstance().getName());
		D(_activeChar.getCustomNpcInstance().getRace());
		D(_activeChar.getCustomNpcInstance().isFemaleSex() ? 1 : 0);
		D(_activeChar.getCustomNpcInstance().getClassId());
		D(_activeChar.getCustomNpcInstance().PAPERDOLL_HAIR());
		D(0);
		D(_activeChar.getCustomNpcInstance().PAPERDOLL_RHAND());
		D(_activeChar.getCustomNpcInstance().PAPERDOLL_LHAND());
		D(_activeChar.getCustomNpcInstance().PAPERDOLL_GLOVES());
		D(_activeChar.getCustomNpcInstance().PAPERDOLL_CHEST());
		D(_activeChar.getCustomNpcInstance().PAPERDOLL_LEGS());
		D(_activeChar.getCustomNpcInstance().PAPERDOLL_FEET());
		D(_activeChar.getCustomNpcInstance().PAPERDOLL_HAIR());
		D(_activeChar.getCustomNpcInstance().PAPERDOLL_RHAND());
		D(_activeChar.getCustomNpcInstance().PAPERDOLL_HAIR());
		D(_activeChar.getCustomNpcInstance().PAPERDOLL_HAIR2());
		write('H', 0, 24);
		D(_activeChar.getCustomNpcInstance().getPvpFlag() ? 1 : 0);
		D(_activeChar.getCustomNpcInstance().getKarma());
		D(_activeChar.getMAtkSpd());
		D(_activeChar.getPAtkSpd());
		D(_activeChar.getCustomNpcInstance().getPvpFlag() ? 1 : 0);
		D(_activeChar.getCustomNpcInstance().getKarma());
		D(_activeChar.getRunSpeed());
		D(_activeChar.getRunSpeed() / 2);
		D(_activeChar.getRunSpeed() / 3);
		D(_activeChar.getRunSpeed() / 3);
		D(_activeChar.getRunSpeed());
		D(_activeChar.getRunSpeed());
		D(_activeChar.getRunSpeed());
		D(_activeChar.getRunSpeed());
		F(_activeChar.getStat().getMovementSpeedMultiplier());
		F(_activeChar.getStat().getAttackSpeedMultiplier());
		F(CharTemplateTable.getInstance().getTemplate(_activeChar.getCustomNpcInstance().getClassId()).getCollisionRadius());
		F(CharTemplateTable.getInstance().getTemplate(_activeChar.getCustomNpcInstance().getClassId()).getCollisionHeight());
		D(_activeChar.getCustomNpcInstance().getHairStyle());
		D(_activeChar.getCustomNpcInstance().getHairColor());
		D(_activeChar.getCustomNpcInstance().getFace());
		S(_activeChar.getCustomNpcInstance().getTitle());
		D(_activeChar.getCustomNpcInstance().getClanId());
		D(_activeChar.getCustomNpcInstance().getClanCrestId());
		D(_activeChar.getCustomNpcInstance().getAllyId());
		D(_activeChar.getCustomNpcInstance().getAllyCrestId());
		D(0);
		C(1);
		C(_activeChar.isRunning() ? 1 : 0);
		C(_activeChar.isInCombat() ? 1 : 0);
		C(_activeChar.isAlikeDead() ? 1 : 0);
		write('C', 0, 3);
		H(0);
		C(0x00);
		D(_activeChar.getAbnormalEffect());
		C(0);
		H(0);
		D(_activeChar.getCustomNpcInstance().getClassId());
		D(_activeChar.getMaxCp());
		D((int) _activeChar.getStatus().getCurrentCp());
		C(_activeChar.getCustomNpcInstance().getEnchantWeapon());
		C(0x00);
		D(0);// clan crest
		C(_activeChar.getCustomNpcInstance().isNoble() ? 1 : 0);
		C(_activeChar.getCustomNpcInstance().isHero() ? 1 : 0);
		C(0);
		write('D', 0, 3);
		D(_activeChar.getCustomNpcInstance().nameColor());
		D(0);
		D(_activeChar.getCustomNpcInstance().getPledgeClass());
		D(0);
		D(_activeChar.getCustomNpcInstance().titleColor());
		D(0x00);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__03_CUSTOMNPCINFO;
	}
	
	private final void write(final char type, final int value, final int times)
	{
		for (int i = 0; i < times; i++)
		{
			switch (type)
			{
				case 'C':
					C(value);
					break;
				case 'D':
					D(value);
					break;
				case 'F':
					F(value);
					break;
				case 'H':
					H(value);
					break;
			}
		}
	}
	
}
