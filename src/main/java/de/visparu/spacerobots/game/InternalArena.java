package de.visparu.spacerobots.game;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.visparu.spacerobots.game.entities.external.Robot;
import de.visparu.spacerobots.game.entities.graphics.InternalArenaGraphics;
import de.visparu.spacerobots.game.entities.graphics.particles.Explosion;
import de.visparu.spacerobots.game.entities.internal.Asteroid;
import de.visparu.spacerobots.game.entities.internal.Bullet;
import de.visparu.spacerobots.game.entities.internal.InternalRobot;
import de.visparu.spacerobots.game.entities.internal.Rocket;
import de.visparu.spacerobots.gui.MainMenu;
import de.visparu.spacerobots.settings.GameInfo;
import de.visparu.spacerobots.settings.GraphicsInfo;
import de.visparu.spacerobots.util.Vector2D;

public final class InternalArena
{
	private boolean init;
	
	private List<InternalRobot> robots;
	private List<Bullet>        bullets;
	private List<Rocket>        rockets;
	private List<Asteroid>      asteroids;
	
	private List<Explosion> explosions;
	
	private Vector2D[] stars;
	private int[]      starSizes;
	private double[]   starBrightness;
	
	private String winnerText;
	private String winnerName;
	private int    winnerIteration;
	
	private int iteration;
	
	private Random rand;
	
	private double suddenDeathSeverity;
	private int    superNovaIndex;
	private int    currentShakeX;
	private int    currentShakeY;
	
	public InternalArena()
	{
		this.init = false;
	}
	
	public void addBullet(final Bullet bullet)
	{
		this.bullets.add(bullet);
	}
	
	public void addRobot(final String name, final Vector2D position, final double heading)
	{
		try
		{
			final Class<?> cls      = Class.forName("de.visparu.spacerobots.game.entities.external.robots." + name);
			final var      ir       = new InternalRobot();
			final var      newRobot = Robot.instantiate(cls, ir, this);
			if (newRobot != null)
			{
				ir.init(newRobot, position, heading);
				try
				{
					newRobot.initialize();
				}
				catch (final Exception e)
				{
					ir.subtractHealth(ir.getHealth());
					System.out.println("Exception occurred during initialization of Robot '" + ir.getName() + "'! RIP");
					e.printStackTrace();
				}
				this.robots.add(ir);
			}
		}
		catch (final ClassNotFoundException e)
		{
			System.err.println("Class " + name + " not found!\n");
			e.printStackTrace();
		}
		catch (final SecurityException e)
		{
			e.printStackTrace();
		}
	}
	
	public void addRocket(final Rocket rocket)
	{
		this.rockets.add(rocket);
	}
	
