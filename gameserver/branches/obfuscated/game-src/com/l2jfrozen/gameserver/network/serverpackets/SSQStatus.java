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

import org.apache.log4j.Logger;

import com.l2jfrozen.common.CommonConfig;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;
import com.l2jfrozen.gameserver.model.entity.sevensigns.SevenSigns;
import com.l2jfrozen.gameserver.model.entity.sevensigns.SevenSignsFestival;
import com.l2jfrozen.gameserver.network.SystemMessageId;
import com.l2jfrozen.gameserver.templates.StatsSet;

/**
 * Seven Signs Record Update packet type id 0xf5 format: c cc (Page Num = 1 -> 4, period) 1: [ddd cc dd ddd c ddd c] 2: [hc [cd (dc (S))] 3: [ccc (cccc)] 4: [(cchh)]
 * @author Tempy
 */
public class SSQStatus extends L2GameServerPacket
{
	private static Logger LOGGER = Logger.getLogger(SSQStatus.class);
	
	private static final String _S__F5_SSQStatus = "[S] F5 RecordUpdate";
	private final L2PcInstance _activevChar;
	private final int _page;
	
	public SSQStatus(final L2PcInstance player, final int recordPage)
	{
		_activevChar = player;
		_page = recordPage;
	}
	
	@Override
	protected final void writeImpl()
	{
		final int winningCabal = SevenSigns.getInstance().getCabalHighestScore();
		final int totalDawnMembers = SevenSigns.getInstance().getTotalMembers(SevenSigns.CABAL_DAWN);
		final int totalDuskMembers = SevenSigns.getInstance().getTotalMembers(SevenSigns.CABAL_DUSK);
		
		C(0xf5);
		
		C(_page);
		C(SevenSigns.getInstance().getCurrentPeriod()); // current period?
		
		int dawnPercent = 0;
		int duskPercent = 0;
		
		switch (_page)
		{
			case 1:
				// [ddd cc dd ddd c ddd c]
				D(SevenSigns.getInstance().getCurrentCycle());
				
				final int currentPeriod = SevenSigns.getInstance().getCurrentPeriod();
				
				switch (currentPeriod)
				{
					case SevenSigns.PERIOD_COMP_RECRUITING:
						D(SystemMessageId.INITIAL_PERIOD.getId());
						break;
					case SevenSigns.PERIOD_COMPETITION:
						D(SystemMessageId.QUEST_EVENT_PERIOD.getId());
						break;
					case SevenSigns.PERIOD_COMP_RESULTS:
						D(SystemMessageId.RESULTS_PERIOD.getId());
						break;
					case SevenSigns.PERIOD_SEAL_VALIDATION:
						D(SystemMessageId.VALIDATION_PERIOD.getId());
						break;
				}
				
				switch (currentPeriod)
				{
					case SevenSigns.PERIOD_COMP_RECRUITING:
					case SevenSigns.PERIOD_COMP_RESULTS:
						D(SystemMessageId.UNTIL_TODAY_6PM.getId());
						break;
					case SevenSigns.PERIOD_COMPETITION:
					case SevenSigns.PERIOD_SEAL_VALIDATION:
						D(SystemMessageId.UNTIL_MONDAY_6PM.getId());
						break;
				}
				
				C(SevenSigns.getInstance().getPlayerCabal(_activevChar));
				C(SevenSigns.getInstance().getPlayerSeal(_activevChar));
				
				D(SevenSigns.getInstance().getPlayerStoneContrib(_activevChar)); // Seal Stones Turned-In
				D(SevenSigns.getInstance().getPlayerAdenaCollect(_activevChar)); // Ancient Adena to Collect
				
				final double dawnStoneScore = SevenSigns.getInstance().getCurrentStoneScore(SevenSigns.CABAL_DAWN);
				final int dawnFestivalScore = SevenSigns.getInstance().getCurrentFestivalScore(SevenSigns.CABAL_DAWN);
				
				final double duskStoneScore = SevenSigns.getInstance().getCurrentStoneScore(SevenSigns.CABAL_DUSK);
				final int duskFestivalScore = SevenSigns.getInstance().getCurrentFestivalScore(SevenSigns.CABAL_DUSK);
				
				final double totalStoneScore = duskStoneScore + dawnStoneScore;
				
				/*
				 * Scoring seems to be proportionate to a set base value, so base this on the maximum obtainable score from festivals, which is 500.
				 */
				int duskStoneScoreProp = 0;
				int dawnStoneScoreProp = 0;
				
				if (totalStoneScore != 0)
				{
					duskStoneScoreProp = Math.round((float) duskStoneScore / (float) totalStoneScore * 500);
					dawnStoneScoreProp = Math.round((float) dawnStoneScore / (float) totalStoneScore * 500);
				}
				
				final int duskTotalScore = SevenSigns.getInstance().getCurrentScore(SevenSigns.CABAL_DUSK);
				final int dawnTotalScore = SevenSigns.getInstance().getCurrentScore(SevenSigns.CABAL_DAWN);
				
				final int totalOverallScore = duskTotalScore + dawnTotalScore;
				
				if (totalOverallScore != 0)
				{
					dawnPercent = Math.round((float) dawnTotalScore / (float) totalOverallScore * 100);
					duskPercent = Math.round((float) duskTotalScore / (float) totalOverallScore * 100);
				}
				
				if (CommonConfig.DEBUG)
				{
					LOGGER.info("Dusk Stone Score: " + duskStoneScore + " - Dawn Stone Score: " + dawnStoneScore);
					LOGGER.info("Dusk Festival Score: " + duskFestivalScore + " - Dawn Festival Score: " + dawnFestivalScore);
					LOGGER.info("Dusk Score: " + duskTotalScore + " - Dawn Score: " + dawnTotalScore);
					LOGGER.info("Overall Score: " + totalOverallScore);
					LOGGER.info("");
					if (totalStoneScore == 0)
					{
						LOGGER.info("Dusk Prop: 0 - Dawn Prop: 0");
					}
					else
					{
						LOGGER.info("Dusk Prop: " + duskStoneScore / totalStoneScore * 500 + " - Dawn Prop: " + dawnStoneScore / totalStoneScore * 500);
					}
					LOGGER.info("Dusk %: " + duskPercent + " - Dawn %: " + dawnPercent);
				}
				
				/* DUSK */
				D(duskStoneScoreProp); // Seal Stone Score
				D(duskFestivalScore); // Festival Score
				D(duskTotalScore); // Total Score
				
				C(duskPercent); // Dusk %
				
				/* DAWN */
				D(dawnStoneScoreProp); // Seal Stone Score
				D(dawnFestivalScore); // Festival Score
				D(dawnTotalScore); // Total Score
				
				C(dawnPercent); // Dawn %
				break;
			case 2:
				// c cc hc [cd (dc (S))]
				H(1);
				
				C(5); // Total number of festivals
				
				for (int i = 0; i < 5; i++)
				{
					C(i + 1); // Current client-side festival ID
					D(SevenSignsFestival.FESTIVAL_LEVEL_SCORES[i]);
					
					final int duskScore = SevenSignsFestival.getInstance().getHighestScore(SevenSigns.CABAL_DUSK, i);
					final int dawnScore = SevenSignsFestival.getInstance().getHighestScore(SevenSigns.CABAL_DAWN, i);
					
					// Dusk Score \\
					D(duskScore);
					
					StatsSet highScoreData = SevenSignsFestival.getInstance().getHighestScoreData(SevenSigns.CABAL_DUSK, i);
					String[] partyMembers = highScoreData.getString("members").split(",");
					
					if (partyMembers != null)
					{
						C(partyMembers.length);
						
						for (final String partyMember : partyMembers)
						{
							S(partyMember);
						}
					}
					else
					{
						C(0);
					}
					
					// Dawn Score \\
					D(dawnScore);
					
					highScoreData = SevenSignsFestival.getInstance().getHighestScoreData(SevenSigns.CABAL_DAWN, i);
					partyMembers = highScoreData.getString("members").split(",");
					
					if (partyMembers != null)
					{
						C(partyMembers.length);
						
						for (final String partyMember : partyMembers)
						{
							S(partyMember);
						}
					}
					else
					{
						C(0);
					}
				}
				break;
			case 3:
				// c cc [ccc (cccc)]
				C(10); // Minimum limit for winning cabal to retain their seal
				C(35); // Minimum limit for winning cabal to claim a seal
				C(3); // Total number of seals
				
				for (int i = 1; i < 4; i++)
				{
					final int dawnProportion = SevenSigns.getInstance().getSealProportion(i, SevenSigns.CABAL_DAWN);
					final int duskProportion = SevenSigns.getInstance().getSealProportion(i, SevenSigns.CABAL_DUSK);
					
					if (CommonConfig.DEBUG)
					{
						LOGGER.info(SevenSigns.getSealName(i, true) + " = Dawn Prop: " + dawnProportion + "(" + dawnProportion / totalDawnMembers * 100 + "%)" + ", Dusk Prop: " + duskProportion + "(" + duskProportion / totalDuskMembers * 100 + "%)");
					}
					
					C(i);
					C(SevenSigns.getInstance().getSealOwner(i));
					
					if (totalDuskMembers == 0)
					{
						if (totalDawnMembers == 0)
						{
							C(0);
							C(0);
						}
						else
						{
							C(0);
							C(Math.round((float) dawnProportion / (float) totalDawnMembers * 100));
						}
					}
					else
					{
						if (totalDawnMembers == 0)
						{
							C(Math.round((float) duskProportion / (float) totalDuskMembers * 100));
							C(0);
						}
						else
						{
							C(Math.round((float) duskProportion / (float) totalDuskMembers * 100));
							C(Math.round((float) dawnProportion / (float) totalDawnMembers * 100));
						}
					}
				}
				break;
			case 4:
				// c cc [cc (cchh)]
				C(winningCabal); // Overall predicted winner
				C(3); // Total number of seals
				
				for (int i = 1; i < 4; i++)
				{
					final int dawnProportion = SevenSigns.getInstance().getSealProportion(i, SevenSigns.CABAL_DAWN);
					final int duskProportion = SevenSigns.getInstance().getSealProportion(i, SevenSigns.CABAL_DUSK);
					dawnPercent = Math.round(dawnProportion / (totalDawnMembers == 0 ? 1 : (float) totalDawnMembers) * 100);
					duskPercent = Math.round(duskProportion / (totalDuskMembers == 0 ? 1 : (float) totalDuskMembers) * 100);
					final int sealOwner = SevenSigns.getInstance().getSealOwner(i);
					
					C(i);
					
					switch (sealOwner)
					{
						case SevenSigns.CABAL_NULL:
							switch (winningCabal)
							{
								case SevenSigns.CABAL_NULL:
									C(SevenSigns.CABAL_NULL);
									H(SystemMessageId.COMPETITION_TIE_SEAL_NOT_AWARDED.getId());
									break;
								case SevenSigns.CABAL_DAWN:
									if (dawnPercent >= 35)
									{
										C(SevenSigns.CABAL_DAWN);
										H(SystemMessageId.SEAL_NOT_OWNED_35_MORE_VOTED.getId());
									}
									else
									{
										C(SevenSigns.CABAL_NULL);
										H(SystemMessageId.SEAL_NOT_OWNED_35_LESS_VOTED.getId());
									}
									break;
								case SevenSigns.CABAL_DUSK:
									if (duskPercent >= 35)
									{
										C(SevenSigns.CABAL_DUSK);
										H(SystemMessageId.SEAL_NOT_OWNED_35_MORE_VOTED.getId());
									}
									else
									{
										C(SevenSigns.CABAL_NULL);
										H(SystemMessageId.SEAL_NOT_OWNED_35_LESS_VOTED.getId());
									}
									break;
							}
							break;
						case SevenSigns.CABAL_DAWN:
							switch (winningCabal)
							{
								case SevenSigns.CABAL_NULL:
									if (dawnPercent >= 10)
									{
										C(SevenSigns.CABAL_DAWN);
										H(SystemMessageId.SEAL_OWNED_10_MORE_VOTED.getId());
										break;
									}
									C(SevenSigns.CABAL_NULL);
									H(SystemMessageId.COMPETITION_TIE_SEAL_NOT_AWARDED.getId());
									break;
								case SevenSigns.CABAL_DAWN:
									if (dawnPercent >= 10)
									{
										C(sealOwner);
										H(SystemMessageId.SEAL_OWNED_10_MORE_VOTED.getId());
									}
									else
									{
										C(SevenSigns.CABAL_NULL);
										H(SystemMessageId.SEAL_OWNED_10_LESS_VOTED.getId());
									}
									break;
								case SevenSigns.CABAL_DUSK:
									if (duskPercent >= 35)
									{
										C(SevenSigns.CABAL_DUSK);
										H(SystemMessageId.SEAL_NOT_OWNED_35_MORE_VOTED.getId());
									}
									else if (dawnPercent >= 10)
									{
										C(SevenSigns.CABAL_DAWN);
										H(SystemMessageId.SEAL_OWNED_10_MORE_VOTED.getId());
									}
									else
									{
										C(SevenSigns.CABAL_NULL);
										H(SystemMessageId.SEAL_OWNED_10_LESS_VOTED.getId());
									}
									break;
							}
							break;
						case SevenSigns.CABAL_DUSK:
							switch (winningCabal)
							{
								case SevenSigns.CABAL_NULL:
									if (duskPercent >= 10)
									{
										C(SevenSigns.CABAL_DUSK);
										H(SystemMessageId.SEAL_OWNED_10_MORE_VOTED.getId());
										break;
									}
									C(SevenSigns.CABAL_NULL);
									H(SystemMessageId.COMPETITION_TIE_SEAL_NOT_AWARDED.getId());
									break;
								case SevenSigns.CABAL_DAWN:
									if (dawnPercent >= 35)
									{
										C(SevenSigns.CABAL_DAWN);
										H(SystemMessageId.SEAL_NOT_OWNED_35_MORE_VOTED.getId());
									}
									else if (duskPercent >= 10)
									{
										C(sealOwner);
										H(SystemMessageId.SEAL_OWNED_10_MORE_VOTED.getId());
									}
									else
									{
										C(SevenSigns.CABAL_NULL);
										H(SystemMessageId.SEAL_OWNED_10_LESS_VOTED.getId());
									}
									break;
								case SevenSigns.CABAL_DUSK:
									if (duskPercent >= 10)
									{
										C(sealOwner);
										H(SystemMessageId.SEAL_OWNED_10_MORE_VOTED.getId());
									}
									else
									{
										C(SevenSigns.CABAL_NULL);
										H(SystemMessageId.SEAL_OWNED_10_LESS_VOTED.getId());
									}
									break;
							}
							break;
					}
					H(0);
				}
				break;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.l2jfrozen.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__F5_SSQStatus;
	}
}
