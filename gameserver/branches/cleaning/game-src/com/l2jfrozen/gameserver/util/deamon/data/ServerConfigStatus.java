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
package com.l2jfrozen.gameserver.util.deamon.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Server Config status
 * @author Shyla
 * @version 1.0
 */

@XmlRootElement
public class ServerConfigStatus implements Serializable
{
	private static final long serialVersionUID = -4059798755377928431L;
	
	@XmlElement
	private String ServerName;
	@XmlElement
	private String GameServerIp;
	@XmlElement
	private int GameServerPort;
	@XmlElement
	private float GameServerRate;
	@XmlElement
	private float GameServerAdenaRate;
	@XmlElement
	private String GameServerLink;
	@XmlElement
	private String GameServerHopzoneLink;
	@XmlElement
	private String GameServerTopzoneLink;
	@XmlElement
	private String GameServerL2NetworkLink;
	
	public ServerConfigStatus()
	{
		refreshStatus();
	}
	
	public String getServerName()
	{
		return ServerName;
	}
	
	public String getGameServerIp()
	{
		return GameServerIp;
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
	
	public void refreshStatus()
	{
		final Properties settings = new Properties();
		InputStream is = null;
		
		try
		{
			is = new FileInputStream(new File("./config/functions/l2jfrozen.properties"));
			settings.load(is);
			is.close();
		}
		catch (final Exception e)
		{
			
		}
		
		try
		{
			is = new FileInputStream(new File("./config/network/gameserver.properties"));
			settings.load(is);
			is.close();
		}
		catch (final Exception e)
		{
			
		}
		
		try
		{
			is = new FileInputStream(new File("./config/head/rates.properties"));
			settings.load(is);
			is.close();
		}
		catch (final Exception e)
		{
			
		}
		
		try
		{
			is = new FileInputStream(new File("./config/powerpak/powerpak.properties"));
			settings.load(is);
			is.close();
		}
		catch (final Exception e)
		{
			
		}
		
		ServerName = settings.getProperty("ServerName", "");
		GameServerIp = settings.getProperty("ExternalHostname", "*");
		GameServerPort = Integer.parseInt(settings.getProperty("GameserverPort", "7777"));
		GameServerRate = Float.parseFloat(settings.getProperty("RateXp", "1.00"));
		GameServerAdenaRate = Float.parseFloat(settings.getProperty("RateDropAdena", "1.00"));
		GameServerLink = settings.getProperty("ServerWebSite", "");
		GameServerHopzoneLink = settings.getProperty("VotesSiteHopZoneUrl", "");
		GameServerTopzoneLink = settings.getProperty("VotesSiteTopZoneUrl", "");
		GameServerL2NetworkLink = settings.getProperty("VotesSiteL2NetworkUrl", "");
		
	}
	
}