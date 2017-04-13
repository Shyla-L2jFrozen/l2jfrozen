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
package com.l2jfrozen.gameserver.ai.special.manager;

public enum Action
{
	/** on spell finished action when npc finish casting skill */
	ON_SPELL_FINISHED(true),
	
	/** a person came within the Npc/Mob's range */
	ON_AGGRO_RANGE_ENTER(true),
	
	/** OnSpawn */
	ON_SPAWN(true),
	
	/** OnSkillUse (MOB_TARGETED_BY_SKILL) */
	ON_SKILL_USE(true),
	
	/** OnKill (MOBKILLED) */
	ON_KILL(true),
	
	/** OnAttack (MOBGOTATTACKED) */
	ON_ATTACK(true);
	
	private final boolean _isRegistred;
	
	Action(final boolean reg)
	{
		_isRegistred = reg;
	}
	
	public boolean isRegistred()
	{
		return _isRegistred;
	}
}