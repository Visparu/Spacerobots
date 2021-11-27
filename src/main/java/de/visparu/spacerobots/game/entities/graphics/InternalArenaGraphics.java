package de.visparu.spacerobots.game.entities.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import de.visparu.spacerobots.settings.GameInfo;
import de.visparu.spacerobots.settings.GraphicsInfo;
import de.visparu.spacerobots.util.GraphicsHelper;
import de.visparu.spacerobots.util.Vector2D;

public final class InternalArenaGraphics
{
	private InternalArenaGraphics()
	{
		
	}
	
	public static void renderAmbient(final Graphics2D g2d, final Vector2D[] stars, final int[] starSizes, final double[] starBrightness, final int iteration, final int explodedIndex)
	{
		InternalArenaGraphics.renderAmbientStars(g2d, stars, starSizes, starBrightness, iteration, explodedIndex);
		InternalArenaGraphics.renderAmbientBorders(g2d);
	}
	
	private static void renderAmbientBorders(final Graphics2D g2d)
	{
		final var arenaOrigin       = GraphicsInfo.ARENA_ORIGIN;
		final var arenaOutlineColor = GraphicsInfo.ARENA_COLOR_OUTLINE;
		final var arenaBounds       = GameInfo.ARENA_BOUNDS;
		
		g2d.setColor(arenaOutlineColor);
		
		int lineStartX = (int) arenaOrigin.getX();
		int lineStartY = (int) arenaOrigin.getY();
		int lineEndX   = (int) (arenaOrigin.getX() + arenaBounds.getWidth());
		int lineEndY   = (int) arenaOrigin.getY();
		g2d.drawLine(lineStartX, lineStartY, lineEndX, lineEndY);
		
		lineStartX = lineEndX;
		lineEndY   = (int) (arenaOrigin.getY() + arenaBounds.getHeight());
		g2d.drawLine(lineStartX, lineStartY, lineEndX, lineEndY);
		
		lineStartY = lineEndY;
		lineEndX   = (int) arenaOrigin.getX();
		g2d.drawLine(lineStartX, lineStartY, lineEndX, lineEndY);
		
		lineStartX = lineEndX;
		lineEndY   = (int) arenaOrigin.getY();
		g2d.drawLine(lineStartX, lineStartY, lineEndX, lineEndY);
	}
	
	private static void renderAmbientStars(final Graphics2D g2d, final Vector2D[] stars, final int[] starSizes, final double[] starBrightness, final int iteration, final int explodedIndex)
	{
		final var starBrightnessChangeDelay = GraphicsInfo.ARENA_STAR_BRIGHTNESS_CHANGE_DELAY;
		final var starBrightnessVariance    = GraphicsInfo.ARENA_STAR_BRIGHTNESS_VARIANCE;
		final var starColor                 = GraphicsInfo.ARENA_STAR_COLOR;
		
		final var rand = new Random();
		for (var i = 0; i < stars.length; i++)
		{
			if (i == explodedIndex)
			{
				continue;
			}
			
			if ((iteration % starBrightnessChangeDelay) == 0)
			{
				starBrightness[i] = 1 - (rand.nextDouble() * starBrightnessVariance);
			}
			final var red          = (int) (starColor.getRed() * starBrightness[i]);
			final var green        = (int) (starColor.getGreen() * starBrightness[i]);
			final var blue         = (int) (starColor.getBlue() * starBrightness[i]);
			final var currentColor = new Color(red, green, blue);
			
			g2d.setColor(currentColor);
			g2d.fillOval((int) stars[i].x, (int) stars[i].y, starSizes[i], starSizes[i]);
		}
	}
	
	public static void renderInformationEndWinner(final Graphics2D g2d, final int iteration, final int winnerIteration, final String winnerString, final String winnerName)
	{
		final var buildUpTime  = GraphicsInfo.ARENA_WINNER_ANNOUNCEMENT_BUILDUP;
		final var maxFontSize  = GraphicsInfo.ARENA_WINNER_ANNOUNCEMENT_TEXT_SIZE;
		final var winnerColor  = GraphicsInfo.ARENA_WINNER_ANNOUNCEMENT_COLOR;
		final var canvasBounds = GraphicsInfo.CANVAS_DIMENSION_BOUNDS;
		final var bold         = Font.BOLD;
		
		final var iterationDifference = Math.min(iteration - winnerIteration, buildUpTime);
		final var fontSize            = (int) (maxFontSize * ((double) iterationDifference / (double) buildUpTime));
		
		final var font1 = new Font("Times New Roman", bold, fontSize);
		final var font2 = new Font("Times New Roman", bold, (fontSize * 2) / 3);
		final var x     = (int) canvasBounds.getWidth() / 2;
		final var y     = (int) canvasBounds.getHeight() / 2;
		
		GraphicsHelper.renderCenteredText(g2d, winnerString, font1, x, y, winnerColor);
		GraphicsHelper.renderCenteredText(g2d, winnerName, font2, x, y + ((fontSize * 2.0) / 3.0), winnerColor);
	}
	
