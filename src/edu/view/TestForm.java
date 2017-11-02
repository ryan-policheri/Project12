package edu.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import edu.model.batteries.BatteryGrid;
import edu.model.batteries.GravitationalBattery;

public class TestForm extends JFrame
{
	// View
	private JPanel panelMain;
	private JPanel panelLeft;
	private JPanel panelRight;
	private JPanel panelBottom;
	private JButton btnAdd;
	private JButton btnEdit;
	private JButton btnDelete;
	private JLabel lblName;
	private JLabel lblHeight;
	private JLabel lblMass;
	private JTextField txtName;
	private JTextField txtMass;
	private JTextField txtHeight;

	// Model
	private BatteryGrid grid = new BatteryGrid();

	public TestForm()
	{
		super("Main Panel");

		// Listeners
		btnAdd.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Add the gravitational battery to the grid
				GravitationalBattery battery = new GravitationalBattery(txtName.getText(),
						Double.parseDouble(txtMass.getText()), Double.parseDouble(txtHeight.getText()));
				grid.addGravitationalBattery(battery);
				System.out.println("-----------------");

				grid.displayGrid();
				JOptionPane.showMessageDialog(null, txtName.getText() + " added to grid.");
				JOptionPane.showMessageDialog(null, "Check console");
			}
		});
	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("TestForm");
		frame.setContentPane(new TestForm().panelMain);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
