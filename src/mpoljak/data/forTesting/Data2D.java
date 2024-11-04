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

    public static void main(String[] args) {
        Data2D d1 = new Data2D(1, 0,2);
        Data2D d2 = new Data2D(2, 1,2);
//        Data4D d1 = new Data4D(5.2, "v", 5, 5.4, 1);
//        Data4D d2 = new Data4D(8.6, "avdca", -5, 55.4, 2);
//        Data4D d3 = new Data4D(-5.2, "sdvasa", 5, -0.4, 3);
//        Data4D d4 = new Data4D(16.2, "csadb", -24, 23.4, 4);
//        Data4D d5 = new Data4D(7.2, "cryw", 5, -5.4, 5);
//        Data4D d6 = new Data4D(-5.2, "cymuy", 18, 31.4, 6);
//        Data4D d7 = new Data4D(5.2, "kc fsa", -5, 5.4, 7);

        System.out.println(" VALUES: d1="+d1+"  d2="+d2);

        System.out.println("\nd1.cmp(d2, dim=1) -> " + d1.compareTo(d2,1));
        System.out.println("d1.cmp(d2, dim=2) -> " + d1.compareTo(d2,2));
        System.out.println("d1.cmp(d2, dim=3) -> " + d1.compareTo(d2,3));
        System.out.println("d1.cmp(d2, dim=4) -> " + d1.compareTo(d2,4));
        System.out.println("isSame? "+d1.isSame(d2));
        System.out.println("isSameKey? "+d1.isSameKey(d2));
//        :V{key=4D[a=201599356;b='8a65240o2g';c=981488427;d=165174539,4252; ID=25]}
//        :V{key=4D[a=233462430;b='c6hla0lg55';c=-734479398;d=412380266,6969; ID=36]}
    }
}
