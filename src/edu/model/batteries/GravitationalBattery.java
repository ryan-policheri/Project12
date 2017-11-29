package edu.model.batteries;

import java.util.Timer;
import java.util.TimerTask;

public class GravitationalBattery extends VolatileBattery
{
	// ATTRIBUTES
	//private String batteryName;
	private final double massInKilograms;
	private final double maxHeightInMeters;
	
	//private final double forceOfGravity;
	
	//double efficiencyModifierForStoring; may use later
	//double efficiencyModifierForReleasing; may use later
	
	//private final double maxEnergyInJoules;
	
	private double currentEnergyInJoules;
	private double currentHeightInMeters;
			
	private boolean inUse;
	
	// CONSTRUCTORS
	public GravitationalBattery(String batteryName, double massInKilograms, double maxHeightInMeters) 
	{
		//this.batteryName = batteryName;
		super(batteryName, massInKilograms);
		this.massInKilograms = massInKilograms;
		this.maxHeightInMeters = maxHeightInMeters;
		
		//this.forceOfGravity = 9.81;
		
		//this.maxEnergyInJoules = this.massInKilograms * this.forceOfGravity * this.maxHeightInMeters;
		this.initializeMaxEnergyInJoulesForGravitationalBattery(this.maxHeightInMeters);
		
		this.currentEnergyInJoules = 0;
		this.currentHeightInMeters = 0;
				
		this.inUse = false;
	}
	
	// FUNCTIONS
	
	//Puts energy in an individual battery. If the incoming energy plus the already stored energy is greater than the max capacity
	//of the battery then the battery will be filled up and the remainder will be sent back to the grid. If the incoming energy
	//plus the already stored energy is less than the max capacity then the battery will charge and there will be no remainder.
	public Surplus storeEnergy(Surplus surplus)
	{		
		double incomingEnergyInWatts = surplus.getEnergyAvailableInWatts();
		double timeIncomingEnergyLastsInSeconds = surplus.getTimeAvailableInSeconds();
		
		double incomingEnergyInJoules = incomingEnergyInWatts * timeIncomingEnergyLastsInSeconds;
		
		//hypothetically how much total energy is involved and hypothetically how high could the weight raise with that energy
		double totalSystemEnergyInJoules = this.currentEnergyInJoules + incomingEnergyInJoules;
		
		//calculate the hypothetical height using: PE = M * g * h
		//where PE = totalSystemEnergyInJoules, M = this.massInKilograms, g = this.forceOfGravity, h is height (unknown)
		double potentialHeightInMeters = totalSystemEnergyInJoules / (this.massInKilograms * this.forceOfGravity);
		
		double remainingTimeOfIncomingEnergy;
		
		//the battery can hold the entire surplus, entire surplus is stored and there is none left
		if (potentialHeightInMeters <= this.maxHeightInMeters)
		{
			this.currentHeightInMeters = potentialHeightInMeters;
			remainingTimeOfIncomingEnergy = 0;
		}
		//the battery cannot hold the entire surplus, the battery is filled and the remaining surplus is returned
		else
		{
			this.currentHeightInMeters = this.maxHeightInMeters;
			
			double remainingJoules = totalSystemEnergyInJoules - this.maxEnergyInJoules;
			remainingTimeOfIncomingEnergy = remainingJoules / incomingEnergyInWatts;
		}
		
		//find the new energy based on the new height
		this.currentEnergyInJoules = this.calculateCurrentEnergyInJoules();
		//this is simply for recalibration
		this.currentHeightInMeters = this.calculateCurrentHeightInMeters();
		
		double timeInUseInSeconds = timeIncomingEnergyLastsInSeconds - remainingTimeOfIncomingEnergy;
		this.startInUseTimer(timeInUseInSeconds);
		
		Surplus returnSurplus = new Surplus(incomingEnergyInWatts, remainingTimeOfIncomingEnergy);
		
		return returnSurplus;
		
	}

	//Takes energy from an individual battery. If the energy needed is less than the energy stored, the battery handles
	//the entire demand and an empty demand is sent back to the grid. If the energy needed is more than the energy stored, 
	//the battery gives all it has to the demand and the remaining demand is sent back to the grid to be reallocated.
	public Demand releaseEnergy(Demand demand)
	{
		double energyDemandInWatts = demand.getEnergyNeededInWatts();
		double timeDemandIsNeededInSeconds = demand.getTimeNeededInSeconds();
				
		//this is how you would do it if you had an efficiency modifier:
		//double secondsDemandCanBeProvided = (this.currentEnergyInJoules * this.efficiencyModifierForReleasing) / energyDemandInWatts;
		//since we don't, do it like so:
		double secondsDemandCanBeProvided = this.currentEnergyInJoules / energyDemandInWatts;
		
		double secondsLeftWattageIsNeeded;
		
		//the battery could handle the entire demand, there is no remaining demand
		if (timeDemandIsNeededInSeconds <= secondsDemandCanBeProvided)
		{
			double joulesNeededBeforeEfficiencyModifier = energyDemandInWatts * timeDemandIsNeededInSeconds;
			//this is how you would do it if you had an efficiency modifier:
			//double joulesNeeded = joulesNeededBeforeEfficiencyModifier * (1 + (1 - this.efficiencyModifierForReleasing));
			//this.currentEnergyInJoules -= joulesNeeded;
			//since we don't, do it like so:
			this.currentEnergyInJoules -= joulesNeededBeforeEfficiencyModifier;
			
			//find the new height based on the new energy
			this.currentHeightInMeters = this.calculateCurrentHeightInMeters();
			//this is simply for recalibration
			this.currentEnergyInJoules = this.calculateCurrentEnergyInJoules();

			secondsLeftWattageIsNeeded = 0;
		}
		//the battery could not handle the entire demand, battery is empty and remaining demand is returned
		else
		{
			this.currentEnergyInJoules = 0;
			this.currentHeightInMeters = 0;
			secondsLeftWattageIsNeeded = timeDemandIsNeededInSeconds - secondsDemandCanBeProvided;
		}
		
		double timeInUseInSeconds = secondsDemandCanBeProvided;
		this.startInUseTimer(timeInUseInSeconds);
		
		Demand returnDemand = new Demand(energyDemandInWatts, secondsLeftWattageIsNeeded);
		
		return returnDemand;
	}
	
	private void startInUseTimer(double timeInUseInSeconds)
	{
		this.inUse = true;
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
	}
	
	public double getCurrentEnergyInJoules()
	{
		return this.currentEnergyInJoules;
	}
	
	public String displayBattery()
	{
		String batteryDisplay = "Battery: " + this.getBatteryName() + " - Current Storage in Joules: " + Double.toString(this.currentEnergyInJoules);
		return batteryDisplay;
	}
	
	public boolean isBatteryInUse()
	{
		return this.inUse;
	}
	
	public boolean isBatteryFull()
	{
		return this.maxHeightInMeters == this.currentHeightInMeters;
	}
	
	//Equation for calculations: PE = M * g * h
	private double calculateCurrentEnergyInJoules()
	{
		double energyInJoules = this.massInKilograms * this.forceOfGravity * this.currentHeightInMeters;
		
		return energyInJoules;
	}
	
	private double calculateCurrentHeightInMeters()
	{
		double heightInMeters = this.currentEnergyInJoules / (this.massInKilograms * this.forceOfGravity);
		
		return heightInMeters;
	}
	
	private void markBatteryNotInUse()
	{
		this.inUse = false;
	}

	public String getBatteryName()
	{
		return super.displayBattery();
	}
	
}
