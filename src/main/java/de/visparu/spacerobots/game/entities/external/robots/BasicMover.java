package de.visparu.spacerobots.game.entities.external.robots;

import java.util.Random;

import de.visparu.spacerobots.game.entities.external.Robot;
import de.visparu.spacerobots.game.entities.external.sensors.Dashboard;

// Note that this class EXTENDS Robot. It does not implement it,
// it does not NOT extend it, it EXTENDS it. Very important.
public class BasicMover extends Robot
{

	// Declaration of all needed attributes
	private static final int    BOOST_TIME   = 30;
	private static final double ACCELERATION = 0.52;
	private int                 currentBoostTime;
	private double              heading;
	
	private Random rand;
	
	// This method is just for giving your robot a name. You can even change it during runtime.
	// Much flashy, very wow
	@Override
	public String getName()
	{
		// Set your robots name to something cool. "Basic Mover" is not cool, you can do better.
		return "Basic Mover";
	}

	// initialize is called instead of a constructor.
	// DO NOT USE A CONSTRUCTOR IN YOUR ROBOT, IT WILL NOT WORK CORRECTLY!
	@Override
	public void initialize()
	{
		// Initialization of all internal attributes
		this.currentBoostTime = 0;
		this.heading          = 0;
		this.rand             = new Random();
	}

	// this thing is executed 30 fucking times in one second
	// so pay attention to what you're doing here,
	// this will be the behavior of your robot!
	// You decide if it's a retard or not.
	// Another thing: If an exception is thrown in this method, you WILL be punished. Severely.
	@Override
	public void update()
	{
		this.updateCheckCollision();
		this.updateBoost();
	}
	
	private void updateCheckCollision()
	{
		// Check if robot collides with arena walls
		final var col = super.getDashboard().getWallCollision();
		if (col != Dashboard.WallCollision.NONE)
		{
			// Check which wall the robot collides and move in the other direction
			switch (col)
			{
				case NORTH:
					this.heading = (this.rand.nextDouble() * 90) + 225;
					break;
				case EAST:
					this.heading = (this.rand.nextDouble() * 90) + 135;
					break;
				case SOUTH:
					this.heading = (this.rand.nextDouble() * 90) + 45;
					break;
				case WEST:
					this.heading = (this.rand.nextDouble() * 90) + 315;
					break;
				case NORTHEAST:
					this.heading = (this.rand.nextDouble() * 90) + 180;
					break;
				case SOUTHEAST:
					this.heading = (this.rand.nextDouble() * 90) + 90;
					break;
				case SOUTHWEST:
					this.heading = this.rand.nextDouble() * 90;
					break;
				case NORTHWEST:
					this.heading = (this.rand.nextDouble() * 90) + 270;
					break;
				case NONE:
					break;
			}
			this.currentBoostTime = BasicMover.BOOST_TIME;
		}
	}
	
	private void updateBoost()
	{
		// Boost has ended: calculate new direction and reset boost timer
		if (this.currentBoostTime == 0)
		{
			final var position    = super.getDashboard().getPosition();
			final var arenaBounds = super.getArena().getArenaSize();
			
			final var high = position.y < (arenaBounds.height / 2);
			final var left = position.x < (arenaBounds.width / 2);
			
			if (left && !high)
			{
				this.heading = this.rand.nextDouble() * 90;
			}
			if (!left && !high)
			{
				this.heading = (this.rand.nextDouble() * 90) + 90;
			}
			if (!left && high)
			{
				this.heading = (this.rand.nextDouble() * 90) + 180;
			}
			if (left && high)
			{
				this.heading = (this.rand.nextDouble() * 90) + 270;
			}
			
			this.currentBoostTime = BasicMover.BOOST_TIME;
		}
		// Boost is still going: move farther into the movement direction and decrement timer
		else
		{
			super.getEngine().move(this.heading, BasicMover.ACCELERATION);
			this.currentBoostTime--;
		}
	}
}
