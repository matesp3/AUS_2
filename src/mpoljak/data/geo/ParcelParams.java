package mpoljak.data.geo;

import mpoljak.dataStructures.IParams;

public class ParcelParams implements IParams {
    private int uniqueId;
    private int parcelId;
    private String description;
    private GPS gps1;
    private GPS gps2;

    public ParcelParams(int parcelId, String description, GPS gps1, GPS gps2, int uniqueId) {
        this.parcelId = parcelId;
        this.description = description;
        this.gps1 = gps1;
        this.gps2 = gps2;
        this.uniqueId = uniqueId;
    }

    public int getParcelId() {
        return parcelId;
    }

    public String getDescription() {
        return description;
    }

    public GPS getGps1() {
        return gps1;
    }

    public GPS getGps2() {
        return gps2;
    }

    public void setParcelId(int parcelId) {
        this.parcelId = parcelId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGps1(GPS gps1) {
        this.gps1 = gps1;
    }

    public void setGps2(GPS gps2) {
        this.gps2 = gps2;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }
}