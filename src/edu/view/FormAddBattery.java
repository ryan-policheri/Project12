package edu.view;

import edu.controllers.Controller;
import edu.model.batteries.FlywheelBearing;
import edu.model.batteries.FlywheelMaterial;
import edu.model.batteries.GravitationalBattery;
import edu.model.batteries.RotationalBattery;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormAddBattery
{
	//region Initialize swing elements
	private static JFrame frameMain;
	private JPanel panelMain;

	//region Batteries panel
	private JPanel panelBatteries;

	private JList listBatteryTypes;
	private JPanel panelCityImage;
	private JLabel lblBatteryType;
	//endregion

	//region Attributes panel
	private JPanel panelAttributes;

	//region Gravitational batteries panel
	private JPanel panelGravitationalBatteries;

	private JTextField txtGravName;
	private JTextField txtGravMass;
	private JTextField txtGravHeight;
	//endregion

	//region Rotational batteries panel
	private JPanel panelRotationalBatteries;

	private JTextField txtRotName;
	private JTextField txtRotMass;
	private JTextField txtRotRadius;
	private JRadioButton rbRotMaterialTitanium;
	private JRadioButton rbRotMaterialCarbonFiber;
	private JRadioButton rbRotMaterialSteel;
	private JRadioButton rbRotMaterialAluminum;
	private JRadioButton rbRotBearingMechanical;
	private JRadioButton rbRotBearingMagnetic;
	private JRadioButton rbRotBearingSuper;
	//endregion
	//endregion

	//region Buttons panel
	private JPanel panelButtons;

	private JButton btnAdd;
	private JButton btnCancel;
	//endregion

	//region Info panel
	private JPanel panelInfo;
	//endregion

	//region Initialize attributes
	private static DefaultListModel<String> model = new DefaultListModel<>();

	private final int gravitationalBatterySelectedIndex = 0;
	private final int rotationalBatterySelectedIndex = 1;
	//endregion

	public FormAddBattery()
	{
		//TODO: make panelInfo provide info based on what's added in the text fields (ex: total storage capacity of
		// battery)

		// add batteries to the model/list, then change the selected index to the first item
		model.clear();
		model.addElement("Gravitational Battery");
		model.addElement("Rotational Battery");
		listBatteryTypes.setModel(model);
		listBatteryTypes.setSelectedIndex(0);

		//region Listeners
		btnAdd.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkValidInputs() && listBatteryTypes.getSelectedIndex() == gravitationalBatterySelectedIndex)
				{
					addGravitationalBattery();
				}
				else if (checkValidInputs() && listBatteryTypes.getSelectedIndex() == rotationalBatterySelectedIndex)
				{
					addRotationalBattery();
				}
			}
		});
		btnCancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Controller.getGrid().displayGrid();
				frameMain.setVisible(false);
			}
		});
		// switch the attributes panel based on the selected index
		listBatteryTypes.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				switch (listBatteryTypes.getSelectedIndex())
				{
					case 0:
						switchAttributesPanelTo(panelGravitationalBatteries);
						break;
					case 1:
						switchAttributesPanelTo(panelRotationalBatteries);
						break;
					default:
						switchAttributesPanelTo(panelGravitationalBatteries);
				}
			}
		});
		//endregion
	}

	private void switchAttributesPanelTo(JPanel panel)
	{
		panelAttributes.removeAll();
		panelAttributes.add(panel);
		panelAttributes.repaint();
		panelAttributes.revalidate();
	}

	private void addGravitationalBattery()
	{
		GravitationalBattery battery = new GravitationalBattery(txtGravName.getText(), Double.parseDouble(txtGravMass.getText()),
				Double.parseDouble(txtGravHeight.getText()));
		Controller.addGravitationalBattery(battery);

		JOptionPane.showMessageDialog(null, txtGravName.getText() + " added successfully.");
	}

	private boolean checkValidInputs()
	{
		return true;
	}

	//TODO: continue updating this class along with the addGravitationalBattery form. After that, edit the FormEdit forms
	private void addRotationalBattery()
	{
		String materialName;
		double materialDensity;
		double materialTensileStress;
		String bearingName;
		double bearingLoss;

		//TODO: put these in their own Flywheel classes
		//region Checking which materials are selected
		if (rbRotMaterialTitanium.isSelected())
		{
			materialName = "Titanium";
			materialDensity = 4506;
			materialTensileStress = 8.8 * java.lang.Math.pow(10, 8);
		}
		else if (rbRotMaterialCarbonFiber.isSelected())
		{
			materialName = "Carbon Fiber";
			materialDensity = 1799;
			materialTensileStress = 4000000000.0;
		}
		else if (rbRotMaterialSteel.isSelected())
		{
			materialName = "Steel";
			materialDensity = 8050;
			materialTensileStress = 6.9 * java.lang.Math.pow(10, 8);
		}
		else
		{
			materialName = "Aluminum";
			materialDensity = 2700;
			materialTensileStress = 5 * java.lang.Math.pow(10, 8);
		}

		if (rbRotBearingMechanical.isSelected())
		{
			bearingName = "Mechanical";
			bearingLoss = 3.472222222 * java.lang.Math.pow(10, -5);
		}
		else if (rbRotBearingMagnetic.isSelected())
		{
			bearingName = "Magnetic";
			bearingLoss = 4.166666667 * java.lang.Math.pow(10, -6);
		}
		else
		{
			bearingName = "Super";
			bearingLoss = 6.944444444 * java.lang.Math.pow(10, -8);
		}
		//endregion

		FlywheelMaterial material = new FlywheelMaterial(materialName, materialDensity, materialTensileStress);
		FlywheelBearing bearing = new FlywheelBearing(bearingName, bearingLoss);

		RotationalBattery battery = new RotationalBattery(txtRotName.getText(), Double.parseDouble(txtRotMass.getText
				()), Double.parseDouble(txtRotRadius.getText()), material, bearing);

		Controller.addRotationalBattery(battery);

		JOptionPane.showMessageDialog(null, txtRotName.getText() + " added successfully.");
	}

	//region Getters/Setters
	public JPanel getPanelMain()
	{
		return panelMain;
	}
	//endregion

	public static void main(String[] args)
	{
		frameMain = new JFrame("FormAddBattery");
		frameMain.setContentPane(new FormAddBattery().panelMain);
		frameMain.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameMain.pack();
		frameMain.setVisible(true);
	}
}
