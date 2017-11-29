package edu.model.batteries;

abstract class VolatileBattery extends Battery
{
	// ATTRIBUTES
	
	// CONSTRUCTORS
	public VolatileBattery(String batteryName, double massInKilograms)
	{
		super(batteryName, massInKilograms);
	}

	// FUNCTIONS
	public double getCurrentEnergyInJoules()
	{
		return 0;
	}

	public boolean isBatteryFull()
	{
		return false;
	}

	public boolean isBatteryInUse()
	{
		return false;
	}

	public Surplus storeEnergy(Surplus surplus)
	{
		return null;
	}

	public Demand releaseEnergy(Demand demand)
	{
		return null;
	}

	public String displayBattery()
	{
		return super.displayBattery();
	}
}
