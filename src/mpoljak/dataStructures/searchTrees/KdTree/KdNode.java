package mpoljak.dataStructures.searchTrees.KdTree;

import java.util.ArrayList;
import java.util.List;

//public class KdNode <T extends IKdComparable<T, K> & IKeySetChooseable, K extends Comparable<K> > { // T type must implement IKdComparable interface, K is anything
//public class KdNode <T extends IKdComparableII<K, M>, K, M extends Comparable<M> > {
public class KdNode <T, K extends IKdComparableII<K, M>, M extends Comparable<M> > {
    private KdNode<T,K,M> parent;
    private KdNode<T,K,M> leftSon;
    private KdNode<T,K,M> rightSon;
    private final T data;
    private final K usedKey;
    private List<M> lMaximums;

    public KdNode(KdNode<T,K,M> parent, KdNode<T,K,M> leftSon, KdNode<T,K,M> rightSon, T data, int dim, K usedKey) {
        this.parent = parent;
        this.leftSon = leftSon;
        this.rightSon = rightSon;
        if (data == null)
            throw new NullPointerException("Data in KdNode is null");
        this.data = data;
        this.usedKey = usedKey;
        this.lMaximums = new ArrayList<M>(dim);
//        for (int i = 0; i < dim; i++) {
//            this.upperBounds.add(data.getUpperBound(i + 1));
//        }
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

    public void setParent(KdNode<T,K,M> parent) { this.parent = parent; }
    public void setLeftSon(KdNode<T,K,M> leftSon) { this.leftSon = leftSon; }
    public void setRightSon(KdNode<T,K,M> rightSon) { this.rightSon = rightSon; }

    public KdNode<T,K,M> getParent() { return parent; }
    public KdNode<T,K,M> getLeftSon() { return leftSon; }
    public KdNode<T,K,M> getRightSon() { return rightSon; }

    public boolean isParent(KdNode<T,K,M> node) { return this.parent == node; }
    public boolean isLeftSon(KdNode<T,K,M> node) { return this.leftSon == node; }
    public boolean isRightSon(KdNode<T,K,M> node) { return this.rightSon == node; }

    public boolean hasParent() { return this.parent != null; }
    public boolean hasLeftSon() { return this.leftSon != null; }
    public boolean hasRightSon() { return this.rightSon != null; }

    public M getUpperBound(int dim) {
        if (this.lMaximums == null)
            return null;
        return this.lMaximums.get(dim - 1);
    }
    public void setUpperBound(M upperBound, int dim) {
        this.lMaximums.set(dim - 1, upperBound);
    }

    @Override
    public String toString() {
        return "V{key=" + this.usedKey + "; data=" + data.toString() + '}';
    }

}
