package mpoljak.dataStructures.searchTrees.KdTree;

/**
 * This interface represents types, which can behave like data and could be compared by more than one duplicate key.
 * @param <T> Type, which can has more duplicate keys
 * @param <K> Type of key
 */
public interface IKdComparable<T, K extends Comparable<K>> { // super means

    /**
     * Comparing instances which have only one key set available for comparing.
     * @param other other instance of the same type to be compared
     * @param dim defines the level of K-D tree - by which key have to be instances compared. Dim is from interval <1,2,...,n>
     * @return -1 - other instance is bigger, 0 - both instances are equal, 1 - other is smaller
     */
    public int compareTo(T other, int dim);

    /**
     * Comparing instances which have at least two equivalent key sets, by which should 'other' instance be compared
     * @param other other instance of the same type to be compared
     * @param dim defines the level of K-D tree - by which key have to be instances compared. Dim is from interval <1,2,...,n>
     * @param otherKeySetId key set(from available equivalent key sets situated in instance) for comparing 'other' instance
     * @return -1 - other instance is bigger, 0 - both instances are equal, 1 - other is smaller
     */
    public int compareTo(T other, int dim, int otherKeySetId);

    /**
     *
     * @param dim dimension for which upper bound is inquired. Dim is from interval <1,2,...,n>
     * @return upper bound for given dimension. Null if not supported (when not implementing interval keys).
     */
    public K getUpperBound(int dim);

//    public int compare(int dim, D data, D otherData);
}
