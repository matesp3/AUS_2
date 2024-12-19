package mpoljak.App.GUI.controllers;

import mpoljak.App.GUI.GeoAppFrame;
import mpoljak.App.GUI.models.GeneratorModel;
import mpoljak.App.GUI.models.GeoInfoModel;
import mpoljak.App.GUI.models.ParcelTableModel;
import mpoljak.App.GUI.models.PropertyTableModel;
import mpoljak.App.Logic.GeoDbClient;
import mpoljak.data.geo.GPS;
import mpoljak.data.geo.GeoResource;
import mpoljak.data.geo.Parcel;
import mpoljak.data.geo.Property;

import java.util.ArrayList;
import java.util.List;

public class OperationsController {
    private GeoDbClient client;

    public OperationsController(GeoDbClient client) {
        if (client == null)
            throw new NullPointerException("No client provided");
        this.client = client;
    }

    public boolean insertDataToDb(GPS g1, GPS g2, GeoInfoModel info) {
        if (g1 == null || g2 == null || info == null)
            return false;
        if (info.getGeoType() == GeoAppFrame.TYPE_PARCEL)
            client.addParcel(info.getNumber(), info.getDescription(), g1, g2);
        else if (info.getGeoType() == GeoAppFrame.TYPE_PROPERTY)
            client.addProperty(info.getNumber(), info.getDescription(), g1, g2);
        return true;
    }

    public boolean searchDataInDb(GPS g1, GPS g2, boolean parcelSelected, boolean propertySelected,
                                  boolean bothSelected, ParcelTableModel parcelModel, PropertyTableModel propertyModel) {
        if (g1 == null)
            return false;
        if (parcelSelected) {
            List<Parcel> lPar = client.findParcels(g1);
            parcelModel.setModels(lPar);
            propertyModel.clear();
        }
        else if (propertySelected) {
            List<Property> lProps = client.findProperties(g1);
            propertyModel.setModels(lProps);
            parcelModel.clear();
        }
        else if (bothSelected) {
            if (g2 == null)
                return false;
            List<GeoResource> lData = client.findGeoResources(g1, g2);
            List<Property> lProps = new ArrayList<>();
            List<Parcel> lParcels = new ArrayList<>();
            propertyModel.clear();  // need to do this, because don't know what has been retrieved (only parc e.g.)
            parcelModel.clear();    // need to do this, because don't know what has been retrieved (only props e.g.)
            for (GeoResource g : lData) {
                if (g instanceof Property)
                    lProps.add((Property) g);
                else if (g instanceof Parcel)
                    lParcels.add((Parcel) g);
                propertyModel.setModels(lProps);
                parcelModel.setModels(lParcels);
            }
        }
        return true;
    }

    public boolean deleteDataFromDb(GPS g1, GPS g2, GeoInfoModel info, ParcelTableModel parcelModel, int parcelIdx,
                                    PropertyTableModel propertyModel, int propertyIdx) {
        if (g1 == null || g2 == null || info == null)
            return false;
        boolean deleteOk = false;
        if (info.getGeoType() == GeoAppFrame.TYPE_PARCEL && parcelIdx > -1) {
            Parcel selectedParcel = parcelModel.getModel(parcelIdx);
            deleteOk = client.removeParcel(selectedParcel);
            if (deleteOk)
                parcelModel.remove(parcelIdx);
        }
        else if (info.getGeoType() == GeoAppFrame.TYPE_PROPERTY && propertyIdx > -1) {
            Property selectedProperty = propertyModel.getModel(propertyIdx);
            deleteOk = client.removeProperty(selectedProperty);
            if (deleteOk)
                propertyModel.remove(propertyIdx);
        }
        return deleteOk;
    }

