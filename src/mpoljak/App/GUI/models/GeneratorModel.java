package mpoljak.App.GUI.models;

public class GeneratorModel {
    private int parcelsCount;
    private int propertiesCount;
    private double overlayProbability;

    public GeneratorModel(int parcelsCount, int propertiesCount, double overlayProbability) {
        this.parcelsCount = parcelsCount;
        this.propertiesCount = propertiesCount;
        this.overlayProbability = overlayProbability;
    }

    public int getParcelsCount() {
        return parcelsCount;
    }

    public int getPropertiesCount() {
        return propertiesCount;
    }

    public double getOverlayProbability() {
        return overlayProbability;
    }
}
