package mpoljak.utilities;

/** Singleton implementation of DoubleComparator which compares double values according to given epsilon.
 *  It is a singleton because of the need to have 1 same epsilon for whole application logic. */
public class DoubleComparator {

    private static DoubleComparator instance = null;

    public static DoubleComparator getInstance() {
        if (instance == null) {
            instance = new DoubleComparator();
        }
        return instance;
    }

    private double epsilon;

    private DoubleComparator() { this.epsilon = 0.0001; }

    /**
     * Compares value 'a' to value 'b'
     * @param a
     * @param b
     * @return case a < b then -1; case a == b(regarding set epsilon) then 0; case a > b then 1
     */
    public int compare(double a, double b) {
        return this.compare(a, b, this.epsilon);
    }

    public int compare(double a, double b, double epsilon) {
        double diff = a - b;
        return (Math.abs(diff) < epsilon) ? 0 : (int) Math.signum(diff);
    }

    /**
     * Sets maximum difference in absolute value between two double values in order to claim that they are equal.
     * */
    public void setEpsilon(double newEpsilon) { this.epsilon = Math.abs(newEpsilon); }

    public static void showExamplesOfComparing() {
        System.out.println("\n -- COMPARING 2 double values: a, b --\n POSSIBLE RESULTS of compare(a,b):");
        System.out.println("    * compare(a,b) = -1 ['a' is less than 'b']");
        System.out.println("    * compare(a,b) =  0 ['a' is is equal to 'b' for given epsilon]");
        System.out.println("    * compare(a,b) =  1 ['a' is greater than 'b']\n");

        double epsilon = 0.0001;

        printRes(1.543,1.5432, epsilon);
        printRes(1.5435,1.5432, epsilon);
        printRes(1.5429,1.5432, epsilon);
        printRes(1.542,1.5432, epsilon);
        printRes(1.5431,1.5432, epsilon);
        printRes(1.54311,1.5432, epsilon);
        printRes(1.5431,1.54321, epsilon);
    }

    private static void printRes(double a, double b, double eps) {
        DoubleComparator dComp = DoubleComparator.getInstance();
        dComp.setEpsilon(eps);
        System.out.println(String.format("compare(a=%.5f; b=%.5f) => %d [epsilon=%f]", a, b, dComp.compare(a, b), eps));
    }
}
