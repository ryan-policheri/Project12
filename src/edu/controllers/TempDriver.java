package edu.controllers;

import edu.model.EnergyCommander;
import edu.model.batteries.BatteryGrid;
import edu.model.batteries.VolatileBattery;
import edu.model.city.City;
import edu.model.energySources.solarFarm.PhotovoltaicSolarFarm;
import edu.model.energySources.windmillFarm.WindmillFarm;

import java.util.ArrayList;

public class TempDriver
{
    public static void main(String[] args)
    {
        //default wind tiers - actually initialized in city. put here just for reference
        //int[] defaultWindTiers = {1,1,1,1,1,2,2,5,5,5,2,1,2,1,4,3,4,5,5,4,3,2,1,1};

        //CREATE CITY
        City desMoines = new City("Des Moines", 0.1);
        City chicago = new City("Chicago", 0.1);

        //CREATE ENERGY SOURCES
        ArrayList<WindmillFarm> windFarms = BuildDefaults.createListOfDefaultWindFarms(desMoines,0.1);
        ArrayList<PhotovoltaicSolarFarm> PVSolarFarms = BuildDefaults.createListOfDefaultPVSolarFarms(desMoines, 0.1);

        //CREATE BATTERY GRID
        BatteryGrid batteryGrid = new BatteryGrid();

        ArrayList<VolatileBattery> volatileBatteries = BuildDefaults.createListOfDefaultVolatileBatteries();
        //ArrayList<ConstantFlowBattery> constantFlowBatteries = BuildDefaults.createListOfDefaultConstantFlowBatteries();

        batteryGrid.addListOfRotationalBatteries(volatileBatteries);
        //batteryGrid.addListOfHeindlBatteries(constantFlowBatteries);

        //BUILD ENERGY COMMANDER
        EnergyCommander energyCommander = new EnergyCommander(batteryGrid);

        long intervalInMilliseconds = 1000;
        long terminateCounter = 0;
        long amountOfTimesRan = 239;

        boolean done = false;

        while (done == false)
        {
            terminateCounter += 1;

            try
            {
                Thread.sleep(intervalInMilliseconds);
            }
            catch (InterruptedException ie)
            {
                System.out.println("Catastrophic failure");
            }

            double cityDemand = desMoines.nextDemand();

            double energyProduced = 0;

            for (WindmillFarm windFarm: windFarms)
            {
                double windFarmSurplus = windFarm.nextSurplus();
                energyProduced += windFarmSurplus;
            }
            for (PhotovoltaicSolarFarm PVSolarFarm : PVSolarFarms)
            {
                double solarFarmSurplus = PVSolarFarm.nextSurplus();
                energyProduced += solarFarmSurplus;
            }

            EnergyCommander.commandEnergy(energyProduced, cityDemand);

            //System.out.println("City Demand: " + cityDemand + " Farm Surplus: " + farmSurplus);
            System.out.println(cityDemand + "," + energyProduced);

            if (terminateCounter == amountOfTimesRan)
            {
                done = true;
            }
        }

        System.out.println("Energy Shortage: " + batteryGrid.getEnergyShortageInWatts());
        System.out.println("Amount Of Energy Shortages: " + batteryGrid.getAmountOfEnergyShortages());
        System.out.println("Energy Wasted: " + batteryGrid.getEnergyWasted());
    }
}
