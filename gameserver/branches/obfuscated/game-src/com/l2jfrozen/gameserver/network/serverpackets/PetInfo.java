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

import com.l2jfrozen.gameserver.model.L2Summon;
import com.l2jfrozen.gameserver.model.actor.instance.L2PetInstance;
import com.l2jfrozen.gameserver.model.actor.instance.L2SummonInstance;

/**
 * This class ...
 * @version $Revision: 1.6.2.5.2.12 $ $Date: 2005/03/31 09:19:16 $
 */
public class PetInfo extends L2GameServerPacket
{
	// private static Logger LOGGER = Logger.getLogger(PetInfo.class);
	
	private static final String _S__CA_PETINFO = "[S] b1 PetInfo";
	private final L2Summon _summon;
	private final int _x, _y, _z, _heading;
	private final boolean _isSummoned;
	private final int _mAtkSpd, _pAtkSpd;
	private final int _runSpd, _walkSpd, _swimRunSpd, _swimWalkSpd;
	private int _flRunSpd;
	private int _flWalkSpd;
	private int _flyRunSpd;
	private int _flyWalkSpd;
	private final int _maxHp, _maxMp;
	private int _maxFed, _curFed;
	
	/**
	 * rev 478 dddddddddddddddddddffffdddcccccSSdddddddddddddddddddddddddddhc
	 * @param summon
	 */
	public PetInfo(final L2Summon summon)
	{
		_summon = summon;
		_isSummoned = _summon.isShowSummonAnimation();
		_x = _summon.getX();
		_y = _summon.getY();
		_z = _summon.getZ();
		_heading = _summon.getHeading();
		_mAtkSpd = _summon.getMAtkSpd();
		_pAtkSpd = _summon.getPAtkSpd();
		_runSpd = _summon.getRunSpeed();
		_walkSpd = _summon.getWalkSpeed();
		_swimRunSpd = _flRunSpd = _flyRunSpd = _runSpd;
		_swimWalkSpd = _flWalkSpd = _flyWalkSpd = _walkSpd;
		_maxHp = _summon.getMaxHp();
		_maxMp = _summon.getMaxMp();
		if (_summon instanceof L2PetInstance)
		{
			final L2PetInstance pet = (L2PetInstance) _summon;
			_curFed = pet.getCurrentFed(); // how fed it is
			_maxFed = pet.getMaxFed(); // max fed it can be
		}
		else if (_summon instanceof L2SummonInstance)
		{
			final L2SummonInstance sum = (L2SummonInstance) _summon;
			_curFed = sum.getTimeRemaining();
			_maxFed = sum.getTotalLifeTime();
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0xb1);
		D(_summon.getSummonType());
		D(_summon.getObjectId());
		D(_summon.getTemplate().idTemplate + 1000000);
		D(0); // 1=attackable
		
		D(_x);
		D(_y);
		D(_z);
		D(_heading);
		D(0);
		D(_mAtkSpd);
		D(_pAtkSpd);
		D(_runSpd);
		D(_walkSpd);
		D(_swimRunSpd);
		D(_swimWalkSpd);
		D(_flRunSpd);
		D(_flWalkSpd);
		D(_flyRunSpd);
		D(_flyWalkSpd);
		
		F(1/* _cha.getProperMultiplier() */);
		F(1/* _cha.getAttackSpeedMultiplier() */);
		F(_summon.getTemplate().collisionRadius);
		F(_summon.getTemplate().collisionHeight);
		D(0); // right hand weapon
		D(0);
		D(0); // left hand weapon
		C(1); // name above char 1=true ... ??
		C(_summon.isRunning() ? 1 : 0); // running=1
		C(_summon.isInCombat() ? 1 : 0); // attacking 1=true
		C(_summon.isAlikeDead() ? 1 : 0); // dead 1=true
		C(_isSummoned ? 2 : 0); // invisible ?? 0=false 1=true 2=summoned (only works if model has a summon animation)
		S(_summon.getName());
		S(_summon.getTitle());
		D(1);
		D(_summon.getOwner() != null ? _summon.getOwner().getPvpFlag() : 0); // 0 = white,2= purpleblink, if its greater then karma = purple
		D(_summon.getOwner() != null ? _summon.getOwner().getKarma() : 0); // karma
		D(_curFed); // how fed it is
		D(_maxFed); // max fed it can be
		D((int) _summon.getCurrentHp());// current hp
		D(_maxHp);// max hp
		D((int) _summon.getCurrentMp());// current mp
		D(_maxMp);// max mp
		D(_summon.getStat().getSp()); // sp
		D(_summon.getLevel());// lvl
		Q(_summon.getStat().getExp());
		Q(_summon.getExpForThisLevel());// 0% absolute value
		Q(_summon.getExpForNextLevel());// 100% absoulte value
		D(_summon instanceof L2PetInstance ? _summon.getInventory().getTotalWeight() : 0);// weight
		D(_summon.getMaxLoad());// max weight it can carry
		D(_summon.getPAtk(null));// patk
		D(_summon.getPDef(null));// pdef
		D(_summon.getMAtk(null, null));// matk
		D(_summon.getMDef(null, null));// mdef
		D(_summon.getAccuracy());// accuracy
		D(_summon.getEvasionRate(null));// evasion
		D(_summon.getCriticalHit(null, null));// critical
		D(_runSpd);// speed
		D(_summon.getPAtkSpd());// atkspeed
		D(_summon.getMAtkSpd());// casting speed
		
		D(0);// c2 abnormal visual effect... bleed=1; poison=2; poison & bleed=3; flame=4;
		final int npcId = _summon.getTemplate().npcId;
		
		if (npcId >= 12526 && npcId <= 12528)
		{
			H(1);// c2 ride button
		}
		else
		{
			H(0);
		}
		
		C(0); // c2
		
		// Following all added in C4.
		H(0); // ??
		C(0); // team aura (1 = blue, 2 = red)
		D(_summon.getSoulShotsPerHit()); // How many soulshots this servitor uses per hit
		D(_summon.getSpiritShotsPerHit()); // How many spiritshots this servitor uses per hit
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__CA_PETINFO;
	}
	
}
