package edu.model.batteries;

public class Battery 
{
	// ATTRIBUTES
	private String batteryName;
	
	// CONSTRUCTORS
	public Battery()
	{
		
	}

	// FUNCTIONS
	@Override
	public String toString()
	{
		return "Battery: " + this.batteryName;
	}
}
