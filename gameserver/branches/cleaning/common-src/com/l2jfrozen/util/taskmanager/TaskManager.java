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
package com.l2jfrozen.util.taskmanager;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.apache.log4j.Logger;

import com.l2jfrozen.CommonConfig;

import com.l2jfrozen.thread.ThreadPoolManager;
import com.l2jfrozen.util.CloseUtil;
import com.l2jfrozen.util.database.DatabaseUtils;
import com.l2jfrozen.util.database.L2DatabaseFactory;
import com.sun.org.omg.SendingContext._CodeBaseImplBase;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author ProGramMoS
 */
public final class TaskManager
{
	private static final Logger LOGGER = Logger.getLogger(TaskManager.class);
	private static TaskManager _instance;
	
	private static final String[] SQL_STATEMENTS =
	{
		"SELECT id,task,type,last_activation,param1,param2,param3 FROM global_tasks",
		"UPDATE global_tasks SET last_activation=? WHERE id=?",
		"SELECT id FROM global_tasks WHERE task=?",
		"INSERT INTO global_tasks (task,type,last_activation,param1,param2,param3) VALUES(?,?,?,?,?,?)"
	};
	
	private final FastMap<Integer, Task> _tasks = new FastMap<>();
	private final FastList<ExecutedTask> _currentTasks = new FastList<>();
	
	public static TaskManager getInstance()
	{
		if (_instance == null)
			_instance = new TaskManager();
		return _instance;
	}
	
	private TaskManager()
	{
		//initializate();
		//startAllTasks();
	}
	
	/*
	private void initializate()
	{
		registerTask(new TaskCleanUp());
		// registerTask(new TaskJython());
		registerTask(new TaskOlympiadSave());
		registerTask(new TaskRaidPointsReset());
		registerTask(new TaskRecom());
		registerTask(new TaskRestart());
		registerTask(new TaskSevenSignsUpdate());
		registerTask(new TaskShutdown());
	}
	*/
	
	public void registerTask(final Task task)
	{
		final int key = task.getName().hashCode();
		if (!_tasks.containsKey(key))
		{
			_tasks.put(key, task);
			task.initializate();
		}
	}
	
