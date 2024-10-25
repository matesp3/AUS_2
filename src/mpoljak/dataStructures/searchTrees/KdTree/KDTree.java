package mpoljak.dataStructures.searchTrees.KdTree;
import java.util.ArrayList;
import java.lang.Integer;
import java.util.List;

/** functional interface */
interface IOperation <T, K extends IKdComparable<K, M>, M extends Comparable<M> > {
    void doSomething(KdNode<T,K,M> node);
}

//public class KDTree<T extends IKdComparable<T, K> & IKeySetChooseable, K extends Comparable<K> > {
//public class KDTree<E extends IKdComparableII<K, M>, K, M extends Comparable<M> > {
public class KDTree<E, K extends IKdComparable<K, M>, M extends Comparable<M> > {
    private static final int ROW_NODE_MAX = 30;
    private static final int PREFIX_LENGTH = 6;

    private final int k;    // dimension of tree, k is from {1,2,...,n}

    private final IOperation<E,K,M> operationPrint = (node) -> {
        System.out.println("[" + node.toString() + "], ");
    };

    private KdNode<E,K,M> root;

    /**
     * Use in case, inserted data has only one key set. No two equivalent key sets in data.
     * @param k defines how many dimensions will tree work with
     */
    public KDTree(int k) {
        if (k < 1)
            throw new IllegalArgumentException("Parameter 'k' must be positive number greater than zero");

        this.k = k;
    }

    public void insert(K key, E data) {
        /*
            * k1 <= node.k1: go left+
            * if k1 > node.k1: go right
            * height <0,h>, h+1 levels
         */
        if (this.root == null) {
            this.root = new KdNode<E,K,M>(null, null, null, data, 1, key);
            return;
        }

        KdNode<E,K,M> currentNode = this.root;
        boolean inserted = false;
        int height = 0; // from 0, in order to start with dim = 1, which is the lowest acceptable number of dim
        int dim = Integer.MIN_VALUE;    // undefined
        int cmp = Integer.MIN_VALUE;    // undefined

        while (!inserted) {
            dim = (height % this.k) + 1;
//            cmp = data.compareTo(currentNode.getData(), dim);
//            cmp = data.compareTo(currentNode.getData(), dim, this.keySetIdForTreeBuilding);
//            cmp = operationCompare.compareTo(data, currentNode.getData(), dim);
//            cmp = data.compare(data.getCompositeKey(), currentNode.getData().getCompositeKey(), dim);
            cmp = currentNode.compareTo(key, dim);
            if (cmp == 0 || cmp == 1) { // v------ go to the left subtree (currentNode has same or greater value of key)
                if (!currentNode.hasLeftSon()) {
                    KdNode<E,K,M> leafNode = new KdNode<E,K,M>(currentNode, null, null, data, dim, key);
                    currentNode.setLeftSon(leafNode);
                    inserted = true;
                }
                currentNode = currentNode.getLeftSon();
                height++;
            } else if (cmp == -1) {      // v------- go to the right subtree (currentNode has lower value of key)
                if (!currentNode.hasRightSon()) {
                    KdNode<E,K,M> leafNode = new KdNode<E,K,M>(currentNode, null, null, data, dim, key);
                    currentNode.setRightSon(leafNode);
                    inserted = true;
                }
                currentNode = currentNode.getRightSon();
                height++;
            } else {
                if (cmp == Error.INVALID_DIMENSION.getErrCode())
                    throw new java.lang.IllegalArgumentException("Node.compareTo(): Not a valid dimension");
                else if (cmp == Error.NULL_PARAMETER.getErrCode())
                    throw new NullPointerException("KdNode.compareTo(): NULL argument!");
            }
        }

        if (currentNode == null) throw new NullPointerException("CurrentNode should be inserted leaf, but is null.");

//        KdNode<T, K> parent = currentNode.getParent();
//        while (parent != null) {
////            dim = (height % this.k) + 1;
//            for (int i = 1; i <= this.k; i++) {
//                cmp = currentNode.getUpperBound(i).compareTo( parent.getUpperBound(i) );
//                if (cmp == 1) { // current.upper > parent.upper
//                    parent.setUpperBound( currentNode.getUpperBound(i), i);
//                }
//            }
//
//            currentNode = parent;
//            parent = parent.getParent();
//            height--;
//        }
    }

