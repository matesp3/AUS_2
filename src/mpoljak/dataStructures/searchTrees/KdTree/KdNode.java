package mpoljak.dataStructures.searchTrees.KdTree;

import java.util.ArrayList;
import java.util.List;

//public class KdNode <T extends IKdComparable<T, K> & IKeySetChooseable, K extends Comparable<K> > { // T type must implement IKdComparable interface, K is anything
//public class KdNode <T extends IKdComparableII<K, M>, K, M extends Comparable<M> > {
public class KdNode <T extends ISimilar<?>, K extends IKdComparable<K> > {
    private KdNode<T,K> parent;
    private KdNode<T,K> leftSon;
    private KdNode<T,K> rightSon;
    private final T data;
    private final K usedKey;
    private final K maxDimValues; // but values inside will be modified

    public KdNode(KdNode<T,K> parent, KdNode<T,K> leftSon, KdNode<T,K> rightSon, T data, K usedKey) {
        this.parent = parent;
        this.leftSon = leftSon;
        this.rightSon = rightSon;
        if (data == null)
            throw new NullPointerException("Data in KdNode is null");
        this.data = data;
        this.usedKey = usedKey;
        this.maxDimValues = usedKey.copyConstruct();
    }

    /**
     * Determines it's worth to search for some key in a subtree.
     * @param wantedKey key that it is being searched for
     * @return
     */
    public boolean canExistInSubtree(K wantedKey) {
        return wantedKey.fallsInto(this.maxDimValues);
    }

    /** Updates max key values of all dimensions that are lower than values in given composite key. */
    public void updateMaxKeyValues(K wantedKey) {
        this.maxDimValues.mapGreaterValues(wantedKey);
    }

    /***
     * @return whole key containing keys for all dimensions
     */
    public K getUsedKey() {
        return this.usedKey;
    }

    public T getData() {
        return this.data;
    }

    /**
     * Compares nodes by defined dimension.
     * @param otherKey other key to be compared with key used in node
     * @param dim defines the level of K-D tree - by which key have to be instances compared
     * @return -1 - otherKey instance is bigger, 0 - both instances are equal, 1 - otherKey is smaller, else
     *                                      instance of 'mpoljak.dataStructures.searchTrees.KdTree.ERROR'
     */
    public int compareTo(K otherKey, int dim) {
        if (dim < 1)
            return Error.INVALID_DIMENSION.getErrCode();
        if (otherKey == null)
            return Error.NULL_PARAMETER.getErrCode();

        return this.usedKey.compareTo(otherKey, dim);
    }

    public int compareTo(KdNode<T,K> otherNode, int dim) {
        return this.compareTo(otherNode.usedKey, dim);
    }

    public void setParent(KdNode<T,K> parent) { this.parent = parent; }
    public void setLeftSon(KdNode<T,K> leftSon) { this.leftSon = leftSon; }
    public void setRightSon(KdNode<T,K> rightSon) { this.rightSon = rightSon; }

    public KdNode<T,K> getParent() { return parent; }
    public KdNode<T,K> getLeftSon() { return leftSon; }
    public KdNode<T,K> getRightSon() { return rightSon; }

    public boolean isParent(KdNode<T,K> node) { return this.parent == node; }
    public boolean isLeftSon(KdNode<T,K> node) { return this.leftSon == node; }
    public boolean isRightSon(KdNode<T,K> node) { return this.rightSon == node; }

    public boolean hasParent() { return this.parent != null; }
    public boolean hasLeftSon() { return this.leftSon != null; }
    public boolean hasRightSon() { return this.rightSon != null; }

    @Override
    public String toString() {
        return "V{key=" + this.usedKey + "; data=" + data.toString() + '}';
//        return "V{key=" + this.usedKey + "; maxKey=" + this.maxDimValues + "; data= " + this.data + "}";
    }

}
