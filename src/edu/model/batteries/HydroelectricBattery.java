package edu.model.batteries;

/**
 * Created by Aaron on 1/30/2018.
 */
public class HydroelectricBattery extends ConstantFlowBattery{

    //ATTRIBUTES
    private final double radiusInMeters;
    private final double densityOfMassInKilogramMetersCubed;

    private final double maxLiftHeightInMeters;
    private double currentLiftHeightInMeters;

    //private double maxEnergyOutputInJoules;
    //private double maxEnergyInputInJoules;

    //CONSTRUCTORS
    public HydroelectricBattery(String batteryName, double densityOfMassInKilogramMetersCubed, double radiusInMeters, double massInKilograms){
        super(batteryName, massInKilograms);

        this.radiusInMeters = radiusInMeters;
        this.densityOfMassInKilogramMetersCubed = densityOfMassInKilogramMetersCubed;
        this.maxLiftHeightInMeters = radiusInMeters;
        this.currentLiftHeightInMeters = 0;
        this.initializedMaxEnergyInJoulesForHydroelectricBattery(this.maxLiftHeightInMeters, this.densityOfMassInKilogramMetersCubed, this.radiusInMeters);
        //this.maxEnergyOutputInJoules
        //this.maxEnergyInputInJoules
    }

    public double storeEnergy(double incomingEnergyInJoules){

        double remainingJoules;
        double currentEnergyInJoules = this.getCurrentEnergyInJoules();
        double maxEnergyInJoules = this.getMaxEnergyInJoules();

        if (incomingEnergyInJoules + currentEnergyInJoules <= maxEnergyInJoules){
            remainingJoules = 0;
            this.currentLiftHeightInMeters = calculateCurrentLiftHeight(currentEnergyInJoules + incomingEnergyInJoules);
        } else {
            this.currentLiftHeightInMeters = maxLiftHeightInMeters;
            remainingJoules = currentEnergyInJoules +incomingEnergyInJoules - maxEnergyInJoules;
        }

        this.adjustCurrentEnergyInJoulesForHydroelectricBattery(this.currentLiftHeightInMeters, this.densityOfMassInKilogramMetersCubed, this.radiusInMeters);
        return remainingJoules;
    }

    public double releaseEnergy(double energyDemandInJoules){

        double joulesThatCanBeProvided = this.getCurrentEnergyInJoules();

        double remainingJoulesNeeded = energyDemandInJoules;

        //if(energyDemandInJoules < this.maxEnergyOutputInJoules) {
            if (joulesThatCanBeProvided - energyDemandInJoules > 0) {
                double newCurrentEnergyInJoules = joulesThatCanBeProvided - energyDemandInJoules;
                this.currentLiftHeightInMeters = calculateCurrentLiftHeight(newCurrentEnergyInJoules);
                this.adjustCurrentEnergyInJoulesForHydroelectricBattery(this.currentLiftHeightInMeters, this.densityOfMassInKilogramMetersCubed, this.radiusInMeters);
            } else {
                this.setCurrentEnergyInJoulesToZero();
                this.currentLiftHeightInMeters = 0;
                remainingJoulesNeeded = energyDemandInJoules - joulesThatCanBeProvided;
            }
        //}

        return remainingJoulesNeeded;
    }

    public double calculateCurrentLiftHeight(double currentEnergyInJoules){
        double densityStuff = (2*densityOfMassInKilogramMetersCubed-3/2*densityOfWater);
        double radiusStuff = Math.PI*forceOfGravity*Math.pow(radiusInMeters,3.0);
        double currentHeight = currentEnergyInJoules/(densityStuff*radiusStuff);
        return currentHeight;
    }

    public String getBatteryType() {
        return "Hydroelectric Battery";
    };

    public String toString() {
        return "Hydroelectric Battery: " + this.getBatteryName();
    };



}
