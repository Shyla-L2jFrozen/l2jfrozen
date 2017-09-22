package com.l2jfrozen.common.util.random;

import java.util.Random;

/**
 * This class extends {@link java.util.Random} but do not compare and store atomically.<br>
 * Instead it`s using thread local ensure reading and storing the whole 64bit seed chunk.<br>
 * This implementation is the fastest, never generates the same seed for 2 threads.<br>
 * Each thread has it`s own random instance.
 * @author Forsaiken
 * @see java.util.Random
 */
public class ThreadLocalRandom extends Random
{
	private class Seed
	{
		long _seed;
		
		Seed(final long seed)
		{
			setSeed(seed);
		}
		
		final int next(final int bits)
		{
			return (int) ((_seed = ((_seed * Rnd.MULTIPLIER) + Rnd.ADDEND) & Rnd.MASK) >>> (48 - bits));
		}
		
		final void setSeed(final long seed)
		{
			_seed = (seed ^ Rnd.MULTIPLIER) & Rnd.MASK;
		}
	}
	
	private static final long serialVersionUID = 1L;
	private final ThreadLocal<Seed> _seedLocal;
	
	public ThreadLocalRandom()
	{
		_seedLocal = new ThreadLocal<Seed>()
		{
			@Override
			public final Seed initialValue()
			{
				return new Seed(++Rnd.SEED_UNIQUIFIER + System.nanoTime());
			}
		};
	}
	
	public ThreadLocalRandom(final long seed)
	{
		_seedLocal = new ThreadLocal<Seed>()
		{
			@Override
			public final Seed initialValue()
			{
				return new Seed(seed);
			}
		};
	}
	
	@Override
	public final int next(final int bits)
	{
		return _seedLocal.get().next(bits);
	}
	
	@Override
	public final void setSeed(final long seed)
	{
		if (_seedLocal != null)
		{
			_seedLocal.get().setSeed(seed);
		}
	}
}
