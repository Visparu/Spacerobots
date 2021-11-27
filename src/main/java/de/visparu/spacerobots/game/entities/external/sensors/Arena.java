package de.visparu.spacerobots.game.entities.external.sensors;

import java.awt.Dimension;

import de.visparu.spacerobots.game.InternalArena;
import de.visparu.spacerobots.settings.GameInfo;

public final class Arena
{
	private final InternalArena ia;
	
	public Arena(final InternalArena ia)
	{
		this.ia = ia;
	}
	
	/**
	 * Returns the size of the arena in pixels
	 */
	public Dimension getArenaSize()
	{
		return GameInfo.ARENA_BOUNDS;
	}
	
	/**
	 * Returns the current game iteration. <br>
	 * One second contains approximately 30 iterations
	 */
	public int getIteration()
	{
		return this.ia.getIteration();
	}
	
	/**
	 * Returns the amount of robots still in the arena
	 */
	public int getRobotCount()
	{
		return this.ia.getRobotCount();
	}
}
