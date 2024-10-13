package mpoljak.dataStructures.searchTrees.KdTree;

public class KdNode <T extends IKdComparable<T>> {
    private KdNode<T> parent;
    private KdNode<T> leftSon;
    private KdNode<T> rightSon;
    private T data;

    public KdNode(KdNode<T> parent, KdNode<T> leftSon, KdNode<T> rightSon, T data) {
        this.parent = parent;
        this.leftSon = leftSon;
        this.rightSon = rightSon;
        this.data = data;
    }
    /**
     * Compares nodes by defined dimension(key).
     * @param other other instance of the same type to be compared
     * @param dim defines the level of K-D tree - by which key have to be instances compared
     * @return -1 - other instance is bigger, 0 - both instances are equal, 1 - other is smaller, else
     *                                      instance of 'mpoljak.dataStructures.searchTrees.KdTree.ERROR'
     */
    public int compareTo(KdNode<T> other, int dim) {
        if (dim < 1)
            return Error.INVALID_DIMENSION.getErrCode();
        if (other == null)
            return Error.NULL_PARAMETER.getErrCode();
        return data.compareTo(other.data, dim);
    }

    public void setParent(KdNode<T> parent) { this.parent = parent; }
    public void setLeftSon(KdNode<T> leftSon) { this.leftSon = leftSon; }
    public void setRightSon(KdNode<T> rightSon) { this.rightSon = rightSon; }

    public KdNode<T> getParent() { return parent; }
    public KdNode<T> getLeftSon() { return leftSon; }
    public KdNode<T> getRightSon() { return rightSon; }
    public T getData() {
        return this.data;
    }
}
