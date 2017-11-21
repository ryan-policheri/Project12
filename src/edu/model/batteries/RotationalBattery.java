package edu.model.batteries;

import java.util.Timer;
import java.util.TimerTask;

//Useful site on flywheels http://large.stanford.edu/courses/2010/ph240/wheeler1/

public class RotationalBattery extends VolatileBattery
{
	// ATTRIBUTES
	private String batteryName;
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
	
	private double currentAngularVelocity;
	private double currentEnergyInJoules;
	
	private boolean inUse;
	
	// CONSTRUCTORS
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
		
		this.currentAngularVelocity = 0;
		this.currentEnergyInJoules = 0;
		
		this.inUse = false;
		
		this.startFrictionalLossUpdate();	
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
		
		//hypothetically how much total energy is involved
		double totalSystemEnergyInJoules = this.currentEnergyInJoules + incomingEnergyInJoules;
		
		//calculate the hypothetical angular velocity using: KE =  I/2 * W^2
		//where KE = totalSystemEnergyInJoules, I = this.momentOfIntertia, W is angular velocity (unknown)
		double potentialAngularVelocitySquared = totalSystemEnergyInJoules / (this.momentOfIntertia / 2);
		double potentialAngularVelocity = Math.sqrt(potentialAngularVelocitySquared);
		
		double remainingTimeOfIncomingEnergy;
		
		//the battery can hold the entire surplus, entire surplus is stored and there is none left
		if (potentialAngularVelocity <= this.maxAngularVelocity)
		{
			this.currentAngularVelocity = potentialAngularVelocity;
			this.currentEnergyInJoules = totalSystemEnergyInJoules; //it used to be += ? dont know why
			remainingTimeOfIncomingEnergy = 0;
		}
		//the battery cannot hold the entire surplus, the battery is filled and the remaining surplus is returned
		else
		{
			this.currentAngularVelocity = this.maxAngularVelocity;
			
			double remainingJoules = totalSystemEnergyInJoules - this.maxJoulesStorage;
			remainingTimeOfIncomingEnergy = remainingJoules / incomingEnergyInWatts;
		}
		
		//find the new energy based on the new angular velocity
		this.currentEnergyInJoules = this.calculateCurrentEnergy();
		//this is simply for recalibration
		this.currentAngularVelocity = this.calculateCurrentAngularVelocity();
		
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
		
		double secondsDemandCanBeProvided = this.currentEnergyInJoules / energyDemandInWatts;
		double remainingSecondsWattageIsNeeded;
		
		//the battery could handle the entire demand, there is no remaining demand
		if (timeDemandIsNeededInSeconds <= secondsDemandCanBeProvided)
		{
			double joulesNeeded = energyDemandInWatts * timeDemandIsNeededInSeconds;
			this.currentEnergyInJoules -= joulesNeeded;
			//find the new angular velocity based on the new energy
			this.currentAngularVelocity = this.calculateCurrentAngularVelocity();
			//this is simply for recalibration
			this.currentEnergyInJoules = this.calculateCurrentEnergy();
			
			remainingSecondsWattageIsNeeded = 0;
		}
		//the battery could not handle the entire demand, battery is empty and remaining demand is returned
		else
		{
			this.currentEnergyInJoules = 0;
			this.currentAngularVelocity = 0;
			remainingSecondsWattageIsNeeded = timeDemandIsNeededInSeconds - secondsDemandCanBeProvided;
		}
		
		double timeInUseInSeconds = secondsDemandCanBeProvided;
		this.startInUseTimer(timeInUseInSeconds);
		
		Demand returnDemand = new Demand(energyDemandInWatts, remainingSecondsWattageIsNeeded);
		
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
	
	private void computeFrictionalLoss()
	{
		if (this.currentAngularVelocity > 0)
		{
			this.currentEnergyInJoules = this.bearingType.computeFrictionalLoss(this.currentEnergyInJoules);
			
			calculateCurrentAngularVelocity();
			calculateCurrentEnergy();
		}
		
	}
	
	//Equation for calculations: KE =  I/2 * W^2
	private double calculateCurrentEnergy()
	{
		double energyInJoules = (this.momentOfIntertia / 2) * (this.currentAngularVelocity * this.currentAngularVelocity);
	
		return energyInJoules;
	}
	
	private double calculateCurrentAngularVelocity()
	{
		double currentEnergyInJoulesDividedByIntertiaDividedByTwo = this.currentEnergyInJoules / (this.momentOfIntertia / 2);
		double angularVelocity = Math.sqrt(currentEnergyInJoulesDividedByIntertiaDividedByTwo);
		
		return angularVelocity;
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
