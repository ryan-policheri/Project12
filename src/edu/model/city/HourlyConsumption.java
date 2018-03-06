package edu.model.city;

public class HourlyConsumption
{
    private double minimumHourlyConsumptionInWatts;
    private double maxDeviationInWatts;

    public HourlyConsumption(double minimumHourlyConsumptionInWatts, double maxDeviationInWatts)
    {
        this.minimumHourlyConsumptionInWatts = minimumHourlyConsumptionInWatts;
        this.maxDeviationInWatts = maxDeviationInWatts;
    }

    public double calculateConsumption(double timeFrameAsPercentageOfHour)
    {
        double minimumDemand = this.minimumHourlyConsumptionInWatts;
        double maxDeviation = this.maxDeviationInWatts;
        double maximumDemand = this.minimumHourlyConsumptionInWatts + maxDeviation;

        double randomDemand = minimumDemand + (Math.random() * ((maximumDemand - minimumDemand) + 1));
        randomDemand *= timeFrameAsPercentageOfHour;

        return randomDemand;
    }

    public String toString()
    {
        return this.minimumHourlyConsumptionInWatts + "," + this.maxDeviationInWatts;
    }

    public double getMinimumHourlyConsumptionInWatts()
    {
        return minimumHourlyConsumptionInWatts;
    }

    public double getMaxDeviationInWatts()
    {
        return maxDeviationInWatts;
    }
}
