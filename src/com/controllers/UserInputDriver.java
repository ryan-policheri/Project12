package com.controllers;

import com.model.batteries.Demand;
import com.model.batteries.Surplus;
import com.model.batteries.BatteryGrid;

import java.util.Scanner;

public class UserInputDriver 
{
	private static BatteryGrid grid = new BatteryGrid();
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) 
	{
		// TO DO
		showStartMessage();
		displayInputs();
		
		// used for gathering user input
		String userInput = "";
		
		// loops through the program until the user is done
		while (!userInput.equals("-done"))
		{
			System.out.println("------------------------------------------------------");
			userInput = askForInput();
			System.out.println();
		}
	} 
	
	private static String askForInput()
	{
		System.out.println("What would you like to do: ");
		String userInput = scanner.nextLine();
		System.out.println("------------------------------------------------------");
		
		switch (userInput) 
		{
			case "-help":
				displayInputs();
				break;
			case "-addBattery":
				addBattery();
				break;
			case "-removeBattery":
				removeBattery();
				break;
			case "-addEnergy":
				addEnergySurplus();
				break;
			case "-demandEnergy":
				demandEnergy();
				break;
			case "-displayBatteryStats":
				displayBatteryStats();
				break;
			default: 
				System.out.println("Command not recognized.");
				break;
		}
		
		return userInput;
	}
	
	private static void showStartMessage()
	{
		System.out.println("This is the user interface for Project 12.");
		System.out.println("Provide commands in the following formats:");
	}
	
	private static void displayInputs()
	{
		System.out.println("Add a gravitational battery to the battery grid: -addBattery");
		System.out.println("Remove a battery from the battery grid: -removeBattery");
		System.out.println("Add energy to the battery grid: -addEnergy");
		System.out.println("com.model.batteries.Demand energy from the battery grid: -demandEnergy");
		System.out.println("Display current battery stats: -displayBatteryStats");
		System.out.println("Show this message again: -help");
		System.out.println("Terminate the program: -done");
	}
	
	private static void addBattery()
	{
		/* System.out.println("Enter the battery's name: ");
		String batteryName = scanner.nextLine();
				
		System.out.println("Enter the battery's mass in kilograms: ");
		double batteryMassInKilograms = scanner.nextDouble();*/
		
		System.out.println("Enter the battery's maximum height in meters: ");
		double batteryHeightInMeters = scanner.nextDouble();
		
		// com.model.batteries.GravitationalBattery battery = new com.model.batteries.GravitationalBattery("Name", 10, batteryHeightInMeters);
		// grid.addGravitationalBattery(battery);
		
		System.out.println("Name" + " added to the battery grid.");
	}
	
	private static void removeBattery()
	{
		
	}
	
	private static void addEnergySurplus()
	{
		System.out.println("Enter the amount of energy to add in watts: ");
		double incomingEnergyInWatts = scanner.nextDouble();
		
		System.out.println("Enter the amount of time the incoming energy lasts in seconds: ");
		double timeIncomingEnergyLastsInSeconds = scanner.nextDouble();
		
		Surplus surplus = new Surplus(incomingEnergyInWatts, timeIncomingEnergyLastsInSeconds);
		
		grid.allocateEnergySurplus(surplus);
	}

	private static void demandEnergy()
	{
		System.out.println("Enter the amount of energy to demand in watts: ");
		double energyDemandInWatts = scanner.nextDouble();
		
		System.out.println("Enter how long the demand is needed in seconds: ");
		double timeDemandIsNeededInSeconds = scanner.nextDouble();
		
		com.model.batteries.Demand demand = new com.model.batteries.Demand(energyDemandInWatts, timeDemandIsNeededInSeconds);
		
		grid.allocateEnergyDemand(demand);
	}

	private static void displayBatteryStats()
	{
		grid.displayGrid();
	}
}
