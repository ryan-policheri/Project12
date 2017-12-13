package edu.controllers;

import edu.model.EnergyCommander;
import edu.model.GraphDataPoint;
import edu.model.batteries.BatteryGrid;
import edu.model.batteries.Demand;
import edu.model.batteries.RotationalBattery;
import edu.model.batteries.Surplus;
import edu.model.city.City;
import edu.model.city.CitySimulator;
import edu.model.energySources.windmillFarm.WindmillFarm;
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
		
		//Make using 2nd constructor
		
		//magnetic bearing
		FlywheelBearing magneticBearing = new FlywheelBearing("Magnetic");
		
		//super conductor bearing
		FlywheelBearing modernBearing = new FlywheelBearing("Modern");
		
		
		//MAKE THE GRID
		BatteryGrid batteryGrid = new BatteryGrid();
		
		//ADD THE BATTERIES
		double GB_Supreme_Count = 5;
		double GB_PowerHouse_Count = 5;
		double GB_Classic_Count = 5;
		double GB_Lite_Count = 5;
		
		double RB_MegaSonic_Count = 5;
		double RB_SuperSonic_Count = 5;
		double RB_BigSexy_Count = 5;
		double RB_LittleTitan_Count = 0;
		double RB_Classic_Count = 0;
		
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
		
		
		//GIVE THE BATTERY GRID SOME JUICE TO START OUT WITH
		
		for(int i = 0; i < 5; i++)
		{
			batteryGrid.allocateEnergySurplus(new Surplus(19200000,5));
		}
		
		TimeUnit.SECONDS.sleep(10);
		
		batteryGrid.displayGrid();
		
		
		//CREATE CITY
		int[] energyConsumptionTiersDesMoines = {1, 1, 1, 1, 1, 2, 2, 2, 2, 4, 5, 4, 2, 5, 5, 3, 3, 1, 1, 3, 4, 3, 3, 1};
		
		City desMoines = new City("Des Moines", energyConsumptionTiersDesMoines);
		CitySimulator citySimulator = new CitySimulator(desMoines);
		
		
		//CREATE WINDMILL FARM
		int[] energyProductionTiersWarrenCountyWindmiillFarm = {1, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 5, 3, 3, 3, 4, 2, 2, 2, 1, 1};
		
		WindmillFarm warrenCountyWindmillFarm = new WindmillFarm("Warren County Windmill Farm", energyProductionTiersWarrenCountyWindmiillFarm);
		WindmillFarmSimulator windmillFarmSimulator = new WindmillFarmSimulator(warrenCountyWindmillFarm);
		
		
		//CREATE ENERGY COMMANDER
		EnergyCommander energyCommander = new EnergyCommander(batteryGrid);
		
		
		//BUILD ARRAYS THAT GIVE THE TOTAL ENERGY EVERY MILLISECOND - FOR THE USER INTERFACE GRAPHS
		citySimulator.constructMagnitudeByMillisecondArray();
		//windmillFarmSimulator.constructMagnitudeByMillisecondArray();
		
		
		//SIMULATE
		
/*		new Thread(new Runnable() {
		     public void run() {
		    	 citySimulator.simulate();
		     }
		}).start();*/
		
/*		new Thread(new Runnable() {
		     public void run() {
		    	 windmillFarmSimulator.simulate();
		     }
		}).start();*/
		
		//windmillFarmSimulator.simulate();
		//citySimulator.simulate();

		
		//TimeUnit.SECONDS.sleep(300);
		
		//batteryGrid.displayGrid();
	}

}
 