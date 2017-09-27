/*
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
package com.l2jfrozen.manager.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.GregorianCalendar;

import com.l2jfrozen.util.database.CloseUtil;
import com.l2jfrozen.util.database.L2DatabaseFactory;

public class Server
{
	private static final String TABLE ="linked_servers";
	private static final String COLUMN_IDS="ServerName,LoginServerIp,LoginServerPort,GameServerIp,GameServerPort,Date";
	
	/**
	 * Retained for compatibility with PowerPak
	 */
	public int sendInfo(String ServerName,String LoginServerIp,int LoginServerPort, String GameServerIp, int GameServerPort)
	{
		System.out.println("ReceivedInfo:");
		System.out.println("	ServerName: "+ServerName);
		System.out.println("	LoginServerIp: "+LoginServerIp);
		System.out.println("	LoginServerPort: "+LoginServerPort);
		System.out.println("	GameServerIp: "+GameServerIp);
		System.out.println("	GameServerPort: "+GameServerPort);
		
		String Date = DateFormat.getDateTimeInstance().format(GregorianCalendar.getInstance().getTime());
		System.out.println("	Date: "+Date);
		
		/*
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection(false);
			statement = con.prepareStatement("INSERT INTO "+TABLE+" ("+COLUMN_IDS+") values (?,?,?,?,?,?)");
			statement.setString(1, ServerName);
			statement.setString(2, LoginServerIp);
			statement.setInt(3, LoginServerPort);
			statement.setString(4, GameServerIp);
			statement.setInt(5, GameServerPort);
			statement.setString(6, Date);
			statement.executeUpdate();
			
			statement.close();
			statement = null;
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseUtil.close(con);
		}
		*/
		
		return 0;
	}

	
}
