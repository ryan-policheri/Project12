package edu.model.batteries;

import java.util.Timer;
import java.util.TimerTask;

//Useful site on flywheels http://large.stanford.edu/courses/2010/ph240/wheeler1/

public class RotationalBattery extends VolatileBattery
{
	//ATTRIBUTES
	private final FlywheelMaterial material;
	private final FlywheelBearing bearingType;
	
	private final double radiusInMeters;
	private final double radiusSquared;
	private final double momentOfInertia;
	
	private final double maxAngularVelocity;
	private double currentAngularVelocity;
	
	//double efficiencyModifierForStoring; may use later
	//double efficiencyModifierForReleasing; may use later
	
	//CONSTRUCTORS
	public RotationalBattery(String batteryName, double massInKilograms, double radiusInMeters, FlywheelMaterial material, FlywheelBearing bearingType) 
	{
		super(batteryName, massInKilograms);
		
		this.material = material;
		this.bearingType = bearingType;
		
		this.radiusInMeters = radiusInMeters;
		this.radiusSquared = this.radiusInMeters * this.radiusInMeters;
		this.momentOfInertia = this.radiusSquared * this.getMassInKilograms();
		
		this.maxAngularVelocity = material.calculateMaxAngularVelocity(this.radiusInMeters);
		this.initializeMaxEnergyInJoulesForRotationalBattery(this.momentOfInertia, this.maxAngularVelocity);
		
		this.currentAngularVelocity = 0;
	}
	
	//FUNCTIONS
	
	//Puts energy in an individual battery. If the incoming energy plus the already stored energy is greater than the max capacity
	//of the battery then the battery will be filled up and the remainder will be sent back to the grid. If the incoming energy
	//plus the already stored energy is less than the max capacity then the battery will charge and there will be no remainder.
	public double storeEnergy(double incomingEnergyInJoules)
	{
		double currentEnergyStoredInJoulesBeforeSurplusIsAdded = this.getCurrentEnergyInJoules();

		//hypothetically how much total energy is involved
		double totalSystemEnergyInJoules = this.getCurrentEnergyInJoules() + incomingEnergyInJoules;
		
		//calculate the hypothetical angular velocity using: KE =  I/2 * W^2
		//where KE = totalSystemEnergyInJoules, I = this.momentOfIntertia, W is angular velocity (unknown)
		double potentialAngularVelocitySquared = totalSystemEnergyInJoules / (this.momentOfInertia / 2);
		double potentialAngularVelocity = Math.sqrt(potentialAngularVelocitySquared);
		
		double remainingJoules;
		
		//the battery can hold the entire surplus, entire surplus is stored and there is none left
		if (potentialAngularVelocity <= this.maxAngularVelocity)
		{
			this.currentAngularVelocity = potentialAngularVelocity;
			remainingJoules = 0;
		}
		//the battery cannot hold the entire surplus, the battery is filled and the remaining surplus is returned
		else
		{
			this.currentAngularVelocity = this.maxAngularVelocity;
			remainingJoules = totalSystemEnergyInJoules - this.getMaxEnergyInJoules();
		}
		
		//find the new energy based on the new angular velocity
		this.adjustCurrentEnergyInJoulesForRotationalBattery(this.momentOfInertia, this.currentAngularVelocity);
		//this is simply for recalibration
		this.currentAngularVelocity = this.calculateCurrentAngularVelocity(this.getCurrentEnergyInJoules());

		//there was no energy before we started and we just put energy in. Need to start frictional loss update
		if (currentEnergyStoredInJoulesBeforeSurplusIsAdded == 0)
		{
			//this.startFrictionalLossUpdate(); //add back later
		}
		
		return remainingJoules;
	}
	
	//Takes energy from an individual battery. If the energy needed is less than the energy stored, the battery handles
	//the entire demand and an empty demand is sent back to the grid. If the energy needed is more than the energy stored, 
	//the battery gives all it has to the demand and the remaining demand is sent back to the grid to be reallocated.
	public double releaseEnergy(double energyDemandInWatts)
	{
		double wattsThatCanBeProvided = this.getCurrentEnergyInJoules();
		
		double remainingWattageNeeded;
		
		//the battery could handle the entire demand, there is no remaining demand
		if (wattsThatCanBeProvided - energyDemandInWatts >= 0)
		{
			double newCurrentEnergyInJoules = this.getCurrentEnergyInJoules() - energyDemandInWatts;
			
			//find the new angular velocity based on the new energy
			this.currentAngularVelocity = this.calculateCurrentAngularVelocity(newCurrentEnergyInJoules);
			//this is simply for recalibration
			this.adjustCurrentEnergyInJoulesForRotationalBattery(this.momentOfInertia, this.currentAngularVelocity);
			
			remainingWattageNeeded = 0;
		}
		//the battery could not handle the entire demand, battery is empty and remaining demand is returned
		else
		{
			this.setCurrentEnergyInJoulesToZero();
			this.currentAngularVelocity = 0;
			remainingWattageNeeded = energyDemandInWatts - wattsThatCanBeProvided;
		}
		
		return remainingWattageNeeded;
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
										            	if (getCurrentEnergyInJoules() == 0)
										            	{
										            		timer.cancel();
										            	}
										            }
									              }
									, 0, intervalInMilliseconds);
	}
	
	private void computeFrictionalLoss()
	{
		if (this.currentAngularVelocity > 0)
		{
			this.adjustForFrictionalLossOnRotationalBattery(this.bearingType);
			
			this.currentAngularVelocity = this.calculateCurrentAngularVelocity(this.getCurrentEnergyInJoules());
			this.adjustCurrentEnergyInJoulesForRotationalBattery(this.momentOfInertia, this.currentAngularVelocity);
		}
		
	}
	
	//Equation: KE =  I/2 * W^2
	private double calculateCurrentAngularVelocity(double currentEnergyInJoules)
	{
		double currentEnergyInJoulesDividedByIntertiaDividedByTwo = currentEnergyInJoules / (this.momentOfInertia / 2);
		double angularVelocity = Math.sqrt(currentEnergyInJoulesDividedByIntertiaDividedByTwo);
		
		return angularVelocity;
	}

	public String toString()
	{
		return "RotationalBattery: " + this.getBatteryName();
	}
	
}
