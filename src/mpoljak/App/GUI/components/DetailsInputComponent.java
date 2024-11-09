package mpoljak.App.GUI.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DetailsInputComponent extends JPanel {
    private JRadioButton optionParcel;
    private JRadioButton optionProperty;


    private JTextField numberInput;
    private JTextField descInput;

    private JLabel selectedDetailLabel;
    private JLabel numberSpecLabel;
    private JLabel descLabel;

    private JButton executeBtn;

    private class RadioButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton btn = (JRadioButton) e.getSource();
            if (btn == optionParcel) {
                selectedDetailLabel.setText("Parcel's details:");
                numberSpecLabel.setText("Parcel's number:");
            }
            else if (btn == optionProperty) {
                selectedDetailLabel.setText("Property's details:");
                numberSpecLabel.setText("Property's number:");
            }
        }
    }

    public DetailsInputComponent(int prefWidth, int prefHeight, Color bgColor) {
        this.setPreferredSize(new Dimension(prefWidth, prefHeight));
        this.setBackground(bgColor);
        int inputHeight = 22;
        RadioButtonActionListener rbActionListener = new RadioButtonActionListener();
        this.optionParcel = new JRadioButton("Parcel");
        this.optionParcel.setBackground(bgColor);
        this.optionParcel.addActionListener(rbActionListener);

        this.optionProperty = new JRadioButton("Property");
        this.optionProperty.setBackground(bgColor);
        this.optionProperty.addActionListener(rbActionListener);

        // choose type of location
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(this.optionParcel);;
        btnGroup.add(this.optionProperty);

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagConstraints con = new GridBagConstraints();
        con.weighty = 0.5;
        con.weightx = 0.5;

        con.gridx = 0;
        con.gridy = 0;
        this.add(this.optionParcel, con);
        this.optionParcel.setSelected(true);

        con.gridx = 1;
        con.gridy = 0;
        this.add(this.optionProperty, con);

        con.gridwidth = 2;
        con.gridx = 0;
        con.gridy = 1;
        con.anchor = GridBagConstraints.WEST;
        this.selectedDetailLabel = new JLabel("Parcel's details:");
        this.add(this.selectedDetailLabel, con);

        con.gridwidth = 1;
        con.gridx = 0;
        con.gridy = 2;
        this.numberSpecLabel = new JLabel("Parcel's number:");
        this.add(numberSpecLabel, con);

        con.gridx = 1;
        con.gridy = 2;
        this.numberInput = new JTextField();
        this.numberInput.setPreferredSize(new Dimension(60, inputHeight));
        this.add(numberInput, con);

        con.gridx = 0;
        con.gridy = 3;
        this.descLabel = new JLabel("Description:");
        this.add(descLabel, con);

        con.anchor = GridBagConstraints.CENTER;
        con.gridwidth = 2;
        con.gridx = 0;
        con.gridy = 4;
        this.descInput = new JTextField();
        this.descInput.setPreferredSize(new Dimension(prefWidth - 10, inputHeight));
        this.add(descInput, con);

        con.anchor = GridBagConstraints.EAST;
        con.gridwidth = 1;
        con.gridx = 1;
        con.gridy = 5;
        this.executeBtn = new JButton("Execute");
        this.add(executeBtn, con);
    }

}
