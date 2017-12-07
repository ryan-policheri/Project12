package edu.view.misc;

import edu.view.EnergyForm;
import edu.view.FormEditEnergyConsumption;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// TESTING
// We can't have the form
public class SliderListener implements ChangeListener
{
	private EnergyForm form;

	public SliderListener(FormEditEnergyConsumption formEditEnergyConsumption)
	{
		this.form = formEditEnergyConsumption;
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		this.form.updateGraphs();
	}
}