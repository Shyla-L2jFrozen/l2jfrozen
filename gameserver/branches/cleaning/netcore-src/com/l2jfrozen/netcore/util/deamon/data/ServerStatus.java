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
package com.l2jfrozen.netcore.util.deamon.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.l2jfrozen.netcore.MMOClientsManager;

/**
 * Server status
 * @author Shyla
 * @version 1.0
 */

@XmlRootElement
public class ServerStatus implements Serializable
{
	private static final long serialVersionUID = -4059798755377928430L;
	
//	@XmlElement
//	private int TotalOnline;
//	@XmlElement
//	private int ActivePlayers;
//	@XmlElement
//	private int OfflinePlayers;
//	@XmlElement
//	private int FakePlayers;
	@XmlElement
	private int ActiveClients;
	
	public ServerStatus()
	{
		refreshStatus();
	}
	
//	/**
//	 * @return the totalOnline
//	 */
//	public int getTotalOnline()
//	{
//		return TotalOnline;
//	}
//	
//	/**
//	 * @return the activePlayers
//	 */
//	public int getActivePlayers()
//	{
//		return ActivePlayers;
//	}
//	
//	/**
//	 * @return the offlinePlayers
//	 */
//	public int getOfflinePlayers()
//	{
//		return OfflinePlayers;
//	}
//	
//	/**
//	 * @return the fakePlayers
//	 */
//	public int getFakePlayers()
//	{
//		return FakePlayers;
//	}
	
	/**
	 * @return the activeClients
	 */
	public int getActiveClients()
	{
		return ActiveClients;
	}
	
	public void refreshStatus()
	{
		
//		final com.l2jfrozen.gameserver.util.monitoring.ServerStatus currentStatus = new com.l2jfrozen.gameserver.util.monitoring.ServerStatus();
//		
//		ActivePlayers = currentStatus.getActivePlayers();
//		OfflinePlayers = currentStatus.getOfflinePlayers();
//		FakePlayers = currentStatus.getFakePlayers();
//		TotalOnline = currentStatus.getTotalOnline();
		ActiveClients = MMOClientsManager.getInstance().getActiveClientsNumber();
		
	}
}