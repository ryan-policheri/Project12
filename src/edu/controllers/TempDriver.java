package edu.controllers;

import edu.model.city.City;
import edu.model.city.CitySimulator;
import edu.model.energySources.solarFarm.PhotovoltaicSolarFarm;
import edu.model.energySources.windmillFarm.WindCondition;
import edu.model.energySources.windmillFarm.Windmill;
import edu.model.energySources.windmillFarm.WindmillFarm;
import edu.model.energySources.windmillFarm.WindmillFarmSimulator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TempDriver
{
    public static void main(String[] args)
    {
        //CREATE CITY
        City desMoines = new City("Des Moines", 0.1);

        ArrayList<WindmillFarm> windFarms = BuildDefaults.createListOfDefaultWindFarms(desMoines,0.1);
        System.out.println(windFarms);

/*        //CREATE WINDMILL FARM
        int[] energyProductionTiersWarrenCountyWindmiillFarm = {1,1,1,1,1,2,2,5,5,5,2,1,2,1,4,3,4,5,5,4,3,2,1,1};

        Windmill windmill = new Windmill("funk master", 5000000);
        WindmillFarm warrenCountyWindFarm = new WindmillFarm("Warren County Wind Farm", energyProductionTiersWarrenCountyWindmiillFarm, 0.1);

        for (int i = 0; i < 100; i++)
        {
            warrenCountyWindFarm.addWindmill(new Windmill("funk master", 5000000));
        }*/

/*
        //CREATE PHOTOVOLTAIC SOLAR FARM
        PhotovoltaicSolarFarm warrenCountySolarFarm = new PhotovoltaicSolarFarm("Warren County Solar Farm", 10000, 0.2, 2, 0.1);
*/

/*        long intervalInMilliseconds = 1000;
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

            double cityDemand = desMoines.nextDemand();
            double windFarmSurplus = warrenCountyWindFarm.nextSurplus();
            double solarFarmSurplus = warrenCountySolarFarm.nextSurplus();
            //System.out.println("City Demand: " + cityDemand + " Farm Surplus: " + farmSurplus);
            System.out.println(cityDemand + "," + windFarmSurplus + "," + solarFarmSurplus);

            if (terminateCounter == amountOfTimesRan)
            {
                done = true;
            }
        }*/

/*        double total = 0;

        for (int windTier : energyProductionTiersWarrenCountyWindmiillFarm)
        {
            for (int percentOfHour = 0; percentOfHour < 1; percentOfHour += 1)
            {
                WindCondition windCondition = new WindCondition(windTier, 0.35);
                total += warrenCountyWindFarm.aggregateWindmillEnergyOutput(windCondition, 1);
            }
        }

        System.out.println(total);*/
    }
}
