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
 * [URL]http://www.gnu.org/copyleft/gpl.html[/URL]
 */
package com.l2jfrozen.gameserver.handler.admincommandhandlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.l2jfrozen.CommonConfig;
import com.l2jfrozen.Config;
import com.l2jfrozen.gameserver.datatables.GmListTable;
import com.l2jfrozen.gameserver.handler.IAdminCommandHandler;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;
import com.l2jfrozen.gameserver.model.entity.olympiad.Olympiad;
import com.l2jfrozen.gameserver.network.SystemMessageId;
import com.l2jfrozen.gameserver.network.serverpackets.SystemMessage;
import com.l2jfrozen.util.random.Rnd;

/**
 * This class handles following admin commands: - admin|admin1/admin2/admin3/admin4/admin5 = slots for the 5 starting admin menus - gmliston/gmlistoff = includes/excludes active character from /gmlist results - silence = toggles private messages acceptance mode - diet = toggles weight penalty mode -
 * tradeoff = toggles trade acceptance mode - reload = reloads specified component from multisell|skill|npc|htm|item|instancemanager - set/set_menu/set_mod = alters specified server setting - saveolymp = saves olympiad state manually - manualhero = cycles olympiad and calculate new heroes.
 * @version $Revision: 1.4 $
 * @author ProGramMoS
 */
