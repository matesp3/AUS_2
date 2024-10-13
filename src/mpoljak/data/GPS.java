package mpoljak.data;

import mpoljak.dataStructures.searchTrees.KdTree.IKdComparable;

public class GPS implements IKdComparable<GPS, Double> {
    private static final double MAX_LATITUDE_DEGREES = 90.0;
    private static final double MAX_LONGITUDE_DEGREES = 180.0;

    private final char latitude;    // zem. sirka {N, S}
    private final double latDeg;    // if 'S', represent as negative
    private final char longitude;   // zem. dlzka {E, W}
    private final double longDeg;   // if 'W' represent as negative

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
    public int compareTo(GPS other, int dim) {
        // TODO
        return 0;
    }

    @Override
    public Double getUpperBound(int dim) {
        return null;
    }
}
