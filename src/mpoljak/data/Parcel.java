package mpoljak.data;

import mpoljak.App.Logic.GeoDbClient;

import java.util.ArrayList;
import java.util.List;

public class Parcel extends GeoResource {
    private int parcelId;
    private String description;
    private GPS[] positions;
    private List<Property> properties;

    public Parcel(int parcelId, String description, GPS gps1, GPS gps2, int uniqueId) {
        super(uniqueId);
        if (parcelId < 0)
            throw new IllegalArgumentException("Error constructing Parcel: 'parcelId' must be positive integer.");
        if (gps1 == null || gps2 == null)
            throw new NullPointerException("Error constructing Parcel: GPS instance cannot be null.");

        this.parcelId = parcelId;
        this.description = description;

        this.positions = new GPS[2];
        this.positions[0] = gps1;
        this.positions[1] = gps2;

        this.properties = new ArrayList<Property>();
    }

    /**
     * Deep copy of other instance without property references.
     * @param other
     */
    public Parcel(Parcel other) {
        this(other.parcelId, other.description, new GPS(other.positions[0]),
                new GPS(other.positions[1]), other.uniqueId);
        this.properties = new ArrayList<>();
    }

    public int getParcelId() {
        return this.parcelId;
    }

    public String getDescription() {
        return this.description;
    }

    public GPS getGps1() {
        return this.positions[0];
    }

    public GPS getGps2() {
        return this.positions[1];
    }

    public List<Property> getProperties() {
        return this.properties;
    }

    public void setParcelId(int parcelId) {
        this.parcelId = parcelId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addProperty(Property property) {
        if (property == null)
            return;
        this.properties.add(property);
    }

    public void addProperties(List<Property> lProps) {
        if (lProps == null)
            return;
        this.properties.addAll(lProps);
    }

    public void removeProperty(Property property) {
        this.properties.remove(property);
    }

    public void removeProperties() {
        this.properties.clear();
    }

    @Override
    public String toString() {
        return String.format("Par[nr=%d;g1%s;g2%s,desc='%s']", this.parcelId, this.positions[0], this.positions[1], this.description);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Parcel)) return false;
        Parcel otherParcel = (Parcel) obj;
        return super.isSame(otherParcel);
//        && (
//                this.parcelId == otherParcel.parcelId
//                && this.positions[0].isSame(otherParcel.positions[0])
//                && this.positions[1].isSame(otherParcel.positions[1])
//                && (this.description == null ? "" : this.description).
//                    compareTo(otherParcel.description == null ? "" : otherParcel.description) == 0
//        );
    }

    @Override
    public GeoResource deepCopy() {
        Parcel newParcel = new Parcel(this);
        for (Property p : this.properties) {
            newParcel.addProperty(new Property(p));
        }
        return newParcel;
    }

    @Override
    public String toCsvLine(char delimiter, String delimiterReplacement, String blankSpaceReplacement) {
        if (delimiterReplacement == null)
            delimiterReplacement = "";
        char gpsDelim = (delimiter != '&') ? '&' : '%'; /* in order to differentiate inner attributes of gps with this
                                                            instance */
        String strGpsRepr = this.positions[0].toCsvLine(gpsDelim, null, null)
                            + delimiter
                            + this.positions[1].toCsvLine(gpsDelim, null, null);
        if (blankSpaceReplacement == null || blankSpaceReplacement.isEmpty())
            blankSpaceReplacement = " ";
        String parcelDesc = this.description == null || this.description.isEmpty()
                ? blankSpaceReplacement
                : (this.description.equals(blankSpaceReplacement) ? blankSpaceReplacement + this.description
                     : this.description.replaceAll(String.valueOf(delimiter), delimiterReplacement));
        return strGpsRepr + delimiter + this.parcelId + delimiter + parcelDesc;
    }

    @Override
    public GeoResource fromCsvLine(String csvLine, char delimiter, String delimiterReplacement,
                                   String blankSpaceReplacement) {
        if (csvLine == null || csvLine.isBlank())
            return null;
        String[] tokens = csvLine.split(String.valueOf(delimiter));
        if (tokens.length != 4)
            return null;
        char gpsDelim = (delimiter != '&') ? '&' : '%'; /* in order to differentiate inner attributes of gps with this
                                                            instance */
        GPS gFactory = new GPS('N',1,'E',1);    // JUST FOR calling member method
        GPS g1 = gFactory.fromCsvLine(tokens[0], gpsDelim, null, null);   // TODO?FACTORY design pattern?
        GPS g2 = gFactory.fromCsvLine(tokens[1], gpsDelim, null, null);
        int parcelId = Integer.parseInt(tokens[2]);
        if (delimiterReplacement == null)
            delimiterReplacement = "";
        if (blankSpaceReplacement == null)
            blankSpaceReplacement = "";
        String parcDesc;
        if (tokens[3].equals(blankSpaceReplacement))
            parcDesc = null;
        else if (tokens[3].equals(blankSpaceReplacement.repeat(2)))
            parcDesc = blankSpaceReplacement;
        else
            parcDesc = tokens[3].replaceAll(delimiterReplacement, String.valueOf(delimiter));
        return new Parcel(parcelId, parcDesc, g1, g2, -1);
    }
}
