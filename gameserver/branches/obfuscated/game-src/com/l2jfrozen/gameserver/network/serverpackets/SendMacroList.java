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

import com.l2jfrozen.gameserver.model.L2Macro;

/**
 * packet type id 0xe7 sample e7 d // unknown change of Macro edit,add,delete c // unknown c //count of Macros c // unknown d // id S // macro name S // desc S // acronym c // icon c // count c // entry c // type d // skill id c // shortcut id S // command name format: cdhcdSSScc (ccdcS)
 */
public class SendMacroList extends L2GameServerPacket
{
	private static final String _S__E7_SENDMACROLIST = "[S] E7 SendMacroList";
	
	private final int _rev;
	private final int _count;
	private final L2Macro _macro;
	
	public SendMacroList(final int rev, final int count, final L2Macro macro)
	{
		_rev = rev;
		_count = count;
		_macro = macro;
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0xE7);
		
		D(_rev); // macro change revision (changes after each macro edition)
		C(0); // unknown
		C(_count); // count of Macros
		C(_macro != null ? 1 : 0); // unknown
		
		if (_macro != null)
		{
			D(_macro.id); // Macro ID
			S(_macro.name); // Macro Name
			S(_macro.descr); // Desc
			S(_macro.acronym); // acronym
			C(_macro.icon); // icon
			
			C(_macro.commands.length); // count
			
			for (int i = 0; i < _macro.commands.length; i++)
			{
				final L2Macro.L2MacroCmd cmd = _macro.commands[i];
				C(i + 1); // i of count
				C(cmd.type); // type 1 = skill, 3 = action, 4 = shortcut
				D(cmd.d1); // skill id
				C(cmd.d2); // shortcut id
				S(cmd.cmd); // command name
			}
		}
		
		// D(1); //unknown change of Macro edit,add,delete
		// C(0); //unknown
		// C(1); //count of Macros
		// C(1); //unknown
		//
		// D(1430); //Macro ID
		// S("Admin"); //Macro Name
		// S("Admin Command"); //Desc
		// S("ADM"); //acronym
		// C(0); //icon
		// C(2); //count
		//
		// C(1); //i of count
		// C(3); //type 1 = skill, 3 = action, 4 = shortcut
		// D(0); // skill id
		// C(0); // shortcut id
		// S("/loc"); // command name
		//
		// C(2); //i of count
		// C(3); //type 1 = skill, 3 = action, 4 = shortcut
		// D(0); // skill id
		// C(0); // shortcut id
		// S("//admin"); // command name
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__E7_SENDMACROLIST;
	}
	
}
