/*
 * Copyright (C) 2004-2016 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jfrozen.loginserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author -Wooden-
 */
public abstract class FloodProtectedListener extends Thread
{
	private final Logger _log = Logger.getLogger(FloodProtectedListener.class.getName());
	private final Map<String, ForeignConnection> _floodProtection = new ConcurrentHashMap<>();
	private ServerSocket _serverSocket;
	
	public FloodProtectedListener(final String listenIp, final int port) throws IOException
	{
		if (listenIp.equals("*"))
		{
			_serverSocket = new ServerSocket(port);
		}
		else
		{
			_serverSocket = new ServerSocket(port, 50, InetAddress.getByName(listenIp));
		}
	}
	
	@Override
	public void run()
	{
		Socket connection = null;
		while (!isInterrupted())
		{
			try
			{
				connection = _serverSocket.accept();
				if (LoginConfig.FLOOD_PROTECTION)
				{
					ForeignConnection fConnection = _floodProtection.get(connection.getInetAddress().getHostAddress());
					if (fConnection != null)
					{
						fConnection.connectionNumber += 1;
						if (((fConnection.connectionNumber > LoginConfig.FAST_CONNECTION_LIMIT) && ((System.currentTimeMillis() - fConnection.lastConnection) < LoginConfig.NORMAL_CONNECTION_TIME)) || ((System.currentTimeMillis() - fConnection.lastConnection) < LoginConfig.FAST_CONNECTION_TIME) || (fConnection.connectionNumber > LoginConfig.MAX_CONNECTION_PER_IP))
						{
							fConnection.lastConnection = System.currentTimeMillis();
							connection.close();
							fConnection.connectionNumber -= 1;
							if (!fConnection.isFlooding)
							{
								_log.warning("Potential Flood from " + connection.getInetAddress().getHostAddress());
							}
							fConnection.isFlooding = true;
							continue;
						}
						if (fConnection.isFlooding) // if connection was flooding server but now passed the check
						{
							fConnection.isFlooding = false;
							_log.info(connection.getInetAddress().getHostAddress() + " is not considered as flooding anymore.");
						}
						fConnection.lastConnection = System.currentTimeMillis();
					}
					else
					{
						fConnection = new ForeignConnection(System.currentTimeMillis());
						_floodProtection.put(connection.getInetAddress().getHostAddress(), fConnection);
					}
				}
				
				addClient(connection);
			}
			catch (final Exception e)
			{
				if (isInterrupted())
				{
					// shutdown?
					try
					{
						_serverSocket.close();
					}
					catch (final IOException io)
					{
						_log.log(Level.INFO, "", io);
					}
					break;
				}
			}
		}
	}
	
	
	
	public abstract void addClient(Socket s);
	
	public void removeFloodProtection(final String ip)
	{
		if (!LoginConfig.FLOOD_PROTECTION)
		{
			return;
		}
		final ForeignConnection fConnection = _floodProtection.get(ip);
		if (fConnection != null)
		{
			fConnection.connectionNumber -= 1;
			if (fConnection.connectionNumber == 0)
			{
				_floodProtection.remove(ip);
			}
		}
		else
		{
			_log.warning("Removing a flood protection for a GameServer that was not in the connection map??? :" + ip);
		}
	}
	
	public void close()
	{
		try
		{
			_serverSocket.close();
		}
		catch (final IOException e)
		{
			_log.warning(getClass().getSimpleName() + ": " + e.getMessage());
		}
	}
}