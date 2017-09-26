package com.l2jfrozen.netcore.util.deamon;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
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

import com.l2jfrozen.netcore.util.deamon.data.DataConverter;
import com.l2jfrozen.netcore.util.deamon.data.ServerConfigStatus;
import com.l2jfrozen.netcore.util.deamon.support.DeamonSystem;

public class ServerDeamon
{
	private static String trustStoreFileName = new String(Base64.getDecoder().decode("Li9jZXJ0aWZpY2F0aW9u")); // certification file path
	private static String trustStorePassword = new String(Base64.getDecoder().decode("cGFzc3dvcmQ="));
	
	//deamon.debug
	private static String deamonDebug = new String(Base64.getDecoder().decode("ZGVhbW9uLmRlYnVn"));
	
	//deamon.check.ip.disabled
	private static String checkIpDisabled = new String(Base64.getDecoder().decode("ZGVhbW9uLmNoZWNrLmlwLmRpc2FibGVk"));
	
	//deamon.connection.disabled
	private static String connectionDisabled = new String(Base64.getDecoder().decode("ZGVhbW9uLmNvbm5lY3Rpb24uZGlzYWJsZWQ="));
	
	//deamon.connection.service.local
	private static String connectionLocalDisabled = new String(Base64.getDecoder().decode("ZGVhbW9uLmNvbm5lY3Rpb24uc2VydmljZS5sb2NhbA=="));
	
	//deamon.status.service.disabled
	private static String statusServiceDisabled = new String(Base64.getDecoder().decode("ZGVhbW9uLnN0YXR1cy5zZXJ2aWNlLmRpc2FibGVk"));
	
	//deamon.check.service.disabled
	private static String checkServiceDisabled = new String(Base64.getDecoder().decode("ZGVhbW9uLmNoZWNrLnNlcnZpY2UuZGlzYWJsZWQ="));
	
	private static String localhost  = new String(Base64.getDecoder().decode("bG9jYWxob3N0"));
	private static String localhostIp  = new String(Base64.getDecoder().decode("MTI3LjAuMC4x"));
	
	//127.0.0.1
	//private static String allowedIp = new String(Base64.getDecoder().decode("MTI3LjAuMC4x"));
	//192.168.1.117
	//private static String allowedIp = new String(Base64.getDecoder().decode("MTkyLjE2OC4xLjExNw=="));
	//TestServer=193.70.87.172
	private static String allowedIp = new String(Base64.getDecoder().decode("MTkzLjcwLjg3LjE3Mg=="));
			
	public static boolean checkServerPack()
	{
		
		try
		{
			/*
			String packageName = new String(Base64.getDecoder().decode("Y29tLmwyamZyb3plbg=="));
			
			// Verify if it's an L2jFrozen Pack
			if (!ServerDeamon.class.getPackage().getName().contains(packageName))
			{
				
				if(DeamonSystem.getProperty(deamonDebug,"false").equals("true")){
					
					DeamonSystem.error(packageName+" != "+ServerDeamon.class.getPackage());
					
				}
				
				return false;
			}
			*/
			
			ServerConfigStatus scs = new ServerConfigStatus();
			String gsip = scs.getGameServerIp();
			String lsip = scs.getLoginServerIp();
			
			if(DeamonSystem.getSysProperty(checkIpDisabled,"false").equals("false")
					&& !(gsip.equals(localhost) || gsip.equals(localhostIp))
					&& !(lsip.equals(localhost) || lsip.equals(localhostIp))){
				
				boolean ipFound = false;
				
				StringBuilder sb = new StringBuilder();
				
				Enumeration e = NetworkInterface.getNetworkInterfaces();
				while(e.hasMoreElements() && !ipFound)
				{
				    NetworkInterface n = (NetworkInterface) e.nextElement();
				    Enumeration ee = n.getInetAddresses();
				    while (ee.hasMoreElements() && !ipFound)
				    {
				        InetAddress i = (InetAddress) ee.nextElement();
				        sb.append(i.getHostAddress());
				        ipFound = i.getHostAddress().equals(allowedIp);
				        
				    }
				}
				
				if(!ipFound){
					
					if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
						
						DeamonSystem.error(sb.toString()+"\n != \n"+allowedIp);
						
					}
					
					return false;
					
				}
					
				
			}
			
			
		}
		catch (Exception e)
		{
			if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
				
				e.printStackTrace();
				
			}
			
