package de.visparu.spacerobots.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;

public final class GraphicsHelper
{
	private GraphicsHelper()
	{
		
	}
	
	public static void renderAngledRectangle(final Graphics2D g2d, final Vector2D center, final double size, final double angularExtend, final double heading, final Color color)
	{
		final var centerX = center.x;
		final var centerY = center.y;
		
		final var c1A = Math.toRadians(angularExtend + heading);
		final var c1X = centerX + (Math.cos(c1A) * size);
		final var c1Y = centerY + (Math.sin(c1A) * size);
		
		final var c2A = Math.toRadians((180 - angularExtend) + heading);
		final var c2X = centerX + (Math.cos(c2A) * size);
		final var c2Y = centerY + (Math.sin(c2A) * size);
		
		final var c3A = Math.toRadians(180 + angularExtend + heading);
		final var c3X = centerX + (Math.cos(c3A) * size);
		final var c3Y = centerY + (Math.sin(c3A) * size);
		
		final var c4A = Math.toRadians((360 - angularExtend) + heading);
		final var c4X = centerX + (Math.cos(c4A) * size);
		final var c4Y = centerY + (Math.sin(c4A) * size);
		
		final int[] xVals = { (int) c1X, (int) c2X, (int) c3X, (int) c4X };
		final int[] yVals = { (int) c1Y, (int) c2Y, (int) c3Y, (int) c4Y };
		final var   p     = new Polygon(xVals, yVals, 4);
		
		g2d.setColor(color);
		g2d.fillPolygon(p);
	}
	
	public static void renderCenteredText(final Graphics2D g2d, final String text, final Font font, final double x, final double y, final Color color)
	{
		final var metrics = g2d.getFontMetrics(font);
		final var ulx     = (int) (x - (metrics.stringWidth(text) / 2.0));
		final var uly     = (int) ((y - (metrics.getHeight() / 2.0)) + metrics.getAscent());
		g2d.setFont(font);
		g2d.setColor(color);
		g2d.drawString(text, ulx, uly);
	}
	
	public static Polygon turnPolygon(final double[] x, final double[] y, final double angle, final boolean absolute)
	{
		if (x.length != y.length)
		{
			return null;
		}
		
		var avX = 0D;
		var avY = 0D;
		for (var i = 0; i < x.length; i++)
		{
			avX += x[i];
			avY += y[i];
		}
		avX /= x.length;
		avY /= y.length;
		final var anchor = new Vector2D(avX, avY);
		
		return GraphicsHelper.turnPolygon(x, y, angle, absolute, anchor);
	}
	
	public static Polygon turnPolygon(final double[] x, final double[] y, final double angle, final boolean absolute, final Vector2D anchor)
	{
		if (x.length != y.length)
		{
			return null;
		}
		
		final var xInt = new int[x.length];
		final var yInt = new int[y.length];
		
		for (var i = 0; i < x.length; i++)
		{
			final var curPoint        = new Vector2D(x[i], y[i]);
			final var fromAnchor      = curPoint.sub(anchor);
			final var fromAnchorAngle = fromAnchor.getAngle();
			final var fromAnchorDist  = fromAnchor.getLength();
			final var newAngle        = absolute ? angle : fromAnchorAngle + angle;
			final var newX            = anchor.x + (Math.cos(Math.toRadians(newAngle)) * fromAnchorDist);
			final var newY            = anchor.y + (Math.sin(Math.toRadians(newAngle)) * fromAnchorDist);
			xInt[i] = (int) newX;
			yInt[i] = (int) newY;
		}
		
		return new Polygon(xInt, yInt, xInt.length);
	}
}
