package de.visparu.spacerobots.game.entities.external.sensors;

import de.visparu.spacerobots.game.entities.internal.InternalRobot;
import de.visparu.spacerobots.settings.GameInfo;
import de.visparu.spacerobots.settings.GraphicsInfo;
import de.visparu.spacerobots.util.Vector2D;

public final class Dashboard
{
	public enum WallCollision
	{
		NONE,
		NORTH,
		NORTHEAST,
		EAST,
		SOUTHEAST,
		SOUTH,
		SOUTHWEST,
		WEST,
		NORTHWEST
	}
	
	private final InternalRobot ir;
	
	public Dashboard(final InternalRobot ir)
	{
		this.ir = ir;
	}
	
	/**
	 * Returns the current energy of your robot.<br>
	 * Energy is needed for every action your robot performs.
	 */
	public double getEnergy()
	{
		return this.ir.getEnergy();
	}
	
	/**
	 * Returns the current heading of your robot in degrees<br>
	 * 0� = east<br>
	 * 90� = north<br>
	 * 180� = west<br>
	 * 270� = south<br>
	 */
	public double getHeading()
	{
		return this.ir.getHeading();
	}
	
	/**
	 * Returns the current health of your robot.<br>
	 * If your robot's health drops to 0, it is destroyed.
	 */
	public double getHealth()
	{
		return this.ir.getHealth();
	}
	
	/**
	 * Returns the current position of your robot's center point in the arena.<br>
	 * Top Left: 0, 0<br>
	 * Top Right: arenaWidth, 0<br>
	 * Bottom Left: 0, arenaHeight<br>
	 * Bottom Right: arenaWidth, arenaHeight<br>
	 */
	public Vector2D getPosition()
	{
		final var arenaOrigin = GraphicsInfo.ARENA_ORIGIN;

		return this.ir.getPosition().sub(new Vector2D(arenaOrigin));
	}
	
	/**
	 * Returns the current speed of your robot in pixels/second
	 */
	public double getSpeed()
	{
		return this.ir.getSpeed();
	}
	
	/**
	 * Returns a value indicating if your robot is currently hitting a wall
	 *
	 * @return a "WallCollision" object
	 */
	public WallCollision getWallCollision()
	{
		final var robotSize = GameInfo.ROBOT_SIZE;
		final var margin    = GameInfo.ROBOT_GAMEPLAY_WALL_DETECTION_MARGIN;
		final var arenaSize = GameInfo.ARENA_BOUNDS;

		final var x = this.getPosition().x;
		final var y = this.getPosition().y;

		final var north = y <= ((robotSize / 2) + margin);
		final var east  = x >= (arenaSize.width - (robotSize / 2) - margin);
		final var south = y >= (arenaSize.height - (robotSize / 2) - margin);
		final var west  = x <= ((robotSize / 2) + margin);
		
		if (north && east)
		{
			return WallCollision.NORTHEAST;
		}
		if (north && west)
		{
			return WallCollision.NORTHWEST;
		}
		if (south && west)
		{
			return WallCollision.SOUTHWEST;
		}
		if (south && east)
		{
			return WallCollision.SOUTHEAST;
		}
		if (north)
		{
			return WallCollision.NORTH;
		}
		if (east)
		{
			return WallCollision.EAST;
		}
		if (south)
		{
			return WallCollision.SOUTH;
		}
		if (west)
		{
			return WallCollision.WEST;
		}
		return WallCollision.NONE;
	}
}
