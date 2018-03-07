package edu.model.batteries;

import java.util.ArrayList;

public class BatteryGrid
{
	//ATTRIBUTES
	private ArrayList<VolatileBattery> gravitationalBatteries;
	private ArrayList<VolatileBattery> rotationalBatteries;

	//private ArrayList<ConstantFlow> heindlBatteries;
	
	private double minimumVolatileEnergyInWatts; //5 Gigawatts

	private double energyWasted;
	private double energyShortageInWatts;
	private int amountOfEnergyShortages;
	
	//CONSTRUCTORS
	public BatteryGrid() 
	{	
		this.gravitationalBatteries = new ArrayList<VolatileBattery>();
		this.rotationalBatteries = new ArrayList<VolatileBattery>();

		//this.heindlBatteries = new ArrayList<ConstantFlow>();

		this.minimumVolatileEnergyInWatts = 5000000000.0;

		this.energyWasted = 0;
		this.energyShortageInWatts = 0;
		this.amountOfEnergyShortages = 0;
	}

	//FUNCTIONS
	public void allocateEnergySurplus(double energySurplusInWatts)
	{
		double leftOverEnergyInWatts = 0;

		if (this.calculateCurrentVolatileEnergyInJoules() < this.minimumVolatileEnergyInWatts)
		{
			leftOverEnergyInWatts = this.giveEnergyToVolatileBatteries(this.rotationalBatteries, energySurplusInWatts,true); //true indicates that we are filling only to the minimum threshold
		}

		//fill as much constant flow batteries as you can
		//leftOverEnergyInWatts = this.giveEnergyToConstantFlowBatteries(this.heindlBatteries, leftOverEnergyInWatts);

		//put the excess energy(that could not fit in the constant flow filling process) in to volatile batteries
		leftOverEnergyInWatts = this.giveEnergyToVolatileBatteries(this.rotationalBatteries, leftOverEnergyInWatts, false); //false indicates fill as much as you can

		if(leftOverEnergyInWatts > 0)
		{
			this.energyWasted += leftOverEnergyInWatts;
		}
	}

	public void allocateEnergyDemand(double energyDemandInWatts)
	{
		double energyStillNeededToFullfillDemand = energyDemandInWatts;

		//pull what is able to be pulled from the constant flow batteries
		//double constantFlowEnergy = this.takeEnergyFromConstantFlowBatteries(this.heindlBatteries);

		//if constantFlowEnergy > energyDemandInWatts -in the case that the constant flows were geared to release more than needed
			//double excessEnergyPulled = constantFlowEnergy - energyDemandInWatts;
			//this.allocateEnergySurplus(excessEnergyPulled);
			//constantFlowEnergy = energyDemandInWatts

		//energyStillNeededToFullfillDemand = energyDemandInWatts - constantFlowEnergy

		if (energyStillNeededToFullfillDemand > 0)
		{
			energyStillNeededToFullfillDemand = this.takeEnergyFromVolatileBatteries(this.rotationalBatteries, energyStillNeededToFullfillDemand);
			if (energyStillNeededToFullfillDemand > 0)
			{
				this.energyShortageInWatts += energyStillNeededToFullfillDemand;
				this.amountOfEnergyShortages += 1;
			}
		}
	}
	
	private double giveEnergyToVolatileBatteries(ArrayList<VolatileBattery> batteries, double energySurplusInWatts, boolean fillOnlyToVolatileEnergyMinimum)
	{
		double highestJoules = -1;
		int highestJoulesPosition = -1;

		for (int x = 0; x < batteries.size(); x++)
		{
			double tempJoulesInBattery = batteries.get(x).getCurrentEnergyInJoules();
			boolean tempIsBatteryFull = batteries.get(x).isBatteryFull();
			
			if(tempJoulesInBattery > highestJoules && tempIsBatteryFull == false)
			{
				highestJoules = batteries.get(x).getCurrentEnergyInJoules();
				highestJoulesPosition = x;
			}
		}
		
		//all batteries were all full, return the surplus as is
		if(highestJoulesPosition == -1)
		{
			return energySurplusInWatts;
		}
		
		//there was at least one available battery that was not completely full
		else
		{
			double remainingSurplus = batteries.get(highestJoulesPosition).storeEnergy(energySurplusInWatts);
		
			//if there is a remaining surplus and we want to fill indefinitely, see if there is another volatile battery to charge
			if (remainingSurplus > 0 && fillOnlyToVolatileEnergyMinimum == false)
			{
				return giveEnergyToVolatileBatteries(batteries, remainingSurplus, fillOnlyToVolatileEnergyMinimum);
			}
			//if there is a remaining surplus and we want to stop fill ASAP and we are still below the minimum volatile energy threshold
			else if (remainingSurplus > 0 && fillOnlyToVolatileEnergyMinimum == true && this.calculateCurrentVolatileEnergyInJoules() < this.minimumVolatileEnergyInWatts)
			{
				return giveEnergyToVolatileBatteries(batteries, remainingSurplus, fillOnlyToVolatileEnergyMinimum);
			}
			//all of the surplus has been used or we want to stop filling volatiles, return the surplus so the grid knows that
			else
			{
				return remainingSurplus;
			}
		}
	}
	