    public List<E> find(K key) {
        KdNode<E,K,M> resultNode = findNodeWithKey(key, this.root);
        if (resultNode == null)
            return null;
        List<E> foundNodes = new ArrayList<>();
        do {
            foundNodes.add(resultNode.getData());
            resultNode = findNodeWithKey(key, resultNode.getLeftSon());
        } while (resultNode != null);

        return foundNodes;
    }

    public void remove(K key) {}

    public void printTree() {
//        inOrderProcessing(operationPrint, false);    // in-order for 1-dimension
        inOrderProcessing(null, true);      // general hierarchical structure
    }

    /**
     * Tries to search in (sub)tree for the first occurrence of node with data, that are searched for.
     * @param key key to found
     * @param startingNode node(subtree) from which will be searched node with data on the way down
     * @return node with searched data | null if there's no node with wanted data in the given subtree
     */
    private KdNode<E,K,M> findNodeWithKey(K key, KdNode<E,K,M> startingNode) {
        if (startingNode == null)
            return null;
//            throw new NullPointerException("findNodeWithKey: starting node is null, cannot launch searching for data");

        KdNode<E,K,M> currentNode = startingNode;
        /* (k1 <= node.k1: go left) ; (k1 > node.k1: go right)
         * height <0,h> */
        int height = 0  ; // from 0, in order to start with dim = 1, which is the lowest number of dim
        int cmp = Integer.MIN_VALUE;
        int dim = Integer.MIN_VALUE;
        do {
            dim = (height++ % this.k) + 1;
            cmp = currentNode.compareTo(key, dim);
            if (cmp == 0) {
                System.out.println("level: " + height);
                return currentNode; // node with searched data found
            } else if (cmp == 1) {
                currentNode = currentNode.getLeftSon();
            } else if (cmp == -1) {
                currentNode = currentNode.getRightSon();
            } else {
                currentNode = null;
                if (cmp == Error.INVALID_DIMENSION.getErrCode())
                    throw new java.lang.IllegalArgumentException("Node.compareTo(): Not a valid dimension");
                else if (cmp == Error.NULL_PARAMETER.getErrCode())
                    throw new NullPointerException("Node.compareTo(): NULL argument!");
            }
        } while (currentNode != null);

        return currentNode;
    }

    private void inOrderProcessing(IOperation<E,K,M> operation, boolean printHierarchy) {
        ArrayList<Boolean> llb = new ArrayList<>();
        int level = 0;
        KdNode<E,K,M> current = this.root;
//        if (printHierarchy) printNode(level, current.getData().toString(), false, llb);
        if (printHierarchy) printNode(level, current.toString(), false, llb);
        boolean isLeftSubTreeProcessed = false;
        while (current != null) {
            if (!isLeftSubTreeProcessed) {
                if (current.hasLeftSon()) {
                    if (printHierarchy) {
                        llb.add(current.hasRightSon());  // if current has both sons
                        level++;
                    }
                    current = current.getLeftSon();
//                    if (printHierarchy) printNode(level, current.getData().toString(), true, llb);
                    if (printHierarchy) printNode(level, current.toString(), true, llb);
                } else { // hasRightSon
                    if (operation != null) operation.doSomething(current);
                    if (current.hasRightSon()) {
                        if (printHierarchy) {
                            level++;
                            llb.add(current.hasLeftSon());  // if current has both sons
                        }
                        current = current.getRightSon();
//                        if (printHierarchy) printNode(level, current.getData().toString(), false, llb);
                        if (printHierarchy) printNode(level, current.toString(), false, llb);
                    }
                    else { // hasNoneSon
                        KdNode<E,K,M> parent = current.getParent();
                        while (parent != null && !parent.isLeftSon(current)) {
                            if (printHierarchy) {
                                llb.remove(llb.size() - 1);
                                level--;
                            }
                            current = parent;
                            parent = parent.getParent();
                        }
                        if (printHierarchy) {
                            if (!llb.isEmpty())
                                llb.remove(llb.size() - 1);
                            level--;
                        }
                        current = parent;
                        isLeftSubTreeProcessed = true;
                    }
                }
            }
           else { // left subtree is processed
                if (operation != null) operation.doSomething(current);
               if (current.hasRightSon()) {
                   if (printHierarchy) {
                       level++;
                       llb.add(false);// left surely exists, because it was processed && after right son is no other son
                   }
                   current = current.getRightSon();
//                   if (printHierarchy) printNode(level, current.getData().toString(), false, llb);
                   if (printHierarchy) printNode(level, current.toString(), false, llb);
                   isLeftSubTreeProcessed = false;  // continue processing left subtree of right son
               }
               else {
                   while (current.getParent() != null && current.getParent().isRightSon(current)) {
                       if (printHierarchy) {
                           llb.remove(llb.size() - 1);
                           level--;
                       }
                       current = current.getParent();
                   }
                   if (current.getParent() == null) /* this could be true only for returning back to the root from
                                                       right subtree */
                       return;
                   else {
                       if (printHierarchy) {
                           llb.remove(llb.size() - 1);
                           level--;
                       }
                       current = current.getParent();
                   }
               }
           }
        }
    }

