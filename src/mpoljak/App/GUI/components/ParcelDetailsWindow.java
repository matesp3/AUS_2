package mpoljak.App.GUI.components;

import mpoljak.App.GUI.models.PropertyTableModel;
import mpoljak.utilities.SwingTableColumnResizer;

import javax.swing.*;
import java.awt.*;

public class ParcelDetailsWindow {
    private JFrame frame;
    private JTable propertyTab;

    public ParcelDetailsWindow(int prefWidth, int prefHeight, PropertyTableModel propertyModel) {
        this.frame = new JFrame();
        this.frame.setSize(prefWidth, prefHeight);
//        this.frame.setPreferredSize(new Dimension(prefWidth, prefHeight));
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.propertyTab = new JTable(propertyModel);
        SwingTableColumnResizer.setJTableColsWidth(propertyTab, prefWidth - 10,
                new double[] {8,22,9,8.5,9,8.5,9,8.5,9,8.5});
        JScrollPane scrollPane = new JScrollPane(propertyTab);
        scrollPane.setPreferredSize(new Dimension(prefWidth - 10,prefHeight - 10));
        this.frame.add(scrollPane);
//--------------------------------------------
        this.frame.setVisible(true);
    }
}
