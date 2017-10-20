package com.l2jfrozen.netcore.util.random;

import java.util.Random;

/**
 * This class extends {@link java.util.Random} but do not compare and store atomically.<br>
 * Instead it`s using a simple volatile flag to ensure reading and storing the whole 64bit seed chunk.<br>
 * This implementation is much faster on parallel access, but may generate the same seed for 2 threads.
 * @author Forsaiken
 * @see java.util.Random
 */
public class NonAtomicRandom extends Random
{
	private static final long serialVersionUID = 1L;
	private volatile long _seed;
	
	public NonAtomicRandom()
	{
		this(++Rnd.SEED_UNIQUIFIER + System.nanoTime());
	}
	
	public NonAtomicRandom(final long seed)
	{
		setSeed(seed);
	}
	
	@Override
	public final int next(final int bits)
	{
		return (int) ((_seed = ((_seed * Rnd.MULTIPLIER) + Rnd.ADDEND) & Rnd.MASK) >>> (48 - bits));
	}
	
	@Override
	public final void setSeed(final long seed)
	{
		_seed = (seed ^ Rnd.MULTIPLIER) & Rnd.MASK;
	}
}
