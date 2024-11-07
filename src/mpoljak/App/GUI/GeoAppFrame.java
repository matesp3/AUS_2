package mpoljak.App.GUI;

import mpoljak.App.GUI.components.GpsInputComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GeoAppFrame extends JFrame implements ActionListener {
    private JButton btn;

    public GeoAppFrame() {

        ImageIcon icon = new ImageIcon("src/mpoljak/files/GeoApp-icon.png");
        this.setIconImage(icon.getImage());


        this.setSize(1000,500);
        this.setLayout(null);
//        this.setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        this.setResizable(false); // if I don't want client to resize window
        this.setTitle("Geo app");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //set layout alebo setBounds, ak chcem vnutorne veci relativne posuvat
//        JPanel panel = this.createPanel();
//        this.add(panel);

//        JLabel label = this.createLabel();
//        panel.add(label);

//        this.btn = createButton();
//        this.add(this.btn);
//        JPanel panel1 = new JPanel();
//        JPanel panel2 = new JPanel();
//        JPanel panel3 = new JPanel();
//        JPanel panel4 = new JPanel();
//        JPanel panel5 = new JPanel();
//
//        panel1.setBackground(Color.red);
//        panel2.setBackground(Color.green);
//        panel3.setBackground(Color.yellow);
//        panel4.setBackground(Color.black);
//        panel5.setBackground(Color.blue);
//
//
//        panel1.setPreferredSize(new Dimension(100,200));
//        panel2.setPreferredSize(new Dimension(100,200));
//        panel3.setPreferredSize(new Dimension(100,200));
//        panel4.setPreferredSize(new Dimension(100,200));
//        panel5.setPreferredSize(new Dimension(100,200));
//        this.add(panel1);
//        this.add(panel2);
//        this.add(panel3);
//        this.add(panel4);
//        this.add(panel5);
        GpsInputComponent gpsComp1 = new GpsInputComponent("GPS position nr. 1", this,0,0);;
        GpsInputComponent gpsComp2 = new GpsInputComponent("GPS position nr. 2", this,0,GpsInputComponent.COMPONENT_HEIGHT);;

        this.setVisible(true);
//        this.setLocationRelativeTo(null);
//        this.getContentPane().setBackground(Color.LIGHT_GRAY);

    }



    private JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.red);
        panel.setBounds(200, 200, 200, 200);

        return panel;
    }

    private JLabel createLabel() {
        JLabel label = new JLabel();
        label.setText("Geo App");
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.TOP);
        label.setBackground(Color.GRAY);
        label.setForeground(Color.BLACK);
        label.setBorder(BorderFactory.createLineBorder(Color.BLUE,3));
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(0, 0, 100, 100);
        return label;
    }

    private JButton createButton() {
        JButton button = new JButton();
        button.setBackground(Color.yellow);
        button.setForeground(Color.BLUE);
        button.setText("Click me!");
        button.setBounds(50,50, 100,100);
        button.setEnabled(true);
        button.addActionListener(e -> System.out.println("Clicked."));
        button.setBorder(BorderFactory.createEtchedBorder());
        return button;
    }

    private void testComp() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JPanel panel5 = new JPanel();

        panel1.setBackground(Color.red);
        panel2.setBackground(Color.green);
        panel3.setBackground(Color.yellow);
        panel4.setBackground(Color.black);
        panel5.setBackground(Color.blue);

        panel1.setPreferredSize(new Dimension(100,200));
        panel2.setPreferredSize(new Dimension(100,200));
        panel3.setPreferredSize(new Dimension(100,200));
        panel4.setPreferredSize(new Dimension(100,200));
        panel5.setPreferredSize(new Dimension(100,200));

        frame.add(panel1, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.SOUTH);
//        frame.add(panel3, BorderLayout.NORTH);
//        frame.add(panel4, BorderLayout.NORTH);
//        frame.add(panel5, BorderLayout.NORTH);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.btn) {
            System.out.println("jeee");
        }
    }
}
