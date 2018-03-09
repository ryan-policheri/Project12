package edu.view;

import edu.controllers.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PVSolarFarmAddForm extends JFrame
{

    private JPanel panelMain;
    private JTextField txtName;
    private JComboBox comboBoxMonths;
    private JTextField txtConversionEfficiency;
    private JTextField txtArea;
    private JButton btnAddPhotovoltaicSolarFarm;

    public PVSolarFarmAddForm()
    {
        setTitle("WindFarmForm");
        setContentPane(panelMain);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

        btnAddPhotovoltaicSolarFarm.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    String name = txtName.getText();
                    double areaInSquareMeters = Double.parseDouble(txtArea.getText());
                    double conversionEfficiency = Double.parseDouble(txtConversionEfficiency.getText());
                    int currentMonth = convertMonthTextToInt(comboBoxMonths.getSelectedItem().toString());

                    Controller.addPVSolarFarm(name, areaInSquareMeters, conversionEfficiency, currentMonth);
                    GRESBIMB.updatePVSolarList();
                    dispose();
                }
                catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(null, "Something was entered incorrectly. Try again.");
                }
            }
        });
    }

    private static int convertMonthTextToInt(String monthAsText)
    {
        int monthAsInt = -1;

        switch(monthAsText)
        {
            case "January":
                monthAsInt = 1;
                break;

            case "February":
                monthAsInt = 2;
                break;

            case "March":
                monthAsInt = 3;
                break;

            case "April":
                monthAsInt = 4;
                break;

            case "May":
                monthAsInt = 5;
                break;

            case "June":
                monthAsInt = 6;
                break;

            case "July":
                monthAsInt = 7;
                break;

            case "August":
                monthAsInt = 8;
                break;

            case "September":
                monthAsInt = 9;
                break;

            case "October":
                monthAsInt = 10;
                break;

            case "November":
                monthAsInt = 11;;
                break;

            case "December":
                monthAsInt = 12;
                break;
        }

        return monthAsInt;
    }
}
