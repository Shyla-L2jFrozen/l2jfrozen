package com.l2jfrozen.gameserver.handler.admincommandhandlers;

import com.l2jfrozen.gameserver.handler.IAdminCommandHandler;
import com.l2jfrozen.gameserver.model.L2Object;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;
import com.l2jfrozen.gameserver.model.entity.FakeOnline;

/**
 * @author Nefer
 */
public class AdminFakePlayerOnline implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_fakeplayer"
	};
	
	@Override
	public boolean useAdminCommand(final String command, final L2PcInstance activeChar)
	{
		if (command.equalsIgnoreCase("admin_fakeplayer"))
		{
			final L2Object obj = activeChar.getTarget();
			
			if (obj == null)
			{
				activeChar.sendMessage("You have no target to set a fakeplayer!");
				return false;
			}
			
			if (!(obj instanceof L2PcInstance))
			{
				activeChar.sendMessage("You must target a player to set a fakeplayer!");
				return false;
			}
			
			if (((L2PcInstance) obj).isInOfflineMode())
			{
				activeChar.sendMessage("Your target is in offline shop!");
				return false;
			}
			
			if (((L2PcInstance) obj).isFakeOfflinePlayer())
			{
				activeChar.sendMessage("Your target is already a fake player!");
				return false;
			}
			
			if (obj instanceof L2PcInstance)
			{
				activeChar.sendMessage("You have set " + obj + " as a fake player.");
				FakeOnline.setfakeplayers((L2PcInstance) obj);
				((L2PcInstance) obj).setfakeplayer(true);
				((L2PcInstance) obj).deleteMe();
				((L2PcInstance) obj).logout();
			}
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
}