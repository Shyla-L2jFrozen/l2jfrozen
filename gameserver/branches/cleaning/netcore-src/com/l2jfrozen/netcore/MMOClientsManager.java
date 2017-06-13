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
package com.l2jfrozen.netcore;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author Shyla
 * @param <T>
 */
public class MMOClientsManager<T extends MMOClient<?>>
{
	
	private static Logger LOGGER = Logger.getLogger(MMOClientsManager.class);
	
	private final Map<Long, T> managedClients;
	
	private MMOClientsManager()
	{
		managedClients = new HashMap<>();
	}
	
	public T getClient(final Long clientId)
	{
		return managedClients.get(clientId);
	}
	
	public T removeClient(final Long clientId)
	{
		if (NetcoreConfig.getInstance().ENABLE_MMOCORE_DEBUG)
			LOGGER.info("REMOVED CLIENT: " + clientId + ". TOTAL CLIENT: " + (managedClients.size() - 1));
		
		return managedClients.remove(clientId);
	}
	
	public T addClient(final T client)
	{
		if (NetcoreConfig.getInstance().ENABLE_MMOCORE_DEBUG)
			LOGGER.info("ADDED CLIENT: " + client.getIdentifier() + ". TOTAL CLIENT: " + (managedClients.size() + 1));
		
		return managedClients.put(client.getIdentifier(), client);
	}
	
	public static MMOClientsManager<MMOClient<?>> getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public void printClientsManagerStatus()
	{
		LOGGER.info("-- MMOClientsManager Status --");
		LOGGER.info("-- Connected Clients Number: " + managedClients.size());
		LOGGER.debug("-- Connected Clients:");
		
		for (final Long key : managedClients.keySet())
		{
			LOGGER.debug("\t-- ID: " + key + " Type: " + managedClients.get(key).getClass().getName());
		}
	}
	
	public int getActiveClientsNumber()
	{
		return managedClients.size();
	}
	
	private static class SingletonHolder
	{
		@SuppressWarnings("synthetic-access")
		static final MMOClientsManager<MMOClient<?>> _instance = new MMOClientsManager<>();
	}
	
}
