package edu.view;

import edu.controllers.Controller;

import javax.swing.*;
import java.util.ArrayList;

public class FormEditEnergyProduction
{
	//region Initialize swing elements
	private JPanel panelMain;
	private JPanel panelHeader;
	private JPanel panelSliderValues;

	//region Sliders
	private JSlider slider12AM;
	private JSlider slider1AM;
	private JSlider slider2AM;
	private JSlider slider3AM;
	private JSlider slider4AM;
	private JSlider slider5AM;
	private JSlider slider6AM;
	private JSlider slider7AM;
	private JSlider slider8AM;
	private JSlider slider9AM;
	private JSlider slider10AM;
	private JSlider slider11AM;
	private JSlider slider12PM;
	private JSlider slider1PM;
	private JSlider slider2PM;
	private JSlider slider3PM;
	private JSlider slider4PM;
	private JSlider slider5PM;
	private JSlider slider6PM;
	private JSlider slider7PM;
	private JSlider slider8PM;
	private JSlider slider9PM;
	private JSlider slider10PM;
	private JSlider slider11PM;
	//endregion

	//region Image panel
	private JPanel panelImage;
	//endregion

	//region Buttons panel
	private JPanel panelButtons;
	private JButton btnSave;
	private JButton btnCancel;
	//endregion
	//endregion

	private static ArrayList<JSlider> sliders = new ArrayList<>();
	private static final int NUM_OF_TIERS = Controller.getnumOfTiers();
	private static final int MAJOR_TICK_SPACING = Controller.getMajorTickSpacing();

	public FormEditEnergyProduction()
	{
		addSlidersToSliderList();

		for (int i = 0; i < sliders.size(); i++)
		{
			sliders.get(i).setMaximum(NUM_OF_TIERS);
			sliders.get(i).setMajorTickSpacing(MAJOR_TICK_SPACING);
			sliders.get(i).setSnapToTicks(true);
		}
	}

	private void addSlidersToSliderList()
	{
		sliders.add(slider12AM);
		sliders.add(slider1AM);
		sliders.add(slider2AM);
		sliders.add(slider3AM);
		sliders.add(slider4AM);
		sliders.add(slider5AM);
		sliders.add(slider6AM);
		sliders.add(slider7AM);
		sliders.add(slider8AM);
		sliders.add(slider9AM);
		sliders.add(slider10AM);
		sliders.add(slider11AM);
		sliders.add(slider12PM);
		sliders.add(slider1PM);
		sliders.add(slider2PM);
		sliders.add(slider3PM);
		sliders.add(slider4PM);
		sliders.add(slider5PM);
		sliders.add(slider6PM);
		sliders.add(slider7PM);
		sliders.add(slider8PM);
		sliders.add(slider9PM);
		sliders.add(slider10PM);
		sliders.add(slider11PM);
	}

	public JPanel getPanelMain()
	{
		return panelMain;
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
