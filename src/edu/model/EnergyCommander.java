package edu.model;

import edu.model.batteries.BatteryGrid;

public class EnergyCommander
{
	private static BatteryGrid batteryGrid;
	
	public EnergyCommander(BatteryGrid batteryGrid)
	{
		this.batteryGrid = batteryGrid;
	}

	public static void commandEnergy(double energyProduced, double energyDemanded)
	{
		//if there is more energy produced that needed, then store energy
		if (energyProduced - energyDemanded >= 0)
		{
			double energySurplusInWatts = energyProduced - energyDemanded;
			batteryGrid.allocateEnergySurplus(energySurplusInWatts);
		}
		else
		{
			double energyDemandInWatts = energyDemanded - energyProduced;
			batteryGrid.allocateEnergyDemand(energyDemandInWatts);
		}
	}

}
