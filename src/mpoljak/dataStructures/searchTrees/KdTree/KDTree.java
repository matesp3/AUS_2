package mpoljak.dataStructures.searchTrees.KdTree;
/** functional interface */
interface IOperation<D extends IKdComparable<D> > {
    void doSomething(KdNode<D> node);
}

public class KDTree<T extends IKdComparable<T>> {
    private final int k;    // dimension of tree, k is from {1,2,...,n}

    private final IOperation operation = (node) -> {
        System.out.println(node);
    };

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
    }

    public void remove(T data) {}

    private void inOrderProcessing(IOperation operation) {
        KdNode<T> current = this.root;
        boolean isLeftSonProcessed = false;
        while (current != null) {
            if (!isLeftSonProcessed) {
               if (current.hasLeftSon()) {
                  current = current.getLeftSon();
               } else {
                   operation.doSomething(current);
                   if (current.hasRightSon()) {
                       current = current.getRightSon();
                   }
                   else {
                       if (current.getParent().isLeftSon(current)) {
                           current = current.getParent();
                       }
                       else { // isRightSon
                           // current = current.parent;
                           // current = (current.hasParent()) ? current.parent : current;
                           current = current.getParent().getParent();    // may be null if current.parent is a root
                       }
                       isLeftSonProcessed = true;
                   }
               }
           }
           else { // left son is processed
               operation.doSomething(current);
               if (current.hasRightSon()) {
                   current = current.getRightSon();
                   isLeftSonProcessed = false;
               }
               else {
                   current = current.getParent();
               }
           }
        }
    }
}
