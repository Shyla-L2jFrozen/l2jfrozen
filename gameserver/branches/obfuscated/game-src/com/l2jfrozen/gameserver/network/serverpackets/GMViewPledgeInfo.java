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

import com.l2jfrozen.gameserver.datatables.sql.ClanTable;
import com.l2jfrozen.gameserver.model.L2Clan;
import com.l2jfrozen.gameserver.model.L2ClanMember;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;

/**
 * format SdSS dddddddd d (Sddddd)
 * @version $Revision: 1.1.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class GMViewPledgeInfo extends L2GameServerPacket
{
	private static final String _S__A9_GMVIEWPLEDGEINFO = "[S] 90 GMViewPledgeInfo";
	private final L2Clan _clan;
	private final L2PcInstance _activeChar;
	
	public GMViewPledgeInfo(final L2Clan clan, final L2PcInstance activeChar)
	{
		_clan = clan;
		_activeChar = activeChar;
	}
	
	@Override
	protected final void writeImpl()
	{
		final int TOP = ClanTable.getInstance().getTopRate(_clan.getClanId());
		C(0x90);
		S(_activeChar.getName());
		D(_clan.getClanId());
		D(0x00);
		S(_clan.getName());
		S(_clan.getLeaderName());
		D(_clan.getCrestId()); // -> no, it's no longer used (nuocnam) fix by game
		D(_clan.getLevel());
		D(_clan.getHasCastle());
		D(_clan.getHasHideout());
		D(TOP);
		D(_clan.getReputationScore());
		D(0);
		D(0);
		
		D(_clan.getAllyId()); // c2
		S(_clan.getAllyName()); // c2
		D(_clan.getAllyCrestId()); // c2
		D(_clan.isAtWar()); // c3
		
		final L2ClanMember[] members = _clan.getMembers();
		D(members.length);
		
		for (final L2ClanMember member : members)
		{
			S(member.getName());
			D(member.getLevel());
			D(member.getClassId());
			D(0);
			D(1);
			D(member.isOnline() ? member.getObjectId() : 0);
			D(0);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__A9_GMVIEWPLEDGEINFO;
	}
	
}
