package de.visparu.spacerobots.game.entities.external.scanned;

import de.visparu.spacerobots.util.Vector2D;

public final class ScannedRobot
{
	public final double health;
	public final double energy;
	
	public final Vector2D position;
	
	public ScannedRobot(final double health, final double energy, final Vector2D position)
	{
		this.health = health;
		this.energy = energy;
		
		this.position = position;
	}
}
