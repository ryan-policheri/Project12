package edu.controllers;

import edu.model.EnergyCommander;
import edu.model.batteries.*;
import edu.model.city.City;
import edu.model.energySources.solarFarm.PhotovoltaicSolarFarm;
import edu.model.energySources.windmillFarm.Windmill;
import edu.model.energySources.windmillFarm.WindmillFarm;
import edu.view.GRESBIMB;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Controller
{
	private static final int NUM_OF_TIERS = 5;
	private static final int MAJOR_TICK_SPACING = 1;

	private static long terminateCounter = 0;

	private static final double defaultTimeFrameAsPercentageOfHour = 0.1;

	//region Battery Grid
	private static BatteryGrid batteryGrid = new BatteryGrid();
	private static EnergyCommander energyCommander = new EnergyCommander(batteryGrid);
	//endregion

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

	// Connect it to the main form form
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

	//region simuation
	public static void preFlightSetup()
	{
		ArrayList<ConstantFlowBattery> constantFlowBatteries = BuildDefaults.createListOfDefaultConstantFlowBatteries();
		batteryGrid.addListOfHeindlBatteries(constantFlowBatteries);

		gresbimb.setMaxVolatileEnergyInJoules(batteryGrid.calculateMaxVolatileEnergyInJoules());
		gresbimb.setMaxCFEnergyInJoules(batteryGrid.calculateMaxConstantFlowEnergyInJoules());

		energyCommander.commandEnergy(7.5 * Math.pow(10,11) / 10, 0);
		energyCommander.commandEnergy(7.5 * Math.pow(10,11) / 10, 0);
		energyCommander.commandEnergy(7.5 * Math.pow(10,11) / 10, 0);
		energyCommander.commandEnergy(7.5 * Math.pow(10,11) / 10, 0);
		energyCommander.commandEnergy(7.5 * Math.pow(10,11) / 10, 0);
		energyCommander.commandEnergy(7.5 * Math.pow(10,11) / 10, 0);
		energyCommander.commandEnergy(7.5 * Math.pow(10,11) / 10, 0);
		energyCommander.commandEnergy(7.5 * Math.pow(10,11) / 10, 0);
		energyCommander.commandEnergy(7.5 * Math.pow(10,11) / 10, 0);
		energyCommander.commandEnergy(7.5 * Math.pow(10,11) / 10, 0);


		updateGresbimbSimulationScreen(0,0,0,0, 0);
	}

	public static void updateGresbimbSimulationScreen(double energyProduced, double windEnergyProduced, double PVSolarEnergyProduced, double energyDemanded, long graphIndex)
	{
		double currentVolatileEnergyInJoules = batteryGrid.calculateCurrentVolatileEnergyInJoules();
		double currentCFEnergyInJoules = batteryGrid.calculateCurrentConstantFlowEnergyInJoules();

		int numberOfShortages = batteryGrid.getAmountOfEnergyShortages();
		double amountShortInJoules = batteryGrid.getEnergyShortageInWatts();

		gresbimb.updateSimulationScreen(currentVolatileEnergyInJoules, currentCFEnergyInJoules, energyProduced, windEnergyProduced, PVSolarEnergyProduced, energyDemanded, graphIndex, numberOfShortages, amountShortInJoules);
	}

	public static void launchSimulation()
	{
		long intervalInMilliseconds = 1000;
		long amountOfTimesRan = 239;

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
								  {
									  @Override
									  public void run()
									  {
										  terminateCounter += 1;

										  double cityDemand = desMoines.nextDemand();

										  double windEnergy = 0;

										  for (WindmillFarm windFarm : windFarms)
										  {
											  double windFarmSurplus = windFarm.nextSurplus();
											  windEnergy += windFarmSurplus;
										  }

										  double pvSolarEnergy = 0;

										  for (PhotovoltaicSolarFarm PVSolarFarm : PVSolarFarms)
										  {
											  double solarFarmSurplus = PVSolarFarm.nextSurplus();
											  pvSolarEnergy += solarFarmSurplus;
										  }

										  double energyProduced = windEnergy + pvSolarEnergy;

										  EnergyCommander.commandEnergy(energyProduced, cityDemand);
										  updateGresbimbSimulationScreen(energyProduced, windEnergy, pvSolarEnergy, cityDemand, terminateCounter);

										  if (terminateCounter == amountOfTimesRan)
										  {
											  timer.cancel();
										  }
									  }
								  }
				, 0, intervalInMilliseconds);
	}
	//endregion

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

	public static double[] getEnergyMinimumsByHour() { return selectedCity.getEnergyMinimumsByHour(); }

	//endregion

}
