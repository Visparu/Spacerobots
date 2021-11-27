package de.visparu.spacerobots.game.entities.graphics;

import java.awt.Graphics2D;

import de.visparu.spacerobots.settings.GameInfo;
import de.visparu.spacerobots.settings.GraphicsInfo;
import de.visparu.spacerobots.util.Vector2D;

public final class RocketGraphics
{
	private RocketGraphics()
	{
		
	}
	
	public static void renderBase(final Graphics2D g2d, final Vector2D position, final boolean lockedOn)
	{
		final var rocketColorBase    = GraphicsInfo.ROCKET_COLOR_BASE;
		final var homingRange        = GameInfo.ROCKET_HOMING_RANGE;
		final var rocketSuccessColor = GraphicsInfo.ROCKET_COLOR_RANGE_SUCCESS;
		final var rocketFailureColor = GraphicsInfo.ROCKET_COLOR_RANGE_FAILURE;

		g2d.setColor(rocketColorBase);
		g2d.fillOval((int) (position.x - 5), (int) (position.y - 5), 10, 10);
		
		if (!lockedOn)
		{
			g2d.setColor(rocketFailureColor);
		}
		else
		{
			g2d.setColor(rocketSuccessColor);
		}
		final var rangeX      = (int) (position.x - homingRange);
		final var rangeY      = (int) (position.y - homingRange);
		final var rangeWidth  = (int) (homingRange * 2);
		final var rangeHeight = (int) (homingRange * 2);
		g2d.drawOval(rangeX, rangeY, rangeWidth, rangeHeight);
	}
}
