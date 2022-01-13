package de.visparu.spacerobots.game.entities.external.robots;

import de.visparu.spacerobots.game.entities.external.Robot;

//Note that this class extends the Robot class. This is very important and should not be touched.
public class BasicScanner extends Robot
{
	// Declaration of all needed constants
	private static final double APERTURE = 30;
	private static final double DISTANCE = 300;
	
	// Declaration of all needed attributes
	private double heading;
	
	private boolean lock;
	
	// This method is just for giving your robot a name. You can even change it during runtime.
	// Much flashy, very wow
	@Override
	public String getName()
	{
		// Set your robots name to something cool. "Basic Scanner" is not cool, you can do better.
		return "Basic Scanner";
	}
	
	// initialize() is called instead of a constructor.
	// DO NOT USE A CONSTRUCTOR IN YOUR ROBOT, IT WILL NOT WORK CORRECTLY!
	@Override
	public void initialize()
	{
		// Initialization of all internal attributes
		this.heading = 0;
		this.lock    = false;
	}
	
	// This method is executed exactly 30 times per second
	// so pay attention to what you're doing here,
	// this will be the behavior of your robot!
	// It is up to you whether it will be an idiot or not.
	// Another thing: If an uncaught exception is thrown in this method, you WILL be punished. Severely.
	@Override
	public void update()
	{
		// Check whether an enemy robot has been scanned during the last iteration
		this.lock = !super.getScanner().getLastScanResult().getScannedRobots().isEmpty();
		if (super.getScanner().scan(this.heading, BasicScanner.DISTANCE, BasicScanner.APERTURE) && !this.lock)
		{
			// If no robot could be found, rotate the next scan a bit
			this.heading += BasicScanner.APERTURE;
		}
	}
}
