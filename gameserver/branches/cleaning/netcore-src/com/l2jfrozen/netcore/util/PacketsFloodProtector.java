/*
 * L2jFrozen Project - www.l2jfrozen.com 
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jfrozen.netcore.util;

import com.l2jfrozen.netcore.MMOClient;
import com.l2jfrozen.netcore.NetcoreConfig;

/**
 * @author Shyla
 */
public class PacketsFloodProtector
{
	
	private static PacketsFloodServer protected_server = null;
	
	public static void setProtectedServer(PacketsFloodServer server){
		
		protected_server = server;
		
	}
	
	/**
	 * Checks whether the request is flood protected or not.
	 * @param opcode
	 * @param opcode2
	 * @param client
	 * @return true if action is allowed, otherwise false
	 */
	public static boolean tryPerformAction(final int opcode, final int opcode2, final MMOClient<?> client)
	{
		if (NetcoreConfig.getInstance().DISABLE_FULL_PACKETS_FLOOD_PROTECTOR
			|| protected_server == null)
			return true;
		
		// filter on opcodes
		if (!protected_server.isOpCodeToBeTested(opcode, opcode2))
			return true;
					
		return protected_server.tryPerformAction(opcode, opcode2, client);
		
	}
	
}