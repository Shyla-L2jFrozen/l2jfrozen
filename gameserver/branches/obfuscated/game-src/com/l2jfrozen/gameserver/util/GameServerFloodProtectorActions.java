/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jfrozen.gameserver.util;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import a.a.f;
import a.a.k;
import a.a.x;

import com.l2jfrozen.gameserver.controllers.GameTimeController;
import com.l2jfrozen.gameserver.model.actor.instance.PunishLevel;
import com.l2jfrozen.gameserver.network.L2GameClient;

/**
 * @author Shyla
 */
public class GameServerFloodProtectorActions implements x 
{
	
	private static final Logger LOGGER = Logger.getLogger(GameServerFloodProtectorActions.class);
	
	private static final int MAX_CONCURRENT_ACTIONS_PER_PLAYER = 10;
	
	private static Hashtable<String, AtomicInteger> clients_concurrent_actions = new Hashtable<>();
	
	private static Hashtable<String, Hashtable<Integer, AtomicInteger>> clients_actions = new Hashtable<>();
	
	private static Hashtable<String, Hashtable<Integer, Integer>> clients_nextGameTick = new Hashtable<>();
	
	private static Hashtable<String, Boolean> punishes_in_progress = new Hashtable<>();
	
	/**
	 * Checks whether the request is flood protected or not.
	 * @param opcode
	 * @param opcode2
	 * @param client
	 * @return true if action is allowed, otherwise false
	 */
	@Override
	public boolean tryPerformAction(final int opcode, final int opcode2, final f<?> client)
	{
		
		String account = null;
		
		final L2GameClient game_cl = (L2GameClient) client;
		account = game_cl.accountName;
		
		if (account == null)
			return true;
		
		// Ignore flood protector for GM char
		if (game_cl.getActiveChar() != null && game_cl.getActiveChar().isGM())
			return true;
		
		// get actual concurrent actions number for account
		AtomicInteger actions_per_account = clients_concurrent_actions.get(account);
		if (actions_per_account == null)
		{
			actions_per_account = new AtomicInteger(0);
		}
		if (actions_per_account.get() < MAX_CONCURRENT_ACTIONS_PER_PLAYER)
		{
			final int actions = actions_per_account.incrementAndGet();
			
			if (k.a().f)
			{
				LOGGER.info(" -- account " + account + " has performed " + actions + " concurrent actions until now");
			}
			
			clients_concurrent_actions.put(account, actions_per_account);
		}
		else
			return false;
		
		final int curTick = GameTimeController.getGameTicks();
		
		Hashtable<Integer, Integer> account_nextGameTicks = clients_nextGameTick.get(account);
		if (account_nextGameTicks == null)
		{
			account_nextGameTicks = new Hashtable<>();
		}
		Integer _nextGameTick = account_nextGameTicks.get(opcode);
		if (_nextGameTick == null)
		{
			_nextGameTick = curTick;
			account_nextGameTicks.put(opcode, _nextGameTick);
		}
		clients_nextGameTick.put(account, account_nextGameTicks);
		
		Boolean _punishmentInProgress = punishes_in_progress.get(account);
		if (_punishmentInProgress == null)
		{
			_punishmentInProgress = false;
		}
		else if (_punishmentInProgress)
		{
			final AtomicInteger actions = clients_concurrent_actions.get(account);
			actions.decrementAndGet();
			clients_concurrent_actions.put(account, actions);
			return false;
		}
		punishes_in_progress.put(account, _punishmentInProgress);
		
		Hashtable<Integer, AtomicInteger> received_commands_actions = clients_actions.get(account);
		if (received_commands_actions == null)
		{
			received_commands_actions = new Hashtable<>();
		}
		AtomicInteger command_count = null;
		if ((command_count = received_commands_actions.get(opcode)) == null)
		{
			command_count = new AtomicInteger(0);
			received_commands_actions.put(opcode, command_count);
		}
		clients_actions.put(account, received_commands_actions);
		
		if (curTick <= _nextGameTick && !_punishmentInProgress) // time to check operations
		{
			command_count.incrementAndGet();
			clients_actions.get(account).put(opcode, command_count);
			
			if (k.a().f)
			{
				LOGGER.info("-- called OpCode " + Integer.toHexString(opcode) + " ~" + String.valueOf((k.a().s - (_nextGameTick - curTick)) * GameTimeController.MILLIS_IN_TICK) + " ms after first command...");
				LOGGER.info("   total received packets with OpCode " + Integer.toHexString(opcode) + " into the Interval: " + command_count.get());
			}
			
			if (k.a().u > 0 && command_count.get() >= k.a().u && k.a().v != null)
			{
				punishes_in_progress.put(account, true);
				
				if (!isOpCodeToBeTested(opcode, opcode2))
				{
					if (k.a().t)
						LOGGER.warn("ATTENTION: Account " + account + " is flooding the server...");
					
					if ("kick".equals(k.a().v))
					{
						if (k.a().t)
							LOGGER.warn(" ------- kicking account " + account);
						kickPlayer(client, opcode);
					}
					else if ("ban".equals(k.a().v))
					{
						if (k.a().t)
							LOGGER.warn(" ------- banning account " + account);
						banAccount(client, opcode);
					}
				}
				// clear already punished account
				punishes_in_progress.remove(account);
				clients_nextGameTick.remove(account);
				clients_actions.remove(account);
				clients_concurrent_actions.remove(account);
				
				return false;
			}
			
			if (curTick == _nextGameTick)
			{ // if is the first time, just calculate the next game tick
				_nextGameTick = curTick + k.a().s;
				clients_nextGameTick.get(account).put(opcode, _nextGameTick);
			}
			
			final AtomicInteger actions = clients_concurrent_actions.get(account);
			actions.decrementAndGet();
			clients_concurrent_actions.put(account, actions);
			
			return true;
		}
		punishes_in_progress.put(account, false);
		clients_nextGameTick.get(account).remove(opcode);
		clients_actions.get(account).remove(opcode);
		
		final AtomicInteger actions = clients_concurrent_actions.get(account);
		actions.decrementAndGet();
		clients_concurrent_actions.put(account, actions);
		
		return true;
		
	}
	
	@Override
	public boolean isOpCodeToBeTested(final int opcode, final int opcode2)
	{
		if (opcode == 0xd0)
		{
			if (k.a().w.contains(opcode))
			{
				return !k.a().x.contains(opcode2);
			}
			return true;
			
		}
		
		return !k.a().w.contains(opcode);
		
	}
	
	private static void kickPlayer(final f<?> _client, final int opcode)
	{
		final L2GameClient game_cl = (L2GameClient) _client;
		game_cl.closeNow();
		
		LOGGER.warn("Player with account " + game_cl.accountName + " kicked for flooding with packet " + Integer.toHexString(opcode));
		
	}
	
	private static void banAccount(final f<?> _client, final int opcode)
	{
		
		final L2GameClient game_cl = (L2GameClient) _client;
		
		if (game_cl.getActiveChar() != null)
		{
			game_cl.getActiveChar().setPunishLevel(PunishLevel.ACC, 0);
			LOGGER.warn("Player " + game_cl.getActiveChar() + " of account " + game_cl.accountName + " banned forever for flooding with packet " + Integer.toHexString(opcode));
			game_cl.getActiveChar().logout();
		}
		
		game_cl.closeNow();
		LOGGER.warn("Player with account " + game_cl.accountName + " kicked for flooding with packet " + Integer.toHexString(opcode));
		
	}
	
}
