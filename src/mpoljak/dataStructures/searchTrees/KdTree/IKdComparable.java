package mpoljak.dataStructures.searchTrees.KdTree;

/**
 * This interface represents types, which can behave like data and could be compared by more than one duplicate key.
 * @param <T> Type, which can has more duplicate keys
 */
public interface IKdComparable<T> {

    /**
     *
     * @param other other instance of the same type to be compared
     * @param dim defines the level of K-D tree - by which key have to be instances compared. Dim is from interval <1,2,...,n>
     * @return -1 - other instance is bigger, 0 - both instances are equal, 1 - other is smaller
     */
    public int compareTo(T other, int dim);

//    public int compare(int dim, D data, D otherData);
}
