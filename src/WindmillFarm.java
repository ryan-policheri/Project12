
public class WindmillFarm
{

	private double powerOutput;
	private int lowestPowerOutput = 50000;
	
	public WindmillFarm()
	{
	}
	
	public double calculateWindmillFarmOutput(WindmillFarmPerformanceType performance)
	{
		if(performance.returnPerformanceType() == "High")
		{
			randomizeHighOutput();
		}
		else if(performance.returnPerformanceType() == "Moderate")
		{
			randomizeModerateOutput();
		}
		else if(performance.returnPerformanceType() == "Low")
		{
			randomizeLowOutput();
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
