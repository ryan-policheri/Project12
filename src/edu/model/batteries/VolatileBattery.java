package edu.model.batteries;

import java.util.Timer;
import java.util.TimerTask;

public abstract class VolatileBattery extends Battery
{
	//ATTRIBUTES
	private boolean inUse = false;
	
	//CONSTRUCTORS
	public VolatileBattery(String batteryName, double massInKilograms)
	{
		super(batteryName, massInKilograms);
	}

	//FUNCTIONS
	
	//functions overridden in child classes
	public Surplus storeEnergy(Surplus surplus)
	{
		return null;
	}

	public Demand releaseEnergy(Demand demand)
	{
		return null;
	}
	
	//functions used in child classes
	protected void startInUseTimer(double timeInUseInSeconds)
	{
		this.inUse = true;
		long timeInUseInMilliseconds = (long) (timeInUseInSeconds * 1000);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
										  @Override
										  public void run()
										  {
											  markBatteryNotInUse();
											  timer.cancel();
										  }
									   }
						, timeInUseInMilliseconds);
	}
	
	private void markBatteryNotInUse()
	{
		this.inUse = false;
	}
	
	//other functions
	public boolean isBatteryInUse()
	{
		return this.inUse;
	}
	
	
}
