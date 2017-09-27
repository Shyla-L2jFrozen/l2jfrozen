package com.l2jfrozen.manager.service;

import java.util.Set;
import java.util.HashSet;
import javax.ws.rs.core.Application;

public class ManagerServiceApplication extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();
	public ManagerServiceApplication(){
	     singletons.add(new ManagerServiceImpl());
	}
	@Override
	public Set<Class<?>> getClasses() {
	     return empty;
	}
	@Override
	public Set<Object> getSingletons() {
	     return singletons;
	}
}
