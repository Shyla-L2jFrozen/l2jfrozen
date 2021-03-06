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
package com.l2jfrozen.gameserver.model.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.l2jfrozen.common.util.CloseUtil;
import com.l2jfrozen.common.util.database.L2DatabaseFactory;
import com.l2jfrozen.gameserver.LoginServerThread;
import com.l2jfrozen.gameserver.config.Config;
import com.l2jfrozen.gameserver.model.ItemContainer;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;
import com.l2jfrozen.gameserver.network.GameClientState;
import com.l2jfrozen.gameserver.network.L2GameClient;
import a.a.B;
import a.a.E;

/**
 * @author FOFAS
 */
public class FakeOnline
{
	private static Logger LOGGER = Logger.getLogger(FakeOnline.class);
	
	// select fake player
	private static final String LOAD_OFFLINE_STATUS = "SELECT * FROM fakeplayer";
	// insert fake player
	private static final String SET_OFFLINE_STATUS = "INSERT INTO fakeplayer (charId) VALUES (?)";
	
	public static void restoreFakePlayers()
	{
		LOGGER.info("Loading Fake player(s)...");
		
		final long LoadStart = System.currentTimeMillis();
		
		int nfakeplayer = 0;
		Connection con = null;
		
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection(false);
			final PreparedStatement stm = con.prepareStatement(LOAD_OFFLINE_STATUS);
			final ResultSet rs = stm.executeQuery();
			
			while (rs.next())
			{
				L2PcInstance player = null;
				try
				{
					if (nfakeplayer != 0 && nfakeplayer % 200 == 0)
						LOGGER.info("Loaded " + nfakeplayer + " fake players.");
					
					final L2GameClient client = new L2GameClient((E) null);
					player = L2PcInstance.restore(rs.getInt("charId"), true);
					client.setActiveChar(player);
					player.setOnlineStatus(true);
					client.setAccountName(player.getAccountName());
					client.setState(GameClientState.IN_GAME);
					client.setDetached(true); // set client detached
					player.setClient(client);
					player.spawnMe(player.getX(), player.getY(), player.getZ());
					LoginServerThread.getInstance().addGameServerLogin(player.getAccountName(), client);
					
					if (player.getClient() != null)
						B.n().b(player.getClient().gi());
					
					final ItemContainer items = player.getInventory();
					items.restore();
					// L2Clan clan = player.getClan();
					// clan.checkCrests();
					player.setfakeplayer(true);
					player.setOnlineStatus(true);
					
					if (Config.FAKEPLAYER_SET_NAME_COLOR)
					{
						player._originalNameColorOffline = player.getAppearance().getNameColor();
						player.getAppearance().setNameColor(Config.FAKEPLAYER_NAME_COLOR);
					}
					
					player.restoreEffects();
					player.broadcastUserInfo();
					nfakeplayer++;
				}
				catch (final Exception e)
				{
					LOGGER.warn("FakePlayer: Error loading trader: " + player, e);
					if (player != null)
					{
						player.deleteMe(false);
					}
				}
			}
			rs.close();
			stm.close();
			
			LOGGER.info("Loaded: " + nfakeplayer + " Fake player(s) in " + (System.currentTimeMillis() - LoadStart) / 1000 + " seconds");
		}
		catch (final Exception e)
		{
			LOGGER.warn("FakePlayer: Error while loading FakePlayer: ", e);
		}
		finally
		{
			CloseUtil.close(con);
		}
	}
	
	public static void setfakeplayers(final L2PcInstance player)
	{
		try (
			Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			final PreparedStatement statement = con.prepareStatement(SET_OFFLINE_STATUS);
			statement.setInt(1, player.getObjectId());
			statement.execute();
			statement.close();
		}
		catch (final Exception e)
		{
		}
	}
}