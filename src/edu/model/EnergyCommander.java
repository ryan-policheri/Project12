package edu.model;

import edu.model.batteries.Battery;
import edu.model.batteries.BatteryGrid;
import edu.model.batteries.Demand;
import edu.model.batteries.Surplus;

public class EnergyCommander
{
	private static BatteryGrid batteryGrid;
	
	public EnergyCommander(BatteryGrid batteryGrid)
	{
		this.batteryGrid = batteryGrid;
	}

	public static void commandEnergy(Surplus surplus)
	{
		batteryGrid.allocateEnergySurplus(surplus);
		batteryGrid.displayGrid();
	}
	
	public static void commandEnergy(Demand demand)
	{
		batteryGrid.allocateEnergyDemand(demand);
		batteryGrid.displayGrid();
	}
		
	
}
