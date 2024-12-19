package mpoljak.dataStructures.KdTree;

public interface ISame<D> {

    /**
     * If both instances are the same instance or both instances have values of unique attributes equal.
     * @param other
     * @return  true, if both instances are the same or their all inner unique attributes are same.
     */
    public  boolean isSame(D other);
}