	public void startRegisteredTasksPresentOnDB()
	{
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection(false);
			PreparedStatement statement = con.prepareStatement(SQL_STATEMENTS[0]);
			ResultSet rset = statement.executeQuery();
			
			while (rset.next())
			{
				final Task task = _tasks.get(rset.getString("task").trim().toLowerCase().hashCode());
				
				if (task == null)
				{
					continue;
				}
				
				final TaskTypes type = TaskTypes.valueOf(rset.getString("type"));
				
				if (type != TaskTypes.TYPE_NONE)
				{
					final ExecutedTask current = new ExecutedTask(task, type, rset);
					if (launchTask(current))
					{
						_currentTasks.add(current);
					}
				}
				
			}
			
			DatabaseUtils.close(rset);
			DatabaseUtils.close(statement);
			rset = null;
			statement = null;
			
		}
		catch (final Exception e)
		{
			LOGGER.error("Error while loading Global Task table ", e);
		}
		finally
		{
			CloseUtil.close(con);
			con = null;
		}
	}
	
	private boolean launchTask(final ExecutedTask task)
	{
		final ThreadPoolManager scheduler = ThreadPoolManager.getInstance();
		final TaskTypes type = task.getType();
		
		if (type == TaskTypes.TYPE_STARTUP)
		{
			task.run();
			return false;
		}
		else if (type == TaskTypes.TYPE_SHEDULED)
		{
			final long delay = Long.valueOf(task.getParams()[0]);
			task.setScheduled(scheduler.scheduleGeneral(task, delay));
			return true;
		}
		else if (type == TaskTypes.TYPE_FIXED_SHEDULED)
		{
			final long delay = Long.valueOf(task.getParams()[0]);
			final long interval = Long.valueOf(task.getParams()[1]);
			
			task.setScheduled(scheduler.scheduleGeneralAtFixedRate(task, delay, interval));
			return true;
		}
		else if (type == TaskTypes.TYPE_TIME)
		{
			try
			{
				final Date desired = DateFormat.getInstance().parse(task.getParams()[0]);
				final long diff = desired.getTime() - System.currentTimeMillis();
				if (diff >= 0)
				{
					task.setScheduled(scheduler.scheduleGeneral(task, diff));
					return true;
				}
				LOGGER.info("Task " + task.getId() + " is obsoleted.");
			}
			catch (final Exception e)
			{
				if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
					e.printStackTrace();
			}
		}
		else if (type == TaskTypes.TYPE_SPECIAL)
		{
			final ScheduledFuture<?> result = task.getTask().launchSpecial(task);
			if (result != null)
			{
				task.setScheduled(result);
				return true;
			}
		}
		else if (type == TaskTypes.TYPE_GLOBAL_TASK)
		{
			final long interval = Long.valueOf(task.getParams()[0]) * 86400000L;
			final String[] hour = task.getParams()[1].split(":");
			
			if (hour.length != 3)
			{
				LOGGER.warn("Task " + task.getId() + " has incorrect parameters");
				return false;
			}
			
			final Calendar check = Calendar.getInstance();
			check.setTimeInMillis(task.getLastActivation() + interval);
			
			final Calendar min = Calendar.getInstance();
			try
			{
				min.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour[0]));
				min.set(Calendar.MINUTE, Integer.valueOf(hour[1]));
				min.set(Calendar.SECOND, Integer.valueOf(hour[2]));
			}
			catch (final Exception e)
			{
				if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
					e.printStackTrace();
				LOGGER.warn("Bad parameter on task " + task.getId() + ": " + e.getMessage());
				return false;
			}
			
			long delay = min.getTimeInMillis() - System.currentTimeMillis();
			
			if (check.after(min) || delay < 0)
			{
				delay += interval;
			}
			
			task.setScheduled(scheduler.scheduleGeneralAtFixedRate(task, delay, interval));
			
			return true;
		}
		
		return false;
	}
	
	public static boolean addUniqueTaskOnDB(final String task, final TaskTypes type, final String param1, final String param2, final String param3)
	{
		return addUniqueTaskOnDB(task, type, param1, param2, param3, 0);
	}
	
	public static boolean updateExecutedTaskOnDB(long lastActivation, int id)
	{
		Connection con = null;
		
		boolean output = false;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection(false);
			PreparedStatement statement = con.prepareStatement(SQL_STATEMENTS[1]);
			statement.setLong(1, lastActivation);
			statement.setInt(2, id);
			statement.executeUpdate();
			DatabaseUtils.close(statement);
			statement = null;
			output = true;
			
		}
		catch (final SQLException e)
		{
			if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
				e.printStackTrace();
			
			LOGGER.warn("cannot updated the Global Task " + id + ": " + e.getMessage());
		}
		finally
		{
			CloseUtil.close(con);
			con = null;
		}
		
		return output;
	}
	
	public static boolean addUniqueTaskOnDB(final String task, final TaskTypes type, final String param1, final String param2, final String param3, final long lastActivation)
	{
		Connection con = null;
		
		boolean output = false;
		
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection(false);
			PreparedStatement statement = con.prepareStatement(SQL_STATEMENTS[2]);
			statement.setString(1, task);
			ResultSet rset = statement.executeQuery();
			
			if (!rset.next())
			{
				statement = con.prepareStatement(SQL_STATEMENTS[3]);
				statement.setString(1, task);
				statement.setString(2, type.toString());
				statement.setLong(3, lastActivation);
				statement.setString(4, param1);
				statement.setString(5, param2);
				statement.setString(6, param3);
				statement.execute();
			}
			
			DatabaseUtils.close(rset);
			DatabaseUtils.close(statement);
			rset = null;
			statement = null;
			
			output = true;
		}
		catch (final SQLException e)
		{
			if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
				e.printStackTrace();
			LOGGER.warn("cannot add the unique task: " + e.getMessage());
		}
		finally
		{
			CloseUtil.close(con);
		}
		
		return output;
	}
	
	public static boolean addTaskOnDB(final String task, final TaskTypes type, final String param1, final String param2, final String param3)
	{
		return addTaskOnDB(task, type, param1, param2, param3, 0);
	}
	
	public static boolean addTaskOnDB(final String task, final TaskTypes type, final String param1, final String param2, final String param3, final long lastActivation)
	{
		Connection con = null;
		
		boolean output = false;
		
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection(false);
			PreparedStatement statement = con.prepareStatement(SQL_STATEMENTS[3]);
			statement.setString(1, task);
			statement.setString(2, type.toString());
			statement.setLong(3, lastActivation);
			statement.setString(4, param1);
			statement.setString(5, param2);
			statement.setString(6, param3);
			statement.execute();
			
			DatabaseUtils.close(statement);
			statement = null;
			
			output = true;
		}
		catch (final SQLException e)
		{
			if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
				e.printStackTrace();
			LOGGER.warn("cannot add the task:  " + e.getMessage());
		}
		finally
		{
			CloseUtil.close(con);
		}
		
		return output;
	}

	/**
	 * @param executedTask
	 */
	public void removeActiveTask(ExecutedTask executedTask)
	{
		_currentTasks.remove(executedTask);
	}
}
