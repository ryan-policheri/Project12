package edu.model.batteries;

public class FlywheelBearing
{
	private final String bearingTypeName;
	private double percentFrictionalLossPerSecond;
	
	public FlywheelBearing(String bearingTypeName, double percentFrictionalLossPerSecond)
	{
		this.bearingTypeName = bearingTypeName;
		this.percentFrictionalLossPerSecond = percentFrictionalLossPerSecond;
	}

	public FlywheelBearing(String bearingTypeName)
	{
		this.bearingTypeName = bearingTypeName;

		// give the bearing a frictional loss value based on the type of bearing
		switch (bearingTypeName)
		{
			case "Mechanical":
				this.percentFrictionalLossPerSecond = 3.472222222 * java.lang.Math.pow(10, -5);
				break;
			case "Magnetic":
				this.percentFrictionalLossPerSecond = 4.166666667 * java.lang.Math.pow(10, -6);
				break;
			case "Modern":
				this.percentFrictionalLossPerSecond = 5.787037037 * java.lang.Math.pow(10, -7);
				break;
			default:
				this.percentFrictionalLossPerSecond = 0;
				break;
		}

		// Convert to simulation time
		this.percentFrictionalLossPerSecond = this.percentFrictionalLossPerSecond * 360;
	}
	
	public double calculateFrictionalLoss(double energyStoredInJoules)
	{
		double remainingJoulesStored = energyStoredInJoules * (1 - this.percentFrictionalLossPerSecond);
		
		return remainingJoulesStored;
	}
	
	public void displayBearingType()
	{
		String displayString = "The flywheel has " + this.bearingTypeName + " bearings";
		System.out.println(displayString);
	}

	@Override
	public String toString()
	{
		return "FlywheelBearing: " +
				"bearingTypeName='" + this.bearingTypeName + '\'' +
				", percentFrictionalLossPerSecond=" + this.percentFrictionalLossPerSecond;
	}
}
