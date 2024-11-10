package mpoljak.App.GUI.models;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ParcelTableModel extends AbstractTableModel {

    private final List<ParcelModel> lParcels;

    private final String[] aColNames = new String[] {
            "Inventory nr.",
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

    public ParcelTableModel(List<ParcelModel> lParcels) {
        if (lParcels == null)
            throw new NullPointerException("Parcels data are not provided");
        this.lParcels = lParcels;
    }

    public void add(ParcelModel parcel) {
        lParcels.add(parcel);
        this.fireTableDataChanged();
    }

    public ParcelModel getModel(int index) {
        return lParcels.get(index);
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
        return this.lParcels.size();
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
        ParcelModel parcel = lParcels.get(rowIndex);
        if (columnIndex == 0)
            return parcel.getInventoryNr();
        else if (columnIndex == 1)
            return parcel.getDescription();
        else if (columnIndex == 2)
            return parcel.getGps1().getLatDeg();
        else if (columnIndex == 3)
            return parcel.getGps1().getLatitude();
        else if (columnIndex == 4)
            return parcel.getGps1().getLongDeg();
        else if (columnIndex == 5)
            return parcel.getGps1().getLongitude();
        else if (columnIndex == 6)
            return parcel.getGps2().getLatDeg();
        else if (columnIndex == 7)
            return parcel.getGps2().getLatitude();
        else if (columnIndex == 8)
            return parcel.getGps2().getLongDeg();
        else if (columnIndex == 9)
            return parcel.getGps2().getLongitude();
        return null;
    }
}
