package edu.model.energySources.solarFarm;

public class SolarCondition
{
    final static private int secondsInAnHour = 3600;

    private int solarTier;

    private double[] solarTierPercentOfDailyProduction = {0.0,0.01,0.08,0.12,0.16};

    public SolarCondition(int solarTier)
    {
        this.solarTier = solarTier;
    }

    public double calculateSolarTierXSurplusForOneSquareMeter(double KWhPerSquareMeterForToday, double conversionEfficiency, double timeFrameAsPercentageOfHour)
    {
        double percentOfDailyPowerProducedThisHour = solarTierPercentOfDailyProduction[this.solarTier - 1]; //subtract 1 from solar tier so it matches array index

        double KWPerSquareMeterForToday = KWhPerSquareMeterForToday * this.secondsInAnHour; //convert daily KWh to daily KW
        double wattsPerSquareMeterForToday = KWPerSquareMeterForToday * 1000; //convert KW to watts

        double wattsGeneratedThisHour = (percentOfDailyPowerProducedThisHour * wattsPerSquareMeterForToday) * conversionEfficiency;
        double wattsGeneratedThisTimeFrame = wattsGeneratedThisHour * timeFrameAsPercentageOfHour;

        return wattsGeneratedThisTimeFrame;
    }


}
