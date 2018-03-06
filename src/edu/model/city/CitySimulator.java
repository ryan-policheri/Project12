package edu.model.city;

import edu.controllers.WriteToFile;
import edu.model.EnergyCommander;
import edu.model.batteries.Demand;
import edu.model.batteries.Surplus;
import edu.model.energySources.windmillFarm.WindCondition;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CitySimulator
{
	private City city;
	private int[] consumptionTiersByHour;

	private int currentConsumptionTier;
	private double consumptionVariabilityAsPercentDeviation;
	private int currentSample;
	private int currentConsumptionTierIndex;
	private int samplesPerTier;
	private double timeFrameAsPercentageOfHour;

	public CitySimulator(City city, int[] consumptionTiersByHour, double timeFrameAsPercentageOfHour)
	{
		this.city = city;
		this.consumptionTiersByHour = consumptionTiersByHour;
		this.timeFrameAsPercentageOfHour = timeFrameAsPercentageOfHour;

		this.samplesPerTier = (int) (1 / this.timeFrameAsPercentageOfHour);

		this.currentConsumptionTier = this.consumptionTiersByHour[0];
		this.currentConsumptionTierIndex = 0;
		this.currentSample = 0;
	}

	public double nextDemand()
	{
		this.currentSample += 1;

		if (this.currentSample == this.samplesPerTier)
		{
			this.currentConsumptionTierIndex += 1;
			this.currentConsumptionTier = this.consumptionTiersByHour[this.currentConsumptionTierIndex];
			this.currentSample = 0;
		}

		//double wattageOutputForTotalTime = city.calculateCityEnergyNeed(this.currentConsumptionTier, this.timeFrameAsPercentageOfHour);

		//Demand demand = new Demand(1,1);
		//return wattageOutputForTotalTime;
		return -1;
	}

}

