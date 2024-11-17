package mpoljak.App.Logic;

import mpoljak.data.GPS;
import mpoljak.data.GeoResource;
import mpoljak.data.Parcel;
import mpoljak.data.Property;
import mpoljak.dataStructures.searchTrees.KdTree.KDTree;
import mpoljak.utilities.IntegerIdGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeoDbClient {
    public static final char GEO_TYPE_PARCEL = 'A';
    public static final char GEO_TYPE_PROPERTY = 'B';

    private static final String ID_FILE_PARCEL = "#_PARCELS";
    private static final String ID_FILE_PROPERTY = "#_PROPERTIES";

    private static final String DELIMITER_REPLACEMENT = "%#%";
    private static final String STR_BLANK_REPLACEMENT = "NULL";
    private static final char DELIMITER = ';';

    private KDTree<Property, GeoResource, GPS> kdTreeProps;
    private KDTree<Parcel, GeoResource, GPS> kdTreeParcels;
    private KDTree<GeoResource, GeoResource, GPS> kdTreeResources;
    private IntegerIdGenerator idGenerator;
    private final String defaultDir          = "src/mpoljak/App/files";
    private final String configPath          = this.defaultDir + "/config.txt";
    private final String defaultParcFilePath = this.defaultDir + "/data-parcels.txt";
    private final String defaultPropFilePath = this.defaultDir + "/data-properties.txt";
    private String propertiesFilePath;
    private String parcelsFilePath;

    public GeoDbClient() {
        this.kdTreeProps = new KDTree<>(2);
        this.kdTreeParcels = new KDTree<>(2);
        this.kdTreeResources = new KDTree<>(2);
        this.idGenerator = IntegerIdGenerator.getInstance();
        this.readConfing();
//        System.out.println(this.loadDataFromCsvFile());
//        System.out.println(this.writeDataToCsvFile());
    }

    public boolean setGeoPropertiesPath(String path) {
        if (path == null || path.isEmpty())
            return false;

        return true;
    }

    public boolean setGeoParcelsPath(String path) {
        return true;
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

    /**
     * Generates and inserts random parcel and property data with given overlay probability of one GPS position.
     * @param parcelsCount
     * @param propertiesCount
     * @param overlayProbability
     */
    public void generateData(int parcelsCount, int propertiesCount, double overlayProbability) {
        Random generator = new Random();
        Random dirGenerator = new Random(generator.nextInt());
        ArrayList<Parcel> lGps = new ArrayList<>(parcelsCount);
        for (int i = 0; i < parcelsCount; i++) {
            GPS gps1 = GPS.generateGPS(generator, dirGenerator);
            GPS gps2 = GPS.generateGPS(generator, dirGenerator);
            Parcel p = new Parcel(i, "generated", gps1, gps2, IntegerIdGenerator.getInstance().nextId());
            lGps.add(p);

            this.addParcel(i, "generated", gps1, gps2);
//            this.kdTreeParcels.insert(gps1, p);
//            this.kdTreeParcels.insert(gps2, p);
//
//            this.kdTreeResources.insert(gps1,p);
//            this.kdTreeResources.insert(gps2,p);
        }

        for (int a = 0; a < propertiesCount; a++) {
            GPS gps1;
            if (!lGps.isEmpty() && generator.nextDouble() <= overlayProbability)
                gps1 = lGps.get(generator.nextInt(lGps.size())).getGps1();
            else
                gps1 = GPS.generateGPS(generator, dirGenerator);
            GPS gps2 = GPS.generateGPS(generator, dirGenerator);
//            Property prop = new Property(a, "generated", gps1, gps2, IntegerIdGenerator.getInstance().nextId());
            this.addProperty(a, "generated", gps1, gps2);
//            this.kdTreeProps.insert(gps1, prop);
//            this.kdTreeProps.insert(gps2, prop);
//            this.kdTreeResources.insert(gps1, prop);
//            this.kdTreeResources.insert(gps2, prop);
        }
    }

    /**
     * Creates string representation of all saved properties.
     * @return
     */
    public String getPropertiesRepresentation() {
        return this.getTreeDataRepresentation(this.kdTreeProps);
    }

    /**
     * Creates string representation of all saved parcels.
     * @return
     */
    public String getParcelsRepresentation() {
        return this.getTreeDataRepresentation(this.kdTreeParcels);
    }

    /**
     * Saves current db data to specified file.
     * @param fileName if not specified - <code>null</code> - then it is saved in default directory's file
     * @return information about success of saving to specified file
     */
    public boolean saveState(String fileName, char geoType) {
        if (geoType == GEO_TYPE_PARCEL)
            this.parcelsFilePath = fileName != null ? fileName : this.parcelsFilePath;
        else if (geoType == GEO_TYPE_PROPERTY)
            this.propertiesFilePath = fileName != null ? fileName : this.propertiesFilePath;
        else
            return false;   // unsupported type to save

        boolean success = this.writeDataToCsvFile();
        this.saveConfig();
        return success;
    }

    /**
     * Loads data (appends them to current data) to db from specified file by parameter <code>fileName</code>
     * @param fileName file path to file, from which will be data loaded
     * @return information about success of loading data from file
     */
    public boolean loadDataFromFile(String fileName) {
        return this.loadGeoResourceFromCsvFile(fileName);
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

    private String getTreeDataRepresentation(KDTree<? extends GeoResource, GeoResource, GPS> kdtree) {
        if (kdtree == null)
            return null;
        if (kdtree.isEmpty())
            return "    ..No data.";
        KDTree<? extends GeoResource,GeoResource,GPS>.KdTreeInOrderIterator<? extends GeoResource,GeoResource,GPS> it =
                kdtree.inOrderIterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext())
            sb.append(String.format("%s\n",it.next()));
        return sb.toString();
    }

    /**
     * Writes data of all currently existing geo data in RAM to files.
     * @return <code>true</code> if ALL geo resources were successfully saved to files, else <code>false</code>.
     */
    private boolean writeDataToCsvFile() {
        boolean success = true;
        if (!this.kdTreeParcels.isEmpty())
            success = this.writeGeoResourceToCsvFile(this.parcelsFilePath, ID_FILE_PARCEL,
                        this.kdTreeParcels.levelOrderIterator());
        if (!this.kdTreeProps.isEmpty())
            success = success && this.writeGeoResourceToCsvFile(this.propertiesFilePath, ID_FILE_PROPERTY,
                        this.kdTreeProps.levelOrderIterator());
        return success;
    }

    /**
     * Loads data of all available data files.
     * @return <code>true</code> if ALL geo resources were successfully loaded from files, else <code>false</code>.
     */
    private boolean loadDataFromCsvFile() {
        boolean success;
        success = this.loadGeoResourceFromCsvFile(this.parcelsFilePath);
        success = success && this.loadGeoResourceFromCsvFile(this.propertiesFilePath);
        return success;
    }

    /**
     * Writes current data of specified geo resource type <code>T</code> to file from passed tree structure.
     * @param filePath where data should be stores
     * @param identification identification, which type of geo resource is stored in file.
     * @param iterator iterator of data structure whose instances of geo resources will be written to file
     * @return success of writing data to file
     */
    private <T extends GeoResource> boolean writeGeoResourceToCsvFile(String filePath, String identification,
                                                                      KDTree<T, GeoResource, GPS>.KdTreeLevelOrderIterator
                                                                       <T, GeoResource, GPS> iterator) {
        if (iterator == null)
            return false;
        try (FileWriter fw = new FileWriter(filePath)) { // n * 2 * log2(n)
            fw.write(identification+"\n");
            KDTree<T, GeoResource, GPS> kdTreeIdEvidence = new KDTree<>(2);

            while (iterator.hasNext()) {
                T geoRes = iterator.next();
                List<T> lFound = kdTreeIdEvidence.findAll(geoRes.getGps1());
                if (lFound == null) {
                    kdTreeIdEvidence.insert(geoRes.getGps1(), geoRes);    // do evidence of parcel
                    fw.write( geoRes.toCsvLine(DELIMITER, DELIMITER_REPLACEMENT, STR_BLANK_REPLACEMENT)+ "\n");
                }
                else {
                    boolean notFound = true;
                    for (T res : lFound) {
                        if (res.isSame(geoRes)) {
                            notFound = false;
                            break;
                        }
                    }
                    if (notFound) {
                        kdTreeIdEvidence.insert(geoRes.getGps1(), geoRes);    // do evidence of parcel
                        fw.write( geoRes.toCsvLine(DELIMITER, DELIMITER_REPLACEMENT, STR_BLANK_REPLACEMENT)+ "\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Read data stored in file and appends them to current data.
     * @param filePath where data are stored
     * @return <code>false</code> if structure of file is corrupted, else <code>true</code>
     */
    private boolean loadGeoResourceFromCsvFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int typeToRead = -1;
            final int TYPE_PARCEL = 1;
            final int TYPE_PROPERTY = 2;
            GeoResource geoResource;
            GPS gForFact = new GPS('N',1,'E',1); // just for factory instance
            String line = br.readLine();
            if (line == null)
                return false;
            if (line.equals(ID_FILE_PARCEL)) {
                typeToRead = TYPE_PARCEL;
                geoResource = new Parcel(1,null,gForFact,gForFact,-1);// just for factory purposes
            }
            else if (line.equals(ID_FILE_PROPERTY)) {
                typeToRead = TYPE_PROPERTY;
                geoResource = new Property(1,null,gForFact,gForFact,-1);// just for factory purposes
            } else
                return false;

            while ((line = br.readLine()) != null) {
                if (typeToRead == TYPE_PARCEL) {
                    Parcel pa = (Parcel) geoResource.fromCsvLine(line,
                            GeoDbClient.DELIMITER, GeoDbClient.DELIMITER_REPLACEMENT, GeoDbClient.STR_BLANK_REPLACEMENT);
                    this.addParcel(pa.getParcelId(), pa.getDescription(), pa.getGps1(), pa.getGps2());
                }
                else if (typeToRead == TYPE_PROPERTY) {
                    Property pr = (Property) geoResource.fromCsvLine(line,
                            GeoDbClient.DELIMITER, GeoDbClient.DELIMITER_REPLACEMENT, GeoDbClient.STR_BLANK_REPLACEMENT);
                    this.addProperty(pr.getPropertyId(), pr.getDescription(), pr.getGps1(), pr.getGps2());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void readConfing() {
        boolean[] aConfigSet = {false, false};
        try (BufferedReader br = new BufferedReader(new FileReader(this.configPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.trim().split("=");
                if (data.length == 2) {
                    if (data[0].equals("parcelFP")) {
                        File f = new File(data[1]);
                        if (f.exists())
                            this.parcelsFilePath = f.getAbsolutePath();
                        aConfigSet[0] = true;
                    }
                    else if (data[0].equals("propertyFP")) {
                        File f = new File(data[1]);
                        if (f.exists())
                            this.propertiesFilePath = f.getAbsolutePath();
                        aConfigSet[1] = true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!aConfigSet[0]) {
            this.parcelsFilePath = this.defaultParcFilePath;
//            try (FileWriter fw = new FileWriter(this.parcelsFilePath)) {
//                fw.write(ID_FILE_PARCEL+"\n");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        }
        if (!aConfigSet[1]) {
            this.propertiesFilePath = this.defaultPropFilePath;
//            try (FileWriter fw = new FileWriter(this.propertiesFilePath)) {
//                fw.write(ID_FILE_PROPERTY+"\n");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        }
    }

    private void saveConfig() {
        try (FileWriter fw = new FileWriter(this.configPath)) {
                fw.write("parcelFP="+this.parcelsFilePath+"\n");
                fw.write("propertyFP="+this.propertiesFilePath+"\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }

    public static void main(String[] args) {
    }

}
