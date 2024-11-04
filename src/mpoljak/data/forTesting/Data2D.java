package mpoljak.data.forTesting;

import mpoljak.dataStructures.searchTrees.KdTree.Error;
import mpoljak.dataStructures.searchTrees.KdTree.IKdComparable;
import mpoljak.dataStructures.searchTrees.KdTree.ISame;

import java.util.Comparator;

public class Data2D implements IKdComparable<Data2D>, ISame<Data2D> {
    private final int id;
    private int x;
    private int y;

    public static  class Data2DComparator implements Comparator<Data2D> {

        @Override
        public int compare(Data2D o1, Data2D o2) {
            return Integer.compare(o1.id, o2.id);
        }
    }

    public Data2D(int uniqueId, int x, int y) {
        this.id = uniqueId;
        this.x = x;
        this.y = y;
    }

    public Data2D(Data2D other, int uniqueId) {
        this(uniqueId, other.x, other.y);
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    @Override
    public int compareTo(Data2D other, int dim) {
        if (dim == 1)
            return Integer.compare(this.x, other.x);
        if (dim == 2)
            return Integer.compare(this.y, other.y);

        return Error.INVALID_DIMENSION.getErrCode();
    }

    @Override
    public Data2D copyConstruct() {
        return new Data2D(this.id, this.x, this.y);
    }

    @Override
    public boolean fallsInto(Data2D other) {
        return this.x <= other.x && this.y <= other.y;
    }

    @Override
    public void mapGreaterValues(Data2D other) {
        if (other.x > this.x)
            this.x = other.x;
        if (other.y > this.y)
            this.y = other.y;
    }

    @Override
    public boolean isSameKey(Data2D other) {
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public boolean isSame(Data2D other) {
        return this == other || this.id == other.id;
    }

    @Override
    public String toString() {
        return String.format("2D[ID=%d, x=%2d, y=%2d]", this.id, this.x, this.y);
    }
}
