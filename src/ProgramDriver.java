import java.util.concurrent.TimeUnit;

public class ProgramDriver
{

	public static void main(String[] args) throws InterruptedException
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
		
		WindmillFarmSimulator simulator = new WindmillFarmSimulator();
		simulator.simulate()
		*/
		
/*		BatteryGrid batteryGrid = new BatteryGrid();
		
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_1",10000,40));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_2",5000,50));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_3",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_4",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_5",2500,20));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_6",10000,40));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_7",5000,50));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_8",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_9",7500,30));
		batteryGrid.addGravitationalBattery(new GravitationalBattery("GB_10",2500,20));*/
		
		//batteryGrid.displayGrid();
		//batteryGrid.allocateEnergySurplus(new Surplus(100,20));
		//batteryGrid.displayGrid();
		//TimeUnit.SECONDS.sleep(5);
		//batteryGrid.allocateEnergySurplus(new Surplus(100,10));
		//batteryGrid.displayGrid();
		//TimeUnit.SECONDS.sleep(10);
		//batteryGrid.allocateEnergyDemand(new Demand(100,5));
/*		
		batteryGrid.displayGrid();
		
		WindmillFarmSimulator windmillFarmSimulator = new WindmillFarmSimulator();
		
		EnergyCommander energyCommander = new EnergyCommander(batteryGrid);
		
		windmillFarmSimulator.simulate();*/
		
/*		batteryGrid.allocateEnergySurplus(new Surplus(239024.48901238025,10));
		batteryGrid.displayGrid();
		TimeUnit.SECONDS.sleep(11);
		batteryGrid.allocateEnergySurplus(new Surplus(182135.43543721945,10));
		batteryGrid.displayGrid();
		TimeUnit.SECONDS.sleep(11);
		batteryGrid.allocateEnergySurplus(new Surplus(380508.38637304987,10));
		batteryGrid.displayGrid();
		TimeUnit.SECONDS.sleep(11);
		batteryGrid.allocateEnergySurplus(new Surplus(274450.70316027646,10));
		batteryGrid.displayGrid();
		TimeUnit.SECONDS.sleep(11);*/
		
		double densityOfCarbonFiberInKilogramsMetersCubed = 1799;
		double tensileStressOfCarbonFiberInPascals = 4000000000.0;
		FlywheelMaterial carbonFiber = new FlywheelMaterial("Carbon Fiber", densityOfCarbonFiberInKilogramsMetersCubed, tensileStressOfCarbonFiberInPascals);
		
		double percentFrictionalLossPerSecondForStandardMecahnicalBearing = 0.000034722222222222222222; //number derived from 25 percent / 7200
		FlywheelBearing mechanicalBearing = new FlywheelBearing("Mechanical Bearing", percentFrictionalLossPerSecondForStandardMecahnicalBearing);
		
		RotationalBattery rotBat = new RotationalBattery("RB_1", 450, 10.72, carbonFiber, mechanicalBearing);
		rotBat.displayBattery();
		rotBat.storeEnergy(new Surplus(50013574,10));
		rotBat.displayBattery();
		TimeUnit.SECONDS.sleep(11);
		//rotBat.releaseEnergy(new Demand(37445006,10));
		rotBat.displayBattery();
		TimeUnit.SECONDS.sleep(11);
		//rotBat.releaseEnergy(new Demand(29744500,10));
		rotBat.displayBattery();

		
	}

}
 