package de.visparu.spacerobots.settings;

import java.awt.Dimension;

public final class GameInfo
{
	private GameInfo()
	{
		
	}
	
	public static final Dimension ARENA_BOUNDS       = new Dimension(900, 900);
	public static final double    ARENA_BOUNDS_OUTER = 200;
	
	public static final int NORMAL_PLAY_DELIMITER  = 720;
	public static final int SUDDEN_DEATH_DELIMITER = 1440;
	public static final int TIMEOUT_DELIMITER      = 90;
	
	public static final double SUDDEN_DEATH_SEVERITY_CHANGE = 1;
	
	public static final double ROBOT_SIZE     = 30.0;
	public static final double COLLISION_DRAG = 0.7;
	
	public static final double ROBOT_GAMEPLAY_MAXENERGY             = 1500.0;
	public static final double ROBOT_GAMEPLAY_STARTENERGY           = 500.0;
	public static final double ROBOT_GAMEPLAY_ENERGYREGEN           = 50.0;
	public static final double ROBOT_GAMEPLAY_MAXHEALTH             = 200.0;
	public static final double ROBOT_GAMEPLAY_DRAG                  = 0.97;
	public static final int    ROBOT_GAMEPLAY_MAXEXCEPTIONS         = 10;
	public static final double ROBOT_GAMEPLAY_WALL_DETECTION_MARGIN = 5;
	
	public static final double BULLET_SIZE   = 10.0;
	public static final double BULLET_SPEED  = 5.0;
	public static final double BULLET_DAMAGE = 10;
	
	public static final double ROCKET_SIZE            = 15.0;
	public static final double ROCKET_SPEED           = 3.5;
	public static final double ROCKET_HOMING_ACCURACY = 0.05;
	public static final double ROCKET_HOMING_RANGE    = 150;
	public static final double ROCKET_DAMAGE          = 20;
	
	public static final double ASTEROID_SIZE   = 30.0;
	public static final double ASTEROID_SPEED  = 10.0;
	public static final double ASTEROID_DAMAGE = 25.0;
	
	public static final double ENERGY_SCAN_BASE       = 30.0;                                  // To avoid spam scanning
	public static final double ENERGY_SCAN_PERUNIT    = 0.00084882636315677512410071340465342; // One 300p 30� scan per turn
	public static final double ENERGY_MOVE_BASE       = 3;                                     // To avoid spam moving
	public static final double ENERGY_MOVE_MULTIPLIER = 90;                                    // One full power move every two turns
	public static final double ENERGY_BULLET_BASE     = 850.0;                                 // A little less than two bullets per second
	public static final double ENERGY_ROCKET_BASE     = 1000.0;                                // Three rockets in two seconds with no homing ability
	public static final double ENERGY_ROCKET_HOMING   = 3.3333333333333333333333333333333;     // Maximum of 5 seconds homing
}