	private double takeEnergyFromVolatileBatteries(ArrayList<VolatileBattery> batteries, double energyDemandInWatts)
	{
		double lowestJoules = Double.MAX_VALUE;
		int lowestJoulesPosition = -1;
		
		for (int x = 0; x < batteries.size(); x++)
		{
			double tempJoulesInBattery = batteries.get(x).getCurrentEnergyInJoules();
			
			if(tempJoulesInBattery < lowestJoules && tempJoulesInBattery > 0)
			{
				lowestJoules = batteries.get(x).getCurrentEnergyInJoules();
				lowestJoulesPosition = x;
			}
		}
		
		//If they were all empty, return the demand as is
		if(lowestJoulesPosition == -1)
		{
			return energyDemandInWatts;
		}
		else
		{
			double remainingDemand = batteries.get(lowestJoulesPosition).releaseEnergy(energyDemandInWatts);
		
			//if there is a remaining demand, see if there is another volatile battery that can take it
			if (remainingDemand > 0)
			{
				return this.takeEnergyFromVolatileBatteries(batteries, remainingDemand);
			}
			//all of the demand has been allocated, return the empty demand so the grid knows that
			else
			{
				return remainingDemand;
			}
		}	
	}

/*	private double giveEnergyToConstantFlowBatteries(ArrayList<ConstantFlow> batteries, double energySurplusInWatts)
	{
		return leftoverEnergyInWatts;
	}

	private double takeAllConstantFlowEnergyAvailable(ArrayList<ConstantFlow> batteries)
	{
		return allConstantFlowEnergyAvailableInWatts;
	}*/

	//region add and remove batteries
	public void addGravitationalBattery(GravitationalBattery gravitationalBattery)
	{
		this.gravitationalBatteries.add(gravitationalBattery);
	}

	public void addRotationalBattery(RotationalBattery rotationalBattery)
	{
		this.rotationalBatteries.add(rotationalBattery);
	}

	public void addListOfRotationalBatteries(ArrayList<VolatileBattery> volatileBatteries)
	{
		this.rotationalBatteries.addAll(volatileBatteries);
	}

	public void removeRotationalBattery(int index)
	{
		this.rotationalBatteries.remove(index);
	}

	public void removeAllBatteries()
	{
		this.gravitationalBatteries.clear();
		this.rotationalBatteries.clear();
	}
	//endregion

	//region sum current battery energy and battery energy capacity
	public double calculateCurrentVolatileEnergyInJoules()
	{
		double currentEnergyInJoules = 0;

		// Calculate energy of the rotational batteries
		for (Battery battery : this.rotationalBatteries)
		{
			currentEnergyInJoules += battery.getCurrentEnergyInJoules();
		}

		return currentEnergyInJoules;
	}

	public double calculateCurrentConstantFlowEnergyInJoules()
	{
		double currentEnergyInJoules = 0;

/*		// Calculate energy of the rotational batteries
		for (Battery battery : this.heindlBatteries)
		{
			currentEnergyInJoules += battery.getCurrentEnergyInJoules();
		}*/

		return currentEnergyInJoules;
	}

	public double calculateMaxVolatileEnergyInJoules()
	{
		double maxEnergyInJoules = 0;

		// Calculate energy of the rotational batteries
		for (Battery battery : this.rotationalBatteries)
		{
			maxEnergyInJoules += battery.getMaxEnergyInJoules();
		}

		return maxEnergyInJoules;
	}

	public double calculateMaxConstantFlowEnergyInJoules()
	{
		double maxEnergyInJoules = 0;

/*		// Calculate energy of the constant flow batteries
		for (Battery battery : this.heindlBatteries)
		{
			maxEnergyInJoules += battery.getMaxEnergyInJoules();
		}*/

		return maxEnergyInJoules;
	}
	//endregion

	public void displayGrid()
	{
/*		for (Battery battery : this.heindlBatteries)
		{
			System.out.println(battery.displayBattery());
		}*/

		for (Battery rotationalBattery : this.rotationalBatteries)
		{
			System.out.println(rotationalBattery.displayBattery());
		}
	}

	//region Getters/Setters
	public ArrayList<VolatileBattery> getGravitationalBatteries()
	{
		return this.gravitationalBatteries;
	}

	public ArrayList<VolatileBattery> getRotationalBatteries()
	{
		return this.rotationalBatteries;
	}

	public double getEnergyShortageInWatts()
	{
		return energyShortageInWatts;
	}

	public int getAmountOfEnergyShortages()
	{
		return amountOfEnergyShortages;
	}

	public double getEnergyWasted()
	{
		return energyWasted;
	}
	//endregion
	
}
