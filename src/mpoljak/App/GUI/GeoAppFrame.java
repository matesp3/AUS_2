package mpoljak.App.GUI;

import mpoljak.App.GUI.components.GpsInputComponent;

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
    private ArrayList<JButton> lButtons;

    private boolean disabledState; // when some operation has been chosen


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
        con.gridx = 0;
        con.gridy = 0;
        this.gpsPanel = this.createGpsFormsArea(300,200, gpsColor, frameColor);
        managePanel.add(this.gpsPanel, con);

        con.gridx = 0;
        con.gridy = 1;
        con.insets = insets;                                        // outer margin
        lButtons = new ArrayList<>(6);
        JPanel operationsPanel = createButtonsSection(300, 180, gpsColor, btnColor);
        managePanel.add(operationsPanel, con);
//        this.add(this.gpsPanel);
//        this.createButtonsSection(this);
//      ---- set all visible
        this.setVisible(true);
        this.disabledState = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this) {
            System.out.println("Me.");
        }


    }

    private void disableOtherBtns(JButton clickedBtn) {
        for (JButton btn : lButtons) {
            btn.setEnabled(btn == clickedBtn);
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

    private JPanel createButtonsSection(int prefWidth, int prefHeight, Color background, Color btnBackground) {
        int btnWidth = 98;
        int btnHeight = 25;

        JPanel btnPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        btnPanel.setLayout(gbl);
        btnPanel.setBackground(background);
        btnPanel.setPreferredSize(new Dimension(prefWidth, prefHeight));
        btnPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1, true));
//        btnPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createCompoundBorder(), "Operations"));

        Insets insets = new Insets(10, 0, 0, 0);
        GridBagConstraints con = new GridBagConstraints();
        con.weightx = 0.25;
        con.weighty = 0.5;

        con.gridx = 0;
        con.gridy = 0;
        con.anchor = GridBagConstraints.WEST;
        JLabel labelTitle = new JLabel("CHOOSE OPERATION:");
        btnPanel.add(labelTitle, con);

        con.anchor = GridBagConstraints.BASELINE;
        con.gridx = 0;
        con.gridy = 1;
        JButton insertBtn = this.createButton(btnWidth, btnHeight, "Add data", btnBackground);
        insertBtn.addActionListener(e -> {
            this.btnExecution("INSERT", insertBtn);
        });
        btnPanel.add(insertBtn, con);
        this.lButtons.add(insertBtn);

        con.gridx = 1;
        con.gridy = 1;
//        con.anchor = GridBagConstraints.EAST;
        JButton searchBtn = this.createButton(btnWidth, btnHeight, "Find data", btnBackground);
        searchBtn.addActionListener(e -> {
            this.btnExecution("SEARCH", searchBtn);
        });
        btnPanel.add(searchBtn, con);
        this.lButtons.add(searchBtn);

        con.gridx = 0;
        con.gridy = 2;
//        con.anchor = GridBagConstraints.WEST;
        JButton editBtn = this.createButton(btnWidth, btnHeight, "Edit data", btnBackground);
        editBtn.addActionListener(e -> {
            this.btnExecution("EDIT", editBtn);
        });
        btnPanel.add(editBtn, con);
        this.lButtons.add(editBtn);

        con.gridx = 1;
        con.gridy = 2;
        JButton deleteBtn = this.createButton(btnWidth, btnHeight, "Delete data", btnBackground);
        deleteBtn.addActionListener(e -> {
            this.btnExecution("DELETE", deleteBtn);
        });
        btnPanel.add(deleteBtn, con);
        this.lButtons.add(deleteBtn);

        con.gridx = 0;
        con.gridy = 3;
        JButton generateBtn = this.createButton(-1, -1, "Generate data", btnBackground);
        generateBtn.addActionListener(e -> {
            this.btnExecution("GENERATE", generateBtn);
        });
        btnPanel.add(generateBtn, con);
        this.lButtons.add(generateBtn);

        con.gridx = 1;
        con.gridy = 3;
        JButton printBtn = this.createButton(-1, -1, "Get all data", btnBackground);
        printBtn.addActionListener(e -> {
            this.btnExecution("PRINT", printBtn);
        });
        btnPanel.add(printBtn, con);
        this.lButtons.add(printBtn);

        return btnPanel;
    }

    private JButton createButton(int width, int height, String text, Color background) {
        JButton button = new JButton();
        button.setText(text);
        if (height > 0 && width > 0)
            button.setPreferredSize(new Dimension(width, height));
        button.setBackground(background);
        return button;
    }

    private void btnExecution(String action, JButton clickedBtn) {
        if (!this.disabledState) {
            this.disabledState = true;
            System.out.println(action);
            this.disableOtherBtns(clickedBtn);
        } else {
            this.disabledState = false;
            this.enableAllBtns();
        }
    }

    private void enableAllBtns() {
        for (JButton btn : lButtons)
            btn.setEnabled(true);
    }

}