	private void calculateCollisions()
	{
		final var bulletDamage   = GameInfo.BULLET_DAMAGE;
		final var rocketDamage   = GameInfo.ROCKET_DAMAGE;
		final var asteroidDamage = GameInfo.ASTEROID_DAMAGE;
		
		final var collisionDrag = GameInfo.COLLISION_DRAG;
		
		final var arenaOrigin      = GraphicsInfo.ARENA_ORIGIN;
		final var arenaBounds      = GameInfo.ARENA_BOUNDS;
		final var arenaOuterBounds = GameInfo.ARENA_BOUNDS_OUTER;

		for (var i = 0; i < this.robots.size(); i++)
		{
			final var robot = this.robots.get(i);
			for (var j = 0; j < this.bullets.size(); j++)
			{
				final var bullet = this.bullets.get(j);
				if (bullet.getShooter() == robot)
				{
					continue;
				}
				if (robot.getBounds().intersects(bullet.getBounds()))
				{
					this.explosions.add(Explosion.bullet(bullet.getPosition()));
					this.bullets.remove(j);
					j--;
					if (this.winnerIteration == -1)
					{
						robot.subtractHealth(bulletDamage);
					}
				}
			}
			for (var j = 0; j < this.rockets.size(); j++)
			{
				final var rocket = this.rockets.get(j);
				if (rocket.getShooter() == robot)
				{
					continue;
				}
				if (robot.getBounds().intersects(rocket.getBounds()))
				{
					this.explosions.add(Explosion.rocket(rocket.getPosition()));
					this.rockets.remove(j);
					j--;
					if (this.winnerIteration == -1)
					{
						robot.subtractHealth(rocketDamage);
					}
				}
			}
			for (final InternalRobot robot2 : this.robots)
			{
				if (robot == robot2)
				{
					continue;
				}
				if (robot.getBounds().intersects(robot2.getBounds()))
				{
					final var collisionVector = robot2.getPosition().sub(robot.getPosition()).div(4);
					robot.setPosition(new Vector2D(robot.getPosition().add(collisionVector.mul(-1))));
					robot2.setPosition(new Vector2D(robot2.getPosition().add(collisionVector)));
					robot.setHeading(collisionVector.getAngle() + 180);
					robot2.setHeading(collisionVector.getAngle());
					robot.setSpeed(robot2.getSpeed() * collisionDrag);
					robot2.setSpeed(robot.getSpeed() * collisionDrag);
				}
			}
			for (var j = 0; j < this.asteroids.size(); j++)
			{
				final var asteroid = this.asteroids.get(j);
				if (asteroid.getTimeSinceKill() > 0)
				{
					continue;
				}
				if (robot.getBounds().intersects(asteroid.getBounds()))
				{
					this.explosions.add(Explosion.asteroid(asteroid.getPosition()));
					asteroid.kill();
					if (this.winnerIteration == -1)
					{
						robot.subtractHealth(asteroidDamage);
					}
				}
			}
		}
		for (var i = 0; i < this.rockets.size(); i++)
		{
			final var rocket = this.rockets.get(i);
			for (var j = 0; j < this.bullets.size(); j++)
			{
				final var bullet = this.bullets.get(j);
				if (rocket.getBounds().intersects(bullet.getBounds()))
				{
					rocket.subtractHealth(1);
					if (rocket.getHealth() <= 0)
					{
						this.explosions.add(Explosion.rocket(rocket.getPosition()));
						this.rockets.remove(i);
						i--;
						break;
					}
					this.explosions.add(Explosion.bullet(bullet.getPosition()));
					this.bullets.remove(j);
					j--;
				}
			}
			for (var j = 0; j < this.rockets.size(); j++)
			{
				final var rocket2 = this.rockets.get(j);
				if (rocket == rocket2)
				{
					continue;
				}
				if (rocket.getBounds().intersects(rocket2.getBounds()))
				{
					this.explosions.add(Explosion.rocket(rocket.getPosition()));
					this.rockets.remove(i);
					if (i < j)
					{
						j--;
					}
					i--;
					this.explosions.add(Explosion.rocket(rocket2.getPosition()));
					this.rockets.remove(j);
					j--;
				}
			}
			for (var j = 0; j < this.asteroids.size(); j++)
			{
				final var asteroid = this.asteroids.get(j);
				if (asteroid.getTimeSinceKill() > 0)
				{
					continue;
				}
				if (rocket.getBounds().intersects(asteroid.getBounds()))
				{
					this.explosions.add(Explosion.rocket(rocket.getPosition()));
					this.rockets.remove(i);
					i--;
				}
			}
		}
		for (var i = 0; i < this.asteroids.size(); i++)
		{
			final var asteroid = this.asteroids.get(i);
			if (asteroid.getTimeSinceKill() > 0)
			{
				continue;
			}
			for (var j = 0; j < this.bullets.size(); j++)
			{
				final var bullet = this.bullets.get(j);
				if (asteroid.getBounds().intersects(bullet.getBounds()))
				{
					this.explosions.add(Explosion.bullet(bullet.getPosition()));
					this.bullets.remove(j);
					j--;
				}
			}
			for (var j = 0; j < this.asteroids.size(); j++)
			{
				if (i == j)
				{
					continue;
				}
				final var asteroid2 = this.asteroids.get(j);
				if (asteroid2.getTimeSinceKill() > 0)
				{
					continue;
				}
				if (asteroid.getBounds().intersects(asteroid2.getBounds()))
				{
					this.explosions.add(Explosion.asteroid(asteroid.getPosition()));
					this.explosions.add(Explosion.asteroid(asteroid2.getPosition()));
					asteroid.kill();
					asteroid2.kill();
				}
			}
		}
		
		final var arenaOriginX = arenaOrigin.getX();
		final var arenaOriginY = arenaOrigin.getY();
		final var arenaWidth   = arenaBounds.getWidth();
		final var arenaHeight  = arenaBounds.getHeight();
		final var outerBorder  = arenaOuterBounds;
		for (var i = 0; i < this.rockets.size(); i++)
		{
			final var rocket = this.rockets.get(i);
			if ((rocket.getPosition().x < (arenaOriginX - outerBorder)) ||
				(rocket.getPosition().x > (arenaOriginX + arenaWidth + outerBorder)) ||
				(rocket.getPosition().y < (arenaOriginY - outerBorder)) ||
				(rocket.getPosition().y > (arenaOriginY + arenaHeight + outerBorder)))
			{
				this.rockets.remove(i);
				i--;
			}
		}
		for (var i = 0; i < this.bullets.size(); i++)
		{
			final var bullet = this.bullets.get(i);
			if ((bullet.getPosition().x < (arenaOriginX - outerBorder)) ||
				(bullet.getPosition().x > (arenaOriginX + arenaWidth + outerBorder)) ||
				(bullet.getPosition().y < (arenaOriginY - outerBorder)) ||
				(bullet.getPosition().y > (arenaOriginY + arenaHeight + outerBorder)))
			{
				this.bullets.remove(i);
				i--;
			}
		}
		for (final Asteroid asteroid : this.asteroids)
		{
			if ((asteroid.getPosition().x < (arenaOriginX - (outerBorder * 2))) ||
				(asteroid.getPosition().x > (arenaOriginX + arenaWidth + (outerBorder * 2))) ||
				(asteroid.getPosition().y < (arenaOriginY - (outerBorder * 2))) ||
				(asteroid.getPosition().y > (arenaOriginY + arenaHeight + (outerBorder * 2))))
			{
				asteroid.kill();
			}
		}
	}
	
