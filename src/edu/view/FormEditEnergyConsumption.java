package edu.view;

import javax.swing.*;

public class FormEditEnergyConsumption
{
	private JPanel panelHeader;
	private JPanel panelValues;
	private JSlider slider12AM;
	private JSlider slider6AM;
	private JSlider slider12PM;
	private JSlider slider6PM;
	private JSlider slider1AM;
	private JSlider slider7AM;
	private JSlider slider1PM;
	private JSlider slider7PM;
	private JSlider slider2AM;
	private JSlider slider8AM;
	private JSlider slider2PM;
	private JSlider slider8PM;
	private JSlider slider3AM;
	private JSlider slider9AM;
	private JSlider slider3PM;
	private JSlider slider9PM;
	private JSlider slider4AM;
	private JSlider slider10AM;
	private JSlider slider4PM;
	private JSlider slider10PM;
	private JSlider slider5AM;
	private JSlider slider11AM;
	private JSlider slider5PM;
	private JSlider slider11PM;
	private JPanel panelImage;
	private JPanel panelButtons;
	private JButton btnSave;
	private JButton btnCancel;
	private JPanel panelMain;

	public JPanel getPanelMain()
	{
		return panelMain;
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
