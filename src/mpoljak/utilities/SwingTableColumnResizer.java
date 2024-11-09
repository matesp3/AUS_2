package mpoljak.utilities;

import javax.swing.*;
import javax.swing.table.TableColumn;

/**
 * Dedicated for work with Java Swing Table
 */
public class SwingTableColumnResizer {
    /**
     *
     * @param table table which columns are to be resized
     * @param prefWidth preferred width of whole table
     * @param aPercentages percentages of <code>prefWidth</code> for width of each column
     */
    public static void setJTableColsWidth(JTable table, int prefWidth, double[] aPercentages) {

        if (aPercentages == null || table == null)
            return;
        int colCount = table.getColumnModel().getColumnCount();
        if (colCount != aPercentages.length)
            return;
        double sumPercent = 0.0;
        for (int i = 0; i < colCount; i++) {
            sumPercent += aPercentages[i];
        }
        DoubleComparator dc = DoubleComparator.getInstance();;
        if (dc.compare(sumPercent, 100.0, 0.1) != 0)
            return;
        for (int c = 0; c < colCount; c++) {
            TableColumn col = table.getColumnModel().getColumn(c);
            col.setPreferredWidth( (int) (prefWidth * (aPercentages[c] / 100.0)));
        }
    }
}
