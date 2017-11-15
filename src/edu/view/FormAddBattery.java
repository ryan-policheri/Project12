package edu.view;

import edu.controllers.Controller;
import oracle.jvm.hotspot.jfr.JFR;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class FormAddBattery
{
	private static JFrame frameMain;
	private JPanel panelMain;
	private JPanel panelAddBattery;
	private JLabel lblName;
	private JLabel lblMass;
	private JLabel lblHeight;
	private JTextField txtName;
	private JTextField txtMass;
	private JTextField txtHeight;
	private JButton btnAdd;
	private JButton btnCancel;

	public FormAddBattery()
	{
		btnAdd.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkValidInputs()) addBattery();
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
	}

	private void addBattery()
	{
		Controller.addBattery(txtName.getText(), Double.parseDouble(txtMass.getText()),
				Double.parseDouble(txtHeight.getText()));
		JOptionPane.showMessageDialog(null, txtName.getText() + " added successfully.");
	}

	private boolean checkValidInputs()
	{
		return true;
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
