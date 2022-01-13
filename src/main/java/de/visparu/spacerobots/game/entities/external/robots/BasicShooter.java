package de.visparu.spacerobots.game.entities.external.robots;

import java.util.Random;

import de.visparu.spacerobots.game.entities.external.Robot;

//Note that this class extends the Robot class. This is very important and should not be touched.
public class BasicShooter extends Robot
{
	
	// Declaration of all needed attributes
	private double turnSpeed;
	private double heading;
	
	// This method is just for giving your robot a name. You can even change it during runtime.
	// Much flashy, very wow
	@Override
	public String getName()
	{
		// Set your robots name to something cool. "Basic Shooter" is not cool, you can do better.
		return "Basic Shooter";
	}
	
	// initialize() is called instead of a constructor.
	// DO NOT USE A CONSTRUCTOR IN YOUR ROBOT, IT WILL NOT WORK CORRECTLY!
	@Override
	public void initialize()
	{
		// Initialization of all internal attributes
		Random rand = new Random();
		this.turnSpeed = rand.nextInt(5) + 3.0;
		this.heading   = rand.nextInt(360);
	}
	
	// This method is executed exactly 30 times per second
	// so pay attention to what you're doing here,
	// this will be the behavior of your robot!
	// It is up to you whether it will be an idiot or not.
	// Another thing: If an uncaught exception is thrown in this method, you WILL be punished. Severely.
	@Override
	public void update()
	{
		// check if bullet can be shot and do so if possible
		if (super.getWeapons().shootBullet(this.heading))
		{
			// if bullet was shot, turn a few degrees
			this.heading += this.turnSpeed;
		}
	}
}
