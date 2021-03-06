package edu.view;

import edu.controllers.Controller;
import edu.view.misc.SliderListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class WindTierForm extends JFrame
{
	//region Initialize swing elements

	protected JPanel panelMain;

	protected JPanel panelHeader;
	protected JPanel panelSliderValues;
	protected JLabel lblTitle;

	//region Sliders
	protected JSlider slider12AM;
	protected JSlider slider1AM;
	protected JSlider slider2AM;
	protected JSlider slider3AM;
	protected JSlider slider4AM;
	protected JSlider slider5AM;
	protected JSlider slider6AM;
	protected JSlider slider7AM;
	protected JSlider slider8AM;
	protected JSlider slider9AM;
	protected JSlider slider10AM;
	protected JSlider slider11AM;
	protected JSlider slider12PM;
	protected JSlider slider1PM;
	protected JSlider slider2PM;
	protected JSlider slider3PM;
	protected JSlider slider4PM;
	protected JSlider slider5PM;
	protected JSlider slider6PM;
	protected JSlider slider7PM;
	protected JSlider slider8PM;
	protected JSlider slider9PM;
	protected JSlider slider10PM;
	protected JSlider slider11PM;
	//endregion

	protected JPanel panelWindTierGraph;
	//endregion

	//region Buttons panel
	protected JPanel panelButtons;
	protected JButton btnOk;
	//endregion
	//endregion

	//region Initialize attributes

	protected static ArrayList<JSlider> sliders = new ArrayList<>();
	protected static final int NUM_OF_TIERS = Controller.getNumOfTiers();
	protected static final int MAJOR_TICK_SPACING = Controller.getMajorTickSpacing();
	protected static int[] windTiers = WindFarmAddForm.getWindTiers();
	//endregion

	public WindTierForm()
	{
		setTitle("WindTierForm");
		setContentPane(getPanelMain());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);

		// Set default settings for sliders
		addSlidersToSliderList();
		setSliderDefaultSettings();
		setDefaultPanelSliderValues();

		btnOk.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				for(int i = 0; i < windTiers.length; i++)
				{
					windTiers[i] = sliders.get(i).getValue();
				}

				WindFarmAddForm.setWindTiers(windTiers);

				dispose();
			}

		});
	}

	private void addSlidersToSliderList()
	{
		sliders.clear();

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

	private void setSliderDefaultSettings()
	{
		for (JSlider slider : sliders)
		{
			slider.setMaximum(NUM_OF_TIERS);
			slider.setMajorTickSpacing(MAJOR_TICK_SPACING);
			slider.setSnapToTicks(true);

			// Add listeners
			slider.addChangeListener(new SliderListener(this));
		}
	}

	private void setDefaultPanelSliderValues()
	{
		for(int i = 0; i < windTiers.length; i++)
		{
			sliders.get(i).setValue(windTiers[i]);
		}
	}

	public void updateGraphs()
	{
		int[] modifiedEnergyProductionTiers = new int[24];

		for (int i = 0; i < modifiedEnergyProductionTiers.length; i++)
		{
			modifiedEnergyProductionTiers[i] = sliders.get(i).getValue();
		}

		addJFreeChartToJPanel(this.panelWindTierGraph, modifiedEnergyProductionTiers, false);
	}

	protected void addJFreeChartToJPanel(JPanel panel, int[] windTiers, boolean isPrevious)
	{
		XYSeries series = new XYSeries("XYGraph");

		// Add city energyConsumptionTiers data to the series
		for (int i = 0; i < windTiers.length; i++)
		{
			series.add(i, windTiers[i]);
		}

		// Add the series to your data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);

		// Generate the graph
		String title = "Wind Tiers by Hour";

		JFreeChart chart = ChartFactory.createXYLineChart(title,"Hour","Wind Tier", dataset,
				PlotOrientation.VERTICAL, false, false, false);

		// Make data points visible
		XYPlot chartPlot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chartPlot.getRenderer();
		renderer.setBaseShapesVisible(true);

		// Set the range
		XYPlot xyPlot = (XYPlot) chart.getPlot();
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		// Domain
		NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
		domain.setRange(0, 23.5);
		domain.setTickUnit(new NumberTickUnit(1));
		domain.setTickLabelsVisible(true);
		// Range
		NumberAxis range = (NumberAxis) chartPlot.getRangeAxis();
		range.setRange(0, NUM_OF_TIERS + .5);
		range.setTickUnit(new NumberTickUnit(MAJOR_TICK_SPACING));

		// Add it the graph to the JPanel
		ChartPanel CP = new ChartPanel(chart);
		panel.add(CP, BorderLayout.CENTER);
		panel.validate();
	}

	//region Getters/Setters
	public JPanel getPanelMain()
	{
		return panelMain;
	}
	//endregion

}
