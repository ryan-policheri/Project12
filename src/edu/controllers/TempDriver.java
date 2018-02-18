package edu.controllers;

import edu.model.city.City;
import edu.model.city.CitySimulator;
import edu.model.energySources.windmillFarm.WindCondition;
import edu.model.energySources.windmillFarm.Windmill;
import edu.model.energySources.windmillFarm.WindmillFarm;
import edu.model.energySources.windmillFarm.WindmillFarmSimulator;

import java.util.Timer;
import java.util.TimerTask;

public class TempDriver
{
    public static void main(String[] args)
    {
        //CREATE CITY
        int[] workingEnergyConsumptionTiersDesMoines = {1,1,1,1,1,2,2,2,2,4,5,4,2,5,5,3,3,1,1,3,4,3,3,1};

        City yeahBitch = new City("Yeah Bitch", workingEnergyConsumptionTiersDesMoines, 0.1);

        //CREATE WINDMILL FARM
        int[] energyProductionTiersWarrenCountyWindmiillFarm = {1,1,1,1,1,2,2,5,5,5,2,1,2,1,4,3,4,5,5,4,3,2,1,1};

        //WindmillFarm warrenCountyWindmillFarm = new WindmillFarm("Warren County Windmill Farm", energyProductionTiersWarrenCountyWindmiillFarm);
        //WindmillFarmSimulator windmillFarmSimulator = new WindmillFarmSimulator(warrenCountyWindmillFarm);

        Windmill windmill = new Windmill("funk dawg", 5000000);
        WindmillFarm funkDawgFarm = new WindmillFarm("funk dawg farm", energyProductionTiersWarrenCountyWindmiillFarm, 0.1);

        for (int i = 0; i < 100; i++)
        {
            funkDawgFarm.addWindmill(new Windmill("funk dawg", 5000000));
        }

        //WindmillFarmSimulator funkDawgFarmSimulator = new WindmillFarmSimulator(funkDawgFarm, energyProductionTiersWarrenCountyWindmiillFarm, .1);
        //CitySimulator yeahBitchCitySimulator = new CitySimulator(yeahBitch, workingEnergyConsumptionTiersDesMoines, 0.1);

        long intervalInMilliseconds = 1000;
        long terminateCounter = 0;
        long amountOfTimesRan = 240000;

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

            double cityDemand = yeahBitch.nextDemand();
            double farmSurplus = funkDawgFarm.nextSurplus();
            //System.out.println("City Demand: " + cityDemand + " Farm Surplus: " + farmSurplus);
            System.out.println(cityDemand + "," + farmSurplus);

            if (terminateCounter == amountOfTimesRan)
            {
                done = true;
            }
        }

/*        double total = 0;

        for (int windTier : energyProductionTiersWarrenCountyWindmiillFarm)
        {
            for (int percentOfHour = 0; percentOfHour < 1; percentOfHour += 1)
            {
                WindCondition windCondition = new WindCondition(windTier, 0.35);
                total += funkDawgFarm.aggregateWindmillEnergyOutput(windCondition, 1);
            }
        }

        System.out.println(total);*/
    }
}
