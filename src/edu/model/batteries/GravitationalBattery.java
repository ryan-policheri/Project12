package edu.model.batteries;

public class GravitationalBattery extends VolatileBattery
{
	//ATTRIBUTES
	private final double maxHeightInMeters;
	private double currentHeightInMeters;
	
	//double efficiencyModifierForStoring; may use later
	//double efficiencyModifierForReleasing; may use later
	
	//CONSTRUCTORS
	public GravitationalBattery(String batteryName, double massInKilograms, double maxHeightInMeters) 
	{
		super(batteryName, massInKilograms);
		this.maxHeightInMeters = maxHeightInMeters;
		
		this.initializeMaxEnergyInJoulesForGravitationalBattery(this.maxHeightInMeters);
		
		this.currentHeightInMeters = 0;
	}
	
	//FUNCTIONS
	
	//Puts energy in an individual battery. If the incoming energy plus the already stored energy is greater than the max capacity
	//of the battery then the battery will be filled up and the remainder will be sent back to the grid. If the incoming energy
	//plus the already stored energy is less than the max capacity then the battery will charge and there will be no remainder.
	public Surplus storeEnergy(Surplus surplus)
	{		
		double incomingEnergyInWatts = surplus.getEnergyAvailableInWatts();
		double timeIncomingEnergyLastsInSeconds = surplus.getTimeAvailableInSeconds();
		
		double incomingEnergyInJoules = incomingEnergyInWatts * timeIncomingEnergyLastsInSeconds;
		
		//hypothetically how much total energy is involved and hypothetically how high could the weight raise with that energy
		double totalSystemEnergyInJoules = this.getCurrentEnergyInJoules() + incomingEnergyInJoules;
		
		//calculate the hypothetical height using: PE = M * g * h
		//where PE = totalSystemEnergyInJoules, M = this.massInKilograms, g = this.forceOfGravity, h is height (unknown)
		double potentialHeightInMeters = totalSystemEnergyInJoules / (this.getMassInKilograms() * Battery.forceOfGravity);
		
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
			
			double remainingJoules = totalSystemEnergyInJoules - this.getMaxEnergyInJoules();
			remainingTimeOfIncomingEnergy = remainingJoules / incomingEnergyInWatts;
		}
		
		//find the new energy based on the new height
		this.adjustCurrentEnergyInJoulesForGravitationalBattery(this.currentHeightInMeters);
		//this is simply for recalibration
		this.currentHeightInMeters = this.calculateCurrentHeightInMeters(this.getCurrentEnergyInJoules());
		
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
		double secondsDemandCanBeProvided = this.getCurrentEnergyInJoules() / energyDemandInWatts;
		
		double secondsLeftWattageIsNeeded;
		double timeInUseInSeconds;
		
		//the battery could handle the entire demand, there is no remaining demand
		if (timeDemandIsNeededInSeconds <= secondsDemandCanBeProvided)
		{
			double joulesNeededBeforeEfficiencyModifier = energyDemandInWatts * timeDemandIsNeededInSeconds;
			//this is how you would do it if you had an efficiency modifier:
			//double joulesNeeded = joulesNeededBeforeEfficiencyModifier * (1 + (1 - this.efficiencyModifierForReleasing));
			//this.currentEnergyInJoules -= joulesNeeded;
			//since we don't, do it like so:
			double newCurrentEnergyInJoules = this.getCurrentEnergyInJoules() - joulesNeededBeforeEfficiencyModifier;
			
			//find the new height based on the new energy
			this.currentHeightInMeters = this.calculateCurrentHeightInMeters(newCurrentEnergyInJoules);
			//this is simply for recalibration
			this.adjustCurrentEnergyInJoulesForGravitationalBattery(this.currentHeightInMeters);

			secondsLeftWattageIsNeeded = 0;
			timeInUseInSeconds = timeDemandIsNeededInSeconds;
		}
		//the battery could not handle the entire demand, battery is empty and remaining demand is returned
		else
		{
			this.setCurrentEnergyInJoulesToZero();
			this.currentHeightInMeters = 0;
			secondsLeftWattageIsNeeded = timeDemandIsNeededInSeconds - secondsDemandCanBeProvided;
			timeInUseInSeconds = secondsDemandCanBeProvided;
		}
		
		this.startInUseTimer(timeInUseInSeconds);
		
		Demand returnDemand = new Demand(energyDemandInWatts, secondsLeftWattageIsNeeded);
		
		return returnDemand;
	}
	
	//Equation: PE = M * g * h	
	private double calculateCurrentHeightInMeters(double currentEnergyInJoules)
	{
		double heightInMeters = currentEnergyInJoules / (this.getMassInKilograms() * Battery.forceOfGravity);
		
		return heightInMeters;
	}

	public String toString()
	{                                                  
		return "GravitationalBattery: " + this.getBatteryName();
	}
	
}
