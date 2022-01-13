package de.visparu.spacerobots.game.entities.external.scanned;

import java.util.ArrayList;
import java.util.List;

import de.visparu.spacerobots.game.entities.internal.Asteroid;
import de.visparu.spacerobots.game.entities.internal.Bullet;
import de.visparu.spacerobots.game.entities.internal.InternalRobot;

public final class ScanResult
{
	private final List<ScannedRobot>    robots;
	private final List<ScannedBullet>   bullets;
	private final List<ScannedAsteroid> asteroids;
	
	private final int iteration;
	
	public ScanResult(final List<InternalRobot> robots, final List<Bullet> bullets, final List<Asteroid> asteroids, final int iteration)
	{
		this.iteration = iteration;
		
		this.robots    = new ArrayList<>();
		this.bullets   = new ArrayList<>();
		this.asteroids = new ArrayList<>();
		
		for (var i = 0; i < robots.size(); i++)
		{
			final var robot    = robots.get(i);
			final var name     = robot.getName();
			final var health   = robot.getHealth();
			final var energy   = robot.getEnergy();
			final var position = robot.getPosition();
			final var heading  = robot.getHeading();
			final var speed    = robot.getSpeed();
			final var scan     = new ScannedRobot(name, health, energy, position, heading, speed);
			this.robots.add(scan);
		}
		for (var i = 0; i < bullets.size(); i++)
		{
			final var bullet   = bullets.get(i);
			final var position = bullet.getPosition();
			final var heading  = bullet.getHeading();
			final var scan     = new ScannedBullet(position, heading);
			this.bullets.add(scan);
		}
		for (var i = 0; i < asteroids.size(); i++)
		{
			final var asteroid = asteroids.get(i);
			final var health   = asteroid.getHealth();
			final var position = asteroid.getPosition();
			final var heading  = asteroid.getHeading();
			final var scan     = new ScannedAsteroid(health, position, heading);
			this.asteroids.add(scan);
		}
	}
	
	/**
	 * @return the iteration in which this ScanResult was created.
	 */
	public int getIteration()
	{
		return this.iteration;
	}
	
	/**
	 * @return a list of all bullets within the scan range and their properties.
	 */
	public List<ScannedBullet> getScannedBullets()
	{
		return this.bullets;
	}
	
	/**
	 * @return a list of all robots within the scan range and their properties.
	 */
	public List<ScannedRobot> getScannedRobots()
	{
		return this.robots;
	}
	
	/**
	 * @return a list of all asteroids within the scan range and their properties.
	 */
	public List<ScannedAsteroid> getScannedAsteroids()
	{
		return this.asteroids;
	}
}
