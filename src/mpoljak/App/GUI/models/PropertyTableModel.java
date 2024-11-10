package mpoljak.App.GUI.models;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PropertyTableModel extends AbstractTableModel {
    private final List<PropertyModel> lProperties;

    private final String[] aColNames = new String[] {
            "Property nr.",
            "Description",
            "g1 Lat[°]",
            "g1 Lat[dir]",
            "g1 Lon[°]",
            "g1 Lon[dir]",
            "g2 Lon[°]",
            "g2 Lat[dir]",
            "g2 Lon[°]",
            "g2 Lon[dir]"};
    private final Class<?>[] aColClasses = new Class<?>[] {
            Integer.class,
            String.class,
            Double.class,
            Character.class,
            Double.class,
            Character.class,
            Double.class,
            Character.class,
            Double.class,
            Character.class};

    public PropertyTableModel(List<PropertyModel> lProperties) {
        if (lProperties == null)
            throw new NullPointerException("Parcels data are not provided");
        this.lProperties = lProperties;
    }

    public void add(PropertyModel property) {
        lProperties.add(property);
        this.fireTableDataChanged();
    }

    public PropertyModel getModel(int index) {
        return lProperties.get(index);
    }

    @Override
    public String getColumnName(int column) {
        return this.aColNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return this.aColClasses[columnIndex];
    }

    @Override
    public int getRowCount() {
        return this.lProperties.size();
    }

    @Override
    public int getColumnCount() {
        return this.aColNames.length;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PropertyModel property = lProperties.get(rowIndex);
        if (columnIndex == 0)
            return property.getPropertyNr();
        else if (columnIndex == 1)
            return property.getDescription();
        else if (columnIndex == 2)
            return property.getGps1().getLatDeg();
        else if (columnIndex == 3)
            return property.getGps1().getLatitude();
        else if (columnIndex == 4)
            return property.getGps1().getLongDeg();
        else if (columnIndex == 5)
            return property.getGps1().getLongitude();
        else if (columnIndex == 6)
            return property.getGps2().getLatDeg();
        else if (columnIndex == 7)
            return property.getGps2().getLatitude();
        else if (columnIndex == 8)
            return property.getGps2().getLongDeg();
        else if (columnIndex == 9)
            return property.getGps2().getLongitude();
        return null;
    }
}
