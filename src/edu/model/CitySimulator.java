package edu.model;

import java.util.Timer;
import java.util.TimerTask;

import edu.model.batteries.Demand;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CitySimulator
{
	private int totalDemandsInDay = 2500;
	private int randomHour = (int) (Math.random() * 24);
	private double oneSecondInSimTime = .0027;
	private int hoursInDay = 24;
	
	private City desMoines = new City("Des Moines");
	private String[] hoursOfDay = new String[this.hoursInDay];
	
	
	public CitySimulator()
	{
		simulate();
		buildHoursOfDayArray();
		
		/*
		for (int i = 0; i < desMoines.getDailyDemand().size(); i++)
		{
			System.out.println("Needs " + desMoines.getDailyDemand().get(i) + " at the " +
					desMoines.getDailyDemandTimesOfDayInMilliseconds().get(i) + " millisecond time of day");
		}
		*/
	}
	
	public void simulate()
	{
		buildDemandArray();
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
	
	
	private void buildDemandArray()
	{
		
	/*	Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		
		int currentTimeInMilliseconds = (hour * 3600000) + (minutes * 60000);*/
		
		for (int i = 0; i < totalDemandsInDay; i++)
		{
			
			long randomMillisecondInDay = (long)(Math.random() * desMoines.millisecondsInDay);
			int randomMillisecondInDayToHour = (int) ((randomMillisecondInDay / 1000 / 60 / 60) % 24);
			
			
			desMoines.addDemand(new Demand((desMoines.calculateCityDemand(randomMillisecondInDayToHour) + oneSecondInSimTime), Math.random() * 10 ), 
					randomMillisecondInDay);
			
		}
		
		System.out.println("Done");
		
		// For next Time (below)
		
		//nextDemandTime = (first item in array)
		
		/* Add Timer to check milliseconds of day to see if that millisecond matches
		 * milliseconds in Array of Milliseconds for times of day.
		 * 
		 * If == send surplus to the energy commander -- similar to windmillfarm simulator
		 * 
		 * Then in windmillfarm simulator, parallel array of surpluses to windmillfarm 
		 * simulator. Then add same functionality.
		 * 
		 * Use different timer for this funtionality. Use same timer in gravitational
		 * battery class. - not interval.
		 * 
		 * Don't change methods, make another.
		 * 
		 * Might have to make changes to energy commander
		 * 
		 */
	}

	}
