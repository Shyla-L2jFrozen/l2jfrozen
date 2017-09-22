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
package com.l2jfrozen.gameserver;

public enum ShutdownModeType1
{
	SIGTERM("Terminating"),
	SHUTDOWN("Shutting down"),
	RESTART("Restarting"),
	ABORT("Aborting"),
	TASK_SHUT("Shuting down"),
	TASK_RES("Restarting"),
	TELL_SHUT("Shuting down"),
	TELL_RES("Restarting");
	
	private final String _modeText;
	
	ShutdownModeType1(final String modeText)
	{
		_modeText = modeText;
	}
	
	public String getText()
	{
		return _modeText;
	}
}