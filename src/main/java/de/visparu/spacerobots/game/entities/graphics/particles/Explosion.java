package de.visparu.spacerobots.game.entities.graphics.particles;

import java.awt.Color;
import java.awt.Graphics2D;

import de.visparu.spacerobots.settings.GraphicsInfo;
import de.visparu.spacerobots.util.Vector2D;

public final class Explosion
{
	public static Explosion asteroid(final Vector2D center)
	{
		final var lifeTime = GraphicsInfo.EXPLOSION_ASTEROID_LIFETIME;
		final var maxSize  = GraphicsInfo.EXPLOSION_ASTEROID_SIZE_MAX;
		final var layers   = GraphicsInfo.EXPLOSION_ASTEROID_LAYERS;
		final var decay    = GraphicsInfo.EXPLOSION_ASTEROID_DECAY;
		
		return new Explosion(center, lifeTime, maxSize, layers, decay);
	}

	public static Explosion bullet(final Vector2D center)
	{
		final var lifeTime = GraphicsInfo.EXPLOSION_BULLET_LIFETIME;
		final var maxSize  = GraphicsInfo.EXPLOSION_BULLET_SIZE_MAX;
		final var layers   = GraphicsInfo.EXPLOSION_BULLET_LAYERS;
		final var decay    = GraphicsInfo.EXPLOSION_BULLET_DECAY;
		
		return new Explosion(center, lifeTime, maxSize, layers, decay);
	}

	public static Explosion robot(final Vector2D center)
	{
		final var lifeTime = GraphicsInfo.EXPLOSION_ROBOT_LIFETIME;
		final var maxSize  = GraphicsInfo.EXPLOSION_ROBOT_SIZE_MAX;
		final var layers   = GraphicsInfo.EXPLOSION_ROBOT_LAYERS;
		final var decay    = GraphicsInfo.EXPLOSION_ROBOT_DECAY;
		
		return new Explosion(center, lifeTime, maxSize, layers, decay);
	}

	public static Explosion rocket(final Vector2D center)
	{
		final var lifeTime = GraphicsInfo.EXPLOSION_ROCKET_LIFETIME;
		final var maxSize  = GraphicsInfo.EXPLOSION_ROCKET_SIZE_MAX;
		final var layers   = GraphicsInfo.EXPLOSION_ROCKET_LAYERS;
		final var decay    = GraphicsInfo.EXPLOSION_ROCKET_DECAY;
		
		return new Explosion(center, lifeTime, maxSize, layers, decay);
	}

	private final Vector2D center;

	private final int    startLifeTime;
	private final double maxSize;

	private final int layers;
	
	private final double decay;
	
	private int lifeTime;
	
	private double curSize;
	
	public Explosion(final Vector2D center, final int lifeTime, final int maxSize, final int layers, final double decay)
	{
		this.center        = center;
		this.startLifeTime = lifeTime;
		this.lifeTime      = lifeTime;
		this.maxSize       = maxSize;
		this.curSize       = this.maxSize / 5;
		this.layers        = layers;
		this.decay         = decay;
	}
	
	public double getCurrentSize()
	{
		return this.curSize;
	}
	
	public void render(final Graphics2D g2d)
	{
		final var outerColor = GraphicsInfo.EXPLOSION_COLOR_OUTER;

		for (var i = 0; i < this.layers; i++)
		{
			final var x      = (int) (this.center.x - (this.curSize / (i + 1)));
			final var y      = (int) (this.center.y - (this.curSize / (i + 1)));
			final var width  = (int) ((this.curSize / (i + 1)) * 2);
			final var height = (int) ((this.curSize / (i + 1)) * 2);
			
			final var red   = outerColor.getRed() / ((i + 2) / 2);
			final var green = outerColor.getGreen() / (i + 1);
			final var blue  = 0;
			
			g2d.setColor(new Color(red, green, blue));
			g2d.fillOval(x, y, width, height);
		}
	}
	
	public void update()
	{
		if (this.lifeTime > 0)
		{
			this.curSize += this.maxSize / this.startLifeTime;
		}
		else
		{
			this.curSize /= this.decay;
		}
		this.lifeTime--;
	}
}
