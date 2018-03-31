package edu.model.energySources.solarFarm;

import java.util.Random;

public class SolarCondition
{
    final static private int secondsInAnHour = 3600;

    private int solarTier;

    private double[] solarTierPercentOfDailyProduction = {0.0,0.01,0.08,0.12,0.16};
    private double currentCapacityFactor;
    private double solarVariabilityAsPercentDeviation = 0.15;

    public SolarCondition(int solarTier)
    {
        this.solarTier = solarTier;
    }

    public double calculateSolarTierXSurplusForOneSquareMeter(double KWhPerSquareMeterForToday, double conversionEfficiency, double timeFrameAsPercentageOfHour)
    {
        double percentOfDailyPowerProducedThisHour = solarTierPercentOfDailyProduction[this.solarTier - 1]; //subtract 1 from solar tier so it matches array index
        this.currentCapacityFactor = this.fluctuatePercentage(percentOfDailyPowerProducedThisHour);

        double KWPerSquareMeterForToday = KWhPerSquareMeterForToday * this.secondsInAnHour; //convert daily KWh to daily KW
        double wattsPerSquareMeterForToday = KWPerSquareMeterForToday * 1000; //convert KW to watts

        double wattsGeneratedThisHour = (this.currentCapacityFactor * wattsPerSquareMeterForToday) * conversionEfficiency;
        double wattsGeneratedThisTimeFrame = wattsGeneratedThisHour * timeFrameAsPercentageOfHour;

        return wattsGeneratedThisTimeFrame;
    }

    private double fluctuatePercentage(double percentageAsDecimal)
    {
        if (percentageAsDecimal != 0.0)
        {
            //generate magnitude of flucuation
            double minPercentDeviation = 0.0;
            double maxPercentDeviation = this.solarVariabilityAsPercentDeviation * percentageAsDecimal;

            Random random = new Random();
            double randomPercentDeviation = minPercentDeviation + ((maxPercentDeviation - minPercentDeviation) * random.nextDouble());

            //generate sign of fluctuation
            boolean sign;
            sign = random.nextBoolean();

            if (sign == true)
            {
                randomPercentDeviation *= -1;
            }

            //fluctuate the original percent and return
            percentageAsDecimal += randomPercentDeviation;
        }
        //else we are keeping it as 0, no deviation

        return percentageAsDecimal;
    }


}
