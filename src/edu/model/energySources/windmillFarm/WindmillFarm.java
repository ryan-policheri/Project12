package edu.model.energySources.windmillFarm;

import java.util.ArrayList;

public class WindmillFarm
{
	private String windmillFarmName;
	private int[] windTiersByHour;
	private double timeFrameAsPercentageOfHour;

	private ArrayList<Windmill> windmills;

	private double windVariabilityAsPercentDeviation;

	private int samplesPerTier;
	private int currentWindTier;
	private int currentWindTierIndex;
	private int currentSample;

	public WindmillFarm(String windmillFarmName, int[] windTiersByHour, double timeFrameAsPercentageOfHour)
	{
		this.windmillFarmName = windmillFarmName;
		this.windTiersByHour = windTiersByHour;	//should come in as an array of length 24. 1 slot for each hour
		this.timeFrameAsPercentageOfHour = timeFrameAsPercentageOfHour;

		this.windmills = new ArrayList<Windmill>();

		this.windVariabilityAsPercentDeviation = 0.35;

		this.currentWindTier = this.windTiersByHour[0];
		this.samplesPerTier = (int) (1 / this.timeFrameAsPercentageOfHour);

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

		double wattageOutputForTotalTime = this.aggregateWindmillEnergyOutput(windCondition, this.timeFrameAsPercentageOfHour);

		return wattageOutputForTotalTime;
	}

	private double aggregateWindmillEnergyOutput(WindCondition windCondition, double timeFrameAsPercentageOfHour)
	{
		double totalOutput = 0;

		for (Windmill windmill : this.windmills)
		{
			totalOutput += windmill.calculateCurrentMegawattHours(windCondition, timeFrameAsPercentageOfHour);
		}

		return totalOutput;
	}

	public void addWindmill(Windmill windmill)
	{
		this.windmills.add(windmill);
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

	public int[] getEnergyProductionTiers()
	{
		return new int[24];
	}

	public String toString()
	{
		return this.windmillFarmName;
	}

}
