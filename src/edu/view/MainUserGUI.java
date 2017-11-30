package edu.view;

//region Imports
import edu.controllers.Controller;
import edu.model.EnergyCommander;
import edu.model.batteries.*;
import edu.model.energySources.windmillFarm.WindmillFarmSimulator;

import javax.swing.*;
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
	//endregion

	//region Batteries panel
	private JPanel panelBatteries;

	private JList listBatteries;
	private JButton btnBatteriesAdd;
	private JButton btnBatteriesRemove;
	private JLabel lblBatteriesSelectedBatteryName;
	private JButton btnBatteriesBack;
	private JButton btnBatteriesNext;
	//endregion

	//region Place Batteries panel
	private JPanel panelPlaceBatteries;
	private JList listPlaceBatteries;
	private JLabel lblPlaceBatteriesCityName;
	private JButton btnPlaceBatteriesBack;
	private JButton btnPlaceBatteriesSimulate;
	//endregion
	//endregion

	//region Initialize attributes
	private static DefaultListModel<Battery> model = new DefaultListModel<>();
	//endregion

	//region Methods
	public MainUserGUI()
	{
		// TESTING FOR REMOVE FUNCTIONALITY
		for (int i = 10; i > 0; i--)
		{
			int a = (int) Math.round(Math.random() * 1000);

			GravitationalBattery gravitationalBattery = new GravitationalBattery("GRAV_" + a, 100, 100);
			Controller.addGravitationalBattery(gravitationalBattery);

			RotationalBattery rotationalBattery = new RotationalBattery("ROT_" + a, 100, 100,
					new FlywheelMaterial("Titanium"), new FlywheelBearing("Mechanical"));
			Controller.addRotationalBattery(rotationalBattery);
		}

		// make the two battery lists have the same model
		listBatteries.setModel(model);
		listPlaceBatteries.setModel(model);

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
		btnBatteriesNext.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				switchToPanel(panelPlaceBatteries);
			}
		});
		btnPlaceBatteriesBack.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				switchToPanel(panelBatteries);
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
		//endregion
		//endregion
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
	}
	//endregion

	//region Update functions
	public static void update()
	{
		updateListModel();
	}

	// updates the list's model according to the Controller's battery lists
	private static void updateListModel()
	{
		model.clear();
		System.out.println("Grid has been cleared.");

		BatteryGrid grid = Controller.getGrid();

		// add gravitational batteries
		for (Battery battery : grid.getGravitationalBatteries())
		{
			model.addElement(battery);
			System.out.println(battery.toString() + " was added to the battery grid.");
		}

		// add rotational batteries
		for (Battery battery : grid.getRotationalBatteries())
		{
			model.addElement(battery);
			System.out.println(battery.toString() + " was added to the battery grid.");
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
