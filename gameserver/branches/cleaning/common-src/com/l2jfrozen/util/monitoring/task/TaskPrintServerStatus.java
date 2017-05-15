/* L2jFrozen Project - www.l2jfrozen.com 
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
package com.l2jfrozen.util.monitoring.task;

import com.l2jfrozen.util.Util;
import com.l2jfrozen.util.monitoring.StatusManager;
import com.l2jfrozen.util.taskmanager.ExecutedTask;
import com.l2jfrozen.util.taskmanager.Task;
import com.l2jfrozen.util.taskmanager.TaskManager;
import com.l2jfrozen.util.taskmanager.TaskTypes;

/**
 * @author Shyla
 */
public class TaskPrintServerStatus extends Task
{
	private final String NAME = "printserverstatus";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(final ExecutedTask task)
	{
		Util.printSection("Server Status");
		StatusManager.getInstance().logCurrentMonitoredStatuses();
		Util.printSection("Server Status");
	}
	
	@Override
	public void initializate()
	{
		super.initializate();
		TaskManager.addUniqueTaskOnDB(NAME, TaskTypes.TYPE_FIXED_SHEDULED, "3600000", "3600000", "");
	}
}