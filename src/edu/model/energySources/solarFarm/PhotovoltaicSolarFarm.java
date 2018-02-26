package edu.model.energySources.solarFarm;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class PhotovoltaicSolarFarm
{
    private String name;
    private double solarPanelAreaInSquareMeters;
    private double conversionEfficiencyAsPercent;
    private int currentMonthIndex;
    private double timeFrameAsPercentageOfHour;

    private String filePath = "..\\Capstone\\src\\edu\\model\\energySources\\solarFarm\\SolarData.txt";

    private double[] averageKWhPerSquareMeterPerDayByMonth = new double[12];

    private double averageKWhPerSquareMeterPerDay;
    private double maxSolarVariabilityFromWeatherAsPercentDeviation;
    private double todaysKWhPerSquareMeter;

    private int[] solarTiersByHour = {1,1,1,1,1,1,2,2,3,4,5,5,5,4,3,3,2,2,1,1,1,1,1,1};

    private int samplesPerTier;
    private int currentSolarTier;
    private int currentSolarTierIndex;
    private int currentSample;

    public PhotovoltaicSolarFarm(String name, double solarPanelAreaInSquareMeters, double conversionEfficiencyAsPercent, int currentMonthAsNumber, double timeFrameAsPercentageOfHour)
    {
        this.name = name;
        this.solarPanelAreaInSquareMeters = solarPanelAreaInSquareMeters;
        this.conversionEfficiencyAsPercent = conversionEfficiencyAsPercent;
        this.currentMonthIndex = currentMonthAsNumber - 1;
        this.timeFrameAsPercentageOfHour = timeFrameAsPercentageOfHour;

        this.pullSolarData();

        this.averageKWhPerSquareMeterPerDay = averageKWhPerSquareMeterPerDayByMonth[currentMonthIndex];
        this.maxSolarVariabilityFromWeatherAsPercentDeviation = 0.35;
        this.todaysKWhPerSquareMeter = this.randomizeTodaysKWhPerSquareMeter();

        this.currentSolarTier = this.solarTiersByHour[0];
        this.samplesPerTier = (int) (1 / this.timeFrameAsPercentageOfHour);

        this.currentSolarTierIndex = 0;
        this.currentSample = 0;
    }

    public double nextSurplus()
    {
        SolarCondition solarCondition = new SolarCondition(this.currentSolarTier);

        this.currentSample += 1;

        if (this.currentSample == this.samplesPerTier)
        {
            this.currentSolarTierIndex += 1;
            this.currentSolarTier = this.solarTiersByHour[this.currentSolarTierIndex];
            this.currentSample = 0;
        }

        double wattageOutputPerMeterForTimeFrame = solarCondition.calculateSolarTierXSurplusForOneSquareMeter(this.todaysKWhPerSquareMeter, this.conversionEfficiencyAsPercent, this.timeFrameAsPercentageOfHour);
        double totalWattageOutput = wattageOutputPerMeterForTimeFrame * this.solarPanelAreaInSquareMeters;

        return totalWattageOutput;
    }

    private double randomizeTodaysKWhPerSquareMeter()
    {
        double maxAmountKWhFluctuation = this.averageKWhPerSquareMeterPerDay * this.maxSolarVariabilityFromWeatherAsPercentDeviation;

        double todaysMinimumKWhPerSquareMeter = this.averageKWhPerSquareMeterPerDay - maxAmountKWhFluctuation;
        double todaysMaximumKWhPerSquareMeter = this.averageKWhPerSquareMeterPerDay + maxAmountKWhFluctuation;

        double todaysKWhPerSquareMeter = todaysMinimumKWhPerSquareMeter + ((todaysMaximumKWhPerSquareMeter - todaysMinimumKWhPerSquareMeter) * new Random().nextDouble());

        return todaysKWhPerSquareMeter;
    }

    private void pullSolarData()
    {
        try
        {
            File file = new File(this.filePath);
            Scanner scanner = new Scanner(file);

            int lineNumber = -2; //-2 because there are two lines of header

            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                String[] lineSplit = line.split(",");

                if(lineNumber > -1)
                {
                    double averageKWhPerSquareMeterPerDay = Double.parseDouble(lineSplit[1]);

                    averageKWhPerSquareMeterPerDayByMonth[lineNumber] = averageKWhPerSquareMeterPerDay;
                }

                lineNumber ++;
            }

            scanner.close();
        }
        catch(IOException ex)
        {
            System.out.println("Problem loading solar data");
        }
    }

}
