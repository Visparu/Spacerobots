package de.visparu.spacerobots.game.entities.external.scanned;

import de.visparu.spacerobots.util.Vector2D;

public final class ScannedAsteroid
{
	/**
	 * The amount of remaining shots necessary to break this asteroid.<br/>
	 * Every asteroid needs to be hit twice to be destroyed.
	 */
	public final int health;
	
	/**
	 * The coordinates of the asteroid relative to the upper left corner of the arena.<br/>
	 * The y-axis is inverted. Greater y values are lower on the screen.
	 */
	public final Vector2D position;
	/**
	 * The direction the asteroid is traveling towards in degrees.
	 */
	public final double   heading;
	
	public ScannedAsteroid(final int health, final Vector2D position, final double heading)
	{
		this.health = health;
		
		this.position = position;
		this.heading  = heading;
	}
}
