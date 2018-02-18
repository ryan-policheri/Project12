package edu.model.city;
//https://www.eia.gov/todayinenergy/detail.php?id=830

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class City
{
	//ATTRIBUTES
	private String cityName;
	private double timeFrameAsPercentageOfHour;

	private String filePath = "..\\Capstone\\src\\edu\\model\\city\\ConsumptionByHourData.txt";
	private HourlyConsumption[] hourlyConsumptions = new HourlyConsumption[24];
	private double percentOfMISO = 0.0132234375;

	private HourlyConsumption currentHourlyConsumption;
	private int currentSample;
	private int currentHourlyConsumptionIndex;
	private int samplesPerTier;

	//CONSTRUCTORS
	public City(String cityName, int[] consumptionTiersByHour, double timeFrameAsPercentageOfHour)
	{
		this.cityName = cityName;
		this.timeFrameAsPercentageOfHour = timeFrameAsPercentageOfHour;

		this.samplesPerTier = (int) (1 / this.timeFrameAsPercentageOfHour);

		this.buildHourlyConsumptionsArray();

		this.currentHourlyConsumption = this.hourlyConsumptions[0];
		this.currentHourlyConsumptionIndex = 0;
		this.currentSample = 0;
	}

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

			int lineNumber = -1;

			while(scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				String[] lineSplit = line.split(",");

				if(lineNumber > -1)
				{
					double minimum = Double.parseDouble(lineSplit[0]) * this.percentOfMISO;
					double deviation = Double.parseDouble(lineSplit[1]) * this.percentOfMISO;

					//convert from megawatt hours to watts
					minimum *= 1000000; //meagwatt hours to watt hours
					minimum *= 3600; //watts hours to watts
					deviation *= 1000000; //meagwatt hours to watt hours
					deviation *= 3600; //watts hours to watts

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
}
