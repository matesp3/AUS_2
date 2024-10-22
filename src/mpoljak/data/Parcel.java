package mpoljak.data;

import mpoljak.dataStructures.searchTrees.KdTree.IKdComparable;
import mpoljak.dataStructures.searchTrees.KdTree.IKeySetChooseable;

import java.util.ArrayList;
import java.util.List;

public class Parcel implements IKdComparable<Parcel, Double>, IKeySetChooseable {
    private int parcelId;
    private String description;
    private GPS[] positions;
    private List<Property> properties;
    private int currentKeySetId;

    public Parcel(int parcelId, String description, GPS gps1, GPS gps2) {
        if (parcelId < 0)
            throw new IllegalArgumentException("Error constructing Parcel: 'parcelId' must be positive integer.");
        if (gps1 == null || gps2 == null)
            throw new NullPointerException("Error constructing Parcel: GPS instance cannot be null.");

        this.parcelId = parcelId;
        this.description = description;

        this.positions = new GPS[2];
        this.positions[0] = gps1;
        this.positions[1] = gps2;

        this.properties = new ArrayList<Property>();
        this.currentKeySetId = 0;
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

    @Override
    public int compareTo(Parcel other, int dim) {
        return this.positions[this.currentKeySetId].compareTo(other.positions[0], dim);//always compare with first key set
    }

    @Override
    public int compareTo(Parcel other, int dim, int otherKeySetId) {
        return this.positions[this.currentKeySetId].compareTo(other.positions[otherKeySetId], dim);
    }

    @Override
    public Double getUpperBound(int dim) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void toggleComparedKey() {
        this.currentKeySetId = (this.currentKeySetId + 1) % this.positions.length;
    }

    @Override
    public void setComparedKey(int key) {
        if (key > 0 && key < (this.positions.length + 1))
            this.currentKeySetId = key - 1;
    }

    @Override
    public int getKeysCount() {
        return this.positions.length;
    }

    @Override
    public String getKeySetsDescription() {
        return " * KeySet1 - GPS position 1 [keySetId=1],\n * KeySet2 - GPS position 2 [keySetId=2]";
    }

    @Override
    public String toString() {
        return String.format("Par[id=%d;g1%s;g2%s]", this.parcelId, this.positions[0], this.positions[1]);
    }
}
