package de.visparu.spacerobots.game.entities.external.sensors;

import de.visparu.spacerobots.game.entities.internal.InternalRobot;
import de.visparu.spacerobots.settings.GameInfo;

public final class EngineController
{
	private final InternalRobot ir;
	
	public EngineController(final InternalRobot ir)
	{
		this.ir = ir;
	}
	
	/**
	 * @param intensity
	 *            the intensity of the movement
	 *
	 * @return how much energy would be needed for a specific movement
	 */
	public double getEnergyConsumptionForMove(final double intensity)
	{
		final var energyMoveBase       = GameInfo.ENERGY_MOVE_BASE;
		final var energyMoveMultiplier = GameInfo.ENERGY_MOVE_MULTIPLIER;

		return energyMoveBase + (intensity * energyMoveMultiplier);
	}
	
	/**
	 * @return how much energy would be needed for stopping your robot instantly
	 */
	public double getEnergyConsumptionForStop()
	{
		final var energyMoveBase       = GameInfo.ENERGY_MOVE_BASE;
		final var energyMoveMultiplier = GameInfo.ENERGY_MOVE_MULTIPLIER;

		return energyMoveBase + (this.ir.getSpeed() * energyMoveMultiplier);
	}
	
	/**
	 * Accelerates your robot into the given direction
	 *
	 * @param heading
	 *            The direction to accelerate into
	 * @param intensity
	 *            The intensity of the movement in pixels/second�
	 *            
	 * @return true if the move was triggered. False if there is not enough energy.
	 */
	public boolean move(final double heading, final double intensity)
	{
		if (this.ir.hasNextMove())
		{
			return false;
		}
		
		final var necessaryEnergy = this.getEnergyConsumptionForMove(intensity);
		if (necessaryEnergy > this.ir.getEnergy())
		{
			return false;
		}
		this.ir.subtractEnergy(necessaryEnergy);
		
		this.ir.move(heading, intensity);
		return true;
	}
	
	/**
	 * Tries to stop your robot immediately.
	 * 
	 * @return true if the stop was triggered. False if there is not enough energy.
	 */
	public boolean stop()
	{
		if (this.ir.hasNextMove())
		{
			return false;
		}
		
		final var necessaryEnergy = this.getEnergyConsumptionForStop();
		if (necessaryEnergy > this.ir.getEnergy())
		{
			return false;
		}
		this.ir.subtractEnergy(necessaryEnergy);
		
		this.ir.move(this.ir.getHeading() + 180, this.ir.getSpeed());
		return true;
	}
}
