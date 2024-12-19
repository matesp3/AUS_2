package mpoljak.App.GUI.components;

import mpoljak.App.GUI.GeoAppFrame;
import mpoljak.App.GUI.models.GeoInfoModel;

import javax.swing.*;
import java.awt.*;

public class DetailsInputComponent extends JPanel {
    private JTextField numberInput;
    private JTextField descInput;

    private JLabel selectedDetailLabel;
    private JLabel numberSpecLabel;
    private JLabel descLabel;

    private char currentType;
    private boolean enabledState;

    public DetailsInputComponent(int prefWidth, int prefHeight, Color bgColor) {
        this.setSize(new Dimension(prefWidth, prefHeight));
        this.setBackground(bgColor);
        int inputHeight = 22;

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagConstraints con = new GridBagConstraints();
        con.weighty = 0.5;
        con.weightx = 0.5;
        con.insets = new Insets(0, 5, 0, 3);

        con.gridwidth = 2;
        con.gridx = 0;
        con.gridy = 0;
        this.selectedDetailLabel = new JLabel("Parcel's details");
        this.add(this.selectedDetailLabel, con);

        con.anchor = GridBagConstraints.WEST;
        con.gridwidth = 1;
        con.gridx = 0;
        con.gridy = 1;
        this.numberSpecLabel = new JLabel("Parcel's number:");
        this.add(numberSpecLabel, con);

        con.gridx = 1;
        con.gridy = 1;
        this.numberInput = new JTextField();
        this.numberInput.setPreferredSize(new Dimension(80, inputHeight));
        this.add(numberInput, con);

        con.gridx = 0;
        con.gridy = 2;
        this.descLabel = new JLabel("Description:");
        this.add(descLabel, con);

        con.anchor = GridBagConstraints.CENTER;
        con.gridwidth = 2;
        con.gridx = 0;
        con.gridy = 3;
        this.descInput = new JTextField();
        this.descInput.setPreferredSize(new Dimension(prefWidth - 20, inputHeight));
        this.add(descInput, con);

        this.enabledState = true;
        this.currentType = GeoAppFrame.TYPE_PARCEL;
    }

    /**
     * Gets data, which input fields contain
     * @return model representation of visualized information
     */
    public GeoInfoModel getModel() {
        try {
            int nr = Integer.parseInt(this.numberInput.getText());
            String desc = this.descInput.getText();
            char type = (this.currentType == GeoAppFrame.TYPE_PARCEL) ? GeoAppFrame.TYPE_PARCEL : GeoAppFrame.TYPE_PROPERTY;
            return new GeoInfoModel(type, nr, desc);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Fills input fields with data from model
     * @param model contains data to visualize
     */
    public void setModel(GeoInfoModel model) {
        if (model == null)
            return;
        if (model.getGeoType() == GeoAppFrame.TYPE_PROPERTY)
            this.setDetailsType(GeoAppFrame.TYPE_PROPERTY);
        else
            this.setDetailsType(GeoAppFrame.TYPE_PARCEL);
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
//        this.selectedDetailLabel.setEnabled(enabled);
//        this.numberSpecLabel.setEnabled(enabled);
        this.numberInput.setEnabled(enabled);
//        this.descLabel.setEnabled(enabled);
        this.descInput.setEnabled(enabled);
        this.enabledState = enabled;
        if (!enabled) {
            this.numberInput.setText(null);
            this.descInput.setText(null);
        }
    }

    /**
     * Updates labels description and clears input fields, if change of type occurs
     * @param type can be set as <code>GeoAppFrame.TYPE_PROPERTY</code> or <code>GeoAppFrame.TYPE_PARCEL</code>
     */
    public void setDetailsType(char type) {
        if (this.currentType == type)
            return;
        if (type == GeoAppFrame.TYPE_PARCEL) {
            selectedDetailLabel.setText("Parcel's details:");
            numberSpecLabel.setText("Parcel's number:");
        }
        else if (type == GeoAppFrame.TYPE_PROPERTY) {
            selectedDetailLabel.setText("Property's details:");
            numberSpecLabel.setText("Property's number:");
        }
        else
            return;
        numberInput.setText(null);
        descInput.setText(null);
        this.currentType = type;
    }


}
