package mpoljak.data.forTesting;


public class MyIntervalInt {
    private int min;
    private int max;
    public MyIntervalInt(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
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
        return "<" + min + ", " + max + '>';
    }
}
