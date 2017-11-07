package edu.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewTestForm
{
	public JPanel panelMain;
	private JButton btnBack;

	private static JFrame frame = new JFrame("Energy");

	public NewTestForm()
	{
		btnBack.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//TODO: find a way to switch between the main TestForm and the NewTestForm
				// frame.setContentPane(new TestForm().panelMain);
				// frame.pack();
			}
		});
	}

	public static void main(String[] args)
	{
		frame.setContentPane(new NewTestForm().panelMain);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
