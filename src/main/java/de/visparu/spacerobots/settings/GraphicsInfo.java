package de.visparu.spacerobots.settings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;

public final class GraphicsInfo
{
	private GraphicsInfo()
	{
		
	}
	
	public static final String FRAME_TITLE    = "KI Competition";
	public static final int    CANVAS_BUFFERS = 2;
	
	public static final Color MENU_BACKGROUND = new Color(0, 0, 0, 0.8F);
	
	public static final int   MENU_PANEL_SIZE_SPACER_CONTENT = 10;
	public static final int   MENU_PANEL_SIZE_CONTENT        = 150;
	public static final int   MENU_PANEL_SIZE_SPACER_MARGIN  = 20;
	public static final Color MENU_PANEL_COLOR_BORDER        = new Color(255, 255, 255);
	public static final Color MENU_PANEL_COLOR_BACKGROUND    = new Color(0.0F, 1.0F, 1.0F, 0.2F);
	
	public static final int   MENU_CONTROLS_DISTANCE     = 5;
	public static final Color MENU_CONTROLS_BACKGROUND   = new Color(0, 100, 100);
	public static final Color MENU_CONTROLS_BORDER       = new Color(0, 200, 200);
	public static final int   MENU_CONTROLS_BORDER_WIDTH = 4;
	
	public static final int   MENU_INFORMATION_SUDDEN_DEATH_TIMER_WIDTH                = 900;
	public static final Color MENU_INFORMATION_SUDDEN_DEATH_TIMER_BACKGROUND_IDLE      = new Color(0, 200, 0);
	public static final Color MENU_INFORMATION_SUDDEN_DEATH_TIMER_BACKGROUND_WARNING   = new Color(200, 200, 0);
	public static final Color MENU_INFORMATION_SUDDEN_DEATH_TIMER_BACKGROUND_CRITICAL  = new Color(200, 0, 0);
	public static final Color MENU_INFORMATION_SUDDEN_DEATH_TIMER_BACKGROUND_COUNTDOWN = new Color(0, 100, 200);
	public static final Color MENU_INFORMATION_SUDDEN_DEATH_TIMER_BACKGROUND_SUPERNOVA = new Color(255, 0, 255);
	public static final Color MENU_INFORMATION_SUDDEN_DEATH_TIMER_BORDER               = new Color(255, 255, 255);
	
	public static final int   MENU_START_HEIGHT     = 100;
	public static final int   MENU_START_MARGIN     = 10;
	public static final int   MENU_START_TEXT_SIZE  = 40;
	public static final Color MENU_START_TEXT_COLOR = new Color(255, 255, 255);
	public static final Color MENU_START_OUTER      = new Color(255, 125, 0);
	public static final Color MENU_START_INNER      = new Color(255, 175, 0);
	
	public static final int   MENU_ROBOT_PREVIEW_TEXT_DISTANCE    = 10;
	public static final int   MENU_ROBOT_PREVIEW_SLOT_DISTANCE    = 10;
	public static final int   MENU_ROBOT_PREVIEW_SLOT_SIZE        = 70;
	public static final int   MENU_ROBOT_PREVIEW_TEXT_SIZE        = 12;
	public static final Color MENU_ROBOT_PREVIEW_TEXT_COLOR       = new Color(255, 255, 255);
	public static final Color MENU_ROBOT_PREVIEW_COLOR_BORDER     = new Color(255, 255, 255);
	public static final Color MENU_ROBOT_PREVIEW_COLOR_BACKGROUND = new Color(0.0F, 0.0F, 0.0F, 0.7F);
	
	public static final double MENU_ROBOT_PREVIEW_SPIN_SPEED = 2.0;
	
	public static final Point2D ARENA_ORIGIN           = new Point2D.Double(50, 50);
	public static final int     ARENA_COUNTDOWN_AMOUNT = 5;
	public static final int     ARENA_COUNTDOWN_TIME   = 100;
	public static final int     ARENA_STARTTEXT_TIME   = 3;
	
	public static final int   ARENA_WINNER_ANNOUNCEMENT_TEXT_SIZE = 70;
	public static final int   ARENA_WINNER_ANNOUNCEMENT_BUILDUP   = 3;
	public static final Color ARENA_WINNER_ANNOUNCEMENT_COLOR     = new Color(255, 255, 255);
	
	public static final int    ARENA_STAR_COUNT                   = 200;
	public static final int    ARENA_STAR_SIZE_MAX                = 3;
	public static final double ARENA_STAR_BRIGHTNESS_VARIANCE     = 0.3;
	public static final int    ARENA_STAR_BRIGHTNESS_CHANGE_DELAY = 7;
	public static final Color  ARENA_STAR_COLOR                   = new Color(230, 230, 255);
	
	public static final Dimension CANVAS_DIMENSION_BOUNDS = new Dimension(1000, 1000);
	public static final Color     CANVAS_COLOR_BACKGROUND = new Color(10, 15, 20);
	
	public static final Color ARENA_COLOR_OUTLINE   = new Color(50, 230, 255);
	public static final Color ARENA_COLOR_COUNTDOWN = new Color(255, 255, 200);
	public static final int   ARENA_SIZE_COUNTDOWN  = 2000;
	public static final int   ARENA_SIZE_STARTTEXT  = 100;
	
