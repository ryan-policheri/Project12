package edu.model.batteries;

import edu.controllers.Controller;

/**
 * Created by Aaron on 1/28/2018.
 */
public abstract class ConstantFlowBattery extends Battery
{
    boolean batteryCharging;
    boolean gaveEnergyThisHour;

    //CONSTRUCTORS
    public ConstantFlowBattery(String batteryName, double massInKilograms)
    {
        super(batteryName, massInKilograms);
        //this.batteryCharging = false;
    }

    public double storeEnergy(double incomingEnergyInJoules){return -1;}

    public double releaseEnergy(double energyDemandInJoules){return -1;}


    public boolean checkIfCharging()
    {
        return this.batteryCharging;
    }

    public boolean checkIfGaveEnergyThisHour()
    {
        return  this.gaveEnergyThisHour;
    }

}
