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
	private int estimatedLandAvailableInAcres;
	private int cityPopulation;
	private int statePopulation;
	private int demandsThroughOutADay;
	private double cityEnergyConsumedYearly;
	private double cityEnergyProducedYearly;
	private double cityRenewableEnergyProducedYearly;
	private int lowestPowerDemand = 50000;
	private long millisecondsInDay = (long) (8640000000L);
	long randomMillisecondInDay = (long)(Math.random() * millisecondsInDay);
	
	
	//CONSTRUCTORS
	public City(String cityName)
	{	
		this.cityName = cityName;
		this.energyConsumptionTiers = new int[24];

		for (int i = 0; i < this.energyConsumptionTiers.length; i++)
		{
			this.energyConsumptionTiers[i] = 1;
		}
	}
	public City(String cityName, int[] energyConsumptionTiers)//, int[] energyProductionTiers)
	{
		this.cityName = cityName;
		this.energyConsumptionTiers = energyConsumptionTiers;
		//this.energyProductionTiers = energyProductionTiers;
	}
	
	//FUNTIONS
	
/*	public double powerNeed(double energyNeededInWatts, double amountOfTimeInSeconds)
	{
		// Get energy to Megawatts    multiplied    by how many hours.
		double energyInMegawattHours = (energyNeededInWatts / 1000000) * (3600 / amountOfTimeInSeconds);
		// Below is just in case it is needed
		//double energyInJoules = energyNeededInWatts * amountOfTimeInSeconds;
		return energyInMegawattHours;
	}
	*/

	public double calculateCityDemand(int hourOfDay, int expectedDemandsPerHour)
	{
		double powerDemand = -1;

		// Tier 1 through 5
		switch (this.energyConsumptionTiers[hourOfDay])
		{
			case 1:
				powerDemand = randomizeTier1Demand(expectedDemandsPerHour);
				break;
			case 2:
				powerDemand = randomizeTier2Demand(expectedDemandsPerHour);
				break;
			case 3:
				powerDemand = randomizeTier3Demand(expectedDemandsPerHour);
				break;
			case 4:
				powerDemand = randomizeTier4Demand(expectedDemandsPerHour);
				break;
			case 5:
				powerDemand = randomizeTier5Demand(expectedDemandsPerHour);
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
	private double randomizeTier1Demand(int expectedDemandsPerHour)
	{
		double minimumDemand = 300000000 / expectedDemandsPerHour; // 300,000,000 watts split up among how ever many average demands are in an hour
		double maximumDemand = 800000000 / expectedDemandsPerHour; // 800,000,000
		
		double randomDemand = minimumDemand + (Math.random() * ((maximumDemand - minimumDemand) + 1));
		
		return randomDemand;
	}

	private double randomizeTier2Demand(int expectedDemandsPerHour)
	{
		double minimumDemand = 800000000 / expectedDemandsPerHour; // 800,000,000
		double maximumDemand = 1800000000 / expectedDemandsPerHour; // 1,800,000,000
		
		double randomDemand = minimumDemand + (Math.random() * ((maximumDemand - minimumDemand) + 1));
		
		return randomDemand;
	}

	private double randomizeTier3Demand(int expectedDemandsPerHour)
	{
		double minimumDemand = 1800000000 / expectedDemandsPerHour; // 1,800,000,000
		double maximumDemand = 3200000000L / expectedDemandsPerHour; // 3,200,000,000
	
		double randomDemand = minimumDemand + (Math.random() * ((maximumDemand - minimumDemand) + 1));
		
		return randomDemand;
	}

	private double randomizeTier4Demand(int expectedDemandsPerHour)
	{
		double minimumDemand = 3200000000L / expectedDemandsPerHour; // 3,200,000,000
		double maximumDemand = 6400000000L / expectedDemandsPerHour; // 6,400,000,000
		
		double randomDemand = minimumDemand + (Math.random() * ((maximumDemand - minimumDemand) + 1));
		
		return randomDemand;
	}

	private double randomizeTier5Demand(int expectedDemandsPerHour)
	{
		double minimumDemand = 6400000000L / expectedDemandsPerHour; // 6,400,000,000
		double maximumDemand = 10000000000L  / expectedDemandsPerHour; // 10,000,000,000
		
		double randomDemand = minimumDemand + (Math.random() * ((maximumDemand - minimumDemand) + 1));
		
		return randomDemand;
	}
	//endregion

	//region Getters/Setters

	public int[] getEnergyConsumptionTiers()
	{
		return energyConsumptionTiers;
	}

	public void setEnergyConsumptionTiers(int[] energyConsumptionTiers)
	{
		this.energyConsumptionTiers = energyConsumptionTiers;
	}
	//endregion

	@Override
	public String toString()
	{
		return this.cityName;
	}
}
