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

/**
 * This class ...
 * @version $Revision: 1.2.2.1.2.3 $ $Date: 2005/03/27 15:29:39 $
 */
public class PledgeShowInfoUpdate extends L2GameServerPacket
{
	private static final String _S__A1_PLEDGESHOWINFOUPDATE = "[S] 88 PledgeShowInfoUpdate";
	private final L2Clan _clan;
	
	public PledgeShowInfoUpdate(final L2Clan clan)
	{
		_clan = clan;
	}
	
	@Override
	protected final void writeImpl()
	{
		final int TOP = ClanTable.getInstance().getTopRate(_clan.getClanId());
		// ddddddddddSdd
		C(0x88);
		// sending empty data so client will ask all the info in response ;)
		D(_clan.getClanId());
		D(_clan.getCrestId());
		D(_clan.getLevel()); // clan level
		D(_clan.getHasFort() != 0 ? _clan.getHasFort() : _clan.getHasCastle());
		D(_clan.getHasHideout());
		D(TOP);
		D(_clan.getReputationScore()); // clan reputation score
		D(0);
		D(0);
		D(_clan.getAllyId());
		S(_clan.getAllyName());
		D(_clan.getAllyCrestId());
		D(_clan.isAtWar());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__A1_PLEDGESHOWINFOUPDATE;
	}
	
}
