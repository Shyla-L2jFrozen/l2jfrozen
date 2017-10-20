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
package com.l2jfrozen.loginserver;

import com.l2jfrozen.common.CommonConfigFiles;

/**
 * @author Shyla
 */
public class LoginConfigFiles extends CommonConfigFiles
{
	
	public static String LOGIN_CONFIGURATION_FILE;
	public static String BANNED_IP;
	public static String SERVER_NAME_FILE;
	
	// load statically the configuration files
	static
	{
		loadConfigurationFilesPaths(CommonConfigFiles.MAIN_CONFIGURATION_FILE);
	}
	
	public static void loadConfigurationFilesPaths(final String mainConfigurationFile)
	{
		CommonConfigFiles.loadConfigurationFilesPaths(mainConfigurationFile);
		
		LOGIN_CONFIGURATION_FILE = configFiles.getProperty("LoginConfigFilePath", "./config/loginserver.properties");
		BANNED_IP = configFiles.getProperty("BannedIpsConfigFilePath", "./config/others/banned_ip.cfg");
		SERVER_NAME_FILE = configFiles.getProperty("ServersNameConfigFilePath", "./config/others/servername.xml");
		
	}
	
}
