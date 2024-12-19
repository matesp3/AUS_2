package mpoljak.data.geo;

import mpoljak.App.Logic.ICsvFormattable;
import mpoljak.dataStructures.ITableData;

public abstract class GeoResource implements ITableData, ICsvFormattable<GeoResource> {
    protected final int uniqueId;
    public GeoResource(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public abstract GPS getGps1();
    public abstract GPS getGps2();

    /**
     * Deep copy constructing with deep copied relationships.
     * @return deep copy of instance
     */
    public abstract GeoResource deepCopy();

    public int getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public boolean isSame(ITableData other) {
        if (!(other instanceof GeoResource))
            return false;
        GeoResource g = (GeoResource) other;
        return this == other || this.uniqueId == g.uniqueId;
    }
}
