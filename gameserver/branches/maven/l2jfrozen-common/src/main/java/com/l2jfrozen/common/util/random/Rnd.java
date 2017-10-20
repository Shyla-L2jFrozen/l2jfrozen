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

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Forsaiken
 */
public final class Rnd
{
	
	public static final long ADDEND = 0xBL;
	
	public static final long MASK = (1L << 48) - 1;
	
	public static final long MULTIPLIER = 0x5DEECE66DL;
	
	public static volatile long SEED_UNIQUIFIER = 8682522807148012L;
	
	public static final RandomContainer newInstance(final RandomType type)
	{
		switch (type)
		{
			case UNSECURE_ATOMIC:
				return new RandomContainer(new Random());
			
			case UNSECURE_VOLATILE:
				return new RandomContainer(new NonAtomicRandom());
			
			case UNSECURE_THREAD_LOCAL:
				return new RandomContainer(new ThreadLocalRandom());
			
			case SECURE:
				return new RandomContainer(new SecureRandom());
		}
		
		throw new IllegalArgumentException();
	}
	
	private static final RandomContainer rnd = newInstance(RandomType.UNSECURE_THREAD_LOCAL);
	
	public static final Random directRandom()
	{
		return rnd.directRandom();
	}
	
	/**
	 * Get a random double number from 0 to 1
	 * @return A random double number from 0 to 1
	 */
	public static final double get()
	{
		return rnd.nextDouble();
	}
	
	/**
	 * Gets a random integer number from 0(inclusive) to n(exclusive)
	 * @param n The superior limit (exclusive)
	 * @return A random integer number from 0 to n-1
	 */
	public static final int get(final int n)
	{
		return rnd.get(n);
	}
	
	/**
	 * Gets a random integer number from min(inclusive) to max(inclusive)
	 * @param min The minimum value
	 * @param max The maximum value
	 * @return A random integer number from min to max
	 */
	public static final int get(final int min, final int max)
	{
		return rnd.get(min, max);
	}
	
	/**
	 * Gets a random long number from min(inclusive) to max(inclusive)
	 * @param min The minimum value
	 * @param max The maximum value
	 * @return A random long number from min to max
	 */
	public static final long get(final long min, final long max)
	{
		return rnd.get(min, max);
	}
	
	/**
	 * Get a random boolean state (true or false)
	 * @return A random boolean state (true or false)
	 * @see java.util.Random#nextBoolean()
	 */
	public static final boolean nextBoolean()
	{
		return rnd.nextBoolean();
	}
	
	/**
	 * Fill the given array with random byte numbers from Byte.MIN_VALUE(inclusive) to Byte.MAX_VALUE(inclusive)
	 * @param array The array to be filled with random byte numbers
	 * @see java.util.Random#nextBytes(byte[] bytes)
	 */
	public static final void nextBytes(final byte[] array)
	{
		rnd.nextBytes(array);
	}
	
	/**
	 * Get a random double number from 0 to 1
	 * @return A random double number from 0 to 1
	 * @see java.util.Random#nextDouble()
	 */
	public static final double nextDouble()
	{
		return rnd.nextDouble();
	}
	
	/**
	 * Get a random float number from 0 to 1
	 * @return A random integer number from 0 to 1
	 * @see java.util.Random#nextFloat()
	 */
	public static final float nextFloat()
	{
		return rnd.nextFloat();
	}
	
	/**
	 * Get a random gaussian double number from 0 to 1
	 * @return A random gaussian double number from 0 to 1
	 * @see java.util.Random#nextGaussian()
	 */
	public static final double nextGaussian()
	{
		return rnd.nextGaussian();
	}
	
	/**
	 * Get a random integer number from Integer.MIN_VALUE(inclusive) to Integer.MAX_VALUE(inclusive)
	 * @return A random integer number from Integer.MIN_VALUE to Integer.MAX_VALUE
	 * @see java.util.Random#nextInt()
	 */
	public static final int nextInt()
	{
		return rnd.nextInt();
	}
	
	/**
	 * @param n
	 * @return
	 */
	public static final int nextInt(final int n)
	{
		return get(n);
	}
	
	/**
	 * Get a random long number from Long.MIN_VALUE(inclusive) to Long.MAX_VALUE(inclusive)
	 * @return A random integer number from Long.MIN_VALUE to Long.MAX_VALUE
	 * @see java.util.Random#nextLong()
	 */
	public static final long nextLong()
	{
		return rnd.nextLong();
	}
}