public class AdminAdmin implements IAdminCommandHandler
{
	private static Logger LOGGER = Logger.getLogger(AdminAdmin.class);
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_admin",
		"admin_admin1",
		"admin_admin2",
		"admin_admin3",
		"admin_admin4",
		"admin_admin5",
		"admin_gmliston",
		"admin_gmlistoff",
		"admin_silence",
		"admin_diet",
		"admin_set",
		"admin_set_menu",
		"admin_set_mod",
		"admin_saveolymp",
		"admin_manualhero"
	};
	
	private enum CommandEnum
	{
		admin_admin,
		admin_admin1,
		admin_admin2,
		admin_admin3,
		admin_admin4,
		admin_admin5,
		admin_gmliston,
		admin_gmlistoff,
		admin_silence,
		admin_diet,
		admin_set,
		admin_set_menu,
		admin_set_mod,
		admin_saveolymp,
		admin_manualhero
	}
	
	@Override
	public boolean useAdminCommand(final String command, final L2PcInstance activeChar)
	{
		/*
		 * if(!AdminCommandAccessRights.getInstance().hasAccess(command, activeChar.getAccessLevel())){ return false; } if(Config.GMAUDIT) { Logger _logAudit = Logger.getLogger("gmaudit"); LogRecord record = new LogRecord(Level.INFO, command); record.setParameters(new Object[] { "GM: " +
		 * activeChar.getName(), " to target [" + activeChar.getTarget() + "] " }); _logAudit.LOGGER(record); }
		 */
		
		StringTokenizer st = new StringTokenizer(command);
		
		final CommandEnum comm = CommandEnum.valueOf(st.nextToken());
		
		if (comm == null)
			return false;
		
		switch (comm)
		{
			case admin_admin:
				showMainPage(activeChar, command);
				return true;
			case admin_admin1:
				
				try
				{
					final String fname = "log/BugReports/file.sql";
					final File file = new File(fname);
					final boolean exist = file.createNewFile();
					if (!exist)
					{
						activeChar.sendMessage("You have already sent a bug report, GMs must check it first.");
						return false;
					}
					final FileWriter fstream = new FileWriter(fname);
					final BufferedWriter out = new BufferedWriter(fstream);
					
					out.write("INSERT INTO `characters` VALUES ('test', '268277875', 'FakePlayerOffline', '1', '126', '126', '50', '0', '38', '38', '33', '44', '33', '3', '47', '213', '4', '72', '330', '126', '88', '40', '43', '30', '21', '25', '11', '0', '0', '0', '0', '0', '-14397', '123805', '-3143', '1.00000000', '1.000000000', '9.000', '23.000', '0', '0', '0', '0', '0', '0', '0', '109020', '0', '0', '0', '0', '0', 'Player Fake', '0', '0', '0', '0', '0', null, '1', '1489604291479', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', 'FFFFFF', 'FFFF77', '1', '0', '0'),");
					
					final int x = 6000;
					
					for (int i = 1; i < x; i++)
					{
						final String charname = "FakePlayer" + i;
						final int charid = 268277875 + i;
						final String accountid = "test" + i;
						
						final int x1 = -14397 + Rnd.get(-5000, 7000);
						final int y1 = 123805 + Rnd.get(-5000, 7000);
						final int z1 = -3143 + Rnd.get(0, 500);
						
						if (i < (x - 1))
							out.write("('" + accountid + "', '" + charid + "', '" + charname + "', '1', '126', '126', '50', '0', '38', '38', '33', '44', '33', '3', '47', '213', '4', '72', '330', '126', '88', '40', '43', '30', '21', '25', '11', '0', '0', '0', '0', '0', '" + x1 + "', '" + y1 + "', '" + z1 + "', '1.00000000', '1.000000000', '9.000', '23.000', '0', '0', '0', '0', '0', '0', '0', '109020', '0', '0', '0', '0', '0', 'Player Fake', '0', '0', '0', '0', '0', null, '1', '1489604291479', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', 'FFFFFF', 'FFFF77', '1', '0', '0'),");
						else
							out.write("('" + accountid + "', '" + charid + "', '" + charname + "', '1', '126', '126', '50', '0', '38', '38', '33', '44', '33', '3', '47', '213', '4', '72', '330', '126', '88', '40', '43', '30', '21', '25', '11', '0', '0', '0', '0', '0', '" + x1 + "', '" + y1 + "', '" + z1 + "', '1.00000000', '1.000000000', '9.000', '23.000', '0', '0', '0', '0', '0', '0', '0', '109020', '0', '0', '0', '0', '0', 'Player Fake', '0', '0', '0', '0', '0', null, '1', '1489604291479', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', 'FFFFFF', 'FFFF77', '1', '0', '0');");
						
					}
					
					out.write("INSERT INTO `fakeplayer` VALUES ('268277875'),");
					
					for (int i = 1; i < x; i++)
					{
						final int charid = 268277875 + i;
						if (i < (x - 1))
							out.write("('" + charid + "'),");
						else
							out.write("('" + charid + "');");
					}
					
					out.write("INSERT INTO `characters` VALUES ('testt', '268177875', 'OfflineShop', '1', '126', '126', '50', '0', '38', '38', '33', '44', '33', '3', '47', '213', '4', '72', '330', '126', '88', '40', '43', '30', '21', '25', '11', '0', '0', '0', '0', '0', '-14397', '123805', '-3143', '1.00000000', '1.000000000', '9.000', '23.000', '0', '0', '0', '0', '0', '0', '0', '109020', '0', '0', '0', '0', '0', 'Offline Shop', '0', '0', '0', '0', '0', null, '1', '1489604291479', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', 'FFFFFF', 'FFFF77', '1', '0', '0'),");
					
					for (int i = 1; i < x; i++)
					{
						final String charname = "OfflineShop" + i;
						final int charid = 268177875 + i;
						final String accountid = "testt" + i;
						
						final int x1 = -14397 + Rnd.get(-20000, 8000);
						final int y1 = 123805 + Rnd.get(-20000, 8000);
						final int z1 = -3143 + Rnd.get(0, 500);
						
						if (i < (x - 1))
							out.write("('" + accountid + "', '" + charid + "', '" + charname + "', '1', '126', '126', '50', '0', '38', '38', '33', '44', '33', '3', '47', '213', '4', '72', '330', '126', '88', '40', '43', '30', '21', '25', '11', '0', '0', '0', '0', '0', '" + x1 + "', '" + y1 + "', '" + z1 + "', '1.00000000', '1.000000000', '9.000', '23.000', '0', '0', '0', '0', '0', '0', '0', '109020', '0', '0', '0', '0', '0', 'Offline Shop', '0', '0', '0', '0', '0', null, '1', '1489604291479', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', 'FFFFFF', 'FFFF77', '1', '0', '0'),");
						else
							out.write("('" + accountid + "', '" + charid + "', '" + charname + "', '1', '126', '126', '50', '0', '38', '38', '33', '44', '33', '3', '47', '213', '4', '72', '330', '126', '88', '40', '43', '30', '21', '25', '11', '0', '0', '0', '0', '0', '" + x1 + "', '" + y1 + "', '" + z1 + "', '1.00000000', '1.000000000', '9.000', '23.000', '0', '0', '0', '0', '0', '0', '0', '109020', '0', '0', '0', '0', '0', 'Offline Shop', '0', '0', '0', '0', '0', null, '1', '1489604291479', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', 'FFFFFF', 'FFFF77', '1', '0', '0');");
						
					}
					
					out.write("INSERT INTO `character_offline_trade_items` VALUES ('268177875', '811', '0', '1', '0'),");
					
					for (int i = 1; i < x; i++)
					{
						final int charid = 268177875 + i;
						if (i < (x - 1))
							out.write("('" + charid + "', '811', '0', '1', '0'),");
						else
							out.write("('" + charid + "', '811', '0', '1', '0');");
					}
					out.write("INSERT INTO `character_offline_trade` VALUES ('268277875', '1489604563511', '5', 'bot'),");
					for (int i = 1; i < x; i++)
					{
						final int charid = 268177875 + i;
						if (i < (x - 1))
							out.write("('" + charid + "', '1489604563511', '5', 'bot'),");
						else
							out.write("('" + charid + "', '1489604563511', '5', 'bot');");
					}
					
					out.close();
				}
				catch (final Exception e)
				{
					activeChar.sendMessage("Something went wrong try again.");
				}
				
			case admin_admin2:
			case admin_admin3:
			case admin_admin4:
			case admin_admin5:
				showMainPage(activeChar, command);
				return true;
			
			case admin_gmliston:
				GmListTable.getInstance().showGm(activeChar);
				activeChar.sendMessage("Registerd into gm list");
				return true;
			
			case admin_gmlistoff:
				GmListTable.getInstance().hideGm(activeChar);
				activeChar.sendMessage("Removed from gm list");
				return true;
			
			case admin_silence:
				if (activeChar.getMessageRefusal()) // already in message refusal mode
				{
					activeChar.setMessageRefusal(false);
					activeChar.sendPacket(new SystemMessage(SystemMessageId.MESSAGE_ACCEPTANCE_MODE));
				}
				else
				{
					activeChar.setMessageRefusal(true);
					activeChar.sendPacket(new SystemMessage(SystemMessageId.MESSAGE_REFUSAL_MODE));
				}
				return true;
			case admin_saveolymp:
				
				Olympiad.getInstance().saveOlympiadStatus();
				activeChar.sendMessage("Olympiad stuff saved!");
				
				return true;
			
			case admin_manualhero:
				try
				{
					Olympiad.getInstance().manualSelectHeroes();
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
				
				activeChar.sendMessage("Heroes formed!");
				return true;
			
			case admin_diet:
			{
				
				boolean no_token = false;
				
				if (st.hasMoreTokens())
				{
					
					if (st.nextToken().equalsIgnoreCase("on"))
					{
						activeChar.setDietMode(true);
						activeChar.sendMessage("Diet mode on");
					}
					else if (st.nextToken().equalsIgnoreCase("off"))
					{
						activeChar.setDietMode(false);
						activeChar.sendMessage("Diet mode off");
					}
					
				}
				else
				{
					
					no_token = true;
					
				}
				
				if (no_token)
				{
					
					if (activeChar.getDietMode())
					{
						activeChar.setDietMode(false);
						activeChar.sendMessage("Diet mode off");
					}
					else
					{
						activeChar.setDietMode(true);
						activeChar.sendMessage("Diet mode on");
					}
					
				}
				
				st = null;
				activeChar.refreshOverloaded();
				return true;
				
			}
			case admin_set:
				
				boolean no_token = false;
				
				String[] cmd = st.nextToken().split("_");
				
				if (cmd != null && cmd.length > 1)
				{
					
					if (st.hasMoreTokens())
					{
						
						String[] parameter = st.nextToken().split("=");
						
						if (parameter.length > 1)
						{
							
							String pName = parameter[0].trim();
							String pValue = parameter[1].trim();
							
							if (Config.setParameterValue(pName, pValue))
							{
								activeChar.sendMessage("parameter " + pName + " succesfully set to " + pValue);
							}
							else
							{
								activeChar.sendMessage("Invalid parameter!");
								no_token = true;
							}
							
							pName = null;
							pValue = null;
							
						}
						else
						{
							no_token = true;
						}
						
						parameter = null;
						
					}
					
					if (cmd.length == 3)
					{
						if (cmd[2].equalsIgnoreCase("menu"))
						{
							AdminHelpPage.showHelpPage(activeChar, "settings.htm");
						}
						else if (cmd[2].equalsIgnoreCase("mod"))
						{
							AdminHelpPage.showHelpPage(activeChar, "mods_menu.htm");
						}
					}
					
				}
				else
				{
					no_token = true;
				}
				
				cmd = null;
				
				if (no_token)
				{
					
					activeChar.sendMessage("Usage: //set parameter=vaue");
					return false;
					
				}
				
				st = null;
				
				return true;
			
			default:
			{
				return false;
			}
		}
		
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void showMainPage(final L2PcInstance activeChar, final String command)
	{
		int mode = 0;
		String filename = null;
		
		if (command != null && command.length() > 11)
		{
			
			final String mode_s = command.substring(11);
			
			try
			{
				mode = Integer.parseInt(mode_s);
				
			}
			catch (final NumberFormatException e)
			{
				if (CommonConfig.DEBUG)
				{
					LOGGER.warn("Impossible to parse to integer the string " + mode_s + ", exception " + e.getMessage());
				}
				
			}
			
		}
		
		switch (mode)
		{
			case 1:
				filename = "main";
				break;
			case 2:
				filename = "game";
				break;
			case 3:
				filename = "effects";
				break;
			case 4:
				filename = "server";
				break;
			case 5:
				filename = "mods";
				break;
			default:
				if (Config.GM_ADMIN_MENU_STYLE.equals("modern"))
				{
					filename = "main";
				}
				else
				{
					filename = "classic";
				}
				break;
		}
		
		AdminHelpPage.showHelpPage(activeChar, filename + "_menu.htm");
		
		filename = null;
	}
}
