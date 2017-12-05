package edu.model.batteries;

<<<<<<< HEAD
abstract class Battery 
=======
public class Battery
>>>>>>> 00aa5f558b5dc9784ca4ad62cb14d8a5d5941ce9
{
	//ATTRIBUTES
	protected static final double forceOfGravity = 9.81; //CONSTANT
	
	private final String batteryName;
	private final double massInKilograms;
	
	private double currentEnergyInJoules;
	private double maxEnergyInJoules;
	
	//CONSTRUCTORS
	public Battery(String batteryName, double massInKilograms)
	{		
		this.batteryName = batteryName;
		this.massInKilograms = massInKilograms;
		
		this.currentEnergyInJoules = 0;
	}

	//FUNCTIONS
	
	//functions for initialization
	//PE = M * g * h
	protected void initializeMaxEnergyInJoulesForGravitationalBattery(double maxHeightInMeters)
	{
		this.maxEnergyInJoules = this.massInKilograms * Battery.forceOfGravity * maxHeightInMeters; 
	}
	
	//KE =  I/2 * W^2
	protected void initializeMaxEnergyInJoulesForRotationalBattery(double momentOfIntertia, double maxAngularVelocity)
	{
		this.maxEnergyInJoules = (momentOfIntertia / 2) * (maxAngularVelocity * maxAngularVelocity);
	}
	
	//functions for storing and releasing energy in children of VolatileBattery
	
	//Equation: PE = M * g * h
	protected void adjustCurrentEnergyInJoulesForGravitationalBattery(double currentHeightInMeters)
	{
		this.currentEnergyInJoules = this.massInKilograms * Battery.forceOfGravity * currentHeightInMeters;
	}
	
	//Equation: KE =  I/2 * W^2
	protected void adjustCurrentEnergyInJoulesForRotationalBattery(double momentOfInertia, double currentAngularVelocity)
	{
		this.currentEnergyInJoules = (momentOfInertia / 2) * (currentAngularVelocity * currentAngularVelocity);
	}
	
	protected void adjustForFrictionalLossOnRotationalBattery(FlywheelBearing bearingType)
	{
		this.currentEnergyInJoules = bearingType.calculateFrictionalLoss(this.currentEnergyInJoules);
	}
	
	protected void setCurrentEnergyInJoulesToZero()
	{
		this.currentEnergyInJoules = 0;
	}
<<<<<<< HEAD
	
	//functions about the state of the object
	public boolean isBatteryFull()
	{
		return this.maxEnergyInJoules == this.currentEnergyInJoules;
	}
	
	//getters
	public double getCurrentEnergyInJoules()
	{
		return this.currentEnergyInJoules;
	}
	
	public double getMaxEnergyInJoules()
	{
		return maxEnergyInJoules;
	}
	
	public double getMassInKilograms()
	{
		return this.massInKilograms;
	}

	protected String getBatteryName()
	{
		return this.batteryName;
	}
	
	//display functions
	public String displayBattery()
	{
		String batteryDisplay = "Battery: " + this.batteryName + " - Current Storage in Joules: " + Double.toString(this.currentEnergyInJoules);
		return batteryDisplay;
	}
	
=======

	public String getBatteryName()
	{
		return batteryName;
	}
>>>>>>> 00aa5f558b5dc9784ca4ad62cb14d8a5d5941ce9
}
