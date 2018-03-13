package edu.model.city;
//https://www.eia.gov/todayinenergy/detail.php?id=830

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class City
{
	//ATTRIBUTES
	public static int hoursInDay = 24;

	private String cityName;
	private double timeFrameAsPercentageOfHour;

	private String filePath = "..\\Capstone\\src\\edu\\model\\city\\ConsumptionByHourData";
	private HourlyConsumption[] hourlyConsumptions = new HourlyConsumption[City.hoursInDay];
	private double percentOfMISO = 0.0132234375;

	private HourlyConsumption currentHourlyConsumption;
	private int currentSample;
	private int currentHourlyConsumptionIndex;
	private int samplesPerTier;

	private int[] defaultWindTiersByHour = {1,1,1,1,1,2,2,5,5,5,2,1,2,1,4,3,4,5,5,4,3,2,1,1}; //this is the default wind tiers for the area surrounding the city. Only to be used when building defualts

	//CONSTRUCTORS
	public City(String cityName, double timeFrameAsPercentageOfHour)
	{
		this.cityName = cityName;
		this.timeFrameAsPercentageOfHour = timeFrameAsPercentageOfHour;

		this.filePath = this.filePath + this.cityName.replaceAll(" ", "") + ".txt";

		this.samplesPerTier = (int) (1 / this.timeFrameAsPercentageOfHour);

		this.buildHourlyConsumptionsArray();

		this.currentHourlyConsumption = this.hourlyConsumptions[0];
		this.currentHourlyConsumptionIndex = 0;
		this.currentSample = 0;
	}

	//METHODS
	public double nextDemand()
	{
		this.currentSample += 1;

		if (this.currentSample == this.samplesPerTier)
		{
			this.currentHourlyConsumptionIndex += 1;
			this.currentHourlyConsumption = this.hourlyConsumptions[this.currentHourlyConsumptionIndex];
			this.currentSample = 0;
		}

		double wattageOutputForTotalTime = this.currentHourlyConsumption.calculateConsumption(this.timeFrameAsPercentageOfHour);

		//Demand demand = new Demand(1,1);
		return wattageOutputForTotalTime;
	}

	private void buildHourlyConsumptionsArray()
	{
		try
		{
			File file = new File(this.filePath);
			Scanner scanner = new Scanner(file);

			int lineNumber = -1; //-1 because there is one line of header

			while(scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				String[] lineSplit = line.split(",");

				if(lineNumber > -1)
				{
					double minimum = Double.parseDouble(lineSplit[0]) * this.percentOfMISO;
					double deviation = Double.parseDouble(lineSplit[1]) * this.percentOfMISO;

					//convert from megawatt hours to watts
					minimum *= 1000000; //megawatt hours to watt hours
					minimum *= 3600; //watt hours to watts
					deviation *= 1000000; //megawatt hours to watt hours
					deviation *= 3600; //watt hours to watts

					hourlyConsumptions[lineNumber] = new HourlyConsumption(minimum, deviation);
				}

				lineNumber ++;
			}

			scanner.close();
		}
		catch(IOException ex)
		{
			System.out.println("Problem loading hourly consumption file");
		}

	}

	@Override
	public String toString()
	{
		return this.cityName;
	}

	//GETTERS
	public int[] getDefaultWindTiersByHour()
	{
		return defaultWindTiersByHour;
	}

	public double[] getEnergyMinimumsByHourInMegawatts()
	{
		double[] energyMinimumsByHour = new double[City.hoursInDay];

		for(int i = 0; i < City.hoursInDay; i++)
		{
			double energyMinimumInWatts = hourlyConsumptions[i].getMinimumHourlyConsumptionInWatts();
			double energyMinimumInMegawatts = (energyMinimumInWatts / 1000000) / 3600; //convert to megawatt hours
			energyMinimumsByHour[i] = energyMinimumInMegawatts;
		}

 		return energyMinimumsByHour;
	}

	public double[] getEnergyMinimumsByHour()
	{
		double[] energyMinimumsByHour = new double[City.hoursInDay];

		for(int i = 0; i < City.hoursInDay; i++)
		{
			double energyMinimumInWatts = hourlyConsumptions[i].getMinimumHourlyConsumptionInWatts();
			energyMinimumsByHour[i] = energyMinimumInWatts;
		}

		return energyMinimumsByHour;
	}

	public double[] getEnergyMaximumsByHourInMegawatts()
	{
		double[] energyMaximumsByHour = new double[City.hoursInDay];

		for(int i = 0; i < this.hourlyConsumptions.length; i++)
		{
			double energyMinimumInWatts = hourlyConsumptions[i].getMinimumHourlyConsumptionInWatts();
			double energyMinimumInMegawattHours = (energyMinimumInWatts / 1000000) / 3600; //convert to megawatt hours

			double energyDeviationInWatts = hourlyConsumptions[i].getMaxDeviationInWatts();
			double energyDeviationInMegawattHours = (energyDeviationInWatts / 1000000) / 3600; //convert to megawatt hours

			energyMaximumsByHour[i] = energyMinimumInMegawattHours + energyDeviationInMegawattHours;
		}

		return energyMaximumsByHour;
	}

	public double getHighestEnergyValue()
	{
		double highestEnergyValue = -1;

		for (double value : this.getEnergyMaximumsByHourInMegawatts())
		{
			if (value > highestEnergyValue)
			{
				highestEnergyValue = value;
			}
		}

		return highestEnergyValue;
	}
}
