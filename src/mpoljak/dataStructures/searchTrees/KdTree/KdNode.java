package mpoljak.dataStructures.searchTrees.KdTree;

//public class KdNode <T extends ISimilar<?>, K extends IKdComparable<K> > {
public class KdNode<D extends T, T extends ISimilar<T>, K extends IKdComparable<K> > {
    private KdNode<D,T,K> parent;
    private KdNode<D,T,K> leftSon;
    private KdNode<D,T,K> rightSon;
    private final D  data;
    private final K usedKey;
    private final K maxDimValues; // but values inside will be modified

    public KdNode(KdNode<D,T,K> parent, KdNode<D,T,K> leftSon, KdNode<D,T,K> rightSon, D data, K usedKey) {
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

    public D getData() {
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

    public int compareTo(KdNode<D,T,K> otherNode, int dim) {
        return this.compareTo(otherNode.usedKey, dim);
    }

    public void setParent(KdNode<D,T,K> parent) { this.parent = parent; }
    public void setLeftSon(KdNode<D,T,K> leftSon) { this.leftSon = leftSon; }
    public void setRightSon(KdNode<D,T,K> rightSon) { this.rightSon = rightSon; }

    public KdNode<D,T,K> getParent() { return parent; }
    public KdNode<D,T,K> getLeftSon() { return leftSon; }
    public KdNode<D,T,K> getRightSon() { return rightSon; }

    public boolean isParent(KdNode<D,T,K> node) { return this.parent == node; }
    public boolean isLeftSon(KdNode<D,T,K> node) { return this.leftSon == node; }
    public boolean isRightSon(KdNode<D,T,K> node) { return this.rightSon == node; }

    public boolean hasParent() { return this.parent != null; }
    public boolean hasLeftSon() { return this.leftSon != null; }
    public boolean hasRightSon() { return this.rightSon != null; }
    public boolean hasNoneSons() { return this.leftSon == null && this.rightSon == null; }

    public void removeChild(KdNode<D, T, K> node) {
        if (node == null)
            return;
        if (this.leftSon == node)
            this.leftSon = null;
        else if (this.rightSon == node)
            this.rightSon = null;
    }
    @Override
    public String toString() {
        return "V{key=" + this.usedKey + "; data=" + data.toString() + '}';
//        return "V{key=" + this.usedKey + "; maxKey=" + this.maxDimValues + "; data= " + this.data + "}";
    }


}
