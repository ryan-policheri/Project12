package edu.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import edu.model.batteries.BatteryGrid;
import edu.model.batteries.GravitationalBattery;
import edu.controllers.Controller;

public class TestForm extends JFrame
{
	//region Form items
	private JPanel panelMain;
	private JPanel panelLeft;
	private JPanel panelRight;
	private JPanel panelBottom;
	private JButton btnBattery;
	private JButton btnShowGrid;
	private JButton btnEnergy;
	private JLabel lblName;
	private JLabel lblHeight;
	private JLabel lblMass;
	private JTextField txtName;
	private JTextField txtMass;
	private JTextField txtHeight;

	private static JFrame frame = new JFrame("TestForm");
	private static JFrame newTestForm = new JFrame("NewTestForm");
	//endregion

	public TestForm()
	{
		super("Main Panel");

		//region Listeners
		btnBattery.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Add the gravitational battery to the grid
				Controller.addBattery(txtName.getText(), Double.parseDouble(txtMass.getText()), Double.parseDouble
						(txtHeight.getText()));

				//TODO: Add update() functionality
			}
		});

		btnShowGrid.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{

			}
		});
		//endregion
		btnEnergy.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				frame.setContentPane(new NewTestForm().panelMain);
				frame.pack();
			}
		});
	}

	//region Methods
	private static void update()
	{

	}
	//endregion

	public static void main(String[] args)
	{
		// TestForm JFrame
		frame.setContentPane(new TestForm().panelMain);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		// Other JFrames
//		newTestForm.setContentPane(new NewTestForm().panelMain);
//		newTestForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		newTestForm.pack();
//		newTestForm.setVisible(true);
	}
}