	private void calculateDeaths()
	{
		final var asteroidTimeToKill = GraphicsInfo.ASTEROID_KILL_TIME;

		this.robots.stream()
			.filter(robot -> robot.getHealth() <= 0)
			.forEach(robot -> this.explosions.add(Explosion.robot(robot.getPosition())));

		this.robots.removeIf(robot -> robot.getHealth() <= 0);		
		this.asteroids.removeIf(asteroid -> asteroid.getTimeSinceKill() >= asteroidTimeToKill);
		this.explosions.removeIf(explosion -> explosion.getCurrentSize() <= 1);
		
		if ((this.robots.size() <= 1) && (this.winnerIteration == -1))
		{
			this.winnerIteration = this.iteration;
			this.winnerText      = this.robots.isEmpty() ? "DRAW" : "WINNER";
			this.winnerName      = this.robots.isEmpty() ? "" : this.robots.get(0).getName();
		}
	}
	
	public Bullet getBullet(final int index)
	{
		return this.bullets.get(index);
	}
	
	public int getBulletCount()
	{
		return this.bullets.size();
	}
	
	public int getIteration()
	{
		return this.iteration;
	}

	public InternalRobot getRobot(final int index)
	{
		return this.robots.get(index);
	}
	
	public int getRobotCount()
	{
		return this.robots.size();
	}
	
	public Rocket getRocket(final int index)
	{
		return this.rockets.get(index);
	}
	
	public int getRocketCount()
	{
		return this.rockets.size();
	}
	
	public double getSuddenDeathSeverity()
	{
		return this.suddenDeathSeverity;
	}
	
	public void init(final List<Class<?>> robotClasses, final int preIteration, final int seed)
	{
		if (this.init)
		{
			return;
		}
		
		this.initFields(preIteration, seed);
		
		this.initStars();
		
		this.spawnRobots(robotClasses);
		
		this.init = true;
	}
	
	private void initFields(final int preIteration, final int seed)
	{
		this.robots    = new ArrayList<>();
		this.bullets   = new ArrayList<>();
		this.rockets   = new ArrayList<>();
		this.asteroids = new ArrayList<>();
		
		this.explosions = new ArrayList<>();
		
		this.winnerText      = "";
		this.winnerIteration = -1;
		
		this.iteration = -preIteration;
		if (MainMenu.USE_SEED)
		{
			this.rand = new Random(seed);
		}
		else
		{
			this.rand = new Random();
		}
		this.suddenDeathSeverity = 0;
		this.superNovaIndex      = -1;
		this.currentShakeX       = 0;
		this.currentShakeY       = 0;
	}
	
