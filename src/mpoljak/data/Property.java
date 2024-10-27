package mpoljak.data;

import mpoljak.dataStructures.searchTrees.KdTree.ISimilar;

import java.util.ArrayList;
import java.util.List;

//public class Property extends GeoData {
public class Property implements IGpsLocalizable {
//    private int instanceId;
    private int propertyId;         // url: https://sk.wikipedia.org/wiki/S%C3%BApisn%C3%A9_%C4%8D%C3%ADslo
    private String description;
    GPS[] positions;                // positions by which is Property bounded
    private List<Parcel> parcels;   // on which Property is located
    private int currentKeySetId;

//    public Property(int instanceId, int propertyId, String description, GPS gps1, GPS gps2) {
    public Property(int propertyId, String description, GPS gps1, GPS gps2) {
        if (propertyId < 0)
            throw new IllegalArgumentException("Error constructing Property: 'propertyId' must be positive integer.");
        if (gps1 == null || gps2 == null)
            throw new NullPointerException("Error constructing Property: GPS instance cannot be null.");

//        this.instanceId = instanceId;
        this.propertyId = propertyId;
        this.description = description;

        this.positions = new GPS[2];    // both positions could have latitude 'N' at the same time
        this.positions[0] = gps1;
        this.positions[1] = gps2;

        this.parcels = new ArrayList<Parcel>();
        this.currentKeySetId = 0;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public String getDescription() {
        return description;
    }

    public GPS[] getPositions() {
//        return new GPS[] { new GPS(this.positions[0]), new GPS(this.positions[1]) };
        return this.positions;
    }

    public Parcel[] getParcels() {
        return this.parcels.toArray(new Parcel[this.parcels.size()]);
    }

    public void addParcel(Parcel parcel) {
        if (parcel == null)
            return;
        this.parcels.add(parcel);
    }

    @Override
    public String toString() {
//        return String.format("Prop[id=%d;g1%s;g2%s]", this.propertyId, this.positions[0], this.positions[1]);
        return String.format("Prop[id=%d;g1%s]", this.propertyId, this.positions[0]);
//        return String.format("Prop[id=%d]", this.propertyId); // temporally
    }

//    @Override
//    public boolean isSame(IGpsLocalizable other) {
//        if (!(other instanceof Property)) return false;
//        Property otherProp = (Property) other;
//        return this.propertyId == otherProp.propertyId &&
//                this.positions[0].isSame(otherProp.positions[0]) && this.positions[1].isSame(otherProp.positions[1]);
//                && (this.description == null ? "" : this.description).
//                    compareTo(other.description == null ? "" : other.description) == 0;
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Property)) return false;
        Property otherProp = (Property) obj;
        return this.propertyId == otherProp.propertyId &&
                this.positions[0].isSame(otherProp.positions[0]) && this.positions[1].isSame(otherProp.positions[1]);
//                && (this.description == null ? "" : this.description).
//                    compareTo(other.description == null ? "" : other.description) == 0;
    }

    @Override
    public boolean isSame(IGpsLocalizable other) {
        return false;
    }
}
