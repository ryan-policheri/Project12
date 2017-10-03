public class City 
{
	//ATTRIBUTES
	String cityName;
	int estimatedLandAvailableInAcres;
	int cityPopulation;
	int statePopulation;
	double cityEnergyConsumedYearly;
	double cityEnergyProducedYearly;
	double cityRenewableEnergyProducedYearly;
	
	//CONSTRUCTORS
	public City(String cityName)
	{
		this.cityName = cityName;
	}
	
	//FUNTIONS
	public double powerNeed(double energyNeededInWatts, double amountOfTimeInSeconds)
	{
		// Get energy to Megawatts    multiplied    by how many hours.
		double energyInMegawattHours = (energyNeededInWatts / 1000000) * (3600 / amountOfTimeInSeconds);
		// Below is just in case it is needed
		//double energyInJoules = energyNeededInWatts * amountOfTimeInSeconds;
		return energyInMegawattHours;
	}
}
