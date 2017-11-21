package edu.model.batteries;

public class VolatileBattery extends Battery
{
	// ATTRIBUTES
	
	// CONSTRUCTORS
	public VolatileBattery()
	{
		super();
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

	public void displayBattery()
	{
		
	}
}
