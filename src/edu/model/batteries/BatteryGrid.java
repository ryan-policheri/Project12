package edu.model.batteries;

import java.util.ArrayList;

public class BatteryGrid
{
	private ArrayList<GravitationalBattery> gravitationalBatteries = new ArrayList<GravitationalBattery>();
	private ArrayList<RotationalBattery> rotationalBatteries = new ArrayList<RotationalBattery>();
	
	private double totalRotationalEnergyInJoules;
	private double totalGravitationalEnergyInJoules;

	double shortTimeThresholdInSeconds = 2.5; // this would be a 15 minutes in our simulation
	
	// CONSTRUCTORS
	public BatteryGrid() 
	{	
		totalRotationalEnergyInJoules = 0;
		totalGravitationalEnergyInJoules = 0;
	}
	
	public void addGravitationalBattery(GravitationalBattery gravitationalBattery)
	{
		this.gravitationalBatteries.add(gravitationalBattery);
	}
	
	public void addRotationalBattery(RotationalBattery rotationalBattery)
	{
		this.rotationalBatteries.add(rotationalBattery);
	}
		
	public void allocateEnergySurplus(Surplus surplus)
	{
		double timeIncomingEnergyLastsInSeconds = surplus.getTimeAvailableInSeconds();
		
		//rotational batteries are first choice
		if (timeIncomingEnergyLastsInSeconds <= this.shortTimeThresholdInSeconds)
		{
			Surplus tempSurplus = giveEnergyToRotationalBatteries(surplus);
			
			//give to gravitational batteries if there is still surplus left
			if (tempSurplus.isSurplusGone() == false)
			{
				tempSurplus = giveEnergyToGravitationalBatteries(tempSurplus);
				
				if (tempSurplus.isSurplusGone() == false)
				{
					System.out.println("No place to store energy");
				}
			}
		}
		
		//gravitational batteries are first choice
		else
		{
			Surplus tempSurplus = giveEnergyToGravitationalBatteries(surplus);
			
			//give to rotational batteries if there is still surplus left
			if (tempSurplus.isSurplusGone() == false)
			{
				tempSurplus = giveEnergyToRotationalBatteries(tempSurplus);
				
				if (tempSurplus.isSurplusGone() == false)
				{
					System.out.println("No place to store energy");
				}
			}
		}

	}

	public void allocateEnergyDemand(Demand demand)
	{
		double timeDemandIsNeededInSeconds = demand.getTimeNeededInSeconds();
		
		//rotational batteries are first choice
		if (timeDemandIsNeededInSeconds <= this.shortTimeThresholdInSeconds)
		{
			Demand tempDemand = takeEnergyFromRotationalBatteries(demand);
			
			//give to gravitational batteries if there is still demand left
			if (tempDemand.isDemandGone() == false)
			{
				tempDemand = takeEnergyFromRotationalBatteries(tempDemand);
				
				if (tempDemand.isDemandGone() == false)
				{
					System.out.println("No stored energy");
				}
			}
		}
		
		//gravitational batteries are first choice
		else
		{
			Demand tempDemand = takeEnergyFromGravitationalBatteries(demand);
			
			//give to rotational batteries if there is still surplus left
			if (tempDemand.isDemandGone() == false)
			{
				tempDemand = takeEnergyFromRotationalBatteries(tempDemand);
				
				if (tempDemand.isDemandGone() == false)
				{
					System.out.println("No stored energy");
				}
			}
		}
		
	}
	
	private Surplus giveEnergyToGravitationalBatteries(Surplus surplus)
	{
		//double incomingEnergyInWatts = surplus.getEnergyAvailableInWatts();
		//double timeIncomingEnergyLastsInSeconds = surplus.getTimeAvailableInSeconds();

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
		
		//there were no available batteries or they were all full, return the surplus as is
		if(highestJoulesPosition == -1)
		{
			return surplus;
		}
		
		//there was at least one available battery that was not completely full
		else
		{
			Surplus remainingSurplus = gravitationalBatteries.get(highestJoulesPosition).storeEnergy(surplus);
		
			//if there is a remaining surplus, see if there is another gravitational battery to charge
			if (remainingSurplus.isSurplusGone() == false)
			{
				return giveEnergyToGravitationalBatteries(remainingSurplus);
			}
			//all of the surplus has been used, return the empty surplus so the grid knows that
			else
			{
				//System.out.println("Surplus successfully allocated");
				return remainingSurplus;
			}
		}
	}
	
	
	private Surplus giveEnergyToRotationalBatteries(Surplus surplus)
	{
		//double incomingEnergyInWatts = surplus.getEnergyAvailableInWatts();
		//double timeIncomingEnergyLastsInSeconds = surplus.getTimeAvailableInSeconds();

		double highestJoules = -1;
		int highestJoulesPosition = -1;

		for (int x = 0; x < rotationalBatteries.size(); x++)
		{
			double tempJoulesInBattery = rotationalBatteries.get(x).getCurrentEnergyInJoules();
			boolean tempIsBatteryFull = rotationalBatteries.get(x).isBatteryFull();
			boolean tempIsBatteryInUse = rotationalBatteries.get(x).isBatteryInUse();
			
			if(tempJoulesInBattery > highestJoules && tempIsBatteryFull == false && tempIsBatteryInUse == false)
			{
				highestJoules = rotationalBatteries.get(x).getCurrentEnergyInJoules();
				highestJoulesPosition = x;
			}
		}
		
		//there were no available batteries or they were all full, return the surplus as is
		if(highestJoulesPosition == -1)
		{
			return surplus;
		}
		
		//there was at least one available battery that was not completely full
		else
		{
			Surplus remainingSurplus = rotationalBatteries.get(highestJoulesPosition).storeEnergy(surplus);
		
			//if there is a remaining surplus, see if there is another rotational battery to charge
			if (remainingSurplus.isSurplusGone() == false)
			{
				return giveEnergyToRotationalBatteries(remainingSurplus);
			}
			//all of the surplus has been used, return the empty surplus so the grid knows that
			else
			{
				//System.out.println("Surplus successfully allocated");
				return remainingSurplus;
			}
		}
	}
	
