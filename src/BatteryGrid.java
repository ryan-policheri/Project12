import java.util.ArrayList;

public class BatteryGrid
{
	ArrayList<GravitationalBattery> gravitationalBatteries = new ArrayList<GravitationalBattery>();
	
	// CONSTRUCTORS
	public BatteryGrid() 
	{	
	}
	
	public void addGravitationalBattery(GravitationalBattery gravitationalBattery)
	{
		this.gravitationalBatteries.add(gravitationalBattery);
	}
	
	public void allocateEnergySurplus(double incomingEnergyInWatts, double timeIncomingEnergyLastsInSeconds)
	{
		double highestJoules = -1;
		int highestJoulesPosition = -1;
		
		for (int x = 0; x < gravitationalBatteries.size(); x++)
		{
			double tempJoulesInBattery = gravitationalBatteries.get(x).getCurrentEnergyInJoules();
			boolean tempIsBatteryFull = gravitationalBatteries.get(x).isBatteryFull();
			boolean tempIsBatteryInUse = gravitationalBatteries.get(x).isBatteryInUse();
			
			if(tempJoulesInBattery > highestJoules && tempIsBatteryFull == false && tempIsBatteryInUse == false)
			{
				highestJoules = gravitationalBatteries.get(x).getCurrentEnergyInJoules();
				highestJoulesPosition = x;
			}
		}
		
		if(highestJoulesPosition == -1)
		{
			System.out.println("All the batteries are full or in use.");
		}
		else
		{
			double timeRemainingForDemand = gravitationalBatteries.get(highestJoulesPosition).storeEnergy(incomingEnergyInWatts, timeIncomingEnergyLastsInSeconds);
		
			if (timeRemainingForDemand > 0)
			{
				allocateEnergySurplus(incomingEnergyInWatts, timeRemainingForDemand);
			}
			else
			{
				System.out.println("Surplus succesfully allocated");
			}
		}
	}
	
	public void allocateEnergyDemand(double energyDemandInWatts, double timeDemandIsNeededInSeconds)
	{
		double lowestJoules = Double.MAX_VALUE;
		int lowestJoulesPosition = -1;
		
		for (int x = 0; x < gravitationalBatteries.size(); x++)
		{
			double tempJoulesInBattery = gravitationalBatteries.get(x).getCurrentEnergyInJoules();
			boolean tempIsBatteryInUse = gravitationalBatteries.get(x).isBatteryInUse();
			
			if(tempJoulesInBattery < lowestJoules && tempJoulesInBattery > 0 && tempIsBatteryInUse == false)
			{
				lowestJoules = gravitationalBatteries.get(x).getCurrentEnergyInJoules();
				lowestJoulesPosition = x;
			}
		}
		
		if(lowestJoulesPosition == -1)
		{
			System.out.println("Out of stored energy! Find some natural gas or something!");
		}
		else
		{
			double timeRemainingForDemand = gravitationalBatteries.get(lowestJoulesPosition).releaseEnergy(energyDemandInWatts, timeDemandIsNeededInSeconds);
		
			if (timeRemainingForDemand > 0)
			{
				allocateEnergyDemand(energyDemandInWatts, timeRemainingForDemand);
			}
			else
			{
				System.out.println("Demand succesfully allocated");
			}
		}
		
	}
	
	public void displayGrid()
	{
		for (GravitationalBattery gravitationalBattery : this.gravitationalBatteries)
		{
			gravitationalBattery.displayBattery();
		}
	}
	
	
	
}
