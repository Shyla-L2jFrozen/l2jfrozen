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
package com.l2jfrozen.util.taskmanager;

import static com.l2jfrozen.util.taskmanager.TaskTypes.TYPE_SHEDULED;
import static com.l2jfrozen.util.taskmanager.TaskTypes.TYPE_TIME;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ScheduledFuture;

import com.l2jfrozen.CommonConfig;
import com.l2jfrozen.util.CloseUtil;
import com.l2jfrozen.util.database.DatabaseUtils;
import com.l2jfrozen.util.database.L2DatabaseFactory;

/**
 * @author Shyla
 *
 */
public class ExecutedTask implements Runnable
{
	private int id;
	private long lastActivation;
	private Task task;
	private TaskTypes type;
	private String[] params;
	private ScheduledFuture<?> scheduled;
	
	public ExecutedTask(final Task ptask, final TaskTypes ptype, final ResultSet rset) throws SQLException
	{
		task = ptask;
		type = ptype;
		id = rset.getInt("id");
		lastActivation = rset.getLong("last_activation");
		params = new String[]
		{
			rset.getString("param1"),
			rset.getString("param2"),
			rset.getString("param3")
		};
	}
	
	@Override
	public void run()
	{
		task.onTimeElapsed(this);
		lastActivation = System.currentTimeMillis();
		
		TaskManager.updateExecutedTaskOnDB(lastActivation, id);
		
		if (type == TYPE_SHEDULED || type == TYPE_TIME)
		{
			stopTask();
			
		}
	}
	
	@Override
	public boolean equals(final Object object)
	{
		if (object == null)
		{
			return false;
		}
		return id == ((ExecutedTask) object).id;
	}
	
	@Override
	public int hashCode()
	{
		return id;
	}
	
	public Task getTask()
	{
		return task;
	}
	
	public TaskTypes getType()
	{
		return type;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String[] getParams()
	{
		return params;
	}
	
	public long getLastActivation()
	{
		return lastActivation;
	}
	
	private void stopTask()
	{
		task.onDestroy();
		
		if (scheduled != null)
		{
			scheduled.cancel(true);
		}
		
		TaskManager.getInstance().removeActiveTask(this);
		
	}

//	/**
//	 * @return the scheduled
//	 */
//	public ScheduledFuture<?> getScheduled()
//	{
//		return scheduled;
//	}

	/**
	 * @param scheduled the scheduled to set
	 */
	public void setScheduled(ScheduledFuture<?> scheduled)
	{
		this.scheduled = scheduled;
	}
	
}

