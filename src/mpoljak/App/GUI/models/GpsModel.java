package mpoljak.App.GUI.models;

public class GpsModel {
    private char latitude;    // zem. sirka {N, S}
    private double latDeg;    // if 'S', represent as negative
    private char longitude;   // zem. dlzka {E, W}
    private double longDeg;   // if 'W' represent as negative

    public GpsModel(char latitude, double latDeg, char longitude, double longDeg) {
        this.latitude = latitude;
        this.latDeg = latDeg;
        this.longitude = longitude;
        this.longDeg = longDeg;
    }

    public char getLatitude() {
        return latitude;
    }

    public void setLatitude(char latitude) {
        this.latitude = latitude;
    }

    public double getLatDeg() {
        return latDeg;
    }

    public void setLatDeg(double latDeg) {
        this.latDeg = latDeg;
    }

    public char getLongitude() {
        return longitude;
    }

    public void setLongitude(char longitude) {
        this.longitude = longitude;
    }

    public double getLongDeg() {
        return longDeg;
    }

    public void setLongDeg(double longDeg) {
        this.longDeg = longDeg;
    }
}
