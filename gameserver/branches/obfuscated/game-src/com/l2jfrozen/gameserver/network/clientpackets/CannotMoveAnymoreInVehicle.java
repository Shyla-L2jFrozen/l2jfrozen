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
package com.l2jfrozen.gameserver.network.clientpackets;

import com.l2jfrozen.common.util.Point3D;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;
import com.l2jfrozen.gameserver.network.serverpackets.StopMoveInVehicle;

/**
 * @author Maktakien
 */
public final class CannotMoveAnymoreInVehicle extends L2GameClientPacket
{
	private int _x;
	private int _y;
	private int _z;
	private int _heading;
	private int _boatId;
	
	@Override
	protected void readImpl()
	{
		_boatId = D();
		_x = D();
		_y = D();
		_z = D();
		_heading = D();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance player = g().getActiveChar();
		
		if (player == null)
			return;
		
		if (player.isInBoat())
		{
			if (player.getBoat().getObjectId() == _boatId)
			{
				player.setInBoatPosition(new Point3D(_x, _y, _z));
				player.getPosition().setHeading(_heading);
				player.broadcastPacket(new StopMoveInVehicle(player, _boatId));
			}
		}
	}
	
	@Override
	public String getType()
	{
		return "[C] 5D CannotMoveAnymoreInVehicle";
	}
}