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
package com.l2jfrozen.gameserver.network.serverpackets;

import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;

public class RecipeShopMsg extends L2GameServerPacket
{
	private static final String _S__DB_RecipeShopMsg = "[S] db RecipeShopMsg";
	
	private int _chaObjectId;
	private String _chaStoreName;
	
	public RecipeShopMsg(final L2PcInstance player)
	{
		if (player.getCreateList() == null || player.getCreateList().getStoreName() == null)
			return;
		_chaObjectId = player.getObjectId();
		_chaStoreName = player.getCreateList().getStoreName();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xdb);
		writeD(_chaObjectId);
		writeS(_chaStoreName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__DB_RecipeShopMsg;
	}
}