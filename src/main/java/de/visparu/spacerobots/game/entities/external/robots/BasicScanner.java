package de.visparu.spacerobots.game.entities.external.robots;

import de.visparu.spacerobots.game.entities.external.Robot;

public class BasicScanner extends Robot
{
	private static final double APERTURE = 30;
	private static final double DISTANCE = 300;
	private double              heading;
	
	private boolean lock;
	
	@Override
	public String getName()
	{
		return "Basic Scanner";
	}

	@Override
	public void initialize()
	{
		this.heading = 0;
		this.lock    = false;
	}

	@Override
	public void update()
	{
		this.lock = !super.getScanner().getLastScanResult().getScannedRobots().isEmpty();
		if (super.getScanner().scan(this.heading, BasicScanner.DISTANCE, BasicScanner.APERTURE) && !this.lock)
		{
			this.heading += BasicScanner.APERTURE;
		}
	}
}
