package edu.view;

//region Imports

import edu.controllers.Controller;
import edu.model.batteries.*;
import edu.model.city.City;
import edu.model.city.CitySimulator;
import edu.model.energySources.solarFarm.PhotovoltaicSolarFarm;
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

	private JButton btnRemoveAllWindFarms;
	private JButton btnRemoveAllPVSolarFarms;
	private JButton btnEnergyBack;
	private JButton btnEnergyNext;
	private JPanel panelEnergyProductionChart;
	private JPanel panelEnergyConsumptionChart;
	//endregion

	//region Batteries panel
	private JPanel panelBatteries;

	private JList listBatteries;
	private JButton btnBuildDefualtBatteries;
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
	private JButton buildDefaultButton;
	private JButton btnBuildDefaultWindFarms;
	private JList listWindFarms;
	private JList listPVSolarFarms;
	private JButton btnAddWindFarm;
	private JButton btnAddPVSolarFarm;
	private JButton btnBuildDefaultPVSolarFarms;
	//endregion
	//endregion

	//region Initialize attributes
	private Controller controller;
	private static DefaultListModel<Battery> batteryDefaultListModel = new DefaultListModel<>();
	private static DefaultListModel<City> cityDefaultListModel = new DefaultListModel<>();
	private static DefaultListModel<WindmillFarm> windFarmDefaultListModel = new DefaultListModel<>();
	private static DefaultListModel<PhotovoltaicSolarFarm> PVSolarFarmDefaultListModel = new DefaultListModel<>();
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

	public GRESBIMB()
	{
		controller = new Controller(this);

		// Set lists
		listBatteries.setModel(batteryDefaultListModel);
		listCities.setModel(cityDefaultListModel);
		listWindFarms.setModel(windFarmDefaultListModel);
		listPVSolarFarms.setModel(PVSolarFarmDefaultListModel);

		//region Set the selected city
/*		Controller.setSelectedCity(Controller.getCity());
		selectedCity = Controller.getSelectedCity();*/
		//System.out.println("Selected City: " + Controller.getSelectedCity().toString());
		//endregion

		Surplus surplus = new Surplus(2000000000, 1);

		//Controller.allocateEnergySurplus(2000000000);

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
				Controller.buildDefaultCities();
				updateCityListModel();
				switchToPanel(panelPickCity);
			}
		});
		btnExit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		//endregion

		//region Energy Sources screen buttons
		//region wind farm buttons
		btnBuildDefaultWindFarms.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Controller.buildDefaultWindFarms();
				updateWindFarmList();
			}
		});
		btnAddWindFarm.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new WindFarmAddForm();
			}
		});
		btnRemoveAllWindFarms.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Controller.removeAllWindFarms();
				updateWindFarmList();
			}
		});
		//endregion
		//region PV solar farm buttons
		btnBuildDefaultPVSolarFarms.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Controller.buildDefaultPVSolarFarms();
				updatePVSolarList();
			}
		});
		btnAddPVSolarFarm.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new PVSolarFarmAddForm();
			}
		});
		btnRemoveAllPVSolarFarms.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Controller.removeAllPVSolarFarms();
				updatePVSolarList();
			}
		});
		//endregion
		//endregion

		//region Batteries screen buttons
		btnBuildDefualtBatteries.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Controller.buildDefaultVolatileBatteries();
				updateBatteryListModel();
			}
		});
		btnBatteriesAdd.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new BatteryAddForm();
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
					updateBatteryListModel();
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
				updateBatteryListModel();
			}
		});
		//endregion

		//region Simulate screen buttons
/*		btnSimulationStop.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				citySimulator.interruptTimer();
				windmillFarmSimulator.interruptTimer();
			}
		});*/
