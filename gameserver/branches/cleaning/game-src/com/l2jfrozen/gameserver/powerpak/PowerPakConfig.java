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
package com.l2jfrozen.gameserver.powerpak;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.l2jfrozen.common.CommonConfig;
import com.l2jfrozen.common.L2Properties;
import com.l2jfrozen.gameserver.config.FService;
import com.l2jfrozen.gameserver.datatables.sql.ItemTable;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author Nick
 */
public class PowerPakConfig
{
	private static final Logger LOGGER = Logger.getLogger(PowerPakConfig.class);
	
	public static int BUFFER_NPC;
	public static boolean BUFFER_ENABLED;
	public static List<String> BUFFER_EXCLUDE_ON = new FastList<>();
	public static String BUFFER_COMMAND;
	public static int BUFFER_PRICE;
	public static boolean BUFFER_USEBBS;
	public static boolean BUFFER_USECOMMAND;
	
	public static FastMap<Integer, Integer> FIGHTER_SKILL_LIST;
	public static FastMap<Integer, Integer> MAGE_SKILL_LIST;
	
	public static int NPCBUFFER_MAX_SCHEMES;
	public static int NPCBUFFER_MAX_SKILLS;
	public static boolean NPCBUFFER_STORE_SCHEMES;
	public static int NPCBUFFER_STATIC_BUFF_COST;
	
	public static List<String> GLOBALGK_EXCLUDE_ON;
	public static boolean GLOBALGK_ENABDLED;
	public static boolean GLOBALGK_USEBBS;
	public static int GLOBALGK_NPC;
	public static int GLOBALGK_PRICE;
	public static int GLOBALGK_TIMEOUT;
	public static String GLOBALGK_COMMAND;
	public static boolean GLOBALGK_USECOMMAND;
	
	public static int GMSHOP_NPC;
	public static boolean GMSHOP_ENABLED;
	public static boolean GMSHOP_USEBBS;
	public static String GMSHOP_COMMAND;
	public static List<String> GMSHOP_EXCLUDE_ON;
	public static boolean GMSHOP_USECOMMAND;
	
	public static boolean WEBSERVER_ENABLED;
	public static int WEBSERVER_PORT;
	public static String WEBSERVER_HOST;
	
	public static boolean L2TOPDEMON_ENABLED;
	public static int L2TOPDEMON_POLLINTERVAL;
	public static boolean L2TOPDEMON_IGNOREFIRST;
	public static int L2TOPDEMON_MIN;
	public static int L2TOPDEMON_MAX;
	public static int L2TOPDEMON_ITEM;
	public static String L2TOPDEMON_MESSAGE;
	public static String L2TOPDEMON_URL;
	public static String L2TOPDEMON_PREFIX;
	
	// Vote Reward System Configs
	public static int VOTES_FOR_REWARD;
	
	public static String VOTES_REWARDS;
	public static String VOTES_SITE_TOPZONE_URL;
	public static String VOTES_SITE_HOPZONE_URL;
	public static String VOTES_SITE_L2NETWORK_URL;
	public static FastMap<Integer, Integer> VOTES_REWARDS_LIST;
	
	public static int VOTES_SYSYEM_INITIAL_DELAY;
	public static int VOTES_SYSYEM_STEP_DELAY;
	
	// vote commands
	public static boolean ENABLE_VOTE_COMMAND;
	
	public static String SERVER_WEB_SITE;
	public static boolean AUTOVOTEREWARD_ENABLED;
	
	public static boolean ENABLE_SAY_SOCIAL_ACTIONS;
	
	public static boolean CHAR_REPAIR;
	public static boolean VOTEMANAGER_LOG;
	
