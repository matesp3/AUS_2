package mpoljak.dataStructures.searchTrees.KdTree;

public interface ISimilar<D> {

    /**
     * If both instances are the same instance or both instances have all inner values equal (as it was done deep copy
     * of one of them).
     * @param other
     * @return  true, if both instances are the same or their all inner values are same (as it was done deep copy of one
     * of them).
     */
    public boolean isSame(D other);
}
