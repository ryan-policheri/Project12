
public class ProgramDriver
{

	public static void main(String[] args)
	{
		
/*		GravitationalBattery testBattery = new GravitationalBattery("GB_1",10000,40);
		System.out.println(testBattery.currentHeightInMeters);
		System.out.println(testBattery.currentPotentialEnergyInJoules);
		
		System.out.println(testBattery.storeEnergy(1000,4362));
		System.out.println(testBattery.currentHeightInMeters);
		System.out.println(testBattery.currentPotentialEnergyInJoules);
		
		System.out.println(testBattery.isBatteryFull());
		
		System.out.println(testBattery.releaseEnergy(1000, 2000));
		System.out.println(testBattery.currentHeightInMeters);
		System.out.println(testBattery.currentPotentialEnergyInJoules);
		
		testBattery.displayBattery();
 		*/
		
/*		BatteryGrid batteryGrid = new BatteryGrid();
		
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_1",10000,40));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_2",5000,50));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_3",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_4",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_5",2500,20));
		
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergySurplus(5000, 2000);
		batteryGrid.displayGrid();
		
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergySurplus(1500, 360);
		batteryGrid.displayGrid();
		
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergyDemand(2250, 1000);
		batteryGrid.displayGrid();
		
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergySurplus(900, 7500);
		batteryGrid.displayGrid();
		
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergyDemand(2250, 1000);
		batteryGrid.displayGrid();
		*/
		
/*		City testCity = new City("Des Moines");
		testCity.powerNeed(1500000, 3600);
		testCity.cityPopulation = 215472;
		testCity.statePopulation = 3134693;
		testCity.estimatedLandAvailableInAcres = 2000;
		
		// in MwH
		testCity.cityEnergyProducedYearly = 15503646.4985;
		testCity.cityRenewableEnergyProducedYearly = 3970593.45744;
		testCity.cityEnergyConsumedYearly = 30136087.4678;
		
		System.out.println(testCity.powerNeed(1500000, 3600));
		
		BatteryGrid batteryGrid = new BatteryGrid();
		
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_1",10000,40));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_2",5000,50));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_3",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_4",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_5",2500,20));
		
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergySurplus(100,10);
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergySurplus(100,10);
		batteryGrid.displayGrid();
		batteryGrid.allocateEnergyDemand(100,5);
		batteryGrid.displayGrid();
		*/
		
/*		WindmillFarmPerformanceType performance = new WindmillFarmPerformanceType();
		performance.setToLowPerformance();
		
		WindmillFarm farm = new WindmillFarm();
		System.out.println(farm.calculateWindmillFarmOutput(performance));
		*/
		
		WindmillFarmSimulator simulator = new WindmillFarmSimulator();
		simulator.simulateWindFarm();
	}

}
 