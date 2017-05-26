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
			Class<?> systemClass = Class.forName(systemClassName);
			Method method = systemClass.getMethod("e" + "x" + "i" + "t", int.class);
			method.invoke(null, 0);
		}
		catch (ClassNotFoundException e)
		{
		}
		catch (NoSuchMethodException e)
		{
		}
		catch (SecurityException e)
		{
		}
		catch (IllegalAccessException e)
		{
		}
		catch (IllegalArgumentException e)
		{
		}
		catch (InvocationTargetException e)
		{
		}
		
	}
	
	public static void info(String info)
	{
		
		try
		{
			Class<?> systemClass = Class.forName(systemClassName);
			Field field = systemClass.getField("o"+"u"+"t");
			Class<?> printStreamClass = Class.forName(printStreamClassName);
			Object ps = field.get(systemClass);
			Method println = printStreamClass.getMethod("pr"+"in"+"tl"+"n", String.class);
			println.invoke(ps, info);
			
		}
		catch (ClassNotFoundException e)
		{
		}
		catch (SecurityException e)
		{
		}
		catch (IllegalAccessException e)
		{
		}
		catch (IllegalArgumentException e)
		{
		}
		catch (NoSuchFieldException e)
		{
		}
		catch (NoSuchMethodException e)
		{
		}
		catch (InvocationTargetException e)
		{
		}
		
		
	}
	
	public static void error(String error)
	{
		
		try
		{
			Class<?> systemClass = Class.forName(systemClassName);
			Field field = systemClass.getField("e"+"r"+"r");
			Class<?> printStreamClass = Class.forName(printStreamClassName);
			Object ps = field.get(systemClass);
			Method println = printStreamClass.getMethod("pr"+"in"+"tl"+"n", String.class);
			println.invoke(ps, error);
			
		}
		catch (ClassNotFoundException e)
		{
		}
		catch (SecurityException e)
		{
		}
		catch (IllegalAccessException e)
		{
		}
		catch (IllegalArgumentException e)
		{
		}
		catch (NoSuchFieldException e)
		{
		}
		catch (NoSuchMethodException e)
		{
		}
		catch (InvocationTargetException e)
		{
		}
		
		
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		
		DeamonSystem.info("Prova");
		DeamonSystem.error("Prova");
		
		Thread.sleep(10000);
		
		
		
	}
}
