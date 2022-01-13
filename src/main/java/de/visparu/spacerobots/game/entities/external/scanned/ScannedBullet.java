package de.visparu.spacerobots.game.entities.external.scanned;

import de.visparu.spacerobots.util.Vector2D;

public final class ScannedBullet
{
	/**
	 * The coordinates of the bullet relative to the upper left corner of the arena.<br/>
	 * The y-axis is inverted. Greater y values are lower on the screen.
	 */
	public final Vector2D position;
	/**
	 * The direction the bullet is traveling towards in degrees.
	 */
	public final double   heading;
	
	public ScannedBullet(final Vector2D position, final double heading)
	{
		this.position = position;
		this.heading  = heading;
	}
}
