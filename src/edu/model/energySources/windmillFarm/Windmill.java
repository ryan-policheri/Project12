package edu.model.energySources.windmillFarm;

//website about converting windmill capacity to actual output: https://www.wind-watch.org/faq-output.php
//website about conversion wind speed to energy: https://www.windpowerengineering.com/construction/calculate-wind-power-output/

import java.util.Random;

public class Windmill
{
    private final String modelName;
    private final double maxCapacityInWatts; //the maximum watt output (per second)

    private double capacityFactor; //this is a percentage. .15 - .30 are typical in real world

    private boolean windmillShutdown;

    public Windmill(String modelName, double maxCapacityInWatts)
    {
        this.modelName = modelName;
        this.maxCapacityInWatts = maxCapacityInWatts;

        windmillShutdown = false;
    }

    protected double calculateCurrentMegawattHours(WindCondition windCondition, double timeFrameAsPercentageOfHour)
    {
        return windCondition.randomizeWindTierXSurplus(this.maxCapacityInWatts, timeFrameAsPercentageOfHour);
    }

    public String toString()
    {
        return this.modelName + " " + this.maxCapacityInWatts;
    }

}
