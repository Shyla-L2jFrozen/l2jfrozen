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
import com.l2jfrozen.gameserver.model.L2Clan.SubPledge;
import com.l2jfrozen.gameserver.model.L2ClanMember;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;

//import org.apache.log4j.Logger;
/**
 * sample 0000: 68 b1010000 48 00 61 00 6d 00 62 00 75 00 72 00 67 00 00 00 H.a.m.b.u.r.g... 43 00 61 00 6c 00 61 00 64 00 6f 00 6e 00 00 00 C.a.l.a.d.o.n... 00000000 crestid | not used (nuocnam) 00000000 00000000 00000000 00000000 22000000 00000000 00000000 00000000 ally id 00 00 ally name 00000000
 * ally crrest id 02000000 6c 00 69 00 74 00 68 00 69 00 75 00 6d 00 31 00 00 00 l.i.t.h.i.u.m... 0d000000 level 12000000 class id 00000000 01000000 offline 1=true 00000000 45 00 6c 00 61 00 6e 00 61 00 00 00 E.l.a.n.a... 08000000 19000000 01000000 01000000 00000000 format dSS dddddddddSdd d
 * (Sddddd) dddSS dddddddddSdd d (Sdddddd)
 * @version $Revision: 1.6.2.2.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class PledgeShowMemberListAll extends L2GameServerPacket
{
	private static final String _S__68_PLEDGESHOWMEMBERLISTALL = "[S] 53 PledgeShowMemberListAll";
	private final L2Clan _clan;
	private final L2PcInstance _activeChar;
	private final L2ClanMember[] _members;
	private int _pledgeType;
	
	// private static Logger LOGGER = Logger.getLogger(PledgeShowMemberListAll.class);
	
	public PledgeShowMemberListAll(final L2Clan clan, final L2PcInstance activeChar)
	{
		_clan = clan;
		_activeChar = activeChar;
		_members = _clan.getMembers();
	}
	
	@Override
	protected final void writeImpl()
	{
		
		_pledgeType = 0;
		writePledge(0);
		
		final SubPledge[] subPledge = _clan.getAllSubPledges();
		for (final SubPledge element : subPledge)
		{
			_activeChar.sendPacket(new PledgeReceiveSubPledgeCreated(element));
		}
		
		for (final L2ClanMember m : _members)
		{
			if (m.getPledgeType() == 0)
			{
				continue;
			}
			_activeChar.sendPacket(new PledgeShowMemberListAdd(m));
		}
		
		// unless this is sent sometimes, the client doesn't recognise the player as the leader
		_activeChar.sendPacket(new UserInfo(_activeChar));
		
	}
	
	void writePledge(final int mainOrSubpledge)
	{
		final int TOP = ClanTable.getInstance().getTopRate(_clan.getClanId());
		
		C(0x53);
		
		D(mainOrSubpledge); // c5 main clan 0 or any subpledge 1?
		D(_clan.getClanId());
		D(_pledgeType); // c5 - possibly pledge type?
		S(_clan.getName());
		S(_clan.getLeaderName());
		
		D(_clan.getCrestId()); // crest id .. is used again
		D(_clan.getLevel());
		D(_clan.getHasCastle());
		D(_clan.getHasHideout());
		D(TOP);
		D(_clan.getReputationScore()); // was activechar lvl
		D(0); // 0
		D(0); // 0
		
		D(_clan.getAllyId());
		S(_clan.getAllyName());
		D(_clan.getAllyCrestId());
		D(_clan.isAtWar());
		D(_clan.getSubPledgeMembersCount(_pledgeType));
		
		int yellow;
		for (final L2ClanMember m : _members)
		{
			if (m.getPledgeType() != _pledgeType)
			{
				continue;
			}
			if (m.getPledgeType() == -1)
				yellow = m.getSponsor() != 0 ? 1 : 0;
			else if (m.getPlayerInstance() != null)
				yellow = m.getPlayerInstance().isClanLeader() ? 1 : 0;
			else
				yellow = 0;
			S(m.getName());
			D(m.getLevel());
			D(m.getClassId());
			D(0);
			D(m.getObjectId());
			D(m.isOnline() ? 1 : 0);
			D(yellow);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__68_PLEDGESHOWMEMBERLISTALL;
	}
	
}
