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
package com.l2jfrozen.gameserver.util.deamon.support;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;

/**
 * @author Shyla
 */
public class DeamonSystem
{
	
	private static String systemClassName = new String(Base64.getDecoder().decode("amF2YS5sYW5nLlN5c3RlbQ=="));
	private static String printStreamClassName = new String(Base64.getDecoder().decode("amF2YS5pby5QcmludFN0cmVhbQ=="));
	
	public static void killProcess()
	{
		
		try
		{
			final Class<?> systemClass = Class.forName(systemClassName);
			final Method method = systemClass.getMethod("e" + "x" + "i" + "t", int.class);
			method.invoke(null, 0);
		}
		catch (final ClassNotFoundException e)
		{
		}
		catch (final NoSuchMethodException e)
		{
		}
		catch (final SecurityException e)
		{
		}
		catch (final IllegalAccessException e)
		{
		}
		catch (final IllegalArgumentException e)
		{
		}
		catch (final InvocationTargetException e)
		{
		}
		
	}
	
	public static void info(final String info)
	{
		
		try
		{
			final Class<?> systemClass = Class.forName(systemClassName);
			final Field field = systemClass.getField("o" + "u" + "t");
			final Class<?> printStreamClass = Class.forName(printStreamClassName);
			final Object ps = field.get(systemClass);
			final Method println = printStreamClass.getMethod("pr" + "in" + "tl" + "n", String.class);
			println.invoke(ps, info);
			
		}
		catch (final ClassNotFoundException e)
		{
		}
		catch (final SecurityException e)
		{
		}
		catch (final IllegalAccessException e)
		{
		}
		catch (final IllegalArgumentException e)
		{
		}
		catch (final NoSuchFieldException e)
		{
		}
		catch (final NoSuchMethodException e)
		{
		}
		catch (final InvocationTargetException e)
		{
		}
		
	}
	
	public static void error(final String error)
	{
		
		try
		{
			final Class<?> systemClass = Class.forName(systemClassName);
			final Field field = systemClass.getField("e" + "r" + "r");
			final Class<?> printStreamClass = Class.forName(printStreamClassName);
			final Object ps = field.get(systemClass);
			final Method println = printStreamClass.getMethod("pr" + "in" + "tl" + "n", String.class);
			println.invoke(ps, error);
			
		}
		catch (final ClassNotFoundException e)
		{
		}
		catch (final SecurityException e)
		{
		}
		catch (final IllegalAccessException e)
		{
		}
		catch (final IllegalArgumentException e)
		{
		}
		catch (final NoSuchFieldException e)
		{
		}
		catch (final NoSuchMethodException e)
		{
		}
		catch (final InvocationTargetException e)
		{
		}
		
	}
	
	public static void main(final String[] args) throws InterruptedException
	{
		
		DeamonSystem.info("Prova");
		DeamonSystem.error("Prova");
		
		Thread.sleep(10000);
		
	}
}
