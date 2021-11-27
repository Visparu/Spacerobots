package de.visparu.spacerobots.game.entities.graphics;

import java.awt.Graphics2D;

import de.visparu.spacerobots.settings.GameInfo;
import de.visparu.spacerobots.settings.GraphicsInfo;
import de.visparu.spacerobots.util.Vector2D;

public final class BulletGraphics
{
	private BulletGraphics()
	{
		
	}
	
	public static void renderBase(final Graphics2D g2d, final Vector2D position)
	{
		final var colorBulletBase = GraphicsInfo.BULLET_COLOR_BASE;
		final var bulletSize      = GameInfo.BULLET_SIZE;

		g2d.setColor(colorBulletBase);
		
		final var x      = (int) (position.x - (bulletSize / 2));
		final var y      = (int) (position.y - (bulletSize / 2));
		final var width  = (int) bulletSize;
		final var height = (int) bulletSize;
		g2d.fillOval(x, y, width, height);
	}
}
