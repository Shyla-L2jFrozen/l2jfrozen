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
package com.l2jfrozen.gameserver.network.clientpackets;

import org.apache.log4j.Logger;

import com.l2jfrozen.Config;
import com.l2jfrozen.gameserver.datatables.csv.TeleportWhereType;
import com.l2jfrozen.gameserver.geo.GeoData;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;
import com.l2jfrozen.gameserver.network.serverpackets.CharMoveToLocation;
import com.l2jfrozen.gameserver.network.serverpackets.ValidateLocation;
import com.l2jfrozen.gameserver.network.serverpackets.ValidateLocationInVehicle;

public final class ValidatePosition extends L2GameClientPacket
{
	private static Logger LOGGER = Logger.getLogger(ValidatePosition.class);
	
	private int _x;
	private int _y;
	private int _z;
	private int _heading;
	@SuppressWarnings("unused")
	private int _data;
	
	@Override
	protected void readImpl()
	{
		_x = readD();
		_y = readD();
		_z = readD();
		_heading = readD();
		_data = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null || activeChar.isTeleporting() || activeChar.inObserverMode())
			return;
		
		if (_x == 0 && _y == 0 && activeChar.getX() != 0)
			return;
		
		if (activeChar.getX() == 0 && activeChar.getY() == 0)
		{
			activeChar.teleToLocation(TeleportWhereType.Town);
			clientToServer(activeChar);
		}
		
		final int realX = activeChar.getX();
		final int realY = activeChar.getY();
		final int realZ = activeChar.getZ();
		
		final double dx = _x - realX;
		final double dy = _y - realY;
		final double dz = _z - realZ;
		final double diffSq = dx * dx + dy * dy;
		
		int finalZ = _z;
		if (Math.abs(dz) <= 200)
		{
			finalZ = realZ;
		}
		
		final int geoZ = GeoData.getInstance().getHeight(realX, realY, finalZ);
		
		if (Config.ZONE_DEBUG)
		{
			final int realHeading = activeChar.getHeading();
			LOGGER.info("client pos: " + _x + " " + _y + " " + _z + " head " + _heading);
			LOGGER.info("server pos: " + realX + " " + realY + " " + realZ + " head " + realHeading);
			LOGGER.info("finalZ" + finalZ + " geoZ: " + geoZ + " destZ: " + activeChar.getZdestination());
		}
		
		if (activeChar.isFalling(_z))
		{
			return; // disable validations during fall to avoid "jumping"
		}
		
		// COORD Client<-->Server synchronization
		switch (Config.COORD_SYNCHRONIZE)
		{
			
			case 1:
			{ // full synchronization Client --> Server
				// only * using this option it is difficult
				// for players to bypass obstacles
				
				if (!activeChar.isMoving() || !activeChar.validateMovementHeading(_heading)) // Heading changed on client = possible obstacle
				{
					// character is not moving, take coordinates from client
					if (diffSq < 2500)
					{ // 50*50 - attack won't work fluently if even small differences are corrected
						activeChar.getPosition().setXYZ(realX, realY, finalZ);
						
					}
					else
					{
						activeChar.getPosition().setXYZ(_x, _y, finalZ);
					}
				}
				else
				{
					activeChar.getPosition().setXYZ(realX, realY, finalZ);
					
				}
				
				activeChar.setHeading(_heading);
				
			}
				break;
			case 2:
			{ // full synchronization Server --> Client (bounces for validation)
				
				if (Config.GEODATA > 0 && (diffSq > 250000 || Math.abs(dz) > 200))
				{
					if (Math.abs(dz) > 200)
					{
						
						if (Math.abs(finalZ - activeChar.getClientZ()) < 800)
						{
							activeChar.getPosition().setXYZ(realX, realY, finalZ);
						}
						
					}
					else
					{
						if (!activeChar.isMoving())
						{
							
							if (activeChar.isInBoat())
								sendPacket(new ValidateLocationInVehicle(activeChar));
							else
								sendPacket(new ValidateLocation(activeChar));
							
						}
						else if (diffSq > activeChar.getStat().getMoveSpeed())
							activeChar.broadcastPacket(new CharMoveToLocation(activeChar));
						
						finalZ = activeChar.getPosition().getZ();
					}
					
				}
				
			}
				break;
			case -1:
			{ // just (client-->server) Z coordination
				
				if (Math.abs(dz) > 200)
				{
					
					if (Math.abs(_z - activeChar.getClientZ()) < 800)
						activeChar.getPosition().setXYZ(realX, realY, finalZ);
					
				}
				else
					finalZ = realZ;
				
			}
				break;
			default:
			case 0:
			{ // no synchronization at all
				// the server has the correct information
				finalZ = realZ;
			}
				break;
			
		}
		
		// check water
		if (Config.ALLOW_WATER)
			activeChar.checkWaterState();
		
		// check falling if previous client Z is less then
		if (Config.FALL_DAMAGE)
		{
			activeChar.isFalling(finalZ);
		}
		
		activeChar.setClientX(_x);
		activeChar.setClientY(_y);
		activeChar.setClientZ(_z);
		activeChar.setClientHeading(_heading);
		
	}
	
	private void clientToServer(final L2PcInstance player)
	{
		_x = player.getX();
		_y = player.getY();
		_z = player.getZ();
	}
	
	public boolean equal(final ValidatePosition pos)
	{
		return _x == pos._x && _y == pos._y && _z == pos._z && _heading == pos._heading;
	}
	
	@Override
	public String getType()
	{
		return "[C] 48 ValidatePosition";
	}
}
