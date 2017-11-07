
public class FlywheelBearing
{
	private final String bearingTypeName;
	private double percentFrictionalLossPerSecond;
	
	public FlywheelBearing(String bearingTypeName, double percentFrictionalLossPerSecond)
	{
		this.bearingTypeName = bearingTypeName;
		this.percentFrictionalLossPerSecond = percentFrictionalLossPerSecond;
	}
	
	public double computeFrictionalLoss(double energyStoredInJoules)
	{
		double remainingJoulesStored = energyStoredInJoules * (1 - this.percentFrictionalLossPerSecond);
		
		return remainingJoulesStored;
	}
	
	public void displayBearingType()
	{
		String displayString = "The flywheel has " + this.bearingTypeName + " bearings";
		System.out.println(displayString);
	}

}
