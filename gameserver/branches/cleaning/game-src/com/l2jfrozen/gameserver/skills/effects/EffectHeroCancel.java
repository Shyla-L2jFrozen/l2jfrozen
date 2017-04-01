package com.l2jfrozen.gameserver.skills.effects;

import com.l2jfrozen.gameserver.model.L2Effect;
import com.l2jfrozen.gameserver.model.L2Skill.SkillType;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;
import com.l2jfrozen.gameserver.network.SystemMessageId;
import com.l2jfrozen.gameserver.network.serverpackets.SystemMessage;
import com.l2jfrozen.gameserver.skills.Env;
import com.l2jfrozen.gameserver.skills.Stats;
import com.l2jfrozen.util.random.Rnd;

/**
 * Infinity Spear effect like L2OFF Interlude (lineage2.com 2007 year)
 * @Authors: Souverain and OnePaTuBHuK
 */ 

final class EffectHeroCancel extends L2Effect
{
	public EffectHeroCancel(final Env env, final EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.HERO_CANCEL;
	}
	
	/** Notify started */
	@Override
	public void onStart()
	{
			L2Effect[] effects = getEffected().getAllEffects();
			int maxdisp = (int) getSkill().getNegatePower();

			for (final L2Effect e : effects)
			{
				switch (e.getEffectType())
				{
					case SIGNET_GROUND:
					case SIGNET_EFFECT:
						continue;
				}
				
				if (e.getSkill().getId() != 4082 && e.getSkill().getId() != 4215 && e.getSkill().getId() != 5182 && e.getSkill().getId() != 4515 && e.getSkill().getId() != 110 && e.getSkill().getId() != 111 && e.getSkill().getId() != 1323 && e.getSkill().getId() != 1325)
				{
					if (e.getSkill().getSkillType() == SkillType.BUFF)
					{							
							int rate = 10;
							
							if (Rnd.get(100) < rate)
							{
								e.exit(true);
								maxdisp--;
								if (maxdisp == 0)
								{
									break;
								}
							}
					}
				}
			}
			effects = null;
}
	
	/** Notify exited */
	@Override
	public void onExit()
	{
		// null
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}
