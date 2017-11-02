package com.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private JTextField textHeight;
	private JButton btnMSG;

	// Model

	public TestForm()
	{
		super("Main Panel");

		btnMSG.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(null, "Hello World!");
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
