package de.visparu.spacerobots.game.entities.internal;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.visparu.spacerobots.game.entities.graphics.BulletGraphics;
import de.visparu.spacerobots.settings.GameInfo;
import de.visparu.spacerobots.util.Vector2D;

public final class Bullet
{
	private Vector2D position;
	
	private final double heading;
	
	private final InternalRobot shooter;
	
	public Bullet(final InternalRobot shooter, final Vector2D position, final double heading)
	{
		this.shooter  = shooter;
		this.position = position;
		this.heading  = heading;
	}
	
	public Rectangle2D getBounds()
	{
		final var bulletSize = GameInfo.BULLET_SIZE;

		final var x      = this.position.x - (bulletSize / 2);
		final var y      = this.position.y - (bulletSize / 2);
		final var width  = bulletSize;
		final var height = bulletSize;
		return new Rectangle2D.Double(x, y, width, height);
	}
	
	public double getHeading()
	{
		return this.heading;
	}
	
	public Vector2D getPosition()
	{
		return this.position;
	}
	
	public InternalRobot getShooter()
	{
		return this.shooter;
	}
	
	public void render(final Graphics2D g2d)
	{
		BulletGraphics.renderBase(g2d, this.position);
	}
	
	public void update()
	{
		final var bulletSpeed = GameInfo.BULLET_SPEED;

		final var dirX = Math.cos(Math.toRadians(this.heading));
		final var dirY = -Math.sin(Math.toRadians(this.heading));
		final var velX = dirX * bulletSpeed;
		final var velY = dirY * bulletSpeed;
		this.position = this.position.add(new Vector2D(velX, velY));
	}
}
