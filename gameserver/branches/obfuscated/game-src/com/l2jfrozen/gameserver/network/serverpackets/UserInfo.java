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

import com.l2jfrozen.gameserver.config.Config;
import com.l2jfrozen.gameserver.datatables.sql.NpcTable;
import com.l2jfrozen.gameserver.managers.CursedWeaponsManager;
import com.l2jfrozen.gameserver.model.Inventory;
import com.l2jfrozen.gameserver.model.L2Summon;
import com.l2jfrozen.gameserver.model.actor.instance.L2CubicInstance;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;
import com.l2jfrozen.gameserver.templates.L2NpcTemplate;

/**
 * 0000: 04 03 15 00 00 77 ff 00 00 80 f1 ff ff 00 00 00 .....w.......... 0010: 00 2a 89 00 4c 43 00 61 00 6c 00 61 00 64 00 6f .*..LC.a.l.a.d.o 0020: 00 6e 00 00 00 01 00 00 00 00 00 00 00 19 00 00 .n.............. 0030: 00 0d 00 00 00 ee 81 02 00 15 00 00 00 18 00 00 ................ 0040: 00 19
 * 00 00 00 25 00 00 00 17 00 00 00 28 00 00 .....%.......(.. 0050: 00 14 01 00 00 14 01 00 00 02 01 00 00 02 01 00 ................ 0060: 00 fa 09 00 00 81 06 00 00 26 34 00 00 2e 00 00 .........&4..... 0070: 00 00 00 00 00 db 9f a1 41 93 26 64 41 de c8 31 ........A.&dA..1 0080: 41 ca 73 c0 41 d5
 * 22 d0 41 83 bd 41 41 81 56 10 A.s.A.".A..AA.V. 0090: 41 00 00 00 00 27 7d 30 41 69 aa e0 40 b4 fb d3 A....'}0Ai..@... 00a0: 41 91 f9 63 41 00 00 00 00 81 56 10 41 00 00 00 A..cA.....V.A... 00b0: 00 71 00 00 00 71 00 00 00 76 00 00 00 74 00 00 .q...q...v...t.. 00c0: 00 74 00 00 00 2a 00 00 00 e8
 * 02 00 00 00 00 00 .t...*.......... 00d0: 00 5f 04 00 00 ac 01 00 00 cf 01 00 00 62 04 00 ._...........b.. 00e0: 00 00 00 00 00 e8 02 00 00 0b 00 00 00 52 01 00 .............R.. 00f0: 00 4d 00 00 00 2a 00 00 00 2f 00 00 00 29 00 00 .M...*.../...).. 0100: 00 12 00 00 00 82 01 00 00 52 01 00 00 53
 * 00 00 .........R...S.. 0110: 00 00 00 00 00 00 00 00 00 7a 00 00 00 55 00 00 .........z...U.. 0120: 00 32 00 00 00 32 00 00 00 00 00 00 00 00 00 00 .2...2.......... 0130: 00 00 00 00 00 00 00 00 00 a4 70 3d 0a d7 a3 f0 ..........p=.... 0140: 3f 64 5d dc 46 03 78 f3 3f 00 00 00 00 00 00 1e
 * ?d].F.x.?....... 0150: 40 00 00 00 00 00 00 38 40 02 00 00 00 01 00 00 @......8@....... 0160: 00 00 00 00 00 00 00 00 00 00 00 c1 0c 00 00 01 ................ 0170: 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ................ 0180: 00 00 00 00 ....
 * dddddSdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddffffddddSdddcccdd (h) dddddSddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd ffffddddSdddddcccddh (h) c dc hhdh
 * dddddSdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddffffddddSdddddcccddh (h) c dc hhdh ddddc c dcc cddd d (from 654) but it actually reads dddddSdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddffffddddSdddddcccddh (h) c dc *dddddddd* hhdh ddddc dcc
 * cddd d *...*: here i am not sure at least it looks like it reads that much data (32 bytes), not sure about the format inside because it is not read thanks to the ususal
 * parsingfunctiondddddSddddQddddddddddddddddddddddddddddddddddddddddddddddddhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhddddddddddddddddddddffffddddSdddddcccddh [h] c dc d hhdh ddddc c dcc cddd d c dd d d
 * @version $Revision: 1.14.2.4.2.12 $ $Date: 2005/04/11 10:05:55 $
 */