	public static void load()
	{
		try
		{
			final L2Properties p = new L2Properties(FService.POWERPACK_CONFIG_FILE);
			
			BUFFER_ENABLED = Boolean.parseBoolean(p.getProperty("BufferEnabled", "false"));
			StringTokenizer st = new StringTokenizer(p.getProperty("BufferExcludeOn", ""), " ");
			while (st.hasMoreTokens())
			{
				BUFFER_EXCLUDE_ON.add(st.nextToken());
			}
			BUFFER_COMMAND = p.getProperty("BufferCommand", "buffme");
			BUFFER_NPC = Integer.parseInt(p.getProperty("BufferNpcId", "50019"));
			BUFFER_PRICE = Integer.parseInt(p.getProperty("BufferPrice", "-1"));
			BUFFER_USEBBS = Boolean.parseBoolean(p.getProperty("BufferUseBBS", "false"));
			BUFFER_USECOMMAND = Boolean.parseBoolean(p.getProperty("BufferUseCommand", "false"));
			
			FIGHTER_SKILL_LIST = new FastMap<>();
			MAGE_SKILL_LIST = new FastMap<>();
			
			String[] fPropertySplit;
			fPropertySplit = p.getProperty("FighterSkillList", "").split(";");
			
			String[] mPropertySplit;
			mPropertySplit = p.getProperty("MageSkillList", "").split(";");
			
			for (final String skill : fPropertySplit)
			{
				final String[] skillSplit = skill.split(",");
				if (skillSplit.length != 2)
				{
					LOGGER.info("[FighterSkillList]: invalid config property -> FighterSkillList \"" + skill + "\"");
				}
				else
				{
					try
					{
						FIGHTER_SKILL_LIST.put(Integer.parseInt(skillSplit[0]), Integer.parseInt(skillSplit[1]));
					}
					catch (final NumberFormatException nfe)
					{
						if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
							nfe.printStackTrace();
						
						if (!skill.equals(""))
						{
							LOGGER.info("[FighterSkillList]: invalid config property -> FighterSkillList \"" + skillSplit[0] + "\"" + skillSplit[1]);
						}
					}
				}
			}
			
			for (final String skill : mPropertySplit)
			{
				final String[] skillSplit = skill.split(",");
				if (skillSplit.length != 2)
				{
					LOGGER.info("[MageSkillList]: invalid config property -> MageSkillList \"" + skill + "\"");
				}
				else
				{
					try
					{
						MAGE_SKILL_LIST.put(Integer.parseInt(skillSplit[0]), Integer.parseInt(skillSplit[1]));
					}
					catch (final NumberFormatException nfe)
					{
						if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
							nfe.printStackTrace();
						
						if (!skill.equals(""))
						{
							LOGGER.info("[MageSkillList]: invalid config property -> MageSkillList \"" + skillSplit[0] + "\"" + skillSplit[1]);
						}
					}
				}
			}
			
			NPCBUFFER_MAX_SCHEMES = Integer.parseInt(p.getProperty("NPCBufferMaxSchemesPerChar", "4"));
			NPCBUFFER_MAX_SKILLS = Integer.parseInt(p.getProperty("NPCBufferMaxSkllsperScheme", "24"));
			NPCBUFFER_STORE_SCHEMES = Boolean.parseBoolean(p.getProperty("NPCBufferStoreSchemes", "True"));
			NPCBUFFER_STATIC_BUFF_COST = Integer.parseInt(p.getProperty("NPCBufferStaticCostPerBuff", "-1"));
			
			GLOBALGK_NPC = Integer.parseInt(p.getProperty("GKNpcId", "7077"));
			GLOBALGK_ENABDLED = Boolean.parseBoolean(p.getProperty("GKEnabled", "false"));
			GLOBALGK_COMMAND = p.getProperty("GKCommand", "teleport");
			GLOBALGK_TIMEOUT = Integer.parseInt(p.getProperty("GKTimeout", "10"));
			if (GLOBALGK_TIMEOUT < 1)
			{
				GLOBALGK_TIMEOUT = 1;
			}
			GLOBALGK_PRICE = Integer.parseInt(p.getProperty("GKPrice", "-1"));
			GLOBALGK_USECOMMAND = Boolean.parseBoolean(p.getProperty("GKUseCommand", "false"));
			GLOBALGK_USEBBS = Boolean.parseBoolean(p.getProperty("GKUseBBS", "true"));
			GLOBALGK_EXCLUDE_ON = new FastList<>();
			st = new StringTokenizer(p.getProperty("GKExcludeOn", ""), " ");
			while (st.hasMoreTokens())
			{
				GLOBALGK_EXCLUDE_ON.add(st.nextToken().toUpperCase());
			}
			
			GMSHOP_NPC = Integer.parseInt(p.getProperty("GMShopNpcId", "53"));
			GMSHOP_ENABLED = Boolean.parseBoolean(p.getProperty("GMShopEnabled", "false"));
			GMSHOP_COMMAND = p.getProperty("GMShopCommand", "gmshop");
			GMSHOP_USEBBS = Boolean.parseBoolean(p.getProperty("GMShopUseBBS", "false"));
			GMSHOP_USECOMMAND = Boolean.parseBoolean(p.getProperty("GMShopUseCommand", "false"));
			GMSHOP_EXCLUDE_ON = new FastList<>();
			st = new StringTokenizer(p.getProperty("GMShopExcludeOn", ""), " ");
			while (st.hasMoreTokens())
			{
				GMSHOP_EXCLUDE_ON.add(st.nextToken().toUpperCase());
			}
			
			L2TOPDEMON_ENABLED = Boolean.parseBoolean(p.getProperty("L2TopDeamonEnabled", "false"));
			L2TOPDEMON_URL = p.getProperty("L2TopDeamonURL", "");
			L2TOPDEMON_POLLINTERVAL = Integer.parseInt(p.getProperty("L2TopDeamonPollInterval", "5"));
			L2TOPDEMON_PREFIX = p.getProperty("L2TopDeamonPrefix", "");
			L2TOPDEMON_ITEM = Integer.parseInt(p.getProperty("L2TopDeamonRewardItem", "0"));
			L2TOPDEMON_MESSAGE = L2Utils.loadMessage(p.getProperty("L2TopDeamonMessage", ""));
			L2TOPDEMON_MIN = Integer.parseInt(p.getProperty("L2TopDeamonMin", "1"));
			L2TOPDEMON_MAX = Integer.parseInt(p.getProperty("L2TopDeamonMax", "1"));
			L2TOPDEMON_IGNOREFIRST = Boolean.parseBoolean(p.getProperty("L2TopDeamonDoNotRewardAtFirstTime", "false"));
			if (ItemTable.getInstance().getTemplate(L2TOPDEMON_ITEM) == null)
			{
				L2TOPDEMON_ENABLED = false;
				LOGGER.error("Powerpak: Unknown item (" + L2TOPDEMON_ITEM + ") as vote reward. Vote disabled");
			}
			
			WEBSERVER_ENABLED = Boolean.parseBoolean(p.getProperty("WebServerEnabled", "true"));
			WEBSERVER_HOST = p.getProperty("WebServerHost", "localhost");
			WEBSERVER_PORT = Integer.parseInt(p.getProperty("WebServerPort", "8080"));
			
			AUTOVOTEREWARD_ENABLED = Boolean.parseBoolean(p.getProperty("VoteRewardSystem", "true"));
			VOTES_FOR_REWARD = Integer.parseInt(p.getProperty("VotesRequiredForReward", "100"));
			VOTES_SYSYEM_INITIAL_DELAY = Integer.parseInt(p.getProperty("VotesSystemInitialDelay", "60000"));
			VOTES_SYSYEM_STEP_DELAY = Integer.parseInt(p.getProperty("VotesSystemStepDelay", "1800000"));
			VOTES_SITE_HOPZONE_URL = p.getProperty("VotesSiteHopZoneUrl", "");
			VOTES_SITE_TOPZONE_URL = p.getProperty("VotesSiteTopZoneUrl", "");
			VOTES_SITE_L2NETWORK_URL = p.getProperty("VotesSiteL2NetworkUrl", "");
			SERVER_WEB_SITE = p.getProperty("ServerWebSite", "");
			VOTES_REWARDS = p.getProperty("VotesRewards", "");
			VOTES_REWARDS_LIST = new FastMap<>();
			ENABLE_VOTE_COMMAND = Boolean.parseBoolean(p.getProperty("EnableVoteCommand", "false"));
			
			final String[] splitted_VOTES_REWARDS = VOTES_REWARDS.split(";");
			
			for (final String reward : splitted_VOTES_REWARDS)
			{
				
				final String[] item_count = reward.split(",");
				
				if (item_count.length != 2)
				{
					LOGGER.info("[VotesRewards]: invalid config property -> VotesRewards \"" + VOTES_REWARDS + "\"");
				}
				else
					VOTES_REWARDS_LIST.put(Integer.parseInt(item_count[0]), Integer.parseInt(item_count[1]));
				
			}
			
			ENABLE_SAY_SOCIAL_ACTIONS = Boolean.parseBoolean(p.getProperty("EnableSocialSayActions", "false"));
			
			CHAR_REPAIR = Boolean.parseBoolean(p.getProperty("CharacterRepair", "False"));
			VOTEMANAGER_LOG = Boolean.parseBoolean(p.getProperty("VoteManagerLog", "False"));
		}
		catch (final Exception e)
		{
			if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
				e.printStackTrace();
			
			LOGGER.error("PowerPak: Unable to read  " + FService.POWERPACK_CONFIG_FILE);
		}
	}
}
