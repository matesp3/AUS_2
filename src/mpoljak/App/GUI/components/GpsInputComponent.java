package mpoljak.App.GUI.components;

import mpoljak.App.GUI.models.GpsModel;
import mpoljak.data.GPS;

import javax.swing.*;
import java.awt.*;

public class GpsInputComponent extends JPanel {
    public static final int PREFERRED_WIDTH = 300;
    public static final int PREFERRED_HEIGHT = 100;

    private JTextField latDegTextField;
    private JTextField lonDegTextField;
    private JTextField latDirTextField;
    private JTextField lonDirTextField;

    private JLabel latDegLabel;
    private JLabel lonDegLabel;
    private JLabel latDirLabel;
    private JLabel lonDirLabel;
    private JLabel gpsComponentNameLabel;

    private boolean enabledState;

    public GpsInputComponent(String componentName, int width, int height) {
        this.gpsComponentNameLabel = new JLabel(componentName);
//      ----------------------------------------- latitude
        this.latDegLabel = new JLabel("Lat degrees: ");
        this.latDegTextField = new JTextField();
        this.latDegTextField.setPreferredSize(new Dimension(50, 22));

        this.latDirLabel = new JLabel("Lat direction: ");
        this.latDirTextField = new JTextField();
        this.latDirTextField.setPreferredSize(new Dimension(18, 22));
//      ----------------------------------------- longitude
        this.lonDegLabel = new JLabel("Lon degrees: ");
        this.lonDegTextField = new JTextField();
        this.lonDegTextField.setPreferredSize(new Dimension(50, 22));

        this.lonDirLabel = new JLabel("Lon direction: ");
        this.lonDirTextField = new JTextField();
        this.lonDirTextField.setPreferredSize(new Dimension(18, 22));

        this.createLayout(this);

//        this.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.setBorder(BorderFactory.createEtchedBorder());
//        this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.setSize(new Dimension(width, height));

        this.enabledState = true;
    }

    public void setPosition(int x, int y) {
        this.setBounds(x, y, PREFERRED_WIDTH, PREFERRED_HEIGHT);
    }

    public void setBorders(int x, int y, int width, int height) {
        this.setBounds(x, y, width, height);
    }

    public void setBgColor(Color color) {
        this.setBackground(color);
    }

//    /**
//     * Gets data, which input fields contain
//     * @return model representation of visualized information
//     */
//    public GpsModel getModel() {
//        char lat = latDegTextField.getText().charAt(0);
//        double latDeg = Double.parseDouble(latDegTextField.getText());
//        char lon = lonDegTextField.getText().charAt(0);
//        double lonDeg = Double.parseDouble(lonDegTextField.getText());
//        return new GpsModel(lat, latDeg, lon, lonDeg);
//    }
//
//    /**
//     * Fills input fields with data from model
//     * @param model contains data to visualize
//     */
//    public void setModel(GpsModel model) {
//        this.latDegTextField.setText(String.format("%.4f", model.getLatDeg()));
//        this.latDirTextField.setText(String.format("%C", model.getLatitude()));
//        this.lonDegTextField.setText(String.format("%.4f", model.getLongDeg()));
//        this.lonDirTextField.setText(String.format("%C", model.getLongitude()));
//    }
    /**
     * Gets data, which input fields contain
     * @return model representation of visualized information
     */
    public GPS getModel() {
        char lat = latDirTextField.getText().charAt(0);
        double latDeg = Double.parseDouble(latDegTextField.getText());
        char lon = lonDirTextField.getText().charAt(0);
        double lonDeg = Double.parseDouble(lonDegTextField.getText());
        return new GPS(lat, latDeg, lon, lonDeg);
    }

    /**
     * Fills input fields with data from model
     * @param model contains data to visualize
     */
    public void setModel(GPS model) {
        this.latDegTextField.setText(String.format("%.4f", model.getLatDeg()).replace(",","."));
        this.latDirTextField.setText(String.format("%C", model.getLatitude()));
        this.lonDegTextField.setText(String.format("%.4f", model.getLongDeg()).replace(",","."));
        this.lonDirTextField.setText(String.format("%C", model.getLongitude()));
    }

    /**
     * Enables/disables component's children
     * @param enabled if component's children should be enabled or not
     */
    public void setComponentEnable(boolean enabled) {
        if (this.enabledState == enabled)
            return;
        this.gpsComponentNameLabel.setEnabled(enabled);
        this.latDegLabel.setEnabled(enabled);
        this.latDegTextField.setEnabled(enabled);
        this.latDirLabel.setEnabled(enabled);
        this.latDirTextField.setEnabled(enabled);
        this.lonDegLabel.setEnabled(enabled);
        this.lonDegTextField.setEnabled(enabled);
        this.lonDirLabel.setEnabled(enabled);
        this.lonDirTextField.setEnabled(enabled);
        this.enabledState = enabled;
        if (!enabled) {
            this.latDegTextField.setText(null);
            this.latDirTextField.setText(null);
            this.lonDegTextField.setText(null);
            this.lonDirTextField.setText(null);
        }
    }

    private void createLayout(JPanel panel) {
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        Insets insets = new Insets(5, 5, 5, 5);
        GridBagConstraints con = new GridBagConstraints();
        con.insets = insets;
        con.weightx = 0.5;
        con.weighty = 0.5;
        con.anchor = GridBagConstraints.WEST;
        con.ipadx = 5;

        con.gridwidth = GridBagConstraints.REMAINDER;
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 0;
        con.gridy = 0;
        panel.add(gpsComponentNameLabel, con);

        con.gridx = 0;
        con.gridy = 1;
        con.gridwidth = 1;
        panel.add(this.latDegLabel, con);
        con.gridx = 1;
        con.gridy = 1;
        con.gridwidth = 3;
        panel.add(this.latDegTextField, con);
        con.gridx = 4;
        con.gridy = 1;
        con.gridwidth = 1;
        panel.add(this.latDirLabel, con);
        con.gridx = 5;
        con.gridy = 1;
        con.gridwidth = 1;
        panel.add(this.latDirTextField, con);

        con.gridx = 0;
        con.gridy = 2;
        con.gridwidth = 1;
        panel.add(this.lonDegLabel, con);
        con.gridx = 1;
        con.gridy = 2;
        con.gridwidth = 3;
        panel.add(this.lonDegTextField, con);
        con.gridx = 4;
        con.gridy = 2;
        con.gridwidth = 1;
        panel.add(this.lonDirLabel, con);
        con.gridx = 5;
        con.gridy = 2;
        con.gridwidth = 1;
        panel.add(this.lonDirTextField, con);
    }
}
