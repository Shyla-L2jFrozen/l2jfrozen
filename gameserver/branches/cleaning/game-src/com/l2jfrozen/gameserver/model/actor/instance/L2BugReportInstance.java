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
package com.l2jfrozen.gameserver.model.actor.instance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.StringTokenizer;

import com.l2jfrozen.gameserver.ai.CtrlIntention;
import com.l2jfrozen.gameserver.model.L2World;
import com.l2jfrozen.gameserver.network.L2GameClient;
import com.l2jfrozen.gameserver.network.clientpackets.Say2;
import com.l2jfrozen.gameserver.network.serverpackets.ActionFailed;
import com.l2jfrozen.gameserver.network.serverpackets.CreatureSay;
import com.l2jfrozen.gameserver.network.serverpackets.MyTargetSelected;
import com.l2jfrozen.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jfrozen.gameserver.network.serverpackets.ValidateLocation;
import com.l2jfrozen.gameserver.templates.L2NpcTemplate;

import javolution.text.TextBuilder;

/**
 * @author squallcs
 */
public class L2BugReportInstance extends L2FolkInstance
{
	private static String _type;
	
	public L2BugReportInstance(final int objectId, final L2NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(final L2PcInstance player, final String command)
	{
		if (command.startsWith("send_report"))
		{
			final StringTokenizer st = new StringTokenizer(command);
			st.nextToken();
			String msg = null;
			String type = null;
			type = st.nextToken();
			msg = st.nextToken();
			try
			{
				while (st.hasMoreTokens())
				{
					msg = msg + " " + st.nextToken();
				}
				
				sendReport(player, type, msg);
			}
			catch (final StringIndexOutOfBoundsException e)
			{
			}
		}
	}
	
	static
	{
		new File("log/BugReports/").mkdirs();
	}
	
	private void sendReport(final L2PcInstance player, final String command, final String msg)
	{
		final String type = command;
		final L2GameClient info = player.getClient().getConnection().getClient();
		
		if (type.equals("General"))
			_type = "General";
		if (type.equals("Fatal"))
			_type = "Fatal";
		if (type.equals("Misuse"))
			_type = "Misuse";
		if (type.equals("Balance"))
			_type = "Balance";
		if (type.equals("Other"))
			_type = "Other";
		
		try
		{
			final String fname = "log/BugReports/" + player.getName() + ".txt";
			final File file = new File(fname);
			final boolean exist = file.createNewFile();
			if (!exist)
			{
				player.sendMessage("You have already sent a bug report, GMs must check it first.");
				return;
			}
			final FileWriter fstream = new FileWriter(fname);
			final BufferedWriter out = new BufferedWriter(fstream);
			out.write("Character Info: " + info + "\r\nBug Type: " + _type + "\r\nMessage: " + msg);
			player.sendMessage("Report sent. GMs will check it soon. Thanks...");
			
			for (final L2PcInstance allgms : L2World.getInstance().getAllGMs())
				allgms.sendPacket(new CreatureSay(0, Say2.SHOUT, "Bug Report Manager", player.getName() + " sent a bug report."));
			
			System.out.println("Character: " + player.getName() + " sent a bug report.");
			out.close();
		}
		catch (final Exception e)
		{
			player.sendMessage("Something went wrong try again.");
		}
	}
	
	@Override
	public void onAction(final L2PcInstance player)
	{
		if (!canTarget(player))
		{
			return;
		}
		
		if (this != player.getTarget())
		{
			player.setTarget(this);
			
			player.sendPacket(new MyTargetSelected(getObjectId(), 0));
			
			player.sendPacket(new ValidateLocation(this));
		}
		else if (!canInteract(player))
		{
			player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, this);
		}
		else
		{
			showHtmlWindow(player);
		}
		
		player.sendPacket(new ActionFailed());
	}
	
	private void showHtmlWindow(final L2PcInstance activeChar)
	{
		final NpcHtmlMessage nhm = new NpcHtmlMessage(5);
		final TextBuilder replyMSG = new TextBuilder("");
		
		replyMSG.append("<html><title>Bug Report Manager</title>");
		replyMSG.append("<body><br><br><center>");
		replyMSG.append("<table border=0 height=10 bgcolor=\"444444\" width=240>");
		replyMSG.append("<tr><td align=center><font color=\"00FFFF\">Hello " + activeChar.getName() + ".</font></td></tr>");
		replyMSG.append("<tr><td align=center><font color=\"00FFFF\">There are no Gms online</font></td></tr>");
		replyMSG.append("<tr><td align=center><font color=\"00FFFF\">and you want to report something?</font></td></tr>");
		replyMSG.append("</table><br>");
		replyMSG.append("<img src=\"L2UI.SquareWhite\" width=280 height=1><br><br>");
		replyMSG.append("<table width=250><tr>");
		replyMSG.append("<td><font color=\"LEVEL\">Select Report Type:</font></td>");
		replyMSG.append("<td><combobox width=105 var=type list=General;Fatal;Misuse;Balance;Other></td>");
		replyMSG.append("</tr></table><br><br>");
		replyMSG.append("<multiedit var=\"msg\" width=250 height=50><br>");
		replyMSG.append("<br><img src=\"L2UI.SquareWhite\" width=280 height=1><br><br><br><br><br><br><br>");
		replyMSG.append("<button value=\"Send Report\" action=\"bypass -h npc_" + getObjectId() + "_send_report $type $msg\" width=204 height=20 back=\"sek.cbui75\" fore=\"sek.cbui75\">");
		replyMSG.append("</center></body></html>");
		
		nhm.setHtml(replyMSG.toString());
		activeChar.sendPacket(nhm);
		
		activeChar.sendPacket(new ActionFailed());
	}
}