/*
 * $Header: BlockList.java, 21/11/2005 14:53:53 luisantonioa Exp $
 *
 * $Author: luisantonioa $
 * $Date: 21/11/2005 14:53:53 $
 * $Revision: 1 $
 * $Log: BlockList.java,v $
 * Revision 1  21/11/2005 14:53:53  luisantonioa
 * Added copyright notice
 *
 *
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
package com.l2jfrozen.gameserver.model;

public enum TriggerType
{
	// You hit an enemy
	ON_HIT(1),
	// You hit an enemy - was crit
	ON_CRIT(2),
	// You cast a skill
	ON_CAST(4),
	// You cast a skill - it was a physical one
	ON_PHYSICAL(8),
	// You cast a skill - it was a magic one
	ON_MAGIC(16),
	// You cast a skill - it was a magic one - good magic
	ON_MAGIC_GOOD(32),
	// You cast a skill - it was a magic one - offensive magic
	ON_MAGIC_OFFENSIVE(64),
	// You are attacked by enemy
	ON_ATTACKED(128),
	// You are attacked by enemy - by hit
	ON_ATTACKED_HIT(256),
	// You are attacked by enemy - by hit - was crit
	ON_ATTACKED_CRIT(512),
	// A skill was casted on you
	ON_HIT_BY_SKILL(1024),
	// An evil skill was casted on you
	ON_HIT_BY_OFFENSIVE_SKILL(2048),
	// A good skill was casted on you
	ON_HIT_BY_GOOD_MAGIC(4096);
	
	private int _mask;
	
	private TriggerType(final int mask)
	{
		_mask = mask;
	}
	
	public boolean check(final int event)
	{
		return (_mask & event) != 0; // Trigger (sub-)type contains event (sub-)type
	}
}