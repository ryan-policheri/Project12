package edu.view;

//region Imports
import edu.controllers.Controller;
import edu.model.EnergyCommander;
import edu.model.batteries.Battery;
import edu.model.batteries.BatteryGrid;
import edu.model.energySources.windmillFarm.WindmillFarmSimulator;

import javax.swing.*;
import javax.xml.transform.sax.SAXSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//endregion

public class MainUserGUI
{
	//region Initialize swing elements
	private JPanel panelMain;
	//region Welcome panel
	private JPanel panelWelcome;
	private JLabel lblWelcome;
	private JButton btnBatteries;
	private JButton btnEnergy;
	//endregion
	//region Energy panel
	private JPanel panelEnergy;
	private JLabel lblEnergy;
	private JButton btnRunWFS;
	private JButton btnEnergyCancel;
	//endregion
	//region Batteries panel
	private JList listBatteries;
	private JPanel panelBatteries;
	//endregion

	//region Initialize attributes
	private static DefaultListModel<Battery> model = new DefaultListModel<>();
	//endregion

	public MainUserGUI()
	{
		//TODO: find a way to automatically update the model... make model static?
		//TODO: Research how to use the different layouts
		listBatteries.setModel(model);

		//region Button listeners
		btnBatteries.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				createNewJFrame(new FormAddBattery().getPanelMain(), "Add Battery", JFrame.DISPOSE_ON_CLOSE);
			}
		});

		btnEnergy.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				changePanels(panelEnergy);
			}
		});

		btnEnergyCancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				changePanels(panelWelcome);
			}
		});

		btnRunWFS.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				createNewWindmillFarmSimulator();
			}
		});
		//endregion
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

	private void changePanels(JPanel panel)
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

	}

	// updates the list's model according to the Controller's battery lists
	private static void updateListModel()
	{
		model.clear();

		BatteryGrid grid = Controller.getGrid();

		// add gravitational batteries
		for (Battery battery : grid.getGravitationalBatteries())
		{
			model.addElement(battery);
			System.out.println(battery.toString() + "was added to the battery grid.");
		}

		for (Battery battery : grid.getRotationalBatteries())
		{
			model.addElement(battery);
			System.out.println(battery.toString() + "was added to the battery grid.");
		}
	}

	public static void main(String[] args)
	{
		createNewJFrame(new MainUserGUI().panelMain, "Project 12", JFrame.EXIT_ON_CLOSE);
	}
}
