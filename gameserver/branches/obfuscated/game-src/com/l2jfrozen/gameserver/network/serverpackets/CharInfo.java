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

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.l2jfrozen.gameserver.config.Config;
import com.l2jfrozen.gameserver.datatables.sql.NpcTable;
import com.l2jfrozen.gameserver.managers.CursedWeaponsManager;
import com.l2jfrozen.gameserver.model.Inventory;
import com.l2jfrozen.gameserver.model.L2Character;
import com.l2jfrozen.gameserver.model.actor.instance.L2CubicInstance;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;
import com.l2jfrozen.gameserver.templates.L2NpcTemplate;

/**
 * 0000: 03 32 15 00 00 44 fe 00 00 80 f1 ff ff 00 00 00 .2...D..........
 * <p>
 * 0010: 00 6b b4 c0 4a 45 00 6c 00 6c 00 61 00 6d 00 69 .k..JE.l.l.a.m.i
 * <p>
 * 0020: 00 00 00 01 00 00 00 01 00 00 00 12 00 00 00 00 ................
 * <p>
 * 0030: 00 00 00 2a 00 00 00 42 00 00 00 71 02 00 00 31 ...*...B...q...1
 * <p>
 * 0040: 00 00 00 18 00 00 00 1f 00 00 00 25 00 00 00 00 ...........%....
 * <p>
 * 0050: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 f9 ................
 * <p>
 * 0060: 00 00 00 b3 01 00 00 00 00 00 00 00 00 00 00 7d ...............}
 * <p>
 * 0070: 00 00 00 5a 00 00 00 32 00 00 00 32 00 00 00 00 ...Z...2...2....
 * <p>
 * 0080: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 67 ...............g
 * <p>
 * 0090: 66 66 66 66 66 f2 3f 5f 63 97 a8 de 1a f9 3f 00 fffff.?_c.....?.
 * <p>
 * 00a0: 00 00 00 00 00 1e 40 00 00 00 00 00 00 37 40 01 .............7..
 * <p>
 * 00b0: 00 00 00 01 00 00 00 01 00 00 00 00 00 c1 0c 00 ................
 * <p>
 * 00c0: 00 00 00 00 00 00 00 00 00 01 01 00 00 00 00 00 ................
 * <p>
 * 00d0: 00 00
 * <p>
 * <p>
 * dddddSdddddddddddddddddddddddddddffffdddSdddccccccc (h)
 * <p>
 * dddddSdddddddddddddddddddddddddddffffdddSdddddccccccch dddddSddddddddddddddddddddddddddddffffdddSdddddccccccch (h) c (dchd) ddc dcc c cddd d dddddSdddddddddddddddhhhhhhhhhhhhhhhhhhhhhhhhddddddddddddddffffdddSdddddccccccch [h] c (ddhd) ddc c ddc cddd d d dd d d d
 * @version $Revision: 1.7.2.6.2.11 $ $Date: 2005/04/11 10:05:54 $
 */
public class CharInfo extends L2GameServerPacket
{
	private static final Logger LOGGER = Logger.getLogger(CharInfo.class);
	
	private static final String _S__03_CHARINFO = "[S] 03 CharInfo";
	private final L2PcInstance _activeChar;
	private final Inventory _inv;
	private final int _x, _y, _z, _heading;
	private final int _mAtkSpd, _pAtkSpd;
	private final int _runSpd, _walkSpd, _swimRunSpd, _swimWalkSpd;
	
	private int _flRunSpd;
	
	private int _flWalkSpd;
	
	private int _flyRunSpd;
	
	private int _flyWalkSpd;
	private final float _moveMultiplier, _attackSpeedMultiplier;
	private final int _maxCp;
	
	/**
	 * @param cha
	 */
	public CharInfo(final L2PcInstance cha)
	{
		_activeChar = cha;
		_inv = cha.getInventory();
		_x = _activeChar.getX();
		_y = _activeChar.getY();
		_z = _activeChar.getZ();
		_heading = _activeChar.getHeading();
		_mAtkSpd = _activeChar.getMAtkSpd();
		_pAtkSpd = _activeChar.getPAtkSpd();
		_moveMultiplier = _activeChar.getMovementSpeedMultiplier();
		_attackSpeedMultiplier = _activeChar.getAttackSpeedMultiplier();
		_runSpd = (int) (_activeChar.getRunSpeed() / _moveMultiplier);
		_walkSpd = (int) (_activeChar.getWalkSpeed() / _moveMultiplier);
		_swimRunSpd = _flRunSpd = _flyRunSpd = _runSpd;
		_swimWalkSpd = _flWalkSpd = _flyWalkSpd = _walkSpd;
		_maxCp = _activeChar.getMaxCp();
	}
	
