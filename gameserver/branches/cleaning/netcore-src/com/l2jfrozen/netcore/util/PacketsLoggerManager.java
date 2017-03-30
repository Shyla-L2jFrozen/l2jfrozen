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
package com.l2jfrozen.netcore.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import com.l2jfrozen.logs.Log;
import com.l2jfrozen.netcore.MMOClient;
import com.l2jfrozen.netcore.ReceivablePacket;
import com.thoughtworks.xstream.XStream;

/**
 * @author Shyla
 * @param <T> 
 */
public class PacketsLoggerManager<T extends MMOClient<?>>
{
	private static Logger LOGGER = Logger.getLogger(PacketsLoggerManager.class);
	
	private final List<String> _monitored_characters = new ArrayList<>();
	private final Hashtable<String, List<String>> _character_blocked_packets = new Hashtable<>();
	private final XStream xstream;
	
	protected PacketsLoggerManager()
	{
		_character_blocked_packets.clear();
		_monitored_characters.clear();
		
		xstream = new XStream();
		
	}
	
	public void startCharacterPacketsMonitoring(final String character)
	{
		
		if (!_monitored_characters.contains(character))
			_monitored_characters.add(character);
		
	}
	
	public void stopCharacterPacketsMonitoring(final String character)
	{
		
		if (_monitored_characters.contains(character))
			_monitored_characters.remove(character);
		
	}
	
	public void blockCharacterPacket(final String character, final String packet)
	{
		
		List<String> blocked_packets = _character_blocked_packets.get(character);
		if (blocked_packets == null)
		{
			blocked_packets = new ArrayList<>();
		}
		
		if (!blocked_packets.contains(packet))
		{
			blocked_packets.add(packet);
		}
		_character_blocked_packets.put(character, blocked_packets);
		
	}
	
	public void restoreCharacterPacket(final String character, final String packet)
	{
		
		final List<String> blocked_packets = _character_blocked_packets.get(character);
		if (blocked_packets != null)
		{
			
			if (blocked_packets.contains(packet))
			{
				blocked_packets.remove(packet);
			}
			
			_character_blocked_packets.put(character, blocked_packets);
			
		}
		
	}
	
	public boolean isCharacterMonitored(final String character)
	{
		return _monitored_characters.contains(character);
	}
	
	public boolean isCharacterPacketBlocked(final String character, final String packet)
	{
		
		final List<String> blocked_packets = _character_blocked_packets.get(character);
		if (blocked_packets != null)
		{
			
			if (blocked_packets.contains(packet))
			{
				return true;
			}
			
		}
		
		return false;
		
	}
	
	public void logCharacterPacket(final String character, final String packet)
	{
		
		Log.add("[Character: " + character + "] has sent [Packet: " + packet + "]", character + "_packets");
		
	}
	
	public void logReceivedPacket(ReceivablePacket<?> cp)
	{
		
		String packet_content = xstream.toXML(cp);
		
		
		Log.add(packet_content, "netcore/"+cp.getClient().getClass().getName(), cp.getClass().getSimpleName());
		
	}
	
	public static PacketsLoggerManager<MMOClient<?>> getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		public static final PacketsLoggerManager<MMOClient<?>> _instance = new PacketsLoggerManager<>();
	}
	
	public static final void add(final String text, final String cat, final String ref)
	{
		String date = new SimpleDateFormat("yy.MM.dd-H_mm_ss").format(new Date());
		
		new File("log/"+cat).mkdirs();
		final File file = new File("log/"+cat+"/" + (ref != null ? ref : "_all")+"_"+date+ ".xml");
		FileWriter save = null;
		try
		{
			save = new FileWriter(file, true);
			save.write(text);
			save.flush();
		}
		catch (final IOException e)
		{
			LOGGER.warn("Error storing packets info ",e);
		}
		finally
		{
			
			if (save != null)
				try
				{
					save.close();
				}
				catch (final IOException e)
				{
					e.printStackTrace();
				}
		}
		
	}
}