	private void initStars()
	{
		final var canvasBounds = GraphicsInfo.CANVAS_DIMENSION_BOUNDS;

		final var starCount              = GraphicsInfo.ARENA_STAR_COUNT;
		final var starBrightnessVariance = GraphicsInfo.ARENA_STAR_BRIGHTNESS_VARIANCE;
		final var starSizeMax            = GraphicsInfo.ARENA_STAR_SIZE_MAX;

		this.stars          = new Vector2D[starCount];
		this.starBrightness = new double[starCount];
		this.starSizes      = new int[starCount];
		for (var i = 0; i < this.stars.length; i++)
		{
			final var starX      = this.rand.nextInt((int) canvasBounds.getWidth());
			final var starY      = this.rand.nextInt((int) canvasBounds.getHeight());
			final var starVector = new Vector2D(starX, starY);
			this.stars[i] = starVector;
			
			this.starBrightness[i] = 1 - (this.rand.nextDouble() * starBrightnessVariance);

			this.starSizes[i] = this.rand.nextInt(starSizeMax) + 1;
		}
	}
	
	public void render(final Graphics2D g2d, final boolean gameRunning)
	{
		final var arenaStartTime   = GraphicsInfo.ARENA_STARTTEXT_TIME;
		final var normalDelimiter  = GameInfo.NORMAL_PLAY_DELIMITER;
		final var deathDelimiter   = GameInfo.SUDDEN_DEATH_DELIMITER;
		final var timeoutDelimiter = GameInfo.TIMEOUT_DELIMITER;

		final var shake = (this.iteration >= (normalDelimiter + deathDelimiter)) && gameRunning;
		if (shake)
		{
			g2d.translate(this.currentShakeX, this.currentShakeY);
		}
		
		if ((this.iteration >= (normalDelimiter + deathDelimiter)) && (this.superNovaIndex != -1))
		{
			final var supernovaProgress = (double) (this.iteration - normalDelimiter - deathDelimiter) / timeoutDelimiter;
			InternalArenaGraphics.renderSupernova(g2d, this.stars[this.superNovaIndex], supernovaProgress);
		}
		
		InternalArenaGraphics.renderAmbient(g2d, this.stars, this.starSizes, this.starBrightness, this.iteration, this.superNovaIndex);
		
		for (final Bullet bullet : this.bullets)
		{
			bullet.render(g2d);
		}
		for (final Rocket rocket : this.rockets)
		{
			rocket.render(g2d);
		}
		for (final Asteroid asteroid : this.asteroids)
		{
			asteroid.render(g2d);
		}
		for (final InternalRobot robot : this.robots)
		{
			robot.render(g2d, true, this.winnerIteration != -1);
		}
		for (final Explosion explosion : this.explosions)
		{
			explosion.render(g2d);
		}
		
		if (shake)
		{
			g2d.translate(-this.currentShakeX, -this.currentShakeY);
		}

		if (this.iteration < 0)
		{
			InternalArenaGraphics.renderInformationStartCountdown(g2d, this.iteration);
		}
		else if (this.iteration < arenaStartTime)
		{
			InternalArenaGraphics.renderInformationStartSign(g2d);
		}
		if (gameRunning && (this.winnerIteration != -1))
		{
			InternalArenaGraphics.renderInformationEndWinner(g2d, this.iteration, this.winnerIteration, this.winnerText, this.winnerName);
		}
	}
	
	public void reset()
	{
		this.init = false;
	}
	
	private void spawnRobots(final List<Class<?>> robotClasses)
	{
		final var arenaOrigin = GraphicsInfo.ARENA_ORIGIN;
		
		final var arenaBounds = GameInfo.ARENA_BOUNDS;
		
		final var robotSize = GameInfo.ROBOT_SIZE;

		final var spawns   = new Vector2D[robotClasses.size()];
		final var headings = new double[robotClasses.size()];

		for (var i = 0; i < spawns.length; i++)
		{
			final var x = (this.rand.nextDouble() * (arenaBounds.getWidth() - robotSize)) + arenaOrigin.getX() + (robotSize / 2);
			final var y = (this.rand.nextDouble() * (arenaBounds.getHeight() - robotSize)) + arenaOrigin.getY() + (robotSize / 2);
			spawns[i] = new Vector2D(x, y);
		}
		
		for (var i = 0; i < headings.length; i++)
		{
			headings[i] = this.rand.nextDouble() * 360;
		}
		
		for (var i = 0; i < robotClasses.size(); i++)
		{
			this.addRobot(robotClasses.get(i).getSimpleName(), spawns[i], headings[i]);
		}
	}
	
