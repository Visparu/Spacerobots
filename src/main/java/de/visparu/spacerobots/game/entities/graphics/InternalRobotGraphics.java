package de.visparu.spacerobots.game.entities.graphics;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Arc2D;

import de.visparu.spacerobots.settings.GameInfo;
import de.visparu.spacerobots.settings.GraphicsInfo;
import de.visparu.spacerobots.util.GraphicsHelper;
import de.visparu.spacerobots.util.Vector2D;

public final class InternalRobotGraphics
{
	private InternalRobotGraphics()
	{
		
	}
	
	public static void renderBody(final Graphics2D g2d, final Vector2D position, final double heading)
	{
		InternalRobotGraphics.renderBodyBase(g2d, position, heading);
		InternalRobotGraphics.renderBodyTurret(g2d, position, heading);
	}
	
	private static void renderBodyBase(final Graphics2D g2d, final Vector2D position, final double heading)
	{
		final var robotSize          = GameInfo.ROBOT_SIZE;
		final var robotAngularExtend = GraphicsInfo.ROBOT_ANGULAR_EXTEND;
		final var robotColorBase     = GraphicsInfo.ROBOT_COLOR_BASE;

		final var centerX = position.x;
		final var centerY = position.y;
		
		final var center = new Vector2D(centerX, centerY);
		GraphicsHelper.renderAngledRectangle(g2d, center, robotSize, robotAngularExtend, heading, robotColorBase);
	}
	
	private static void renderBodyTurret(final Graphics2D g2d, final Vector2D position, final double heading)
	{
		final var robotSize          = GameInfo.ROBOT_SIZE;
		final var robotAngularExtend = GraphicsInfo.ROBOT_ANGULAR_EXTEND;
		final var turretColor        = GraphicsInfo.ROBOT_COLOR_TURRET;

		final var centerX = position.x + (Math.cos(Math.toRadians(heading)) * ((robotSize * 4) / 5));
		final var centerY = position.y + (Math.sin(Math.toRadians(heading)) * ((robotSize * 4) / 5));
		
		final var size          = robotSize / 4;
		final var angularExtend = (robotAngularExtend * 3) / 4;
		final var center        = new Vector2D(centerX, centerY);
		GraphicsHelper.renderAngledRectangle(g2d, center, size, angularExtend, heading, turretColor);
	}
	
	public static void renderInfo(final Graphics2D g2d, final Vector2D position, final String name, final double health, final double energy, final boolean winner)
	{
		InternalRobotGraphics.renderInfoWinner(g2d, position, winner);
		InternalRobotGraphics.renderInfoName(g2d, position, name);
		InternalRobotGraphics.renderInfoHealthBar(g2d, position, health);
		InternalRobotGraphics.renderInfoEnergyBar(g2d, position, energy);
	}
	
	private static void renderInfoEnergyBar(final Graphics2D g2d, final Vector2D position, final double energy)
	{
		final var infoBarWidth  = GraphicsInfo.ROBOT_INFO_BARS_WIDTH;
		final var infoBarHeight = GraphicsInfo.ROBOT_INFO_BARS_HEIGHT;
		final var robotSize     = GameInfo.ROBOT_SIZE;
		final var maxEnergy     = GameInfo.ROBOT_GAMEPLAY_MAXENERGY;
		final var energyColor   = GraphicsInfo.ROBOT_INFO_BARS_ENERGY_COLOR;

		final var x          = (int) (position.x - (infoBarWidth / 2.0F));
		final var y          = (int) (position.y + robotSize + infoBarHeight);
		final var percEnergy = energy / maxEnergy;
		final var absWidth   = (int) (infoBarWidth * percEnergy);
		g2d.setColor(energyColor);
		g2d.fillRect(x, y, absWidth, infoBarHeight);
	}
	
	private static void renderInfoHealthBar(final Graphics2D g2d, final Vector2D position, final double health)
	{
		final var infoBarWidth        = GraphicsInfo.ROBOT_INFO_BARS_WIDTH;
		final var infoBarHeight       = GraphicsInfo.ROBOT_INFO_BARS_HEIGHT;
		final var robotSize           = GameInfo.ROBOT_SIZE;
		final var robotMaxHealth      = GameInfo.ROBOT_GAMEPLAY_MAXHEALTH;
		final var healthThresholdHalf = GraphicsInfo.ROBOT_INFO_BARS_HEALTH_THRESHOLD_HALF;
		final var healthThresholdLow  = GraphicsInfo.ROBOT_INFO_BARS_HEALTH_THRESHOLD_LOW;
		final var healthFullColor     = GraphicsInfo.ROBOT_INFO_BARS_HEALTH_COLOR_FULL;
		final var healthHalfColor     = GraphicsInfo.ROBOT_INFO_BARS_HEALTH_COLOR_HALF;
		final var healthLowColor      = GraphicsInfo.ROBOT_INFO_BARS_HEALTH_COLOR_LOW;

		final var x = (int) (position.x - (infoBarWidth / 2.0F));
		final var y = (int) (position.y + robotSize);
		
		final var percHealth = health / robotMaxHealth;
		var       absWidth   = (int) (infoBarWidth * percHealth);
		if ((absWidth < 1) && (health > 0))
		{
			absWidth = 1;
		}
		
		if (percHealth > healthThresholdHalf)
		{
			g2d.setColor(healthFullColor);
		}
		else if (percHealth > healthThresholdLow)
		{
			g2d.setColor(healthHalfColor);
		}
		else
		{
			g2d.setColor(healthLowColor);
		}
		
		g2d.fillRect(x, y, absWidth, infoBarHeight);
	}
	
