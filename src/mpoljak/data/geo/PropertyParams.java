package mpoljak.data.geo;

import mpoljak.dataStructures.IParams;

import java.util.GregorianCalendar;

public class PropertyParams implements IParams {
    private int uniqueId;
    private int propertyNr;
    private String description;
    private GregorianCalendar evidedFrom;
    private GPS gps1;
    private GPS gps2;

    public PropertyParams(int propertyNr, String description, GPS gps1, GPS gps2, int uniqueId, GregorianCalendar evidedFrom) {
        this.propertyNr = propertyNr;
        this.description = description;
        this.gps1 = gps1;
        this.gps2 = gps2;
        this.evidedFrom = evidedFrom;
        this.uniqueId = uniqueId;
    }

    public int getPropertyNr() {
        return propertyNr;
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

    public void setPropertyNr(int propertyNr) {
        this.propertyNr = propertyNr;
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

    public GregorianCalendar getEvidedFrom() {
        return evidedFrom;
    }

    public void setEvidedFrom(GregorianCalendar evidedFrom) {
        this.evidedFrom = evidedFrom;
    }

    @Override
    public IParams cloneInstance() {
        return new PropertyParams(this.propertyNr, this.description, new GPS(this.gps1), new GPS(this.gps2), this.uniqueId, this.evidedFrom);
    }
}
