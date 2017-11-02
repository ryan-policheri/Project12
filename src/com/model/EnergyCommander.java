package com.model;

import com.model.batteries.BatteryGrid;
import com.model.batteries.Surplus;

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
		
	
}
