package com.l2jfrozen.manager.service.test;

import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ConnectionTest {

//	public static void main(String[] args) throws Exception {
		
//		HttpClient clientHttp = new HttpClient();
//		
//		String httpServicePath_GET = "http://localhost:8080/l2jfrozen-manager/ManagerService/testGet";
//		String httpServicePath_POST = "http://localhost:8080/l2jfrozen-manager/ManagerService/checkServer";
//		
//		clientHttp.sendGet(httpServicePath_GET);
//		
//		String data = "<test>ProvaInput</test>";
//		clientHttp.sendPost(httpServicePath_POST,data );
		
//		String httpsServicePath = "https://www.l2jfrozen.com";
//		
//		HttpsClient clientHttps = new HttpsClient();
//		clientHttps.testHttps(httpsServicePath);
//		
//	}
	
	/*
	static {
	    //for localhost testing only
	    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
	    new javax.net.ssl.HostnameVerifier(){

	        public boolean verify(String hostname,
	                javax.net.ssl.SSLSession sslSession) {
	            if (hostname.equals("localhost")) {
	                return true;
	            }
	            return false;
	        }
	    });
	}*/
	
	
	public static void main(String [] args) throws Exception {
        // configure the SSLContext with a TrustManager
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
        SSLContext.setDefault(ctx);

        String httpsServicePath = "https://localhost:4343";
		URL url = new URL(httpsServicePath);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
        
        HttpsClient clientHttps = new HttpsClient();
		clientHttps.print_https_cert(conn);
		clientHttps.print_content(conn);
        
        System.out.println(conn.getResponseCode());
        conn.disconnect();
    }

    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

}
