package edu.model.batteries;

import edu.controllers.Controller;

/**
 * Created by Aaron on 1/28/2018.
 */
public abstract class ConstantFlowBattery extends Battery
{
    private boolean batteryCharging;
    private boolean gaveEnergyThisSample;


    //CONSTRUCTORS
    public ConstantFlowBattery(String batteryName, double massInKilograms)
    {
        super(batteryName, massInKilograms);
        this.batteryCharging = false;
    }

    public double storeEnergy(double incomingEnergyInJoules){return -1;}

    public double releaseEnergy(double energyDemandInJoules, double maximumOutput){return -1;}

    public boolean checkIfCharging()
    {
        return this.batteryCharging;
    }

    public boolean checkIfGaveEnergyThisHour()
    {
        return  this.gaveEnergyThisSample;
    }

    public void setGaveEnergyThisSampleToTrue()
    {
        this.gaveEnergyThisSample = true;
    }

    public void setGaveEnergyThisSampleToFalse()
    {
        this.gaveEnergyThisSample = false;
    }

    public void setBatteryToCharging()
    {
        this.batteryCharging = true;
    }

    public void setBatteryToSupplying()
    {
        this.batteryCharging = false;
    }

}
