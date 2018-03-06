package edu.view;

import javax.swing.*;

public class FormEditEnergyProduction extends EnergyForm
{
	public FormEditEnergyProduction()
	{
		// Set default slider values
/*		for (int i = 0; i < sliders.size(); i++)
		{
			sliders.get(i).setValue(windmillFarm.getEnergyProductionTiers()[i]);
		}*/

		// Set title
		//String title = "Energy Production for " + city.toString();
		//this.lblTitle.setText(title);

		// Create charts based on the city's energyProductionTiers
		//addJFreeChartToJPanel(this.panelPreviousGraph, windmillFarm.getEnergyProductionTiers(), true);
		//addJFreeChartToJPanel(this.panelNewGraph, windmillFarm.getEnergyProductionTiers(), false);
	}

	public void updateGraphs()
	{
		int[] modifiedEnergyProductionTiers = new int[24];

		for (int i = 0; i < modifiedEnergyProductionTiers.length; i++)
		{
			modifiedEnergyProductionTiers[i] = sliders.get(i).getValue();
		}

		addJFreeChartToJPanel(this.panelNewGraph, modifiedEnergyProductionTiers, false);
	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("FormEditEnergyProduction");
		frame.setContentPane(new FormEditEnergyProduction().panelMain);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
