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
package com.l2jfrozen.util.monitoring.data;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * @author Shyla
 */
public abstract class MonitoredStatus implements Serializable
{
	protected final Logger LOGGER = Logger.getLogger(this.getClass());
	
	public abstract void refreshStatus();
	
	public abstract String getStaticStatus();
	
	public abstract String getDynamicStatus();
	
	public void printDynamicStatus()
	{
		LOGGER.info(getDynamicStatus());
	}
	
	public void printStaticStatus()
	{
		LOGGER.info(getStaticStatus());
	}
	
}
