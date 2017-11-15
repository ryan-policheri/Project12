package edu.model.batteries;// Source Control Test : Ryan Policheri

public class Battery 
{
	// ATTRIBUTES
	float maximumStorage;
	float currentStorage;
	float efficiency;
	int size;
	String batteryName;
	
	// CONSTRUCTORS
	public Battery()
	{
		
	}

	@Override
	public String toString()
	{
		return "Battery: " + batteryName;
	}
}
