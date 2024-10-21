package mpoljak.dataStructures.searchTrees.KdTree;

import mpoljak.data.Parcel;

import java.util.ArrayList;
import java.util.List;

public class KdNode <T extends IKdComparable<T, K> & IKeyChoosable, K extends Comparable<K> > { // T type must implement IKdComparable interface, K is anything
    private KdNode<T, K> parent;
    private KdNode<T, K> leftSon;
    private KdNode<T, K> rightSon;
    private T data;
    private List<K> upperBounds;

    public KdNode(KdNode<T, K> parent, KdNode<T, K> leftSon, KdNode<T, K> rightSon, T data, int dim) {
        this.parent = parent;
        this.leftSon = leftSon;
        this.rightSon = rightSon;
        if (data == null)
            throw new NullPointerException("Data in KdNode is null");
        this.data = data;

        this.upperBounds = new ArrayList<K>(dim);
        for (int i = 0; i < dim; i++) {
            this.upperBounds.add(data.getUpperBound(i + 1));
        }
    }
    /**
     * Compares nodes by defined dimension(key).
     * @param other other instance of the same type to be compared
     * @param dim defines the level of K-D tree - by which key have to be instances compared
     * @return -1 - other instance is bigger, 0 - both instances are equal, 1 - other is smaller, else
     *                                      instance of 'mpoljak.dataStructures.searchTrees.KdTree.ERROR'
     */
    public int compareTo(KdNode<T, K> other, int dim) {
        if (dim < 1)
            return Error.INVALID_DIMENSION.getErrCode();
        if (other == null)
            return Error.NULL_PARAMETER.getErrCode();
        return data.compareTo(other.data, dim);
//        return data.compareTo(other.data, dim, 0);
    }

    /**
     * Compares nodes by defined dimension(key) and defined key set of other node.
     * @param other other instance of the same type to be compared
     * @param dim defines the level of K-D tree - by which key have to be instances compared
     * @param keySetId key set of other node
     * @return -1 - other instance is bigger, 0 - both instances are equal, 1 - other is smaller, else
     *                                      instance of 'mpoljak.dataStructures.searchTrees.KdTree.ERROR'
     */
    public int compareTo(KdNode<T, K> other, int dim, int keySetId) {
        if (dim < 1)
            return Error.INVALID_DIMENSION.getErrCode();
        if (other == null)
            return Error.NULL_PARAMETER.getErrCode();
        return data.compareTo(other.data, dim, keySetId);
    }

    public void setParent(KdNode<T, K> parent) { this.parent = parent; }
    public void setLeftSon(KdNode<T, K> leftSon) { this.leftSon = leftSon; }
    public void setRightSon(KdNode<T, K> rightSon) { this.rightSon = rightSon; }

    public KdNode<T, K> getParent() { return parent; }
    public KdNode<T, K> getLeftSon() { return leftSon; }
    public KdNode<T, K> getRightSon() { return rightSon; }

    public boolean isParent(KdNode<T, K> node) { return this.parent == node; }
    public boolean isLeftSon(KdNode<T, K> node) { return this.leftSon == node; }
    public boolean isRightSon(KdNode<T, K> node) { return this.rightSon == node; }

    public boolean hasParent() { return this.parent != null; }
    public boolean hasLeftSon() { return this.leftSon != null; }
    public boolean hasRightSon() { return this.rightSon != null; }

    public K getUpperBound(int dim) {
        if (this.upperBounds == null)
            return null;
        return this.upperBounds.get(dim - 1);
    }
    public void setUpperBound(K upperBound, int dim) {
        this.upperBounds.set(dim - 1, upperBound);
    }

    public T getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "KdNode{" + data.toString() + '}';
    }

//    @Override
//    public String toString() {
//        return "KdNode{" + data.toString() +
//                ", upperBound=" + upperBounds.toString() +
//                '}';
//    }
}
