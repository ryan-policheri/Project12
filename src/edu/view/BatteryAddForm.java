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

public class BatteryAddForm extends JFrame
{
	//region Initialize swing elements
	private JPanel panelMain;

	//region Battery Type panel
	private JPanel panelBatteryType;

	private JList listBatteryTypes;
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
	private JRadioButton rbRotBearingModern;
	//endregion
	//endregion

	//region Buttons panel
	private JPanel panelButtons;

	private JButton btnAdd;
	//endregion

	//region Info panel
	private JPanel panelInfo;
	//endregion
	//endregion

	//region Initialize attributes
	private static DefaultListModel<String> model = new DefaultListModel<>();

	private static ButtonGroup rbGroupRotMaterial = new ButtonGroup();
	private static ButtonGroup rbGroupRotBearing = new ButtonGroup();

	private final int gravitationalBatterySelectedIndex = 0;
	private final int rotationalBatterySelectedIndex = 1;
	//endregion

	public BatteryAddForm()
	{
		setTitle("Add Battery Form");
		setContentPane(panelMain);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);

		// Add batteries to the model/list, then change the selected index to the first item
		model.clear();
		model.addElement("Gravitational Battery");
		model.addElement("Rotational Battery");
		listBatteryTypes.setModel(model);
		listBatteryTypes.setSelectedIndex(0);

		// Add radio buttons to their respective groups and make the first radio button in each group selected
		rbGroupRotMaterial.add(rbRotMaterialTitanium);
		rbGroupRotMaterial.add(rbRotMaterialCarbonFiber);
		rbGroupRotMaterial.add(rbRotMaterialSteel);
		rbGroupRotMaterial.add(rbRotMaterialAluminum);

		rbGroupRotBearing.add(rbRotBearingMechanical);
		rbGroupRotBearing.add(rbRotBearingMagnetic);
		rbGroupRotBearing.add(rbRotBearingModern);

		rbRotMaterialTitanium.setSelected(true);
		rbRotBearingMechanical.setSelected(true);

		// Set reasonable default values for the batteries
		String defaultGravMass = "100";
		String defaultGravHeight = "10";
		txtGravMass.setText(defaultGravMass);
		txtGravHeight.setText(defaultGravHeight);

		String defaultRotMass = "100";
		String defaultRotRadius = ".5";
		txtRotMass.setText(defaultRotMass);
		txtRotRadius.setText(defaultRotRadius);

		//region Button click listeners
		btnAdd.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					switch (listBatteryTypes.getSelectedIndex())
					{
						case gravitationalBatterySelectedIndex:
							addGravitationalBattery();
							break;
						case rotationalBatterySelectedIndex:
							String flywheelMaterial = findSelectedFlywheelMaterial();
							String flywheelBearing = findSelectedFlywheelBearing();
							addRotationalBattery(flywheelMaterial, flywheelBearing);
							break;
					}

					dispose();
				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null,"Something was entered incorrectly. Try again.");
				}
			}
		});
		//endregion

		//region Switch selection listeners
		listBatteryTypes.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				switch (listBatteryTypes.getSelectedIndex())
				{
					case 0:
						switchAttributesPanelTo(panelGravitationalBatteries);
						lblBatteryType.setText("Gravitational Battery");
						break;
					case 1:
						switchAttributesPanelTo(panelRotationalBatteries);
						lblBatteryType.setText("Rotational Battery");
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

	//region Add batteries to the Controller's battery grid
	private void addGravitationalBattery()
	{
		String batteryName = txtGravName.getText();
		double massInKilograms = Double.parseDouble(txtGravMass.getText());
		double maxHeightInMeters = Double.parseDouble(txtGravHeight.getText());

		GravitationalBattery battery = new GravitationalBattery(batteryName, massInKilograms, maxHeightInMeters);
		Controller.addGravitationalBattery(battery);
		GRESBIMB.updateBatteryListModel();

		JOptionPane.showMessageDialog(null, batteryName + " added successfully.");
	}

	private void addRotationalBattery(String material, String bearing)
	{
		String batteryName = txtRotName.getText();
		double massInKilograms = Double.parseDouble(txtRotMass.getText());
		double radiusInMeters = Double.parseDouble(txtRotRadius.getText());
		FlywheelMaterial flywheelMaterial = new FlywheelMaterial(material);
		FlywheelBearing flywheelBearing = new FlywheelBearing(bearing);

		RotationalBattery battery = new RotationalBattery(batteryName, massInKilograms, radiusInMeters, flywheelMaterial, flywheelBearing);
		Controller.addRotationalBattery(battery);
		GRESBIMB.updateBatteryListModel();

		JOptionPane.showMessageDialog(null, batteryName + " added successfully.");
	}
	//endregion

	//region Find selected radio buttons
	private String findSelectedFlywheelMaterial()
	{
		String materialName;
		if (rbRotMaterialTitanium.isSelected())
		{
			materialName = "Titanium";
		}
		else if (rbRotMaterialCarbonFiber.isSelected())
		{
			materialName = "Carbon Fiber";
		}
		else if (rbRotMaterialSteel.isSelected())
		{
			materialName = "Steel";
		}
		else
		{
			materialName = "Aluminum";
		}

		return materialName;
	}

	private String findSelectedFlywheelBearing()
	{
		String bearingName;

		if (rbRotBearingMechanical.isSelected())
		{
			bearingName = "Mechanical";
		}
		else if (rbRotBearingMagnetic.isSelected())
		{
			bearingName = "Magnetic";
		}
		else
		{
			bearingName = "Modern";
		}

		return bearingName;
	}
	//endregion

}
