package mpoljak.App.GUI.components;

import javax.swing.*;
import java.awt.*;

public class GpsInputComponentOld extends JPanel {
    public static final int COMPONENT_WIDTH = 300;
    public static final int COMPONENT_HEIGHT = 100;

    private JPanel compositePanel;
    private JPanel upperPanel;
    private JPanel lowerPanel;

    private JTextField latDegTextField;
    private JTextField lonDegTextField;
    private JTextField latDirTextField;
    private JTextField lonDirTextField;

    private JLabel latDegLabel;
    private JLabel lonDegLabel;
    private JLabel latDirLabel;
    private JLabel lonDirLabel;
    private JLabel gpsComponentNameLabel;


    public GpsInputComponentOld(String componentName, JFrame frame, int posX, int posY) {
//      ----------------------------------------- latitude
        this.upperPanel = new JPanel();
        this.upperPanel.setLayout(new BoxLayout(this.upperPanel, BoxLayout.X_AXIS));

        this.latDegLabel = new JLabel("Lat degrees: ");
        this.latDegTextField = new JTextField();
        this.upperPanel.add(Box.createHorizontalStrut(5));
        this.upperPanel.add(this.latDegLabel);
        this.upperPanel.add(Box.createHorizontalStrut(6));
        this.upperPanel.add(this.latDegTextField);
        this.upperPanel.add(Box.createHorizontalStrut(12));

        this.upperPanel.add(Box.createHorizontalStrut(5));
        this.latDirLabel = new JLabel("Lat direction: ");
        this.latDirTextField = new JTextField();
        this.upperPanel.add(this.latDirLabel);
        this.upperPanel.add(Box.createHorizontalStrut(6));
        this.upperPanel.add(this.latDirTextField);
        this.upperPanel.add(Box.createHorizontalStrut(5));

        this.upperPanel.setBounds(0, 0, GpsInputComponentOld.COMPONENT_WIDTH,
                GpsInputComponentOld.COMPONENT_HEIGHT / 2);
//        this.upperPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
//      ----------------------------------------- longitude
        this.lowerPanel = new JPanel();
        this.lowerPanel.setLayout(new BoxLayout(this.lowerPanel, BoxLayout.X_AXIS));

        this.lonDegLabel = new JLabel("Lon degrees: ");
        this.lonDegTextField = new JTextField();
        this.lowerPanel.add(Box.createHorizontalStrut(5));
        this.lowerPanel.add(this.lonDegLabel);
        this.lowerPanel.add(Box.createHorizontalStrut(6));
        this.lowerPanel.add(this.lonDegTextField);
        this.lowerPanel.add(Box.createHorizontalStrut(12));

        this.lowerPanel.add(Box.createHorizontalStrut(5));
        this.lonDirLabel = new JLabel("Lon direction: ");
        this.lonDirTextField = new JTextField();
        this.lowerPanel.add(this.lonDirLabel);
        this.lowerPanel.add(Box.createHorizontalStrut(6));
        this.lowerPanel.add(this.lonDirTextField);
        this.lowerPanel.add(Box.createHorizontalStrut(5));

        this.lowerPanel.setBounds(0, GpsInputComponentOld.COMPONENT_HEIGHT / 2,
                GpsInputComponentOld.COMPONENT_WIDTH, GpsInputComponentOld.COMPONENT_HEIGHT / 2);
//        this.lowerPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
//      ------------------------------------------ nesting panels

        this.compositePanel = new JPanel();
        this.compositePanel.setLayout(new BoxLayout(this.compositePanel, BoxLayout.Y_AXIS));

        this.gpsComponentNameLabel = new JLabel(componentName);
        this.gpsComponentNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.compositePanel.add(Box.createVerticalStrut(5));
        this.compositePanel.add(this.gpsComponentNameLabel);
        this.compositePanel.add(Box.createVerticalStrut(10));
        this.compositePanel.add(this.upperPanel);
        this.compositePanel.add(Box.createVerticalStrut(8));
        this.compositePanel.add(this.lowerPanel);
        this.compositePanel.add(Box.createVerticalStrut(5));
        this.compositePanel.setBounds(posX, posY, GpsInputComponentOld.COMPONENT_WIDTH, GpsInputComponentOld.COMPONENT_HEIGHT);
        this.compositePanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        frame.add(compositePanel);
    }

    public void setVisible(boolean visible) {
        this.compositePanel.setVisible(visible);
    }

    public String getLatDir() {
        return this.latDirTextField.getText();
    }

    public String getLonDir() {
        return this.lonDirTextField.getText();
    }

    public String getLatDeg() {
        return this.latDegTextField.getText();
    }

    public String getLonDeg() {
        return this.lonDegTextField.getText();
    }
}
