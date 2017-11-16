package edu.model.batteries;

public class Demand
{
	double energyNeededInWatts;
	double timeNeededInSeconds;
	
	public Demand(double energyNeededInWatts, double timeNeededInSeconds)
	{
		this.energyNeededInWatts = energyNeededInWatts;
		this.timeNeededInSeconds = timeNeededInSeconds;
	}
	
	public double getEnergyNeededInWatts()
	{
		return this.energyNeededInWatts;
	}
	
	//Make toString method.
	
	public String toString()
	{
		return this.energyNeededInWatts + " watts over " + this.timeNeededInSeconds + " seconds";
	}
	
	public double getTimeNeededInSeconds()
	{
		return this.timeNeededInSeconds;
	}
	
	public boolean isDemandGone()
	{
		if (this.timeNeededInSeconds == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
