package edu.view;

import edu.controllers.Controller;
import javafx.scene.control.Slider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormEditEnergyConsumption extends EnergyForm
{
	public FormEditEnergyConsumption()
	{
		// Set default slider values
		for (int i = 0; i < sliders.size(); i++)
		{
			sliders.get(i).setValue(city.getEnergyConsumptionTiers()[i]);
		}

		// Set title
		String title = "Energy Consumption for " + city.toString();
		this.lblTitle.setText(title);

		// Create charts based on the city's energyConsumptionTiers
		addJFreeChartToJPanel(this.panelPreviousGraph, city.getEnergyConsumptionTiers(), true);
		addJFreeChartToJPanel(this.panelNewGraph, city.getEnergyConsumptionTiers(), false);

		btnSave.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int[] sliderValues = new int[24];
				for (int i = 0; i < sliderValues.length; i++)
				{
					sliderValues[i] = sliders.get(i).getValue();
				}

				Controller.setSelectedCityConsumptionValues(sliderValues);
				JOptionPane.showMessageDialog(null, "Values saved successfully.");
			}
		});
	}

	public void updateGraphs()
	{
		int[] modifiedEnergyConsumptionTiers = new int[24];

		for (int i = 0; i < modifiedEnergyConsumptionTiers.length; i++)
		{
			modifiedEnergyConsumptionTiers[i] = sliders.get(i).getValue();
		}

		addJFreeChartToJPanel(this.panelNewGraph, modifiedEnergyConsumptionTiers, false);
	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("FormEditEnergyConsumption");
		frame.setContentPane(new FormEditEnergyConsumption().panelMain);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
