package edu.controllers;

import edu.model.EnergyCommander;
import edu.model.batteries.*;
import edu.model.city.City;
import edu.model.city.CitySimulator;
import edu.model.energySources.windmillFarm.WindmillFarm;
import edu.model.energySources.windmillFarm.WindmillFarmSimulator;
import edu.view.MainUserGUI;

import java.util.ArrayList;

public class Controller
{
	//TODO: Implement the CitySimulator for a full-fledged simulation
	//TODO: Add energy production and energy consumption arrays for graph functionality
	private static BatteryGrid grid = new BatteryGrid();

	private static final int NUM_OF_TIERS = 5;
	private static final int MAJOR_TICK_SPACING = 1;

	//region Cities
	//region Des Moines
	private static int[] energyConsumptionTiersDesMoines = {
			1, 1, 1, 1, 1, 2, 2, 2, 2, 4, 5, 4, 2, 5, 5, 3, 3, 1, 1, 3, 4, 3, 3, 1};
	private static int[] energyProductionTiersWarrenCountyWMF = {
			1, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 5, 3, 3, 3, 4, 2, 2, 2, 1, 1};
	private static City desMoines = new City("Des Moines", energyConsumptionTiersDesMoines);
	//endregion

	//region Chicago
	private static int[] energyConsumptionTiersChicago = {
			1, 1, 1, 1, 1, 2, 2, 2, 2, 4, 5, 4, 2, 5, 5, 3, 3, 1, 1, 3, 4, 3, 3, 1};
	private static int[] energyProductionTiersChicago = {
			1, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 5, 3, 3, 3, 4, 2, 2, 2, 1, 1};
	private static City chicago = new City("Chicago", energyConsumptionTiersChicago);
	//endregion

	private static ArrayList<City> availableCities = new ArrayList<City>();
	private static City selectedCity = desMoines;
	//endregion

	// Set default city
	private static CitySimulator citySimulator = new CitySimulator(desMoines);
	private static EnergyCommander energyCommander = new EnergyCommander(grid);

	// Windmill farm
	private static WindmillFarm warrenCountyWindmillFarm = new WindmillFarm("Warren County Windmill Farm",
			energyProductionTiersWarrenCountyWMF);
	private static WindmillFarmSimulator windmillFarmSimulator = new WindmillFarmSimulator(warrenCountyWindmillFarm);
	private static ArrayList<Double> magnitudeOfDemandsByMillisecond;
	private static ArrayList<Double> magnitudeOfSurplusesByMillisecond;
	private static WindmillFarm selectedWMF = warrenCountyWindmillFarm;

	// Times
	private static long currentMillisecond = 0;

	// Connect it to the correct form
	private static MainUserGUI mainUserGUI;

	public Controller(MainUserGUI mainUserGUI)
	{
		this.mainUserGUI = mainUserGUI;
	}

	public static void updateCities()
	{
		availableCities.clear();
		availableCities.add(chicago);
		availableCities.add(desMoines);

		citySimulator = new CitySimulator(selectedCity);
		energyCommander = new EnergyCommander(grid);
		//TODO: Run this when the "simulate" button is hit
		// magnitudeByMillisecondArray = citySimulator.constructMagnitudeByMillisecondArray();
	}

	public static void updateMagnitudeByMillisecondArrays()
	{
		magnitudeOfDemandsByMillisecond = citySimulator.constructMagnitudeByMillisecondArray();
		magnitudeOfSurplusesByMillisecond = windmillFarmSimulator.constructMagnitudeByMillisecondArray();
	}

	public static void updateTimeInformation()
	{
		currentMillisecond = windmillFarmSimulator.getCurrentMillisecond();
		mainUserGUI.updateSimulationChartWithCurrentMillisecond(currentMillisecond);
	}

	public static void addGravitationalBattery(GravitationalBattery battery)
	{
		grid.addGravitationalBattery(battery);

		// updateCities the view
		MainUserGUI.update();
	}

	public static void addRotationalBattery(RotationalBattery battery)
	{
		grid.addRotationalBattery(battery);

		// updateCities the view
		MainUserGUI.update();
	}

	public static void removeBattery(int index)
	{
		grid.removeBattery(index);

		// updateCities the view
		MainUserGUI.update();
	}

	public static void removeAllBatteries()
	{
		grid.removeAllBatteries();
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
	
	//region Getters/Setters
	public static ArrayList<City> getAvailableCities()
	{
		return availableCities;
	}

	public static WindmillFarm getSelectedWMF()
	{
		return selectedWMF;
	}

	public static void setAvailableCities(ArrayList<City> availableCities)
	{
		Controller.availableCities = availableCities;
	}

	public static BatteryGrid getGrid()
	{
		return grid;
	}

	public static int getNumOfTiers()
	{
		return NUM_OF_TIERS;
	}

	public static int getMajorTickSpacing()
	{
		return MAJOR_TICK_SPACING;
	}

	public static City getSelectedCity()
	{
		return selectedCity;
	}

	public static void setSelectedCity(City newCity)
	{
		selectedCity = newCity;
	}

	public static WindmillFarmSimulator getWindmillFarmSimulator()
	{
		return windmillFarmSimulator;
	}

	public static City getDesMoines()
	{
		return desMoines;
	}

	public static ArrayList<Double> getMagnitudeOfDemandsByMillisecond()
	{
		return magnitudeOfDemandsByMillisecond;
	}

	public static ArrayList<Double> getMagnitudeOfSurplusesByMillisecond()
	{
		return magnitudeOfSurplusesByMillisecond;
	}

	//endregion
}
