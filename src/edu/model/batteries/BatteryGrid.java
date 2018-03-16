package edu.model.batteries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import edu.controllers.Controller;
import edu.controllers.TempDriver;
import edu.model.city.City;

public class BatteryGrid
{
	//ATTRIBUTES
	private ArrayList<VolatileBattery> gravitationalBatteries;
	private ArrayList<VolatileBattery> rotationalBatteries;

	private ArrayList<ConstantFlowBattery> heindlBatteries;
	
	private double minimumVolatileEnergyInWatts; //5 Gigawatts

	private double energyWasted;
	private double energyShortageInWatts;
	private int amountOfEnergyShortages;

	private int currentSample;
	private int samplesPerTier;
	private int currentHourlyMaxIndex;
	
	//CONSTRUCTORS
	public BatteryGrid() 
	{	
		this.gravitationalBatteries = new ArrayList<VolatileBattery>();
		this.rotationalBatteries = new ArrayList<VolatileBattery>();
		this.heindlBatteries = new ArrayList<ConstantFlowBattery>();

		this.minimumVolatileEnergyInWatts = 5000000000.0;

		this.energyWasted = 0;
		this.energyShortageInWatts = 0;
		this.amountOfEnergyShortages = 0;
		this.currentSample = 0;
		this.currentHourlyMaxIndex = 0;
		this.samplesPerTier = 10; //I Cheated this needs to be changed at some point!
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
		leftOverEnergyInWatts = this.giveEnergyToConstantFlowBatteries(this.heindlBatteries, leftOverEnergyInWatts);

		//put the excess energy(that could not fit in the constant flow filling process) in to volatile batteries
		leftOverEnergyInWatts = this.giveEnergyToVolatileBatteries(this.rotationalBatteries, leftOverEnergyInWatts, false); //false indicates fill as much as you can

		if(leftOverEnergyInWatts > 0)
		{
			this.energyWasted += leftOverEnergyInWatts;
		}
	}

