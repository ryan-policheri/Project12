package edu.model.energySources.windmillFarm;

import java.util.Random;

public class WindCondition
{
    private int windTier;
    private double windVariabilityAsPercentDeviation;
    private final int secondsInHour = 3600;

    private double[] windTierPercentOfMaxCapacityValues = {0.1,0.15,0.35,0.6,0.85};

    private double currentCapacityFactor;

    public WindCondition(int windTier, double windVariabilityAsPercentDeviation)
    {
        this.windTier = windTier;
        this.windVariabilityAsPercentDeviation = windVariabilityAsPercentDeviation;

        this.setCurrentCapacityFactor();
    }

    protected double calculateSurplus(double maxCapacityInWatts, double timeFrameAsPercentageOfHour)
    {
        double wattsGeneratedHourly = maxCapacityInWatts * this.currentCapacityFactor * this.secondsInHour;
        double wattsGeneratedForTimeFrame = timeFrameAsPercentageOfHour * wattsGeneratedHourly;

        return wattsGeneratedForTimeFrame;
    }

    private void setCurrentCapacityFactor()
    {
        double capacityFactor = this.windTierPercentOfMaxCapacityValues[this.windTier - 1];
        this.currentCapacityFactor = fluctuatePercentage(capacityFactor);
    }

    private double fluctuatePercentage(double percentageAsDecimal)
    {
        if (percentageAsDecimal != 0.0)
        {
            //generate magnitude of flucuation
            double minPercentDeviation = 0.0;
            double maxPercentDeviation = this.windVariabilityAsPercentDeviation * percentageAsDecimal;

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
