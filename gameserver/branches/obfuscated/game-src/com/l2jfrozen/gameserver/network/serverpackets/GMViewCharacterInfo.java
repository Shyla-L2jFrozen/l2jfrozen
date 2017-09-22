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

import com.l2jfrozen.gameserver.model.Inventory;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;

/**
 * TODO Add support for Eval. Score dddddSdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddffffddddSddd rev420 dddddSdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddffffddddSdddcccddhh rev478
 * dddddSdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddffffddddSdddcccddhhddd rev551
 * @version $Revision: 1.2.2.2.2.8 $ $Date: 2005/03/27 15:29:39 $
 */
public class GMViewCharacterInfo extends L2GameServerPacket
{
	
	/** The Constant _S__8F_GMVIEWCHARINFO. */
	private static final String _S__8F_GMVIEWCHARINFO = "[S] 8F GMViewCharacterInfo";
	
	/** The _active char. */
	private final L2PcInstance _activeChar;
	
	/**
	 * Instantiates a new gM view character info.
	 * @param character the character
	 */
	public GMViewCharacterInfo(final L2PcInstance character)
	{
		_activeChar = character;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.network.serverpackets.L2GameServerPacket#writeImpl()
	 */
	@Override
	protected final void writeImpl()
	{
		final float moveMultiplier = _activeChar.getMovementSpeedMultiplier();
		final int _runSpd = (int) (_activeChar.getRunSpeed() / moveMultiplier);
		final int _walkSpd = (int) (_activeChar.getWalkSpeed() / moveMultiplier);
		
		C(0x8f);
		
		D(_activeChar.getX());
		D(_activeChar.getY());
		D(_activeChar.getZ());
		D(_activeChar.getHeading());
		D(_activeChar.getObjectId());
		S(_activeChar.getName());
		D(_activeChar.getRace().ordinal());
		D(_activeChar.getAppearance().getSex() ? 1 : 0);
		D(_activeChar.getClassId().getId());
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
		D(0x28); // unknown
		
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
		
		// c6 new h's
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
		H(0x00);
		H(0x00);
		H(0x00);
		H(0x00);
		H(0x00);
		H(0x00);
		// end of c6 new h's
		
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
		D(_runSpd); // swimspeed
		D(_walkSpd); // swimspeed
		D(_runSpd);
		D(_walkSpd);
		D(_runSpd);
		D(_walkSpd);
		F(moveMultiplier);
		F(_activeChar.getAttackSpeedMultiplier()); // 2.9);//
		F(_activeChar.getTemplate().collisionRadius); // scale
		F(_activeChar.getTemplate().collisionHeight); // y offset ??!? fem dwarf 4033
		D(_activeChar.getAppearance().getHairStyle());
		D(_activeChar.getAppearance().getHairColor());
		D(_activeChar.getAppearance().getFace());
		D(_activeChar.isGM() ? 0x01 : 0x00); // builder level
		
		S(_activeChar.getTitle());
		D(_activeChar.getClanId()); // pledge id
		D(_activeChar.getClanCrestId()); // pledge crest id
		D(_activeChar.getAllyId()); // ally id
		C(_activeChar.getMountType()); // mount type
		C(_activeChar.getPrivateStoreType());
		C(_activeChar.hasDwarvenCraft() ? 1 : 0);
		D(_activeChar.getPkKills());
		D(_activeChar.getPvpKills());
		
		H(_activeChar.getRecomLeft());
		H(_activeChar.getRecomHave()); // Blue value for name (0 = white, 255 = pure blue)
		D(_activeChar.getClassId().getId());
		D(0x00); // special effects? circles around player...
		D(_activeChar.getMaxCp());
		D((int) _activeChar.getCurrentCp());
		
		C(_activeChar.isRunning() ? 0x01 : 0x00); // changes the Speed display on Status Window
		
		C(321);
		
		D(_activeChar.getPledgeClass()); // changes the text above CP on Status Window
		
		C(_activeChar.isNoble() ? 0x01 : 0x00);
		C(_activeChar.isHero() ? 0x01 : 0x00);
		
		D(_activeChar.getAppearance().getNameColor());
		D(_activeChar.getAppearance().getTitleColor());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__8F_GMVIEWCHARINFO;
	}
}
