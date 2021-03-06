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
package com.l2jfrozen.common.util.monitoring;

import java.util.ArrayList;
import java.util.List;

import com.l2jfrozen.common.util.monitoring.data.MonitoredStatus;
import com.l2jfrozen.common.util.monitoring.data.RuntimeStatus;
import com.l2jfrozen.common.util.monitoring.task.TaskPrintServerStatus;
import com.l2jfrozen.common.util.taskmanager.TaskManager;

/**
 * @author Shyla
 */
public class StatusManager
{
	private final List<MonitoredStatus> monitoredStatus = new ArrayList<>();
	
	private StatusManager()
	{
		registerMonitoredStatus(new RuntimeStatus());
		
		TaskManager.getInstance().registerTask(new TaskPrintServerStatus());
	}
	
	/*
	 * public static RuntimeStatus getRuntimeStatusInfo() { RuntimeStatus output = new RuntimeStatus(); return output; }
	 */
	
	public boolean registerMonitoredStatus(final MonitoredStatus status)
	{
		if (!monitoredStatus.contains(status))
		{
			monitoredStatus.add(status);
			return true;
		}
		
		return false;
	}
	
	public boolean unregisterMonitoredStatus(final MonitoredStatus status)
	{
		if (!monitoredStatus.contains(status))
		{
			return false;
		}
		
		monitoredStatus.remove(status);
		return true;
	}
	
	public void logStaticMonitoredStatuses()
	{
		
		for (final MonitoredStatus status : monitoredStatus)
		{
			status.refreshStatus();
			status.printStaticStatus();
		}
		
	}
	
	public void logCurrentMonitoredStatuses()
	{
		for (final MonitoredStatus status : monitoredStatus)
		{
			status.refreshStatus();
			status.printDynamicStatus();
		}
	}
	
	public List<MonitoredStatus> getCurrentMonitoredStatuses()
	{
		for (final MonitoredStatus status : monitoredStatus)
		{
			status.refreshStatus();
		}
		return monitoredStatus;
	}
	
	public static StatusManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final StatusManager _instance = new StatusManager();
	}
}