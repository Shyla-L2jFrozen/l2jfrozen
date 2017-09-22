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

//import java.util.Calendar; //signed time related
//import org.apache.log4j.Logger;

import com.l2jfrozen.gameserver.datatables.sql.ClanTable;
import com.l2jfrozen.gameserver.model.L2Clan;
import com.l2jfrozen.gameserver.model.L2SiegeClan;
import com.l2jfrozen.gameserver.model.entity.siege.Fort;

/**
 * Populates the Siege Defender List in the SiegeInfo Window<BR>
 * <BR>
 * packet type id 0xcb<BR>
 * format: cddddddd + dSSdddSSd<BR>
 * <BR>
 * c = 0xcb<BR>
 * d = FortID<BR>
 * d = unknow (0x00)<BR>
 * d = unknow (0x01)<BR>
 * d = unknow (0x00)<BR>
 * d = Number of Defending Clans?<BR>
 * d = Number of Defending Clans<BR>
 * { //repeats<BR>
 * d = ClanID<BR>
 * S = ClanName<BR>
 * S = ClanLeaderName<BR>
 * d = ClanCrestID<BR>
 * d = signed time (seconds)<BR>
 * d = Type -> Owner = 0x01 || Waiting = 0x02 || Accepted = 0x03<BR>
 * d = AllyID<BR>
 * S = AllyName<BR>
 * S = AllyLeaderName<BR>
 * d = AllyCrestID<BR>
 * @author programmos
 */
public final class FortSiegeDefenderList extends L2GameServerPacket
{
	private static final String _S__CA_SiegeDefenderList = "[S] cb SiegeDefenderList";
	// private static Logger LOGGER = Logger.getLogger(SiegeDefenderList.class);
	private final Fort _fort;
	
	public FortSiegeDefenderList(final Fort fort)
	{
		_fort = fort;
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0xcb);
		D(_fort.getFortId());
		D(0x00); // 0
		D(0x01); // 1
		D(0x00); // 0
		final int size = _fort.getSiege().getDefenderClans().size() + _fort.getSiege().getDefenderWaitingClans().size();
		if (size > 0)
		{
			L2Clan clan;
			
			D(size);
			D(size);
			// Listing the Lord and the approved clans
			for (final L2SiegeClan siegeclan : _fort.getSiege().getDefenderClans())
			{
				clan = ClanTable.getInstance().getClan(siegeclan.getClanId());
				if (clan == null)
				{
					continue;
				}
				
				D(clan.getClanId());
				S(clan.getName());
				S(clan.getLeaderName());
				D(clan.getCrestId());
				D(0x00); // signed time (seconds) (not storated by L2J)
				switch (siegeclan.getType())
				{
					case OWNER:
						D(0x01); // owner
						break;
					case DEFENDER_PENDING:
						D(0x02); // approved
						break;
					case DEFENDER:
						D(0x03); // waiting approved
						break;
					default:
						D(0x00);
						break;
				}
				D(clan.getAllyId());
				S(clan.getAllyName());
				S(""); // AllyLeaderName
				D(clan.getAllyCrestId());
			}
			for (final L2SiegeClan siegeclan : _fort.getSiege().getDefenderWaitingClans())
			{
				clan = ClanTable.getInstance().getClan(siegeclan.getClanId());
				D(clan.getClanId());
				S(clan.getName());
				S(clan.getLeaderName());
				D(clan.getCrestId());
				D(0x00); // signed time (seconds) (not storated by L2J)
				D(0x02); // waiting approval
				D(clan.getAllyId());
				S(clan.getAllyName());
				S(""); // AllyLeaderName
				D(clan.getAllyCrestId());
			}
		}
		else
		{
			D(0x00);
			D(0x00);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__CA_SiegeDefenderList;
	}
	
}
