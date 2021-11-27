package de.visparu.spacerobots.game.entities.internal;

import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

import de.visparu.spacerobots.game.entities.external.Robot;
import de.visparu.spacerobots.game.entities.graphics.InternalRobotGraphics;
import de.visparu.spacerobots.settings.GameInfo;
import de.visparu.spacerobots.settings.GraphicsInfo;
import de.visparu.spacerobots.util.Vector2D;

public final class InternalRobot
{
	private Robot robot;
	
	private double   energy;
	private double   speed;
	private double   heading;
	private double   health;
	private Vector2D position;
	
	private Vector2D nextMove;
	private Arc2D    currentScan;
	private boolean  currentScanSuccess;
	
	private int exceptions;
	
	private boolean init;
	
	public InternalRobot()
	{
		this.init = false;
	}
	
	public Rectangle2D getBounds()
	{
		final var robotSize = GameInfo.ROBOT_SIZE;
		
		final var x      = this.position.x - (robotSize / 2);
		final var y      = this.position.y - (robotSize / 2);
		final var width  = robotSize;
		final var height = robotSize;
		return new Rectangle2D.Double(x, y, width, height);
	}
	
	public double getEnergy()
	{
		return this.energy;
	}
	
	public double getHeading()
	{
		return this.heading;
	}
	
	public double getHealth()
	{
		return this.health;
	}
	
	public String getName()
	{
		try
		{
			return this.robot.getName();
		}
		catch (final Exception e)
		{
			return "This absolute chad threw an exception in his getName method";
		}
	}
	
	public Vector2D getPosition()
	{
		return this.position;
	}
	
	public Robot getRobot()
	{
		return this.robot;
	}
	
	public double getSpeed()
	{
		return this.speed;
	}
	
	public boolean hasNextMove()
	{
		return this.nextMove != null;
	}
	
	public void init(final Robot robot, final Vector2D position, final double heading)
	{
		final var startEnergy = GameInfo.ROBOT_GAMEPLAY_STARTENERGY;
		final var maxHealth   = GameInfo.ROBOT_GAMEPLAY_MAXHEALTH;
		
		if (this.init)
		{
			return;
		}
		
		this.robot = robot;
		
		this.position = position;
		this.energy   = startEnergy;
		this.speed    = 0.0;
		this.heading  = heading;
		this.health   = maxHealth;
		
		this.exceptions = 0;
		
		this.init = true;
	}
	
	public void move(final double heading, final double intensity)
	{
		final var x    = Math.cos(Math.toRadians(heading)) * intensity;
		final var y    = -Math.sin(Math.toRadians(heading)) * intensity;
		final var move = new Vector2D(x, y);
		this.nextMove = move;
	}
	
	public void render(final Graphics2D g2d, final boolean drawInfo, final boolean winner)
	{
		InternalRobotGraphics.renderScan(g2d, this.currentScan, this.currentScanSuccess);
		InternalRobotGraphics.renderBody(g2d, this.position, this.heading);
		if (drawInfo)
		{
			InternalRobotGraphics.renderInfo(g2d, this.position, this.getName(), this.health, this.energy, winner);
		}
	}
	
	public void setCurrentScan(final Arc2D area, final boolean success)
	{
		this.currentScan        = area;
		this.currentScanSuccess = success;
	}
	
	public void setHeading(final double heading)
	{
		this.heading = heading;
	}
	
	public void setPosition(final Vector2D position)
	{
		this.position = position;
	}
	
	public void setSpeed(final double speed)
	{
		this.speed = speed;
	}
	
	public void subtractEnergy(final double energy)
	{
		this.energy -= energy;
	}
	
	public void subtractHealth(final double health)
	{
		this.health -= health;
	}
	
	public void update(final boolean finish)
	{
		final var drag          = GameInfo.ROBOT_GAMEPLAY_DRAG;
		final var arenaOrigin   = GraphicsInfo.ARENA_ORIGIN;
		final var arenaBounds   = GameInfo.ARENA_BOUNDS;
		final var robotSize     = GameInfo.ROBOT_SIZE;
		final var energyRegen   = GameInfo.ROBOT_GAMEPLAY_ENERGYREGEN;
		final var maxEnergy     = GameInfo.ROBOT_GAMEPLAY_MAXENERGY;
		final var maxExceptions = GameInfo.ROBOT_GAMEPLAY_MAXEXCEPTIONS;
		
		if (this.nextMove != null)
		{
			final var currentMovement    = Vector2D.getNormalizedVectorFromAngle(this.heading).mul(this.speed);
			final var nextMovementVector = currentMovement.add(this.nextMove);
			final var updatedAngle       = nextMovementVector.getAngle();
			final var updatedSpeed       = nextMovementVector.getLength();
			this.heading  = updatedAngle;
			this.speed    = updatedSpeed;
			this.nextMove = null;
		}
		
		this.speed *= drag;
		
		final var dirX = Math.cos(Math.toRadians(this.heading));
		final var dirY = Math.sin(Math.toRadians(this.heading));
		final var velX = dirX * this.speed;
		final var velY = dirY * this.speed;
		
		var newX = this.position.x + velX;
		var newY = this.position.y + velY;
		
		if (newX < (arenaOrigin.getX() + (robotSize / 2)))
		{
			newX       = arenaOrigin.getX() + (robotSize / 2);
			this.speed = 0.0;
		}
		if (newY < (arenaOrigin.getY() + (robotSize / 2)))
		{
			newY       = arenaOrigin.getY() + (robotSize / 2);
			this.speed = 0.0;
		}
		if (newX > ((arenaOrigin.getX() + arenaBounds.getWidth()) - (robotSize / 2)))
		{
			newX       = (arenaOrigin.getX() + arenaBounds.getWidth()) - (robotSize / 2);
			this.speed = 0.0;
		}
		if (newY > ((arenaOrigin.getY() + arenaBounds.getHeight()) - (robotSize / 2)))
		{
			newY       = (arenaOrigin.getY() + arenaBounds.getHeight()) - (robotSize / 2);
			this.speed = 0.0;
		}
		
		this.position = new Vector2D(newX, newY);
		
		this.energy += energyRegen;
		if (this.energy > maxEnergy)
		{
			this.energy = maxEnergy;
		}
		
		this.currentScan = null;
		
		try
		{
			this.robot.update();
		}
		catch (final Exception e)
		{
			if (!finish)
			{
				this.exceptions++;
				this.subtractHealth(this.health * (this.exceptions / (double) maxExceptions));
			}
			System.out.println("Exception occurred in Robot '" + this.getName() + "'! (" + this.exceptions + ") [" + e.getMessage() + "]");
		}
	}
}
