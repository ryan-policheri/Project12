package edu.model.batteries;

import java.util.Timer;
import java.util.TimerTask;

public abstract class VolatileBattery extends Battery
{
	//CONSTRUCTORS
	public VolatileBattery(String batteryName, double massInKilograms)
	{
		super(batteryName, massInKilograms);
	}

	//FUNCTIONS
	
	//functions overridden in child classes
	public double storeEnergy(double energySurplusInWatts)
	{
		return -1;
	}

	public double releaseEnergy(double enerfyDemandInWatts)
	{
		return -1;
	}
}
