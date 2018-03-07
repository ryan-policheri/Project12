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

	private static BatteryGrid batteryGrid = new BatteryGrid();
	private static EnergyCommander energyCommander = new EnergyCommander(batteryGrid);

	//region Available Cities
	private static City desMoines = new City("Des Moines", defaultTimeFrameAsPercentageOfHour);
	private static City chicago = new City("Chicago", defaultTimeFrameAsPercentageOfHour);

	private static ArrayList<City> availableCities = new ArrayList<City>();

	private static City selectedCity;
	//endregion

	//region Energy Sources
	private static ArrayList<WindmillFarm> windFarms = new ArrayList<WindmillFarm>();
	private static ArrayList<PhotovoltaicSolarFarm> PVSolarFarms = new ArrayList<PhotovoltaicSolarFarm>();
	//endregion

	// Times
	private static long currentMillisecond = 0;

	// Connect it to the correct form
	private static GRESBIMB gresbimb;

	public Controller(GRESBIMB gresbimb)
	{
		this.gresbimb = gresbimb;
	}

	//region building default cities
	public static void buildDefaultCities()
	{
		availableCities.clear();
		availableCities.add(chicago);
		availableCities.add(desMoines);
	}
	//endregion

	//region creating wind farms functions
	public static void buildDefaultWindFarms()
	{
		ArrayList<WindmillFarm> defaultWindFarms = BuildDefaults.createListOfDefaultWindFarms(desMoines,defaultTimeFrameAsPercentageOfHour);
		windFarms.clear();
		windFarms.addAll(defaultWindFarms);
	}

	public static void addWindFarm(String name, int[] windTiers)
	{
		WindmillFarm windFarm = new WindmillFarm(name, windTiers, defaultTimeFrameAsPercentageOfHour);
		windFarms.add(windFarm);
	}

	public static void addWindTurbineTypesToWindFarm(int indexOfFarm, String modelName, double maxCapacityInMegawatts, int numberOfTurbines)
	{
		double maxCapacityInWatts = maxCapacityInMegawatts * 1000000; //convert to megawatts

		for(int i = 0; i < numberOfTurbines; i++)
		{

			Windmill windmill = new Windmill(modelName, maxCapacityInWatts);
			windFarms.get(indexOfFarm).addWindmill(windmill);
		}
	}

	public static void removeAllWindFarms()
	{
		windFarms.clear();
	}
	//endregion

	//region creating pv solar farms functions
	public static void buildDefaultPVSolarFarms()
	{
		ArrayList<PhotovoltaicSolarFarm> defaultPVSolarFarms = BuildDefaults.createListOfDefaultPVSolarFarms(desMoines, defaultTimeFrameAsPercentageOfHour);
		PVSolarFarms.clear();
		PVSolarFarms.addAll(defaultPVSolarFarms);
	}

	public static void addPVSolarFarm(String name, double areaInSquareMeters, double conversionEfficencyAsPercent, int currenMonthAsInt)
	{
		PhotovoltaicSolarFarm PVSolarFarm = new PhotovoltaicSolarFarm(name, areaInSquareMeters, conversionEfficencyAsPercent, currenMonthAsInt, defaultTimeFrameAsPercentageOfHour);
		PVSolarFarms.add(PVSolarFarm);
	}

	public static void removeAllPVSolarFarms()
	{
		PVSolarFarms.clear();
	}
	//endregion

	//region creating batteries functions
	public static void buildDefaultVolatileBatteries()
	{
		ArrayList<VolatileBattery> defaultVolatileBatteries = BuildDefaults.createListOfDefaultVolatileBatteries();
		batteryGrid.removeAllBatteries();

		batteryGrid.addListOfRotationalBatteries(defaultVolatileBatteries);
	}

	public static void addGravitationalBattery(GravitationalBattery battery)
	{
		batteryGrid.addGravitationalBattery(battery);
	}

	public static void addRotationalBattery(RotationalBattery battery)
	{
		batteryGrid.addRotationalBattery(battery);
	}

	public static void removeBattery(int index)
	{
		batteryGrid.removeRotationalBattery(index);
	}

	public static void removeAllBatteries()
	{
		batteryGrid.removeAllBatteries();
	}
	//endregion


	public static void simulate()
	{

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

	public static double calculateCurrentGridEnergyInJoules()
	{
		return batteryGrid.calculateCurrentVolatileEnergyInJoules();
	}

	public static double calculateMaxTotalEnergyInJoules()
	{
		return batteryGrid.calculateMaxVolatileEnergyInJoules();
	}

	private static void addEnergySurplus()
	{
		/*System.out.println("Enter the amount of energy to add in watts: ");
		double incomingEnergyInWatts = scanner.nextDouble();

		System.out.println("Enter the amount of time the incoming energy lasts in seconds: ");
		double timeIncomingEnergyLastsInSeconds = scanner.nextDouble();

		Surplus surplus = new Surplus(incomingEnergyInWatts, timeIncomingEnergyLastsInSeconds);

		batteryGrid.allocateEnergySurplus(surplus);*/
	}

	private static void demandEnergy()
	{
		/*System.out.println("Enter the amount of energy to demand in watts: ");
		double energyDemandInWatts = scanner.nextDouble();

		System.out.println("Enter how long the demand is needed in seconds: ");
		double timeDemandIsNeededInSeconds = scanner.nextDouble();

		Demand demand = new Demand(energyDemandInWatts, timeDemandIsNeededInSeconds);

		batteryGrid.allocateEnergyDemand(demand);*/
	}

	//region Getters/Setters
	public static ArrayList<City> getAvailableCities()
	{
		return availableCities;
	}

	public static City getSelectedCity()
	{
		return selectedCity;
	}

	public static void setSelectedCity(int index)
	{
		selectedCity = availableCities.get(index);
	}

	public static ArrayList<WindmillFarm> getWindFarms()
	{
		return windFarms;
	}

	public static ArrayList<PhotovoltaicSolarFarm> getPVSolarFarms()
	{
		return PVSolarFarms;
	}

	public static BatteryGrid getBatteryGrid()
	{
		return batteryGrid;
	}

	public static int getNumOfTiers()
	{
		return NUM_OF_TIERS;
	}

	public static int getMajorTickSpacing()
	{
		return MAJOR_TICK_SPACING;
	}
	//endregion

}
