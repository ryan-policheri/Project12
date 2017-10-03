import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GravitationalBattery extends Battery 
{
	// ATTRIBUTES
	String batteryName;
	double massInKilograms;
	double maxHeightInMeters;
	
	double forceOfGravity = 9.81;
	double efficiencyModifierForStoring = 0.9;
	double efficiencyModifierForReleasing = 0.75;
	
	double currentPotentialEnergyInJoules = 0;
	double currentHeightInMeters = 0;
	
	//double maxCapacityInJoules = this.massInKilograms * this.forceOfGravity * this.maxHeightInMeters;
			
	boolean inUse = false;
	
	// CONSTRUCTORS
	public GravitationalBattery(String batteryName, double massInKilograms, double maxHeightInMeters) 
	{
		this.batteryName = batteryName;
		this.massInKilograms = massInKilograms;
		this.maxHeightInMeters = maxHeightInMeters;
	}
	
	// FUNCTIONS
	public double storeEnergy(double incomingEnergyInWatts, double timeIncomingEnergyLastsInSeconds)
	{
		//There is a bug here. When the battery fills and there is an excess amount of incoming power,
		//that excess power really should not be subject to the efficiency modifier. In fact I am not so
		//that the remaining seconds are accurate here. must reinvestigate.
		
		double incomingEnergyInJoules = incomingEnergyInWatts * timeIncomingEnergyLastsInSeconds;
		double massMultipliedByGravity = this.massInKilograms * this.forceOfGravity;
		double heightRaisedInMeters = incomingEnergyInJoules / massMultipliedByGravity;
		heightRaisedInMeters *= this.efficiencyModifierForStoring;
		
		double remainingTimeOfIncomingEnergy = -1;
		
		if (this.currentHeightInMeters + heightRaisedInMeters <= this.maxHeightInMeters)
		{
			this.currentHeightInMeters += heightRaisedInMeters;
			remainingTimeOfIncomingEnergy = 0;
		}
		else
		{
			this.currentHeightInMeters = this.maxHeightInMeters;
			
			double remainingHeight = heightRaisedInMeters - this.maxHeightInMeters;
			double remainingJoules = remainingHeight * this.massInKilograms * this.forceOfGravity;
			remainingTimeOfIncomingEnergy = remainingJoules / incomingEnergyInWatts;
			
		}
		
		this.currentPotentialEnergyInJoules = this.massInKilograms * this.forceOfGravity * this.currentHeightInMeters;
		
		this.inUse = true;
		double timeInUseInSeconds = timeIncomingEnergyLastsInSeconds - remainingTimeOfIncomingEnergy;
		long timeInUseInMilliseconds = (long) (timeInUseInSeconds * 1000);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
										  @Override
										  public void run()
										  {
											  markBatteryNotInUse();
											  timer.cancel();
										  }
									   }
						, timeInUseInMilliseconds);
		
		return remainingTimeOfIncomingEnergy;
		
	}
	
	public double releaseEnergy(double energyDemandInWatts, double timeDemandIsNeededInSeconds)
	{
		double secondsDemandCanBeProvided = (this.currentPotentialEnergyInJoules * this.efficiencyModifierForReleasing) / energyDemandInWatts;
		double secondsLeftWattageIsNeeded;
		
		if (timeDemandIsNeededInSeconds <= secondsDemandCanBeProvided)
		{
			double joulesNeeded = energyDemandInWatts * timeDemandIsNeededInSeconds;
			double heightLowered = joulesNeeded / (this.massInKilograms * this.forceOfGravity);
			heightLowered += heightLowered - (heightLowered * this.efficiencyModifierForReleasing);
			this.currentHeightInMeters -= heightLowered;
			
			this.currentPotentialEnergyInJoules = this.massInKilograms * this.forceOfGravity * this.currentHeightInMeters;
			secondsLeftWattageIsNeeded = 0;
		}
		else
		{
			this.currentPotentialEnergyInJoules = 0;
			this.currentHeightInMeters = 0;
			secondsLeftWattageIsNeeded = timeDemandIsNeededInSeconds - secondsDemandCanBeProvided;
		}
		
		this.inUse = true;
		double timeInUseInSeconds = secondsDemandCanBeProvided;
		long timeInUseInMilliseconds = (long) (timeInUseInSeconds * 1000);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
										  @Override
										  public void run()
										  {
											  markBatteryNotInUse();
											  timer.cancel();
										  }
									   }
						, timeInUseInMilliseconds);
		
		return secondsLeftWattageIsNeeded;
	}
	
	public double getCurrentEnergyInJoules()
	{
		return this.currentPotentialEnergyInJoules;
	}
	
	public void displayBattery()
	{
		String batteryDisplay = "Battery: " + this.batteryName + " - Current Storage in Joules: " + Double.toString(this.currentPotentialEnergyInJoules);
		System.out.println(batteryDisplay);
	}
	
	public boolean isBatteryInUse()
	{
		return this.inUse;
	}
	
	public boolean isBatteryFull()
	{
		return this.maxHeightInMeters == this.currentHeightInMeters;
	}
	
	public void markBatteryNotInUse()
	{
		this.inUse = false;
	}
}
