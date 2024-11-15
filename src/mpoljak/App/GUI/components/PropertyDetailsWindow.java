package mpoljak.App.GUI.components;

import mpoljak.App.GUI.models.ParcelTableModel;
import mpoljak.utilities.SwingTableColumnResizer;

import javax.swing.*;
import java.awt.*;

public class PropertyDetailsWindow {
    private JFrame frame;
    private JTable parcelTabl;

    public PropertyDetailsWindow(int prefWidth, int prefHeight, ParcelTableModel parcelModel) {
        this.frame = new JFrame();
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
//        this.frame.setPreferredSize(new Dimension(prefWidth, prefHeight));
        this.frame.setSize(prefWidth, prefHeight);
        this.frame.setResizable(false);

        this.parcelTabl = new JTable(parcelModel);
        SwingTableColumnResizer.setJTableColsWidth(this.parcelTabl, prefWidth - 10,
                new double[] {8,22,9,8.5,9,8.5,9,8.5,9,8.5});
        JScrollPane scrollPane = new JScrollPane(parcelTabl);
        scrollPane.setPreferredSize(new Dimension(prefWidth - 10,prefHeight - 10));
        this.frame.add(scrollPane);
//--------------------------------------------
        this.frame.setVisible(true);
    }
}
