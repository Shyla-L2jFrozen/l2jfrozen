package com.l2jfrozen.manager.service;

import java.io.File;
import java.util.Enumeration;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.ws.Response;

import com.l2jfrozen.manager.api.ManagerService;

@Path("/ManagerService")
public class ManagerServiceImpl implements ManagerService {

	//private static Logger serviceLogger = LogManager.getLogManager().getLogger("org.apache.tomcat");
	private static Logger serviceLogger = LogManager.getLogManager().getLogger("global");
	
	@POST
	@Consumes(MediaType.TEXT_XML)
	@Produces(MediaType.TEXT_XML)
	@Path("/checkServer")
	public String checkServer(String configInfo) {
		serviceLogger.info(configInfo);
		return "<result>true</result>";
	}

	@POST
	@Consumes(MediaType.TEXT_XML)
	@Path("/sendServerStatus")
	public void sendServerStatus(String configInfo, String runtimeStatus,
			String serverStatus) {
		serviceLogger.info(configInfo);
		serviceLogger.info(runtimeStatus);
		serviceLogger.info(serverStatus);
	}

	@POST
	@Consumes(MediaType.TEXT_XML)
	@Path("/submitBugsReport")
	public void submitBugsReport(String report) {
		serviceLogger.info(report);
	}
	
	
	@GET
	@Produces(MediaType.TEXT_XML)
	@Path("/testGet")
	public String testGet() {
		//serviceLogger.info("testGet");
		return "<testGet>OK</testGet>";
	}
	

	@GET
	@Produces(MediaType.TEXT_XML)
	@Path("/establishConnection")
	public String establishConnection() {
		return "<establishConnection>true</establishConnection>";
	}

	@Override
	public Response updateNetcore() {
		
		/*
		File file = new File(FILE_PATH);  
		   
        ResponseBuilder response = Response.ok((Object) file);  
        response.header("Content-Disposition","attachment; filename=\"javatpoint_file.txt\"");  
        return response.build(); 
        */
		
		return null;
	}



}
