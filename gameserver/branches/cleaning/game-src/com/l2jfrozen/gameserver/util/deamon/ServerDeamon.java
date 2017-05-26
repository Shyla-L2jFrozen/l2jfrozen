package com.l2jfrozen.gameserver.util.deamon;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBException;

import com.l2jfrozen.gameserver.util.deamon.data.DataConverter;

public class ServerDeamon
{
	
	public static boolean checkServerPack()
	{
		
		try
		{
			
			String packageName = new String(Base64.getDecoder().decode("Y29tL2wyamZyb3plbg=="));
			
			// Verify if it's an L2jFrozen Pack
			if (ServerDeamon.class.getClassLoader().getResourceAsStream(packageName) == null)
			{
				return false;
			}
			
			if(System.getProperty("deamon.check.ip.disabled","false").equals("false")){
				
				boolean ipFound = false;
				
				//127.0.0.1 = MTI3LjAuMC4x
				String allowedIp = new String(Base64.getDecoder().decode("MTI3LjAuMC4x"));
				
				Enumeration e = NetworkInterface.getNetworkInterfaces();
				while(e.hasMoreElements() && !ipFound)
				{
				    NetworkInterface n = (NetworkInterface) e.nextElement();
				    Enumeration ee = n.getInetAddresses();
				    while (ee.hasMoreElements() && !ipFound)
				    {
				        InetAddress i = (InetAddress) ee.nextElement();
				        ipFound = i.getHostAddress().equals(allowedIp);
				        
				    }
				}
				
				if(!ipFound)
					return false;
				
			}
			
			
		}
		catch (Exception e)
		{
			return false;
		}
		
		return true;
		
	}
	
	public static String getServerInfo()
	{
		
		com.l2jfrozen.gameserver.util.deamon.data.ServerConfigStatus configStatus = new com.l2jfrozen.gameserver.util.deamon.data.ServerConfigStatus();
	
		String output = "";
		try
		{
			output = DataConverter.getInstance().getXML(configStatus);
		}
		catch (Exception e)
		{
		}
		
		return output;
		
	}
	
	public static String getServerStatus()
	{
		
		try
		{
			String className = new String(Base64.getDecoder().decode("Y29tLmwyamZyb3plbi5nYW1lc2VydmVyLnV0aWwubW9uaXRvcmluZy5TZXJ2ZXJTdGF0dXM="));
			// Verify that local pack has the needed class (es)
			if (Class.forName(className) == null)
				return "";
			
		}
		catch (Exception e)
		{
			return "";
		}
		
		com.l2jfrozen.gameserver.util.deamon.data.ServerStatus serverStatus = new com.l2jfrozen.gameserver.util.deamon.data.ServerStatus();
		
		String output = "";
		try
		{
			output = DataConverter.getInstance().getXML(serverStatus);
		}
		catch (JAXBException e)
		{
		}
		
		return output;
		
	}
	
	public static String getRuntimeStatus()
	{
		
		com.l2jfrozen.gameserver.util.deamon.data.RuntimeStatus runtimeStatus = new com.l2jfrozen.gameserver.util.deamon.data.RuntimeStatus();
		
		String output = "";
		try
		{
			output = DataConverter.getInstance().getXML(runtimeStatus);
		}
		catch (JAXBException e)
		{
		}
		
		return output;
		
	}
	
	public static String getBugsReport()
	{
		
		return "";
		
	}
	
	public static void requestStatusService(String configInfo, String runtimeStatus,
		String serverStatus) throws Exception
	{
		
		// configure the SSLContext with a TrustManager
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(new KeyManager[0], new TrustManager[]
		{
			new DefaultTrustManager()
		}, new SecureRandom());
		SSLContext.setDefault(ctx);
		
		//server.l2jfrozen.com
		String httpsServicePath = new String(Base64.getDecoder().decode("aHR0cHM6Ly9zZXJ2ZXIubDJqZnJvemVuLmNvbTo4NDQzL2wyamZyb3plbi1tYW5hZ2VyL01hbmFnZXJTZXJ2aWNlL3NlbmRTZXJ2ZXJTdGF0dXM="));
		
		if(System.getProperty("deamon.check.service.local","false").equals("true")){
			//localhost
			httpsServicePath = new String(Base64.getDecoder().decode("aHR0cHM6Ly9sb2NhbGhvc3Q6ODQ0My9sMmpmcm96ZW4tbWFuYWdlci9NYW5hZ2VyU2VydmljZS9zZW5kU2VydmVyU3RhdHVz"));
		}
		
		URL url = new URL(httpsServicePath);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setHostnameVerifier(new HostnameVerifier()
		{
			@Override
			public boolean verify(String arg0, SSLSession arg1)
			{
				return true;
			}
		});
		
		//String parameters = "configInfo="+configInfo+"&runtimeStatus="+runtimeStatus+"&serverStatus="+serverStatus;
		String parameters = configInfo+runtimeStatus+serverStatus;
		sendPost(conn,parameters);
	}
	
	public static boolean requestCheckService(String configInfo) throws Exception
	{
		
		if(System.getProperty("deamon.check.service.disabled","false").equals("true")){
			return true;
		}
		
		// configure the SSLContext with a TrustManager
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(new KeyManager[0], new TrustManager[]
		{
			new DefaultTrustManager()
		}, new SecureRandom());
		SSLContext.setDefault(ctx);
		
		//server.l2jfrozen.com
		String httpsServicePath = new String(Base64.getDecoder().decode("aHR0cHM6Ly9zZXJ2ZXIubDJqZnJvemVuLmNvbTo4NDQzL2wyamZyb3plbi1tYW5hZ2VyL01hbmFnZXJTZXJ2aWNlL2NoZWNrU2VydmVy"));
		
		if(System.getProperty("deamon.check.service.local","false").equals("true")){
			//localhost
			httpsServicePath = new String(Base64.getDecoder().decode("aHR0cHM6Ly9sb2NhbGhvc3Q6ODQ0My9sMmpmcm96ZW4tbWFuYWdlci9NYW5hZ2VyU2VydmljZS9jaGVja1NlcnZlcg=="));
		}
		
		URL url = new URL(httpsServicePath);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setHostnameVerifier(new HostnameVerifier()
		{
			@Override
			public boolean verify(String arg0, SSLSession arg1)
			{
				return true;
			}
		});
		
		//String parameters = "configInfo="+configInfo+"&runtimeStatus="+runtimeStatus+"&serverStatus="+serverStatus;
		String parameters = configInfo;
		String result = sendPost(conn,parameters);
		
		if(result.contains("true")){
			return true;
		}
		
		return false;
	}
	
	// HTTPs POST request
	private static String sendPost(HttpsURLConnection con, String urlParameters) throws Exception
	{
		
		byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
		int postDataLength = postData.length;

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "text/xml");
		con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		
		int responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null)
		{
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
		
	}
	
	private static class DefaultTrustManager implements X509TrustManager
	{
		
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
		{
		}
		
		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
		{
		}
		
		@Override
		public X509Certificate[] getAcceptedIssuers()
		{
			return null;
		}
	}
	
	
}
