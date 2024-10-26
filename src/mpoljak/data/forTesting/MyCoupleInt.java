package mpoljak.data.forTesting;


import mpoljak.data.GPS;
import mpoljak.dataStructures.searchTrees.KdTree.IKdComparable;

public class MyCoupleInt  implements IKdComparable<MyCoupleInt, Integer>  {
    private int x;
    private int y;
    public MyCoupleInt(int x, int y) {
        this.x = x;
        this.y = y;
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
        return "[" + x + ", " + y + ']';
    }

    @Override
    public int compareTo(MyCoupleInt other, int dim) {
        if (dim == 1)
            return Integer.compare(y, other.y);
        if (dim == 2)
            return Integer.compare(x, other.x);
        return 0;
    }

    @Override
    public MyCoupleInt copyConstruct() {
        return new MyCoupleInt(x, y);
    }

    @Override
    public boolean fallsInto(MyCoupleInt other) {
        return x < other.x && y < other.y;
    }

    @Override
    public void mapGreaterValues(MyCoupleInt other) {
        if (other.x > x) x = other.x;
        if (other.y > y) y = other.y;
    }
}
