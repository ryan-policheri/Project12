package edu.model.energySources.solarFarm;

public class SolarCondition
{
    private int solarTier;

    private double[] solarTierPercentOfDailyProduction = {0.0,0.01,0.08,0.12,0.16};

    public SolarCondition(int solarTier)
    {
        this.solarTier = solarTier;
    }

    public double calculateSolarTierXSurplusForOneSquareMeter(double KWhPerSquareMeterForToday, double conversionEfficiency, double timeFrameAsPercentageOfHour)
    {
        double percentOfDailyPowerProducedThisHour = solarTierPercentOfDailyProduction[this.solarTier - 1]; //subtract 1 from solar tier so it matches array index
        double wattsGeneratedThisHour = (percentOfDailyPowerProducedThisHour * KWhPerSquareMeterForToday) * conversionEfficiency;
        double wattsGeneratedThisTimeFrame = wattsGeneratedThisHour / timeFrameAsPercentageOfHour;

        return wattsGeneratedThisTimeFrame;
    }


}
