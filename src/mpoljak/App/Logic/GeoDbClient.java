package mpoljak.App.Logic;

import mpoljak.data.GPS;
import mpoljak.data.GeoResource;
import mpoljak.data.Parcel;
import mpoljak.data.Property;
import mpoljak.dataStructures.searchTrees.KdTree.KDTree;
import mpoljak.utilities.IntegerIdGenerator;

import java.util.ArrayList;
import java.util.List;

public class GeoDbClient {
    private KDTree<Property, GeoResource, GPS> kdTreeProps;
    private KDTree<Parcel, GeoResource, GPS> kdTreeParcels;
    private KDTree<GeoResource, GeoResource, GPS> kdTreeResources;
    private IntegerIdGenerator idGenerator;

    public GeoDbClient() {
        this.kdTreeProps = new KDTree<>(2);
        this.kdTreeParcels = new KDTree<>(2);
        this.kdTreeResources = new KDTree<>(2);
        this.idGenerator = IntegerIdGenerator.getInstance();
    }

    /** Task 1 - finds all properties, whose one of the GPS positions leans on given GPS position.*/
    public List<Property> findProperties(GPS gps) {
        List<Property> lResultProps = this.retrieveData(gps, null, this.kdTreeProps);
        List<Property> lResult = new ArrayList<>(lResultProps.size());
        for (Property prop : lResultProps) {
            lResult.add((Property) prop.deepCopy());
        }
        return lResult;
    };

    /** Task 2 - finds all parcels, whose one of the GPS positions leans on given GPS position. */
    public List<Parcel> findParcels(GPS gps) {
        List<Parcel> lResultParcels = this.retrieveData(gps, null, this.kdTreeParcels);
        List<Parcel> lResult = new ArrayList<>(lResultParcels.size());
        for (Parcel parcel : lResultParcels) {
            lResult.add((Parcel) parcel.deepCopy());
        }
        return lResult;
    };

    /** Task 3 - finds all geo resources, which have in common one of the given GPS positions */
    public List<GeoResource> findGeoResources(GPS gps1, GPS gps2) {
        List<GeoResource> lResultData = this.retrieveData(gps1, gps2, this.kdTreeResources);
        List<GeoResource> lResult = new ArrayList<>(lResultData.size());
        for (GeoResource element : lResultData) {
            lResult.add(element.deepCopy());
        }
        return lResult;
    };

    /** Task 4 - add new property with specified parameters */
    public void addProperty(int inventoryNr, String description, GPS position1, GPS position2) {
        Property property = new Property(inventoryNr, description, position1.copyConstruct(),
                position2.copyConstruct(), this.idGenerator.nextId());

        List<Parcel> lParcelsByPos1 = this.kdTreeParcels.findAll(position1);    // log2(m)
        List<Parcel> lParcelsByPos2 = this.kdTreeParcels.findAll(position2);    // log2(m)

        List<Parcel> lResultData = this.mergeData(lParcelsByPos1, lParcelsByPos2);

        for (Parcel parcel : lResultData)
            parcel.addProperty(property);

        property.addParcels(lResultData);

        this.kdTreeProps.insert(position1, property);                           // log2(n)
        this.kdTreeProps.insert(position2, property);                           // log2(n)

        this.kdTreeResources.insert(position1, property);                       // log2(m+n)
        this.kdTreeResources.insert(position2, property);                       // log2(m+n)
    };

    /** Task 5 - add new parcel with specified parameters */
    public void addParcel(int parcelNr, String description, GPS position1, GPS position2) {
        Parcel parcel = new Parcel(parcelNr, description, position1.copyConstruct(),
                position2.copyConstruct(), this.idGenerator.nextId());
        List<Property> lPropsByPos1 = this.kdTreeProps.findAll(position1);    // log2(m)
        List<Property> lPropsByPos2 = this.kdTreeProps.findAll(position2);    // log2(m)

        List<Property> lResultData = this.mergeData(lPropsByPos1, lPropsByPos2);

        for (Property property : lResultData)
            property.addParcel(parcel);

        parcel.addProperties(lResultData);

        this.kdTreeParcels.insert(position1, parcel);                           // log2(n)
        this.kdTreeParcels.insert(position2, parcel);                           // log2(n)

        this.kdTreeResources.insert(position1, parcel);                         // log2(m+n)
        this.kdTreeResources.insert(position2, parcel);                         // log2(m+n)
    };

    /** Task 6 - edit property (its positions could be modified also) */
    public boolean editProperty(Property modifiedProperty, Property originalProperty) {
        if (modifiedProperty == null || originalProperty == null)
            return false;

        if (modifiedProperty.getGps1().isSameKey(originalProperty.getGps1())
                && modifiedProperty.getGps2().isSameKey(originalProperty.getGps2())) {
            List<Property> lResultProps = this.retrieveData(originalProperty.getGps1(), null, this.kdTreeProps);
            if (lResultProps.isEmpty())
                return false;
            Property propToEdit = null;
            for (Property prop : lResultProps) {
                if (prop.isSame(originalProperty)) {
                    propToEdit = prop;
                    break;
                }
            }
            if (propToEdit == null)
                return false;
            if (propToEdit.getPropertyId() != modifiedProperty.getPropertyId())
                propToEdit.setPropertyId(modifiedProperty.getPropertyId());
            if (propToEdit.getDescription().compareTo(modifiedProperty.getDescription()) != 0)
                propToEdit.setDescription(modifiedProperty.getDescription());
        }
        else {  // reinsertion because of secondary key change
            boolean ok = this.removeProperty(originalProperty);
            if (!ok)
                return false;
            this.addProperty(modifiedProperty.getPropertyId(), modifiedProperty.getDescription(),
                    modifiedProperty.getGps1(), modifiedProperty.getGps2());
        }
        return true;
    };

