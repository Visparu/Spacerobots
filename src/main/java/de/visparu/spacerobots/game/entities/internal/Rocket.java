package de.visparu.spacerobots.game.entities.internal;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.visparu.spacerobots.game.InternalArena;
import de.visparu.spacerobots.game.entities.graphics.RocketGraphics;
import de.visparu.spacerobots.settings.GameInfo;
import de.visparu.spacerobots.util.Vector2D;

public final class Rocket
{
	private Vector2D position;
	
	private double heading;
	
	private int homingTime;
	
	private int health;
	
	private final InternalRobot shooter;
	private final InternalArena arena;
	
	private InternalRobot target;
	
	public Rocket(final InternalRobot shooter, final InternalArena arena, final Vector2D position, final double heading, final int homingTime)
	{
		this.shooter    = shooter;
		this.arena      = arena;
		this.position   = position;
		this.heading    = heading;
		this.homingTime = homingTime;
	}
	
	public Rectangle2D getBounds()
	{
		final var rocketSize = GameInfo.ROCKET_SIZE;

		final var x      = this.position.x - (rocketSize / 2);
		final var y      = this.position.y - (rocketSize / 2);
		final var width  = rocketSize;
		final var height = rocketSize;
		return new Rectangle2D.Double(x, y, width, height);
	}
	
	public int getHealth()
	{
		return this.health;
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
		RocketGraphics.renderBase(g2d, this.position, this.target != null);
	}
	
	public void subtractHealth(final int damage)
	{
		this.health -= damage;
	}
	
	public void update()
	{
		final var homingRange    = GameInfo.ROCKET_HOMING_RANGE;
		final var homingAccuracy = GameInfo.ROCKET_HOMING_ACCURACY;
		final var rocketSpeed    = GameInfo.ROCKET_SPEED;

		final var targetDistance = this.target == null ? -1 : this.target.getPosition().getDistance(this.position);
		if ((targetDistance < 0) || (targetDistance > homingRange))
		{
			this.target = null;
			
			var closestDistance = homingRange + 1;
			for (var i = 0; i < this.arena.getRobotCount(); i++)
			{
				final var robot = this.arena.getRobot(i);
				if (robot == this.shooter)
				{
					continue;
				}
				final var tempTarget   = robot.getPosition();
				final var tempDistance = this.position.getDistance(tempTarget);
				if (tempDistance < closestDistance)
				{
					this.target     = robot;
					closestDistance = tempDistance;
				}
			}
		}
		
		if ((this.homingTime > 0) && (this.target != null))
		{
			final var targetPosition     = this.target.getPosition();
			final var toTarget           = targetPosition.sub(this.position);
			final var toTargetNormalized = toTarget.normalize();
			final var toTargetFinalized  = toTargetNormalized.mul(homingAccuracy * rocketSpeed);
			
			final var velX = Math.cos(Math.toRadians(this.heading)) * rocketSpeed;
			final var velY = Math.sin(Math.toRadians(this.heading)) * rocketSpeed;
			
			final var currentMovement = new Vector2D(velX, velY);
			
			final var nextDirection = currentMovement.add(toTargetFinalized).normalize();
			
			this.heading = nextDirection.getAngle();
			
			this.homingTime--;
		}
		
		final var dirX = Math.cos(Math.toRadians(this.heading));
		final var dirY = -Math.sin(Math.toRadians(this.heading));
		final var velX = dirX * rocketSpeed;
		final var velY = dirY * rocketSpeed;
		this.position = this.position.add(new Vector2D(velX, velY));
	}
}
