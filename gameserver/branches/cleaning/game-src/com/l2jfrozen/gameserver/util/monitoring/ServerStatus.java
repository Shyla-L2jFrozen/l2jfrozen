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
package com.l2jfrozen.gameserver.util.monitoring;

import java.io.StringWriter;

import com.l2jfrozen.gameserver.model.L2World;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;
import com.l2jfrozen.netcore.MMOClientsManager;
import com.l2jfrozen.util.monitoring.data.MonitoredStatus;

/**
 * Server status
 * @author Nefer
 * @version 1.0
 */
public class ServerStatus extends MonitoredStatus
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4059798755377928431L;
	
	private int TotalOnline;
	private int ActivePlayers;
	private int OfflinePlayers;
	private int FakePlayers;
	private int ActiveClients;
	
	public ServerStatus()
	{
		
		refreshStatus();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.util.monitoring.data.MonitoredStatus#getStatus()
	 */
	@Override
	public String getStatus()
	{
		final StringWriter sw = new StringWriter();
		
		sw.append("Players Online: " + TotalOnline);
		sw.append("Active: " + ActivePlayers + ", Offline shop: " + OfflinePlayers + ", Fake player: " + FakePlayers);
		sw.append("Alive clients: " + ActiveClients);
		
		return sw.toString();
	}
	
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
	/**
	 * @return the totalOnline
	 */
	public int getTotalOnline()
	{
		return TotalOnline;
	}
	
	/**
	 * @return the activePlayers
	 */
	public int getActivePlayers()
	{
		return ActivePlayers;
	}
	
	/**
	 * @return the offlinePlayers
	 */
	public int getOfflinePlayers()
	{
		return OfflinePlayers;
	}
	
	/**
	 * @return the fakePlayers
	 */
	public int getFakePlayers()
	{
		return FakePlayers;
	}
	
	/**
	 * @return the activeClients
	 */
	public int getActiveClients()
	{
		return ActiveClients;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getStatus();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.util.monitoring.data.MonitoredStatus#refreshStatus()
	 */
	@Override
	public void refreshStatus()
	{
		ActivePlayers = 0;
		OfflinePlayers = 0;
		FakePlayers = 0;
		TotalOnline = 0;
		
		for (final L2PcInstance player : L2World.getInstance().getAllPlayers())
		{
			if (player.isInOfflineMode())
				OfflinePlayers++;
			else if (player.isFakeOfflinePlayer())
				FakePlayers++;
			else
				ActivePlayers++;
		}
		
		TotalOnline = ActivePlayers + OfflinePlayers + FakePlayers;
		ActiveClients = MMOClientsManager.getInstance().printClientsNumber();
	}
	
}