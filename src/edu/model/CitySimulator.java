package edu.model;

import edu.model.City;
import edu.model.batteries.Demand;

import java.util.Timer;
import java.util.TimerTask;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CitySimulator
{
	private int hoursInDay = 24;
	private int randomHour = (int) (Math.random() * 24);
	
	private City desMoines = new City("Des Moines");
	
	
	//parallel array
	List<Demand> dailyDemand= new ArrayList<Demand>();
    List<Double> dailyDemandTimesOfDayInMilliseconds = new ArrayList<Double>();
	
	private String[] hoursOfDay = new String[this.hoursInDay];
	
	
	public CitySimulator()
	{
		simulate();
		buildHoursOfDayArray();
		
		/*for (int i = 0; i < desMoines.dailyDemand.size(); i++)
		{
			System.out.println("Needs " + desMoines.dailyDemand.get(i) + " at the " +
					desMoines.dailyDemandTimesOfDayInMilliseconds.get(i) + " millisecond time of day");
		}*/
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
		
		for (int i = 0; i < hoursInDay; i++)
		{
			/*
			desMoines.addDemand(new Demand((Math.random() * 1000000), desMoines.calculateCityDemand(randomHour)), (long)(Math.random() * desMoines.millisecondsInDay));
			*/
		}
	}

	}
