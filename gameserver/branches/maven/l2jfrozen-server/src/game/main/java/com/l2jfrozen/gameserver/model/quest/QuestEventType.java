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
package com.l2jfrozen.gameserver.model.quest;

public enum QuestEventType
{
	/**
	 * control the first dialog shown by NPCs when they are clicked (some quests must override the default npc action)
	 */
	NPC_FIRST_TALK(false),
	
	/** onTalk action from start npcs */
	QUEST_START(true),
	
	/** onTalk action from npcs participating in a quest */
	QUEST_TALK(true),
	
	/**
	 * onAttack action triggered when a mob gets attacked by someone<br>
	 * <font color=red><b>USE: ON_ATTACK</b></font>
	 */
	@Deprecated
	MOBGOTATTACKED(true),
	
	/**
	 * onKill action triggered when a mob gets killed.<br>
	 * <font color=red><b>USE: ON_KILL</b></font>
	 */
	@Deprecated
	MOBKILLED(true),
	
	/** Call a faction for support */
	ON_FACTION_CALL(true),
	
	/**
	 * onSkillUse action triggered when a character uses a skill on a mob<br>
	 * <font color=red><b>USE: ON_SKILL_USE</b></font>
	 */
	@Deprecated
	MOB_TARGETED_BY_SKILL(true),
	
	/** on spell finished action when npc finish casting skill */
	ON_SPELL_FINISHED(true),
	
	/** a person came within the Npc/Mob's range */
	ON_AGGRO_RANGE_ENTER(true),
	
	/** OnSpawn */
	ON_SPAWN(true),
	
	/** OnSkillUse */
	ON_SKILL_USE(true),
	
	/** OnKill (MOBKILLED) */
	ON_KILL(true),
	
	/** OnAttack (MOBGOTATTACKED) */
	ON_ATTACK(true);
	
	// control whether this event type is allowed for the same npc template in multiple quests
	// or if the npc must be registered in at most one quest for the specified event
	private boolean _allowMultipleRegistration;
	
	QuestEventType(final boolean allowMultipleRegistration)
	{
		_allowMultipleRegistration = allowMultipleRegistration;
	}
	
	public boolean isMultipleRegistrationAllowed()
	{
		return _allowMultipleRegistration;
	}
}