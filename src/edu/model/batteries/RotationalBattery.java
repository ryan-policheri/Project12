package edu.model.batteries;

import java.util.Timer;
import java.util.TimerTask;

//Useful site on flywheels http://large.stanford.edu/courses/2010/ph240/wheeler1/

public class RotationalBattery extends Battery
{
	// ATTRIBUTES
	private final double massInKilograms;
	private final double radiusInMeters;
	private final FlywheelMaterial material;
	private final FlywheelBearing bearingType;
	
	//double efficiencyModifierForStoring; may use later
	//double efficiencyModifierForReleasing; may use later
	
	private final double radiusSquared;
	private final double maxAngularVelocity;
	private final double momentOfIntertia;
	private final double maxJoulesStorage;
	
	private double currentAngularVelocity = 0;
	private double currentEnergyInJoules = 0;
	
	private boolean inUse = false;
	
	public RotationalBattery(String batteryName, double massInKilograms, double radiusInMeters, FlywheelMaterial material, FlywheelBearing bearingType) 
	{
		this.batteryName = batteryName;
		this.massInKilograms = massInKilograms;
		this.radiusInMeters = radiusInMeters;
		this.material = material;
		this.bearingType = bearingType;
		
		this.radiusSquared = this.radiusInMeters * this.radiusInMeters;
		this.momentOfIntertia = this.radiusSquared * this.massInKilograms;
		
		this.maxAngularVelocity = material.calculateMaxAngularVelocity(this.radiusInMeters);
		this.maxJoulesStorage = (this.momentOfIntertia / 2) * (this.maxAngularVelocity * this.maxAngularVelocity);
		
		startFrictionalLossUpdate();	
	}
	
	//Puts energy in an individual battery. If the incoming energy plus the already stored energy is greater than the max capacity
	//of the battery then the battery will be filled up and the remainder will be sent back to the grid. If the incoming energy
	//plus the already stored energy is less than the max capacity then the battery will charge and there will be no remainder.
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
	
	private void startFrictionalLossUpdate()
	{
		
		long intervalInMilliseconds = 1000;
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
										            @Override
										            public void run()
										            {
										            	computeFrictionalLoss();
										            }
									              }
									, 0, intervalInMilliseconds);
	}
	
	private void computeFrictionalLoss()
	{
		if (this.currentAngularVelocity > 0)
		{
			this.currentEnergyInJoules = bearingType.computeFrictionalLoss(this.currentEnergyInJoules);
			
			calculateCurrentAngularVelocity();
			calculateCurrentEnergy();
		}
		
	}
	
	//Equation for calculation: KE =  I/2 * W^2
	private void calculateCurrentEnergy()
	{
		this.currentEnergyInJoules = (this.momentOfIntertia / 2) * (this.currentAngularVelocity * this.currentAngularVelocity);
	}
	
	private void calculateCurrentAngularVelocity()
	{
		double currentEnergyInJoulesDividedByIntertiaDividedByTwo = this.currentEnergyInJoules / (this.momentOfIntertia / 2);
		this.currentAngularVelocity = Math.sqrt(currentEnergyInJoulesDividedByIntertiaDividedByTwo);
	}
	
	private void markBatteryNotInUse()
	{
		this.inUse = false;
	}
	
	public boolean isBatteryInUse()
	{
		return this.inUse;
	}
	
	public boolean isBatteryFull()
	{
		return this.maxJoulesStorage == this.currentEnergyInJoules;
	}
	
	public double getCurrentEnergyInJoules()
	{
		return this.currentEnergyInJoules;
	}
	
	public void displayBattery()
	{
		String batteryDisplay = "Battery: " + this.batteryName + " - Current Storage in Joules: " + Double.toString(this.currentEnergyInJoules);
		System.out.println(batteryDisplay);
	}
	
}
