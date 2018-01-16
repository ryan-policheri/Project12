package edu.model.city;

import edu.controllers.WriteToFile;
import edu.model.EnergyCommander;
import edu.model.batteries.Demand;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CitySimulator
{
	// Working at 500
	private int totalDemandsInDay = 5000; //don't go below 24. should be same as windmill farm simulators
	private double oneSecondInSimTime = .0027;
	private int simulatedHourLengthInSeconds = 10;
	private int hoursInDay = 24;
	private long millisecondsInDay = (long) (86400000L);
	private long simulatedMillisecondsInDay = millisecondsInDay / 360; //* by scale
	private String fileName = "..\\Project12\\src\\edu\\model\\CityDemandDayLog";
	private File file = new File(fileName);
	private City city;
	private String[] hoursOfDay = new String[this.hoursInDay];
	private long currentMillisecond = 0;
	private Timer timer;

	//parallel array
	private List<Demand> dailyDemand = new ArrayList<Demand>();
	private List<Long> dailyDemandTimesOfDayInMilliseconds = new ArrayList<Long>();
	
	private ArrayList<Double> magnitudeByMillisecond = new ArrayList<Double>();

	public CitySimulator(City city)
	{
		this.city = city;

		this.buildHoursOfDayArray();
		this.buildDemandArray();
	}

	public List<Demand> getDailyDemand()
	{
		return dailyDemand;
	}

	public List<Long> getDailyDemandTimesOfDayInMilliseconds()
	{
		return dailyDemandTimesOfDayInMilliseconds;
	}

	public void addDemand(Demand dailyDemand, long dailyDemandTimesOfDayInMilliseconds)
	{
		this.dailyDemand.add(dailyDemand);
		this.dailyDemandTimesOfDayInMilliseconds.add(dailyDemandTimesOfDayInMilliseconds);
	}

	public void removeDemand(Demand dailyDemand, long dailyDemandTimesOfDayInMilliseconds)
	{
		this.dailyDemand.remove(dailyDemand);
		this.dailyDemandTimesOfDayInMilliseconds.remove(dailyDemandTimesOfDayInMilliseconds);
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
			long randomMillisecondInDay = (long)(Math.random() * simulatedMillisecondsInDay);
			int randomMillisecondInDayToHour = (int) ((randomMillisecondInDay / 10000));

			addDemand(new Demand((city.calculateCityDemand(randomMillisecondInDayToHour, (this.totalDemandsInDay / this.hoursInDay))),
					Math.random() * 10 ), randomMillisecondInDay);
		}

		//Sorts the parallel arrays created above
		doSelectionSort(dailyDemandTimesOfDayInMilliseconds, dailyDemand);

		//writeDemandsToCityDemandDayLog();
	}

	public void simulate()
	{
		//Beginning of Timer Code
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
				currentMillisecond ++;

				if (!dailyDemandTimesOfDayInMilliseconds.isEmpty())
				{
					//Out of bounds error when array is done.
					if (currentMillisecond == dailyDemandTimesOfDayInMilliseconds.get(0))
					{
						//System.out.println("Removing " + dailyDemand.get(0) + " at the "
						//		+ dailyDemandTimesOfDayInMilliseconds.get(0) + " millisecond of day");

						sendDemandThroughEnergyCommander();

						//System.out.println("Removed");

						if(currentMillisecond == dailyDemandTimesOfDayInMilliseconds.get(0))
						{
							currentMillisecond--;
							System.out.println("WE DID SOMETHING SPECIAL");
						}
					}
				}
				else
				{
					System.out.println("Done With City Demands");
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

	private void sendDemandThroughEnergyCommander()
	{
		EnergyCommander.commandEnergy(this.dailyDemand.get(0));
		removeDemand(this.dailyDemand.get(0), this.dailyDemandTimesOfDayInMilliseconds.get(0));
	}

	//End Timer Code
	
	public ArrayList<Double> constructMagnitudeByMillisecondArray()
	{
		int lastPositionInDemandArray = this.dailyDemandTimesOfDayInMilliseconds.size() - 1;
		
		//find the last possible active millisecond
		long lastDemandStartTimeInMilliseconds = this.dailyDemandTimesOfDayInMilliseconds.get(lastPositionInDemandArray);
		long lastPossibleRelevantMillisecond = lastDemandStartTimeInMilliseconds + (this.simulatedHourLengthInSeconds * 1000);
		
		long milliIterator = -1;
		long nextMilliWithNewDemand = 0;
		
		int spotInDemandArray = 0;
		
		double currentDemandPower = 0;
		double currentTimeNeeded = 0;
		
		double thisMillisecondMagnitude = 0;
		
		ArrayList<Demand> ongoingDemands = new ArrayList<Demand>();
		
		//find first active millisecond
		nextMilliWithNewDemand = this.dailyDemandTimesOfDayInMilliseconds.get(spotInDemandArray);
		
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
				currentDemandPower = this.dailyDemand.get(spotInDemandArray).getEnergyNeededInWatts();
				currentTimeNeeded = this.dailyDemand.get(spotInDemandArray).getTimeNeededInSeconds();
				thisMillisecondMagnitude += currentDemandPower;
				
				//then add this demand to the be part of future overlapping demands
				Demand tempDemand = new Demand(currentDemandPower, currentTimeNeeded);
				tempDemand.chopMillisecondOff();
				ongoingDemands.add(tempDemand);
				
				//go to next index in array if there is one
				if(spotInDemandArray < lastPositionInDemandArray)
				{
					spotInDemandArray += 1;
				}
				
				//set the next relevant milli
				if(nextMilliWithNewDemand == this.dailyDemandTimesOfDayInMilliseconds.get(spotInDemandArray) && spotInDemandArray < lastPositionInDemandArray)
				{
					milliIterator --;
				}
				nextMilliWithNewDemand = this.dailyDemandTimesOfDayInMilliseconds.get(spotInDemandArray);
				
			}
			
			this.magnitudeByMillisecond.add(thisMillisecondMagnitude);	
		}
		
		//writeMillisecondByMagnitudeToCityDemandDayLog();
		
		return this.magnitudeByMillisecond;
		
		//write (millisecond + 1), and magnitude

	}

	//Method to sort parallel arrays

	//TODO Change this to mergeSort.
	public static void doSelectionSort(List<Long> dailyDemandTimesOfDayInMilliseconds2,
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
			Long min = dailyDemandTimesOfDayInMilliseconds2.get(pos);
			Demand equalMinPosition = dailyDemand2.get(pos);
			dailyDemandTimesOfDayInMilliseconds2.set(pos, dailyDemandTimesOfDayInMilliseconds2.get(i));
			dailyDemand2.set(pos, dailyDemand2.get(i));
			dailyDemandTimesOfDayInMilliseconds2.set(i, min);
			dailyDemand2.set(i, equalMinPosition);
		}
	}
	
	private void writeDemandsToCityDemandDayLog()
	{
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

		System.out.println("Done printing demands");
	}

	private void writeMillisecondByMagnitudeToCityDemandDayLog()
	{
		//Write to CityDemandDayLog

		WriteToFile data = new WriteToFile(fileName, true);

		try {
			data.writeToFile("Milliseconds        ,            Magnitude");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int i = 0; i < this.magnitudeByMillisecond.size(); i++)
		{

			try
			{
				data.writeToFile((i + 1) + "                    ,                           " +
						magnitudeByMillisecond.get(i));

			}

			catch (IOException e)
			{
				System.out.println("Sorry - No can do");
				break;
			}
		}

		System.out.println("Done with magnitudes");
	}
}

