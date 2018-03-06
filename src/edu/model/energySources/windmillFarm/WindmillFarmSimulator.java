package edu.model.energySources.windmillFarm;

import edu.controllers.Controller;
import edu.controllers.WriteToFile;
import edu.model.EnergyCommander;
import edu.model.batteries.Demand;
import edu.model.batteries.Surplus;

import java.util.Timer;
import java.util.TimerTask;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WindmillFarmSimulator
{
	private WindmillFarm windmillFarm;
	private int[] windTiersByHour;
	private double timeFrameAsPercentageOfHour;

	private double windVariabilityAsPercentDeviation;

	private int samplesPerTier;

	private int currentWindTier;
	private int currentWindTierIndex;
	private int currentSample;

	public WindmillFarmSimulator(WindmillFarm windmillFarm, int[] windTiersByHour, double timeFrameAsPercentageOfHour)
	{
		this.windmillFarm = windmillFarm;
		this.windTiersByHour = windTiersByHour;
		this.timeFrameAsPercentageOfHour = timeFrameAsPercentageOfHour;

		this.windVariabilityAsPercentDeviation = 0.35;

		this.samplesPerTier = (int) (1 / this.timeFrameAsPercentageOfHour);

		this.currentWindTier = this.windTiersByHour[0];
		this.currentWindTierIndex = 0;
		this.currentSample = 0;
	}

	public double nextSurplus()
	{
		WindCondition windCondition = new WindCondition(this.currentWindTier, this.windVariabilityAsPercentDeviation);
		this.currentSample += 1;

		if (this.currentSample == this.samplesPerTier)
		{
			this.currentWindTierIndex += 1;
			this.currentWindTier = this.windTiersByHour[this.currentWindTierIndex];
			this.currentSample = 0;
		}

		//double wattageOutputForTotalTime = windmillFarm.aggregateWindmillEnergyOutput(windCondition, this.timeFrameAsPercentageOfHour);

		//Surplus surplus = new Surplus(1,1);
		return -1;//wattageOutputForTotalTime;
	}

	public void modifyWindVariabilityAsPercentDeviation(double newWindVariabilityAsPercentDeviation)
	{
		this.windVariabilityAsPercentDeviation = newWindVariabilityAsPercentDeviation;
	}

	public void modifyWindTiers(int index, int newTier)
	{
		this.windTiersByHour[index] = newTier;
	}

	public int[] getWindTiersByHour()
	{
		return this.windTiersByHour;
	}

}
