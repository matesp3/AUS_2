package mpoljak.dataStructures.searchTrees.KdTree;

//public class KdNode <T extends ISimilar<?>, K extends IKdComparable<K> > {
public class KdNode<D extends T, T extends ISame<T>, K extends IKdComparable<K> > {
    private KdNode<D,T,K> parent;
    private KdNode<D,T,K> leftSon;
    private KdNode<D,T,K> rightSon;
    private final D data;
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
     * Shallow copy of other node
     * @param other instance of which is shallow copy done
     */
    public KdNode(KdNode<D,T,K> other) {
        this(other.parent, other.leftSon, other.rightSon, other.data, other.usedKey);
    }

    /**
     * Determines it's worth to search for some key in a subtree.
     * @param wantedKey key that it is being searched for
     * @return
     */
    public boolean canExistInSubtree(K wantedKey) {
        return wantedKey.fallsInto(this.maxDimValues);
    }

    /**
     * Determines whether node's key is same in all dimensions compared to otherKey.
     * @param otherKey compared key to key of this node
     * @return true - both keys are same in all dimensions. False else.
     */
    public boolean hasSameKey(K otherKey) {
        return (otherKey != null) && this.usedKey.isSameKey(otherKey);
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
    public boolean hasNoRelationships() { return !hasParent() && hasNoneSons();}

    /**
     * Node removes its child if it is his left or right son (determines internally)
     * @param node
     */
    public void removeChild(KdNode<D, T, K> node) {
        this.replaceChild(node, null);
    }

    /**
     * Assigns new son value (determines internally if left or right son should be replaced. No other relationships
     * changed.
     * @param originNode son to replace (left or right)
     * @param newNode substituting son
     */
    public void replaceChild(KdNode<D, T, K> originNode, KdNode<D,T,K> newNode) {
        if (originNode == null)
            return;
        if (this.leftSon == originNode)
            this.leftSon = newNode;
        else if (this.rightSon == originNode)
            this.rightSon = newNode;
    }


    /**
     * Replaces calling instance on its position with other node - substitute - by replacing all related connections.
     * The condition that substitute exists in the subtree of the node, that is going to be replaced, must be true!
     * All connections of calling instance will be removed.
     * @param substitute
     * @return self
     */
    public KdNode<D,T,K> selfsubstitute(KdNode<D, T, K> substitute) {
        if (substitute == null)
            throw new NullPointerException("Substitute node cannot be null");
        KdNode<D,T,K> subsParent = substitute.getParent();      // substitute's original parent
        KdNode<D,T,K> subsLeftSon = substitute.getLeftSon();    // substitute's original left son
        KdNode<D,T,K> subsRightSon = substitute.getRightSon();  // substitute's original right son

        //  v--- changing my parent's reference on me and substitute's parent
        if (this.parent != null) {
            this.parent.replaceChild(this, substitute); // remove myself as a child
            substitute.setParent(this.parent);                  // set newNode as parent's child
        } else {       // substituteNode will be root
            substitute.setParent(null);
        }
        this.parent = null;                                     // I don't have parent from now

        if (subsParent == this) {                               // there was direct relation between me and substitute

        } else {                                                // between me and substitute was someone else

        }
        return this;
    }

    @Override
    public String toString() {
        return "V{key=" + this.usedKey + '}';
//        return "V{key=" + this.usedKey + "; data=" + data.toString() + '}';
//        return "V{key=" + this.usedKey + "; maxKey=" + this.maxDimValues + "; data= " + this.data + "}";
    }


}
