package edu.controllers;

import edu.model.batteries.*;
import edu.model.city.City;
import edu.model.energySources.solarFarm.PhotovoltaicSolarFarm;
import edu.model.energySources.windmillFarm.Windmill;
import edu.model.energySources.windmillFarm.WindmillFarm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class BuildDefaults
{
    private static String splitCharacter = ":";

    protected static ArrayList<WindmillFarm> createListOfDefaultWindFarms(City city, double timeFrameAsPercentageOfHour)
    {
        ArrayList<WindmillFarm> windmillFarms = new ArrayList<WindmillFarm>();

        String filePathForWindFarms = "..\\Capstone\\src\\edu\\controllers\\DefaultWindFarmsByCity.txt";

        try
        {
            File file = new File(filePathForWindFarms);
            Scanner scanner = new Scanner(file);

            String cityName = city.toString();
            int[] defualtWindTiersForCity = city.getDefaultWindTiersByHour();

            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();

                if (line.equals("*")) //denotes that there is a city name on the next line
                {
                    line = scanner.nextLine();

                    if (line.equals(cityName)) //if this is true we found the city we are looking for
                    {
                        boolean done = false;

                        while(done == false)
                        {
                            line = scanner.nextLine();

                            if (line.equals("#")) //denotes that we have a farm
                            {
                                line = scanner.nextLine();
                                String windFarmName = line.split(splitCharacter)[1];
                                WindmillFarm windFarm = new WindmillFarm(windFarmName, defualtWindTiersForCity, timeFrameAsPercentageOfHour);

                                boolean done2 = false;

                                while(done2 == false)
                                {
                                    line = scanner.nextLine();

                                    if (line.equals("$")) //denotes a set of turbines in the farm
                                    {
                                        line = scanner.nextLine();
                                        String windTurbineName = line.split(splitCharacter)[1];
                                        line = scanner.nextLine();
                                        double windTurbineMaxCapacity = Double.parseDouble(line.split(splitCharacter)[1]);
                                        line = scanner.nextLine();
                                        int windTurbineCount = Integer.parseInt(line.split(splitCharacter)[1]);

                                        for (int i = 0; i < windTurbineCount; i++)
                                        {
                                            Windmill windmill = new Windmill(windTurbineName, windTurbineMaxCapacity);
                                            windFarm.addWindmill(windmill);
                                        }
                                    }
                                    else if(line.equals("=") || line.equals(""))
                                    {
                                        done2 = true;
                                    }
                                }
                                windmillFarms.add(windFarm);
                            }
                            else if (line.equals("*") || line.equals("="))
                            {
                                done = true;
                            }
                        }
                    }
                }
            }

            scanner.close();
        }
        catch(IOException ex)
        {
            System.out.println("Problem loading default wind farms");
        }

        return windmillFarms;
    }

    protected static ArrayList<PhotovoltaicSolarFarm> createListOfDefaultPVSolarFarms(City city, double timeFrameAsPercentageOfHour)
    {
        ArrayList<PhotovoltaicSolarFarm> PVSolarFarms = new ArrayList<PhotovoltaicSolarFarm>();

        String filePathForPVSolarFarms = "..\\Capstone\\src\\edu\\controllers\\DefaultPVSolarFarmsByCity.txt";

        try
        {
            File file = new File(filePathForPVSolarFarms);
            Scanner scanner = new Scanner(file);

            String cityName = city.toString();
            int currentMonth = -1;

            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();

                if (line.equals("@"))
                {
                    line = scanner.nextLine();
                    currentMonth = Integer.parseInt(line.split(splitCharacter)[1]);
                }

                if (line.equals("*")) //denotes that there is a city name on the next line
                {
                    line = scanner.nextLine();

                    if (line.equals(cityName)) //if this is true we found the city we are looking for
                    {
                        boolean done = false;

                        while(done == false)
                        {
                            line = scanner.nextLine();

                            if (line.equals("#")) //denotes that we have a farm
                            {
                                line = scanner.nextLine();
                                String PVSolarFarmName = line.split(splitCharacter)[1];
                                line = scanner.nextLine();
                                double PVSolarFarmSize = Double.parseDouble(line.split(splitCharacter)[1]);
                                line = scanner.nextLine();
                                double PVSolarFarmConversionEfficiency = Double.parseDouble(line.split(splitCharacter)[1]);
                                PhotovoltaicSolarFarm PVSolarFarm = new PhotovoltaicSolarFarm(PVSolarFarmName, PVSolarFarmSize, PVSolarFarmConversionEfficiency, currentMonth,timeFrameAsPercentageOfHour);

                                PVSolarFarms.add(PVSolarFarm);
                            }
                            else if (line.equals("*") || line.equals("="))
                            {
                                done = true;
                            }
                        }
                    }
                }
            }

            scanner.close();
        }
        catch(IOException ex)
        {
            System.out.println("Problem loading default PV solar farms");
        }

        return PVSolarFarms;
    }

    protected static ArrayList<VolatileBattery> createListOfDefaultVolatileBatteries()
    {
        ArrayList<VolatileBattery> volatileBatteries = new ArrayList<VolatileBattery>();

        String filePathForDefaultVolatileBatteries = "..\\Capstone\\src\\edu\\controllers\\DefaultVolatileBatteries.txt";

        try
        {
            File file = new File(filePathForDefaultVolatileBatteries);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();

                if (line.equals("()")) //denotes that there is a rotational battery starting on next line
                {
                    line = scanner.nextLine();
                    String batteryName = line.split(splitCharacter)[1];
                    line = scanner.nextLine();
                    double massInKilograms = Double.parseDouble(line.split(splitCharacter)[1]);
                    line = scanner.nextLine();
                    double radiusInMeters = Double.parseDouble(line.split(splitCharacter)[1]);
                    line = scanner.nextLine();
                    String materialName = line.split(splitCharacter)[1];
                    line = scanner.nextLine();
                    String bearingName = line.split(splitCharacter)[1];
                    line = scanner.nextLine();
                    int amountOfBatteriesOfThisType = Integer.parseInt(line.split(splitCharacter)[1]);

                    FlywheelMaterial flywheelMaterial = new FlywheelMaterial(materialName);
                    FlywheelBearing flywheelBearing = new FlywheelBearing(bearingName);

                    for(int i = 0; i < amountOfBatteriesOfThisType; i++)
                    {
                        RotationalBattery rotationalBattery = new RotationalBattery(batteryName + i, massInKilograms, radiusInMeters, flywheelMaterial, flywheelBearing);
                        volatileBatteries.add(rotationalBattery);
                    }
                }

            }

            scanner.close();
        }
        catch(IOException ex)
        {
            System.out.println("Problem loading default volatile batteries");
        }

        return volatileBatteries;
    }

    protected static ArrayList<ConstantFlowBattery> createListOfDefaultConstantFlowBatteries() {
        ArrayList<ConstantFlowBattery> constantFlowBatteries = new ArrayList<ConstantFlowBattery>();
        int batteriesOfThisType = 2;
        for(int i=0; i < batteriesOfThisType; i++)
        {
            HydroelectricBattery tempBattery = new HydroelectricBattery("Heindl_Battery_" + i, -1, 2600,125);

            if(i == 0)
            {
                tempBattery.setBatteryToCharging();
            }
            if(i == 1)
            {
                tempBattery.storeEnergy(tempBattery.getMaxEnergyInJoules());
            }
            constantFlowBatteries.add(tempBattery);
        }

        return constantFlowBatteries;
    }

}
