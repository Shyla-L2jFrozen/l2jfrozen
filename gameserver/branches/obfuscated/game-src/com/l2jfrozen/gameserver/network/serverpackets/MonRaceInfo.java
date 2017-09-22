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

import com.l2jfrozen.gameserver.model.actor.instance.L2NpcInstance;

/**
 * sample 06 8f19904b 2522d04b 00000000 80 950c0000 4af50000 08f2ffff 0000 - 0 damage (missed 0x80) 06 85071048 bc0e504b 32000000 10 fc41ffff fd240200 a6f5ffff 0100 bc0e504b 33000000 10 3.... format dddc dddh (ddc)
 * @version $Revision: 1.1.6.2 $ $Date: 2005/03/27 15:29:39 $
 */
public class MonRaceInfo extends L2GameServerPacket
{
	private static final String _S__DD_MonRaceInfo = "[S] dd MonRaceInfo";
	private final int _unknown1;
	private final int _unknown2;
	private final L2NpcInstance[] _monsters;
	private final int[][] _speeds;
	
	public MonRaceInfo(final int unknown1, final int unknown2, final L2NpcInstance[] monsters, final int[][] speeds)
	{
		/*
		 * -1 0 to initial the race 0 15322 to start race 13765 -1 in middle of race -1 0 to end the race
		 */
		_unknown1 = unknown1;
		_unknown2 = unknown2;
		_monsters = monsters;
		_speeds = speeds;
	}
	
	// 0xf3;;EtcStatusUpdatePacket;ddddd
	
	@Override
	protected final void writeImpl()
	{
		C(0xdd);
		
		D(_unknown1);
		D(_unknown2);
		D(8);
		
		for (int i = 0; i < 8; i++)
		{
			// LOGGER.info("MOnster "+(i+1)+" npcid "+_monsters[i].getNpcTemplate().getNpcId());
			D(_monsters[i].getObjectId()); // npcObjectID
			D(_monsters[i].getTemplate().npcId + 1000000); // npcID
			D(14107); // origin X
			D(181875 + 58 * (7 - i)); // origin Y
			D(-3566); // origin Z
			D(12080); // end X
			D(181875 + 58 * (7 - i)); // end Y
			D(-3566); // end Z
			F(_monsters[i].getTemplate().collisionHeight); // coll. height
			F(_monsters[i].getTemplate().collisionRadius); // coll. radius
			D(120); // ?? unknown
			// *
			for (int j = 0; j < 20; j++)
			{
				if (_unknown1 == 0)
				{
					C(_speeds[i][j]);
				}
				else
				{
					C(0);
				}
			} // */
			/*
			 * D(0x77776666); D(0x99998888); D(0xBBBBAAAA); D(0xDDDDCCCC); D(0xFFFFEEEE); //
			 */
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
		return _S__DD_MonRaceInfo;
	}
}
