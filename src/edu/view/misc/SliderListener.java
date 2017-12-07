package edu.view.misc;

import edu.view.EnergyForm;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderListener implements ChangeListener
{
	private EnergyForm energyForm;

	public SliderListener(EnergyForm energyForm)
	{
		this.energyForm = energyForm;
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		this.energyForm.updateGraphs();
	}
}