	private Demand takeEnergyFromGravitationalBatteries(Demand demand)
	{
		//double energyDemandInWatts = demand.getEnergyNeededInWatts();
		//double timeDemandIsNeededInSeconds = demand.getTimeNeededInSeconds();

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
		
		//there were no available batteries or they were all empty, return the demand as is
		if(lowestJoulesPosition == -1)
		{
			return demand;
		}
		else
		{
			Demand remainingDemand = gravitationalBatteries.get(lowestJoulesPosition).releaseEnergy(demand);
		
			//if there is a remaining demand, see if there is another gravitational battery that can take it
			if (remainingDemand.isDemandGone() == false)
			{
				return takeEnergyFromGravitationalBatteries(remainingDemand);
			}
			//all of the demand has been allocated, return the empty demand so the grid knows that
			else
			{
				//System.out.println("Demand successfully allocated");
				return remainingDemand;
			}
		}	
	}
	
	private Demand takeEnergyFromRotationalBatteries(Demand demand)
	{
		//double energyDemandInWatts = demand.getEnergyNeededInWatts();
		//double timeDemandIsNeededInSeconds = demand.getTimeNeededInSeconds();

		double lowestJoules = Double.MAX_VALUE;
		int lowestJoulesPosition = -1;
		
		for (int x = 0; x < rotationalBatteries.size(); x++)
		{
			double tempJoulesInBattery = rotationalBatteries.get(x).getCurrentEnergyInJoules();
			boolean tempIsBatteryInUse = rotationalBatteries.get(x).isBatteryInUse();
			
			if(tempJoulesInBattery < lowestJoules && tempJoulesInBattery > 0 && tempIsBatteryInUse == false)
			{
				lowestJoules = rotationalBatteries.get(x).getCurrentEnergyInJoules();
				lowestJoulesPosition = x;
			}
		}
		
		//there were no available batteries or they were all empty, return the demand as is
		if(lowestJoulesPosition == -1)
		{
			return demand;
		}
		else
		{
			Demand remainingDemand = rotationalBatteries.get(lowestJoulesPosition).releaseEnergy(demand);
		
			//if there is a remaining demand, see if there is another rotational battery that can take it
			if (remainingDemand.isDemandGone() == false)
			{
				return takeEnergyFromRotationalBatteries(remainingDemand);
			}
			//all of the demand has been allocated, return the empty demand so the grid knows that
			else
			{
				//System.out.println("Demand successfully allocated");
				return remainingDemand;
			}
		}	
	}
	
	private double calculateTotalRotationalEnergyInJoules()
	{
		double totalRotationalEnergyInJoules = 0;
		
		for (RotationalBattery rotBat : rotationalBatteries)
		{
			totalRotationalEnergyInJoules += rotBat.getCurrentEnergyInJoules();
		}
		
		return totalRotationalEnergyInJoules;
	}
	
	private double calculateTotalGravitationalEnergyInJoules()
	{
		double totalGravitationalEnergyInJoules = 0;
		
		for (GravitationalBattery gravBat : gravitationalBatteries)
		{
			totalRotationalEnergyInJoules += gravBat.getCurrentEnergyInJoules();
		}
		
		return totalRotationalEnergyInJoules;
	}
	
	public void displayGrid()
	{
		for (GravitationalBattery gravitationalBattery : this.gravitationalBatteries)
		{
			gravitationalBattery.displayBattery();
		}

		for (RotationalBattery rotationalBattery : this.rotationalBatteries)
		{
			rotationalBattery.displayBattery();
		}
	}
	
	
	
}