/*		btnSimulationExit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				restart();
			}
		});*/
		//endregion
		//endregion

		listCities.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				panelCitiesPreview.removeAll();

				int selectedCityIndex = listCities.getSelectedIndex();
				Controller.setSelectedCity(selectedCityIndex);
				selectedCity = Controller.getSelectedCity();

				String cityName = selectedCity.toString();
				double[] energyMinimumsByHour = selectedCity.getEnergyMinimumsByHourInMegawatts();
				double[] energyMaximumsByHour = selectedCity.getEnergyMaximumsByHourInMegawatts();
				double[][] data = new double[2][24];
				data[0] = energyMinimumsByHour;
				data[1] = energyMaximumsByHour;
				double highestGraphValue = selectedCity.getHighestEnergyValue();
				highestGraphValue *= 1.3; //for scaling

				lblPickCitySelectedCityName.setText("Selected city: " + cityName);

				addJFreeChartToJPanel("Energy Consumption of " + cityName, panelCitiesPreview,
						data, "Hour", "Megawatt Hours", true, 0,
						23.5, 1, highestGraphValue, 100);

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

	//region GUI functions
	//region make JFreeChart
	private void addJFreeChartToJPanel(String title, JPanel panel, double[][] data, String xAxisLabel, String yAxisLabel,
									   boolean hasTicks, double xAxisStartRange, double xAxisEndRange,
									   double xAxisTickUnit, double yAxisRange, double yAxisTickUnit)
	{
		XYSeriesCollection dataset = new XYSeriesCollection();

		for(int i = 0; i < data.length; i++)
		{
			XYSeries series = new XYSeries("XYGraph" + Integer.toString(i));

			// Add data to the series
			for (int j = 0; j < data[i].length; j++)
			{
				series.add(j, data[i][j]);
			}

			// Add the series to your data set
			dataset.addSeries(series);
		}

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
	//endregion

	protected static void createNewJFrame(JPanel panel, String title, int operation)
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

		if (panel.equals(this.panelEnergy))
		{
			lblEnergyTitle.setText("Energy Sources For " + selectedCity.toString());
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

	private void startSimulation()
	{
		// Update the Controller magnitudes + the GRESBIMB magnitudes
		System.out.println("Updating controller magnitude by millisecond array...");
		/*Controller.updateMagnitudeByMillisecondArrays();*/
		//magnitudeOfDemandsByMillisecond = Controller.getMagnitudeOfDemandsByMillisecond();
		//magnitudeOfSurplusesByMillisecond = Controller.getMagnitudeOfSurplusesByMillisecond();
		System.out.println("Simulating...");

		// Set pbSimulationGridEnergyLevel width
		pbSimulationGridEnergyLevel.setSize(500, 500);

		// Set the citySimulator and windmillFarmSimulator
		//citySimulator = Controller.getCitySimulator();
		//windmillFarmSimulator = Controller.getWindmillFarmSimulator();

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

		// Initialize the charts
		this.updateSimulationDemandChartWithCurrentMillisecond(0);
		this.updateSimulationSurplusChartWithCurrentMillisecond(0);

/*		windmillFarmSimulator.simulate();
		citySimulator.simulate();*/
	}

	public void calculateCurrentGridEnergyInJoules()
	{
		currentGridEnergyInJoules = Controller.calculateCurrentGridEnergyInJoules();
		lblEnergyLevel.setText("Current Energy (joules): " + currentGridEnergyInJoules);
		lblMaxEnergy.setText("Max Energy (joules): " + maxTotalEnergyInJoules);
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
	private void allocateEnergySurplus(Surplus surplus)
	{
		//Controller.allocateEnergySurplus(surplus);
	}

	//region update lists
	protected static void updateWindFarmList()
	{
		windFarmDefaultListModel.clear();

		for (WindmillFarm windFarm : Controller.getWindFarms())
		{
			windFarmDefaultListModel.addElement(windFarm);
		}
	}

	protected static void updatePVSolarList()
	{
		PVSolarFarmDefaultListModel.clear();

		for (PhotovoltaicSolarFarm PVSolarFarm : Controller.getPVSolarFarms())
		{
			PVSolarFarmDefaultListModel.addElement(PVSolarFarm);
		}
	}

	protected static void updateBatteryListModel()
	{
		batteryDefaultListModel.clear();

		BatteryGrid grid = Controller.getBatteryGrid();

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

	protected static void updateCityListModel()
	{
		cityDefaultListModel.clear();

		for (City city : Controller.getAvailableCities())
		{
			cityDefaultListModel.addElement(city);
		}
	}
	//endregion

	public void setupSimulation()
	{
		pbSimulationGridEnergyLevel.setSize(500, 500);
	}

	public void updateBatteryGridEnergyLevel(double currentGridEnergyInJoules, double maxTotalEnergyInJoules)
	{
		pbSimulationGridEnergyLevel.setValue((int) ((currentGridEnergyInJoules / maxTotalEnergyInJoules) * 100));
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

/*		addJFreeChartToJPanel("Demands", panelSimulationDemandChart, data, "Time: " +
						returnStringHour + ":" + returnStringMinute, "Total Magnitude of Demands",
				false, 0, SIMULATION_CHART_WIDTH_IN_POINTS, xAxisTickUnit, yAxisRange, yAxisTickUnit);*/

		// Set the progress bar values
		pBSimulation.setValue((int) ((currentMillisecond / 240000) * 100));
		pbSimulationGridEnergyLevel.setValue((int) ((currentGridEnergyInJoules / maxTotalEnergyInJoules) * 100));

		if (currentGridEnergyInJoules == 0 && currentMillisecond > 1000)
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

/*		addJFreeChartToJPanel("Surpluses", panelSimulationSurplusChart, data, "Time: " +
						returnStringHour + ":" + returnStringMinute, "Total Magnitude of Surpluses",
				false, 0, SIMULATION_CHART_WIDTH_IN_POINTS, xAxisTickUnit, yAxisRange, yAxisTickUnit);*/
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

		int minute = (int) Math.floor(currentMillisecond / (10000/60));
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

/*	private void restart()
	{
		windmillFarmSimulator.interruptTimer();
		citySimulator.interruptTimer();
		switchToPanel(panelWelcome);
	}*/

	public static void main(String[] args)
	{
		String title = "GRESBIMB";
		createNewJFrame(new GRESBIMB().panelMain, title, JFrame.EXIT_ON_CLOSE);
	}
}
