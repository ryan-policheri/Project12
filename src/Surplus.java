
public class Surplus
{	
	double energyAvailableInWatts;
	double timeAvailableInSeconds;

	public Surplus(double energyAvailableInWatts, double timeAvailableInSeconds)
	{
		this.energyAvailableInWatts = energyAvailableInWatts;
		this.timeAvailableInSeconds = timeAvailableInSeconds;
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
