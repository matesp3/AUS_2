package mpoljak.dataStructures.KdTree;

import mpoljak.dataStructures.ITableKey;

import java.util.Comparator;

/**
 * This interface represents types, which can behave like data and could be compared by more than one duplicate key.
 * @param <K> Type of key
 */
public interface IKdComparable<K> extends ITableKey {
    /**
     * Comparing calling instance with other instance of the same type for specific dimension (attributes of keys).
     * @param other other instance
     * @param dim by which dimension are keys compared. If dimension of K-D tree is 3 and current depth for compared
     *            instances is 5, then parameter must be equal to (5%3)+1, generally: (currentDepth % treeDimension) +1,
     *            where currentDepth is from sequence <0,h> and treeDimension is from sequence <1,2,3,...>
     * @return (other instance is greater) => -1; (both instances are equal) => 0; (other instance is lower) => 1;
     */
    public int compareTo(K other, int dim);

    public K copyConstruct();

    /**
     * Determines if keys of calling instance are in all dimensions lower or equal to compared key values of other
     * instance.
     * @param other other key
     * @return false if at least in one dimension of calling instance is key value greater than value of other instance.
     *          Else true.
     */
    public boolean fallsInto(K other);

    /**
     * If values at some dimension are greater for other instance, calling instance will map these values into itself.
     * @param other
     */
    public void mapGreaterValues(K other);

    /**
     * Determines, whether both key instances have equal values of keys in all dimensions.
     * @param other
     * @return true if both instances have equal key values in all dimensions
     */
    public boolean isSameKey(K other);

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
//
//    /**
//     *
//     * @param dim dimension for which maximum is inquired. Dim is from interval <1,2,...,n>
//     * @return maximum for given dimension.
//     */
//    public M getDimensionKey(int dim);
}
