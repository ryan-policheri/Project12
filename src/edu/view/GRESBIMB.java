package edu.view;

//region Imports

import edu.controllers.Controller;
import edu.model.EnergyCommander;
import edu.model.batteries.*;
import edu.model.city.City;
import edu.model.city.CitySimulator;
import edu.model.energySources.windmillFarm.WindmillFarm;
import edu.model.energySources.windmillFarm.WindmillFarmSimulator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
//endregion

public class GRESBIMB
{

	//Elements are sorted by:
	//	1) Order in which they appear in-program
	//	2) Grouped by their container panels (top down, left to right)
	//	3) Sorted in the order they appear in those panels (top down)

	//region Initialize swing elements
	private JPanel panelMain;

	//region Welcome panel
	private JPanel panelWelcome;
	private JButton btnStartNewSimulation;
	private JButton btnExit;
	//endregion

	//region Pick City panel
	private JPanel panelPickCity;

	private JList listCities;
	private JLabel lblPickCitySelectedCityName;
	private JButton btnPickCityBack;
	private JButton btnPickCityNext;
	//endregion

	//region Energy panel
	private JPanel panelEnergy;

	private JButton btnEnergyProductionEdit;
	private JButton btnEnergyConsumptionEdit;
	private JButton btnEnergyBack;
	private JButton btnEnergyNext;
	private JPanel panelEnergyProductionChart;
	private JPanel panelEnergyConsumptionChart;
	//endregion

	//region Batteries panel
	private JPanel panelBatteries;

	private JList listBatteries;
	private JButton btnBatteriesAdd;
	private JButton btnBatteriesRemove;
	private JButton btnBatteriesRemoveAll;
	private JLabel lblBatteriesSelectedBatteryName;
	private JButton btnBatteriesBack;
	private JButton btnBatteriesSimulate;
	//endregion

	//region Simulation panel
	private JPanel panelSimulation;
	private JPanel panelSimulationDemandChart;
	private JPanel panelSimulationSurplusChart;
	private JPanel panelSimulationTierCharts;
	private JPanel panelSimulationDemandTiers;
	private JPanel panelSimulationSurplusTiers;
	private JPanel panelSimulationEnergy;
	private JProgressBar pBSimulation;
	private JLabel lblEnergyLevel;
	private JPanel panelSimulationBatteryLevel;
	private JProgressBar pbSimulationGridEnergyLevel;
	private JLabel lblMaxEnergy;
	private JLabel lblCountReachedMaxCapacity;
	private JLabel lblCountPowerOutage;
	private JButton btnSimulationExit;
	private JButton btnSimulationStop;
	private JPanel panelCitiesPreview;
	private JLabel lblEnergyTitle;
	private JLabel lblBatteryType;
	private JLabel lblBatteryMaxStorage;
	private JLabel lblBatteriesTitle;
	//endregion
	//endregion

	//region Initialize attributes
	private Controller controller;
	private static DefaultListModel<Battery> batteryDefaultListModel = new DefaultListModel<>();
	private static DefaultListModel<City> cityDefaultListModel = new DefaultListModel<>();
	private static City selectedCity;

	// JFreeChart stuff
	private static final int NUM_OF_TIERS = Controller.getNumOfTiers();
	private static final int MAJOR_TICK_SPACING = Controller.getMajorTickSpacing();
	private static final int SIMULATION_CHART_WIDTH_IN_POINTS = 2000;
	private static double maximumMagnitudeOfDemandsByMillisecond;
	private static double maximumMagnitudeOfSurplusesByMillisecond;
	private static double maximumMagnitudeByMillisecond;
	private static final double MAX_Y_AXIS_CHART_MULTIPLIER = 1.3;
	private static final double Y_AXIS_TICKS = 5.1;

	// Data for the JFreeChart
	private static ArrayList<Double> magnitudeOfDemandsByMillisecond;
	private static ArrayList<Double> magnitudeOfSurplusesByMillisecond;
	private static WindmillFarmSimulator windmillFarmSimulator;
	private static CitySimulator citySimulator;
	private String returnStringHour;
	private String returnStringMinute;

