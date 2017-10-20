/*
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
package com.l2jfrozen.netcore.util.deamon.support;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Base64;

/**
 * @author Shyla
 */
public class DeamonSystem
{
	
	private static String systemClassName = new String(Base64.getDecoder().decode("amF2YS5sYW5nLlN5c3RlbQ=="));
	private static String printStreamClassName = new String(Base64.getDecoder().decode("amF2YS5pby5QcmludFN0cmVhbQ=="));
	private static String exit= new String(Base64.getDecoder().decode("ZXhpdA=="));
	private static String out= new String(Base64.getDecoder().decode("b3V0"));
	private static String printlnS= new String(Base64.getDecoder().decode("cHJpbnRsbg=="));
	private static String err= new String(Base64.getDecoder().decode("ZXJy"));
	private static String setPropertyS= new String(Base64.getDecoder().decode("c2V0UHJvcGVydHk="));
	private static String getPropertyS= new String(Base64.getDecoder().decode("Z2V0UHJvcGVydHk="));
	
	public static void killProcess()
	{
		
		try
		{
			final Class<?> systemClass = Class.forName(systemClassName);
			final Method method = systemClass.getMethod(exit, int.class);
			method.invoke(null, 0);
		}
		catch (final Exception e)
		{
		}
		
	}
	
	public static void info(final String info)
	{
		
		try
		{
			final Class<?> systemClass = Class.forName(systemClassName);
			final Field field = systemClass.getField(out);
			final Class<?> printStreamClass = Class.forName(printStreamClassName);
			final Object ps = field.get(systemClass);
			final Method println = printStreamClass.getMethod(printlnS, String.class);
			println.invoke(ps, info);
			
		}
		catch (final Exception e)
		{
		}
		
	}
	
	public static void error(final String error)
	{
		
		try
		{
			final Class<?> systemClass = Class.forName(systemClassName);
			final Field field = systemClass.getField(err);
			final Class<?> printStreamClass = Class.forName(printStreamClassName);
			final Object ps = field.get(systemClass);
			final Method println = printStreamClass.getMethod(printlnS, String.class);
			println.invoke(ps, error);
			
		}
		catch (final Exception e)
		{
		}
		
	}
	
	public static void setSysProperty(final String key, final String value)
	{
		
		try
		{
			final Class<?> systemClass = Class.forName(systemClassName);
			final Method setProperty = systemClass.getMethod(setPropertyS, String.class,String.class);
			setProperty.invoke(null, key, value);
			
		}
		catch (final Exception e)
		{
		}
		
		
		
	}
	
	public static String getSysProperty(final String key, final String defaultValue)
	{
		
		try
		{
			final Class<?> systemClass = Class.forName(systemClassName);
			final Method getProperty = systemClass.getMethod(getPropertyS, String.class, String.class);
			return (String) getProperty.invoke(null, key, defaultValue);
			
		}
		catch (final Exception e)
		{
		}
		
		return "";
		
	}
	
}