    public boolean editDataInDb(GPS g1, GPS g2, GeoInfoModel info, ParcelTableModel parcelModel, int parcelIdx,
                                PropertyTableModel propertyModel, int propertyIdx) {
        if (g1 == null || g2 == null || info == null)
            return false;
        boolean editOk = false;
        if (info.getGeoType() == GeoAppFrame.TYPE_PARCEL && parcelIdx > -1) {
            Parcel original = parcelModel.getModel(parcelIdx);
            if (original.getParcelId() == info.getNumber() &&
                    original.getDescription().compareTo(info.getDescription()) == 0 &&
                    g1.isSameKey(original.getGps1()) && g2.isSameKey(original.getGps2())) {
                return true;
            }
            Parcel modified = new Parcel(info.getNumber(), info.getDescription(), g1, g2, -1);
            editOk = client.editParcel(modified, original);
        }
        else if (info.getGeoType() == GeoAppFrame.TYPE_PROPERTY && propertyIdx > -1) {
            Property original = propertyModel.getModel(propertyIdx);
            if (original.getPropertyId() == info.getNumber() &&
                    original.getDescription().compareTo(info.getDescription()) == 0 &&
                    g1.isSameKey(original.getGps1()) && g2.isSameKey(original.getGps2())) {
                return true;
            }
            Property modified = new Property(info.getNumber(), info.getDescription(), g1, g2, -1);
            editOk = client.editProperty(modified, original);
        }
        return editOk;
    }

    public String getPropertiesDataRepresentation() {
        return this.client.getPropertiesRepresentation();
    }

    public String getParcelsDataRepresentation() {
        return this.client.getParcelsRepresentation();
    }

    public void generateValuesToDb(GeneratorModel model) {
        if (model == null)
            return;
        this.client.generateData(model.getParcelsCount(), model.getPropertiesCount(), model.getOverlayProbability());
    }

    /**
     * Sets propModel with relationship of specified parcel
     * @param propModel
     * @param fromModel
     * @param onIndex
     */
    public void setParcelWithProperties(PropertyTableModel propModel, ParcelTableModel fromModel, int onIndex) {
        if (onIndex < 0)
            return;
        Parcel parcel = fromModel.getModel(onIndex);
        propModel.setModels(parcel.getProperties());
    }

    /**
     * Sets parcModel with relationship of specified property
     * @param parcModel
     * @param propertyModel
     * @param selectedRow
     */
    public void setPropertyWithParcels(ParcelTableModel parcModel, PropertyTableModel propertyModel, int selectedRow) {
        if (selectedRow < 0)
            return;
        Property property = propertyModel.getModel(selectedRow);
        parcModel.setModels(property.getParcels());
    }

    /**
     * Requests GeoDB to execute specified operation with selected file.
     * @param filePath file path of selected path
     * @param selectedOperation possible valid choices: <code>GeoAppFrame.FILE_SAVE_OPTION</code> or
     *                          <code>GeoAppFrame.FILE_LOAD_OPTION</code>
     * @param geoType for which type will be <code>selectedOperation</code> executed. Possible valid choices:
     *                <code>GeoAppFrame.TYPE_PARCEL</code>, <code>GeoAppFrame.TYPE_PROPERTY</code>
     * @return message about success of selected file operation
     */
    public String processSelectedFile(String filePath, char selectedOperation, char geoType) {
        if (filePath == null || filePath.isBlank())
            return "E: selected file is ''";
        if (geoType != GeoAppFrame.TYPE_PARCEL && geoType != GeoAppFrame.TYPE_PROPERTY)
            return "E: Unknown geo type, for which the action was to be executed.";

        if (geoType == GeoAppFrame.TYPE_PARCEL)
            geoType = GeoDbClient.GEO_TYPE_PARCEL;
        else if (geoType == GeoAppFrame.TYPE_PROPERTY)
            geoType = GeoDbClient.GEO_TYPE_PROPERTY;

        if (selectedOperation == GeoAppFrame.FILE_SAVE_OPTION) {
            boolean ok = client.saveState(filePath, geoType);
            return ok ? "I: Ok.. Data saved to "+filePath : "E: Saving data failed.";
        }
        else if (selectedOperation == GeoAppFrame.FILE_LOAD_OPTION) {
            boolean ok = client.loadDataFromFile(filePath);
            return ok ? "I: Ok.. Data loaded from "+filePath : "E: Loading data failed.";
        }

        return "E: Unknown operation requested.";
    }
}
