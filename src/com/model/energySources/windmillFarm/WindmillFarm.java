package com.model.energySources.windmillFarm;

public class WindmillFarm
{

	private double powerOutput;
	private int lowestPowerOutput = 50000;
	
	public WindmillFarm()
	{
	}
	
	public double calculateWindmillFarmOutput(int hourOfDay)
	{
		if((hourOfDay >= 0 && hourOfDay <= 5) || (hourOfDay >= 21 && hourOfDay <= 23))
		{
			randomizeLowOutput();
		}
		else if((hourOfDay >= 6 && hourOfDay <= 9) || (hourOfDay >= 13 && hourOfDay <= 17))
		{
			randomizeModerateOutput();
		}
		else if((hourOfDay >= 10 && hourOfDay <= 12) || (hourOfDay >= 18 && hourOfDay <= 20))
		{
			randomizeHighOutput();
		}
		
		return this.powerOutput;
	}
	
	
	private void randomizeHighOutput() 
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
	}

}
