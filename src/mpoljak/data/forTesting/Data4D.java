package mpoljak.data.forTesting;

import mpoljak.dataStructures.searchTrees.KdTree.Error;
import mpoljak.dataStructures.searchTrees.KdTree.IKdComparable;
import mpoljak.dataStructures.searchTrees.KdTree.ISame;
import mpoljak.utilities.DoubleComparator;

import java.util.Comparator;

public class Data4D implements IKdComparable<Data4D>, ISame<Data4D> {
    private final int id;
    private int c;
    private double a;
    private double d;
    private String b;

    public static  class Data4DComparator implements Comparator<Data4D> {
        @Override
        public int compare(Data4D o1, Data4D o2) {
            return Integer.compare(o1.id, o2.id);
        }
    }

    public Data4D(double a, String b, int c, double d, int id) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Data4D(Data4D other, int uniqueId) {
        this(other.a, other.b, other.c, other.d, uniqueId);
    }

    public int getId() {
        return id;
    }

    public double getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public int getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    @Override
    public String toString() {
        return String.format("4D[a=%.4f;b='%s';c=%d;d=%.4f; ID=%d]", a, b, c, d, id);
    }

/*
 1. dim  -  comp A(else then B)
 2. dim  -  comp C
 3. dim  -  comp D
 4. dim  -  comp B(else then C)
*/
    @Override
    public int compareTo(Data4D other, int dim) {
        if (dim == 1) { // comp A, if equal -> comp B
            int cmp = DoubleComparator.getInstance().compare(a, other.a);
            if (cmp != 0)
                return cmp;
            cmp = this.b.compareTo(other.b); // if previous equal, compare strings
            return Integer.compare(cmp, 0);
        }
        else if (dim == 2) {    // comp C
            return Integer.compare(this.c, other.c);
        }
        else if (dim == 3) {    // comp D
            return DoubleComparator.getInstance().compare(this.d, other.d);
        }
        else if (dim == 4) {    // comp B, if equal -> comp C
            int cmp = Integer.compare(this.b.compareTo(other.b), 0); // if greater than then could be other value than 1
            return cmp != 0 ? cmp : Integer.compare(this.c, other.c);

        }
        return Error.INVALID_DIMENSION.getErrCode();
    }

    @Override
    public Data4D copyConstruct() {
        return new Data4D(a, b, c, d, id);
    }

    @Override
    public boolean fallsInto(Data4D other) {
        DoubleComparator dc = DoubleComparator.getInstance();
        return this.c <= other.c
                && dc.compare(this.a, other.a) != 1
                && dc.compare(this.d, other.d) != 1
                && this.b.compareTo(other.b) < 1;
    }

    @Override
    public void mapGreaterValues(Data4D other) {
        DoubleComparator dc = DoubleComparator.getInstance();
        if (this.c < other.c)
            this.c = other.c;
        if (dc.compare(other.a, this.a) == 1)
            this.a = other.a;
        if (dc.compare(other.d, this.d) == 1)
            this.d = other.d;
        if (other.b.compareTo(this.b) > 0)
            this.b = other.b;
    }

    @Override
    public boolean isSameKey(Data4D other) {
        if (this == other) return true;
        DoubleComparator dc = DoubleComparator.getInstance();
        return this.c == other.c
                && dc.compare(this.a, other.a) == 1
                && dc.compare(this.d, other.d) == 1
                && this.b.compareTo(other.b) == 0;
    }

    @Override
    public boolean isSame(Data4D other) {
        return this == other || this.id == other.id;
    }

}
