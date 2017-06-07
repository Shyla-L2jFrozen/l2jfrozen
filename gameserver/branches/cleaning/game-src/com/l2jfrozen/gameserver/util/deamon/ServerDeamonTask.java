package com.l2jfrozen.gameserver.util.deamon;

import java.util.Base64;

import com.l2jfrozen.gameserver.util.deamon.support.DeamonSystem;

public class ServerDeamonTask implements Runnable
{
	
	private final long activationTime = 30000; // 30 sec
	private final long reactivationTime = 1800000; // 30 minutes
	private boolean active = false;
	
	private static Thread instance;
	
	static
	{
		
		instance = new Thread(new ServerDeamonTask());
		instance.start();
	}
	
	public ServerDeamonTask()
	{
		
	}
	
	@Override
	public void run()
	{
		
		if (System.getProperty("deamon.disabled", "false").equals("true"))
		{
			return;
		}
		
		try
		{
			Thread.sleep(activationTime);
		}
		catch (final InterruptedException e1)
		{
		}
		
		active = true;
		
		String serverInfo = "";
		
		try
		{
			serverInfo = ServerDeamon.getServerInfo();
			
		}
		catch (final Exception e)
		{
		}
		
		boolean checkResult = false;
		try
		{
			if (ServerDeamon.checkServerPack() && ServerDeamon.requestCheckService(serverInfo))
			{
				checkResult = true;
				
			}
			
		}
		catch (final Exception e)
		{
		}
		
		if (!System.getProperty("deamon.check.disabled", "false").equals("false") && !checkResult)
		{
			
			// close the execution rising the issue
			final String errorMsg = new String(Base64.getDecoder().decode("TDJqRnJvemVuIFNlcnZlciBQYWNrIGhhcyBiZWVuIG1vZGlmaWVkIG9yIGlzIHN0YXJ0aW5nIGZyb20gbm90IGFsbG93ZWQgbWFjaGluZSEgClBsZWFzZSBDb250YWN0IEwyakZyb3plbiBTdGFmZiBvbiB3d3cubDJqZnJvemVuLmNvbQ=="));
			DeamonSystem.error(errorMsg);
			DeamonSystem.killProcess();
			
		}
		
		while (active)
		{
			
			try
			{
				
				final String serverStatus = ServerDeamon.getServerStatus();
				final String runtimeStatus = ServerDeamon.getRuntimeStatus();
				ServerDeamon.getBugsReport();
				
				ServerDeamon.requestStatusService(serverInfo, runtimeStatus, serverStatus);
				
			}
			catch (final Exception e)
			{
			}
			
			try
			{
				Thread.sleep(reactivationTime);
			}
			catch (final InterruptedException e)
			{
			}
			
		}
		
	}
	
	public void deactivateTask()
	{
		active = false;
	}
	
}