	public void allocateEnergyDemand(double energyDemandInWatts)
	{
		currentSample++;

		if (currentSample == samplesPerTier)
		{
			currentHourlyMaxIndex++;
			currentSample = 0;
		}

		for (int i = 0; i < this.heindlBatteries.size() ; i++)
		{
			this.heindlBatteries.get(i).setGaveEnergyThisSampleToFalse();
		}

		double energyStillNeededToFulfillDemand = energyDemandInWatts;

		//pull what is able to be pulled from the constant flow batteries

		City desMoines = new City("Des Moines", 0.1); //temp so that TempDriver works

		double[] maximumReleaseForConstantFlowByHours = CalculateMaximumEnergyReleaseByHourForConstantFlowBatteries(desMoines); //temp to work with TempDriver
		double currentMaximumReleaseForConstantFlowTier = maximumReleaseForConstantFlowByHours[currentHourlyMaxIndex];
		double constantFlowEnergy = this.takeEnergyFromConstantFlowBatteries(this.heindlBatteries, energyStillNeededToFulfillDemand, currentMaximumReleaseForConstantFlowTier);

		energyStillNeededToFulfillDemand = energyDemandInWatts - constantFlowEnergy;

		if (energyStillNeededToFulfillDemand > 0)
		{
			energyStillNeededToFulfillDemand = this.takeEnergyFromVolatileBatteries(this.rotationalBatteries, energyStillNeededToFulfillDemand);
			if (energyStillNeededToFulfillDemand > 0)
			{
				this.energyShortageInWatts += energyStillNeededToFulfillDemand;
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

	private double giveEnergyToConstantFlowBatteries(ArrayList<ConstantFlowBattery> batteries, double energySurplusInWatts)
	{
		double highestJoules = -1;
		int highestJoulesPosition = -1;

		for (int x = 0; x < batteries.size(); x++)
		{
			double tempJoulesInBattery = batteries.get(x).getCurrentEnergyInJoules();
			boolean tempIsBatteryFull = batteries.get(x).isBatteryFull();
			boolean tempIsBatteryCharging = batteries.get(x).checkIfCharging();

			if(tempJoulesInBattery > highestJoules && tempIsBatteryFull == false && tempIsBatteryCharging == true)
			{
				highestJoules = batteries.get(x).getCurrentEnergyInJoules();
				highestJoulesPosition = x;
			}

			//all batteries were all full, return the surplus as is

		}
		if(highestJoulesPosition == -1)
		{
			return energySurplusInWatts;
		}
		//there was at least one available battery that was not completely full
		else
		{
			double remainingSurplus = batteries.get(highestJoulesPosition).storeEnergy(energySurplusInWatts);

			//if there is a remaining surplus and we want to fill indefinitely, see if there is another volatile battery to charge
			if (remainingSurplus > 0)
			{
				return giveEnergyToConstantFlowBatteries(batteries, remainingSurplus);
			}
			//all of the surplus has been used or we want to stop filling, return the surplus so the grid knows that
			else
			{
				return remainingSurplus;
			}
		}
	}


	private double takeEnergyFromConstantFlowBatteries(ArrayList<ConstantFlowBattery> batteries, double energyDemandInWatts, double maxEnergyForTier)
	{
		double highestJoules = -1;
		int highestJoulesPosition = -1;
		double constantFlowEnergyUsed = 0;

		for (int x = 0; x < batteries.size(); x++)
		{
			double tempJoulesInBattery = batteries.get(x).getCurrentEnergyInJoules();
			boolean tempIsBatteryCharging = batteries.get(x).checkIfCharging();
			boolean tempBatteryUsedThisSample = batteries.get(x).checkIfGaveEnergyThisHour();

			if(highestJoules < tempJoulesInBattery && tempJoulesInBattery > 0 && !tempIsBatteryCharging && !tempBatteryUsedThisSample)
			{
				highestJoules = batteries.get(x).getCurrentEnergyInJoules();
				highestJoulesPosition = x;
			}
		}

		//If they were all empty or charging, return the demand as is
		if(highestJoulesPosition == -1)
		{
			return constantFlowEnergyUsed;
		}
		else
		{
			batteries.get(highestJoulesPosition).setGaveEnergyThisSampleToTrue();
			double remainingDemand = batteries.get(highestJoulesPosition).releaseEnergy(energyDemandInWatts, maxEnergyForTier);
			constantFlowEnergyUsed = energyDemandInWatts - remainingDemand;

			//if there is a remaining demand, see if there is another battery that can take it
			if (remainingDemand > 0)
			{
				maxEnergyForTier -= remainingDemand;
				return this.takeEnergyFromConstantFlowBatteries(batteries, remainingDemand, maxEnergyForTier);
			}
			//all of the demand has been allocated, return the empty demand so the grid knows that
			else
			{
				return constantFlowEnergyUsed;
			}
		}
	}

	//region add and remove batteries
	public void addGravitationalBattery(GravitationalBattery gravitationalBattery)
	{
		this.gravitationalBatteries.add(gravitationalBattery);
	}

	public void addRotationalBattery(RotationalBattery rotationalBattery)
	{
		this.rotationalBatteries.add(rotationalBattery);
	}

	public void addHeindlBattery(HydroelectricBattery heindlBattery)
	{
		this.heindlBatteries.add(heindlBattery);
	}

	public void addListOfRotationalBatteries(ArrayList<VolatileBattery> volatileBatteries)
	{
		this.rotationalBatteries.addAll(volatileBatteries);
	}

	public void addListOfHeindlBatteries(ArrayList<ConstantFlowBattery> constantFlowBatteries)
	{
		this.heindlBatteries.addAll(constantFlowBatteries);
	}

	public void removeRotationalBattery(int index)
	{
		this.rotationalBatteries.remove(index);
	}

	public void removeHeindlBattery(int index)
	{
		this.heindlBatteries.remove(index);
	}

	public void removeAllBatteries()
	{
		this.gravitationalBatteries.clear();
		this.rotationalBatteries.clear();
		this.heindlBatteries.clear();
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

		// Calculate energy of the rotational batteries
		for (Battery battery : this.heindlBatteries)
		{
			currentEnergyInJoules += battery.getCurrentEnergyInJoules();
		}

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

		// Calculate energy of the constant flow batteries
		for (Battery battery : this.heindlBatteries)
		{
			maxEnergyInJoules += battery.getMaxEnergyInJoules();
		}

		return maxEnergyInJoules;
	}
	//endregion

	public void displayGrid()
	{
		for (Battery battery : this.heindlBatteries)
		{
			System.out.println(battery.displayBattery());
		}

		for (Battery rotationalBattery : this.rotationalBatteries)
		{
			System.out.println(rotationalBattery.displayBattery());
		}
	}

	public double[] CalculateMaximumEnergyReleaseByHourForConstantFlowBatteries(City city)
	{
		double[] minimumEnergyDemandByHour = city.getEnergyMinimumsByHour();
		double[] energyCreatedByHour = new double[]{
				1700000000,
				1700000000,
				1700000000,
				1700000000,
				1700000000,
				1700000000,
				2607200000.0,
				2607200000.0,
				3007600000.0,
				6636400000.0,
				11115200000.0,
				11115200000.0,
				15365200000.0,
				15136400000.0,
				14907600000.0,
				14907600000.0,
				14507200000.0,
				10257200000.0,
				5950000000.0,
				2550000000.0,
				2550000000.0,
				1700000000,
				1700000000,
				1700000000,

		};
		double[] energyDifferentialByHour = new double[minimumEnergyDemandByHour.length];
		for (int i = 0; i < minimumEnergyDemandByHour.length; i++){
			energyDifferentialByHour[i] = (minimumEnergyDemandByHour[i] - energyCreatedByHour[i])*.1;
		}

		return energyDifferentialByHour;
	};

	//region Getters/Setters
	public ArrayList<VolatileBattery> getGravitationalBatteries()
	{
		return this.gravitationalBatteries;
	}

	public ArrayList<VolatileBattery> getRotationalBatteries()
	{
		return this.rotationalBatteries;
	}

	public ArrayList<ConstantFlowBattery> getHeindlBatteries()
	{
		return this.heindlBatteries;
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
