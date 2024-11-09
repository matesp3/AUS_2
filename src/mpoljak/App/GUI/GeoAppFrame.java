package mpoljak.App.GUI;

import mpoljak.App.GUI.components.DetailsInputComponent;
import mpoljak.App.GUI.components.GeneratorInputComponent;
import mpoljak.App.GUI.components.GpsInputComponent;
import mpoljak.App.GUI.models.GeoInfoModel;
import mpoljak.App.GUI.models.GpsModel;
import mpoljak.App.GUI.models.ParcelModel;
import mpoljak.App.GUI.models.ParcelTableModel;
import mpoljak.utilities.SwingTableColumnResizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class GeoAppFrame extends JFrame implements ActionListener {
    public static final int OP_SEARCH   = 1;
    public static final int OP_INSERT   = 2;
    public static final int OP_EDIT     = 3;
    public static final int OP_DELETE   = 4;
    public static final int OP_GENERATE = 5;
    public static final int OP_PRINT    = 6;

    private static final int CANVAS_WIDTH = 1400;
    private static final int CANVAS_HEIGHT = 700;
    private static final int MANAGE_PANE_WIDTH = 350;

    public static final char TYPE_PROPERTY = 'Y';
    public static final char TYPE_PARCEL = 'L';

    private GpsInputComponent gpsInput1;
    private GpsInputComponent gpsInput2;
    private DetailsInputComponent detailsPanel;
    private GeneratorInputComponent panelForGenerating;

    private JRadioButton optionParcel;
    private JRadioButton optionProperty;
    private JRadioButton optionAll;

    private JPanel gpsPanel;
    private JPanel optionsPanel;
    private JButton executeBtn;

    private int selectedOp;

    private class RadioButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton rBtn = (JRadioButton) e.getSource();
            if (rBtn == optionAll) {
                if (selectedOp == OP_SEARCH)
                    gpsInput2.setComponentEnable(true); // because this can change when SEARCH op is chosen but
                return;                                 // radio button is changed
            }
            else if (rBtn == optionParcel) {
                detailsPanel.setDetailsType(GeoAppFrame.TYPE_PARCEL);
            }
            else if (rBtn == optionProperty) {
                detailsPanel.setDetailsType(GeoAppFrame.TYPE_PROPERTY);
            }

            if (selectedOp == OP_SEARCH)
                gpsInput2.setComponentEnable(false);
        }
    }

    public GeoAppFrame() {
        this.selectedOp = OP_INSERT;
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
//      ---- DATA -> TABLE OF PARCELS
        JPanel parcelsPanel = new JPanel();
        dataPanel.add(parcelsPanel);
//        parcelsPanel.setPreferredSize(new Dimension(700, 250));
        List<ParcelModel> lParcels = new ArrayList<ParcelModel>();
        lParcels.add(new ParcelModel(new GpsModel('N', 45.2,'W', 15.8),
                new GpsModel('S', 5.2,'E', 47.8), "first-parcel", 1));
        lParcels.add(new ParcelModel(new GpsModel('S', 45.2,'E', 15.8),
                new GpsModel('S', 5.2,'W', 47.8), "second-parcel", 2));
        ParcelTableModel model = new ParcelTableModel(lParcels);
        JTable parcelsTab = new JTable(model);
        SwingTableColumnResizer.setJTableColsWidth(parcelsTab, 980,
                new double[] {8,22,9,8.5,9,8.5,9,8.5,9,8.5});
        JScrollPane scrollPane = new JScrollPane(parcelsTab);
        scrollPane.setPreferredSize(new Dimension(980,250));
        parcelsPanel.add(scrollPane);


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
//      ----- MANAGE PANEL -> RADIO BUTTON FOR CHOOSING TYPE OF GEO ITEM
        con.gridx = 0;
        con.gridy = 1;
        this.optionsPanel = this.createGeoTypeSelection(300, 50, gpsColor);
        managePanel.add(this.optionsPanel, con);
//      ----- MANAGE PANEL -> DETAILS FOR CHOSEN OPERATION
        con.gridx = 0;
        con.gridy = 2;
        con.insets = insets;                                        // outer margin
        this.detailsPanel = this.createDetailsArea(300, 140, frameColor);
        managePanel.add(detailsPanel, con);
//      ----- MANAGE PANEL -> BUTTONS FOR OPERATIONS
        con.gridx = 0;
        con.gridy = 3;
        JPanel operationsPanel = this.createOperationsArea(300, 50, gpsColor, btnColor);
        managePanel.add(operationsPanel, con);
//      ----- MANAGE PANEL -> INPUTS FOR GENERATING DATA
        con.gridx = 0;
        con.gridy = 4;
        this.panelForGenerating = new GeneratorInputComponent(300, 120, frameColor);
        managePanel.add(this.panelForGenerating, con);
//      ---- set all visible
        this.setVisible(true);
        detailsPanel.setModel(new GeoInfoModel('Y',12,"This is property"));
        this.selectedOp = OP_SEARCH;
        this.prepareOperationContext();
    }

    private DetailsInputComponent createDetailsArea(int prefWidth, int prefHeight, Color backgroundColor) {
        DetailsInputComponent detailsPanel = new DetailsInputComponent(prefWidth, prefHeight, backgroundColor);
        detailsPanel.setPreferredSize(new Dimension(prefWidth, prefHeight));
        detailsPanel.setBackground(backgroundColor);
        detailsPanel.setBorder(BorderFactory.createEtchedBorder());
        return detailsPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this) {
            System.out.println("Me.");
        }
    }

    private JPanel createGeoTypeSelection(int prefWidth, int prefHeight, Color bgColor) {
        JPanel radiosPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        radiosPanel.setLayout(layout);
        radiosPanel.setPreferredSize(new Dimension(prefWidth, prefHeight));
        radiosPanel.setBackground(bgColor);

        RadioButtonActionListener rbActionListener = new RadioButtonActionListener();
        this.optionParcel = new JRadioButton("Parcel");
        this.optionParcel.setBackground(bgColor);
        this.optionParcel.addActionListener(rbActionListener);

        this.optionProperty = new JRadioButton("Property");
        this.optionProperty.setBackground(bgColor);
        this.optionProperty.addActionListener(rbActionListener);

        this.optionAll = new JRadioButton("Parcel & Property");
        this.optionAll.setBackground(bgColor);
        this.optionAll.addActionListener(rbActionListener);

        GridBagConstraints con = new GridBagConstraints();
        // choose type of location
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(this.optionParcel);
        btnGroup.add(this.optionProperty);
        btnGroup.add(this.optionAll);

        con.gridx = 0;
        con.gridy = 0;
        radiosPanel.add(this.optionParcel, con);
        this.optionParcel.setSelected(true);

        con.gridx = 1;
        con.gridy = 0;
        radiosPanel.add(this.optionProperty, con);

        con.gridx = 2;
        con.gridy = 0;
        radiosPanel.add(this.optionAll, con);

        return radiosPanel;
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

    private JPanel createOperationsArea(int prefWidth, int prefHeight, Color background, Color btnBackground) {
        int btnWidth = 98;
        int btnHeight = 25;

        JPanel operationsPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        operationsPanel.setLayout(gbl);
        operationsPanel.setBackground(background);
        operationsPanel.setPreferredSize(new Dimension(prefWidth, prefHeight));
//        operationsPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1, true));

        GridBagConstraints con = new GridBagConstraints();
        con.weighty = 0.5;
        con.weightx = 1.0;
        con.gridx = 0;
        con.gridy = 0;
        con.insets = new Insets(15,0,0,0);
        con.anchor = GridBagConstraints.NORTHWEST;
        JLabel labelTitle = new JLabel("Choose operation: ");
        operationsPanel.add(labelTitle, con);

        con.gridx = 1;
        con.gridy = 0;
        con.insets = new Insets(12,0,0,0);
        String[] comboItems = {"search data", "insert data", "edit data", "delete data", "generate data",
                "print all data"};
        JComboBox<String> operationsBox = new JComboBox<>(comboItems);
        operationsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
                String selectedOperation = (String) comboBox.getSelectedItem();
                if (selectedOperation.compareTo("search data") == 0) {
                    selectedOp = GeoAppFrame.OP_SEARCH;
                }
                else if (selectedOperation.compareTo("insert data") == 0) {
                    selectedOp = GeoAppFrame.OP_INSERT;
                }
                else if (selectedOperation.compareTo("edit data") == 0) {
                    selectedOp = GeoAppFrame.OP_EDIT;
                }
                else if (selectedOperation.compareTo("delete data") == 0) {
                    selectedOp = GeoAppFrame.OP_DELETE;
                }
                else if (selectedOperation.compareTo("generate data") == 0) {
                    selectedOp = GeoAppFrame.OP_GENERATE;
                }
                else if (selectedOperation.compareTo("print all data") == 0) {
                    selectedOp = GeoAppFrame.OP_PRINT;
                }
                prepareOperationContext();
            }
        });
        operationsPanel.add(operationsBox, con);

        con.anchor = GridBagConstraints.NORTHEAST;
        con.gridwidth = 1;
        con.gridx = 2;
        con.gridy = 0;
        con.insets = new Insets(10, 0, 0, 0);
        Color c = new Color(146, 236, 236);
        this.executeBtn = createButton(80,30, "Execute", c);
        operationsPanel.add(this.executeBtn, con);

        return operationsPanel;
    }

    private void prepareOperationContext() {
        System.out.println("OP > "+this.selectedOp);
        if (this.selectedOp == OP_INSERT || this.selectedOp == OP_EDIT) {
            this.gpsInput1.setComponentEnable(true);
            this.gpsInput2.setComponentEnable(true);
            this.detailsPanel.setComponentEnable(true);
            this.panelForGenerating.setComponentEnable(false);
            // it is not allowed to change type while editing
            this.setEnableSingleOptions(this.selectedOp == OP_INSERT);
            this.optionAll.setEnabled(false);
            if (this.selectedOp == OP_INSERT)
                this.optionParcel.setSelected(true);
            return;
        }
        else if (this.selectedOp == OP_SEARCH) {
            this.gpsInput1.setComponentEnable(true);
            this.gpsInput2.setComponentEnable(false);
            this.detailsPanel.setComponentEnable(false);
            this.panelForGenerating.setComponentEnable(false);
            this.optionParcel.setEnabled(true);
            this.optionProperty.setEnabled(true);
            this.optionAll.setEnabled(true);
            return;
        }
        this.gpsInput1.setComponentEnable(false);
        this.gpsInput2.setComponentEnable(false);
        this.detailsPanel.setComponentEnable(false);
        if (this.selectedOp == OP_GENERATE) {
            this.panelForGenerating.setComponentEnable(true);
        }
        else if (this.selectedOp == OP_DELETE || this.selectedOp == OP_PRINT) {
            this.panelForGenerating.setComponentEnable(false);
        }
        this.setEnableAllOptions(false);
    }

    private JButton createButton(int width, int height, String text, Color background) {
        JButton button = new JButton();
        button.setText(text);
        if (height > 0 && width > 0)
            button.setPreferredSize(new Dimension(width, height));
        button.setBackground(background);
        return button;
    }

    /**
     * Enables or disables radio button for parcel and radio button for property
     * @param enable
     */
    private void setEnableSingleOptions(boolean enable) {
        this.optionParcel.setEnabled(enable);
        this.optionProperty.setEnabled(enable);
    }

    /**
     * Enables or disables all radio buttons for geo type possible choices
     * @param enable
     */
    private void setEnableAllOptions(boolean enable) {
        this.optionParcel.setEnabled(enable);
        this.optionProperty.setEnabled(enable);
        this.optionAll.setEnabled(enable);
    }
}
