package edu.model;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.model.batteries.Demand;

public class City 
{
	//ATTRIBUTES
	//Do we even need these?
	//Keeping just in case
	private String cityName;
	private int estimatedLandAvailableInAcres;
	private int cityPopulation;
	private int statePopulation;
	private int demandsThroughOutADay;
	private double cityEnergyConsumedYearly;
	private double cityEnergyProducedYearly;
	private double cityRenewableEnergyProducedYearly;
	private double powerDemand;
	private int lowestPowerDemand = 50000;
	long millisecondsInDay = (long) (8640000000L);
	long randomMillisecondInDay = (long)(Math.random() * millisecondsInDay);
	
	
	//CONSTRUCTORS
	public City(String cityName)
	{	
		this.cityName = cityName;	
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
		int minimumDemand = 10; //DON'T WANT LOWER
		int maximumDemand = 500000; //0.5 MW
		
		this.powerDemand = minimumDemand + (Math.random() * ((maximumDemand - minimumDemand) + 1));
		
	}
	

	
}
