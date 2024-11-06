package mpoljak.data.forTesting;

import mpoljak.dataStructures.searchTrees.KdTree.Error;
import mpoljak.dataStructures.searchTrees.KdTree.IKdComparable;
import mpoljak.dataStructures.searchTrees.KdTree.ISame;
import mpoljak.dataStructures.searchTrees.KdTree.Testing.IUniqueDataGenerator;
import mpoljak.utilities.DoubleComparator;
import mpoljak.utilities.CustomStringGenerator;

import java.util.Comparator;
import java.util.Random;

public class Data4D implements IKdComparable<Data4D>, ISame<Data4D> {
    private final int id;
    private int c;
    private double a;
    private double d;
    private String b;

    public static class Data4DComparator implements Comparator<Data4D> {
        @Override
        public int compare(Data4D o1, Data4D o2) {
            return Integer.compare(o1.id, o2.id);
        }
    }

    public static class Data4DGenerator implements IUniqueDataGenerator<Data4D, Integer> {

        @Override
        public Data4D generateInstance(Random valueGenerator, Integer uniqueId) {
            CustomStringGenerator stringIdGenerator = new CustomStringGenerator();
            stringIdGenerator.setCustomCharSet("abcde");

            return new Data4D(valueGenerator.nextDouble() * (valueGenerator.nextInt() % 10),
                    stringIdGenerator.nextString(10, valueGenerator),
                    valueGenerator.nextInt() % 100,
                    valueGenerator.nextDouble() * (valueGenerator.nextInt() % 10),
                    uniqueId);
        }

        @Override
        public Data4D copyConstruct(Data4D other, Integer uniqueId) {
            return new Data4D(other, uniqueId);
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
                && dc.compare(this.a, other.a) == 0
                && dc.compare(this.d, other.d) == 0
                && this.b.compareTo(other.b) == 0;
    }

    @Override
    public boolean isSame(Data4D other) {
        return this == other || this.id == other.id;
    }

    public static void main(String[] args) {
        Data4D d1 = new Data4D(201599356, "8a65240o2g", 981488427, 165174539.4252, 1);
        Data4D d2 = new Data4D(233462430, "c6hla0lg55", -734479398, 412380266.6969, 2);
//        Data4D d1 = new Data4D(5.2, "v", 5, 5.4, 1);
//        Data4D d2 = new Data4D(8.6, "avdca", -5, 55.4, 2);
//        Data4D d3 = new Data4D(-5.2, "sdvasa", 5, -0.4, 3);
//        Data4D d4 = new Data4D(16.2, "csadb", -24, 23.4, 4);
//        Data4D d5 = new Data4D(7.2, "cryw", 5, -5.4, 5);
//        Data4D d6 = new Data4D(-5.2, "cymuy", 18, 31.4, 6);
//        Data4D d7 = new Data4D(5.2, "kc fsa", -5, 5.4, 7);

        System.out.println("/* COMPARING RULES for specific dimensions of Data4D.Class\n" +
                " 1. dim  -  comp A(if equals then comp B)\n" +
                " 2. dim  -  comp C\n" +
                " 3. dim  -  comp D\n" +
                " 4. dim  -  comp B(if equals then comp C)\n" +
                "*/");
        System.out.println(" VALUES: d1="+d1+"  d2="+d2);

        System.out.println("\nd1.cmp(d2, dim=1) -> " + d1.compareTo(d2,1));
        System.out.println("d1.cmp(d2, dim=2) -> " + d1.compareTo(d2,2));
        System.out.println("d1.cmp(d2, dim=3) -> " + d1.compareTo(d2,3));
        System.out.println("d1.cmp(d2, dim=4) -> " + d1.compareTo(d2,4));
//        :V{key=4D[a=201599356;b='8a65240o2g';c=981488427;d=165174539,4252; ID=25]}
//        :V{key=4D[a=233462430;b='c6hla0lg55';c=-734479398;d=412380266,6969; ID=36]}
    }

}
