package edu.view;

//region Imports
import edu.controllers.Controller;
import edu.model.EnergyCommander;
import edu.model.batteries.Battery;
import edu.model.energySources.windmillFarm.WindmillFarmSimulator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//endregion

public class MainUserGUI
{
	//region Initialize swing elements
	private JPanel panelMain;
	private JPanel panelWelcome;
	private JLabel lblWelcome;
	private JButton btnBatteries;
	private JButton btnEnergy;
	private JList listBatteries;
	private JPanel panelBatteries;
	private JPanel panelEnergy;
	private JLabel lblEnergy;
	private JButton btnRunWFS;
	private JButton btnEnergyCancel;
	//endregion

	//region Initialize attributes
	private DefaultListModel<Battery> model = new DefaultListModel<>();
	//endregion

	public MainUserGUI()
	{
		//TODO: find a way to automatically update the model... make model static?
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

	public static void main(String[] args)
	{
		createNewJFrame(new MainUserGUI().panelMain, "Project 12", JFrame.EXIT_ON_CLOSE);
	}
}
