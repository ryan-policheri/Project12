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
<<<<<<< HEAD
	
=======
	private long millisecondsInDay = (long) (86400000L);
	private long simulatedMillisecondsInDay = millisecondsInDay / 360; //* by scale
	private String fileName = "..\\Project12\\src\\edu\\model\\CityDemandDayLog";
	private File file = new File(fileName);
>>>>>>> 00aa5f558b5dc9784ca4ad62cb14d8a5d5941ce9
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
<<<<<<< HEAD
			
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
=======
			long randomMillisecondInDay = (long)(Math.random() * simulatedMillisecondsInDay);
			int randomMillisecondInDayToHour = (int) ((randomMillisecondInDay / 10000));

			addDemand(new Demand((desMoines.calculateCityDemand(randomMillisecondInDayToHour)), 
					Math.random() * 10 ), 
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
				//System.out.println("Needs " + getDailyDemand().get(i) + " at the " +
					//	getDailyDemandTimesOfDayInMilliseconds().get(i) + " millisecond time of day and");
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
				currentMillisecond += 1;
				
				//Out of bounds error when array is done.
				if (currentMillisecond == getDailyDemandTimesOfDayInMilliseconds().get(0))
				{
					System.out.println("Removing " + getDailyDemand().get(0) + " at the " 
							+ getDailyDemandTimesOfDayInMilliseconds().get(0) + " millisecond of day");

					sendDemandThroughEnergyCommander();
					
					System.out.println("Removed");
				}
			}
		}
		, 0, intervalInMilliseconds);
>>>>>>> 00aa5f558b5dc9784ca4ad62cb14d8a5d5941ce9
	}

	}
<<<<<<< HEAD
=======

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

>>>>>>> 00aa5f558b5dc9784ca4ad62cb14d8a5d5941ce9
