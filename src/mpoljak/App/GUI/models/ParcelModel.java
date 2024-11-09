package mpoljak.App.GUI.models;

public class ParcelModel extends GeoItemModel {
    private int inventoryNr;

    public ParcelModel(GpsModel gps1, GpsModel gps2, String description, int inventoryNr) {
        super(gps1, gps2, description, inventoryNr);
        this.inventoryNr = inventoryNr;
    }

    public int getInventoryNr() {
        return inventoryNr;
    }
}
