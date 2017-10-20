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
package com.l2jfrozen.common.util.random;

import java.util.Random;

/**
 * @author Forsaiken
 */
public class RandomContainer
{
	private final Random _random;
	
	protected RandomContainer(final Random random)
	{
		_random = random;
	}
	
	public final Random directRandom()
	{
		return _random;
	}
	
	/**
	 * Get a random double number from 0 to 1
	 * @return A random double number from 0 to 1
	 */
	public final double get()
	{
		return _random.nextDouble();
	}
	
	/**
	 * Gets a random integer number from 0(inclusive) to n(exclusive)
	 * @param n The superior limit (exclusive)
	 * @return A random integer number from 0 to n-1
	 */
	public final int get(final int n)
	{
		return (int) (_random.nextDouble() * n);
	}
	
	/**
	 * Gets a random integer number from min(inclusive) to max(inclusive)
	 * @param min The minimum value
	 * @param max The maximum value
	 * @return A random integer number from min to max
	 */
	public final int get(final int min, final int max)
	{
		return min + (int) (_random.nextDouble() * ((max - min) + 1));
	}
	
	/**
	 * Gets a random long number from min(inclusive) to max(inclusive)
	 * @param min The minimum value
	 * @param max The maximum value
	 * @return A random long number from min to max
	 */
	public final long get(final long min, final long max)
	{
		return min + (long) (_random.nextDouble() * ((max - min) + 1));
	}
	
	/**
	 * Get a random boolean state (true or false)
	 * @return A random boolean state (true or false)
	 * @see java.util.Random#nextBoolean()
	 */
	public final boolean nextBoolean()
	{
		return _random.nextBoolean();
	}
	
	/**
	 * Fill the given array with random byte numbers from Byte.MIN_VALUE(inclusive) to Byte.MAX_VALUE(inclusive)
	 * @param array The array to be filled with random byte numbers
	 * @see java.util.Random#nextBytes(byte[] bytes)
	 */
	public final void nextBytes(final byte[] array)
	{
		_random.nextBytes(array);
	}
	
	/**
	 * Get a random double number from 0 to 1
	 * @return A random double number from 0 to 1
	 * @see java.util.Random#nextDouble()
	 */
	public final double nextDouble()
	{
		return _random.nextDouble();
	}
	
	/**
	 * Get a random float number from 0 to 1
	 * @return A random integer number from 0 to 1
	 * @see java.util.Random#nextFloat()
	 */
	public final float nextFloat()
	{
		return _random.nextFloat();
	}
	
	/**
	 * Get a random gaussian double number from 0 to 1
	 * @return A random gaussian double number from 0 to 1
	 * @see java.util.Random#nextGaussian()
	 */
	public final double nextGaussian()
	{
		return _random.nextGaussian();
	}
	
	/**
	 * Get a random integer number from Integer.MIN_VALUE(inclusive) to Integer.MAX_VALUE(inclusive)
	 * @return A random integer number from Integer.MIN_VALUE to Integer.MAX_VALUE
	 * @see java.util.Random#nextInt()
	 */
	public final int nextInt()
	{
		return _random.nextInt();
	}
	
	/**
	 * Get a random long number from Long.MIN_VALUE(inclusive) to Long.MAX_VALUE(inclusive)
	 * @return A random integer number from Long.MIN_VALUE to Long.MAX_VALUE
	 * @see java.util.Random#nextLong()
	 */
	public final long nextLong()
	{
		return _random.nextLong();
	}
}
