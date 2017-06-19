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
package com.l2jfrozen.gameserver.taskmanager.tasks;

import org.apache.log4j.Logger;

import com.l2jfrozen.CommonConfig;
import com.l2jfrozen.gameserver.model.entity.sevensigns.SevenSigns;
import com.l2jfrozen.gameserver.model.entity.sevensigns.SevenSignsFestival;
import com.l2jfrozen.util.taskmanager.ExecutedTask;
import com.l2jfrozen.util.taskmanager.Task;
import com.l2jfrozen.util.taskmanager.TaskManager;
import com.l2jfrozen.util.taskmanager.TaskTypes;

/**
 * Updates all data for the Seven Signs and Festival of Darkness engines, when time is elapsed.
 * @author Tempy
 */
public class TaskSevenSignsUpdate extends Task
{
	private final Logger LOGGER = Logger.getLogger(TaskSevenSignsUpdate.class);
	private final String NAME = "sevensignsupdate";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(final ExecutedTask task)
	{
		try
		{
			SevenSigns.getInstance().saveSevenSignsData(null, true);
			
			if (!SevenSigns.getInstance().isSealValidationPeriod())
				SevenSignsFestival.getInstance().saveFestivalData(false);
			
			LOGGER.info("[GlobalTask] SevenSigns save launched.");
		}
		catch (final Exception e)
		{
			if (CommonConfig.ENABLE_ALL_EXCEPTIONS)
				e.printStackTrace();
			
			LOGGER.warn("SevenSigns: Failed to save Seven Signs configuration: " + e);
		}
	}
	
	@Override
	public void initializate()
	{
		super.initializate();
		TaskManager.addUniqueTaskOnDB(NAME, TaskTypes.TYPE_FIXED_SHEDULED, "1800000", "1800000", "");
	}
}