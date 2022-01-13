package de.visparu.spacerobots.game.entities.internal;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.visparu.spacerobots.game.entities.graphics.AsteroidGraphics;
import de.visparu.spacerobots.settings.GameInfo;
import de.visparu.spacerobots.util.Vector2D;

public final class Asteroid
{
	private Vector2D position;
	
	private final double heading;
	
	private boolean killed;
	private int     timeSinceKill;
	
	private int health;
	
	public Asteroid(final Vector2D position, final double heading)
	{
		this.position = position;
		this.heading  = heading;
		this.killed   = false;
		this.health   = 2;
	}
	
	public Rectangle2D getBounds()
	{
		final var asteroidSize = GameInfo.ASTEROID_SIZE;
		
		final var x      = this.position.x - (asteroidSize / 2);
		final var y      = this.position.y - (asteroidSize / 2);
		final var width  = asteroidSize;
		final var height = asteroidSize;
		return new Rectangle2D.Double(x, y, width, height);
	}
	
	public Vector2D getPosition()
	{
		return this.position;
	}
	
	public double getHeading()
	{
		return this.heading;
	}
	
	public int getTimeSinceKill()
	{
		return this.timeSinceKill;
	}
	
	public int getHealth()
	{
		return this.health;
	}
	
	public void hit()
	{
		this.health--;
		if (this.health <= 0)
		{
			this.kill();
		}
	}
	
	public void kill()
	{
		this.killed = true;
	}
	
	public void render(final Graphics2D g2d)
	{
		AsteroidGraphics.renderTail(g2d, this.position, this.heading, this.timeSinceKill);
		if (!this.killed)
		{
			AsteroidGraphics.renderBase(g2d, this.position);
		}
	}
	
	public void update()
	{
		final var asteroidSpeed = GameInfo.ASTEROID_SPEED;
		
		if (!this.killed)
		{
			final var dirX = Math.cos(Math.toRadians(this.heading));
			final var dirY = -Math.sin(Math.toRadians(this.heading));
			final var velX = dirX * asteroidSpeed;
			final var velY = dirY * asteroidSpeed;
			this.position = this.position.add(new Vector2D(velX, velY));
		}
		else
		{
			this.timeSinceKill++;
		}
	}
}
