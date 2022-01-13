package de.visparu.spacerobots.game.entities.external;

import de.visparu.spacerobots.game.InternalArena;
import de.visparu.spacerobots.game.entities.external.sensors.Arena;
import de.visparu.spacerobots.game.entities.external.sensors.Dashboard;
import de.visparu.spacerobots.game.entities.external.sensors.EngineController;
import de.visparu.spacerobots.game.entities.external.sensors.ScanController;
import de.visparu.spacerobots.game.entities.external.sensors.WeaponController;
import de.visparu.spacerobots.game.entities.internal.InternalRobot;

public abstract class Robot
{
	/**
	 * Instantiation method for any custom robot.<br>
	 * DO NOT CALL THIS METHOD INSIDE A ROBOT!<br>
	 * Or do, it's not going to do anything except throwing exceptions everywhere...
	 */
	public static final Robot instantiate(final Class<?> cls, final InternalRobot ir, final InternalArena ia)
	{
		Robot newRobot = null;
		try
		{
			newRobot = (Robot) cls.getConstructor().newInstance();
			newRobot.init(ir, ia);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return newRobot;
	}
	
	private ScanController   sc;
	private WeaponController wc;
	private EngineController ec;
	private Dashboard        db;
	private Arena            a;
	
	private boolean init = false;
	
	/**
	 * @return the arena information component of this robot.
	 */
	protected final Arena getArena()
	{
		return this.a;
	}
	
	/**
	 * @return the information component of this robot.
	 */
	protected final Dashboard getDashboard()
	{
		return this.db;
	}
	
	/**
	 * @return the engine component of this robot.
	 */
	protected final EngineController getEngine()
	{
		return this.ec;
	}
	
	/**
	 * @return the robot's name.
	 */
	public abstract String getName();
	
	/**
	 * @return the scanner component of this robot.
	 */
	protected final ScanController getScanner()
	{
		return this.sc;
	}
	
	/**
	 * @return the weapon component of this robot.
	 */
	protected final WeaponController getWeapons()
	{
		return this.wc;
	}
	
	/**
	 * Initialization method for any custom robot.<br>
	 * DO NOT CALL THIS METHOD FROM INSIDE ANY ROBOT<br>
	 * You're just going to get a load of exceptions.
	 */
	public final void init(final InternalRobot ir, final InternalArena ia)
	{
		if (this.init)
		{
			return;
		}
		this.init = true;
		
		this.sc = new ScanController(ir, ia);
		this.wc = new WeaponController(ir, ia);
		this.ec = new EngineController(ir);
		this.db = new Dashboard(ir);
		this.a  = new Arena(ia);
	}

	/**
	 * Called when a robot is first initialized.<br>
	 * REPLACES THE CONSTRUCTOR OF A ROBOT, DO NOT USE A CONSTRUCTOR!!!
	 */
	public abstract void initialize();

	/**
	 * Called once every iteration (30 times per second).
	 */
	public abstract void update();
}
