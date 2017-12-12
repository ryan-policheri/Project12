package edu.model.energySources.windmillFarm;

import edu.controllers.WriteToFile;
import edu.model.EnergyCommander;
import edu.model.batteries.Demand;
import edu.model.batteries.Surplus;

import java.util.Timer;
import java.util.TimerTask;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WindmillFarmSimulator
{
	private double simulatedHourLengthInSeconds = 10;
	private long millisecondsInDay = (long) (86400000L);
	private long simulatedMillisecondsInDay = millisecondsInDay / 360; //* by scale
	private int totalAmountOfSurplusesInDay = 24;
	private int hoursInDay = 24;
	private int currentHourOfDay = 0;
	private long currentMillisecond = 0;
	private static String fileName = "..\\Project12\\src\\edu\\model\\WindmillFarmSurplusLog";
	private static File file = new File(fileName);
	
	private String[] hoursOfDay = new String[this.hoursInDay];
	
	private WindmillFarm windmillFarm = new WindmillFarm();
	
	private List<Surplus> dailySurplus= new ArrayList<Surplus>();
	private List<Double> dailySurplusTimesOfDayInMilliseconds = new ArrayList<Double>();
	
	public void addSurplus(Surplus dailySurplus, double dailySurplusTimesOfDayInMilliseconds)
	{
		this.dailySurplus.add(dailySurplus);
		this.dailySurplusTimesOfDayInMilliseconds.add(dailySurplusTimesOfDayInMilliseconds);
	}

	public void removeSurplus(Surplus dailySurplus, double dailySurplusTimesOfDayInMilliseconds)
	{
		this.dailySurplus.remove(dailySurplus);
		this.dailySurplusTimesOfDayInMilliseconds.remove(dailySurplusTimesOfDayInMilliseconds);
	}
	
	public WindmillFarmSimulator()
	{
		buildHoursOfDayArray();
	}
	
	//CHANGE TO oldSimulate
	/*
	public void oldSimulate()
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
	*/
	
	public void simulate()
	{
		for (int i = 0; i < totalAmountOfSurplusesInDay; i++)
		{
			long randomMillisecondInDay = (long)(Math.random() * simulatedMillisecondsInDay);
			int randomMillisecondInDayToHour = (int) ((randomMillisecondInDay / 10000));

			addSurplus(new Surplus(windmillFarm.calculateWindmillFarmOutput(randomMillisecondInDayToHour), 
					Math.random() * 10 ), randomMillisecondInDay);
		}		
		doSelectionSort(dailySurplusTimesOfDayInMilliseconds, dailySurplus);
		writeToLog();
		
	/*	for (int i = 0; i < dailySurplus.size(); i++)
		{
			//For debugging purposes:
			System.out.println(dailySurplus.get(i) + " at the " +
					dailySurplusTimesOfDayInMilliseconds.get(i) + " millisecond time of day and");
		}
		*/
		
		//Timer Code
		long intervalInMilliseconds = (long) 1;
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run()
			{
				currentMillisecond++;

				if (!dailySurplusTimesOfDayInMilliseconds.isEmpty())
				{
					if (currentMillisecond == dailySurplusTimesOfDayInMilliseconds.get(0))
					{
						System.out.println("Adding " + dailySurplus.get(0) + " at the " 
								+ dailySurplusTimesOfDayInMilliseconds.get(0) + " millisecond of day");

						sendSurplusThroughEnergyCommander();

						System.out.println("Added");
					}
				}
				else
				{
					//System.out.println("Done");
					timer.cancel();
				}
			}
		}
		, 0, intervalInMilliseconds);
	}
	
	
	
private void sendSurplusThroughEnergyCommander()
{
	EnergyCommander.commandEnergy(dailySurplus.get(0));
	removeSurplus(dailySurplus.get(0), dailySurplusTimesOfDayInMilliseconds.get(0));	
}
	/*
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
	*/
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
	
	public static void doSelectionSort(List<Double> dailySurplusTimesOfDayInMilliseconds,
			List<Surplus> dailySurplus) 
	{
		for (int i = 0; i < dailySurplusTimesOfDayInMilliseconds.size(); i++) 
		{
			// find position of smallest number between (i + 1)th element and last element

			int pos = i;
			for (int j = i; j < dailySurplusTimesOfDayInMilliseconds.size(); j++) 
			{
				if (dailySurplusTimesOfDayInMilliseconds.get(j) < dailySurplusTimesOfDayInMilliseconds.get(pos))
					pos = j;
			}

			// Swap smallest number to current position on array
			// Swap the position on the dailyDemand array as well
			Double min = dailySurplusTimesOfDayInMilliseconds.get(pos);
			Surplus equalMinPosition = dailySurplus.get(pos);
			dailySurplusTimesOfDayInMilliseconds.set(pos, dailySurplusTimesOfDayInMilliseconds.get(i));
			dailySurplus.set(pos, dailySurplus.get(i));
			dailySurplusTimesOfDayInMilliseconds.set(i, min);
			dailySurplus.set(i, equalMinPosition);
		}
		

	}
	
	private void writeToLog()
	{
		System.out.println("HELLO PRINTING SURPLUSES");

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

			for (int i = 0; i < dailySurplus.size(); i++)
			{

				try		
				{
					data.writeToFile("Gives " + dailySurplus.get(i) + " at the " +
							dailySurplusTimesOfDayInMilliseconds.get(i) + " millisecond time of day");
				} 

				catch (IOException e) 
				{
					System.out.println("Sorry - No can do");
					break;
				}
			}

			System.out.println("Done with surpluses");
		}
		
	
}