public class UserInfo extends L2GameServerPacket
{
	private static final String _S__04_USERINFO = "[S] 04 UserInfo";
	private final L2PcInstance _activeChar;
	private final int _runSpd, _walkSpd, _swimRunSpd, _swimWalkSpd;
	private int _flRunSpd;
	private int _flWalkSpd;
	private int _flyRunSpd;
	private int _flyWalkSpd;
	private int _relation;
	private final float _moveMultiplier;
	
	public boolean _critical_test = false;
	
	/**
	 * @param character
	 */
	public UserInfo(final L2PcInstance character)
	{
		_activeChar = character;
		
		_moveMultiplier = _activeChar.getMovementSpeedMultiplier();
		_runSpd = (int) (_activeChar.getRunSpeed() / _moveMultiplier);
		_walkSpd = (int) (_activeChar.getWalkSpeed() / _moveMultiplier);
		_swimRunSpd = _flRunSpd = _flyRunSpd = _runSpd;
		_swimWalkSpd = _flWalkSpd = _flyWalkSpd = _walkSpd;
		_relation = _activeChar.isClanLeader() ? 0x40 : 0;
		
		if (_activeChar.getSiegeState() == 1)
		{
			_relation |= 0x180;
		}
		if (_activeChar.getSiegeState() == 2)
		{
			_relation |= 0x80;
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0x04);
		
		if (!_critical_test)
		{
			
			D(_activeChar.getX());
			D(_activeChar.getY());
			D(_activeChar.getZ());
			D(_activeChar.getHeading());
			
		}
		else
		{ // critical values
			
			D(-999999999);
			D(-999999999);
			D(-999999999);
			D(-999999999);
			D(-999999999); // one more to change the UserInfo packet size
			
		}
		
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
		
		D(_activeChar.getLevel());
		Q(_activeChar.getExp());
		D(_activeChar.getSTR());
		D(_activeChar.getDEX());
		D(_activeChar.getCON());
		D(_activeChar.getINT());
		D(_activeChar.getWIT());
		D(_activeChar.getMEN());
		D(_activeChar.getMaxHp());
		D((int) _activeChar.getCurrentHp());
		D(_activeChar.getMaxMp());
		D((int) _activeChar.getCurrentMp());
		D(_activeChar.getSp());
		D(_activeChar.getCurrentLoad());
		D(_activeChar.getMaxLoad());
		
		D(_activeChar.getActiveWeaponItem() != null ? 40 : 20); // 20 no weapon, 40 weapon equippe
		
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_DHAIR));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_REAR));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEAR));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_NECK));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HEAD));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_CHEST));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LEGS));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FEET));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_BACK));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LRHAND));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_HAIR));
		D(_activeChar.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_FACE));
		
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_DHAIR));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_REAR));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEAR));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_NECK));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RFINGER));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LFINGER));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_BACK));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LRHAND));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
		D(_activeChar.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FACE));
		
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
		H(0x00);
		H(0x00);
		D(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_RHAND));
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
		D(_activeChar.getInventory().getPaperdollAugmentationId(Inventory.PAPERDOLL_LRHAND));
		H(0x00);
		H(0x00);
		H(0x00);
		H(0x00);
		
		D(_activeChar.getPAtk(null));
		D(_activeChar.getPAtkSpd());
		D(_activeChar.getPDef(null));
		D(_activeChar.getEvasionRate(null));
		D(_activeChar.getAccuracy());
		D(_activeChar.getCriticalHit(null, null));
		D(_activeChar.getMAtk(null, null));
		
		D(_activeChar.getMAtkSpd());
		D(_activeChar.getPAtkSpd());
		
		D(_activeChar.getMDef(null, null));
		
		D(_activeChar.getPvpFlag()); // 0-non-pvp 1-pvp = violett name
		D(_activeChar.getKarma());
		
		D(_runSpd);
		D(_walkSpd);
		D(_swimRunSpd); // swimspeed
		D(_swimWalkSpd); // swimspeed
		D(_flRunSpd);
		D(_flWalkSpd);
		D(_flyRunSpd);
		D(_flyWalkSpd);
		F(_moveMultiplier);
		F(_activeChar.getAttackSpeedMultiplier());
		
		final L2Summon pet = _activeChar.getPet();
		if (_activeChar.getMountType() != 0 && pet != null)
		{
			F(pet.getTemplate().collisionRadius);
			F(pet.getTemplate().collisionHeight);
		}
		else
		{
			F(_activeChar.getBaseTemplate().collisionRadius);
			F(_activeChar.getBaseTemplate().collisionHeight);
		}
		
		D(_activeChar.getAppearance().getHairStyle());
		D(_activeChar.getAppearance().getHairColor());
		D(_activeChar.getAppearance().getFace());
		D(_activeChar.isGM() ? 1 : 0); // builder level
		
		String title = _activeChar.getTitle();
		if (_activeChar.getAppearance().getInvisible() && _activeChar.isGM())
		{
			title = "Invisible";
		}
		if (_activeChar.getPoly().isMorphed())
		{
			final L2NpcTemplate polyObj = NpcTable.getInstance().getTemplate(_activeChar.getPoly().getPolyId());
			if (polyObj != null)
			{
				title += " - " + polyObj.name;
			}
		}
		S(title);
		
		D(_activeChar.getClanId());
		D(_activeChar.getClanCrestId());
		D(_activeChar.getAllyId());
		D(_activeChar.getAllyCrestId()); // ally crest id
		// 0x40 leader rights
		// siege flags: attacker - 0x180 sword over name, defender - 0x80 shield, 0xC0 crown (|leader), 0x1C0 flag (|leader)
		D(_relation);
		C(_activeChar.getMountType()); // mount type
		C(_activeChar.getPrivateStoreType());
		C(_activeChar.hasDwarvenCraft() ? 1 : 0);
		D(_activeChar.getPkKills());
		D(_activeChar.getPvpKills());
		
		final Map<Integer, L2CubicInstance> cubics = _activeChar.getCubics();
		H(cubics.size());
		for (final Integer id : cubics.keySet())
		{
			H(id);
		}
		
		C(_activeChar.isInPartyMatchRoom() ? 1 : 0);
		
		D(_activeChar.getAbnormalEffect());
		C(0x00); // unk
		
		D(_activeChar.getClanPrivileges());
		
		H(_activeChar.getRecomLeft()); // c2 recommendations remaining
		H(_activeChar.getRecomHave()); // c2 recommendations received
		D(0x00); // FIXME: MOUNT NPC ID
		H(_activeChar.getInventoryLimit());
		
		D(_activeChar.getClassId().getId());
		D(0x00); // FIXME: special effects? circles around player...
		D(_activeChar.getMaxCp());
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
		C(_activeChar.isNoble() ? 1 : 0); // 0x01: symbol on char menu ctrl+I
		C((_activeChar.isHero() || (_activeChar.isGM() && Config.GM_HERO_AURA) || _activeChar.getIsPVPHero()) ? 1 : 0); // 0x01: Hero Aura
		
		C(_activeChar.isFishing() ? 1 : 0); // Fishing Mode
		D(_activeChar.GetFishx()); // fishing x
		D(_activeChar.GetFishy()); // fishing y
		D(_activeChar.GetFishz()); // fishing z
		D(_activeChar.getAppearance().getNameColor());
		
		C(_activeChar.isRunning() ? 0x01 : 0x00); // changes the Speed display on Status Window
		
		D(_activeChar.getPledgeClass()); // changes the text above CP on Status Window
		D(_activeChar.getPledgeType()); // TODO: PLEDGE TYPE
		
		D(_activeChar.getAppearance().getTitleColor());
		
		if (_activeChar.isCursedWeaponEquiped())
		{
			D(CursedWeaponsManager.getInstance().getLevel(_activeChar.getCursedWeaponEquipedId()));
		}
		else
		{
			D(0x00);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__04_USERINFO;
	}
}
