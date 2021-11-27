package de.visparu.spacerobots.game.entities.external.robots;

import de.visparu.spacerobots.game.entities.external.Robot;

//Note that this class EXTENDS Robot. It does not implement it,
//it does not NOT extend it, it EXTENDS it. Very important.
public class BasicShooter extends Robot
{

	// Declaration of all needed attributes
	private static final double TURN_SPEED = 5;
	private double              heading;
	
	// This method is just for giving your robot a name. You can even change it during runtime.
	// Much flashy, very wow
	@Override
	public String getName()
	{
		// Set your robots name to something cool. "Basic Shooter" is not cool, you can do better.
		return "Basic Shooter";
	}

	// initialize is called instead of a constructor.
	// DO NOT USE A CONSTRUCTOR IN YOUR ROBOT, IT WILL NOT WORK CORRECTLY!
	@Override
	public void initialize()
	{
		// Initialization of all internal attributes
		this.heading = 0;
	}

	// this thing is executed 30 fucking times in one second
	// so pay attention to what you're doing here,
	// this will be the behavior of your robot!
	// You decide if it's a retard or not.
	// Another thing: If an exception is thrown in this method, you WILL be punished. Severely.
	@Override
	public void update()
	{
		// check if bullet can be shot and do so if possible
		if (super.getWeapons().shootBullet(this.heading))
		{
			// if bullet was shot, turn a few degrees
			this.heading += BasicShooter.TURN_SPEED;
		}
	}
}