	public static final double ROBOT_ANGULAR_EXTEND                  = 38.0;
	public static final Color  ROBOT_COLOR_BASE                      = new Color(150, 0, 0);
	public static final Color  ROBOT_COLOR_TURRET                    = new Color(100, 0, 0);
	public static final int    ROBOT_NAME_SIZE                       = 12;
	public static final int    ROBOT_WINNER_CROWN_DISTANCE           = 5;
	public static final int    ROBOT_WINNER_CROWN_WIDTH              = 30;
	public static final int    ROBOT_WINNER_CROWN_HEIGHT             = 17;
	public static final int    ROBOT_WINNER_CROWN_SPIKES             = 3;
	public static final int    ROBOT_WINNER_CROWN_SPIKE_SIZE         = 6;
	public static final Color  ROBOT_WINNER_CROWN_COLOR              = new Color(255, 255, 0);
	public static final Color  ROBOT_WINNER_CROWN_SPIKE_COLOR        = new Color(0, 150, 255);
	public static final int    ROBOT_ENERGY_SIZE                     = 12;
	public static final int    ROBOT_INFO_BARS_WIDTH                 = 40;
	public static final int    ROBOT_INFO_BARS_HEIGHT                = 5;
	public static final double ROBOT_INFO_BARS_HEALTH_THRESHOLD_HALF = 0.5;
	public static final double ROBOT_INFO_BARS_HEALTH_THRESHOLD_LOW  = 0.25;
	public static final Color  ROBOT_INFO_BARS_HEALTH_COLOR_FULL     = new Color(0, 200, 0);
	public static final Color  ROBOT_INFO_BARS_HEALTH_COLOR_HALF     = new Color(200, 200, 0);
	public static final Color  ROBOT_INFO_BARS_HEALTH_COLOR_LOW      = new Color(200, 0, 0);
	public static final Color  ROBOT_INFO_BARS_ENERGY_COLOR          = new Color(0, 150, 200);
	
	public static final Color BULLET_COLOR_BASE = new Color(0, 200, 200);
	
	public static final Color ROCKET_COLOR_BASE          = new Color(200, 150, 0);
	public static final Color ROCKET_COLOR_RANGE_FAILURE = new Color(230, 0, 0);
	public static final Color ROCKET_COLOR_RANGE_SUCCESS = new Color(0, 150, 230);
	
	public static final Color  ASTEROID_COLOR_BASE   = new Color(80, 0, 0);
	public static final Color  ASTEROID_COLOR_TAIL   = new Color(0.6F, 0.5F, 0.3F, 0.6F);
	public static final int    ASTEROID_COLOR_LAYERS = 10;
	public static final double ASTEROID_TAIL_LENGTH  = 200;
	public static final int    ASTEROID_KILL_TIME    = 5;
	
	public static final double SUPERNOVA_SCREEN_SHAKE_MAX_INTENSITY = 30.0;
	public static final double SUPERNOVA_EXPLOSION_MAX_SIZE         = 2500.0;
	public static final int    SUPERNOVA_EXPLOSION_LAYERS           = 24;
	public static final double SUPERNOVA_EXPLOSION_DECAY            = 0.3;
	public static final Color  SUPERNOVA_EXPLOSION_COLOR            = new Color(0.2F, 0.0F, 0.2F);
	public static final Color  SUPERNOVA_EXPLOSION_INNER_COLOR      = new Color(255, 255, 255);
	public static final Color  SUPERNOVA_DESTRUCTION_COLOR          = new Color(25, 25, 25);
	public static final double SUPERNOVA_DESTRUCTION_DECAY_TIME     = 0.2;
	
	public static final Color SCAN_COLOR_FAILURE = new Color(230, 0, 0);
	public static final Color SCAN_COLOR_SUCCESS = new Color(0, 150, 230);
	
	public static final Color  EXPLOSION_COLOR_OUTER       = new Color(230, 200, 0);
	public static final int    EXPLOSION_BULLET_SIZE_MAX   = 12;
	public static final int    EXPLOSION_BULLET_LAYERS     = 3;
	public static final int    EXPLOSION_BULLET_LIFETIME   = 4;
	public static final double EXPLOSION_BULLET_DECAY      = 2.0;
	public static final int    EXPLOSION_ROCKET_SIZE_MAX   = 15;
	public static final int    EXPLOSION_ROCKET_LAYERS     = 3;
	public static final int    EXPLOSION_ROCKET_LIFETIME   = 5;
	public static final double EXPLOSION_ROCKET_DECAY      = 2.0;
	public static final int    EXPLOSION_ROBOT_SIZE_MAX    = 60;
	public static final int    EXPLOSION_ROBOT_LAYERS      = 5;
	public static final int    EXPLOSION_ROBOT_LIFETIME    = 5;
	public static final double EXPLOSION_ROBOT_DECAY       = 1.1;
	public static final int    EXPLOSION_ASTEROID_SIZE_MAX = 20;
	public static final int    EXPLOSION_ASTEROID_LAYERS   = 5;
	public static final int    EXPLOSION_ASTEROID_LIFETIME = 5;
	public static final double EXPLOSION_ASTEROID_DECAY    = 2.0;
}
