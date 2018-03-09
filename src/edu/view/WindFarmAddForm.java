package edu.view;

import edu.controllers.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindFarmAddForm extends JFrame
{
    private JPanel panelMain;
    private JPanel panelAddFarm;
    private JPanel panelAddTurbines;
    private JTextField txtWindFarmName;
    private JButton btnEditWindTiers;
    private JButton btnNext;
    private JList listTurbineTypes;
    private JButton btnAddTurbineType;
    private JButton btnFinish;

    private static int[] windTiers = new int[24];
    private static int indexOfNewFarm;
    protected static DefaultListModel<String> uniqueTurbinesAndCount = new DefaultListModel<String>();

    public WindFarmAddForm()
    {
        //set the initial wind tiers equal to rhe default of the selected city
        for(int i = 0; i < windTiers.length; i++)
        {
            windTiers[i] = Controller.getSelectedCity().getDefaultWindTiersByHour()[i];
        }

        setTitle("WindFarmForm");
        setContentPane(getPanelMain());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

        panelMain.removeAll();
        panelMain.add(panelAddFarm);
        panelMain.repaint();
        panelMain.revalidate();

        listTurbineTypes.setModel(uniqueTurbinesAndCount);

        btnEditWindTiers.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new WindTierForm();
            }
        });

        btnNext.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    if (txtWindFarmName.getText().isEmpty() == false)
                    {
                        Controller.addWindFarm(txtWindFarmName.getText(), windTiers);
                        indexOfNewFarm = Controller.getWindFarms().size() - 1;

                        panelMain.removeAll();
                        panelMain.add(panelAddTurbines);
                        panelMain.repaint();
                        panelMain.revalidate();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Enter a name for the farm.");
                    }
                }
                catch (Exception el)
                {
                    JOptionPane.showMessageDialog(null, "Something was entered incorrectly. Try again.");
                }

            }
        });

        btnAddTurbineType.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new WindTurbineForm();
            }
        });

        btnFinish.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(uniqueTurbinesAndCount.size() > 0)
                {
                    GRESBIMB.updateWindFarmList();
                    dispose();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "There needs to be at least one wind turbine type to finish farm");
                }
            }
        });
    }

    //region Getters/Setters
    public static void setWindTiers(int[] newWindTiers)
    {
        windTiers = newWindTiers;
    }

    public static int[] getWindTiers()
    {
        return windTiers;
    }

    protected static int getIndexOfNewFarm()
    {
        return indexOfNewFarm;
    }

    public JPanel getPanelMain()
    {
        return panelMain;
    }
    //endregion
}
