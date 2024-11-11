package mpoljak.App.GUI.controllers;

import mpoljak.App.GUI.GeoAppFrame;
import mpoljak.App.GUI.models.GeoInfoModel;
import mpoljak.App.GUI.models.ParcelTableModel;
import mpoljak.App.GUI.models.PropertyTableModel;
import mpoljak.App.Logic.GeoDbClient;
import mpoljak.data.GPS;
import mpoljak.data.GeoResource;
import mpoljak.data.Parcel;
import mpoljak.data.Property;

import javax.sound.sampled.Port;
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
        }
        else if (info.getGeoType() == GeoAppFrame.TYPE_PROPERTY && propertyIdx > -1) {
            Property selectedProperty = propertyModel.getModel(propertyIdx);
            deleteOk = client.removeProperty(selectedProperty);
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
}
