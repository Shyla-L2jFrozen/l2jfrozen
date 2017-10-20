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

import com.l2jfrozen.common.util.monitoring.data.MonitoredStatus;
import com.l2jfrozen.gameserver.config.Config;
import com.l2jfrozen.gameserver.powerpak.PowerPakConfig;

/**
 * Server status
 * @author Nefer
 * @version 1.0
 */
public class ServerConfigStatus extends MonitoredStatus
{
	private static final long serialVersionUID = -4059798755377928431L;
	
	private String ServerName;
	private String GameServerIps;
	private int GameServerPort;
	private float GameServerRate;
	private float GameServerAdenaRate;
	private String GameServerLink;
	private String GameServerHopzoneLink;
	private String GameServerTopzoneLink;
	private String GameServerL2NetworkLink;
	
	public ServerConfigStatus()
	{
		refreshStatus();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.util.monitoring.data.MonitoredStatus#getStatus()
	 */
	@Override
	public String getStaticStatus()
	{
		final StringWriter sw = new StringWriter();
		
		sw.append("	ServerName: " + ServerName + " \n");
		sw.append("	GameServerIPs: " + GameServerIps + " \n");
		sw.append("	GameServerPort: " + GameServerPort + " \n");
		sw.append("	GameServerRate: " + GameServerRate + " \n");
		sw.append("	GameServerAdenaRate: " + GameServerAdenaRate + " \n");
		sw.append("	GameServerLink: " + GameServerLink + " \n");
		sw.append("	GameServerHopzoneLink: " + GameServerHopzoneLink + " \n");
		sw.append("	GameServerTopzoneLink: " + GameServerTopzoneLink + " \n");
		sw.append("	GameServerL2NetworkLink: " + GameServerL2NetworkLink + " \n");
		
		return sw.toString();
	}
	
	@Override
	public String getDynamicStatus()
	{
		return "";
	}
	
	public String getServerName()
	{
		return ServerName;
	}
	
	public String getGameServerIPs()
	{
		return GameServerIps;
	}
	
	public int getGameServerPort()
	{
		return GameServerPort;
	}
	
	public float getGameServerRate()
	{
		return GameServerRate;
	}
	
	public float getGameServerAdenaRate()
	{
		return GameServerAdenaRate;
	}
	
	public String getGameServerLink()
	{
		return GameServerLink;
	}
	
	public String getGameServerHopzoneLink()
	{
		return GameServerHopzoneLink;
	}
	
	public String getGameServerTopzoneLink()
	{
		return GameServerTopzoneLink;
	}
	
	public String getGameServerL2NetworkLink()
	{
		return GameServerL2NetworkLink;
	}
	
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ServerConfigStatus [ServerName=" + ServerName + ", GameServerIPs=" + GameServerIps + ", GameServerPort=" + GameServerPort + ", GameServerRate=" + GameServerRate + ", GameServerAdenaRate=" + GameServerAdenaRate + ", GameServerLink=" + GameServerLink + ", GameServerHopzoneLink=" + GameServerHopzoneLink + ", GameServerTopzoneLink=" + GameServerTopzoneLink + ", GameServerL2NetworkLink=" + GameServerL2NetworkLink + "]";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.util.monitoring.data.MonitoredStatus#refreshStatus()
	 */
	@Override
	public void refreshStatus()
	{
		ServerName = Config.ALT_Server_Name;
		GameServerIps = "";
		for(String host:Config.GAME_SERVER_HOSTS){
			if(GameServerIps.isEmpty())
				GameServerIps = host;
			else
				GameServerIps=GameServerIps+" "+host;
		}
		
		GameServerPort = Config.PORT_GAME;
		GameServerRate = Config.RATE_XP;
		GameServerAdenaRate = Config.RATE_DROP_ADENA;
		GameServerLink = PowerPakConfig.SERVER_WEB_SITE;
		GameServerHopzoneLink = PowerPakConfig.VOTES_SITE_HOPZONE_URL;
		GameServerTopzoneLink = PowerPakConfig.VOTES_SITE_TOPZONE_URL;
		GameServerL2NetworkLink = PowerPakConfig.VOTES_SITE_L2NETWORK_URL;
	}
}