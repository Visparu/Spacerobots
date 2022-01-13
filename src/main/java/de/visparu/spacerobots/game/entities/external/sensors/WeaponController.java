package de.visparu.spacerobots.game.entities.external.sensors;

import de.visparu.spacerobots.game.InternalArena;
import de.visparu.spacerobots.game.entities.internal.Bullet;
import de.visparu.spacerobots.game.entities.internal.InternalRobot;
import de.visparu.spacerobots.settings.GameInfo;
import de.visparu.spacerobots.util.Vector2D;

public final class WeaponController
{
	
	private final InternalRobot ir;
	private final InternalArena ia;
	
	public WeaponController(final InternalRobot ir, final InternalArena ia)
	{
		this.ir = ir;
		this.ia = ia;
	}
	
	/**
	 * @return the energy needed for a bullet shot
	 */
	public double getEnergyConsumptionForBulletShot()
	{
		return GameInfo.ENERGY_BULLET_BASE;
	}
	
	/**
	 * Shoots a bullet in the given direction
	 *
	 * @param heading
	 *            The direction to shoot towards in degrees
	 *            
	 * @return true if the shot was triggered. False if there is not enough energy.
	 */
	public boolean shootBullet(final double heading)
	{
		final var robotSize = GameInfo.ROBOT_SIZE;
		
		final var necessaryEnergy = this.getEnergyConsumptionForBulletShot();
		if (necessaryEnergy > this.ir.getEnergy())
		{
			return false;
		}
		this.ir.subtractEnergy(necessaryEnergy);
		
		final var position    = this.ir.getPosition();
		final var angleOffset = Vector2D.getNormalizedVectorFromAngle(heading);
		final var offset      = angleOffset.mul(robotSize / 2);
		final var invOffset   = new Vector2D(offset.x, -offset.y);
		final var newBullet   = new Bullet(this.ir, position.add(invOffset), heading);
		this.ia.addBullet(newBullet);
		return true;
	}
	
	/**
	 * Shoots a bullet towards a given point
	 *
	 * @param aimPoint
	 *            The point to shoot towards
	 *            
	 * @return true if the shot was triggered. False if there is not enough energy.
	 */
	public boolean shootBullet(final Vector2D aimPoint)
	{
		final var path = aimPoint.sub(this.ir.getPosition());
		return this.shootBullet(path.getAngle());
	}
}
