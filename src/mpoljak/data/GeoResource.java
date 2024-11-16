package mpoljak.data;

import mpoljak.App.Logic.ICsvFormattable;
import mpoljak.dataStructures.searchTrees.KdTree.ISame;

public abstract class GeoResource implements ISame<GeoResource>, ICsvFormattable<GeoResource> {
    protected final int uniqueId;
    public GeoResource(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * Deep copy constructing with deep copied relationships.
     * @return deep copy of instance
     */
    public abstract GeoResource deepCopy();

    @Override
    public boolean isSame(GeoResource other) {
        return this == other || this.uniqueId == other.uniqueId;
    }
}
