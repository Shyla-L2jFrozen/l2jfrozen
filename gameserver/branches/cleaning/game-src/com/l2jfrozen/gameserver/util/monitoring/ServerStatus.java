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
import java.sql.SQLException;

import com.l2jfrozen.gameserver.model.L2World;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;
import com.l2jfrozen.netcore.MMOClientsManager;
import com.l2jfrozen.util.Memory;
import com.l2jfrozen.util.database.L2DatabaseFactory_c3p0;
import com.l2jfrozen.util.monitoring.data.MonitoredStatus;

/**
 * Server status
 * @author Nefer
 * @version 1.0
 */
public class ServerStatus extends MonitoredStatus
{
	private static final long serialVersionUID = -4059798755377928430L;
	
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
	@SuppressWarnings("static-access")
	@Override
	public String getDynamicStatus()
	{
		final StringWriter sw = new StringWriter();
		sw.append("Players Online: " + TotalOnline + ", Alive clients: " + ActiveClients + ". \n");
		sw.append("INFO  Active: " + ActivePlayers + ", Offline shop: " + OfflinePlayers + ", Fake player: " + FakePlayers + ". \n");
		sw.append("INFO  Free Memory: " + Memory.getFreeMemory() + " MB, Used memory: " + Memory.getUsedMemory() + " MB, Threads count: " + Thread.activeCount() + ". \n");
		try
		{
			sw.append("INFO  Connections c3p0 -> Count: " + ((L2DatabaseFactory_c3p0) L2DatabaseFactory_c3p0.getInstance()).getNumConnections() + ", Idle: " + ((L2DatabaseFactory_c3p0) L2DatabaseFactory_c3p0.getInstance()).getIdleConnectionCount() + ", Busy: " + L2DatabaseFactory_c3p0.getInstance().getBusyConnectionCount() + ". \n");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		sw.append("INFO  =============================================================-[ Server status ] \n");
		
		return sw.toString();
	}
	
	@Override
	public String getStaticStatus()
	{
		return "";
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
		return "ServerStatus [TotalOnline=" + TotalOnline + ", ActivePlayers=" + ActivePlayers + ", OfflinePlayers=" + OfflinePlayers + ", FakePlayers=" + FakePlayers + ", ActiveClients=" + ActiveClients + "]";
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
		ActiveClients = MMOClientsManager.getInstance().getActiveClientsNumber();
	}
}