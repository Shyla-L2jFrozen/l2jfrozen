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
package com.l2jfrozen.gameserver.model.actor.instance;

/**
 * The Enum PunishLevel.
 */
public enum PunishLevel
{
	
	/** The NONE. */
	NONE(0, ""),
	
	/** The CHAT. */
	CHAT(1, "chat banned"),
	
	/** The JAIL. */
	JAIL(2, "jailed"),
	
	/** The CHAR. */
	CHAR(3, "banned"),
	
	/** The ACC. */
	ACC(4, "banned");
	
	/** The pun value. */
	private final int punValue;
	
	/** The pun string. */
	private final String punString;
	
	/**
	 * Instantiates a new punish level.
	 * @param value the value
	 * @param string the string
	 */
	PunishLevel(final int value, final String string)
	{
		punValue = value;
		punString = string;
	}
	
	/**
	 * Value.
	 * @return the int
	 */
	public int value()
	{
		return punValue;
	}
	
	/**
	 * String.
	 * @return the string
	 */
	public String string()
	{
		return punString;
	}
}