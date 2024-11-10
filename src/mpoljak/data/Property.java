package mpoljak.data;

import java.util.ArrayList;
import java.util.List;

public class Property extends GeoResource {
    private int propertyId;         // url: https://sk.wikipedia.org/wiki/S%C3%BApisn%C3%A9_%C4%8D%C3%ADslo
    private String description;
    GPS[] positions;                // positions by which is Property bounded
    private List<Parcel> parcels;   // on which Property is located

    public Property(int propertyId, String description, GPS gps1, GPS gps2, int uniqueId) {
        super(uniqueId);
        if (propertyId < 0)
            throw new IllegalArgumentException("Error constructing Property: 'propertyId' must be positive integer.");
        if (gps1 == null || gps2 == null)
            throw new NullPointerException("Error constructing Property: GPS instance cannot be null.");

        this.propertyId = propertyId;
        this.description = description;

        this.positions = new GPS[2];    // both positions could have latitude 'N' at the same time
        this.positions[0] = gps1;
        this.positions[1] = gps2;

        this.parcels = new ArrayList<Parcel>();
    }

    /**
     * Deep copy of other instance without parcel references
     * @param other
     */
    public Property(Property other) {
        this(other.propertyId, other.description, new GPS(other.positions[0]),
                new GPS(other.positions[1]), other.uniqueId);
        this.parcels = new ArrayList<>();
    }

    public int getPropertyId() {
        return propertyId;
    }

    public String getDescription() {
        return description;
    }

    public GPS getGps1() {
        return this.positions[0];
    }

    public GPS getGps2() {
        return this.positions[1];
    }

    public List<Parcel> getParcels() {
        return this.parcels;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addParcel(Parcel parcel) {
        if (parcel == null)
            return;
        this.parcels.add(parcel);
    }

    public void addParcels(List<Parcel> lParcels) {
        if (lParcels == null)
            return;
        this.parcels.addAll(lParcels);
    }

    public void removeParcel(Parcel parcel) {
        this.parcels.remove(parcel);
    }

    public void removeParcels() {
        this.parcels.clear();
    }

    @Override
    public String toString() {
//        return String.format("Prop[id=%d;g1%s;g2%s]", this.propertyId, this.positions[0], this.positions[1]);
        return String.format("Prop[id=%d;g1%s]", this.propertyId, this.positions[0]);
//        return String.format("Prop[id=%d]", this.propertyId); // temporally
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Property)) return false;
        Property otherProp = (Property) obj;
        return super.isSame(otherProp);
//        && this.propertyId == otherProp.propertyId &&
//                this.positions[0].isSame(otherProp.positions[0]) && this.positions[1].isSame(otherProp.positions[1]);
//                && (this.description == null ? "" : this.description).
//                    compareTo(other.description == null ? "" : other.description) == 0;
    }

    @Override
    public GeoResource deepCopy() {
        Property newProperty = new Property(this);
        for (Parcel p : this.parcels) {
            newProperty.addParcel(new Parcel(p));
        }
        return newProperty;
    }
}
