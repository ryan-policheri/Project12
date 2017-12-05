package edu.model.city;

import edu.model.batteries.Demand;

import java.util.ArrayList;
import java.util.List;

public class City 
{
	//ATTRIBUTES
	//Do we even need these?
	//Keeping just in case
	private String cityName;
	private int[] energyConsumptionTiers;
	private int[] energyProductionTiers;
	private int estimatedLandAvailableInAcres;
	private int cityPopulation;
	private int statePopulation;
	private int demandsThroughOutADay;
	private double cityEnergyConsumedYearly;
	private double cityEnergyProducedYearly;
	private double cityRenewableEnergyProducedYearly;
	private int lowestPowerDemand = 50000;
	long millisecondsInDay = (long) (8640000000L);
	long randomMillisecondInDay = (long)(Math.random() * millisecondsInDay);
	
	
	//parallel array
	private List<Demand> dailyDemand= new ArrayList<Demand>();
    private List<Double> dailyDemandTimesOfDayInMilliseconds = new ArrayList<Double>();
	
	//CONSTRUCTORS
	public City(String cityName)
	{	
		this.cityName = cityName;
		this.energyConsumptionTiers = new int[24];
		this.energyProductionTiers = new int[24];
	}
	public City(String cityName, int[] energyConsumptionTiers, int[] energyProductionTiers)
	{
		this.cityName = cityName;
		this.energyConsumptionTiers = energyConsumptionTiers;
		this.energyProductionTiers = energyProductionTiers;
	}
	
	//FUNTIONS
	public void addDemand(Demand dailyDemand, double dailyDemandTimesOfDayInMilliseconds)
	{
		this.dailyDemand.add(dailyDemand);
		this.dailyDemandTimesOfDayInMilliseconds.add(dailyDemandTimesOfDayInMilliseconds);
	}
	
	public void removeDemand(Demand dailyDemand, long dailyDemandTimesOfDayInMilliseconds)
	{
		this.dailyDemand.remove(dailyDemand);
		this.dailyDemandTimesOfDayInMilliseconds.remove(dailyDemandTimesOfDayInMilliseconds);
	}
	
/*	public double powerNeed(double energyNeededInWatts, double amountOfTimeInSeconds)
	{
		// Get energy to Megawatts    multiplied    by how many hours.
		double energyInMegawattHours = (energyNeededInWatts / 1000000) * (3600 / amountOfTimeInSeconds);
		// Below is just in case it is needed
		//double energyInJoules = energyNeededInWatts * amountOfTimeInSeconds;
		return energyInMegawattHours;
	}
	*/

	public double calculateCityDemand(int hourOfDay)
	{
		double powerDemand = -1;

		// Tier 1 through 5
		switch (this.energyConsumptionTiers[hourOfDay])
		{
			case 1:
				powerDemand = randomizeTier1Demand();
				break;
			case 2:
				powerDemand = randomizeTier2Demand();
				break;
			case 3:
				powerDemand = randomizeTier3Demand();
				break;
			case 4:
				powerDemand = randomizeTier4Demand();
				break;
			case 5:
				powerDemand = randomizeTier5Demand();
				break;
		}

		return powerDemand;

		//// Old code
		//if((hourOfDay >= 0 && hourOfDay <= 5) || (hourOfDay >= 21 && hourOfDay <= 23))
		//{
		//	randomizeLowDemand();
		//}
		//else if((hourOfDay >= 6 && hourOfDay <= 9) || (hourOfDay >= 13 && hourOfDay <= 17))
		//{
		//	randomizeModerateDemand();
		//}
		//else if((hourOfDay >= 10 && hourOfDay <= 12) || (hourOfDay >= 18 && hourOfDay <= 20))
		//{
		//	randomizeHighDemand();
		//}
		//
		//return this.powerDemand;
	}

	//region New tiers code
	//TODO: Figure out reasonable demands for each tier.
	private double randomizeTier1Demand()
	{
		return 1;
	}

	private double randomizeTier2Demand()
	{
		return 1;
	}

	private double randomizeTier3Demand()
	{
		return 1;
	}

	private double randomizeTier4Demand()
	{
		return 1;
	}

	private double randomizeTier5Demand()
	{
		return 1;
	}
	//endregion

	//region Old tiers code
	//private void randomizeHighDemand()
	//{
	//	int minimumDemand = 5000000; //5 MW
	//	int maximumDemand = 10000000; //10 MW
	//
	//	this.powerDemand = minimumDemand + (Math.random() * ((maximumDemand - minimumDemand) + 1));
	//}
	//
	//private void randomizeModerateDemand()
	//{
	//	int minimumDemand = 500000; // 0.5 MW
	//	int maximumDemand = 5000000; //5 MW
	//
	//	this.powerDemand = minimumDemand + (Math.random() * ((maximumDemand - minimumDemand) + 1));
	//}
	//
	//private void randomizeLowDemand()
	//{
	//	int minimumDemand = 10; //DON'T WANT LOWER
	//	int maximumDemand = 500000; //0.5 MW
	//
	//	this.powerDemand = minimumDemand + (Math.random() * ((maximumDemand - minimumDemand) + 1));
	//
	//}
	//endregion

	//region Getters/Setters
	public List<Demand> getDailyDemand()
	{
		return dailyDemand;
	}

	public List<Double> getDailyDemandTimesOfDayInMilliseconds()
	{
		return dailyDemandTimesOfDayInMilliseconds;
	}

	public int[] getEnergyConsumptionTiers()
	{
		return energyConsumptionTiers;
	}

	public void setEnergyConsumptionTiers(int[] energyConsumptionTiers)
	{
		this.energyConsumptionTiers = energyConsumptionTiers;
	}

	public int[] getEnergyProductionTiers()
	{
		return energyProductionTiers;
	}

	public void setEnergyProductionTiers(int[] energyProductionTiers)
	{
		this.energyProductionTiers = energyProductionTiers;
	}
	//endregion

	@Override
	public String toString()
	{
		return this.cityName;
	}
}
