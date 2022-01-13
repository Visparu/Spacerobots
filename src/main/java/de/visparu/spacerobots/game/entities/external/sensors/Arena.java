package de.visparu.spacerobots.game.entities.external.sensors;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

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
	 * @return the size of the arena in pixels.
	 */
	public Dimension getArenaSize()
	{
		return GameInfo.ARENA_BOUNDS;
	}
	
	/**
	 * @return the current game iteration. <br>
	 * There are exactly 30 iterations per second.
	 */
	public int getIteration()
	{
		return this.ia.getIteration();
	}
	
	/**
	 * @return the amount of robots still in the arena.
	 */
	public int getRobotCount()
	{
		return this.ia.getRobotCount();
	}
	
	/**
	 * @return a list of the names of all remaining robots.
	 */
	public List<String> getRobotNames()
	{
		List<String> robotNames = new ArrayList<>();
		for(var i = 0; i < this.ia.getRobotCount(); i++)
		{
			robotNames.add(this.ia.getRobot(i).getName());
		}
		return robotNames;
	}
}