	// For the simulation batteries
	private static double maxTotalEnergyInJoules;
	private static double currentGridEnergyInJoules;

	// For tier charts
	private static double[] energyProductionTiersDouble;
	private static double[] energyConsumptionTiersDouble;

	// For counting simulation
	private static int countReachedMaxCapacity = 0;
	private static int countPowerOutage = 0;
	//endregion

	//region Methods
	public GRESBIMB()
	{
		controller = new Controller(this);

		//TODO: Add cities to the list, be able to select them and change their info on the fly
		//TODO: Add a final "Simulate" feature

		//TODO: MAJOR PRIORITY - see below
		// Get current millisecond from WindmillFarmSimulator as the xAxisStartRange
		// Get a period of time later as the xAxisEndRange
		// Set xAxisTickUnit to a reasonable amount
		// Set yAxisRange to the maximum value of the entire "demands" array
		// Set yAxisTickUnit to roughly 20 ticks
		// Update the graph every millisecond

		Controller.updateCities();

		//region Add Batteries
		// Adding 1000 batteries total with random names
		/*for (int i = 500; i > 0; i--)
		{
			int a = (int) Math.round(Math.random() * 1000);

			GravitationalBattery gravitationalBattery = new GravitationalBattery("GRAV_" + a, 100, 100);
			Controller.addGravitationalBattery(gravitationalBattery);

			RotationalBattery rotationalBattery = new RotationalBattery("ROT_" + a, 100, 100,
					new FlywheelMaterial("Titanium"), new FlywheelBearing("Mechanical"));
			Controller.addRotationalBattery(rotationalBattery);
		}*/

		//ADD THE BATTERIES
		double GB_Supreme_Count = 50;
		double GB_PowerHouse_Count = 200;
		double GB_Classic_Count = 125;
		double GB_Lite_Count = 75;

		double RB_MegaSonic_Count = 50;
		double RB_SuperSonic_Count = 100;
		double RB_BigSexy_Count = 175;
		double RB_LittleTitan_Count = 100;
		double RB_Classic_Count = 125;

		//titanium
		FlywheelMaterial titanium = new FlywheelMaterial("Titanium");

		//aluminum
		FlywheelMaterial aluminum = new FlywheelMaterial("Aluminum");

		//carbon fiber
		double densityOfCarbonFiberInKilogramsMetersCubed = 1799;
		double tensileStressOfCarbonFiberInPascals = 4000000000.0;
		FlywheelMaterial carbonFiber = new FlywheelMaterial("Carbon Fiber", densityOfCarbonFiberInKilogramsMetersCubed, tensileStressOfCarbonFiberInPascals);

		//steel
		double densityOfSteelInKilogramsMetersCubed = 8050;
		double tensileStressOfSteelInPascals = 690000000.0;
		FlywheelMaterial steel = new FlywheelMaterial("Steel", densityOfSteelInKilogramsMetersCubed, tensileStressOfSteelInPascals);

		//mechanical bearing
		double percentFrictionalLossPerSecondForStandardMecahnicalBearing = 0.0125; //number derived from (25 percent / 7200) * 360 (multiplying by 360 converts number to simulation time)
		FlywheelBearing mechanicalBearing = new FlywheelBearing("Mechanical Bearing", percentFrictionalLossPerSecondForStandardMecahnicalBearing);

		//Make using 2nd constructor

		//magnetic bearing
		FlywheelBearing magneticBearing = new FlywheelBearing("Magnetic");

		//super conductor bearing
		FlywheelBearing modernBearing = new FlywheelBearing("Modern");

		//Gravitational:

		//GB_Supremes:
		for (int i = 1; i <= GB_Supreme_Count; i++)
		{
			Controller.addGravitationalBattery(new GravitationalBattery("GB_Supreme_" + Integer.toString(i),40000,150));
		}

		//GB_PowerHouses:
		for (int i = 1; i <= GB_PowerHouse_Count; i++)
		{
			Controller.addGravitationalBattery((new GravitationalBattery("GB_PowerHouse_" + Integer.toString(i),
					20000,120)));
		}

		//GB_Classics:
		for (int i = 1; i <= GB_Classic_Count; i++)
		{
			Controller.addGravitationalBattery((new GravitationalBattery("GB_Classic_" + Integer.toString(i),30000,
					50)));
		}

		//GB_Lites:
		for (int i = 1; i <= GB_Lite_Count; i++)
		{
			Controller.addGravitationalBattery(new GravitationalBattery("GB_Lite_" + Integer.toString(i),40000,150));
		}

		//Rotational:

		//RB_MegaSonics:
		for (int i = 1; i <= RB_MegaSonic_Count; i++)
		{
			Controller.addRotationalBattery(new RotationalBattery("RB_MegaSonic_" + Integer.toString(i),100,0.5,carbonFiber,modernBearing));
		}

		//RB_SuperSonics:
		for (int i = 1; i <= RB_SuperSonic_Count; i++)
		{
			Controller.addRotationalBattery(new RotationalBattery("RB_SuperSonic_" + Integer.toString(i),75,0.5,carbonFiber,magneticBearing));
		}

		//RB_BigSexys:
		for (int i = 1; i <= RB_BigSexy_Count; i++)
		{
			Controller.addRotationalBattery(new RotationalBattery("RB_BigSexy_" + Integer.toString(i),500,1,steel,mechanicalBearing));
		}

		//RB_LittleTitans:
		for (int i = 1; i <= RB_LittleTitan_Count; i++)
		{
			Controller.addRotationalBattery(new RotationalBattery("RB_LittleTitan_" + Integer.toString(i),250,0.25,titanium,magneticBearing));
		}

		//RB_Classics:
		for (int i = 1; i <= RB_Classic_Count; i++)
		{
			Controller.addRotationalBattery(new RotationalBattery("RB_Classic_" + Integer.toString(i),100,0.5,aluminum,mechanicalBearing));
		}
		//endregion

		// Set the battery list batteryDefaultListModel
		listBatteries.setModel(batteryDefaultListModel);

		// Set the cities list batteryDefaultListModel
		listCities.setModel(cityDefaultListModel);

		//region Set the selected city
		Controller.setSelectedCity(Controller.getDesMoines());
		selectedCity = Controller.getSelectedCity();
		System.out.println("Selected City: " + Controller.getSelectedCity().toString());
		//endregion

		Surplus surplus = new Surplus(2000000000, 1);

		// Controller.allocateEnergySurplus(surplus);

		//region Button listeners
		//region Switching between panels
		btnPickCityBack.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				switchToPanel(panelWelcome);
			}
		});
		btnPickCityNext.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (listCities.getSelectedIndex() != -1)
				{
					switchToPanel(panelEnergy);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Please select a city.");
				}
			}
		});
		btnEnergyBack.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				switchToPanel(panelPickCity);
			}
		});
		btnEnergyNext.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				switchToPanel(panelBatteries);
			}
		});
		btnBatteriesBack.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				switchToPanel(panelEnergy);
			}
		});
		btnBatteriesSimulate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				switchToPanel(panelSimulation);
			}
		});
		//endregion

		//region Cities
		//endregion

		//region Welcome screen buttons
		btnStartNewSimulation.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				switchToPanel(panelPickCity);
			}
		});
		//endregion

		//region Energy screen buttons
		btnEnergyProductionEdit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String title = "Edit Energy Production";
				createNewJFrame(new FormEditEnergyProduction().getPanelMain(), title, JFrame.DISPOSE_ON_CLOSE);
			}
		});
		btnEnergyConsumptionEdit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String title = "Edit Energy Consumption";
				createNewJFrame(new FormEditEnergyConsumption().getPanelMain(), title, JFrame.DISPOSE_ON_CLOSE);
			}
		});
		//endregion

		//region Batteries screen buttons
		btnBatteriesAdd.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String title = "Add Battery";
				createNewJFrame(new FormAddBattery().getPanelMain(), title, JFrame.DISPOSE_ON_CLOSE);
			}
		});
		btnBatteriesRemove.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int selectedBatteryIndex = listBatteries.getSelectedIndex();

				if (selectedBatteryIndex != -1)
				{
					Controller.removeBattery(selectedBatteryIndex);
				}

				listBatteries.setSelectedIndex(selectedBatteryIndex);
			}
		});
		btnBatteriesRemoveAll.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Controller.removeAllBatteries();
				update();
			}
		});
		//endregion

		//region Simulate screen buttons
		btnSimulationStop.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				citySimulator.interruptTimer();
				windmillFarmSimulator.interruptTimer();
			}
		});
		btnSimulationExit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				restart();
			}
		});
		//endregion
		//endregion

		listCities.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				panelCitiesPreview.removeAll();
				int[] energyProductionTiers = {2, 2, 2, 3, 3, 4, 3, 3, 5, 4, 5, 5, 5, 5, 4, 3, 4, 4, 3, 5, 2, 2, 3, 3};
				int[] energyConsumptionTiers = selectedCity.getEnergyConsumptionTiers();
				double[] chicagoTiersPreview = new double[energyProductionTiers.length];
				double[] desMoinesTiersPreview = new double[energyConsumptionTiers.length];

				for (int i = 0; i < energyConsumptionTiers.length; i++)
				{
					chicagoTiersPreview[i] = energyProductionTiers[i];
					desMoinesTiersPreview[i] = energyConsumptionTiers[i];
				}

				// This is extremely hardcoded for Chicago on top, Des Moines on bottom
				if (listCities.getSelectedIndex() == 0)
				{
					lblPickCitySelectedCityName.setText("Selected city: Chicago");
					addJFreeChartToJPanel("Energy Consumption of Chicago", panelCitiesPreview,
							chicagoTiersPreview, "Hour", "Tier", true, 0,
							23.5, 1, (double) (NUM_OF_TIERS + .5), MAJOR_TICK_SPACING);
				}
				else if (listCities.getSelectedIndex() == 1)
				{
					lblPickCitySelectedCityName.setText("Selected city: Des Moines");
					addJFreeChartToJPanel("Energy Consumption of Des Moines", panelCitiesPreview,
							desMoinesTiersPreview, "Hour", "Tier", true, 0,
							23.5, 1, (double) (NUM_OF_TIERS + .5), MAJOR_TICK_SPACING);
				}
				else
				{
					lblPickCitySelectedCityName.setText("Please select a city.");
				}
			}
		});
		listBatteries.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				int selectedIndex = listBatteries.getSelectedIndex();
				if (selectedIndex != -1)
				{
					lblBatteriesSelectedBatteryName.setText(batteryDefaultListModel.get(selectedIndex).getBatteryName());
					lblBatteryType.setText(batteryDefaultListModel.get(selectedIndex).getBatteryType());
					lblBatteryMaxStorage.setText(batteryDefaultListModel.get(selectedIndex).getMaxEnergyInJoules() + "");
				}
			}
		});
	}

	private void addJFreeChartToJPanel(String title, JPanel panel, double[] data, String xAxisLabel, String yAxisLabel,
									   boolean hasTicks, double xAxisStartRange, double xAxisEndRange,
									   double xAxisTickUnit, double yAxisRange, double yAxisTickUnit)
	{
		XYSeries series = new XYSeries("XYGraph");

		// Add data to the series
		for (int i = 0; i < data.length; i++)
		{
			series.add(i, data[i]);
		}

		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);

		// Generate the graph
		JFreeChart chart = ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, dataset,
				PlotOrientation.VERTICAL, false, false, false);

		// Make data points visible
		XYPlot chartPlot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chartPlot.getRenderer();
		renderer.setBaseShapesVisible(true);

		// Set the range
		XYPlot xyPlot = (XYPlot) chart.getPlot();
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		// Domain
		NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
		domain.setRange(xAxisStartRange, xAxisEndRange);
		domain.setTickUnit(new NumberTickUnit(xAxisTickUnit));
		domain.setTickLabelsVisible(hasTicks);
		// Range
		NumberAxis range = (NumberAxis) chartPlot.getRangeAxis();
		range.setRange(0, yAxisRange);
		range.setTickUnit(new NumberTickUnit(yAxisTickUnit));

		// Add it the graph to the JPanel
		ChartPanel CP = new ChartPanel(chart);
		panel.add(CP, BorderLayout.CENTER);
		panel.validate();
	}

	private void createNewWindmillFarmSimulator()
	{
		// Ignore this
		WindmillFarmSimulator windmillFarmSimulator = new WindmillFarmSimulator(new WindmillFarm("Default"));
		EnergyCommander energyCommander = new EnergyCommander(Controller.getGrid());
		windmillFarmSimulator.simulate();
	}

	//region GUI functions
	private static void createNewJFrame(JPanel panel, String title, int operation)
	{
		JFrame frame = new JFrame(title);
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(operation);
		frame.pack();
		frame.setVisible(true);
	}

	// Add surplus functionality
	private void switchToPanel(JPanel panel)
	{
		this.panelMain.removeAll();
		this.panelMain.add(panel);
		this.panelMain.repaint();
		this.panelMain.revalidate();

		// If it's the Energy panel, insert the charts into the appropriate JPanels
		if (panel.equals(this.panelEnergy))
		{
			lblEnergyTitle.setText("Energy of " + selectedCity.toString());
			createPanelEnergy();
		}

		if (panel.equals(this.panelBatteries))
		{
			lblBatteriesTitle.setText("Batteries of " + selectedCity.toString());
		}

		// Switching to Simulation panel
		if (panel.equals(this.panelSimulation))
		{
			startSimulation();
		}
	}
	//endregion

	public void createPanelEnergy()
	{
		this.panelEnergyProductionChart.removeAll();
		this.panelEnergyConsumptionChart.removeAll();
		int[] energyProductionTiers = Controller.getSelectedWMF().getEnergyProductionTiers();
		int[] energyConsumptionTiers = selectedCity.getEnergyConsumptionTiers();
		energyProductionTiersDouble = new double[energyProductionTiers.length];
		energyConsumptionTiersDouble = new double[energyConsumptionTiers.length];

		for (int i = 0; i < energyConsumptionTiers.length; i++)
		{
			energyProductionTiersDouble[i] = energyProductionTiers[i];
			energyConsumptionTiersDouble[i] = energyConsumptionTiers[i];
		}

		addJFreeChartToJPanel("Energy Production", this.panelEnergyProductionChart, energyProductionTiersDouble,
				"Hour", "Tier", true, 0, 23.5, 1,
				(double) (NUM_OF_TIERS + .5), MAJOR_TICK_SPACING);
		addJFreeChartToJPanel("Energy Consumption", this.panelEnergyConsumptionChart, energyConsumptionTiersDouble,
				"Hour", "Tier", true, 0, 23.5, 1,
				(double) (NUM_OF_TIERS + .5), MAJOR_TICK_SPACING);
	}

	private void startSimulation()
	{
		// Update the Controller magnitudes + the GRESBIMB magnitudes
		System.out.println("Updating controller magnitude by millisecond array...");
		Controller.updateMagnitudeByMillisecondArrays();
		magnitudeOfDemandsByMillisecond = Controller.getMagnitudeOfDemandsByMillisecond();
		magnitudeOfSurplusesByMillisecond = Controller.getMagnitudeOfSurplusesByMillisecond();
		System.out.println("Simulating...");

		// Set pbSimulationGridEnergyLevel width
		pbSimulationGridEnergyLevel.setSize(500, 500);

		// Set the citySimulator and windmillFarmSimulator
		citySimulator = Controller.getCitySimulator();
		windmillFarmSimulator = Controller.getWindmillFarmSimulator();

		// Find maximums
		findMaximumValueInMagnitudeOfDemandsArray();
		findMaximumValueInMagnitudeOfSurplusArray();

		if (maximumMagnitudeOfDemandsByMillisecond > maximumMagnitudeOfSurplusesByMillisecond)
		{
			maximumMagnitudeByMillisecond = maximumMagnitudeOfDemandsByMillisecond;
		}
		else
		{
			maximumMagnitudeByMillisecond = maximumMagnitudeOfSurplusesByMillisecond;
		}


		// Calculate the maximum total energy (for the battery display)
		calculateMaxTotalEnergyInJoules();

		// Calculate the current energy (for the battery display)
		calculateCurrentGridEnergyInJoules();

		// Add tier charts
		addJFreeChartToJPanel("Energy Production", this.panelSimulationSurplusTiers, energyProductionTiersDouble,
				"Hour", "Tier", true, 0, 23.5, 1,
				(double) (NUM_OF_TIERS + .5), MAJOR_TICK_SPACING);
		addJFreeChartToJPanel("Energy Consumption", this.panelSimulationDemandTiers, energyConsumptionTiersDouble,
				"Hour", "Tier", true, 0, 23.5, 1,
				(double) (NUM_OF_TIERS + .5), MAJOR_TICK_SPACING);

		// Initialize the charts
		this.updateSimulationDemandChartWithCurrentMillisecond(0);
		this.updateSimulationSurplusChartWithCurrentMillisecond(0);

		windmillFarmSimulator.simulate();
		citySimulator.simulate();
	}

	public void calculateCurrentGridEnergyInJoules()
	{
		currentGridEnergyInJoules = Controller.calculateCurrentGridEnergyInJoules();
		lblEnergyLevel.setText("Current Energy in Joules: " + currentGridEnergyInJoules);
		lblMaxEnergy.setText("Maximum Total Energy in Joules: " + maxTotalEnergyInJoules);
	}

	private void calculateMaxTotalEnergyInJoules()
	{
		maxTotalEnergyInJoules = Controller.calculateMaxTotalEnergyInJoules();
	}

	private void findMaximumValueInMagnitudeOfDemandsArray()
	{
		int max = 1;
		for (int i = 0; i < magnitudeOfDemandsByMillisecond.size(); i++)
		{
			if (magnitudeOfDemandsByMillisecond.get(i) > max)
			{
				max = (int) Math.round(magnitudeOfDemandsByMillisecond.get(i));
			}
		}

		maximumMagnitudeOfDemandsByMillisecond = max;
	}

	private void findMaximumValueInMagnitudeOfSurplusArray()
	{
		int max = 1;
		for (int i = 0; i < magnitudeOfSurplusesByMillisecond.size(); i++)
		{
			if (magnitudeOfSurplusesByMillisecond.get(i) > max)
			{
				max = (int) Math.round(magnitudeOfSurplusesByMillisecond.get(i));
			}
		}

		maximumMagnitudeOfSurplusesByMillisecond = max;
	}

	//region Update functions
	public static void update()
	{
		updateBatteryListModel();
		updateCityListModel();
	}

	private void allocateEnergySurplus(Surplus surplus)
	{
		Controller.allocateEnergySurplus(surplus);
	}

	// updates the list's batteryDefaultListModel according to the Controller's battery lists
	private static void updateBatteryListModel()
	{
		batteryDefaultListModel.clear();

		BatteryGrid grid = Controller.getGrid();

		// add gravitational batteries
		for (Battery battery : grid.getGravitationalBatteries())
		{
			batteryDefaultListModel.addElement(battery);
		}

		// add rotational batteries
		for (Battery battery : grid.getRotationalBatteries())
		{
			batteryDefaultListModel.addElement(battery);
		}
	}

	private static void updateCityListModel()
	{
		cityDefaultListModel.clear();

		ArrayList<City> availableCities = Controller.getAvailableCities();

		// add gravitational batteries
		for (City city : availableCities)
		{
			cityDefaultListModel.addElement(city);
		}
	}

	public void updateEnergyScreen()
	{
		createPanelEnergy();
	}

	public void updateSimulationDemandChartWithCurrentMillisecond(double currentMillisecond)
	{
		// Get current millisecond from WindmillFarmSimulator as the xAxisStartRange
		// Get a period of time later as the xAxisEndRange
		// Set xAxisTickUnit to a reasonable amount
		// Set yAxisRange to the maximum value of the entire "demands" array
		// Set yAxisTickUnit to roughly 20 ticks
		// Update the graph every millisecond

		convertCurrentMillisecondToHourAndMinute(currentMillisecond);
		panelSimulationDemandChart.removeAll();

		// Set the data for the chart
		double xAxisStartRange = currentMillisecond;
		double xAxisEndRange = xAxisStartRange + SIMULATION_CHART_WIDTH_IN_POINTS;
		double[] data = new double[SIMULATION_CHART_WIDTH_IN_POINTS];

		for (int i = 0; i < data.length; i++)
		{
			data[i] = magnitudeOfDemandsByMillisecond.get((int) (xAxisStartRange + i)).intValue();
		}

		double xAxisTickUnit = SIMULATION_CHART_WIDTH_IN_POINTS / 2;
		double yAxisRange = MAX_Y_AXIS_CHART_MULTIPLIER * maximumMagnitudeByMillisecond;
		double yAxisTickUnit = Math.round(MAX_Y_AXIS_CHART_MULTIPLIER * maximumMagnitudeByMillisecond / Y_AXIS_TICKS);

		addJFreeChartToJPanel("Demands", panelSimulationDemandChart, data, "Time: " +
						returnStringHour + ":" + returnStringMinute, "Total Magnitude of Demands",
				false, 0, SIMULATION_CHART_WIDTH_IN_POINTS, xAxisTickUnit, yAxisRange, yAxisTickUnit);

		// Set the progress bar values
		pBSimulation.setValue((int) ((currentMillisecond / 240000) * 100));
		pbSimulationGridEnergyLevel.setValue((int) ((currentGridEnergyInJoules / maxTotalEnergyInJoules) * 100));

		if (currentGridEnergyInJoules == 0)
		{
			countPowerOutage++;
		}
		else if (currentGridEnergyInJoules == maxTotalEnergyInJoules)
		{
			maxTotalEnergyInJoules++;
		}

		// Set the label values
		lblCountPowerOutage.setText("Number of power outages: " + countPowerOutage);

	}

	public void updateSimulationSurplusChartWithCurrentMillisecond(double currentMillisecond)
	{
		// Get current millisecond from WindmillFarmSimulator as the xAxisStartRange
		// Get a period of time later as the xAxisEndRange
		// Set xAxisTickUnit to a reasonable amount
		// Set yAxisRange to the maximum value of the entire "demands" array
		// Set yAxisTickUnit to roughly 20 ticks
		// Update the graph every millisecond

		panelSimulationSurplusChart.removeAll();

		// Set the data for the chart
		double xAxisStartRange = currentMillisecond;
		double xAxisEndRange = xAxisStartRange + SIMULATION_CHART_WIDTH_IN_POINTS;
		double[] data = new double[SIMULATION_CHART_WIDTH_IN_POINTS];

		for (int i = 0; i < data.length; i++)
		{
			data[i] = magnitudeOfSurplusesByMillisecond.get((int) (xAxisStartRange + i)).intValue();
		}

		double xAxisTickUnit = SIMULATION_CHART_WIDTH_IN_POINTS / 2;
		double yAxisRange = MAX_Y_AXIS_CHART_MULTIPLIER * maximumMagnitudeByMillisecond;
		double yAxisTickUnit = Math.round(MAX_Y_AXIS_CHART_MULTIPLIER * maximumMagnitudeByMillisecond / Y_AXIS_TICKS);

		addJFreeChartToJPanel("Surpluses", panelSimulationSurplusChart, data, "Time: " +
						returnStringHour + ":" + returnStringMinute, "Total Magnitude of Surpluses",
				false, 0, SIMULATION_CHART_WIDTH_IN_POINTS, xAxisTickUnit, yAxisRange, yAxisTickUnit);
	}

	private void convertCurrentMillisecondToHourAndMinute(double currentMillisecond)
	{
		int hour = (int) Math.floor(currentMillisecond / 10000);

		if (hour < 10)
		{
			returnStringHour = "0" + hour;
		}
		else
		{
			returnStringHour = "" + hour;
		}

		int minute = (int) Math.floor(currentMillisecond / (100000/600));
		minute = minute % 60;

		if (minute < 10)
		{
			returnStringMinute = "0" + minute;
		}
		else
		{
			returnStringMinute = "" + minute;
		}
	}
	//endregion

	private void restart()
	{
		windmillFarmSimulator.interruptTimer();
		citySimulator.interruptTimer();
		switchToPanel(panelWelcome);
	}

	public static void main(String[] args)
	{
		String title = "GRESBIMB";
		createNewJFrame(new GRESBIMB().panelMain, title, JFrame.EXIT_ON_CLOSE);
	}
	//endregion
}
