package edu.model.batteries;

public class Surplus
{	
	double energyAvailableInWatts;
	double timeAvailableInSeconds;

	public Surplus(double energyAvailableInWatts, double timeAvailableInSeconds)
	{
		this.energyAvailableInWatts = energyAvailableInWatts;
		this.timeAvailableInSeconds = timeAvailableInSeconds;
	}
	
	public String toString()
	{
		return this.energyAvailableInWatts + " watts avaiable over " + this.timeAvailableInSeconds + " seconds";
	}
	
	public double getEnergyAvailableInWatts()
	{
		return this.energyAvailableInWatts;
	}
	
	public double getTimeAvailableInSeconds()
	{
		return this.timeAvailableInSeconds;
	}
	
	public boolean isSurplusGone()
	{
		if (this.timeAvailableInSeconds == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
