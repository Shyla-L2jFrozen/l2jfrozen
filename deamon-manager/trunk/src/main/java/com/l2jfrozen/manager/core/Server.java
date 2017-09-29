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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.l2jfrozen.Config;
import com.l2jfrozen.util.database.CloseUtil;
import com.l2jfrozen.util.database.L2DatabaseFactory;

public class Server {
	private static Logger logger = LogManager.getLogManager().getLogger("org.apache.tomcat");
	private static final String TABLE = "linked_servers";
	private static final String COLUMN_IDS = "RealIp,RealMac,ServerName,LoginServerIp,LoginServerPort,GameServerIp,GameServerPort,ActiveClients,Date,MaxOnline";

	public Server() {

		logger.info("Creating Service Core");

	}

	/**
	 * Retained for compatibility with PowerPak
	 */
	public int sendInfo(String ServerName, String LoginServerIp, int LoginServerPort, String GameServerIp,
			int GameServerPort, String networkIPs, int activeClients) {
		logger.info("loading configuration...");
		Config.loadConfig();
		logger.info("DATABASE_URL: " + Config.DATABASE_URL);
		logger.info("DATABASE_LOGIN: " + Config.DATABASE_LOGIN);
		logger.info("DATABASE_PASSWORD: " + Config.DATABASE_PASSWORD);

		String[] networkIP = networkIPs.split(",");
		logger.info("ReceivedInfo:");
		logger.info("	RealIp: " + networkIP[0]);
		logger.info("	RealMac: " + networkIP[1]);
		logger.info("	ServerName: " + ServerName);
		logger.info("	LoginServerIp: " + LoginServerIp);
		logger.info("	LoginServerPort: " + LoginServerPort);
		logger.info("	GameServerIp: " + GameServerIp);
		logger.info("	GameServerPort: " + GameServerPort);
		logger.info("	ActiveClients: " + activeClients);

		String Date = DateFormat.getDateTimeInstance().format(GregorianCalendar.getInstance().getTime());
		logger.info("	Date: " + Date);

		Connection con = null;
		PreparedStatement statement = null;
		PreparedStatement statement2 = null;

		try {
			con = L2DatabaseFactory.getInstance().getConnection(false);

			statement2 = con.prepareStatement("SELECT * FROM " + TABLE + " WHERE RealMac='" + networkIP[1] + "'");

			// execute select SQL stetement
			ResultSet rs = statement2.executeQuery();
			int OldMaxOnline = 0;
			while (rs.next()) {

				OldMaxOnline = rs.getInt("MaxOnline");
				System.out.println("ActiveClients : " + activeClients);
				System.out.println("OldMaxOnline : " + OldMaxOnline);
			}

			int MaxOnline = 0;
			if (activeClients > OldMaxOnline)
				MaxOnline = activeClients;
			else
				MaxOnline = OldMaxOnline;

			statement = con
					.prepareStatement("REPLACE INTO " + TABLE + " (" + COLUMN_IDS + ") values (?,?,?,?,?,?,?,?,?,?)");
			statement.setString(1, networkIP[0]);
			statement.setString(2, networkIP[1]);
			statement.setString(3, ServerName);
			statement.setString(4, LoginServerIp);
			statement.setString(5, "" + LoginServerPort);
			statement.setString(6, GameServerIp);
			statement.setString(7, "" + GameServerPort);
			statement.setString(8, "" + activeClients);
			statement.setString(9, Date);
			statement.setInt(10, MaxOnline);
			statement.executeUpdate();

			statement.close();
			statement = null;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			CloseUtil.close(con);
		}

		return 0;
	}

}