package mpoljak.data.forTesting;

import mpoljak.dataStructures.searchTrees.KdTree.IKdComparable;
import mpoljak.dataStructures.searchTrees.KdTree.IKeySetChooseable;

public class MyInt implements IKdComparable<MyInt, Integer>, IKeySetChooseable {
    private int value;
    public MyInt(int value) {
        this.value = value;
    }
    public int getValue() { return this.value; }

    @Override
    public int compareTo(MyInt other, int dim) {
        if (dim == 1)
            return Integer.compare(this.value, other.value);
        return 0;
    }

    @Override
    public int compareTo(MyInt other, int dim, int otherKeySetId) {
        return compareTo(other, dim);
    }

    @Override
    public Integer getUpperBound(int dim) {
        return null;
    }

    @Override
    public String toString() {
        return "MyInt{" + value + '}';
    }

    @Override
    public void toggleComparedKey() {

    }

    @Override
    public void setComparedKey(int key) {

    }

    @Override
    public int getKeysCount() {
        return 1;
    }

    @Override
    public String getKeysDescription() {
        return "Key - value";
    }
}
