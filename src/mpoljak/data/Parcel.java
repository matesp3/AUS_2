package mpoljak.data;

import java.util.ArrayList;
import java.util.List;

public class Parcel {
    private int parcelId;
    private String description;
    private GPS[] positions;
    private List<Property> properties;

    public Parcel(int parcelId, String description, GPS gps1, GPS gps2) {
        if (parcelId < 1)
            throw new IllegalArgumentException("Error constructing Parcel: 'parcelId' must be positive integer.");
        if (gps1 == null || gps2 == null)
            throw new NullPointerException("Error constructing Parcel: GPS instance cannot be null.");

        this.parcelId = parcelId;
        this.description = description;

        this.positions = new GPS[2];
        this.positions[0] = gps1;
        this.positions[1] = gps2;

        this.properties = new ArrayList<Property>();
    }

    public int getParcelId() {
        return this.parcelId;
    }

    public String getDescription() {
        return this.description;
    }

    public GPS[] getPositions() {
        return this.positions;
    }

    public Property[] getProperties() {
        return this.properties.toArray(new Property[this.properties.size()]);
    }

    public void addProperty(Property property) {
        if (property == null)
            return;
        this.properties.add(property);
    }
}
