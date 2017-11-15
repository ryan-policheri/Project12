package edu.controllers;

import edu.model.batteries.BatteryGrid;
import edu.model.batteries.Demand;
import edu.model.batteries.GravitationalBattery;
import edu.model.batteries.Surplus;
import edu.view.MainUserGUI;

import java.util.Scanner;

public class Controller
{
	private static BatteryGrid grid = new BatteryGrid();

	public static void addBattery(String batteryName, double massInKilograms, double maxHeightInMeters)
	{
		GravitationalBattery battery = new GravitationalBattery(batteryName, massInKilograms, maxHeightInMeters);
		grid.addGravitationalBattery(battery);

		// update the view
		MainUserGUI.update();
	}

	private static void removeBattery()
	{

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
}
