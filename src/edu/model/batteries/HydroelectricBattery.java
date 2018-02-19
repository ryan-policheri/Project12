package edu.model.batteries;

/**
 * Created by Aaron on 1/30/2018.
 */
public class HydroelectricBattery extends ConstantFlowBattery{

    //ATTRIBUTES
    private final double radiusInMeters;
    private final double maxLiftHeightInMeters;
    private final double densityOfMassInKilogramMetersCubed;
    private final double lengthOfMassInMeters;
    private final double pressureOfWaterInSomeKindOfMeasurement;

    private double currentLiftHeightInMeters;


    //CONSTRUCTORS
    public  HydroelectricBattery(String batteryName, double densityOfMassInKilogramMetersCubed, double radiusInMeters, double massInKilograms){
        super(batteryName, massInKilograms);
        this.radiusInMeters = radiusInMeters;
        this.densityOfMassInKilogramMetersCubed = densityOfMassInKilogramMetersCubed;
        this.maxLiftHeightInMeters = radiusInMeters;
        this.lengthOfMassInMeters = 2 * radiusInMeters;
        this.pressureOfWaterInSomeKindOfMeasurement = 0;
        this.currentLiftHeightInMeters = 0;
    }

}