	@Override
	protected final void writeImpl()
	{
		boolean receiver_is_gm = false;
		
		final L2PcInstance tmp = g().getActiveChar();
		if (tmp != null && tmp.isGM())
		{
			receiver_is_gm = true;
		}
		
		if (!receiver_is_gm && _activeChar.getAppearance().getInvisible())
			return;
		
		if (_activeChar.getPoly().isMorphed())
		{
			final L2NpcTemplate template = NpcTable.getInstance().getTemplate(_activeChar.getPoly().getPolyId());
			
			if (template != null)
			{
				C(0x16);
				D(_activeChar.getObjectId());
				D(_activeChar.getPoly().getPolyId() + 1000000); // npctype id
				D(_activeChar.getKarma() > 0 ? 1 : 0);
				D(_x);
				D(_y);
				D(_z);
				D(_heading);
				D(0x00);
				D(_mAtkSpd);
				D(_pAtkSpd);
				D(_runSpd);
				D(_walkSpd);
				D(_swimRunSpd/* 0x32 */); // swimspeed
				D(_swimWalkSpd/* 0x32 */); // swimspeed
				D(_flRunSpd);
				D(_flWalkSpd);
				D(_flyRunSpd);
				D(_flyWalkSpd);
				F(_moveMultiplier);
				F(_attackSpeedMultiplier);
				F(template.collisionRadius);
				F(template.collisionHeight);
				D(_inv.getPaperdollItemId(Inventory.PAPERDOLL_RHAND)); // right hand weapon
				D(0);
				D(_inv.getPaperdollItemId(Inventory.PAPERDOLL_LHAND)); // left hand weapon
				C(1); // name above char 1=true ... ??
				C(_activeChar.isRunning() ? 1 : 0);
				C(_activeChar.isInCombat() ? 1 : 0);
				C(_activeChar.isAlikeDead() ? 1 : 0);
				
				// if(gmSeeInvis)
				// {
				C(0); // if the charinfo is written means receiver can see the char
				// }
				// else
				// {
				// C(_activeChar.getAppearance().getInvisible() ? 1 : 0); // invisible ?? 0=false 1=true 2=summoned (only works if model has a summon animation)
				// }
				
				S(_activeChar.getName());
				
				if (_activeChar.getAppearance().getInvisible())
				// if(gmSeeInvis)
				{
					S("Invisible");
				}
				else
				{
					S(_activeChar.getTitle());
				}
				
				D(0);
				D(0);
				D(0000); // hmm karma ??
				
				if (_activeChar.getAppearance().getInvisible())
				// if(gmSeeInvis)
				{
					D((_activeChar.getAbnormalEffect() | L2Character.ABNORMAL_EFFECT_STEALTH));
				}
				else
				{
					D(_activeChar.getAbnormalEffect()); // C2
				}
				
				D(0); // C2
				D(0); // C2
				D(0); // C2
				D(0); // C2
				C(0); // C2
			}
			else
			{
				LOGGER.warn("Character " + _activeChar.getName() + " (" + _activeChar.getObjectId() + ") morphed in a Npc (" + _activeChar.getPoly().getPolyId() + ") w/o template.");
			}
		}
		else
		{
			C(0x03);
			D(_x);
			D(_y);
			D(_z);
			D(_heading);
			D(_activeChar.getObjectId());
			S(_activeChar.getName());
			D(_activeChar.getRace().ordinal());
			D(_activeChar.getAppearance().getSex() ? 1 : 0);
			
			if (_activeChar.getClassIndex() == 0)
			{
				D(_activeChar.getClassId().getId());
			}
			else
			{
				D(_activeChar.getBaseClass());
			}
			
			D(_inv.getPaperdollItemId(Inventory.PAPERDOLL_DHAIR));
			D(_inv.getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
			D(_inv.getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
			D(_inv.getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
			D(_inv.getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
			D(_inv.getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
			D(_inv.getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
			D(_inv.getPaperdollItemId(Inventory.PAPERDOLL_FEET));
			D(_inv.getPaperdollItemId(Inventory.PAPERDOLL_BACK));
			D(_inv.getPaperdollItemId(Inventory.PAPERDOLL_LRHAND));
			D(_inv.getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
			D(_inv.getPaperdollItemId(Inventory.PAPERDOLL_FACE));
			
			// c6 new h's
			H(0x00);
			H(0x00);
			H(0x00);
			H(0x00);
			D(_inv.getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
			H(0x00);
			H(0x00);
			H(0x00);
			H(0x00);
			H(0x00);
			H(0x00);
			H(0x00);
			H(0x00);
			H(0x00);
			H(0x00);
			H(0x00);
			H(0x00);
			D(_inv.getPaperdollAugmentationId(Inventory.PAPERDOLL_LRHAND));
			H(0x00);
			H(0x00);
			H(0x00);
			H(0x00);
			
			D(_activeChar.getPvpFlag());
			D(_activeChar.getKarma());
			
			D(_mAtkSpd);
			D(_pAtkSpd);
			
			D(_activeChar.getPvpFlag());
			D(_activeChar.getKarma());
			
			D(_runSpd);
			D(_walkSpd);
			D(_swimRunSpd/* 0x32 */); // swimspeed
			D(_swimWalkSpd/* 0x32 */); // swimspeed
			D(_flRunSpd);
			D(_flWalkSpd);
			D(_flyRunSpd);
			D(_flyWalkSpd);
			F(_activeChar.getMovementSpeedMultiplier()); // _activeChar.getProperMultiplier()
			F(_activeChar.getAttackSpeedMultiplier()); // _activeChar.getAttackSpeedMultiplier()
			F(_activeChar.getBaseTemplate().collisionRadius);
			F(_activeChar.getBaseTemplate().collisionHeight);
			
			D(_activeChar.getAppearance().getHairStyle());
			D(_activeChar.getAppearance().getHairColor());
			D(_activeChar.getAppearance().getFace());
			
			if (_activeChar.getAppearance().getInvisible())
			// if(gmSeeInvis)
			{
				S("Invisible");
			}
			else
			{
				S(_activeChar.getTitle());
			}
			
			D(_activeChar.getClanId());
			D(_activeChar.getClanCrestId());
			D(_activeChar.getAllyId());
			D(_activeChar.getAllyCrestId());
			// In UserInfo leader rights and siege flags, but here found nothing??
			// Therefore RelationChanged packet with that info is required
			D(0);
			
			C(_activeChar.isSitting() ? 0 : 1); // standing = 1 sitting = 0
			C(_activeChar.isRunning() ? 1 : 0); // running = 1 walking = 0
			C(_activeChar.isInCombat() ? 1 : 0);
			C(_activeChar.isAlikeDead() ? 1 : 0);
			
			// if(gmSeeInvis)
			// {
			C(0); // if the charinfo is written means receiver can see the char
			// }
			// else
			// {
			// C(_activeChar.getAppearance().getInvisible() ? 1 : 0); // invisible = 1 visible =0
			// }
			
			C(_activeChar.getMountType()); // 1 on strider 2 on wyvern 0 no mount
			C(_activeChar.getPrivateStoreType()); // 1 - sellshop
			
			final Map<Integer, L2CubicInstance> cubics = _activeChar.getCubics();
			
			final Set<Integer> cubicsIds = cubics.keySet();
			
			H(cubicsIds.size());
			for (final Integer id : cubicsIds)
			{
				if (id != null)
					H(id);
			}
			
			C(_activeChar.isInPartyMatchRoom() ? 1 : 0);
			// C(0x00); // find party members
			
			if (_activeChar.getAppearance().getInvisible())
			// if(gmSeeInvis)
			{
				D((_activeChar.getAbnormalEffect() | L2Character.ABNORMAL_EFFECT_STEALTH));
			}
			else
			{
				D(_activeChar.getAbnormalEffect());
			}
			
			C(_activeChar.getRecomLeft()); // Changed by Thorgrim
			H(_activeChar.getRecomHave()); // Blue value for name (0 = white, 255 = pure blue)
			D(_activeChar.getClassId().getId());
			
			D(_maxCp);
			D((int) _activeChar.getCurrentCp());
			C(_activeChar.isMounted() ? 0 : _activeChar.getEnchantEffect());
			
			if (_activeChar.getTeam() == 1)
			{
				C(0x01); // team circle around feet 1= Blue, 2 = red
			}
			else if (_activeChar.getTeam() == 2)
			{
				C(0x02); // team circle around feet 1= Blue, 2 = red
			}
			else
			{
				C(0x00); // team circle around feet 1= Blue, 2 = red
			}
			
			D(_activeChar.getClanCrestLargeId());
			C(_activeChar.isNoble() ? 1 : 0); // Symbol on char menu ctrl+I
			C((_activeChar.isHero() || (_activeChar.isGM() && Config.GM_HERO_AURA) || _activeChar.getIsPVPHero()) ? 1 : 0); // Hero Aura
			
			C(_activeChar.isFishing() ? 1 : 0); // 0x01: Fishing Mode (Cant be undone by setting back to 0)
			D(_activeChar.GetFishx());
			D(_activeChar.GetFishy());
			D(_activeChar.GetFishz());
			
			D(_activeChar.getAppearance().getNameColor());
			
			D(0x00); // isRunning() as in UserInfo?
			
			D(_activeChar.getPledgeClass());
			D(0x00); // ??
			
			D(_activeChar.getAppearance().getTitleColor());
			
			// D(0x00); // ??
			
			if (_activeChar.isCursedWeaponEquiped())
			{
				D(CursedWeaponsManager.getInstance().getLevel(_activeChar.getCursedWeaponEquipedId()));
			}
			else
			{
				D(0x00);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__03_CHARINFO;
	}
}
