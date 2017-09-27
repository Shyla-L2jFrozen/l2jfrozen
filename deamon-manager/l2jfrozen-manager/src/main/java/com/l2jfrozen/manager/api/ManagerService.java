package com.l2jfrozen.manager.api;

import javax.xml.ws.Response;

public interface ManagerService {

	public String checkServer(String configInfo);
	public void sendServerStatus(String configInfo, String runtimeStatus, String serverStatus);	
	public void submitBugsReport(String bugsReport);
	public String establishConnection();
	public Response updateNetcore();
	
}
