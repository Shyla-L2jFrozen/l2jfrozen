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

import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.l2jfrozen.loginserver.network.L2LoginClient;
import com.l2jfrozen.loginserver.network.serverpackets.Init;
import com.l2jfrozen.netcore.IAcceptFilter;
import com.l2jfrozen.netcore.IClientFactory;
import com.l2jfrozen.netcore.IMMOExecutor;
import com.l2jfrozen.netcore.MMOConnection;
import com.l2jfrozen.netcore.ReceivablePacket;
import com.l2jfrozen.netcore.util.IPv4Filter;

/**
 * @author KenM
 */
public class SelectorHelper implements IMMOExecutor<L2LoginClient>, IClientFactory<L2LoginClient>, IAcceptFilter
{
	private static final Logger LOG = Logger.getLogger(LoginController.class.getName());
	private final ThreadPoolExecutor _generalPacketsThreadPool;
	private final IPv4Filter _ipv4filter;
	
	public SelectorHelper()
	{
		_generalPacketsThreadPool = new ThreadPoolExecutor(4, 6, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		_ipv4filter = new IPv4Filter();
	}
	
	@Override
	public void execute(final ReceivablePacket<L2LoginClient> packet)
	{
		_generalPacketsThreadPool.execute(packet);
	}
	
	@Override
	public L2LoginClient create(final MMOConnection<L2LoginClient> con)
	{
		final L2LoginClient client = new L2LoginClient(con);
		client.sendPacket(new Init(client));
		return client;
	}
	
	@Override
	public boolean accept(final SocketChannel sc)
	{
		try
		{
			return _ipv4filter.accept(sc) && !LoginController.getInstance().isBannedAddress(sc.socket().getInetAddress());
		}
		catch (final UnknownHostException e)
		{
			LOG.severe(SelectorHelper.class.getSimpleName() + ": Invalid address: " + sc.socket().getInetAddress() + "; " + e.getMessage());
		}
		return false;
	}
}
