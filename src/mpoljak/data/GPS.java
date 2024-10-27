package mpoljak.data;

import mpoljak.dataStructures.searchTrees.KdTree.Error;
import mpoljak.dataStructures.searchTrees.KdTree.IKdComparable;

import mpoljak.dataStructures.searchTrees.KdTree.ISimilar;
import mpoljak.utilities.DoubleComparator;

import java.util.Comparator;

public class GPS implements IKdComparable<GPS>, ISimilar<GPS> {
    private static final double MAX_LATITUDE_DEGREES = 90.0;
    private static final double MAX_LONGITUDE_DEGREES = 180.0;

    private char latitude;    // zem. sirka {N, S}
    private double latDeg;    // if 'S', represent as negative
    private char longitude;   // zem. dlzka {E, W}
    private double longDeg;   // if 'W' represent as negative

    public GPS(char latitude, double latDeg, char longitude, double longDeg) {
        if (latitude != 'N' && latitude != 'S')
            throw new IllegalArgumentException("Error creating GPS instance: latitude must be 'N' or 'S'");
        if (longitude != 'E' && longitude != 'W')
            throw new IllegalArgumentException("Error creating GPS instance: longitude must be 'E' or 'W'");
        if (latDeg < 0 || longDeg < 0)
            throw new IllegalArgumentException("Error creating GPS instance: latDeg and longDeg must be non-negative");
        if (latDeg > MAX_LATITUDE_DEGREES)
            throw new IllegalArgumentException("Error creating GPS instance: latDeg must be between 0 and 90 degrees");
        if (longDeg > MAX_LONGITUDE_DEGREES)
            throw new IllegalArgumentException("Error creating GPS instance: longDeg must be between 0 and 180 degrees");
        this.latitude = latitude;
        this.latDeg = latDeg;
        this.longitude = longitude;
        this.longDeg = longDeg;
    }

    /** Copy constructor */
    public GPS(GPS otherGPS) {
        if (otherGPS == null)
            throw new NullPointerException("Error copy constructing GPS instance: otherGPS instance is null");
        this.latitude = otherGPS.getLatitude();
        this.latDeg = otherGPS.getLatDeg();
        this.longitude = otherGPS.getLongitude();
        this.longDeg = otherGPS.getLongDeg();
    }
    /** N | S */
    public char getLatitude() {
        return this.latitude;
    }

    /** E | W */
    public char getLongitude() {
        return this.longitude;
    }

    /** Degrees for N | S */
    public double getLatDeg() {
        return this.latDeg;
    }

    /** Degrees for E | W */
    public double getLongDeg() {
        return this.longDeg;
    }

    @Override
    public GPS copyConstruct() {
        return new GPS(this);
    }

    @Override
    public boolean fallsInto(GPS other) {
        DoubleComparator dc = DoubleComparator.getInstance();
        return dc.compare(this.convertLatitude(), other.convertLatitude()) != 1
                && dc.compare(this.convertLongitude(), other.convertLongitude()) != 1;
    }

    @Override
    public void mapGreaterValues(GPS other) {
        DoubleComparator dc = DoubleComparator.getInstance();
        // if other is greater, then update yourself by mapping other's greater values
        if (dc.compare(other.convertLatitude(), this.convertLatitude()) == 1) {
            this.latDeg = other.latDeg;
            this.latitude = other.latitude;
        }
        if (dc.compare(other.convertLongitude(), this.convertLongitude()) == 1) {
            this.longDeg = other.longDeg;
            this.longitude = other.longitude;
        }
    }

    @Override
    public int compareTo(GPS other, int dim) {
        if (dim == 1) {
            return DoubleComparator.getInstance().compare(this.convertLatitude(), other.convertLatitude());
        }
        else if (dim == 2) {
            return DoubleComparator.getInstance().compare(this.convertLongitude(), other.convertLongitude());
        }
        return Error.INVALID_DIMENSION.getErrCode();
    }

    @Override
    public String toString() {
        int c1 = this.latitude == 'N' ? 1 : -1;
        int c2 = this.longitude == 'E' ? 1 : -1;
        return String.format("[%.4f;%.4f]", c1 * this.latDeg, c2 * this.longDeg);
    }

    @Override
    public boolean isSameKey(GPS other) {
        if (this == other) return true;
        DoubleComparator dc = DoubleComparator.getInstance();
        return dc.compare(other.convertLatitude(), this.convertLatitude()) == 0
                && dc.compare(other.convertLongitude(), this.convertLongitude()) == 0;
    }

    @Override
    public boolean isSame(GPS other) {
        return this.isSameKey(other);       // because GPS instance can behave as key and as data at the same time
    }

    private double convertLatitude() {
        return (this.latitude == 'N' ? 1 : -1) * this.latDeg;
    }

    private double convertLongitude() {
        return (this.longitude == 'E' ? 1 : -1) * this.longDeg;
    }

    public static void testFallingIntoGPS() {
        System.out.println(" Test FALLING INTO GPS (falling into means that all key values of first GPS are less" +
                                     " or equal to second GPS key values)\n");

        GPS g1 = new GPS('N', 24.5, 'E', 5.0);
        GPS g3 = new GPS('N', 5.5, 'E', 5.0);
        GPS g4 = new GPS('N', 23.5, 'E', 4.0);
        GPS g5 = new GPS('N', 24.5, 'W', 5.0);
        GPS g6 = new GPS('S', 25.5, 'W', 5.0);
        GPS g7 = new GPS('S', 25.5, 'E', 7.0);
        GPS g8 = new GPS('N', 25.5, 'E', 7.0);
        GPS g9 = new GPS('N', 25.5, 'E', 4.0);

        System.out.println(g3 + " falls into " + g1 + ":" + g3.fallsInto(g1));
        System.out.println(g4 + " falls into " + g1 + ":" + g4.fallsInto(g1));
        System.out.println(g5 + " falls into " + g1 + ":" + g5.fallsInto(g1));
        System.out.println(g6 + " falls into " + g1 + ":" + g6.fallsInto(g1));
        System.out.println(g7 + " falls into " + g1 + ":" + g7.fallsInto(g1));
        System.out.println(g8 + " falls into " + g1 + ":" + g8.fallsInto(g1));
        System.out.println(g9 + " falls into " + g1 + ":" + g9.fallsInto(g1));
    }

}
