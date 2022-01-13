package de.visparu.spacerobots.game.entities.external.scanned;

import de.visparu.spacerobots.util.Vector2D;

public final class ScannedRobot
{
	/**
	 * The name of the scanned robot.
	 */
	public final String name;
	
	/**
	 * The remaining health of the scanned robot.
	 */
	public final double health;
	/**
	 * The currently available energy of the scanned robot.
	 */
	public final double energy;
	
	/**
	 * The position of the scanned robot relative to the arena. This designates the center of the robot's sprite.
	 * The y-axis is inverted. Greater y values are lower on the screen.
	 */
	public final Vector2D position;
	/**
	 * The direction the scanned robot is currently traveling towards in degrees.
	 */
	public final double heading;
	/**
	 * The speed the scanned robot is currently traveling at.
	 */
	public final double speed;
	
	public ScannedRobot(final String name, final double health, final double energy, final Vector2D position, double heading, double speed)
	{
		this.name = name;
		
		this.health = health;
		this.energy = energy;
		
		this.position = position;
		this.heading = heading;
		this.speed = speed;
	}
}
