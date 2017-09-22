package com.l2jfrozen.gameserver.network.serverpackets;

import java.util.Collection;
import java.util.Iterator;

import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;

public class SkillCoolTime extends L2GameServerPacket
{
	
	@SuppressWarnings("rawtypes")
	public Collection _reuseTimeStamps;
	
	public SkillCoolTime(final L2PcInstance cha)
	{
		_reuseTimeStamps = cha.getReuseTimeStamps();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected final void writeImpl()
	{
		@SuppressWarnings("cast")
		final L2PcInstance activeChar = g().getActiveChar();
		if (activeChar == null)
			return;
		C(193);
		D(_reuseTimeStamps.size());
		L2PcInstance.TimeStamp ts;
		for (final Iterator i$ = _reuseTimeStamps.iterator(); i$.hasNext(); D((int) ts.getRemaining() / 1000))
		{
			ts = (L2PcInstance.TimeStamp) i$.next();
			D(ts.getSkill().getId());
			D(0);
			D((int) ts.getReuse() / 1000);
		}
		
	}
	
	@Override
	public String getType()
	{
		return "[S] c1 SkillCoolTime";
	}
}