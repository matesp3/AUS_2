package mpoljak.App.GUI;

import mpoljak.App.GUI.components.DetailsInputComponent;
import mpoljak.App.GUI.components.GpsInputComponent;
import mpoljak.App.GUI.components.OperationsAreaComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GeoAppFrame extends JFrame implements ActionListener {
    private static final int CANVAS_WIDTH = 1400;
    private static final int CANVAS_HEIGHT = 700;
    private static final int MANAGE_PANE_WIDTH = 350;

    private GpsInputComponent gpsInput1;
    private GpsInputComponent gpsInput2;
    private JPanel gpsPanel;


    public GeoAppFrame() {
        ImageIcon icon = new ImageIcon("src/mpoljak/files/GeoApp-icon.png");
        this.setIconImage(icon.getImage());
//      ---- frame properties
        this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        this.setLayout(new BorderLayout());
//        this.setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        this.setResizable(false); // if I don't want client to resize window
        this.setTitle("Geo app");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
//      ---- colors
        Color frameColor = new Color(181, 248, 211);
        Color gpsColor = new Color(179, 234, 182);
        Color btnColor = new Color(210, 229, 191, 255);
        this.getContentPane().setBackground(frameColor);
//      ---- components
//        *** main RIGHT -DATA panel
        JPanel dataPanel = new JPanel();
        dataPanel.setBackground(frameColor);
        dataPanel.setPreferredSize(new Dimension(CANVAS_WIDTH-MANAGE_PANE_WIDTH, CANVAS_HEIGHT));
        dataPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        this.add(dataPanel, BorderLayout.CENTER);
//        *** main LEFT - MANAGE panel
        JPanel managePanel = new JPanel();
        managePanel.setPreferredSize(new Dimension(MANAGE_PANE_WIDTH, CANVAS_HEIGHT));
        managePanel.setBackground(gpsColor);
        managePanel.setBorder(BorderFactory.createEtchedBorder());
        this.add(managePanel, BorderLayout.WEST);

        GridBagLayout gbl = new GridBagLayout();
        managePanel.setLayout(gbl);

        GridBagConstraints con = new GridBagConstraints();
        Insets insets = new Insets(10, 0, 0, 0);
        con.anchor = GridBagConstraints.NORTHWEST;
        con.ipadx = 5;
        con.ipady = 5;                                              // inner margin
//      ----- MANAGE PANEL -> GPS FORMS
        con.gridx = 0;
        con.gridy = 0;
        this.gpsPanel = this.createGpsFormsArea(300,200, gpsColor, frameColor);
        managePanel.add(this.gpsPanel, con);
//      ----- MANAGE PANEL -> DETAILS FOR CHOSEN OPERATION
        con.gridx = 0;
        con.gridy = 1;
        con.insets = insets;                                        // outer margin
        JPanel detailsPanel = createDetailsArea(300, 180, gpsColor);
        managePanel.add(detailsPanel, con);
//      ----- MANAGE PANEL -> BUTTONS FOR OPERATIONS
        con.gridx = 0;
        con.gridy = 2;
        JPanel operationsPanel = new OperationsAreaComponent(300, 180, gpsColor, btnColor);
        managePanel.add(operationsPanel, con);

//      ---- set all visible
        this.setVisible(true);
    }

    private JPanel createDetailsArea(int prefWidth, int prefHeight, Color backgroundColor) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setPreferredSize(new Dimension(prefWidth, prefHeight));
        detailsPanel.setBackground(backgroundColor);
        detailsPanel.setBorder(BorderFactory.createEtchedBorder(1));

//        detailsPanel.setLayout(new GridLayout());
        detailsPanel = new DetailsInputComponent(prefWidth, prefHeight, backgroundColor);
        return detailsPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this) {
            System.out.println("Me.");
        }
    }

    private JPanel createGpsFormsArea(int prefWidth, int prefHeight, Color background, Color gpsBackground) {
        JPanel panelForGPS = new JPanel();
        panelForGPS.setBackground(background);
        panelForGPS.setLayout(new BoxLayout(panelForGPS,BoxLayout.Y_AXIS));
        panelForGPS.setPreferredSize(new Dimension(prefWidth, prefHeight));

        this.gpsInput1 = new GpsInputComponent("GPS position nr. 1",
                GpsInputComponent.PREFERRED_WIDTH,GpsInputComponent.PREFERRED_HEIGHT);
        this.gpsInput1.setBgColor(gpsBackground);
        this.gpsInput2 = new GpsInputComponent("GPS position nr. 2",
                GpsInputComponent.PREFERRED_WIDTH,GpsInputComponent.PREFERRED_HEIGHT);
        this.gpsInput2.setBgColor(gpsBackground);

        panelForGPS.add(this.gpsInput1);
        panelForGPS.add(Box.createVerticalStrut(10)); // space added
        panelForGPS.add(this.gpsInput2);

        return panelForGPS;
    }
}
