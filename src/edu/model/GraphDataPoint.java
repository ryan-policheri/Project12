package edu.model;

public class GraphDataPoint
{
	
	private double magnitudeInJoules;
	private double millisecondOfDay;

	public GraphDataPoint(double magnitudeInJoules, long millisecondOfDay)
	{
		this.magnitudeInJoules = magnitudeInJoules;
		this.millisecondOfDay = millisecondOfDay;
	}

	public double getMagnitudeInJoules()
	{
		return this.magnitudeInJoules;
	}
	
	public double getMillisecondOfDay()
	{
		return this.millisecondOfDay;
	}
	
	public String toString()
	{
		String returnString = "currentMagnitudeInJoules: " + this.magnitudeInJoules + " currentMillisecondOfDay: " + this.millisecondOfDay;
		return returnString;
	}

}
