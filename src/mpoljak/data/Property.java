package mpoljak.data;

import mpoljak.dataStructures.searchTrees.KdTree.IKdComparable;
import mpoljak.dataStructures.searchTrees.KdTree.IKeySetChooseable;

import java.util.ArrayList;
import java.util.List;

public class Property implements IKdComparable<Property, Double>, IKeySetChooseable {
    private int propertyId;         // url: https://sk.wikipedia.org/wiki/S%C3%BApisn%C3%A9_%C4%8D%C3%ADslo
    private String description;
    GPS[] positions;                // positions by which is Property bounded
    private List<Parcel> parcels;   // on which Property is located

    public Property(int propertyId, String description, GPS gps1, GPS gps2) {
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
    public int compareTo(Property other, int dim) {
        return 0;
    }

    @Override
    public int compareTo(Property other, int dim, int otherKeySetId) {
        return 0;
    }

    @Override
    public Double getUpperBound(int dim) {
        int cmp = this.positions[0].compareTo(this.positions[1], dim);
        if (cmp == -1 || cmp == 0)
            return this.positions[1].getUpperBound(dim);
        if (cmp == 1)
            return this.positions[0].getUpperBound(dim);
        return null;
    }

    @Override
    public void toggleComparedKey() {

    }

    @Override
    public void setComparedKey(int key) {

    }

    @Override
    public int getKeysCount() {
        return 0;
    }

    @Override
    public String getKeysDescription() {
        return "";
    }
}
