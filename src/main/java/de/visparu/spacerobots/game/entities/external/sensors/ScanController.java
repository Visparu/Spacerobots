package de.visparu.spacerobots.game.entities.external.sensors;

import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;

import de.visparu.spacerobots.game.InternalArena;
import de.visparu.spacerobots.game.entities.external.scanned.ScanResult;
import de.visparu.spacerobots.game.entities.internal.Bullet;
import de.visparu.spacerobots.game.entities.internal.InternalRobot;
import de.visparu.spacerobots.settings.GameInfo;

public final class ScanController
{
	private final InternalRobot ir;
	private final InternalArena ia;
	
	private ScanResult lastScan;
	private ScanResult nextScan;
	
	public ScanController(final InternalRobot ir, final InternalArena ia)
	{
		this.ir = ir;
		this.ia = ia;
		
		final List<InternalRobot> robotsDummy  = new ArrayList<>();
		final List<Bullet>        bulletsDummy = new ArrayList<>();
		this.lastScan = new ScanResult(robotsDummy, bulletsDummy, -1);
	}
	
	/**
	 * Returns the energy that has to be expended for a specific scan
	 *
	 * @param distance
	 *            The distance to scan from your robot's center point in pixels
	 * @param aperture
	 *            The aperture of the scan arc in degrees
	 */
	public double getEnergyConsumptionForScan(final double distance, final double aperture)
	{
		final var pi                = Math.PI;
		final var energyScanBase    = GameInfo.ENERGY_SCAN_BASE;
		final var energyScanPerUnit = GameInfo.ENERGY_SCAN_PERUNIT;
		
		if (distance < 0)
		{
			return -1;
		}
		
		final var area = distance * distance * pi * (aperture / 360.0);
		return energyScanBase + (area * energyScanPerUnit);
	}
	
	/**
	 * Returns an object holding the last scan's results
	 */
	public ScanResult getLastScanResult()
	{
		if ((this.nextScan != null) && (this.nextScan.getIteration() < this.ia.getIteration()))
		{
			this.lastScan = this.nextScan;
		}
		return this.lastScan;
	}
	
	/**
	 * Scans an area of the arena for enemies, bullets, rockets and asteroids
	 *
	 * @param heading
	 *            The direction of the scan in degrees
	 * @param distance
	 *            The distance of the scan from your robot's center point in pixels
	 * @param aperture
	 *            The aperture of the scan arc in degrees (additive to the heading)
	 */
	public boolean scan(final double heading, final double distance, double aperture)
	{
		final var arcPie = Arc2D.PIE;
		
		if (((this.nextScan != null) && (this.nextScan.getIteration() == this.ia.getIteration())) || (distance < 0) || (aperture < 1))
		{
			return false;
		}
		if (aperture > 360)
		{
			aperture = 360;
		}
		
		final var necessaryEnergy = this.getEnergyConsumptionForScan(distance, aperture);
		if (necessaryEnergy > this.ir.getEnergy())
		{
			return false;
		}
		this.ir.subtractEnergy(necessaryEnergy);
		
		final var centerX = this.ir.getPosition().x;
		final var centerY = this.ir.getPosition().y;
		
		final var x      = centerX - distance;
		final var y      = centerY - distance;
		final var width  = distance * 2;
		final var height = distance * 2;
		
		final Arc2D scanArea = new Arc2D.Double(x, y, width, height, heading, aperture, arcPie);
		
		final List<InternalRobot> scannedRobots  = new ArrayList<>();
		final List<Bullet>        scannedBullets = new ArrayList<>();
		
		for (var i = 0; i < this.ia.getRobotCount(); i++)
		{
			final var cur = this.ia.getRobot(i);
			if (cur == this.ir)
			{
				continue;
			}
			if (scanArea.intersects(cur.getBounds()))
			{
				scannedRobots.add(cur);
			}
		}
		for (var i = 0; i < this.ia.getBulletCount(); i++)
		{
			final var cur = this.ia.getBullet(i);
			if (scanArea.intersects(cur.getBounds()))
			{
				scannedBullets.add(cur);
			}
		}
		
		final var scan = new ScanResult(scannedRobots, scannedBullets, this.ia.getIteration());
		this.nextScan = scan;
		
		this.ir.setCurrentScan(scanArea, !scannedRobots.isEmpty());
		
		return true;
	}
}
