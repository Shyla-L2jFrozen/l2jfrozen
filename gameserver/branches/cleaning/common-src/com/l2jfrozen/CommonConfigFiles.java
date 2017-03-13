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
package com.l2jfrozen;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Shyla
 */
public class CommonConfigFiles
{
	private static final Logger LOGGER = Logger.getLogger(CommonConfigFiles.class);
	
	protected static String MAIN_CONFIGURATION_FILE = "./config/configFilesPaths.properties";
	
	public static String COMMON_CONFIGURATION_FILE;
	public static String LOG4J_CONF_FILE;
	public static String LOG_CONF_FILE;
	
	
	protected static Properties configFiles = new Properties();
	
	// load statically the configuration files
	static
	{
		loadConfigurationFilesPaths(MAIN_CONFIGURATION_FILE);
	}
	
	public static void loadConfigurationFilesPaths()
	{
		loadConfigurationFilesPaths(MAIN_CONFIGURATION_FILE);
	}
	
	public static void loadConfigurationFilesPaths(final String mainConfigurationFile)
	{
		try
		{
			final InputStream is = new FileInputStream(new File(mainConfigurationFile));
			configFiles.load(is);
			is.close();
			
			COMMON_CONFIGURATION_FILE = configFiles.getProperty("CommonConfigFilePath", "./config/common.properties");
			LOG_CONF_FILE = configFiles.getProperty("LogConfigFilePath", "./config/others/log.cfg");
			LOG4J_CONF_FILE = configFiles.getProperty("Log4jConfigFilePath", "./config/others/logger.properties");
			
		}
		catch (final Exception e)
		{
			LOGGER.error("Failed to Load " + mainConfigurationFile + " File.", e);
		}
	}
	
}
