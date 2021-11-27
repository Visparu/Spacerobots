package de.visparu.spacerobots.game.entities.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

import de.visparu.spacerobots.settings.GameInfo;
import de.visparu.spacerobots.settings.GraphicsInfo;
import de.visparu.spacerobots.util.Vector2D;

public final class AsteroidGraphics
{
	private AsteroidGraphics()
	{
		
	}
	
	public static void renderBase(final Graphics2D g2d, final Vector2D position)
	{
		var       colorAsteroidBase = GraphicsInfo.ASTEROID_COLOR_BASE;
		final var layerCount        = GraphicsInfo.ASTEROID_COLOR_LAYERS;
		final var asteroidSize      = GameInfo.ASTEROID_SIZE;

		final var x      = (int) (position.x - (asteroidSize / 2));
		final var y      = (int) (position.y - (asteroidSize / 2));
		final var width  = (int) asteroidSize;
		final var height = (int) asteroidSize;
		
		final var layerDistance = (asteroidSize / 2) / layerCount;
		
		for (var i = 0; i < layerCount; i++)
		{
			g2d.setColor(colorAsteroidBase);
			if ((i % 2) == 0)
			{
				colorAsteroidBase = colorAsteroidBase.darker();
			}
			final var absX      = (int) (x + (layerDistance * i));
			final var absY      = (int) (y + (layerDistance * i));
			final var absWidth  = (int) (width - (layerDistance * i * 2));
			final var absHeight = (int) (height - (layerDistance * i * 2));
			g2d.fillOval(absX, absY, absWidth, absHeight);
		}
	}
	
	public static void renderTail(final Graphics2D g2d, final Vector2D position, final double heading, final int timeSinceKill)
	{
		final var colorAsteroidTail = GraphicsInfo.ASTEROID_COLOR_TAIL;
		final var layerCount        = GraphicsInfo.ASTEROID_COLOR_LAYERS;
		final var asteroidSize      = GameInfo.ASTEROID_SIZE;
		final var maxTailLength     = GraphicsInfo.ASTEROID_TAIL_LENGTH;
		final var timeToKill        = GraphicsInfo.ASTEROID_KILL_TIME;

		final var tailLength = maxTailLength * (1 - ((double) timeSinceKill / (double) timeToKill));
		
		final var startPointX = position.x + (Math.cos(Math.toRadians(heading + 180)) * tailLength);
		final var startPointY = position.y - (Math.sin(Math.toRadians(heading + 180)) * tailLength);
		final var startPoint  = new Vector2D(startPointX, startPointY);
		
		final var p1x = position.x + ((Math.cos(Math.toRadians(heading + 90)) * asteroidSize) / 2);
		final var p1y = position.y - ((Math.sin(Math.toRadians(heading + 90)) * asteroidSize) / 2);
		final var p1  = new Vector2D(p1x, p1y);
		
		final var p2x = position.x + ((Math.cos(Math.toRadians(heading - 90)) * asteroidSize) / 2);
		final var p2y = position.y - ((Math.sin(Math.toRadians(heading - 90)) * asteroidSize) / 2);
		final var p2  = new Vector2D(p2x, p2y);
		
		var startAngle = p2.sub(startPoint).getAngle();
		if (startAngle <= 180)
		{
			startAngle = startAngle + ((180 - startAngle) * 2);
		}
		else
		{
			startAngle = startAngle - ((startAngle - 180) * 2);
		}
		startAngle += 2;
		
		var endAngle = p1.sub(startPoint).getAngle();
		if (endAngle <= 180)
		{
			endAngle = endAngle + ((180 - endAngle) * 2);
		}
		else
		{
			endAngle = endAngle - ((endAngle - 180) * 2);
		}
		endAngle -= 1;
		final var radius = p2.getDistance(startPoint);
		
		final var angleDifference = ((endAngle - startAngle) / 2) / layerCount;
		for (var i = 0; i < layerCount; i++)
		{
			final var red        = colorAsteroidTail.getRed() / (i + 1);
			final var green      = colorAsteroidTail.getGreen() / (i + 1);
			final var blue       = colorAsteroidTail.getBlue() / (i + 1);
			final var layerColor = new Color(red, green, blue);
			
			final var layerStartAngle = startAngle + (i * angleDifference);
			final var layerEndAngle   = endAngle - (i * angleDifference);
			final var aperture        = layerEndAngle - layerStartAngle;
			g2d.setColor(layerColor);
			g2d.fillArc((int) (startPoint.x - radius), (int) (startPoint.y - radius), (int) (radius * 2), (int) (radius * 2), (int) layerStartAngle, (int) aperture);
		}
	}
}
