import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class City 
{
	//ATTRIBUTES
	//make private
	String cityName;
	int estimatedLandAvailableInAcres;
	int cityPopulation;
	int statePopulation;
	double cityEnergyConsumedYearly;
	double cityEnergyProducedYearly;
	double cityRenewableEnergyProducedYearly;
	private double powerDemand;
	private int lowestPowerDemand = 50000;
	
	//parallel array
	List<Demand> dailyDemand= new ArrayList<Demand>();
	List<Time> dailyDemandTimesOfDay = new ArrayList<Time>();
	
	//CONSTRUCTORS
	public City(String cityName)
	{
		this.cityName = cityName;
	}
	
	//FUNTIONS

	
	public void addDemand(Demand dailyDemand, Time dailyDemandTimesOfDay)
	{
		this.dailyDemand.add(dailyDemand);
		this.dailyDemandTimesOfDay.add(dailyDemandTimesOfDay);
	}
	
	public void removeDemand(Demand dailyDemand, Time dailyDemandTimesOfDay)
	{
		this.dailyDemand.remove(dailyDemand);
		this.dailyDemandTimesOfDay.remove(dailyDemandTimesOfDay);
	}
	
	public double powerNeed(double energyNeededInWatts, double amountOfTimeInSeconds)
	{
		// Get energy to Megawatts    multiplied    by how many hours.
		double energyInMegawattHours = (energyNeededInWatts / 1000000) * (3600 / amountOfTimeInSeconds);
		// Below is just in case it is needed
		//double energyInJoules = energyNeededInWatts * amountOfTimeInSeconds;
		return energyInMegawattHours;
	}
	
	public double calculateCityDemand(int hourOfDay)
	{
		if((hourOfDay >= 0 && hourOfDay <= 5) || (hourOfDay >= 21 && hourOfDay <= 23))
		{
			randomizeLowDemand();
		}
		else if((hourOfDay >= 6 && hourOfDay <= 9) || (hourOfDay >= 13 && hourOfDay <= 17))
		{
			randomizeModerateDemand();
		}
		else if((hourOfDay >= 10 && hourOfDay <= 12) || (hourOfDay >= 18 && hourOfDay <= 20))
		{
			randomizeHighDemand();
		}
		
		return this.powerDemand;
	}
	
	private void randomizeHighDemand() 
	{
		int minimumDemand = 5000000; //5 MW
		int maximumDemand = 10000000; //10 MW
		
		this.powerDemand = minimumDemand + (Math.random() * ((maximumDemand - minimumDemand) + 1));
	}

	private void randomizeModerateDemand()
	{
		int minimumDemand = 500000; // 0.5 MW
		int maximumDemand = 5000000; //5 MW
		
		this.powerDemand = minimumDemand + (Math.random() * ((maximumDemand - minimumDemand) + 1));
	}
	
	private void randomizeLowDemand()
	{
		int minimumDemand = 0; //NOTHING
		int maximumDemand = 500000; //0.5 MW
		
		this.powerDemand = minimumDemand + (Math.random() * ((maximumDemand - minimumDemand) + 1));
		
		if (this.powerDemand < this.lowestPowerDemand)
		{
			this.powerDemand = 0;
		}
	}
}
