package edu.model.energySources.windmillFarm;

public class WindmillFarm
{

	private double powerOutput;
	private int lowestPowerOutput = 50000;
	private int[] energyProductionTiers;
	private String windmillFarmName;
	
	public WindmillFarm(String windmillFarmName)
	{
		this.energyProductionTiers = new int[24];

		for (int i = 0; i < this.energyProductionTiers.length; i++)
		{
			this.energyProductionTiers[i] = 1;
		}
	}
	
	public WindmillFarm(String windmillFarmName, int[] energyProductionTiers)//, int[] energyProductionTiers)
	{
		this.windmillFarmName = windmillFarmName;
		this.energyProductionTiers = energyProductionTiers;
	}
	
	public double calculateWindmillFarmOutput(int hourOfDay, int expectedSurplusesPerHour)
	{
		double powerSurplus = -1;

		// Tier 1 through 5
		switch (this.energyProductionTiers[hourOfDay])
		{
			case 1:
				powerSurplus = randomizeTier1Surplus(expectedSurplusesPerHour);
				break;
			case 2:
				powerSurplus = randomizeTier2Surplus(expectedSurplusesPerHour);
				break;
			case 3:
				powerSurplus = randomizeTier3Surplus(expectedSurplusesPerHour);
				break;
			case 4:
				powerSurplus = randomizeTier4Surplus(expectedSurplusesPerHour);
				break;
			case 5:
				powerSurplus = randomizeTier5Surplus(expectedSurplusesPerHour);
				break;
		}

		return powerSurplus;
	}
	
	//region New tiers code
	//TODO: Figure out reasonable surpluses for each tier.
	private double randomizeTier1Surplus(int expectedSurplusesPerHour)
	{
		double minimumSurplus = 1000000 / expectedSurplusesPerHour; // 1,000,000 watts split up among how ever many average surpluses are in an hour
		double maximumSurplus = 1000000000 / expectedSurplusesPerHour; // 1,000,000,000
		
		double randomDemand = minimumSurplus + (Math.random() * ((maximumSurplus - minimumSurplus) + 1));
		
		return randomDemand;
	}

	private double randomizeTier2Surplus(int expectedSurplusesPerHour)
	{
		double minimumSurplus = 1000000000 / expectedSurplusesPerHour; // 1,000,000,000
		double maximumSurplus = 2000000000 / expectedSurplusesPerHour; // 2,000,000,000
		
		double randomDemand = minimumSurplus + (Math.random() * ((maximumSurplus - minimumSurplus) + 1));
		
		return randomDemand;
	}

	private double randomizeTier3Surplus(int expectedSurplusesPerHour)
	{
		double minimumSurplus = 2000000000 / expectedSurplusesPerHour; // 2,000,000,000
		double maximumSurplus = 3000000000L / expectedSurplusesPerHour; // 3,000,000,000
		
		double randomDemand = minimumSurplus + (Math.random() * ((maximumSurplus - minimumSurplus) + 1));
		
		return randomDemand;
	}

	private double randomizeTier4Surplus(int expectedSurplusesPerHour)
	{
		double minimumSurplus = 3000000000L / expectedSurplusesPerHour; // 3,000,000,000
		double maximumSurplus = 4000000000L / expectedSurplusesPerHour; // 4,000,000,000
		
		double randomDemand = minimumSurplus + (Math.random() * ((maximumSurplus - minimumSurplus) + 1));
		
		return randomDemand;
	}
	
	private double randomizeTier5Surplus(int expectedSurplusesPerHour)
	{
		double minimumSurplus = 4000000000L / expectedSurplusesPerHour; // 4,000,000,000
		double maximumSurplus = 5000000000L / expectedSurplusesPerHour; // 5,000,000,000
		
		double randomDemand = minimumSurplus + (Math.random() * ((maximumSurplus - minimumSurplus) + 1));
		
		return randomDemand;
	}
	
	//endregion
	
	/*private void randomizeHighOutput() 
	{
		int minimumOutput = 5000000; //5 MW
		int maximumOutput = 10000000; //10 MW
		
		this.powerOutput = minimumOutput + (Math.random() * ((maximumOutput - minimumOutput) + 1));
	}

	private void randomizeModerateOutput()
	{
		int minimumOutput = 500000; // 0.5 MW
		int maximumOutput = 5000000; //5 MW
		
		this.powerOutput = minimumOutput + (Math.random() * ((maximumOutput - minimumOutput) + 1));
	}
	
	private void randomizeLowOutput()
	{
		int minimumOutput = 0; //NOTHING
		int maximumOutput = 500000; //0.5 MW
		
		this.powerOutput = minimumOutput + (Math.random() * ((maximumOutput - minimumOutput) + 1));
		
		if (this.powerOutput < this.lowestPowerOutput)
		{
			this.powerOutput = 0;
		}
	}*/
	
	public int[] getEnergyProductionTiers()
	{
		return this.energyProductionTiers;
	}

	public void setEnergyProductionTiers(int[] energyProductionTiers)
	{
		this.energyProductionTiers = energyProductionTiers;
	}

}
