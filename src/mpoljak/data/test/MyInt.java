package mpoljak.data.test;

import mpoljak.dataStructures.ITableKey;
import mpoljak.dataStructures.KdTree.IKdComparable;
import mpoljak.dataStructures.KdTree.ISame;

public class MyInt implements IKdComparable<MyInt>, ISame<MyInt> {
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
    public MyInt copyConstruct() {
        return new MyInt(this.value);
    }

    @Override
    public boolean fallsInto(MyInt other) {
        return this.value <= other.value;
    }

    @Override
    public void mapGreaterValues(MyInt other) {
        if (other.value > this.value)
            this.value = other.value;
    }

    @Override
    public boolean isSameKey(MyInt other) {
        if (this == other) return true;
        return this.value == other.value;
    }

    @Override
    public boolean isSame(MyInt other) {
        return (this == other) || (this.value == other.value);
    }

    @Override
    public String toString() {
        return "MyInt{" + value + '}';
    }

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public boolean equalsTo(ITableKey other) {
        return false;
    }
}
