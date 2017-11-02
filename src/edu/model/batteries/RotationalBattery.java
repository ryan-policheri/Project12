package edu.model.batteries;

import java.util.Timer;
import java.util.TimerTask;

//Good site on flywheels http://large.stanford.edu/courses/2010/ph240/wheeler1/

public class RotationalBattery
{
	// ATTRIBUTES
	String batteryName;
	double massInKilograms;
	double radiusInMeters;
	//double efficiencyModifierForStoring = 0.9;
	//double efficiencyModifierForReleasing = 0.75;
	String material = "Carbon Fiber"; //just go with carbon fiber for now, later make Material class and make it a parameter
	
	double radiusSquared;
	double maxAngularVelocity;
	double currentAngularVelocity = 0;
	double momentOfIntertia;
	double maxJoulesStorage;
	
	double currentEnergyInJoules = 0;
	boolean inUse = false;
	
	public RotationalBattery(String batteryName, double massInKilograms, double radiusInMeters) 
	{
		this.batteryName = batteryName;
		this.massInKilograms = massInKilograms;
		this.radiusInMeters = radiusInMeters;
		
		this.radiusSquared = this.radiusInMeters * this.radiusInMeters;
		this.momentOfIntertia = this.radiusSquared * this.massInKilograms;
		
		//calculate the max angular velocity based on parameters
		//see website at top for detail
		double densityOfCarbonFiber = 1799;
		double radiusSquaredMultipliedByDensity = this.radiusSquared * densityOfCarbonFiber;
		double tensileStressOfCarbonFiber = 4000000000.0;
		double tensileStressDividedRadiusSquaredMultipliedByDensity = tensileStressOfCarbonFiber / radiusSquaredMultipliedByDensity;
		
		this.maxAngularVelocity = Math.sqrt(tensileStressDividedRadiusSquaredMultipliedByDensity);
		this.maxJoulesStorage = (this.momentOfIntertia / 2) * (this.maxAngularVelocity * this.maxAngularVelocity);
		
		System.out.println(this.momentOfIntertia);
		System.out.println(this.maxAngularVelocity);
		System.out.println(this.maxJoulesStorage);
	}
	
	public Surplus storeEnergy(Surplus surplus)
	{
		double incomingEnergyInWatts = surplus.getEnergyAvailableInWatts();
		double timeIncomingEnergyLastsInSeconds = surplus.getTimeAvailableInSeconds();
		
		double incomingEnergyInJoules = incomingEnergyInWatts * timeIncomingEnergyLastsInSeconds;
		
		//hypothetically how much total energy is involved
		double totalSystemEnergyInJoules = this.currentEnergyInJoules + incomingEnergyInJoules;
		double momentOfIntertiaDividedByTwo = this.momentOfIntertia / 2;
		double potentialAngularVelocitySquared = totalSystemEnergyInJoules / momentOfIntertiaDividedByTwo;
		double potentialAngularVelocity = Math.sqrt(potentialAngularVelocitySquared);
		
		double remainingTimeOfIncomingEnergy;
		
		if (potentialAngularVelocity <= this.maxAngularVelocity)
		{
			this.currentAngularVelocity = potentialAngularVelocity;
			this.currentEnergyInJoules += totalSystemEnergyInJoules;
			remainingTimeOfIncomingEnergy = 0;
		}
		else
		{
			this.currentAngularVelocity = this.maxAngularVelocity;
			
			double remainingJoules = totalSystemEnergyInJoules - this.maxJoulesStorage;
			remainingTimeOfIncomingEnergy = remainingJoules / incomingEnergyInWatts;
		}
		
		calculateCurrentEnergy();
		calculateCurrentAngularVelocity();
		
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
		
		Surplus returnSurplus = new Surplus(incomingEnergyInWatts, remainingTimeOfIncomingEnergy);
		
		return returnSurplus;
		
	}
	
	public Demand releaseEnergy(Demand demand)
	{
		double energyDemandInWatts = demand.getEnergyNeededInWatts();
		double timeDemandIsNeededInSeconds = demand.getTimeNeededInSeconds();
		
		double secondsDemandCanBeProvided = this.currentEnergyInJoules / energyDemandInWatts;
		double secondsLeftWattageIsNeeded;
		
		if (timeDemandIsNeededInSeconds <= secondsDemandCanBeProvided)
		{
			double joulesNeeded = energyDemandInWatts * timeDemandIsNeededInSeconds;
			this.currentEnergyInJoules -= joulesNeeded;
			calculateCurrentAngularVelocity();
			calculateCurrentEnergy();
			
			secondsLeftWattageIsNeeded = 0;
		}
		else
		{
			this.currentEnergyInJoules = 0;
			this.currentAngularVelocity = 0;
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
		
		Demand returnDemand = new Demand(energyDemandInWatts, secondsLeftWattageIsNeeded);
		
		return returnDemand;
		
	}
	
	//KE =  I * W^2
	private void calculateCurrentEnergy()
	{
		this.currentEnergyInJoules = (this.momentOfIntertia / 2) * (this.currentAngularVelocity * this.currentAngularVelocity);
	}
	
	private void calculateCurrentAngularVelocity()
	{
		double currentEnergyInJoulesDividedByIntertiaDividedByTwo = this.currentEnergyInJoules / (this.momentOfIntertia / 2);
		this.currentAngularVelocity = Math.sqrt(currentEnergyInJoulesDividedByIntertiaDividedByTwo);
	}
	
	public void markBatteryNotInUse()
	{
		this.inUse = false;
	}
	
	public void displayBattery()
	{
		String batteryDisplay = "Battery: " + this.batteryName + " - Current Storage in Joules: " + Double.toString(this.currentEnergyInJoules);
		System.out.println(batteryDisplay);
	}
	

}
