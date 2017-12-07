package edu.view;

import javax.swing.*;

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

	//region Getters/Setters
	public JPanel getPanelMain()
	{
		return panelMain;
	}
	//endregion

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("FormEditEnergyConsumption");
		frame.setContentPane(new FormEditEnergyConsumption().panelMain);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
