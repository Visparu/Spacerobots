package de.visparu.spacerobots.util;

import java.awt.geom.Point2D;

public final class Vector2D
{
	public static Vector2D getNormalizedVectorFromAngle(final double angle)
	{
		final var x = Math.cos(Math.toRadians(angle));
		final var y = Math.sin(Math.toRadians(angle));
		return new Vector2D(x, y);
	}

	public final double x;
	
	public final double y;
	
	public Vector2D(final double x, final double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(final Point2D v)
	{
		this.x = v.getX();
		this.y = v.getY();
	}
	
	public Vector2D(final Vector2D v)
	{
		this.x = v.x;
		this.y = v.y;
	}
	
	public Vector2D add(final Vector2D v)
	{
		return new Vector2D(this.x + v.x, this.y + v.y);
	}
	
	public Vector2D div(final double div)
	{
		return new Vector2D(this.x / div, this.y / div);
	}
	
	public double getAngle()
	{
		var rawAngle = Math.toDegrees(Math.atan(this.y / this.x));
		
		if (this.x < 0)
		{
			rawAngle += 180;
		}
		else if (this.y < 0)
		{
			rawAngle += 360;
		}
		
		return rawAngle;
	}
	
	public double getDistance(final Vector2D v)
	{
		return v.sub(this).getLength();
	}
	
	public double getLength()
	{
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}
	
	public Vector2D mul(final double fac)
	{
		return new Vector2D(this.x * fac, this.y * fac);
	}
	
	public Vector2D normalize()
	{
		return this.div(this.getLength());
	}
	
	public Vector2D sub(final Vector2D v)
	{
		return new Vector2D(this.x - v.x, this.y - v.y);
	}
	
	@Override
	public String toString()
	{
		return String.format("[%d,%d]", (int) this.x, (int) this.y);
	}
}
