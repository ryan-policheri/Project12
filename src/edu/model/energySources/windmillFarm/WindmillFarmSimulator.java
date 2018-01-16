package edu.model.energySources.windmillFarm;

import edu.controllers.Controller;
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
	// Working at 500
	private int totalAmountOfSurplusesInDay = 500; //don't go below 24. should be same as city simulators
	private double simulatedHourLengthInSeconds = 10;
	private long millisecondsInDay = (long) (86400000L);
	private long simulatedMillisecondsInDay = millisecondsInDay / 360; //* by scale
	private int hoursInDay = 24;
	private int currentHourOfDay = 0;
	private long currentMillisecond = 0;
	private static String fileName = "..\\Project12\\src\\edu\\model\\WindmillFarmSurplusLog";
	private static File file = new File(fileName);
	private Timer timer;
	
	private String[] hoursOfDay = new String[this.hoursInDay];
	
	private WindmillFarm windmillFarm;
	
	private List<Surplus> dailySurplus = new ArrayList<Surplus>();
	private List<Long> dailySurplusTimesOfDayInMilliseconds = new ArrayList<Long>();
	
	private ArrayList<Double> magnitudeByMillisecond = new ArrayList<Double>();
	
	public WindmillFarmSimulator(WindmillFarm windmillFarm)
	{
		this.windmillFarm = windmillFarm;
		
		this.buildHoursOfDayArray();
		this.buildSurplusArray();
	}
	
	public void addSurplus(Surplus dailySurplus, long dailySurplusTimesOfDayInMilliseconds)
	{
		this.dailySurplus.add(dailySurplus);
		this.dailySurplusTimesOfDayInMilliseconds.add(dailySurplusTimesOfDayInMilliseconds);
	}

	public void removeSurplus(Surplus dailySurplus, long dailySurplusTimesOfDayInMilliseconds)
	{
		this.dailySurplus.remove(dailySurplus);
		this.dailySurplusTimesOfDayInMilliseconds.remove(dailySurplusTimesOfDayInMilliseconds);
	}
	
	//region Old oldSimulate function
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
	//endregion

	private void buildSurplusArray()
	{
		for (int i = 0; i < this.totalAmountOfSurplusesInDay; i++)
		{
			long randomMillisecondInDay = (long)(Math.random() * simulatedMillisecondsInDay);
			int randomMillisecondInDayToHour = (int) ((randomMillisecondInDay / 10000));

			addSurplus(new Surplus(windmillFarm.calculateWindmillFarmOutput(randomMillisecondInDayToHour, (this.totalAmountOfSurplusesInDay / this.hoursInDay)), 
					Math.random() * 10 ), randomMillisecondInDay);
		}		
		doSelectionSort(dailySurplusTimesOfDayInMilliseconds, dailySurplus);
		
		//writeToLog();
	}

	public void simulate()
	{		
		//Timer Code
		long intervalInMilliseconds = (long) 1;
		timer = new Timer();
		startTimer(intervalInMilliseconds);
	}

	private void startTimer(long intervalInMilliseconds)
	{
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run()
			{
				currentMillisecond++;
				// Update the controller info every 200 milliseconds
				int updateRate = 50;
				if (currentMillisecond % updateRate == 0)
				{
					Controller.updateTimeInformation();
				}

				if (!dailySurplusTimesOfDayInMilliseconds.isEmpty())
				{
					if (currentMillisecond == dailySurplusTimesOfDayInMilliseconds.get(0))
					{
						//System.out.println("Adding " + dailySurplus.get(0) + " at the "
						//		+ dailySurplusTimesOfDayInMilliseconds.get(0) + " millisecond of day");

						sendSurplusThroughEnergyCommander();

						//System.out.println("Added");

						if(currentMillisecond == dailySurplusTimesOfDayInMilliseconds.get(0))
						{
							currentMillisecond--;
							System.out.println("WE DID SOMETHING SPECIAL AGAIN");
						}
					}
				}
				else
				{
					System.out.println("Done With Windmill Farm Surpluses");
					timer.cancel();
				}
			}
		}
		, 0, intervalInMilliseconds);
	}

	public void interruptTimer()
	{
		timer.cancel();
	}
	
	private void sendSurplusThroughEnergyCommander()
	{
		EnergyCommander.commandEnergy(dailySurplus.get(0));
		removeSurplus(dailySurplus.get(0), dailySurplusTimesOfDayInMilliseconds.get(0));
	}

	public ArrayList<Double> constructMagnitudeByMillisecondArray()
	{
		int lastPositionInSurplusArray = this.dailySurplusTimesOfDayInMilliseconds.size() - 1;

		//find the last possible active millisecond
		long lastSurplusStartTimeInMilliseconds = this.dailySurplusTimesOfDayInMilliseconds.get(lastPositionInSurplusArray);
		long lastPossibleRelevantMillisecond = (long) (lastSurplusStartTimeInMilliseconds + (this.simulatedHourLengthInSeconds * 1000));

		long milliIterator = -1;
		long nextMilliWithNewDemand = 0;

		int spotInDemandArray = 0;

		double currentDemandPower = 0;
		double currentTimeNeeded = 0;

		double thisMillisecondMagnitude = 0;

		ArrayList<Demand> ongoingDemands = new ArrayList<Demand>();

		//find first active millisecond
		nextMilliWithNewDemand = this.dailySurplusTimesOfDayInMilliseconds.get(spotInDemandArray);

		while (milliIterator <= lastPossibleRelevantMillisecond)
		{
			milliIterator += 1;

			thisMillisecondMagnitude = 0;

			currentDemandPower = 0;
			currentTimeNeeded = 0;

			//add previous energy needs that may overlap
			//go backwards so that can delete while in loop
			for(int i = ongoingDemands.size() - 1; i >=0; i--)
			{
				thisMillisecondMagnitude += ongoingDemands.get(i).getEnergyNeededInWatts();
				//chop a millisecond off
				ongoingDemands.get(i).chopMillisecondOff();
				//remove empty ones
				if((long) (ongoingDemands.get(i).getTimeNeededInSeconds() * 1000) < 1)
				{
					ongoingDemands.remove(ongoingDemands.get(i));
				}
			}

			//if this milliseconds is a millisecond with a new demand
			if(milliIterator == nextMilliWithNewDemand)
			{
				//energy need specifically at this millisecond
				currentDemandPower = this.dailySurplus.get(spotInDemandArray).getEnergyAvailableInWatts();
				currentTimeNeeded = this.dailySurplus.get(spotInDemandArray).getTimeAvailableInSeconds();
				thisMillisecondMagnitude += currentDemandPower;

				//then add this demand to the be part of future overlapping demands
				Demand tempDemand = new Demand(currentDemandPower, currentTimeNeeded);
				tempDemand.chopMillisecondOff();
				ongoingDemands.add(tempDemand);

				//go to next index in array if there is one
				if(spotInDemandArray < lastPositionInSurplusArray)
				{
					spotInDemandArray += 1;
				}

				//set the next relevant milli
				if(nextMilliWithNewDemand == this.dailySurplusTimesOfDayInMilliseconds.get(spotInDemandArray) && spotInDemandArray < lastPositionInSurplusArray)
				{
					milliIterator --;
				}
				nextMilliWithNewDemand = this.dailySurplusTimesOfDayInMilliseconds.get(spotInDemandArray);
			}

			this.magnitudeByMillisecond.add(thisMillisecondMagnitude);
		}

		//writeMillisecondByMagnitudeToCityDemandDayLog();

		return this.magnitudeByMillisecond;

		//write (millisecond + 1), and magnitude

	}

	//region Old generatePowerFromWindmillFarm function
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
	//endregion

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
	
	public static void doSelectionSort(List<Long> dailySurplusTimesOfDayInMilliseconds,
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
			Long min = dailySurplusTimesOfDayInMilliseconds.get(pos);
			Surplus equalMinPosition = dailySurplus.get(pos);
			dailySurplusTimesOfDayInMilliseconds.set(pos, dailySurplusTimesOfDayInMilliseconds.get(i));
			dailySurplus.set(pos, dailySurplus.get(i));
			dailySurplusTimesOfDayInMilliseconds.set(i, min);
			dailySurplus.set(i, equalMinPosition);
		}
		

	}

	public long getCurrentMillisecond()
	{
		return currentMillisecond;
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