	public void update()
	{
		final var arenaBounds = GameInfo.ARENA_BOUNDS;
		final var outerBounds = GameInfo.ARENA_BOUNDS_OUTER;
		
		final var normalPlayDelimiter  = GameInfo.NORMAL_PLAY_DELIMITER;
		final var suddenDeathDelimiter = GameInfo.SUDDEN_DEATH_DELIMITER;
		final var timeoutDelimiter     = GameInfo.TIMEOUT_DELIMITER;
		
		final var suddenDeathSeverityChange = GameInfo.SUDDEN_DEATH_SEVERITY_CHANGE;
		final var supernovaMaxShake         = GraphicsInfo.SUPERNOVA_SCREEN_SHAKE_MAX_INTENSITY;

		if (this.iteration < 0)
		{
			this.iteration++;
			return;
		}
		
		this.suddenDeathSeverity = (this.iteration - normalPlayDelimiter) * suddenDeathSeverityChange;
		if ((this.suddenDeathSeverity > 0) && (this.winnerIteration == -1))
		{
			final double value = this.rand.nextInt(suddenDeathDelimiter);
			if (value < this.suddenDeathSeverity)
			{
				final var centerX     = arenaBounds.getWidth() / 2;
				final var centerY     = arenaBounds.getHeight() / 2;
				final var distance    = Math.max(arenaBounds.getWidth() + outerBounds, arenaBounds.getHeight() + outerBounds);
				final var angle       = this.rand.nextDouble() * 360;
				final var spawnX      = centerX + (Math.cos(Math.toRadians(angle)) * distance);
				final var spawnY      = centerY + (Math.sin(Math.toRadians(angle)) * distance);
				final var spawnVector = new Vector2D(spawnX, spawnY);
				
				final var targetX      = this.rand.nextDouble() * arenaBounds.width;
				final var targetY      = this.rand.nextDouble() * arenaBounds.height;
				final var targetVector = new Vector2D(targetX, targetY);
				
				final var aimVector          = targetVector.sub(spawnVector);
				final var correctedAimVector = new Vector2D(aimVector.x, -aimVector.y);
				final var heading            = correctedAimVector.getAngle();

				final var asteroid = new Asteroid(spawnVector, heading);
				this.asteroids.add(asteroid);
			}
		}
		
		if ((this.iteration >= (normalPlayDelimiter + suddenDeathDelimiter)) &&
			(this.iteration <= (normalPlayDelimiter + suddenDeathDelimiter + timeoutDelimiter)))
		{
			if (this.superNovaIndex == -1)
			{
				this.superNovaIndex = this.rand.nextInt(this.stars.length);
			}
			
			final var timeoutProgress = (this.iteration - normalPlayDelimiter - suddenDeathDelimiter) / (double) timeoutDelimiter;
			final var currentShake    = (int) (supernovaMaxShake * timeoutProgress);
			this.currentShakeX = (int) ((this.rand.nextDouble() * (currentShake * 2)) - currentShake);
			this.currentShakeY = (int) ((this.rand.nextDouble() * (currentShake * 2)) - currentShake);
			
			if (this.iteration >= (normalPlayDelimiter + suddenDeathDelimiter + timeoutDelimiter))
			{
				for (var i = 0; i < this.robots.size(); i++)
				{
					final var robot = this.robots.get(i);
					this.explosions.add(Explosion.robot(robot.getPosition()));
				}
				for (var i = 0; i < this.bullets.size(); i++)
				{
					final var bullet = this.bullets.get(i);
					this.explosions.add(Explosion.bullet(bullet.getPosition()));
				}
				for (var i = 0; i < this.rockets.size(); i++)
				{
					final var rocket = this.rockets.get(i);
					this.explosions.add(Explosion.rocket(rocket.getPosition()));
				}
				for (var i = 0; i < this.asteroids.size(); i++)
				{
					final var asteroid = this.asteroids.get(i);
					this.explosions.add(Explosion.asteroid(asteroid.getPosition()));
				}
				this.robots.clear();
				this.bullets.clear();
				this.rockets.clear();
				this.asteroids.clear();
			}
		}
		else
		{
			this.currentShakeX = 0;
			this.currentShakeY = 0;
		}
		
		for (final InternalRobot robot : this.robots)
		{
			robot.update(this.winnerIteration != -1);
		}
		for (final Bullet bullet : this.bullets)
		{
			bullet.update();
		}
		for (final Rocket rocket : this.rockets)
		{
			rocket.update();
		}
		for (final Asteroid asteroid : this.asteroids)
		{
			asteroid.update();
		}
		for (final Explosion explosion : this.explosions)
		{
			explosion.update();
		}
		this.calculateCollisions();
		this.calculateDeaths();
		this.iteration++;
	}
}
