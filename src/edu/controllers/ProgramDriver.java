package edu.controllers;

import edu.model.EnergyCommander;
import edu.model.GraphDataPoint;
import edu.model.batteries.BatteryGrid;
import edu.model.batteries.Demand;
import edu.model.batteries.RotationalBattery;
import edu.model.batteries.Surplus;
import edu.model.city.City;
import edu.model.city.CitySimulator;
import edu.model.energySources.windmillFarm.WindmillFarmSimulator;
import edu.model.batteries.FlywheelBearing;
import edu.model.batteries.FlywheelMaterial;
import edu.model.batteries.GravitationalBattery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class ProgramDriver
{

	public static void main(String[] args) throws InterruptedException
	{
		
/*		GravitationalBattery testBattery = new GravitationalBattery("GB_1",10000,40);
		System.out.println(testBattery.currentHeightInMeters);
		System.out.println(testBattery.currentPotentialEnergyInJoules);
		
		System.out.println(testBattery.storeEnergy(1000,4362));
		System.out.println(testBattery.currentHeightInMeters);
		System.out.println(testBattery.currentPotentialEnergyInJoules);
		
		System.out.println(testBattery.isBatteryFull());
		
		System.out.println(testBattery.releaseEnergy(1000, 2000));
		System.out.println(testBattery.currentHeightInMeters);
		System.out.println(testBattery.currentPotentialEnergyInJoules);
		
		testBattery.displayBattery();
 		*/
		
/*		BatteryGrid batteryGrid = new BatteryGrid();
		
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_1",10000,40));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_2",5000,50));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_3",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_4",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_5",2500,20));
		
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergySurplus(5000, 2000);
		batteryGrid.displayGrid();
		
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergySurplus(1500, 360);
		batteryGrid.displayGrid();
		
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergyDemand(2250, 1000);
		batteryGrid.displayGrid();
		
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergySurplus(900, 7500);
		batteryGrid.displayGrid();
		
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergyDemand(2250, 1000);
		batteryGrid.displayGrid();
		*/
		
/*		City testCity = new City("Des Moines");
		testCity.powerNeed(1500000, 3600);
		testCity.cityPopulation = 215472;
		testCity.statePopulation = 3134693;
		testCity.estimatedLandAvailableInAcres = 2000;
		
		// in MwH
		testCity.cityEnergyProducedYearly = 15503646.4985;
		testCity.cityRenewableEnergyProducedYearly = 3970593.45744;
		testCity.cityEnergyConsumedYearly = 30136087.4678;
		
		System.out.println(testCity.powerNeed(1500000, 3600));
		
		BatteryGrid batteryGrid = new BatteryGrid();
		
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_1",10000,40));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_2",5000,50));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_3",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_4",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_5",2500,20));
		
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergySurplus(100,10);
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergySurplus(100,10);
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergyDemand(100,5);
		batteryGrid.displayGrid();
		*/
		
/*		WindmillFarmPerformanceType performance = new WindmillFarmPerformanceType();
		performance.setToLowPerformance();
		
		WindmillFarm farm = new WindmillFarm();
		System.out.println(farm.calculateWindmillFarmOutput(performance));
		
		WindmillFarmSimulator simulator = new WindmillFarmSimulator();
		simulator.simulate()
		*/
		
/*		BatteryGrid batteryGrid = new BatteryGrid();
		
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_1",10000,40));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_2",5000,50));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_3",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_4",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_5",2500,20));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_6",10000,40));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_7",5000,50));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_8",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_9",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_10",2500,20));*/
		
		//batteryGrid.displayGrid();
		//batteryGrid.allocateEnergySurplus(new Surplus(100,20));
		//batteryGrid.displayGrid();
		//TimeUnit.SECONDS.sleep(5);
		//batteryGrid.allocateEnergySurplus(new Surplus(100,10));
		//batteryGrid.displayGrid();
		//TimeUnit.SECONDS.sleep(10);
		//batteryGrid.allocateEnergyDemand(new Demand(100,5));
/*		
		batteryGrid.displayGrid();
		
		WindmillFarmSimulator windmillFarmSimulator = new WindmillFarmSimulator();
		
		EnergyCommander energyCommander = new EnergyCommander(batteryGrid);
		
		windmillFarmSimulator.simulate();*/
		
/*		batteryGrid.allocateEnergySurplus(new Surplus(239024.48901238025,10));
		batteryGrid.displayGrid();
		TimeUnit.SECONDS.sleep(11);
		batteryGrid.allocateEnergySurplus(new Surplus(182135.43543721945,10));
		batteryGrid.displayGrid();
		TimeUnit.SECONDS.sleep(11);
		batteryGrid.allocateEnergySurplus(new Surplus(380508.38637304987,10));
		batteryGrid.displayGrid();
		TimeUnit.SECONDS.sleep(11);
		batteryGrid.allocateEnergySurplus(new Surplus(274450.70316027646,10));
		batteryGrid.displayGrid();
		TimeUnit.SECONDS.sleep(11);*/
		
/*		double densityOfCarbonFiberInKilogramsMetersCubed = 1799;
		double tensileStressOfCarbonFiberInPascals = 4000000000.0;
		FlywheelMaterial carbonFiber = new FlywheelMaterial("Carbon Fiber", densityOfCarbonFiberInKilogramsMetersCubed, tensileStressOfCarbonFiberInPascals);
		
		double percentFrictionalLossPerSecondForStandardMecahnicalBearing = 0.000034722222222222222222; //number derived from 25 percent / 7200
		FlywheelBearing mechanicalBearing = new FlywheelBearing("Mechanical Bearing", percentFrictionalLossPerSecondForStandardMecahnicalBearing);
		
		RotationalBattery rotBat = new RotationalBattery("RB_1", 450, 10.72, carbonFiber, mechanicalBearing);
		rotBat.displayBattery();
		rotBat.storeEnergy(new Surplus(50013574,10));
		rotBat.displayBattery();
		TimeUnit.SECONDS.sleep(11);
		//rotBat.releaseEnergy(new Demand(37445006,10));
		rotBat.displayBattery();
		TimeUnit.SECONDS.sleep(11);
		//rotBat.releaseEnergy(new Demand(29744500,10));
		rotBat.displayBattery();*/
		
/*		double densityOfCarbonFiberInKilogramsMetersCubed = 1799;
		double densityOfSteelInKilogramsMetersCubed = 8050;
		double tensileStressOfCarbonFiberInPascals = 4000000000.0;
		double tensileStressOfSteelInPascals = 690000000.0;
		FlywheelMaterial carbonFiber = new FlywheelMaterial("Carbon Fiber", densityOfCarbonFiberInKilogramsMetersCubed, tensileStressOfCarbonFiberInPascals);
		FlywheelMaterial steel = new FlywheelMaterial("Steel", densityOfSteelInKilogramsMetersCubed, tensileStressOfSteelInPascals);
		
		double percentFrictionalLossPerSecondForStandardMecahnicalBearing = 0.0125; //number derived from 25 percent / 7200
		FlywheelBearing mechanicalBearing = new FlywheelBearing("Mechanical Bearing", percentFrictionalLossPerSecondForStandardMecahnicalBearing);
		
		BatteryGrid batteryGrid = new BatteryGrid();*/
		
/*		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_1",1000,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_2",5000,10));*/
		//batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_3",20000,120));
		//batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_4",2000,20));
		//batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_5",2500,20));
		//batteryGrid.addRotationalBattery(new RotationalBattery("RB_1", 400, 0.5, steel, mechanicalBearing));
/*		batteryGrid.addRotationalBattery(new RotationalBattery("RB_2", 150, 7.5, carbonFiber, mechanicalBearing));
    	batteryGrid.addRotationalBattery(new RotationalBattery("RB_3", 250, 6, carbonFiber, mechanicalBearing));
    	batteryGrid.addRotationalBattery(new RotationalBattery("RB_4", 150, 5, carbonFiber, mechanicalBearing));
		batteryGrid.addRotationalBattery(new RotationalBattery("RB_5", 200, 2.5, carbonFiber, mechanicalBearing));*/
		
		//batteryGrid.displayGrid();
		//batteryGrid.allocateEnergySurplus(new Surplus(100000000,5));
		//batteryGrid.displayGrid();
		//TimeUnit.SECONDS.sleep(20);
		//batteryGrid.displayGrid();
/*		batteryGrid.allocateEnergySurplus(new Surplus(50000,2));
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergySurplus(new Surplus(100000,5));
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergySurplus(new Surplus(20000,2));
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergySurplus(new Surplus(555680500,1));
		batteryGrid.displayGrid();
		TimeUnit.SECONDS.sleep(1);
		batteryGrid.allocateEnergyDemand(new Demand(55566050,1));
		batteryGrid.displayGrid();
		TimeUnit.SECONDS.sleep(10);
		batteryGrid.displayGrid();
		TimeUnit.SECONDS.sleep(10);
		batteryGrid.allocateEnergyDemand(new Demand(2000,3));
		batteryGrid.displayGrid();
		TimeUnit.SECONDS.sleep(5);
		batteryGrid.allocateEnergyDemand(new Demand(10000,10));
		batteryGrid.displayGrid();
		TimeUnit.SECONDS.sleep(5);
		batteryGrid.allocateEnergyDemand(new Demand(10000,10));
		batteryGrid.displayGrid();
		TimeUnit.SECONDS.sleep(10);
		batteryGrid.allocateEnergyDemand(new Demand(90000000,10));
		batteryGrid.displayGrid();*/
		
/*		double densityOfCarbonFiberInKilogramsMetersCubed = 1799;
		double tensileStressOfCarbonFiberInPascals = 4000000000.0;
		FlywheelMaterial carbonFiber = new FlywheelMaterial("Carbon Fiber", densityOfCarbonFiberInKilogramsMetersCubed, tensileStressOfCarbonFiberInPascals);
		
		double percentFrictionalLossPerSecondForStandardMecahnicalBearing = 0.000034722222222222222222; //number derived from 25 percent / 7200
		FlywheelBearing mechanicalBearing = new FlywheelBearing("Mechanical Bearing", percentFrictionalLossPerSecondForStandardMecahnicalBearing);
		
		BatteryGrid batteryGrid = new BatteryGrid();
		
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_1",1000,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_2",5000,10));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_3",500,50));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_4",2000,20));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_5",2500,20));
		batteryGrid.addRotationalBattery(new RotationalBattery("RB_1", 100, 5, carbonFiber, mechanicalBearing));
		batteryGrid.addRotationalBattery(new RotationalBattery("RB_2", 150, 7.5, carbonFiber, mechanicalBearing));
		batteryGrid.addRotationalBattery(new RotationalBattery("RB_3", 250, 2, carbonFiber, mechanicalBearing));
		batteryGrid.addRotationalBattery(new RotationalBattery("RB_4", 150, 5, carbonFiber, mechanicalBearing));
		batteryGrid.addRotationalBattery(new RotationalBattery("RB_5", 50, 5, carbonFiber, mechanicalBearing));
		
		batteryGrid.allocateEnergySurplus(new Surplus(100000,5));
		batteryGrid.allocateEnergySurplus(new Surplus(50000,2));
		batteryGrid.allocateEnergySurplus(new Surplus(100000,5));
		batteryGrid.allocateEnergySurplus(new Surplus(20000,2));
		batteryGrid.allocateEnergySurplus(new Surplus(20000,2));
		batteryGrid.allocateEnergySurplus(new Surplus(20000,2));
		TimeUnit.SECONDS.sleep(20);
		batteryGrid.displayGrid();;
		
		WindmillFarmSimulator windmillFarmSimulator = new WindmillFarmSimulator();
		CitySimulator citySimulator = new CitySimulator();
		
		EnergyCommander energyCommander = new EnergyCommander(batteryGrid);
		
		//windmillFarmSimulator.simulate();
		citySimulator.simulate();	*/
		
		//*********************************************************************
		//POLI'S BEAUTIFUL BATTERY GRID
		//*********************************************************************
		
		//MAKE MATERIALS:
		
		//Make using 1st constructor:
		
		//carbon fiber
		double densityOfCarbonFiberInKilogramsMetersCubed = 1799;
		double tensileStressOfCarbonFiberInPascals = 4000000000.0;
		FlywheelMaterial carbonFiber = new FlywheelMaterial("Carbon Fiber", densityOfCarbonFiberInKilogramsMetersCubed, tensileStressOfCarbonFiberInPascals);
		
		//steel
		double densityOfSteelInKilogramsMetersCubed = 8050;
		double tensileStressOfSteelInPascals = 690000000.0;
		FlywheelMaterial steel = new FlywheelMaterial("Steel", densityOfSteelInKilogramsMetersCubed, tensileStressOfSteelInPascals);
		
		//Make using 2nd constructor:
		
		//titanium
		FlywheelMaterial titanium = new FlywheelMaterial("Titanium");
		
		//aluminum
		FlywheelMaterial aluminum = new FlywheelMaterial("Aluminum");
		
		
		//MAKE BEARINGS:
		
		//Make using 1st constructor:
		
		//mechanical bearing
		double percentFrictionalLossPerSecondForStandardMecahnicalBearing = 0.0125; //number derived from (25 percent / 7200) * 360 (multiplying by 360 converts number to simulation time)
		FlywheelBearing mechanicalBearing = new FlywheelBearing("Mechanical Bearing", percentFrictionalLossPerSecondForStandardMecahnicalBearing);
		
		
		//magnetic bearing
		FlywheelBearing magneticBearing = new FlywheelBearing("Magnetic");
		
		//super conductor bearing
		FlywheelBearing modernBearing = new FlywheelBearing("Modern");
		
		
		//MAKE THE GRID
		BatteryGrid batteryGrid = new BatteryGrid();
		
		//ADD THE BATTERIES
		double GB_Supreme_Count = 50;
		double GB_PowerHouse_Count = 200;
		double GB_Classic_Count = 125;
		double GB_Lite_Count = 75;
		
		double RB_MegaSonic_Count = 45;
		double RB_SuperSonic_Count = 100;
		double RB_BigSexy_Count = 175;
		double RB_LittleTitan_Count = 100;
		double RB_Classic_Count = 125;
		
		//Gravitational:
		
		//GB_Supremes:
		for (int i = 1; i <= GB_Supreme_Count; i++)
		{
			batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_Supreme_" + Integer.toString(i),40000,150));
		}
		
		//GB_PowerHouses:
		for (int i = 1; i <= GB_PowerHouse_Count; i++)
		{
			batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_PowerHouse_" + Integer.toString(i),20000,120));
		}
		
		//GB_Classics:
		for (int i = 1; i <= GB_Classic_Count; i++)
		{
			batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_Classic_" + Integer.toString(i),30000,50));
		}
		
		//GB_Lites:
		for (int i = 1; i <= GB_Lite_Count; i++)
		{
			batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_Lite_" + Integer.toString(i),40000,150));
		}

		
		//Rotational:
		
		//RB_MegaSonics:
		for (int i = 1; i <= RB_MegaSonic_Count; i++)
		{
			batteryGrid.addRotationalBattery(new RotationalBattery("RB_MegaSonic_" + Integer.toString(i),100,0.5,carbonFiber,modernBearing));
		}
		
		//RB_SuperSonics:
		for (int i = 1; i <= RB_SuperSonic_Count; i++)
		{
			batteryGrid.addRotationalBattery(new RotationalBattery("RB_SuperSonic_" + Integer.toString(i),75,0.5,carbonFiber,magneticBearing));
		}
		
		//RB_BigSexys:
		for (int i = 1; i <= RB_BigSexy_Count; i++)
		{
			batteryGrid.addRotationalBattery(new RotationalBattery("RB_BigSexy_" + Integer.toString(i),500,1,steel,mechanicalBearing));
		}
		
		//RB_LittleTitans:
		for (int i = 1; i <= RB_LittleTitan_Count; i++)
		{
			batteryGrid.addRotationalBattery(new RotationalBattery("RB_LittleTitan_" + Integer.toString(i),250,0.25,titanium,magneticBearing));
		}
		
		//RB_Classics:
		for (int i = 1; i <= RB_Classic_Count; i++)
		{
			batteryGrid.addRotationalBattery(new RotationalBattery("RB_Classic_" + Integer.toString(i),100,0.5,aluminum,mechanicalBearing));
		}
		
		//batteryGrid.displayGrid();
		
		//FILL BATTERIES (for testing, waiting for windmill farm)
		for(int i = 0; i < 1; i++)
		{
			batteryGrid.allocateEnergySurplus(new Surplus(19200000,5));
		}
		
		TimeUnit.SECONDS.sleep(10);
		
		//batteryGrid.displayGrid();
		
		//CREATE CITY
		int[] energyConsumptionTiersDesMoines = {1, 1, 1, 1, 1, 2, 2, 2, 2, 4, 5, 4, 2, 5, 5, 3, 3, 1, 1, 3, 4, 3, 3, 1};
		int[] energyProductionTiersDesMoines = {1, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 5, 3, 3, 3, 4, 2, 2, 2, 1, 1};
		
		City desMoines = new City("Des Moines", energyConsumptionTiersDesMoines,energyProductionTiersDesMoines);
		CitySimulator citySimulator = new CitySimulator(desMoines);
		
		//CREATE WINDMILL FARM
		//TODO
		
		//CREATE ENERGY COMMANDER
		EnergyCommander energyCommander = new EnergyCommander(batteryGrid);
		
		//SIMULATE
		for(double x : citySimulator.constructMagnitudeByMillisecondArray())
		{
			System.out.println(x);
		}
		System.out.println("we made it");
		
		citySimulator.simulate();
		
		ArrayList<GraphDataPoint> graphDataPoints = new ArrayList<GraphDataPoint>();
		
		//TimeUnit.SECONDS.sleep(300);
		
		//batteryGrid.displayGrid();
	}

}
 