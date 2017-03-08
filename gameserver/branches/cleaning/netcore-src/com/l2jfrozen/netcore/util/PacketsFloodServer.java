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
package com.l2jfrozen.netcore.util;

import com.l2jfrozen.netcore.MMOClient;

/**
 * @author Shyla
 *
 */
public interface PacketsFloodServer
{
	public boolean tryPerformAction(final int opcode, final int opcode2, final MMOClient<?> client);
	public boolean isOpCodeToBeTested(final int opcode, final int opcode2);
	
}
