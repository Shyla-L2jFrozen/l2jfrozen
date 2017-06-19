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
package com.l2jfrozen.util;

import org.apache.log4j.Logger;

import javolution.text.TextBuilder;

/**
 * This class ...
 * @version $Revision: 1.2 $ $Date: 2004/06/27 08:12:59 $
 * @author luisantonioa
 */
public class Util
{
	protected static final Logger LOGGER = Logger.getLogger(Util.class);
	
	public static boolean isInternalIP(final String ipAddress)
	{
		return ipAddress.startsWith("192.168.") || ipAddress.startsWith("10.") || ipAddress.startsWith("127.0.0.1");
	}
	
	public static String printData(final byte[] data, final int len)
	{
		final TextBuilder result = new TextBuilder();
		
		int counter = 0;
		
		for (int i = 0; i < len; i++)
		{
			if (counter % 16 == 0)
			{
				result.append(fillHex(i, 4) + ": ");
			}
			
			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16)
			{
				result.append("   ");
				
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++)
				{
					final int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80)
					{
						result.append((char) t1);
					}
					else
					{
						result.append('.');
					}
				}
				
				result.append('\n');
				counter = 0;
			}
		}
		
		final int rest = data.length % 16;
		if (rest > 0)
		{
			for (int i = 0; i < 17 - rest; i++)
			{
				result.append("   ");
			}
			
			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++)
			{
				final int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80)
				{
					result.append((char) t1);
				}
				else
				{
					result.append('.');
				}
			}
			
			result.append('\n');
		}
		
		return result.toString();
	}
	
	public static String fillHex(final int data, final int digits)
	{
		String number = Integer.toHexString(data);
		
		for (int i = number.length(); i < digits; i++)
		{
			number = "0" + number;
		}
		
		return number;
	}
	
	/**
	 * @param s
	 */
	
	public static void printSection(String s)
	{
		final int maxlength = 79;
		s = "-[ " + s + " ]";
		final int slen = s.length();
		if (slen > maxlength)
		{
			LOGGER.info(s);
			return;
		}
		int i;
		for (i = 0; i < maxlength - slen; i++)
		{
			s = "=" + s;
		}
		LOGGER.info(s);
	}
	
	/**
	 * @param raw
	 * @return
	 */
	public static String printData(final byte[] raw)
	{
		return printData(raw, raw.length);
	}
	
	/**
	 * converts a given time from minutes -> miliseconds
	 * @param minutesToConvert
	 * @return
	 */
	public static int convertMinutesToMiliseconds(final int minutesToConvert)
	{
		return minutesToConvert * 60000;
	}
	
}
