package mpoljak.App.GUI.models;

public abstract class GeoItemModel {
    private GpsModel gps1;
    private GpsModel gps2;
    private String description;
    private final int uniqueId;

    public GeoItemModel(GpsModel gps1, GpsModel gps2, String description, int uniqueId) {
        this.gps1 = gps1;
        this.gps2 = gps2;
        this.description = description;
        this.uniqueId = uniqueId;
    }

    public GpsModel getGps1() {
        return gps1;
    }

    public GpsModel getGps2() {
        return gps2;
    }

    public String getDescription() {
        return description;
    }

    public int getUniqueId() {
        return uniqueId;
    }
}
