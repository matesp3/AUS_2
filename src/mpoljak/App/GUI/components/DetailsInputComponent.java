package mpoljak.App.GUI.components;

import mpoljak.App.GUI.models.GeoInfoModel;

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

    private boolean enabledState;

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
            numberInput.setText(null);
            descInput.setText(null);
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
        btnGroup.add(this.optionParcel);
        btnGroup.add(this.optionProperty);

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagConstraints con = new GridBagConstraints();
        con.weighty = 0.5;
        con.weightx = 0.5;
        con.insets = new Insets(0, 5, 0, 3);

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
        this.numberInput.setPreferredSize(new Dimension(80, inputHeight));
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

        this.enabledState = true;
    }

    /**
     * Gets data, which input fields contain
     * @return model representation of visualized information
     */
    public GeoInfoModel getModel() {
        int nr =  Integer.parseInt(this.numberInput.getText());
        String desc = this.descInput.getText();
        char type = this.optionParcel.isSelected() ? 'L' : 'Y'; // last character of parceL/propertY
        return new GeoInfoModel(type, nr, desc);
    }

    /**
     * Fills input fields with data from model
     * @param model contains data to visualize
     */
    public void setModel(GeoInfoModel model) {
        if (model == null)
            return;
        if (model.getGeoType() == 'Y')
            this.optionProperty.doClick();
        else
            this.optionParcel.doClick();
        this.numberInput.setText(String.valueOf(model.getNumber()));
        this.descInput.setText(model.getDescription());
    }

    /**
     * Enables/disables component's children
     * @param enabled if component's children should be enabled or not
     */
    public void setComponentEnable(boolean enabled) {
        if (this.enabledState == enabled)
            return;
        this.optionParcel.setEnabled(enabled);
        this.optionProperty.setEnabled(enabled);
        this.selectedDetailLabel.setEnabled(enabled);
        this.numberSpecLabel.setEnabled(enabled);
        this.numberInput.setEnabled(enabled);
        this.descLabel.setEnabled(enabled);
        this.descInput.setEnabled(enabled);
        this.enabledState = enabled;
    }

}
