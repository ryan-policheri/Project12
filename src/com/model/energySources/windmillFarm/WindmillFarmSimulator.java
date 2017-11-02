package com.model.energySources.windmillFarm;

import com.model.EnergyCommander;
import com.model.batteries.Surplus;

import java.util.Timer;
import java.util.TimerTask;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WindmillFarmSimulator
{
	private double simulatedHourLengthInSeconds = 10;
	private int hoursInDay = 24;
	private int currentHourOfDay = 0;
	
	private String[] hoursOfDay = new String[this.hoursInDay];
	
	private WindmillFarm windmillFarm = new WindmillFarm();
	
	
	public WindmillFarmSimulator()
	{
		buildHoursOfDayArray();
	}
	
	public void simulate()
	{
		long intervalInMilliseconds = (long) (this.simulatedHourLengthInSeconds * 1000);
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
										            @Override
										            public void run()
										            {
										        		Surplus surplus = generatePowerFromWindmillFarm();
										        		sendSurplusThroughEnergyCommander(surplus);
										        		//how do I send this surplus to the energy commander?
										            }
									              }
									, 0, intervalInMilliseconds);
	}
	
	private void sendSurplusThroughEnergyCommander(Surplus surplus)
	{
		EnergyCommander.commandEnergy(surplus);
	}
	
	private Surplus generatePowerFromWindmillFarm()
	{
		double windmillFarmOutput = windmillFarm.calculateWindmillFarmOutput(this.currentHourOfDay);
		
		String AMOrPM = "AM";
		
		if(currentHourOfDay >= 12)
		{
			AMOrPM = "PM";
		}
		
		String printString = "At " + this.hoursOfDay[this.currentHourOfDay] + AMOrPM + " the windmill farm produced " + windmillFarmOutput + " watts.";
		System.out.println(printString);
		
		this.currentHourOfDay += 1;
		
		if (currentHourOfDay == this.hoursInDay)
		{
			this.currentHourOfDay = 0;
		}
		
		Surplus surplus = new Surplus(windmillFarmOutput,simulatedHourLengthInSeconds);
		
		return surplus;
	}
	
	private void buildHoursOfDayArray()
	{
		for(int hour = 0; hour < 24; hour++)
		{
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, 00);
			DateFormat df = new SimpleDateFormat("hh:mm");
			String hourOfDay = df.format(cal.getTime());
			
			this.hoursOfDay[hour] = hourOfDay;
		}
		
	}
	
}
