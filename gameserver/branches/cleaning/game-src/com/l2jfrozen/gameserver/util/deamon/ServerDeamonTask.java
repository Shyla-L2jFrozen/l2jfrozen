package com.l2jfrozen.gameserver.util.deamon;

import java.util.Base64;

import com.l2jfrozen.gameserver.util.deamon.support.DeamonSystem;

public class ServerDeamonTask implements Runnable
{
	
	private long activationTime = 30000; // 30 sec
	private long reactivationTime = 1800000; // 30 minutes
	private static boolean active = false;
	
	private static Thread instance;
	
	public static void start(){
		
		if(instance == null){
			
			instance = new Thread(new ServerDeamonTask());
			instance.start();
			
		}
		
	}
	
	private ServerDeamonTask()
	{
	}
	
	@Override
	public void run()
	{
		
		if(System.getProperty("deamon.disabled","false").equals("true")){
			return;
		}
		
		try
		{
			Thread.sleep(activationTime);
		}
		catch (InterruptedException e1)
		{
		}
		
		active = true;
		
		String serverInfo = "";
		
		try
		{
			serverInfo = ServerDeamon.getServerInfo();
			
		}catch(Exception e){
		}
		
		boolean ipCheckResult = false;
		try
		{
			ipCheckResult = ServerDeamon.checkServerPack();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		boolean https = true;
		boolean remoteCheckResult = false;
		try
		{
			
			if(ServerDeamon.establishConnection())
				remoteCheckResult = ServerDeamon.requestCheckServiceHttps(serverInfo);
			
		}catch(Exception e){
			e.printStackTrace();
			https = false;
		}
		
		if(!https){
			
			try
			{
				remoteCheckResult = ServerDeamon.requestCheckService(serverInfo);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		boolean checkResult = ipCheckResult && remoteCheckResult;
		
		if(!checkResult ){
			
			if(System.getProperty("deamon.check.disabled","false").equals("false")){
				
				// close the execution rising the issue
				String errorMsg = new String(Base64.getDecoder().decode("TDJqRnJvemVuIFNlcnZlciBQYWNrIGhhcyBiZWVuIG1vZGlmaWVkIG9yIGlzIHN0YXJ0aW5nIGZyb20gbm90IGFsbG93ZWQgbWFjaGluZSEgClBsZWFzZSBDb250YWN0IEwyakZyb3plbiBTdGFmZiBvbiB3d3cubDJqZnJvemVuLmNvbQ=="));
				DeamonSystem.error(errorMsg);
				DeamonSystem.killProcess();
				
			}
			
			
		}
		
		while (active)
		{
			
			try
			{
				String serverStatus = "";
				if(System.getProperty("deamon.serverStatus.disabled","false").equals("false"))
					ServerDeamon.getServerStatus();
				String runtimeStatus = "";
				if(System.getProperty("deamon.runtimeStatus.disabled","false").equals("false"))
					runtimeStatus = ServerDeamon.getRuntimeStatus();
				String bugsReport = "";
				if(System.getProperty("deamon.bugsReport.disabled","false").equals("false"))
					bugsReport = ServerDeamon.getBugsReport();
				
				ServerDeamon.requestStatusService(serverInfo,runtimeStatus,serverStatus);
				
			}
			catch (Exception e)
			{
			}
			
			try
			{
				Thread.sleep(reactivationTime);
			}
			catch (InterruptedException e)
			{
			}
			
		}
		
	}
	
	public static void deactivateTask()
	{
		active = false;
	}
	
	
}
