package mpoljak.App.GUI.components;

import javax.swing.*;
import java.awt.*;
import java.util.GregorianCalendar;

public class DateInputComponent extends JPanel {
    private JTextField yearTxt;
    private JTextField monthTxt;
    private JTextField dayTxt;

    public DateInputComponent(GregorianCalendar date, int width, int height, Color bg, String description) {
        JLabel desc = new JLabel(description);
        JLabel lDay = new JLabel("Day");
        this.dayTxt = new JTextField(2);
        JLabel lMon = new JLabel("Month");
        this.monthTxt = new JTextField(2);
        JLabel lYear = new JLabel("Year");
        this.yearTxt = new JTextField(4);
        this.setDate(date);
        this.add(desc);
        this.add(lDay);
        this.add(this.dayTxt);
        this.add(lMon);
        this.add(this.monthTxt);
        this.add(lYear);
        this.add(this.yearTxt);
        if (width > 0 && height > 0)
            this.setPreferredSize(new Dimension(width, height));
        if (bg != null)
            this.setBackground(bg);
    }

    public void setDate(GregorianCalendar date) {
        if (date == null)
            return;
        this.dayTxt.setText(String.valueOf(date.get(GregorianCalendar.HOUR_OF_DAY)));
        this.monthTxt.setText(String.valueOf(date.get(GregorianCalendar.DAY_OF_MONTH)));
        this.yearTxt.setText(String.valueOf(date.get(GregorianCalendar.YEAR)));
    }

    public GregorianCalendar getDate() {
        GregorianCalendar date = new GregorianCalendar();
        try {
            date.set(GregorianCalendar.HOUR_OF_DAY, Integer.parseInt(this.dayTxt.getText()));
            date.set(GregorianCalendar.MONTH, Integer.parseInt(this.monthTxt.getText()));
            date.set(GregorianCalendar.YEAR, Integer.parseInt(this.yearTxt.getText()));
            return date;
        } catch (Exception e) {
            return null;
        }
    }
}
