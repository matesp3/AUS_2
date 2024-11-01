package mpoljak.data.forTesting;


import mpoljak.dataStructures.searchTrees.KdTree.IKdComparable;
import mpoljak.dataStructures.searchTrees.KdTree.ISame;

public class MyCoupleInt implements IKdComparable<MyCoupleInt>, ISame<MyCoupleInt> {
    private int x;
    private int y;
    private String description;
    public MyCoupleInt(int x, int y, String desc) {
        this.x = x;
        this.y = y;
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

//    @Override
//    public int compareTo(MyIntervalInt other, int dim) {
//        if (dim == 1)
//            return Integer.compare(this.min, other.min);
//        return 0;
//    }
//
//    @Override
//    public int compareTo(MyIntervalInt other, int dim, int otherKeySetId) {
//        return compareTo(other, dim);
//    }
//
//    @Override
//    public Integer getUpperBound(int dim) {
//        return this.max;
//    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "; desc=" + description + "]";
    }

    @Override
    public int compareTo(MyCoupleInt other, int dim) {
        if (dim == 1)
            return Integer.compare(x, other.x);
        if (dim == 2)
            return Integer.compare(y, other.y);
        return 0;
    }

    @Override
    public MyCoupleInt copyConstruct() {
        return new MyCoupleInt(x, y, description);
    }

    @Override
    public boolean fallsInto(MyCoupleInt other) {
        return x <= other.x && y <= other.y;
    }

    @Override
    public void mapGreaterValues(MyCoupleInt other) {
        if (other.x > x) x = other.x;
        if (other.y > y) y = other.y;
    }

    @Override
    public boolean isSameKey(MyCoupleInt other) {
        if (this == other) return true;

        return x == other.x && y == other.y &&
                (description == null ? "" : description).compareTo(
                        other.description == null ? "" : other.description) == 0;
    }

    @Override
    public boolean isSame(MyCoupleInt other) {
        return isSameKey(other);                // because it can behave as key and as data at the same time
    }

}
