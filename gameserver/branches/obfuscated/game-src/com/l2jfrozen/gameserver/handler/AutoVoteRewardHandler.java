package com.l2jfrozen.gameserver.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.l2jfrozen.common.CommonConfig;
import com.l2jfrozen.common.thread.ThreadPoolManager;
import com.l2jfrozen.gameserver.model.L2World;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;
import com.l2jfrozen.gameserver.model.entity.Announcements;
import com.l2jfrozen.gameserver.powerpak.PowerPakConfig;

/**
 * @author L2jFrozen <br>
 *         <br>
 *         Network working with L2jFrozen AutoVoteReward: <br>
 *         Hopzone -> http://l2.hopzone.net/ <br>
 *         Topzone -> http://l2topzone.com/ <br>
 *         L2network -> http://l2network.eu/ <br>
 */

public class AutoVoteRewardHandler
{
	protected static final Logger LOGGER = Logger.getLogger(AutoVoteRewardHandler.class);
	
	private int _l2networkVotesCount = 0;
	private int _hopzoneVotesCount = 0;
	private int _topzoneVotesCount = 0;
	protected List<String> _already_rewarded;
	
	protected static boolean _l2network = false;
	protected static boolean _topzone = false;
	protected static boolean _hopzone = false;
	
	private AutoVoteRewardHandler()
	{
		LOGGER.info("Vote Reward System Initiated.");
		
		if (_hopzone)
		{
			int hopzone_votes = getHopZoneVotes();
			
			if (hopzone_votes == -1)
			{
				hopzone_votes = 0;
			}
			
			setHopZoneVoteCount(hopzone_votes);
		}
		
		if (_l2network)
		{
			int l2network_votes = getL2NetworkVotes();
			
			if (l2network_votes == -1)
			{
				l2network_votes = 0;
			}
			
			setL2NetworkVoteCount(l2network_votes);
		}
		
		if (_topzone)
		{
			int topzone_votes = getTopZoneVotes();
			
			if (topzone_votes == -1)
			{
				topzone_votes = 0;
			}
			
			setTopZoneVoteCount(topzone_votes);
		}
		
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new AutoReward(), PowerPakConfig.VOTES_SYSYEM_INITIAL_DELAY, PowerPakConfig.VOTES_SYSYEM_STEP_DELAY);
	}
	
	protected class AutoReward implements Runnable
	{
		@Override
		public void run()
		{
			final int minutes = (PowerPakConfig.VOTES_SYSYEM_STEP_DELAY / 1000) / 60;
			
			if (_hopzone)
			{
				final int hopzone_votes = getHopZoneVotes();
				
				if (hopzone_votes != -1)
				{
					if (PowerPakConfig.VOTEMANAGER_LOG)
						LOGGER.info("[Vote] Server HOPZONE Votes: " + hopzone_votes);
					
					Announcements.getInstance().gameAnnounceToAll("[Vote] Actual HOPZONE Votes are " + hopzone_votes + "...");
					
					if (hopzone_votes != 0 && hopzone_votes >= getHopZoneVoteCount() + PowerPakConfig.VOTES_FOR_REWARD)
					{
						_already_rewarded = new ArrayList<>();
						
						final Collection<L2PcInstance> pls = L2World.getInstance().getAllPlayers();
						
						Announcements.getInstance().gameAnnounceToAll("[Vote] Great Work! Check your inventory for Reward!!");
						
						// L2ItemInstance item;
						for (final L2PcInstance player : pls)
						{
							if (player != null && !player.isInOfflineMode() && player.isOnline() == 1 && !player.isFakeOfflinePlayer())
							{
								if (player._active_boxes <= 1 || (player._active_boxes > 1 && checkSingleBox(player)))
								{
									
									final Set<Integer> items = PowerPakConfig.VOTES_REWARDS_LIST.keySet();
									for (final Integer i : items)
									{
										// item = player.getInventory().getItemByItemId(i);
										
										// TODO: check on maxstack for item
										player.addItem("reward", i, PowerPakConfig.VOTES_REWARDS_LIST.get(i), player, true);
										
									}
									
								}
							}
						}
						setHopZoneVoteCount(hopzone_votes);
					}
					Announcements.getInstance().gameAnnounceToAll("[Vote] Next HOPZONE Reward in " + minutes + " minutes at " + (getHopZoneVoteCount() + PowerPakConfig.VOTES_FOR_REWARD) + " Votes!!");
					// site web
					Announcements.getInstance().gameAnnounceToAll("[SiteWeb] " + PowerPakConfig.SERVER_WEB_SITE);
					
				}
				
			}
			
			if (_topzone && _hopzone && PowerPakConfig.VOTES_SYSYEM_STEP_DELAY > 0)
			{
				try
				{
					Thread.sleep(PowerPakConfig.VOTES_SYSYEM_STEP_DELAY / 2);
				}
				catch (final InterruptedException e)
				{
					if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
						e.printStackTrace();
				}
			}
			
			if (_topzone)
			{
				final int topzone_votes = getTopZoneVotes();
				
				if (topzone_votes != -1)
				{
					if (PowerPakConfig.VOTEMANAGER_LOG)
						LOGGER.info("[Vote] Server TOPZONE Votes: " + topzone_votes);
					
					Announcements.getInstance().gameAnnounceToAll("[Vote] Actual TOPZONE Votes are " + topzone_votes + "...");
					
					if (topzone_votes != 0 && topzone_votes >= getTopZoneVoteCount() + PowerPakConfig.VOTES_FOR_REWARD)
					{
						_already_rewarded = new ArrayList<>();
						
						final Collection<L2PcInstance> pls = L2World.getInstance().getAllPlayers();
						
						Announcements.getInstance().gameAnnounceToAll("[Vote] Great Work! Check your inventory for Reward!!");
						
						// L2ItemInstance item;
						for (final L2PcInstance player : pls)
						{
							if (player != null && !player.isInOfflineMode() && player.isOnline() == 1 && !player.isFakeOfflinePlayer())
							{
								if (player._active_boxes <= 1 || (player._active_boxes > 1 && checkSingleBox(player)))
								{
									final Set<Integer> items = PowerPakConfig.VOTES_REWARDS_LIST.keySet();
									for (final Integer i : items)
									{
										// item = player.getInventory().getItemByItemId(i);
										
										// TODO: check on maxstack for item
										player.addItem("reward", i, PowerPakConfig.VOTES_REWARDS_LIST.get(i), player, true);
									}
								}
							}
						}
						setTopZoneVoteCount(topzone_votes);
					}
					
					Announcements.getInstance().gameAnnounceToAll("[Vote] Next TOPZONE Reward in " + minutes + " minutes at " + (getTopZoneVoteCount() + PowerPakConfig.VOTES_FOR_REWARD) + " Votes!!");
					// site web
					Announcements.getInstance().gameAnnounceToAll("[SiteWeb] " + PowerPakConfig.SERVER_WEB_SITE);
				}
			}
			
			if (_topzone && _hopzone && _l2network && PowerPakConfig.VOTES_SYSYEM_STEP_DELAY > 0)
			{
				try
				{
					Thread.sleep(PowerPakConfig.VOTES_SYSYEM_STEP_DELAY / 2);
				}
				catch (final InterruptedException e)
				{
					if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
						e.printStackTrace();
				}
			}
			
			if (_l2network)
			{
				final int l2network_votes = getL2NetworkVotes();
				
				if (l2network_votes != -1)
				{
					if (PowerPakConfig.VOTEMANAGER_LOG)
						LOGGER.info("[Vote] Server L2NETWORK Votes: " + l2network_votes);
					
					Announcements.getInstance().gameAnnounceToAll("[Vote] Actual L2Network Votes are " + l2network_votes + "...");
					
					if (l2network_votes != 0 && l2network_votes >= getL2NetworkVoteCount() + PowerPakConfig.VOTES_FOR_REWARD)
					{
						_already_rewarded = new ArrayList<>();
						
						final Collection<L2PcInstance> pls = L2World.getInstance().getAllPlayers();
						
						Announcements.getInstance().gameAnnounceToAll("[Vote] Great Work! Check your inventory for Reward!!");
						
						// L2ItemInstance item;
						for (final L2PcInstance player : pls)
						{
							if (player != null && !player.isInOfflineMode() && player.isOnline() == 1 && !player.isFakeOfflinePlayer())
							{
								if (player._active_boxes <= 1 || (player._active_boxes > 1 && checkSingleBox(player)))
								{
									final Set<Integer> items = PowerPakConfig.VOTES_REWARDS_LIST.keySet();
									for (final Integer i : items)
									{
										// item = player.getInventory().getItemByItemId(i);
										
										// TODO: check on maxstack for item
										player.addItem("reward", i, PowerPakConfig.VOTES_REWARDS_LIST.get(i), player, true);
									}
								}
							}
						}
						setL2NetworkVoteCount(l2network_votes);
					}
					Announcements.getInstance().gameAnnounceToAll("[Vote] Next L2Network Reward in " + minutes + " minutes at " + (getL2NetworkVoteCount() + PowerPakConfig.VOTES_FOR_REWARD) + " Votes!!");
					// site web
					Announcements.getInstance().gameAnnounceToAll("[SiteWeb] " + PowerPakConfig.SERVER_WEB_SITE);
				}
			}
		}
	}
	
	// Check boxes
	protected boolean checkSingleBox(final L2PcInstance player)
	{
		if (player == null)
			return false;
		
		if (player.getClient() != null && player.getClient().getConnection() != null && !player.getClient().getConnection().i() && player.isOnline() == 1 && !player.isInOfflineMode() && !player.isFakeOfflinePlayer())
		{
			final String playerip = player.getClient().getConnection().c().getHostAddress();
			
			// this must never happend but in case dont give the reward
			if (playerip == null)
				return false;
			
			if (_already_rewarded.contains(playerip))
				return false;
			
			_already_rewarded.add(playerip);
			return true;
		}
		
		// if no connection (maybe offline shop) dnt reward
		return false;
	}
	
	protected static int getHopZoneVotes()
	{
		int votes = -1;
		
		try
		{
			final URL obj = new URL(PowerPakConfig.VOTES_SITE_HOPZONE_URL);
			final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.addRequestProperty("User-Agent", "L2Hopzone");
			con.setConnectTimeout(5000);
			
			final int responseCode = con.getResponseCode();
			if (responseCode == 200)
			{
				try (
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
				{
					String line;
					while ((line = in.readLine()) != null)
					{
						if (line.contains("Total Votes") || line.contains("rank tooltip") || line.contains("no steal make love") || line.contains("no votes here") || line.contains("bang, you don't have votes") || line.contains("la vita e bella") || line.contains("rank anonymous tooltip"))
						{
							final String inputLine = line.split(">")[2].replace("</span", "");
							votes = Integer.parseInt(inputLine);
							break;
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOGGER.warn("[Vote] Server HOPZONE is offline or something is wrong in link");
			Announcements.getInstance().gameAnnounceToAll("[Vote] HOPZONE is offline. We will check reward as it will be online again");
		}
		
		return votes;
	}
	
	protected int getTopZoneVotes()
	{
		int votes = -1;
		URL url = null;
		URLConnection con = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader in = null;
		
		try
		{
			url = new URL(PowerPakConfig.VOTES_SITE_TOPZONE_URL);
			con = url.openConnection();
			con.addRequestProperty("User-Agent", "L2TopZone");
			is = con.getInputStream();
			isr = new InputStreamReader(is);
			in = new BufferedReader(isr);
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				votes = Integer.valueOf(inputLine);
				break;
			}
		}
		catch (final Exception e)
		{
			LOGGER.warn("[Vote] Server TOPZONE is offline or something is wrong in link");
			Announcements.getInstance().gameAnnounceToAll("[Vote] TOPZONE is offline. We will check reward as it will be online again");
		}
		finally
		{
			if (in != null)
				try
				{
					in.close();
				}
				catch (final IOException e1)
				{
					e1.printStackTrace();
				}
			if (isr != null)
				try
				{
					isr.close();
				}
				catch (final IOException e1)
				{
					e1.printStackTrace();
				}
			if (is != null)
				try
				{
					is.close();
				}
				catch (final IOException e1)
				{
					e1.printStackTrace();
				}
		}
		
		return votes;
	}
	
	protected static int getL2NetworkVotes()
	{
		int votes = -1;
		
		try
		{
			final URL obj = new URL(PowerPakConfig.VOTES_SITE_L2NETWORK_URL);
			final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.addRequestProperty("User-Agent", "L2Network");
			con.setConnectTimeout(5000);
			
			final int responseCode = con.getResponseCode();
			if (responseCode == 200)
			{
				try (
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
				{
					String line;
					while ((line = in.readLine()) != null)
					{
						if (line.contains("color:#e7ebf2"))
						{
							// [2] means after the second ">"
							votes = Integer.valueOf(line.split(">")[2].replace("</b", ""));
							break;
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOGGER.warn("[Vote] Server L2NETWORK is offline or something is wrong in link");
			Announcements.getInstance().gameAnnounceToAll("[Vote] L2Network is offline. We will check reward as it will be online again");
		}
		
		return votes;
	}
	
	protected void setHopZoneVoteCount(final int voteCount)
	{
		_hopzoneVotesCount = voteCount;
	}
	
	protected int getHopZoneVoteCount()
	{
		return _hopzoneVotesCount;
	}
	
	protected void setTopZoneVoteCount(final int voteCount)
	{
		_topzoneVotesCount = voteCount;
	}
	
	protected int getTopZoneVoteCount()
	{
		return _topzoneVotesCount;
	}
	
	protected void setL2NetworkVoteCount(final int voteCount)
	{
		_l2networkVotesCount = voteCount;
	}
	
	protected int getL2NetworkVoteCount()
	{
		return _l2networkVotesCount;
	}
	
	public static AutoVoteRewardHandler getInstance()
	{
		Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
		
		if (PowerPakConfig.VOTES_SITE_HOPZONE_URL != null && !PowerPakConfig.VOTES_SITE_HOPZONE_URL.equals(""))
			_hopzone = true;
		
		if (PowerPakConfig.VOTES_SITE_TOPZONE_URL != null && !PowerPakConfig.VOTES_SITE_TOPZONE_URL.equals(""))
			_topzone = true;
		
		if (PowerPakConfig.VOTES_SITE_L2NETWORK_URL != null && !PowerPakConfig.VOTES_SITE_L2NETWORK_URL.equals(""))
			_l2network = true;
		
		if (_topzone || _hopzone || _l2network)
			return SingletonHolder._instance;
		
		return null;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final AutoVoteRewardHandler _instance = new AutoVoteRewardHandler();
	}
}