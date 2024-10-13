package mpoljak.dataStructures.searchTrees.KdTree;

public class KDTree<T extends IKdComparable<T>> {
    private final int k;    // dimension of tree, k is from {1,2,...,n}
    private KdNode<T> root;

    public KDTree(int k) {
        this.k = k;
    }

    public void insert(T data) {} ;
    public void find(T data) {
        KdNode<T> currentNode = this.root;
        // k1 <= node.k1: go left
        // k1 > node.k1: go right
        // if level down => height++; else height--; height <0,h>
        int height = 0; // from 0, in order to start with dim = 1, which is the lowest number of dim

        while (currentNode != null) {

            int dim = (height % this.k) + 1;
            int cmp = data.compareTo(currentNode.getData(), dim);
            if (cmp == -1 || cmp == 0) {
                currentNode = currentNode.getLeftSon();
                height++;
            } else if (cmp == 1 ) {
                currentNode = currentNode.getRightSon();
                height++;
            } else {
                if (cmp == Error.INVALID_DIMENSION.getErrCode())
                    throw new java.lang.IllegalArgumentException("Node.compareTo(): Not a valid dimension");
                else if (cmp == Error.NULL_PARAMETER.getErrCode())
                    throw new NullPointerException("Node.compareTo(): NULL argument!");
            }

        }
    } ;
    public void remove(T data) {} ;


}
