package edu.model.batteries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BatteryGrid
{
	//ATTRIBUTES
	//TODO: Find a way to sort these lists - use the Comparable library, maybe?
	//TODO: Why can't this just be one list? Ask Poli
	private ArrayList<VolatileBattery> gravitationalBatteries = new ArrayList<VolatileBattery>();
	private ArrayList<VolatileBattery> rotationalBatteries = new ArrayList<VolatileBattery>();
	
	private double totalRotationalEnergyInJoules;
	private double totalGravitationalEnergyInJoules;

	private final double shortTimeThresholdInSeconds = 2.5; // this would be a 15 minutes in our simulation
	
	//CONSTRUCTORS
	public BatteryGrid() 
	{	
		gravitationalBatteries = new ArrayList<VolatileBattery>();
		rotationalBatteries = new ArrayList<VolatileBattery>();
		
		this.totalRotationalEnergyInJoules = 0;
		this.totalGravitationalEnergyInJoules= 0;
	}

	//FUNCTIONS
	public void addGravitationalBattery(GravitationalBattery gravitationalBattery)
	{
		this.gravitationalBatteries.add(gravitationalBattery);
		sortBatteries();
	}
	
	public void addRotationalBattery(RotationalBattery rotationalBattery)
	{
		this.rotationalBatteries.add(rotationalBattery);
		sortBatteries();
	}

	private void sortBatteries()
	{
		sortGravitationalBatteries();
	}

	private void sortGravitationalBatteries()
	{
		Collections.sort(this.gravitationalBatteries, Comparator.comparing(VolatileBattery::getBatteryName));
	}

	public void removeBattery(int index)
	{
		if (index < this.gravitationalBatteries.size())
		{
			System.out.println("Removing grav battery " + this.gravitationalBatteries.get(index) + " at index " + index);
			this.gravitationalBatteries.remove(index);
		}
		else
		{
			System.out.println("Removing rot battery " + this.rotationalBatteries.get(index -
					this.gravitationalBatteries.size()) + " at index " + index);
			this.rotationalBatteries.remove(index - this.gravitationalBatteries.size());
		}
	}
		
	public void allocateEnergySurplus(Surplus surplus)
	{
		double timeIncomingEnergyLastsInSeconds = surplus.getTimeAvailableInSeconds();
		
		//rotational batteries are first choice
		if (timeIncomingEnergyLastsInSeconds <= this.shortTimeThresholdInSeconds)
		{
			Surplus tempSurplus = giveEnergyToBatteries(surplus, this.rotationalBatteries);
			
			//give to gravitational batteries if there is still surplus left
			if (tempSurplus.isSurplusGone() == false)
			{
				tempSurplus = giveEnergyToBatteries(tempSurplus, this.gravitationalBatteries);
				
				if (tempSurplus.isSurplusGone() == false)
				{
					System.out.println("No place to store energy");
				}
			}
		}
		
		//gravitational batteries are first choice
		else
		{
			Surplus tempSurplus = giveEnergyToBatteries(surplus, this.gravitationalBatteries);
			
			//give to rotational batteries if there is still surplus left
			if (tempSurplus.isSurplusGone() == false)
			{
				tempSurplus = giveEnergyToBatteries(tempSurplus, this.rotationalBatteries);
				
				if (tempSurplus.isSurplusGone() == false)
				{
					System.out.println("No place to store energy");
				}
			}
		}
		
		adjustEnergyStoredByBatteryType();
	}

	public void allocateEnergyDemand(Demand demand)
	{
		double timeDemandIsNeededInSeconds = demand.getTimeNeededInSeconds();
		
		//rotational batteries are first choice
		if (timeDemandIsNeededInSeconds <= this.shortTimeThresholdInSeconds)
		{
			Demand tempDemand = takeEnergyFromBatteries(demand, this.rotationalBatteries);
			
			//give to gravitational batteries if there is still demand left
			if (tempDemand.isDemandGone() == false)
			{
				tempDemand = takeEnergyFromBatteries(tempDemand, this.gravitationalBatteries);
				
				if (tempDemand.isDemandGone() == false)
				{
					System.out.println("No stored energy");
				}
			}
		}
		
		//gravitational batteries are first choice
		else
		{
			Demand tempDemand = takeEnergyFromBatteries(demand, this.gravitationalBatteries);
			
			//give to rotational batteries if there is still demand left
			if (tempDemand.isDemandGone() == false)
			{
				tempDemand = takeEnergyFromBatteries(tempDemand, this.rotationalBatteries);
				
				if (tempDemand.isDemandGone() == false)
				{
					System.out.println("No stored energy");
				}
			}
		}
		
		adjustEnergyStoredByBatteryType();
	}
	
	private Surplus giveEnergyToBatteries(Surplus surplus, ArrayList<VolatileBattery> batteries)
	{
		//double incomingEnergyInWatts = surplus.getEnergyAvailableInWatts();
		//double timeIncomingEnergyLastsInSeconds = surplus.getTimeAvailableInSeconds();

		double highestJoules = -1;
		int highestJoulesPosition = -1;

		for (int x = 0; x < batteries.size(); x++)
		{
			double tempJoulesInBattery = batteries.get(x).getCurrentEnergyInJoules();
			boolean tempIsBatteryFull = batteries.get(x).isBatteryFull();
			boolean tempIsBatteryInUse = batteries.get(x).isBatteryInUse();
			
			if(tempJoulesInBattery > highestJoules && tempIsBatteryFull == false && tempIsBatteryInUse == false)
			{
				highestJoules = batteries.get(x).getCurrentEnergyInJoules();
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
			Surplus remainingSurplus = batteries.get(highestJoulesPosition).storeEnergy(surplus);
		
			//if there is a remaining surplus, see if there is another gravitational battery to charge
			if (remainingSurplus.isSurplusGone() == false)
			{
				return giveEnergyToBatteries(remainingSurplus, batteries);
			}
			//all of the surplus has been used, return the empty surplus so the grid knows that
			else
			{
				//System.out.println("Surplus successfully allocated");
				return remainingSurplus;
			}
		}
	}
	
	private Demand takeEnergyFromBatteries(Demand demand, ArrayList<VolatileBattery> batteries)
	{
		//double energyDemandInWatts = demand.getEnergyNeededInWatts();
		//double timeDemandIsNeededInSeconds = demand.getTimeNeededInSeconds();

		double lowestJoules = Double.MAX_VALUE;
		int lowestJoulesPosition = -1;
		
		for (int x = 0; x < batteries.size(); x++)
		{
			double tempJoulesInBattery = batteries.get(x).getCurrentEnergyInJoules();
			boolean tempIsBatteryInUse = batteries.get(x).isBatteryInUse();
			
			if(tempJoulesInBattery < lowestJoules && tempJoulesInBattery > 0 && tempIsBatteryInUse == false)
			{
				lowestJoules = batteries.get(x).getCurrentEnergyInJoules();
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
			Demand remainingDemand = batteries.get(lowestJoulesPosition).releaseEnergy(demand);
		
			//if there is a remaining demand, see if there is another gravitational battery that can take it
			if (remainingDemand.isDemandGone() == false)
			{
				return takeEnergyFromBatteries(remainingDemand, batteries);
			}
			//all of the demand has been allocated, return the empty demand so the grid knows that
			else
			{
				//System.out.println("Demand successfully allocated");
				return remainingDemand;
			}
		}	
	}
	
	private void adjustEnergyStoredByBatteryType()
	{
		this.totalGravitationalEnergyInJoules = this.calculateTotalGravitationalEnergyInJoules();
		this.totalRotationalEnergyInJoules = this.calculateTotalRotationalEnergyInJoules();
	}
	
	private double calculateTotalRotationalEnergyInJoules()
	{
		double totalRotationalEnergyInJoules = 0;
		
		for (VolatileBattery rotBat : rotationalBatteries)
		{
			totalRotationalEnergyInJoules += rotBat.getCurrentEnergyInJoules();
		}
		
		return totalRotationalEnergyInJoules;
	}
	
	private double calculateTotalGravitationalEnergyInJoules()
	{
		double totalGravitationalEnergyInJoules = 0;
		
		for (VolatileBattery gravBat : gravitationalBatteries)
		{
			totalRotationalEnergyInJoules += gravBat.getCurrentEnergyInJoules();
		}
		
		return totalRotationalEnergyInJoules;
	}
	
	public void displayGrid()
	{
		for (VolatileBattery gravitationalBattery : this.gravitationalBatteries)
		{
			System.out.println(gravitationalBattery.displayBattery());
		}

		for (VolatileBattery rotationalBattery : this.rotationalBatteries)
		{
			System.out.println(rotationalBattery.displayBattery());
		}
	}

	//region Getters
	public ArrayList<VolatileBattery> getGravitationalBatteries()
	{
		return this.gravitationalBatteries;
	}

	public ArrayList<VolatileBattery> getRotationalBatteries()
	{
		return this.rotationalBatteries;
	}
	//endregion
}
