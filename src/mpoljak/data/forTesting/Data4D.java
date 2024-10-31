package mpoljak.data.forTesting;

import mpoljak.dataStructures.searchTrees.KdTree.Error;
import mpoljak.dataStructures.searchTrees.KdTree.IKdComparable;
import mpoljak.dataStructures.searchTrees.KdTree.ISimilar;
import mpoljak.utilities.DoubleComparator;

public class Data4D implements IKdComparable<Data4D>, ISimilar<Data4D> {
    private double a;
    private String b;
    private int c;
    private double d;

    public Data4D(double a, String b, int c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
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
        return "4D[" +
                "a=" + a +
                ", b='" + b + '\'' +
                ", c=" + c +
                ", d=" + d +
                ']';
    }

    @Override
    public int compareTo(Data4D other, int dim) {
        if (dim == 1) {
            int cmp = DoubleComparator.getInstance().compare(a, other.a);
            if (cmp != 0)
                return cmp;
            cmp = this.b.compareTo(other.b); // if previous equal, compare strings
            return Integer.compare(cmp, 0);
        }
        else if (dim == 2) {
            return Integer.compare(this.c, other.c);
        }
        else if (dim == 3) {
            return DoubleComparator.getInstance().compare(this.d, other.d);
        }
        else if (dim == 4) {
            int cmp = Integer.compare(this.b.compareTo(other.b), 0); // if greater than then could be other value than 1
            return cmp != 0 ? cmp : Integer.compare(this.c, other.c);

        }
        return Error.INVALID_DIMENSION.getErrCode();
    }

    @Override
    public Data4D copyConstruct() {
        return new Data4D(a, b, c, d);
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
        return this.isSameKey(other);
    }
}
