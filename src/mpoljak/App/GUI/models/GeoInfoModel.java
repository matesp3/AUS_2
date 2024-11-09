package mpoljak.App.GUI.models;

public class GeoInfoModel {
    private char geoType;
    private int number;
    private String description;

    public GeoInfoModel(char geoType, int number, String description) {
        this.geoType = geoType;
        this.number = number;
        this.description = description;
    }

    public char getGeoType() {
        return geoType;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }
}
