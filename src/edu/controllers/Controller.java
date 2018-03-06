package edu.controllers;

import edu.model.EnergyCommander;
import edu.model.batteries.*;
import edu.model.city.City;
import edu.model.energySources.solarFarm.PhotovoltaicSolarFarm;
import edu.model.energySources.windmillFarm.Windmill;
import edu.model.energySources.windmillFarm.WindmillFarm;
import edu.view.GRESBIMB;

import java.util.ArrayList;

public class Controller
{
	private static final int NUM_OF_TIERS = 5;
	private static final int MAJOR_TICK_SPACING = 1;

	private static final double defaultTimeFrameAsPercentageOfHour = 0.1;

	private static BatteryGrid grid = new BatteryGrid();

	//region Available Cities
	private static City desMoines = new City("Des Moines", defaultTimeFrameAsPercentageOfHour);
	private static City chicago = new City("Chicago", defaultTimeFrameAsPercentageOfHour);

	private static ArrayList<City> availableCities = new ArrayList<City>();

	private static City selectedCity;
	//endregion

	//region Energy Sources
	private static ArrayList<WindmillFarm> windFarms = BuildDefaults.createListOfDefaultWindFarms(desMoines,defaultTimeFrameAsPercentageOfHour);
	private static ArrayList<PhotovoltaicSolarFarm> PVSolarFarms = BuildDefaults.createListOfDefaultPVSolarFarms(desMoines, defaultTimeFrameAsPercentageOfHour);
	//endregion

	// Set default city
	private static EnergyCommander energyCommander = new EnergyCommander(grid);

	// Times
	private static long currentMillisecond = 0;

	// Connect it to the correct form
	private static GRESBIMB gresbimb;

	public Controller(GRESBIMB gresbimb)
	{
		this.gresbimb = gresbimb;
	}

	public static void buildDefaultWindFarms()
	{
		ArrayList<WindmillFarm> defaultWindFarms = BuildDefaults.createListOfDefaultWindFarms(desMoines,defaultTimeFrameAsPercentageOfHour);
		windFarms.addAll(defaultWindFarms);
	}

	public static void buildDefaultPVSolarFarms()
	{
		ArrayList<PhotovoltaicSolarFarm> defaultPVSolarFarms = BuildDefaults.createListOfDefaultPVSolarFarms(desMoines, defaultTimeFrameAsPercentageOfHour);
		PVSolarFarms.addAll(defaultPVSolarFarms);
	}

	public static ArrayList<WindmillFarm> getWindFarms()
	{
		return windFarms;
	}
	public static ArrayList<PhotovoltaicSolarFarm> getPVSolarFarms()
	{
		return PVSolarFarms;
	}

	public static void buildDefaultCities()
	{
		availableCities.clear();
		availableCities.add(chicago);
		availableCities.add(desMoines);
	}

/*	public static void updateMagnitudeByMillisecondArrays()
	{
		magnitudeOfDemandsByMillisecond = citySimulator.constructMagnitudeByMillisecondArray();
		magnitudeOfSurplusesByMillisecond = windmillFarmSimulator.constructMagnitudeByMillisecondArray();
	}*/

	public static void updateTimeInformation()
	{
		//currentMillisecond = windmillFarmSimulator.getCurrentMillisecond();
		gresbimb.updateSimulationDemandChartWithCurrentMillisecond(currentMillisecond);
		gresbimb.updateSimulationSurplusChartWithCurrentMillisecond(currentMillisecond);
		gresbimb.calculateCurrentGridEnergyInJoules();
	}

	public static void addGravitationalBattery(GravitationalBattery battery)
	{
		grid.addGravitationalBattery(battery);

		// updateCities the view
		GRESBIMB.update();
	}

	public static void addRotationalBattery(RotationalBattery battery)
	{
		grid.addRotationalBattery(battery);

		// updateCities the view
		GRESBIMB.update();
	}

	public static void removeBattery(int index)
	{
		grid.removeBattery(index);

		// updateCities the view
		GRESBIMB.update();
	}

	public static void removeAllBatteries()
	{
		grid.removeAllBatteries();
	}

	public static double calculateCurrentGridEnergyInJoules()
	{
		return grid.calculateCurrentVolatileEnergyInJoules();
	}

	public static double calculateMaxTotalEnergyInJoules()
	{
		return grid.calculateMaxVolatileEnergyInJoules();
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

/*	public static WindmillFarm getSelectedWMF()
	{
		return selectedWMF;
	}*/

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

	public static void setSelectedCity(int index)
	{
		selectedCity = availableCities.get(index);
	}

	public static void setSelectedCityConsumptionValues(int[] sliderValues)
	{
		// Sets the city consumption values and updates the main page
		gresbimb.updateEnergyScreen();
	}

/*	public static WindmillFarm getWindmillFarm()
	{
		return selectedWMF;
	}*/
	//endregion
}