	private static void renderInfoName(final Graphics2D g2d, final Vector2D position, final String name)
	{
		final var fontSize       = GraphicsInfo.ROBOT_NAME_SIZE;
		final var plain          = Font.PLAIN;
		final var robotSize      = GameInfo.ROBOT_SIZE;
		final var countdownColor = GraphicsInfo.ARENA_COLOR_COUNTDOWN;

		final var font    = new Font("Times New Roman", plain, fontSize);
		final var metrics = g2d.getFontMetrics(font);
		final var x       = (int) (position.x - (metrics.stringWidth(name) / 2.0F));
		final var y       = (int) (position.y - (robotSize / 2.0F));
		g2d.setFont(font);
		g2d.setColor(countdownColor);
		g2d.drawString(name, x, y);
	}
	
	private static void renderInfoWinner(final Graphics2D g2d, final Vector2D position, final boolean winner)
	{
		if (!winner)
		{
			return;
		}
		
		final var    fontSize      = GraphicsInfo.ROBOT_NAME_SIZE;
		final var    plain         = Font.PLAIN;
		final var    robotSize     = GameInfo.ROBOT_SIZE;
		final double crownDistance = GraphicsInfo.ROBOT_WINNER_CROWN_DISTANCE;
		final double crownHeight   = GraphicsInfo.ROBOT_WINNER_CROWN_HEIGHT;
		final double crownWidth    = GraphicsInfo.ROBOT_WINNER_CROWN_WIDTH;
		final var    crownSpikes   = GraphicsInfo.ROBOT_WINNER_CROWN_SPIKES;
		final var    spikeSize     = GraphicsInfo.ROBOT_WINNER_CROWN_SPIKE_SIZE;
		final var    crownColor    = GraphicsInfo.ROBOT_WINNER_CROWN_COLOR;
		final var    spikeColor    = GraphicsInfo.ROBOT_WINNER_CROWN_SPIKE_COLOR;
		
		final var font    = new Font("Times New Roman", plain, fontSize);
		final var metrics = g2d.getFontMetrics(font);
		final var centerX = position.x;
		final var bottomY = position.y - ((robotSize) / 2) - metrics.getHeight() - crownDistance;
		
		final var vertexCount = ((crownSpikes * 2) - 1) + 2;
		final var x           = new int[vertexCount];
		final var y           = new int[vertexCount];
		
		x[0] = (int) (centerX + (crownWidth / 2));
		y[0] = (int) bottomY;
		
		x[1] = (int) (centerX - (crownWidth / 2));
		y[1] = (int) bottomY;
		
		final var spikeDistance = crownWidth / (crownSpikes - 1) / 2;
		for (var i = 2; i < vertexCount; i++)
		{
			x[i] = (int) ((centerX - (crownWidth / 2)) + ((i - 2) * spikeDistance));
			y[i] = (int) (((i % 2) == 0) ? bottomY - crownHeight : bottomY - ((crownHeight * 2) / 3));
		}
		
		final var crown = new Polygon(x, y, vertexCount);
		g2d.setColor(crownColor);
		g2d.fillPolygon(crown);
		
		g2d.setColor(spikeColor);
		for (var i = 2; i < vertexCount; i++)
		{
			if ((i % 2) == 0)
			{
				g2d.fillOval(x[i] - (spikeSize / 2), y[i] - (spikeSize / 2), spikeSize, spikeSize);
			}
		}
	}
	
	public static void renderScan(final Graphics2D g2d, final Arc2D scan, final boolean success)
	{
		final var scanSuccessColor = GraphicsInfo.SCAN_COLOR_SUCCESS;
		final var scanFailureColor = GraphicsInfo.SCAN_COLOR_FAILURE;

		if (scan == null)
		{
			return;
		}
		if (success)
		{
			g2d.setColor(scanSuccessColor);
		}
		else
		{
			g2d.setColor(scanFailureColor);
		}
		g2d.draw(scan);
	}
}
