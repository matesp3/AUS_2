package mpoljak.App.GUI.components;

import mpoljak.App.GUI.GeoAppFrame;
import mpoljak.App.GUI.models.GeoInfoModel;
import mpoljak.data.geo.GPS;
import mpoljak.data.geo.ParcelFactory;
import mpoljak.data.geo.ParcelParams;
import mpoljak.dataStructures.IParams;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ParcelForm extends GuiForm {
    private final DetailsInputComponent detailsPanel;
    private ParcelFactory factory;
    private ParcelParams par;
    private GpsInputComponent gpsInput1;
    private GpsInputComponent gpsInput2;

    public ParcelForm(ParcelParams params, int width, int height, Color bg, boolean add) {
        super(!add, (add ? null : params));
        this.factory = new ParcelFactory();
        if (params == null)
            this.par = (ParcelParams) this.factory.createParams();
        else
            this.par = params;
         ///------------------- GUI PART
        this.setSize(new Dimension(width, height));
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle(add ? "Add parcel" : "Edit parcel");

        Color frameColor = new Color(197, 239, 215, 255);
        Color gpsColor = new Color(179, 234, 182);
//        /        *** main LEFT - MANAGE panel
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(width - 20, height));
        mainPanel.setBackground(bg);
        mainPanel.setBorder(BorderFactory.createEtchedBorder());
        this.add(mainPanel);

        GridBagLayout gbl = new GridBagLayout();
        mainPanel.setLayout(gbl);

        GridBagConstraints con = new GridBagConstraints();
        Insets insets = new Insets(10, 0, 0, 0);
        con.anchor = GridBagConstraints.NORTHWEST;
        con.ipadx = 5;
        con.ipady = 5;                                              // inner margin
//      ----- MANAGE PANEL -> GPS FORMS
        con.gridx = 0;
        con.gridy = 0;
        JPanel gpsPanel = this.createGpsFormsArea(width - 40,180, bg, bg,
                params == null ? null : this.par.getGps1(), params == null ? null : this.par.getGps2());
        mainPanel.add(gpsPanel, con);

        con.gridx = 0;
        con.gridy = 1;
        con.insets = insets;                                        // outer margin
        JPanel wrapper = new JPanel();
        wrapper.setBackground(bg);
        wrapper.setPreferredSize(new Dimension(width - 40, 140));
        this.detailsPanel = this.createDetailsArea(width - 40, 140, bg, params);
        wrapper.add(detailsPanel);
        mainPanel.add(detailsPanel, con);


        con.gridx = 0;
        con.gridy = 2;
        con.insets = insets;                                        // outer margin
        JPanel btnsPanel = new JPanel();
        btnsPanel.setPreferredSize(new Dimension(width - 40, 36));
        btnsPanel.setBackground(bg);
        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(e ->  {
            this.updateFields();
            this.setSubmittedState();
            this.dispose();
        });
        btnsPanel.add(btnSubmit);
        JButton btnDecline = new JButton("Decline");
        btnDecline.addActionListener(e -> {
            super.setDeclinedState();
            this.dispose();
        });
        btnsPanel.add(btnDecline);
        mainPanel.add(btnsPanel, con);

//        ------- go live
        this.addClosingBehavior();
        if (!add) {
            this.updateFields();
        }
        this.setVisible(true);
    }

    @Override
    protected IParams getUserInputs() {
        this.updateFields();
        return this.par;
    }

    private void updateFields() {
        try {
            this.par.setGps1(this.gpsInput1.getModel());
            this.par.setGps2(this.gpsInput2.getModel());
            GeoInfoModel details = this.detailsPanel.getModel();
            this.par.setParcelId(details == null ? -1 : this.detailsPanel.getModel().getNumber());
            this.par.setDescription(details == null ? "" : details.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addClosingBehavior() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (!isSubmitted())
                    setDeclinedState();
            }
        });
    }

    private JPanel createGpsFormsArea(int prefWidth, int prefHeight, Color background, Color gpsBackground, GPS g1, GPS g2) {
        JPanel panelForGPS = new JPanel();
        panelForGPS.setBackground(background);
        panelForGPS.setLayout(new BoxLayout(panelForGPS,BoxLayout.Y_AXIS));
        panelForGPS.setPreferredSize(new Dimension(prefWidth, prefHeight));

        this.gpsInput1 = new GpsInputComponent("GPS position nr. 1",
                prefWidth,prefHeight, g1);
//                GpsInputComponent.PREFERRED_WIDTH,GpsInputComponent.PREFERRED_HEIGHT, g1);
        this.gpsInput1.setBgColor(gpsBackground);
        this.gpsInput2 = new GpsInputComponent("GPS position nr. 2",
                prefWidth,prefHeight, g2);
//                GpsInputComponent.PREFERRED_WIDTH,GpsInputComponent.PREFERRED_HEIGHT, g2);
        this.gpsInput2.setBgColor(gpsBackground);

        panelForGPS.add(this.gpsInput1);
        panelForGPS.add(Box.createVerticalStrut(10)); // space added
        panelForGPS.add(this.gpsInput2);

        return panelForGPS;
    }


    private DetailsInputComponent createDetailsArea(int prefWidth, int prefHeight, Color backgroundColor, ParcelParams params) {
        DetailsInputComponent detailsPanel = new DetailsInputComponent(prefWidth, prefHeight, backgroundColor,
                params == null ? null : new GeoInfoModel(GeoAppFrame.TYPE_PARCEL, params.getParcelId(), params.getDescription()));
        detailsPanel.setPreferredSize(new Dimension(prefWidth, prefHeight));
        detailsPanel.setBackground(backgroundColor);
//        detailsPanel.setBorder(BorderFactory.createEtchedBorder());
        return detailsPanel;
    }

    public static void main(String[] args) {
        GPS g = new GPS('N', 27, 'E', 78);

        new ParcelForm(new ParcelParams(12, "ahoj", g, g, -1), 400, 440, new Color(189, 243, 224), false);
        new ParcelForm(null, 400, 440, new Color(189, 243, 224), true);
    }
}