	public static void renderInformationStartCountdown(final Graphics2D g2d, final int iteration)
	{
		final var countdownTime   = GraphicsInfo.ARENA_COUNTDOWN_TIME;
		final var countdownAmount = GraphicsInfo.ARENA_COUNTDOWN_AMOUNT;
		final var countdownSize   = GraphicsInfo.ARENA_SIZE_COUNTDOWN;
		final var countdownColor  = GraphicsInfo.ARENA_COLOR_COUNTDOWN;
		final var canvasBounds    = GraphicsInfo.CANVAS_DIMENSION_BOUNDS;
		final var bold            = Font.BOLD;
		
		final var timerState = (Math.abs(iteration) / (countdownTime / countdownAmount)) + 1;
		final var fontSize   = (int) (countdownSize * ((Math.abs(iteration) % (countdownTime / (double) countdownAmount)) / countdownTime));
		final var font       = new Font("Times New Roman", bold, fontSize);
		final var x          = (int) (canvasBounds.getWidth() / 2);
		final var y          = (int) (canvasBounds.getHeight() / 2);
		
		GraphicsHelper.renderCenteredText(g2d, String.valueOf(timerState), font, x, y, countdownColor);
	}
	
	public static void renderInformationStartSign(final Graphics2D g2d)
	{
		final var startTextSize  = GraphicsInfo.ARENA_SIZE_STARTTEXT;
		final var canvasBounds   = GraphicsInfo.CANVAS_DIMENSION_BOUNDS;
		final var countdownColor = GraphicsInfo.ARENA_COLOR_COUNTDOWN;
		final var bold           = Font.BOLD;
		
		final var font = new Font("Times New Roman", bold, startTextSize);
		final var x    = (int) canvasBounds.getWidth() / 2;
		final var y    = (int) canvasBounds.getHeight() / 2;
		
		GraphicsHelper.renderCenteredText(g2d, "GO!", font, x, y, countdownColor);
	}
	
	public static void renderSupernova(final Graphics2D g2d, final Vector2D starPosition, final double supernovaProgress)
	{
		final var supernovaMaxSize = GraphicsInfo.SUPERNOVA_EXPLOSION_MAX_SIZE;
		final var supernovaLayers  = GraphicsInfo.SUPERNOVA_EXPLOSION_LAYERS;
		final var supernovaDecay   = GraphicsInfo.SUPERNOVA_EXPLOSION_DECAY;
		
		final var explosionColor      = GraphicsInfo.SUPERNOVA_EXPLOSION_COLOR;
		final var explosionInnerColor = GraphicsInfo.SUPERNOVA_EXPLOSION_INNER_COLOR;
		
		final var supernovaCurSize = supernovaMaxSize * Math.min(supernovaProgress, 1.0);
		
		final var supernovaInnerCurSize = supernovaMaxSize * Math.min((supernovaProgress - 1) / supernovaDecay, 1.0);
		
		final var distancePerLayer = (supernovaCurSize / 2) / supernovaLayers;
		if ((supernovaProgress - 1) <= supernovaDecay)
		{
			for (var i = 0; i < supernovaLayers; i++)
			{
				final var red   = (int) (explosionColor.getRed() * (1.0 - (i / (double) supernovaLayers)));
				final var green = (int) (explosionColor.getGreen() * (1.0 - (i / (double) supernovaLayers)));
				final var blue  = (int) (explosionColor.getBlue() * (1.0 - (i / (double) supernovaLayers)));
				g2d.setColor(new Color(red, green, blue));
				
				final var currentDistance = distancePerLayer * i;
				final var absX            = (starPosition.x - (supernovaCurSize / 2)) + currentDistance;
				final var absY            = (starPosition.y - (supernovaCurSize / 2)) + currentDistance;
				final var absWidth        = supernovaCurSize - (currentDistance * 2);
				final var absHeight       = supernovaCurSize - (currentDistance * 2);
				
				final var absInnerX      = starPosition.x - supernovaInnerCurSize;
				final var absInnerY      = starPosition.y - supernovaInnerCurSize;
				final var absInnerWidth  = supernovaInnerCurSize * 2;
				final var absInnerHeight = supernovaInnerCurSize * 2;
				
				final Ellipse2D outerSphere = new Ellipse2D.Double(absX, absY, absWidth, absHeight);
				final Ellipse2D innerSphere = new Ellipse2D.Double(absInnerX, absInnerY, absInnerWidth, absInnerHeight);
				
				g2d.fill(outerSphere);
				g2d.setColor(explosionInnerColor);
				g2d.fill(innerSphere);
			}
		}
		else
		{
			final var colorFactor = 1 - (supernovaProgress - 1);
			final var red         = Math.max((int) (explosionInnerColor.getRed() * colorFactor), 0);
			final var green       = Math.max((int) (explosionInnerColor.getGreen() * colorFactor), 0);
			final var blue        = Math.max((int) (explosionInnerColor.getBlue() * colorFactor), 0);
			g2d.setColor(new Color(red, green, blue));
			g2d.fillRect(0, 0, (int) GraphicsInfo.CANVAS_DIMENSION_BOUNDS.getWidth(), (int) GraphicsInfo.CANVAS_DIMENSION_BOUNDS.getHeight());
		}
	}
}
