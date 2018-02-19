package edu.model.batteries;

/**
 * Created by Aaron on 2/5/2018.
 */
public class HydroelectricPump {

    private final int numberOfPumps;
    private final String pumpType;
    private final double pumpPressure;

    public HydroelectricPump(String pumpType,int numberOfPumps, double pumpPressure){
        this.pumpType = pumpType;
        this.numberOfPumps = numberOfPumps;
        this.pumpPressure = pumpPressure;
    }

    public double CalculateTotalPumpPressure(){
        return pumpPressure*numberOfPumps;
    }




}
