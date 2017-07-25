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
package com.l2jfrozen.loginserver;

import java.net.UnknownHostException;

import com.l2jfrozen.netcore.util.IPSubnet;

/**
 * @author Shyla
 *
 */
/**
 * The Class GameServerAddress.
 */
public class GameServerAddress extends IPSubnet
{
	private final String _serverAddress;
	
	/**
	 * Instantiates a new game server address.
	 * @param subnet the subnet
	 * @param address the address
	 * @throws UnknownHostException the unknown host exception
	 */
	public GameServerAddress(final String subnet, final String address) throws UnknownHostException
	{
		super(subnet);
		_serverAddress = address;
	}
	
	/**
	 * Gets the server address.
	 * @return the server address
	 */
	public String getServerAddress()
	{
		return _serverAddress;
	}
	
	@Override
	public String toString()
	{
		return _serverAddress + super.toString();
	}
}
