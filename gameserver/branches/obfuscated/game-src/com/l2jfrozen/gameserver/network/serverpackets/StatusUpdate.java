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

import java.util.Vector;

import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;

/**
 * 01 // Packet Identifier <BR>
 * c6 37 50 40 // ObjectId <BR>
 * <BR>
 * 01 00 // Number of Attribute Trame of the Packet <BR>
 * <BR>
 * c6 37 50 40 // Attribute Identifier : 01-Level, 02-Experience, 03-STR, 04-DEX, 05-CON, 06-INT, 07-WIT, 08-MEN, 09-Current HP, 0a, Max HP...<BR>
 * cd 09 00 00 // Attribute Value <BR>
 * format d d(dd)
 * @version $Revision: 1.3.2.1.2.5 $ $Date: 2005/03/27 15:29:39 $
 */
public class StatusUpdate extends L2GameServerPacket
{
	private static final String _S__1A_STATUSUPDATE = "[S] 0e StatusUpdate";
	public static final int LEVEL = 0x01;
	public static final int EXP = 0x02;
	public static final int STR = 0x03;
	public static final int DEX = 0x04;
	public static final int CON = 0x05;
	public static final int INT = 0x06;
	public static final int WIT = 0x07;
	public static final int MEN = 0x08;
	
	public static final int CUR_HP = 0x09;
	public static final int MAX_HP = 0x0a;
	public static final int CUR_MP = 0x0b;
	public static final int MAX_MP = 0x0c;
	
	public static final int SP = 0x0d;
	public static final int CUR_LOAD = 0x0e;
	public static final int MAX_LOAD = 0x0f;
	
	public static final int P_ATK = 0x11;
	public static final int ATK_SPD = 0x12;
	public static final int P_DEF = 0x13;
	public static final int EVASION = 0x14;
	public static final int ACCURACY = 0x15;
	public static final int CRITICAL = 0x16;
	public static final int M_ATK = 0x17;
	public static final int CAST_SPD = 0x18;
	public static final int M_DEF = 0x19;
	public static final int PVP_FLAG = 0x1a;
	public static final int KARMA = 0x1b;
	
	public static final int CUR_CP = 0x21;
	public static final int MAX_CP = 0x22;
	
	private L2PcInstance _actor;
	
	private Vector<Attribute> _attributes;
	public int _objectId;
	
	class Attribute
	{
		
		// id values 09 - current health 0a - max health 0b - current mana 0c - max mana
		public int id;
		public int value;
		
		Attribute(final int pId, final int pValue)
		{
			id = pId;
			value = pValue;
		}
	}
	
	public StatusUpdate(final L2PcInstance actor)
	{
		_actor = actor;
	}
	
	public StatusUpdate(final int objectId)
	{
		_attributes = new Vector<>();
		_objectId = objectId;
	}
	
	public void addAttribute(final int id, final int level)
	{
		_attributes.add(new Attribute(id, level));
	}
	
	@Override
	protected final void writeImpl()
	{
		C(0x0e);
		
		if (_actor != null)
		{
			D(_actor.getObjectId());
			D(28); // all the attributes
			
			D(LEVEL);
			D(_actor.getLevel());
			D(EXP);
			D((int) _actor.getExp());
			D(STR);
			D(_actor.getSTR());
			D(DEX);
			D(_actor.getDEX());
			D(CON);
			D(_actor.getCON());
			D(INT);
			D(_actor.getINT());
			D(WIT);
			D(_actor.getWIT());
			D(MEN);
			D(_actor.getMEN());
			
			D(CUR_HP);
			D((int) _actor.getCurrentHp());
			D(MAX_HP);
			D(_actor.getMaxHp());
			D(CUR_MP);
			D((int) _actor.getCurrentMp());
			D(MAX_MP);
			D(_actor.getMaxMp());
			D(SP);
			D(_actor.getSp());
			D(CUR_LOAD);
			D(_actor.getCurrentLoad());
			D(MAX_LOAD);
			D(_actor.getMaxLoad());
			
			D(P_ATK);
			D(_actor.getPAtk(null));
			D(ATK_SPD);
			D(_actor.getPAtkSpd());
			D(P_DEF);
			D(_actor.getPDef(null));
			D(EVASION);
			D(_actor.getEvasionRate(null));
			D(ACCURACY);
			D(_actor.getAccuracy());
			D(CRITICAL);
			D(_actor.getCriticalHit(null, null));
			D(M_ATK);
			D(_actor.getMAtk(null, null));
			
			D(CAST_SPD);
			D(_actor.getMAtkSpd());
			D(M_DEF);
			D(_actor.getMDef(null, null));
			D(PVP_FLAG);
			D(_actor.getPvpFlag());
			D(KARMA);
			D(_actor.getKarma());
			D(CUR_CP);
			D((int) _actor.getCurrentCp());
			D(MAX_CP);
			D(_actor.getMaxCp());
			
		}
		else
		{
			
			D(_objectId);
			D(_attributes.size());
			
			for (int i = 0; i < _attributes.size(); i++)
			{
				final Attribute temp = _attributes.get(i);
				
				D(temp.id);
				D(temp.value);
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
		return _S__1A_STATUSUPDATE;
	}
}