    private boolean isRoot(KdNode<E,K,M> node) { return this.root == node; }

    private static void printNode(int level, String nodeRepr, boolean isLeftSon, ArrayList<Boolean> llb) {
        nodeRepr = nodeRepr == null ? "?" : nodeRepr;
        String nodeType = String.format("_>(%c):", level == 0 ? 'O' : (isLeftSon) ? 'L' : 'R');   // Root | Left | Right
        String prefix = buildTreePrefix(level, llb, '|' );
        if (!isLeftSon) System.out.println(prefix);
        if (nodeRepr.length() > ROW_NODE_MAX) {
            int row = 1;
            int minIdx;
            int remaining = nodeRepr.length();
            //  v------replacing last char for case of wrapping nodeRepr-----v
            String alterPrefix = (prefix.isEmpty()) ? ""
                    : (prefix.substring(0, prefix.length() - 1) + (llb.get(level - 1) ? '|' : ' ') );
            String currExpr;
            while (remaining > 0) {
                minIdx = (row-1) * ROW_NODE_MAX;
                currExpr = nodeRepr.substring(minIdx, minIdx + Math.min(remaining, ROW_NODE_MAX));
                remaining -= ROW_NODE_MAX;
                currExpr = remaining < 0
                        ? (" ".repeat((-1) * remaining) + currExpr)
                        : currExpr;
                System.out.println( (row == 1 ? (prefix + nodeType) : (alterPrefix + " ".repeat(PREFIX_LENGTH)) ) +  currExpr);
                row++;
            }
        } else if (nodeRepr.length() < ROW_NODE_MAX) {
            nodeRepr = "_".repeat(ROW_NODE_MAX - nodeRepr.length()) + nodeRepr;
            System.out.println(prefix + nodeType + nodeRepr);
        } else
            System.out.println(prefix + nodeType + nodeRepr);
    }

    private static String buildTreePrefix(int level, ArrayList<Boolean> llb, char branchSign) {
        if (level < 1) return "";
        StringBuilder sb = new StringBuilder(level * (ROW_NODE_MAX + 1 + 1) + 1);

        for (int i = 0; i < level; i++) {
            sb.append(" ".repeat(PREFIX_LENGTH + ROW_NODE_MAX - 1));
            sb.append(llb.get(i) ? branchSign : ' ');
        }
        if (sb.charAt(sb.length() - 1) == ' ') {
            sb.deleteCharAt(sb.length() - 1);
            sb.append(branchSign);
        }
        return sb.toString();
    }

    private static void printList(List<Boolean> l) {
        for (Boolean i : l) {
            System.out.print((i ? 1 : 0) + ", ");
        }
        System.out.println();
    }
}
