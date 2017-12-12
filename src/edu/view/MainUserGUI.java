package edu.view;

//region Imports
import edu.controllers.Controller;
import edu.model.EnergyCommander;
import edu.model.batteries.*;
import edu.model.city.City;
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
	private JLabel lblBatteriesSelectedBatteryName;
	private JButton btnBatteriesBack;
	private JButton btnBatteriesSimulate;
	//endregion

	//region Place Batteries panel
	private JPanel panelPlaceBatteries;
	private JList listPlaceBatteries;
	private JButton btnPlaceBatteriesBack;
	private JButton btnBatteriesRemoveAll;
	//endregion
	//endregion

	//region Initialize attributes
	private static DefaultListModel<Battery> batteryDefaultListModel = new DefaultListModel<>();
	private static DefaultListModel<City> cityDefaultListModel = new DefaultListModel<>();
	private static City selectedCity;
	protected static final int NUM_OF_TIERS = Controller.getNumOfTiers();
	protected static final int MAJOR_TICK_SPACING = Controller.getMajorTickSpacing();
	//endregion

	//region Methods
	public MainUserGUI()
	{
		//TODO: Add cities to the list, be able to select them and change their info on the fly
		//TODO: Add a final "Simulate" feature
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

				Controller.updateCities();

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

	private void addJFreeChartToJPanel(JPanel panel, int[] energyConsumptionTiers)
	{
		XYSeries series = new XYSeries("XYGraph");

		// Add city energyConsumptionTiers data to the series
		for (int i = 0; i < energyConsumptionTiers.length; i++)
		{
			series.add(i, energyConsumptionTiers[i]);
		}

		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);

		// Generate the graph
		String title = "";
		JFreeChart chart = ChartFactory.createXYLineChart(title,"Hour","Tier", dataset,
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
		domain.setRange(0, 23.5);
		domain.setTickUnit(new NumberTickUnit(1));
		domain.setTickLabelsVisible(false);
		// Range
		NumberAxis range = (NumberAxis) chartPlot.getRangeAxis();
		range.setRange(0, NUM_OF_TIERS + .5);
		range.setTickUnit(new NumberTickUnit(MAJOR_TICK_SPACING));

		// Add it the graph to the JPanel
		ChartPanel CP = new ChartPanel(chart);
		panel.add(CP, BorderLayout.CENTER);
		panel.validate();
	}

	private void createNewWindmillFarmSimulator()
	{
		WindmillFarmSimulator windmillFarmSimulator = new WindmillFarmSimulator();
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

	private void switchToPanel(JPanel panel)
	{
		panelMain.removeAll();
		panelMain.add(panel);
		panelMain.repaint();
		panelMain.revalidate();

		// If it's the Energy panel, insert the charts into the appropriate JPanels
		if (panel.equals(this.panelEnergy))
		{
			this.panelEnergyProductionChart.removeAll();
			this.panelEnergyConsumptionChart.removeAll();
			addJFreeChartToJPanel(this.panelEnergyProductionChart, selectedCity.getEnergyProductionTiers());
			addJFreeChartToJPanel(this.panelEnergyConsumptionChart, selectedCity.getEnergyConsumptionTiers());
		}
	}
	//endregion

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
	//endregion

	public static void main(String[] args)
	{
		String title = "Project 12";
		createNewJFrame(new MainUserGUI().panelMain, title, JFrame.EXIT_ON_CLOSE);
	}
	//endregion
}
