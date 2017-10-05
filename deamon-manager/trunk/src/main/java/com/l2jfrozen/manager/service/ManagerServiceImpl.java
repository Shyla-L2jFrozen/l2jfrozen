package com.l2jfrozen.manager.service;

import java.io.File;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import a.a.i;
import a.a.j;
import a.a.rs;

import com.l2jfrozen.Config;
import com.l2jfrozen.manager.api.ManagerService;
import com.l2jfrozen.manager.core.Server;

@Path("/ManagerService")
public class ManagerServiceImpl implements ManagerService {

	private static Logger serviceLogger = Logger.getGlobal();
	private static Server server = new Server();

	static{
		Config.loadConfig();
	}
	
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
	public void sendServerStatus(@FormParam("configInfo") String configInfo, @FormParam("runtimeStatus") String runtimeStatus,
			@FormParam("serverStatus") String serverStatus) {
		
		serviceLogger.info(configInfo);
		serviceLogger.info(runtimeStatus);
		serviceLogger.info(serverStatus);
		
		//DataConverter.getInstance().getObject(configInfo)
		a.a.i serverConfigInfo = (i) a.a.g.g().f(configInfo);
		a.a.rs runtimeStatusInfo = (rs) a.a.g.g().f(runtimeStatus);
		a.a.j serverStatusInfo = (j) a.a.g.g().f(serverStatus);
		
		server.sendInfo(serverConfigInfo.ggsn(), serverConfigInfo.glsi(), serverConfigInfo.glsp(), serverConfigInfo.ggsi(), serverConfigInfo.ggsp(),serverConfigInfo.h(),serverStatusInfo.ga());
		
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
		serviceLogger.info("testGet");
		return "<testGet>OK</testGet>";
	}
	

	@GET
	@Produces(MediaType.TEXT_XML)
	@Path("/establishConnection")
	public String establishConnection() {
		serviceLogger.info("establishConnection");
		return "<establishConnection>true</establishConnection>";
	}

	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/updateNetcore")
	public Response updateNetcore() {
		serviceLogger.info("updateNetcore");
		
		File file = new File(Config.NETCORE_FILE); // Initialize this to the File path you want to serve.
		return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
	      .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
	      .header("Version", Config.NETCORE_VERSION ) //optional
	      .build();
	}


}
