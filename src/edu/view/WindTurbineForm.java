package edu.view;

import edu.controllers.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindTurbineForm extends JFrame
{
    private JPanel panelMain;
    private JTextField txtName;
    private JTextField txtCapInMW;
    private JTextField txtNumberOfTurbines;
    private JButton btnAddTurbineType;

    public WindTurbineForm()
    {
        setTitle("WindTurbineForm");
        setContentPane(getPanelMain());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

        btnAddTurbineType.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               if(txtName.getText().isEmpty() == false && txtCapInMW.getText().isEmpty() == false && txtNumberOfTurbines.getText().isEmpty() == false)
               {
                   try
                   {
                       String turbineName = txtName.getText();
                       double maxCapacityInMW = Double.parseDouble(txtCapInMW.getText());
                       int numberOfTurbines = Integer.parseInt(txtNumberOfTurbines.getText());
                       int indexOfNewFarm = WindFarmAddForm.getIndexOfNewFarm();

                       Controller.addWindTurbineTypesToWindFarm(indexOfNewFarm, turbineName, maxCapacityInMW, numberOfTurbines);
                       WindFarmAddForm.uniqueTurbinesAndCount.addElement("Model Name: " + turbineName + " Number of Turbines: " + numberOfTurbines);

                       dispose();

                   }
                   catch(Exception el)
                   {
                       JOptionPane.showMessageDialog(null, "Something was entered incorrectly.");
                   }
               }
               else
               {
                   JOptionPane.showMessageDialog(null, "Please fill all the fields.");
               }

            }
        });
    }

    //region Getters/Setters
    public JPanel getPanelMain()
    {
        return panelMain;
    }
    //endregion
}
