package edu.model.batteries;

abstract class Battery 
{
	// ATTRIBUTES
	private final String batteryName;
	
	private double currentEnergyInJoules;
	protected double maxEnergyInJoules;
	
	private final double massInKilograms;
	protected final double forceOfGravity;
	
	private boolean inUse;
	
	// CONSTRUCTORS
	public Battery(String batteryName, double massInKilograms)
	{
		this.batteryName = batteryName;
		this.massInKilograms = massInKilograms;
		
		this.currentEnergyInJoules = 0;
		this.forceOfGravity = 9.81;
		this.inUse = false;

	}

	// FUNCTIONS
	public String displayBattery()
	{
		return this.batteryName;
	}
	
	//PE = M * g * h
	protected void initializeMaxEnergyInJoulesForGravitationalBattery(double maxHeightInMeters)
	{
		this.maxEnergyInJoules = this.massInKilograms * this.forceOfGravity * maxHeightInMeters; 
	}
	
	//KE =  I/2 * W^2
	protected void initializeMaxEnergyInJoulesForRotationalBattery(double momentOfIntertia, double maxAngularVelocity)
	{
		this.maxEnergyInJoules = (momentOfIntertia / 2) * (maxAngularVelocity * maxAngularVelocity);
	}
}
