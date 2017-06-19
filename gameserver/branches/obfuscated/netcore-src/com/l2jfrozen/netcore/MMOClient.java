/* L2jFrozen Project - www.l2jfrozen.com 
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
package com.l2jfrozen.netcore;

import java.nio.ByteBuffer;

/**
 * @param <T>
 * @author KenM
 */
public abstract class MMOClient<T extends MMOConnection<?>>
{
	private static long _nextIdentifier = 0;
	
	private final T _con;
	private final long _identifier;
	
	public MMOClient(final T con)
	{
		_con = con;
		_identifier = _nextIdentifier++;
		
		MMOClientsManager.getInstance().addClient(this);
	}
	
	public T getConnection()
	{
		return _con;
	}
	
	public long getIdentifier()
	{
		return _identifier;
	}
	
	public abstract boolean decrypt(final ByteBuffer buf, final int size);
	
	public abstract boolean encrypt(final ByteBuffer buf, final int size);
	
	protected abstract void onDisconnection();
	
	protected abstract void onForcedDisconnection(boolean critical);
}