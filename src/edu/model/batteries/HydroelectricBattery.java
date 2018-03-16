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

    //private int currentSample;
    //private int samplesPerTier;
    //private int currentHourlyMaxIndex;
    //private double currentHourlyMax;
    //private double timeFrameAsPercentageOfHour;


    //private double maxEnergyOutputInJoules;
    //private double maxEnergyInputInJoules;

    //CONSTRUCTORS
    public HydroelectricBattery(String batteryName, double massInKilograms, double densityOfMassInKilogramMetersCubed, double radiusInMeters){
        super(batteryName, massInKilograms);

        this.radiusInMeters = radiusInMeters;
        this.densityOfMassInKilogramMetersCubed = densityOfMassInKilogramMetersCubed;
        this.maxLiftHeightInMeters = radiusInMeters;
        this.currentLiftHeightInMeters = 0;
        this.initializedMaxEnergyInJoulesForHydroelectricBattery(this.maxLiftHeightInMeters, this.densityOfMassInKilogramMetersCubed, this.radiusInMeters);


        //this.samplesPerTier = (int) (1 / timeFrameAsPercentageOfHour);
        //this.currentSample = 0;
        //this.currentHourlyMaxIndex = 0;
        //this.currentHourlyMax = 0;

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
            remainingJoules = currentEnergyInJoules + incomingEnergyInJoules - maxEnergyInJoules;
            this.setBatteryToSupplying();
        }

        this.adjustCurrentEnergyInJoulesForHydroelectricBattery(this.currentLiftHeightInMeters, this.densityOfMassInKilogramMetersCubed, this.radiusInMeters);
        System.out.println(this.currentLiftHeightInMeters);
        return remainingJoules;
    }

    public double releaseEnergy(double energyDemandInJoules, double maximumOutput){

        double joulesThatCanBeProvided = this.getCurrentEnergyInJoules();

        double remainingJoulesNeeded;

        if (joulesThatCanBeProvided <= energyDemandInJoules && joulesThatCanBeProvided <= maximumOutput)
        {
            this.setCurrentEnergyInJoulesToZero();
            this.currentLiftHeightInMeters = 0;
            remainingJoulesNeeded = energyDemandInJoules - joulesThatCanBeProvided;
        }
        else if (energyDemandInJoules < joulesThatCanBeProvided && energyDemandInJoules < maximumOutput)
        {
            double tempEnergyInJoules = joulesThatCanBeProvided - energyDemandInJoules;
            this.calculateCurrentLiftHeight(tempEnergyInJoules);
            this.adjustCurrentEnergyInJoulesForHydroelectricBattery(this.currentLiftHeightInMeters, this.densityOfMassInKilogramMetersCubed, this.radiusInMeters);
            remainingJoulesNeeded = 0;
        }
        else
        {
            double tempEnergyInJoules = joulesThatCanBeProvided - maximumOutput;
            this.calculateCurrentLiftHeight(tempEnergyInJoules);
            this.adjustCurrentEnergyInJoulesForHydroelectricBattery(this.currentLiftHeightInMeters, this.densityOfMassInKilogramMetersCubed, this.radiusInMeters);
            remainingJoulesNeeded = energyDemandInJoules - maximumOutput;
        }
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