    /** Task 7 - edit parcel (its positions could be modified also) */
    public boolean editParcel(Parcel modifiedParcel, Parcel originalParcel) {
        if (modifiedParcel == null || originalParcel == null)
            return false;

        if (modifiedParcel.getGps1().isSameKey(originalParcel.getGps1())
                && modifiedParcel.getGps2().isSameKey(originalParcel.getGps2())) {
            List<Parcel> lResultParcels = this.retrieveData(originalParcel.getGps1(), null, this.kdTreeParcels);
            if (lResultParcels.isEmpty())
                return false;
            Parcel parcelToEdit = null;
            for (Parcel par : lResultParcels) {
                if (par.isSame(originalParcel)) {
                    parcelToEdit = par;
                    break;
                }
            }
            if (parcelToEdit == null)
                return false;
            if (parcelToEdit.getParcelId() != modifiedParcel.getParcelId())
                parcelToEdit.setParcelId(modifiedParcel.getParcelId());
            if (parcelToEdit.getDescription().compareTo(modifiedParcel.getDescription()) != 0)
                parcelToEdit.setDescription(modifiedParcel.getDescription());
        }
        else {  // reinsertion because of secondary key change
            boolean ok = this.removeParcel(originalParcel);
            if (!ok)
                return false;
            this.addParcel(modifiedParcel.getParcelId(), modifiedParcel.getDescription(),
                    modifiedParcel.getGps1(), modifiedParcel.getGps2());
        }
        return true;
    };

    /** Task 8 - remove specified property */
    public boolean removeProperty(Property property) {
        if (property == null)
            return false;
        boolean deleted = true;
        Property deletedProp = this.kdTreeProps.delete(property.getGps1(), property);
        if (deletedProp == null)
            return false;
        deleted = this.kdTreeProps.delete(property.getGps2(), property) != null;

        for (Parcel parcel : deletedProp.getParcels())
            parcel.removeProperty(deletedProp);

        deletedProp.removeParcels();

        deleted = deleted && this.kdTreeResources.delete(property.getGps1(), property) != null;
        deleted = deleted && this.kdTreeResources.delete(property.getGps2(), property) != null;
        return deleted;
    }

    /** Task 9 - remove specified parcel */
    public boolean removeParcel(Parcel parcel) {
        if (parcel == null)
            return false;
        boolean deleted = true;
        Parcel deletedParcel = this.kdTreeParcels.delete(parcel.getGps1(), parcel);
        if (deletedParcel == null)
            return false;
        deleted = this.kdTreeParcels.delete(parcel.getGps2(), parcel) != null;

        for (Property prop : deletedParcel.getProperties())
            prop.removeParcel(deletedParcel);

        deletedParcel.removeProperties();

        deleted = deleted && this.kdTreeResources.delete(parcel.getGps1(), parcel) != null;
        deleted = deleted && this.kdTreeResources.delete(parcel.getGps2(), parcel) != null;
        return deleted;
    }

//   -  -   -   -   -   -   -   -   -    P R I V A T E  -   -   -   -   -   -   -   -   -   -   -   -

    /**
     * Retrieves data from specified tree by using position parameters. If <code>wantedPos2</code> is set to null,
     * searching is based just on <code>wantedPos1</code>.
     * @param wantedPos1 first gps position of searched type
     * @param wantedPos2 second gps position of searched type or null value
     * @param kdTree k-d tree instance, in which will be search operation(s) executed
     * @return retrieved data from k-d tree
     * @param <T> geographical type of retrieved data
     */
    private <T extends GeoResource> List<T> retrieveData(GPS wantedPos1, GPS wantedPos2,
                                                         KDTree<T, GeoResource, GPS> kdTree) {
        if (wantedPos1 == null)
            return new ArrayList<T>(0);
        List<T> lResultData;
        if (wantedPos2 == null) {
            lResultData = kdTree.findAll(wantedPos1);
            lResultData = (lResultData == null) ? new ArrayList<>(0) : lResultData;
        }
        else {
            List<T> lFoundByPos1 = kdTree.findAll(wantedPos1);
            lFoundByPos1 = (lFoundByPos1 == null) ? new ArrayList<>(0) : lFoundByPos1;

            List<T> lFoundByPos2 = kdTree.findAll(wantedPos2);
            lFoundByPos2 = (lFoundByPos2 == null) ? new ArrayList<>(0) : lFoundByPos2;

            lResultData = this.mergeData(lFoundByPos1, lFoundByPos2);
        }
        return lResultData;
    }

    /**
     * Merges data from both lists, so the result is one list with unique instances (unique by mean of
     * references).
     * @param lData1 first data list
     * @param lData2 second data list
     * @return list of unique data or empty list
     * @param <T> requested data that are inherited from GeoResource abstract type
     */
    private <T extends GeoResource> List<T> mergeData(List<T> lData1, List<T> lData2) {
        if (lData1 == null)
            lData1 = new ArrayList<>(0);
        if (lData2 == null)
            lData2 = new ArrayList<>(0);

        for (T data2 : lData2) {
            boolean notFound = true;
            for (T data1 : lData1) {
                if (data2 == data1) {
                    notFound = false;
                    break;
                }
            }
            if (notFound)
                lData1.add(data2);
        }
        return lData1;
    }
}
