package mpoljak.data.geo;

import mpoljak.dataStructures.IParams;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class Property extends GeoResource {
    private int propertyId;         // url: https://sk.wikipedia.org/wiki/S%C3%BApisn%C3%A9_%C4%8D%C3%ADslo
    private String description;
    private GregorianCalendar evidedFrom;
    GPS[] positions;                // positions by which is Property bounded
    private List<Parcel> parcels;   // on which Property is located

    public Property(int propertyId, String description, GPS gps1, GPS gps2, int uniqueId) {
        this(propertyId, description, gps1, gps2, uniqueId, null);
    }

    public Property(int propertyId, String description, GPS gps1, GPS gps2, int uniqueId, GregorianCalendar evidedFrom) {
        super(uniqueId);
        if (propertyId < 0)
            throw new IllegalArgumentException("Error constructing Property: 'propertyId' must be positive integer.");
        if (gps1 == null || gps2 == null)
            throw new NullPointerException("Error constructing Property: GPS instance cannot be null.");

        this.propertyId = propertyId;
        this.description = description;
        this.evidedFrom = evidedFrom;

        this.positions = new GPS[2];    // both positions could have latitude 'N' at the same time
        this.positions[0] = gps1;
        this.positions[1] = gps2;

        this.parcels = new ArrayList<Parcel>();
    }

    /**
     * Deep copy of other instance without parcel references
     * @param other
     */
    public Property(Property other) {
        this(other.propertyId, other.description, new GPS(other.positions[0]),
                new GPS(other.positions[1]), other.uniqueId, null);
        this.parcels = new ArrayList<>();
    }

    public int getPropertyId() {
        return propertyId;
    }

    public String getDescription() {
        return description;
    }

    public GPS getGps1() {
        return this.positions[0];
    }

    public GPS getGps2() {
        return this.positions[1];
    }

    public List<Parcel> getParcels() {
        return this.parcels;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addParcel(Parcel parcel) {
        if (parcel == null)
            return;
        this.parcels.add(parcel);
    }

    public void addParcels(List<Parcel> lParcels) {
        if (lParcels == null)
            return;
        this.parcels.addAll(lParcels);
    }

    public GregorianCalendar getEvidedFrom() {
        return evidedFrom;
    }

    public void setEvidedFrom(GregorianCalendar evidedFrom) {
        this.evidedFrom = evidedFrom;
    }

    public void removeParcel(Parcel parcel) {
        this.parcels.remove(parcel);
    }

    public void removeParcels() {
        this.parcels.clear();
    }

    @Override
    public String toString() {
//        return String.format("Prop[id=%d;g1%s;g2%s]", this.propertyId, this.positions[0], this.positions[1]);
        return String.format("Prop[nr=%d;g1%s;g2%s,desc='%s']", this.propertyId, this.positions[0], this.positions[1], this.description);
//        return String.format("Prop[id=%d]", this.propertyId); // temporally
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Property)) return false;
        Property otherProp = (Property) obj;
        return super.isSame(otherProp);
//        && this.propertyId == otherProp.propertyId &&
//                this.positions[0].isSame(otherProp.positions[0]) && this.positions[1].isSame(otherProp.positions[1]);
//                && (this.description == null ? "" : this.description).
//                    compareTo(other.description == null ? "" : other.description) == 0;
    }

    @Override
    public GeoResource deepCopy() {
        Property newProperty = new Property(this);
        for (Parcel p : this.parcels) {
            newProperty.addParcel(new Parcel(p));
        }
        return newProperty;
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
        return strGpsRepr + delimiter + this.propertyId + delimiter + parcelDesc;
    }

    @Override
    public GeoResource fromCsvLine(String csvLine, char delimiter, String delimiterReplacement
            , String blankSpaceReplacement) {
        if (csvLine == null || csvLine.isBlank())
            return null;
        String[] tokens = csvLine.split(String.valueOf(delimiter));
        if (tokens.length != 4)
            return null;
        char gpsDelim = (delimiter != '&') ? '&' : '%'; /* in order to differentiate inner attributes of gps with this
                                                            instance */
        GPS gFactory = new GPS('N',1,'E',1);    // JUST FOR calling member method
        GPS g1 = gFactory.fromCsvLine(tokens[0], gpsDelim, null, null);   // TODO? FACTORY design pattern?
        GPS g2 = gFactory.fromCsvLine(tokens[1], gpsDelim, null, null);
        int propId = Integer.parseInt(tokens[2]);
        if (delimiterReplacement == null)
            delimiterReplacement = "";
        if (blankSpaceReplacement == null)
            blankSpaceReplacement = "";
        String propDesc;
        if (tokens[3].equals(blankSpaceReplacement))
            propDesc = null;
        else if (tokens[3].equals(blankSpaceReplacement.repeat(2)))
            propDesc = blankSpaceReplacement;
        else
            propDesc = tokens[3].replaceAll(delimiterReplacement, String.valueOf(delimiter));
        return new Property(propId, propDesc, g1, g2, -1);
    }

    @Override
    public void update(IParams modified) {
        if (!(modified instanceof PropertyParams))
            return;
        PropertyParams par = (PropertyParams) modified;
        this.propertyId = par.getPropertyNr();
        this.description = par.getDescription();
        this.positions[0] = new GPS(par.getGps1());
        this.positions[1] = new GPS(par.getGps2());
    }
}
