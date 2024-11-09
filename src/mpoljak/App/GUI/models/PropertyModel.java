package mpoljak.App.GUI.models;

public class PropertyModel extends GeoItemModel {
    private int propertyNr;

    public PropertyModel(GpsModel gps1, GpsModel gps2, String description, int propertyNr) {
        super(gps1, gps2, description, propertyNr);
        this.propertyNr = propertyNr;
    }

    public int getPropertyNr() {
        return propertyNr;
    }
}
