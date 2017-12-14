package edu.view;

//region Imports
import edu.controllers.Controller;
import edu.model.EnergyCommander;
import edu.model.batteries.*;
import edu.model.city.City;
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
import java.util.ArrayList;
//endregion

public class MainUserGUI
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
	private JButton btnWindmillFarm;
	private JButton btnExit;
	//endregion

	//region Pick City panel
	private JPanel panelPickCity;

	private JList listCities;
	private JLabel lblPickCitySelectedCityName;
	private JLabel lblPickCityEnergyProduction;
	private JLabel lblPickCityEnergyConsumption;
	private JLabel lblPickCitySurplus;
	private JLabel lblPickCitySquareMilage;
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
	//endregion
	//endregion

	//region Initialize attributes
	private Controller controller;
	private static DefaultListModel<Battery> batteryDefaultListModel = new DefaultListModel<>();
	private static DefaultListModel<City> cityDefaultListModel = new DefaultListModel<>();
	private static City selectedCity;
	private static final int NUM_OF_TIERS = Controller.getNumOfTiers();
	private static final int MAJOR_TICK_SPACING = Controller.getMajorTickSpacing();
	private static final int SIMULATION_CHART_WIDTH_IN_POINTS = 2000;
	private static WindmillFarm selectedWindmillFarm = Controller.getSelectedWMF();
	private static ArrayList<Double> magnitudeOfDemandsByMillisecond;
	private static ArrayList<Double> magnitudeOfSurplusesByMillisecond;
	private static WindmillFarmSimulator windmillFarmSimulator = Controller.getWindmillFarmSimulator();
	private static double maximumMagnitudeOfDemandsByMillisecond;
	private static double maximumMagnitudeOfSurplusesByMillisecond;
	private static final double MAX_Y_AXIS_CHART_MULTIPLIER = 1.1;
	private static final double Y_AXIS_TICKS = 5.1;

	// For the simulation batteries
	private static double maxTotalEnergyInJoules;
	private static double currentGridEnergyInJoules;

	// For tier charts
	private static double[] energyProductionTiersDouble;
	private static double[] energyConsumptionTiersDouble;
	//endregion

	//region Methods
	public MainUserGUI()
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

		//region NATHAN TESTING FOR CITY LIST FUNCTIONALITY
		Controller.updateCities();
		//endregion

		//region NATHAN TESTING FOR REMOVE FUNCTIONALITY
		// Adding 20 batteries total with random names
		for (int i = 10; i > 0; i--)
		{
			int a = (int) Math.round(Math.random() * 1000);

			GravitationalBattery gravitationalBattery = new GravitationalBattery("GRAV_" + a, 100, 100);
			Controller.addGravitationalBattery(gravitationalBattery);

			RotationalBattery rotationalBattery = new RotationalBattery("ROT_" + a, 100, 100,
					new FlywheelMaterial("Titanium"), new FlywheelBearing("Mechanical"));
			Controller.addRotationalBattery(rotationalBattery);
		}
		//endregion

		// Set the battery list batteryDefaultListModel
		listBatteries.setModel(batteryDefaultListModel);

		// Set the cities list batteryDefaultListModel
		listCities.setModel(cityDefaultListModel);

		//region NATHAN TESTING FOR SELECTED CITY FUNCTIONALITY
		Controller.setSelectedCity(Controller.getDesMoines());
		selectedCity = Controller.getSelectedCity();
		System.out.println("Selected City: " + Controller.getSelectedCity().toString());
		//endregion

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
				switchToPanel(panelEnergy);
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
		listCities.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				// Set the selectedCity in the Controller to the selected city in the GUI. Update the Controller
				ArrayList<City> cities = Controller.getAvailableCities();
				City selectedCity = cities.get(listCities.getSelectedIndex());
				Controller.setSelectedCity(selectedCity);

				// Made it laggy
				// Controller.updateCities();

				// Update labels
				lblPickCitySelectedCityName.setText("Selected City: " + selectedCity.toString());
			}
		});
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
		btnWindmillFarm.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				createNewWindmillFarmSimulator();
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
		//endregion
	}

	private void addJFreeChartToJPanel(String title, JPanel panel, double[] data, String xAxisLabel, String yAxisLabel,
									   double xAxisStartRange, double xAxisEndRange, double xAxisTickUnit,
									   double yAxisRange, double yAxisTickUnit)
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
		domain.setTickLabelsVisible(false);
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
			createPanelEnergy();
		}

		// Switching to Simulation panel
		if (panel.equals(this.panelSimulation))
		{
			startSimulation();
		}
	}
	//endregion

	private void createPanelEnergy()
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
				"Hour", "Tier", 0, 23.5, 1,
				(double) (NUM_OF_TIERS + .5), MAJOR_TICK_SPACING);
		addJFreeChartToJPanel("Energy Consumption", this.panelEnergyConsumptionChart, energyConsumptionTiersDouble,
				"Hour", "Tier", 0, 23.5, 1,
				(double) (NUM_OF_TIERS + .5), MAJOR_TICK_SPACING);
	}

	private void startSimulation()
	{
		// Update the Controller magnitudes + the MainUserGUI magnitudes
		System.out.println("Updating controller magnitude by millisecond array...");
		Controller.updateMagnitudeByMillisecondArrays();
		magnitudeOfDemandsByMillisecond = Controller.getMagnitudeOfDemandsByMillisecond();
		magnitudeOfSurplusesByMillisecond = Controller.getMagnitudeOfSurplusesByMillisecond();
		System.out.println("Simulating...");

		// Find maximums
		findMaximumValueInMagnitudeOfDemandsArray();
		findMaximumValueInMagnitudeOfSurplusArray();

		// Calculate the maximum total energy (for the battery display)
		calculateMaxTotalEnergyInJoules();

		// Calculate the current energy (for the battery display)
		calculateCurrentGridEnergyInJoules();

		// Add tier charts
		addJFreeChartToJPanel("Energy Production", this.panelSimulationSurplusTiers, energyProductionTiersDouble,
				"Hour", "Tier", 0, 23.5, 1,
				(double) (NUM_OF_TIERS + .5), MAJOR_TICK_SPACING);
		addJFreeChartToJPanel("Energy Consumption", this.panelSimulationDemandTiers, energyConsumptionTiersDouble,
				"Hour", "Tier", 0, 23.5, 1,
				(double) (NUM_OF_TIERS + .5), MAJOR_TICK_SPACING);

		// Initialize the charts
		this.updateSimulationDemandChartWithCurrentMillisecond(0);
		this.updateSimulationSurplusChartWithCurrentMillisecond(0);

		windmillFarmSimulator.simulate();
	}

	public void calculateCurrentGridEnergyInJoules()
	{
		currentGridEnergyInJoules = Controller.calculateCurrentGridEnergyInJoules();
		lblEnergyLevel.setText("Current Energy in Joules: " + currentGridEnergyInJoules + "\n Maximum Total Energy in" +
				" Joules: " + maxTotalEnergyInJoules);
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

	public void updateSimulationDemandChartWithCurrentMillisecond(double currentMillisecond)
	{
		// Get current millisecond from WindmillFarmSimulator as the xAxisStartRange
		// Get a period of time later as the xAxisEndRange
		// Set xAxisTickUnit to a reasonable amount
		// Set yAxisRange to the maximum value of the entire "demands" array
		// Set yAxisTickUnit to roughly 20 ticks
		// Update the graph every millisecond

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
		double yAxisRange = MAX_Y_AXIS_CHART_MULTIPLIER * maximumMagnitudeOfDemandsByMillisecond;
		double yAxisTickUnit = Math.round(MAX_Y_AXIS_CHART_MULTIPLIER * maximumMagnitudeOfDemandsByMillisecond / Y_AXIS_TICKS);

		addJFreeChartToJPanel("Demands", panelSimulationDemandChart, data, "Time: " + currentMillisecond,
				"Total Magnitude of Demands", 0, SIMULATION_CHART_WIDTH_IN_POINTS, xAxisTickUnit, yAxisRange,
				yAxisTickUnit);

		// Set the progress bar
		pBSimulation.setValue((int) ((currentMillisecond / 240000) * 100));
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
		double yAxisRange = MAX_Y_AXIS_CHART_MULTIPLIER * maximumMagnitudeOfSurplusesByMillisecond;
		double yAxisTickUnit = Math.round(MAX_Y_AXIS_CHART_MULTIPLIER * maximumMagnitudeOfSurplusesByMillisecond / Y_AXIS_TICKS);

		addJFreeChartToJPanel("Surpluses", panelSimulationSurplusChart, data, "Time: " + currentMillisecond,
				"Total Magnitude of Surpluses", 0, SIMULATION_CHART_WIDTH_IN_POINTS, xAxisTickUnit, yAxisRange,
				yAxisTickUnit);
	}
	//endregion

	public static void main(String[] args)
	{
		String title = "Project 12";
		createNewJFrame(new MainUserGUI().panelMain, title, JFrame.EXIT_ON_CLOSE);
	}
	//endregion
}