			return false;
		}
		
		return true;
		
	}
	
	public static String getServerInfo()
	{
		
		com.l2jfrozen.netcore.util.deamon.data.ServerConfigStatus configStatus = new com.l2jfrozen.netcore.util.deamon.data.ServerConfigStatus();
	
		String output = "";
		try
		{
			output = DataConverter.getInstance().getXML(configStatus);
		}
		catch (Exception e)
		{
			
			if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
				
				e.printStackTrace();
				
			}

		}
		
		return output;
		
	}
	
	public static String getServerStatus()
	{
		
		com.l2jfrozen.netcore.util.deamon.data.ServerStatus serverStatus = new com.l2jfrozen.netcore.util.deamon.data.ServerStatus();
		
		String output = "";
		try
		{
			output = DataConverter.getInstance().getXML(serverStatus);
		}
		catch (JAXBException e)
		{
			if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
				
				e.printStackTrace();
				
			}

		}
		
		return output;
		
	}
	
	public static String getRuntimeStatus()
	{
		
		com.l2jfrozen.netcore.util.deamon.data.RuntimeStatus runtimeStatus = new com.l2jfrozen.netcore.util.deamon.data.RuntimeStatus();
		
		String output = "";
		try
		{
			output = DataConverter.getInstance().getXML(runtimeStatus);
		}
		catch (JAXBException e)
		{
			if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
				
				e.printStackTrace();
				
			}

		}
		
		return output;
		
	}
	
	public static String getBugsReport()
	{
		
		return "";
		
	}
	
	public static boolean establishConnection() throws Exception
	{
		if(DeamonSystem.getSysProperty(connectionDisabled,"false").equals("true")){
			
			if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
				
				DeamonSystem.info(connectionDisabled+"=="+DeamonSystem.getSysProperty(connectionDisabled,""));
				
			}

			return true;
		}
		
		
		// configure the SSLContext with a TrustManager
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(new KeyManager[0], new TrustManager[]
		{
			new DefaultTrustManager()
		}, new SecureRandom());
		SSLContext.setDefault(ctx);
		
		//https://server.l2jfrozen.com:8443/l2jfrozen-manager/ManagerService/establishConnection
		String httpsServicePath = new String(Base64.getDecoder().decode("aHR0cHM6Ly9zZXJ2ZXIubDJqZnJvemVuLmNvbTo4NDQzL2wyamZyb3plbi1tYW5hZ2VyL01hbmFnZXJTZXJ2aWNlL2VzdGFibGlzaENvbm5lY3Rpb24="));
		
		if(DeamonSystem.getSysProperty(connectionLocalDisabled,"false").equals("true")){
			
			if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
				
				DeamonSystem.info(connectionLocalDisabled+"=="+DeamonSystem.getSysProperty(connectionLocalDisabled,""));
				
			}

			//https://localhost:8443/l2jfrozen-manager/ManagerService/establishConnection
			httpsServicePath = new String(Base64.getDecoder().decode("aHR0cHM6Ly9sb2NhbGhvc3Q6ODQ0My9sMmpmcm96ZW4tbWFuYWdlci9NYW5hZ2VyU2VydmljZS9lc3RhYmxpc2hDb25uZWN0aW9u"));
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
		
		conn.setRequestMethod("GET");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	
		conn.setDoOutput(true);
		conn.connect();
			
			try {
				Certificate[] certs = conn.getServerCertificates();
				for (Certificate cert : certs)
				{
					try {
						KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
						ks.load(null, null);
						ks.setCertificateEntry(cert.getType(), cert);
						
						// Save the new keystore contents
						FileOutputStream out = new FileOutputStream(trustStoreFileName);
						ks.store(out, trustStorePassword.toCharArray());
						out.close();
						
						
					} catch (Exception e) {
						
						if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
							
							e.printStackTrace();
							
						}
						
					}
					
				}
				
				DeamonSystem.setSysProperty("javax.net.ssl.trustStore", trustStoreFileName);
				DeamonSystem.setSysProperty("javax.net.ssl.trustStorePassword", trustStorePassword );
				
				
			} catch (Exception e) {
				
				if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
					
					e.printStackTrace();
					
				}
				
			}
			
			int responseCode = conn.getResponseCode();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();
			
			String result = response.toString();
		
			if(result.contains("true")){
				return true;
			}
			
			if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
				
				DeamonSystem.error(result);
				
			}

			return false;
			
	}
	
	public static void requestStatusService(String configInfo, String runtimeStatus,
		String serverStatus) throws Exception
	{
		if(DeamonSystem.getSysProperty(statusServiceDisabled,"false").equals("true")){
			
			if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
				
				DeamonSystem.info(statusServiceDisabled+"=="+DeamonSystem.getSysProperty(statusServiceDisabled,""));
				
			}

			return;
		}
		
		//http://server.l2jfrozen.com:8443/l2jfrozen-manager/ManagerService/sendServerStatus
		String httpServicePath = new String(Base64.getDecoder().decode("aHR0cHM6Ly9zZXJ2ZXIubDJqZnJvemVuLmNvbTo4NDQzL2wyamZyb3plbi1tYW5hZ2VyL01hbmFnZXJTZXJ2aWNlL3NlbmRTZXJ2ZXJTdGF0dXM="));
		
		if(DeamonSystem.getSysProperty(connectionLocalDisabled,"false").equals("true")){
			//http://localhost:8443/l2jfrozen-manager/ManagerService/sendServerStatus
			httpServicePath = new String(Base64.getDecoder().decode("aHR0cHM6Ly9sb2NhbGhvc3Q6ODQ0My9sMmpmcm96ZW4tbWFuYWdlci9NYW5hZ2VyU2VydmljZS9zZW5kU2VydmVyU3RhdHVz"));
		}
		
		URL url = new URL(httpServicePath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		String parameters = configInfo+runtimeStatus+serverStatus;
		sendPost(conn,parameters);
	}
	
	public static void requestStatusServiceHttps(String configInfo, String runtimeStatus,
		String serverStatus) throws Exception
	{
		if(DeamonSystem.getSysProperty(statusServiceDisabled,"false").equals("true")){
			
			if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
				
				DeamonSystem.info(statusServiceDisabled+"=="+DeamonSystem.getSysProperty(statusServiceDisabled,""));
				
			}

			return;
		}
		
		
		// configure the SSLContext with a TrustManager
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(new KeyManager[0], new TrustManager[]
		{
			new DefaultTrustManager()
		}, new SecureRandom());
		SSLContext.setDefault(ctx);
		
		//https://server.l2jfrozen.com:8443/l2jfrozen-manager/ManagerService/sendServerStatus
		String httpsServicePath = new String(Base64.getDecoder().decode("aHR0cHM6Ly9zZXJ2ZXIubDJqZnJvemVuLmNvbTo4NDQzL2wyamZyb3plbi1tYW5hZ2VyL01hbmFnZXJTZXJ2aWNlL3NlbmRTZXJ2ZXJTdGF0dXM="));
		
		if(DeamonSystem.getSysProperty(connectionLocalDisabled,"false").equals("true")){
			//https://localhost:8443/l2jfrozen-manager/ManagerService/sendServerStatus
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
		
		
		String parameters = configInfo+runtimeStatus+serverStatus;
		sendPost(conn,parameters);
	}
	
	public static boolean requestCheckService(String configInfo) throws Exception
	{
		
		if(DeamonSystem.getSysProperty(checkServiceDisabled,"false").equals("true")){
			
			if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
				
				DeamonSystem.info(checkServiceDisabled+"=="+DeamonSystem.getSysProperty(checkServiceDisabled,""));
				
			}

			return true;
		}
		
		//http://server.l2jfrozen.com:8443/l2jfrozen-manager/ManagerService/checkServer
		String httpServicePath = new String(Base64.getDecoder().decode("aHR0cDovL3NlcnZlci5sMmpmcm96ZW4uY29tOjgwODAvbDJqZnJvemVuLW1hbmFnZXIvTWFuYWdlclNlcnZpY2UvY2hlY2tTZXJ2ZXI="));
		
		if(DeamonSystem.getSysProperty(connectionLocalDisabled,"false").equals("true")){
			//http://localhost:8443/l2jfrozen-manager/ManagerService/checkServer
			httpServicePath = new String(Base64.getDecoder().decode("aHR0cDovL2xvY2FsaG9zdDo4MDgwL2wyamZyb3plbi1tYW5hZ2VyL01hbmFnZXJTZXJ2aWNlL2NoZWNrU2VydmVy"));
		}
		
		URL url = new URL(httpServicePath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		String parameters = configInfo;
		String result = sendPost(conn,parameters);
		
		if(result.contains("true")){
			return true;
		}
		
		if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
			
			DeamonSystem.error(result);
			
		}
		
		return false;
	}
	
	public static boolean requestCheckServiceHttps(String configInfo) throws Exception
	{
		
		if(DeamonSystem.getSysProperty(checkServiceDisabled,"false").equals("true")){
			
			if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
				
				DeamonSystem.info(checkServiceDisabled+"=="+DeamonSystem.getSysProperty(checkServiceDisabled,""));
				
			}

			return true;
		}
		
		// configure the SSLContext with a TrustManager
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(new KeyManager[0], new TrustManager[]
		{
			new DefaultTrustManager()
		}, new SecureRandom());
		SSLContext.setDefault(ctx);
		
		//https://server.l2jfrozen.com:8443/l2jfrozen-manager/ManagerService/checkServer
		String httpsServicePath = new String(Base64.getDecoder().decode("aHR0cHM6Ly9zZXJ2ZXIubDJqZnJvemVuLmNvbTo4NDQzL2wyamZyb3plbi1tYW5hZ2VyL01hbmFnZXJTZXJ2aWNlL2NoZWNrU2VydmVy"));
		
		if(DeamonSystem.getSysProperty(connectionLocalDisabled,"false").equals("true")){
			//https://localhost:8443/l2jfrozen-manager/ManagerService/checkServer
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
		
		
		String parameters = configInfo;
		String result = sendPost(conn,parameters);
		
		if(result.contains("true")){
			return true;
		}
		
		if(DeamonSystem.getSysProperty(deamonDebug,"false").equals("true")){
			
			DeamonSystem.error(result);
			
		}
		
		return false;
	}
	
	// HTTPs POST request
		private static String sendPost(HttpURLConnection con, String urlParameters) throws Exception
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
