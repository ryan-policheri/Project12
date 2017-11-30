package edu.controllers;

import edu.model.batteries.*;
import edu.view.MainUserGUI;

import java.util.Scanner;

public class Controller
{
	//TODO: Implement the CitySimulator for a full-fledged simulation
	//TODO: Add energy production and energy consumption arrays for graph functionality
	private static BatteryGrid grid = new BatteryGrid();

	private static final int NUM_OF_TIERS = 5;
	private static final int MAJOR_TICK_SPACING = 1;

	public static void addGravitationalBattery(GravitationalBattery battery)
	{
		grid.addGravitationalBattery(battery);

		// update the view
		MainUserGUI.update();
	}

	public static void addRotationalBattery(RotationalBattery battery)
	{
		grid.addRotationalBattery(battery);

		// update the view
		MainUserGUI.update();
	}

	public static void removeBattery(int index)
	{
		grid.removeBattery(index);

		// update the view
		MainUserGUI.update();
	}

	private static void addEnergySurplus()
	{
		/*System.out.println("Enter the amount of energy to add in watts: ");
		double incomingEnergyInWatts = scanner.nextDouble();

		System.out.println("Enter the amount of time the incoming energy lasts in seconds: ");
		double timeIncomingEnergyLastsInSeconds = scanner.nextDouble();

		Surplus surplus = new Surplus(incomingEnergyInWatts, timeIncomingEnergyLastsInSeconds);

		grid.allocateEnergySurplus(surplus);*/
	}

	private static void demandEnergy()
	{
		/*System.out.println("Enter the amount of energy to demand in watts: ");
		double energyDemandInWatts = scanner.nextDouble();

		System.out.println("Enter how long the demand is needed in seconds: ");
		double timeDemandIsNeededInSeconds = scanner.nextDouble();

		Demand demand = new Demand(energyDemandInWatts, timeDemandIsNeededInSeconds);

		grid.allocateEnergyDemand(demand);*/
	}

	private static void displayBatteryStats()
	{
		grid.displayGrid();
	}

	public static BatteryGrid getGrid()
	{
		return grid;
	}

	public static int getnumOfTiers()
	{
		return NUM_OF_TIERS;
	}

	public static int getMajorTickSpacing()
	{
		return MAJOR_TICK_SPACING;
	}
}
