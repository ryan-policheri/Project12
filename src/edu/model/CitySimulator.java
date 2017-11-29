package edu.model;

import java.util.Timer;
import java.util.TimerTask;

import edu.controllers.WriteToFile;
import edu.model.batteries.Demand;
import edu.model.batteries.Surplus;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class CitySimulator
{
	private int totalDemandsInDay = 100;
	private double oneSecondInSimTime = .0027;
	private int simulatedHourLengthInSeconds = 10;
	private int hoursInDay = 24;
	private long millisecondsInDay = (long) (8640000000L);
	private String fileName = "..\\Project12\\src\\edu\\model\\CityDemandDayLog";
	private File file = new File(fileName);
	private City desMoines = new City("Des Moines");
	private String[] hoursOfDay = new String[this.hoursInDay];
	private long currentMillisecond = 0;

	//parallel array
	private List<Demand> dailyDemand= new ArrayList<Demand>();
	private List<Double> dailyDemandTimesOfDayInMilliseconds = new ArrayList<Double>();

	public CitySimulator()
	{
		//simulate();
		buildHoursOfDayArray();	
	}

	public List<Demand> getDailyDemand()
	{
		return dailyDemand;
	}

	public List<Double> getDailyDemandTimesOfDayInMilliseconds()
	{
		return dailyDemandTimesOfDayInMilliseconds;
	}

	public void addDemand(Demand dailyDemand, double dailyDemandTimesOfDayInMilliseconds)
	{
		this.dailyDemand.add(dailyDemand);
		this.dailyDemandTimesOfDayInMilliseconds.add(dailyDemandTimesOfDayInMilliseconds);
	}

	public void removeDemand(Demand dailyDemand, double dailyDemandTimesOfDayInMilliseconds)
	{
		this.dailyDemand.remove(dailyDemand);
		this.dailyDemandTimesOfDayInMilliseconds.remove(dailyDemandTimesOfDayInMilliseconds);
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

		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);

		int currentTimeInMilliseconds = (hour * 3600000) + (minutes * 60000);


		for (int i = 0; i < totalDemandsInDay; i++)
		{

			long randomMillisecondInDay = (long)(Math.random() * millisecondsInDay);
			int randomMillisecondInDayToHour = (int) ((randomMillisecondInDay / 1000 / 60 / 60) % 24);

			addDemand(new Demand((desMoines.calculateCityDemand(randomMillisecondInDayToHour)
					+ oneSecondInSimTime), Math.random() * 10 ), 
					randomMillisecondInDay);

		}

		//Sort the parallel arrays created above
		doSelectionSort(dailyDemandTimesOfDayInMilliseconds, dailyDemand);

		//Write to CityDemandDayLog

		if (file.exists()) 
		{
			file.delete(); 
		}

		try 
		{
			file.createNewFile();
		} 

		catch (IOException e1) 
		{
			e1.printStackTrace();
		}

		WriteToFile data = new WriteToFile(fileName, true);

		for (int i = 0; i < getDailyDemand().size(); i++)
		{

			try		
			{
				data.writeToFile("Needs " + getDailyDemand().get(i) + " at the " +
						getDailyDemandTimesOfDayInMilliseconds().get(i) + " millisecond time of day");

				//For debugging purposes:
			//	System.out.println("Needs " + getDailyDemand().get(i) + " at the " +
			//			getDailyDemandTimesOfDayInMilliseconds().get(i) + " millisecond time of day");
			} 

			catch (IOException e) 
			{
				System.out.println("Sorry - No can do");
				break;
			}
		}

		System.out.println("Done");

		//Beginning of Timer Code

		long intervalInMilliseconds = (long) 1;

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run()
			{

				if (currentMillisecond == getDailyDemandTimesOfDayInMilliseconds().get(0))
				{
					System.out.println("Removing " + getDailyDemand().get(0) + " and " 
							+ getDailyDemandTimesOfDayInMilliseconds().get(0)); 

					sendDemandThroughEnergyCommander();

					System.out.println("Removed");
				}
			}
		}
		, 0, intervalInMilliseconds);
	}

	private void sendDemandThroughEnergyCommander()
	{
		EnergyCommander.commandEnergy(this.dailyDemand.get(0));
		removeDemand(this.dailyDemand.get(0), this.dailyDemandTimesOfDayInMilliseconds.get(0));	
	}

	//End Timer Code

	//Method to sort parallel arrays

	//TODO Change this to mergeSort.
	public static void doSelectionSort(List<Double> dailyDemandTimesOfDayInMilliseconds2,
			List<Demand> dailyDemand2) 
	{
		for (int i = 0; i < dailyDemandTimesOfDayInMilliseconds2.size(); i++) {
			// find position of smallest number between (i + 1)th element and last element

			int pos = i;
			for (int j = i; j < dailyDemandTimesOfDayInMilliseconds2.size(); j++) {
				if (dailyDemandTimesOfDayInMilliseconds2.get(j) < dailyDemandTimesOfDayInMilliseconds2.get(pos))
					pos = j;
			}

			// Swap smallest number to current position on array
			// Swap the position on the dailyDemand array as well
			Double min = dailyDemandTimesOfDayInMilliseconds2.get(pos);
			Demand equalMinPosition = dailyDemand2.get(pos);
			dailyDemandTimesOfDayInMilliseconds2.set(pos, dailyDemandTimesOfDayInMilliseconds2.get(i));
			dailyDemand2.set(pos, dailyDemand2.get(i));
			dailyDemandTimesOfDayInMilliseconds2.set(i, min);
			dailyDemand2.set(i, equalMinPosition);
		}
	}
}


