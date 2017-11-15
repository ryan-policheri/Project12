package edu.view;

//region Imports
import edu.controllers.Controller;
import edu.model.EnergyCommander;
import edu.model.batteries.Battery;
import edu.model.batteries.BatteryGrid;
import edu.model.energySources.windmillFarm.WindmillFarmSimulator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//endregion

public class MainUserGUI
{
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
	private JLabel lblPickCityEnergyProduction;
	private JLabel lblPickCityEnergyConsumption;
	private JLabel lblPickCitySurplus;
	private JLabel lblPickCitySquareMilage;
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
	//endregion

	//region Batteries panel
	private JPanel panelBatteries;
	private JList listBatteries;
	private JLabel lblBatteriesSelectedBatteryName;
	private JButton btnBatteriesAdd;
	private JButton btnBatteriesRemove;
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

	public MainUserGUI()
	{
		//TODO: Research how to use the different layouts

		//region Button listeners
		/*btnBatteries.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String title = "Add Battery";
				createNewJFrame(new FormAddBattery().getPanelMain(), title, JFrame.DISPOSE_ON_CLOSE);
			}
		});

		btnEnergy.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				switchToPanel(panelEnergy);
			}
		});

		btnEnergyCancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				switchToPanel(panelWelcome);
			}
		});

		btnRunWFS.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				createNewWindmillFarmSimulator();
			}
		});*/
		//endregion

		btnStartNewSimulation.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				switchToPanel(panelPickCity);
			}
		});
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

	}

	private void createNewWindmillFarmSimulator()
	{
		WindmillFarmSimulator windmillFarmSimulator = new WindmillFarmSimulator();
		EnergyCommander energyCommander = new EnergyCommander(Controller.getGrid()); // TODO: Ask Poli, is this static?
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
			System.out.println(battery.toString() + "was added to the battery grid.");
		}

		// add rotational batteries
		for (Battery battery : grid.getRotationalBatteries())
		{
			model.addElement(battery);
			System.out.println(battery.toString() + "was added to the battery grid.");
		}
	}
	//endregion

	public static void main(String[] args)
	{
		String title = "Project 12";
		createNewJFrame(new MainUserGUI().panelMain, title, JFrame.EXIT_ON_CLOSE);
	}
}
