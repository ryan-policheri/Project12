package edu.view;

//region Imports

import edu.controllers.Controller;
import edu.model.batteries.*;
import edu.model.city.City;
import edu.model.energySources.solarFarm.PhotovoltaicSolarFarm;
import edu.model.energySources.windmillFarm.WindmillFarm;
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
import java.text.DecimalFormat;
import java.util.Arrays;
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
	private JPanel panelOverviewChart;
	private JPanel panelHourlyChart;
	private JPanel panelSimulationTierCharts;
	private JPanel panelSimulationDemandTiers;
	private JPanel panelSimulationSurplusTiers;
	private JPanel panelSimulationEnergy;
	private JProgressBar pBSimulation;
	private JLabel lblCurrentVolatileEnergy;
	private JProgressBar pbSimulationConstantFlowEnergyLevel;
	private JLabel lblMaxVolatileEnergy;
	private JLabel lblCountReachedMaxCapacity;
	private JLabel lblCountPowerOutage;
	private JButton btnSimulationExit;
	private JButton btnSimulationStop;
	private JPanel panelCitiesPreview;
	private JLabel lblEnergyTitle;
	private JLabel lblBatteryType;
	private JLabel lblBatteryMaxStorage;
	private JLabel lblBatteriesTitle;
	private JButton btnBuildDefaultWindFarms;
	private JList listWindFarms;
	private JList listPVSolarFarms;
	private JButton btnAddWindFarm;
	private JButton btnAddPVSolarFarm;
	private JButton btnBuildDefaultPVSolarFarms;
	private JButton btnLaunchSimulation;
	private JProgressBar pbSimulationVolatileEnergyLevel;
	private JLabel lblCurrentCFEnergy;
	private JLabel lblMaxCFEnergy;
	private JLabel lblPowerShortageAmount;
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

	// For counting simulation
	private static int countReachedMaxCapacity = 0;
	private static int countPowerOutage = 0;
	//endregion

	private static double[][] overviewGraphData = new double[4][240];
	private static double[][] hourlyGraphData = new double[4][10];

	private static double maxVolatileEnergyInJoules;
	private static double maxCFEnergyInJoules;

	private static DecimalFormat decimalFormat = new DecimalFormat("###,###.00");


	public GRESBIMB()
	{
		controller = new Controller(this);

		pbSimulationVolatileEnergyLevel.setBorderPainted(true); //couldn't figure out how to set in form so doing it here
		pbSimulationConstantFlowEnergyLevel.setBorderPainted(true); //couldn't figure out how to set in form so doing it here

		//region Set lists
		listBatteries.setModel(batteryDefaultListModel);
		listCities.setModel(cityDefaultListModel);
		listWindFarms.setModel(windFarmDefaultListModel);
		listPVSolarFarms.setModel(PVSolarFarmDefaultListModel);
		//endregion

		//region Switching between panels listeners
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
		//endregion   listen

		//region Welcome screen listeners
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

		//region City listeners
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
					lblBatteryMaxStorage.setText(decimalFormat.format(batteryDefaultListModel.get(selectedIndex).getMaxEnergyInJoules() / 1000000)); //convert to megajoules
				}
			}
		});
		//endregion

		//region Energy sources screen listeners
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

		//region Batteries screen listeners
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

		//region Simulate screen listeners
		btnSimulationStop.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{

			}
		});
		btnSimulationExit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		btnLaunchSimulation.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Controller.launchSimulation();
			}
		});
		//endregion
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
			Controller.preFlightSetup();
		}
	}
	//endregion

	//region Update lists functions
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

	//region Update simulation
	public void updateSimulationScreen(double currentVolatileEnergyInJoules, double currentCFEnergyInJoules, double energyProduced, double windEnergyProduced, double PVSolarEnergyProduced, double energyDemanded, long graphIndex, int numberOfShortages, double amountOfShortageJoules)
	{
		pbSimulationVolatileEnergyLevel.setValue((int) ((currentVolatileEnergyInJoules / this.maxVolatileEnergyInJoules) * 100));
		String existingLabelText = lblCurrentVolatileEnergy.getText().split(":")[0];
		lblCurrentVolatileEnergy.setText(existingLabelText + ": " + decimalFormat.format(currentVolatileEnergyInJoules / 1000000000));

		pbSimulationConstantFlowEnergyLevel.setValue((int) ((currentCFEnergyInJoules / this.maxCFEnergyInJoules) * 100));
		existingLabelText = lblCurrentCFEnergy.getText().split(":")[0];
		lblCurrentCFEnergy.setText(existingLabelText + ": " + decimalFormat.format(currentCFEnergyInJoules / 1000000000));

		existingLabelText = lblCountPowerOutage.getText().split(":")[0];
		lblCountPowerOutage.setText(existingLabelText + ": " + numberOfShortages);

		existingLabelText = lblPowerShortageAmount.getText().split(":")[0];
		lblPowerShortageAmount.setText(existingLabelText + ": " + decimalFormat.format(amountOfShortageJoules / 1000000000));

		pBSimulation.setValue((int) ((graphIndex / 239.0) * 100));

		panelOverviewChart.removeAll();
		panelHourlyChart.removeAll();

		overviewGraphData[0][(int) graphIndex] = energyProduced / 1000000000;
		overviewGraphData[1][(int) graphIndex] = windEnergyProduced / 1000000000;
		overviewGraphData[2][(int) graphIndex] = PVSolarEnergyProduced / 1000000000;
		overviewGraphData[3][(int) graphIndex] = energyDemanded / 1000000000;

		if(graphIndex > 9)
		{
			for(double[] row : hourlyGraphData)
			{
				System.arraycopy(row, 1, row, 0, 9);
			}

			hourlyGraphData[0][9] = energyProduced / 1000000000;
			hourlyGraphData[1][9] = windEnergyProduced / 1000000000;
			hourlyGraphData[2][9] = PVSolarEnergyProduced / 1000000000;
			hourlyGraphData[3][9] = energyDemanded / 1000000000;
		}
		else
		{
			hourlyGraphData[0][(int) graphIndex] = energyProduced / 1000000000;
			hourlyGraphData[1][(int) graphIndex] = windEnergyProduced / 1000000000;
			hourlyGraphData[2][(int) graphIndex] = PVSolarEnergyProduced / 1000000000;
			hourlyGraphData[3][(int) graphIndex] = energyDemanded / 1000000000;
		}


		double xAxisTickUnit = 1;
		double yAxisRange = 1500;
		double yAxisTickUnit = 200;

		addJFreeChartToJPanel("Real-Time View", panelHourlyChart, hourlyGraphData, "Time", "Gigawatts",
				false, 0, 10, xAxisTickUnit, yAxisRange, yAxisTickUnit);

		addJFreeChartToJPanel("Overview", panelOverviewChart,
				overviewGraphData, "Time", "Gigawatts", false, 0,
				240, xAxisTickUnit, yAxisRange, yAxisTickUnit);

	}
	//endregion

	//region convert millisecond to hour and minute
	private void convertCurrentMillisecondToHourAndMinute(double currentMillisecond)
	{
		String returnStringHour = "";

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

		String returnStringMinute = "";

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

	//region getters and setters
	public void setMaxVolatileEnergyInJoules(double maxVolatileEnergyInJoules)
	{
		this.maxVolatileEnergyInJoules = maxVolatileEnergyInJoules;
		this.lblMaxVolatileEnergy.setText(this.lblMaxVolatileEnergy.getText() + decimalFormat.format(this.maxVolatileEnergyInJoules / 1000000000));
	}

	public void setMaxCFEnergyInJoules(double maxCFEnergyInJoules)
	{
		this.maxCFEnergyInJoules = maxCFEnergyInJoules;
		this.lblMaxCFEnergy.setText(this.lblMaxCFEnergy.getText() + decimalFormat.format(this.maxCFEnergyInJoules / 1000000000));
	}
	//endregion

	public static void main(String[] args)
	{
		String title = "GRESBIMB";
		createNewJFrame(new GRESBIMB().panelMain, title, JFrame.EXIT_ON_CLOSE);
	}
}
