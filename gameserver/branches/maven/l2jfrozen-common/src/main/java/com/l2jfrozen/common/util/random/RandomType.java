/*
 * Copyright (C) 2004-2016 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jfrozen.common.util.random;

/**
 * @author Forsaiken
 */
public enum RandomType
{
	/**
	 * For best random quality.
	 * @see java.security.SecureRandom
	 */
	SECURE,
	
	/**
	 * For average random quality.
	 * @see java.util.Random
	 */
	UNSECURE_ATOMIC,
	
	/**
	 * Each thread has it`s own random instance.<br>
	 * Provides best parallel access speed.
	 */
	UNSECURE_THREAD_LOCAL,
	
	/**
	 * Provides much faster parallel access speed.
	 */
	UNSECURE_VOLATILE
}
