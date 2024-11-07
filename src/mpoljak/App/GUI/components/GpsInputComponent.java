package mpoljak.App.GUI.components;

import javax.swing.*;
import java.awt.*;

public class GpsInputComponent {
    public static final int COMPONENT_WIDTH = 300;
    public static final int COMPONENT_HEIGHT = 100;

    private JPanel compositePanel;
    private JPanel upperPanel;
    private JPanel lowerPanel;

    private JTextField latDegTextField;
    private JTextField lonDegTextField;
    private JTextField latSignTextField;
    private JTextField lonSignTextField;

    private JLabel latDegLabel;
    private JLabel lonDegLabel;
    private JLabel latSignLabel;
    private JLabel lonSignLabel;
    private JLabel gpsComponentNameLabel;


    public GpsInputComponent(String componentName, JFrame frame, int posX, int posY) {
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
        this.latSignLabel = new JLabel("Lat direction: ");
        this.latSignTextField = new JTextField();
        this.upperPanel.add(this.latSignLabel);
        this.upperPanel.add(Box.createHorizontalStrut(6));
        this.upperPanel.add(this.latSignTextField);
        this.upperPanel.add(Box.createHorizontalStrut(5));

        this.upperPanel.setBounds(0, 0, GpsInputComponent.COMPONENT_WIDTH,
                GpsInputComponent.COMPONENT_HEIGHT / 2);
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
        this.lonSignLabel = new JLabel("Lon direction: ");
        this.lonSignTextField = new JTextField();
        this.lowerPanel.add(this.lonSignLabel);
        this.lowerPanel.add(Box.createHorizontalStrut(6));
        this.lowerPanel.add(this.lonSignTextField);
        this.lowerPanel.add(Box.createHorizontalStrut(5));

        this.lowerPanel.setBounds(0, GpsInputComponent.COMPONENT_HEIGHT / 2,
                GpsInputComponent.COMPONENT_WIDTH,GpsInputComponent.COMPONENT_HEIGHT / 2);
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
        this.compositePanel.setBounds(posX, posY, GpsInputComponent.COMPONENT_WIDTH, GpsInputComponent.COMPONENT_HEIGHT);
        this.compositePanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        frame.add(compositePanel);
    }

    public void setVisible(boolean visible) {

    }
}
