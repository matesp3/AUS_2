package mpoljak.dataStructures.searchTrees.KdTree;
/**
 * This interface represents types, which can behave like data and could be compared by more than one duplicate key.
 * @param <K> Type of key
 */
public interface IKdComparable<K, M extends Comparable<M>> {

//    /**
//     * Comparing two keys for specific dimension (attributes of keys).
//     * @param k1 key of first instance
//     * @param k2 key of second instance
//     * @param dim by which dimension are keys compared. If dimension of K-D tree is 3 and current depth for compared
//     *            instances is 5, then parameter must be equal to (5%3)+1, generally: (currentDepth % treeDimension) +1,
//     *            where currentDepth is from sequence <0,h> and treeDimension is from sequence <1,2,3,...>
//     * @return (k1 < k2) => -1; (k1 = k2) => 0; (k1 > k2) => 1;
//     */
//    public int compare(K k1, K k2, int dim);

    /**
     * Comparing calling instance with other instance of the same type for specific dimension (attributes of keys).
     * @param other other instance
     * @param dim by which dimension are keys compared. If dimension of K-D tree is 3 and current depth for compared
     *            instances is 5, then parameter must be equal to (5%3)+1, generally: (currentDepth % treeDimension) +1,
     *            where currentDepth is from sequence <0,h> and treeDimension is from sequence <1,2,3,...>
     * @return (other instance is greater) => -1; (both instances are equal) => 0; (other instance is lower) => 1;
     */
    public int compareTo(K other, int dim);

    /**
     *
     * @param dim dimension for which maximum is inquired. Dim is from interval <1,2,...,n>
     * @return maximum for given dimension.
     */
    public M getDimensionKey(int dim);
}
