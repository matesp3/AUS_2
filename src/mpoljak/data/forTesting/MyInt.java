package mpoljak.data.forTesting;

import mpoljak.dataStructures.searchTrees.KdTree.IKdComparableII;

import java.util.ArrayList;

public class MyInt implements IKdComparableII<MyInt, Integer> {
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
    public Integer getDimensionKey(int dim) {
        return this.value;
    }

//    @Override
//    public int compareTo(MyInt other, int dim, int otherKeySetId) {
//        return compareTo(other, dim);
//    }
//
//    @Override
//    public Integer getUpperBound(int dim) {
//        return null;
//    }
//
//    @Override
//    public int compare(Integer k1, Integer k2, int dim) {
//        if (dim == 1)
//            return Integer.compare(k1, k2);
//        return 0;
//    }
//
//    @Override
//    public Integer getCompositeKey() {
//        return this.value;
//    }
//
//    @Override
//    public void toggleComparedKeySet() {
//
//    }
//
//    @Override
//    public void setComparedKeySet(int key) {
//
//    }
//
//    @Override
//    public int getKeySetsCount() {
//        return 1;
//    }
//
//    @Override
//    public int getCurrentKeySet() {
//        return 0;
//    }
//
//    @Override
//    public String getKeySetsDescription() {
//        return "Key - value";
//    }
@Override
public String toString() {
    return "MyInt{" + value + '}';
}
}
