package edu.view.misc;

import edu.view.WindTierForm;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderListener implements ChangeListener
{
	private WindTierForm windTierForm;

	public SliderListener(WindTierForm windTierForm)
	{
		this.windTierForm = windTierForm;
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		this.windTierForm.updateGraphs();
	}
}