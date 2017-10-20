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

import java.lang.reflect.Constructor;

import com.l2jfrozen.gameserver.skills.l2skills.L2SkillCharge;
import com.l2jfrozen.gameserver.skills.l2skills.L2SkillChargeDmg;
import com.l2jfrozen.gameserver.skills.l2skills.L2SkillChargeEffect;
import com.l2jfrozen.gameserver.skills.l2skills.L2SkillCreateItem;
import com.l2jfrozen.gameserver.skills.l2skills.L2SkillDefault;
import com.l2jfrozen.gameserver.skills.l2skills.L2SkillDrain;
import com.l2jfrozen.gameserver.skills.l2skills.L2SkillSeed;
import com.l2jfrozen.gameserver.skills.l2skills.L2SkillSignet;
import com.l2jfrozen.gameserver.skills.l2skills.L2SkillSignetCasttime;
import com.l2jfrozen.gameserver.skills.l2skills.L2SkillSummon;
import com.l2jfrozen.gameserver.templates.StatsSet;

public enum SkillType
{
	// Damage
	PDAM,
	MDAM,
	CPDAM,
	MANADAM,
	DOT,
	MDOT,
	DRAIN_SOUL,
	DRAIN(L2SkillDrain.class),
	DEATHLINK,
	FATALCOUNTER,
	BLOW,
	
	// Disablers
	BLEED,
	POISON,
	STUN,
	ROOT,
	CONFUSION,
	FEAR,
	SLEEP,
	CONFUSE_MOB_ONLY,
	MUTE,
	PARALYZE,
	WEAKNESS,
	
	// hp, mp, cp
	HEAL,
	HOT,
	BALANCE_LIFE,
	HEAL_PERCENT,
	HEAL_STATIC,
	COMBATPOINTHEAL,
	COMBATPOINTPERCENTHEAL,
	CPHOT,
	MANAHEAL,
	MANA_BY_LEVEL,
	MANAHEAL_PERCENT,
	MANARECHARGE,
	MPHOT,
	
	// Aggro
	AGGDAMAGE,
	AGGREDUCE,
	AGGREMOVE,
	AGGREDUCE_CHAR,
	AGGDEBUFF,
	
	// Fishing
	FISHING,
	PUMPING,
	REELING,
	
	// MISC
	UNLOCK,
	ENCHANT_ARMOR,
	ENCHANT_WEAPON,
	SOULSHOT,
	SPIRITSHOT,
	SIEGEFLAG,
	TAKECASTLE,
	DELUXE_KEY_UNLOCK,
	SOW,
	HARVEST,
	GET_PLAYER,
	
	// Creation
	COMMON_CRAFT,
	DWARVEN_CRAFT,
	CREATE_ITEM(L2SkillCreateItem.class),
	SUMMON_TREASURE_KEY,
	
	// Summons
	SUMMON(L2SkillSummon.class),
	FEED_PET,
	DEATHLINK_PET,
	STRSIEGEASSAULT,
	ERASE,
	BETRAY,
	
	// Cancel
	CANCEL,
	MAGE_BANE,
	WARRIOR_BANE,
	NEGATE,
	
	BUFF,
	DEBUFF,
	PASSIVE,
	CONT,
	SIGNET(L2SkillSignet.class),
	SIGNET_CASTTIME(L2SkillSignetCasttime.class),
	
	RESURRECT,
	CHARGE(L2SkillCharge.class),
	CHARGE_EFFECT(L2SkillChargeEffect.class),
	CHARGEDAM(L2SkillChargeDmg.class),
	MHOT,
	DETECT_WEAKNESS,
	LUCK,
	RECALL,
	SUMMON_FRIEND,
	REFLECT,
	SPOIL,
	SWEEP,
	FAKE_DEATH,
	UNBLEED,
	UNPOISON,
	UNDEAD_DEFENSE,
	SEED(L2SkillSeed.class),
	BEAST_FEED,
	FORCE_BUFF,
	CLAN_GATE,
	GIVE_SP,
	COREDONE,
	ZAKENPLAYER,
	ZAKENSELF,
	
	// unimplemented
	NOTDONE;
	
	private final Class<? extends L2Skill> _class;
	
	public L2Skill makeSkill(final StatsSet set)
	{
		try
		{
			final Constructor<? extends L2Skill> c = _class.getConstructor(StatsSet.class);
			
			return c.newInstance(set);
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private SkillType()
	{
		_class = L2SkillDefault.class;
	}
	
	private SkillType(final Class<? extends L2Skill> classType)
	{
		_class = classType;
	}
}