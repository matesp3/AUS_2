package mpoljak.dataStructures.searchTrees.KdTree;
import java.util.ArrayList;
import java.lang.Integer;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/** functional interface */
//interface INodeEvaluation<T extends ISimilar<?>, K extends IKdComparable<K> > {
interface INodeEvaluation<E extends T, T extends ISimilar<T>, K extends IKdComparable<K> > {
    boolean evaluateNode(KdNode<E,T,K> node);
}

/** functional interface */
//interface INodeComparison<T extends ISimilar<?>, K extends IKdComparable<K> > {
interface INodeComparison<E extends T, T extends ISimilar<T>, K extends IKdComparable<K> > {
    boolean compareNodes(KdNode<E,T,K> node, KdNode<E,T,K> nodeWithExtreme);
}

/** functional interface */
//interface INodeProcessing<T extends ISimilar<?>, K extends IKdComparable<K> > {
interface INodeProcessing<E extends T, T extends ISimilar<T>, K extends IKdComparable<K> > {
    void processNode(KdNode<E,T,K> node);
}

/** functional interface */
//interface INodeRetrieving<T extends ISimilar<?>, K extends IKdComparable<K> > {
interface INodeRetrieving<E extends T, T extends ISimilar<T>, K extends IKdComparable<K> > {
    KdNode<E,T,K> retrieveNode(KdNode<E,T,K> node);
}

public class KDTree<E extends T, T extends ISimilar<T>, K extends IKdComparable<K> > {
    private static final int ROW_NODE_MAX = 30;
    private static final int PREFIX_LENGTH = 6;

    private final int k;    // dimension of tree, k is from {1,2,...,n}

    private final INodeProcessing<E,T,K> operationPrint = (node) -> {
        System.out.println("[" + node.toString() + "], ");
    };

    private KdNode<E,T,K> root;

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
            this.root = new KdNode<E,T,K>(null, null, null, data, key);
            return;
        }

        KdNode<E,T,K> currentNode = this.root;
        boolean inserted = false;
        int height = 0; // from 0, in order to start with dim = 1, which is the lowest acceptable number of dim
        int dim = Integer.MIN_VALUE;    // undefined
        int cmp = Integer.MIN_VALUE;    // undefined

        while (!inserted) {
            currentNode.updateMaxKeyValues(key);
            dim = (height % this.k) + 1;
            cmp = currentNode.compareTo(key, dim);
            if (cmp == 0 || cmp == 1) { // v------ go to the left subtree (currentNode has same or greater value of key)
                if (!currentNode.hasLeftSon()) {
                    KdNode<E,T,K> leafNode = new KdNode<E,T,K>(currentNode, null, null, data, key);
                    currentNode.setLeftSon(leafNode);
                    inserted = true;
                }
                currentNode = currentNode.getLeftSon();
                height++;
            } else if (cmp == -1) {      // v------- go to the right subtree (currentNode has lower value of key)
                if (!currentNode.hasRightSon()) {
                    KdNode<E,T,K> leafNode = new KdNode<E,T,K>(currentNode, null, null, data, key);
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

        if (currentNode == null)
            throw new NullPointerException("CurrentNode should be inserted leaf, but is null.");
    }

    public List<KdNode<E,T,K>> find(K key) {
        MyInteger height = new MyInteger(0);
        KdNode<E,T,K> resultNode = findNodeWithKey(key, this.root, height);

        if (resultNode == null)
            return null;
        List<KdNode<E,T,K>> foundNodes = new ArrayList<>();
        do {
            foundNodes.add(resultNode);
            height.setVal(height.getVal() + 1); // because i'm passing left son as parameter, so height has changed
            resultNode = findNodeWithKey(key, resultNode.getLeftSon(), height);
        } while (resultNode != null);

        return foundNodes;
    }

    public void remove(K key) {}

    public void printTree() {
//        inOrderProcessing(operationPrint, false);    // in-order for 1-dimension
        inOrderProcessing(null, true);      // general hierarchical structure
    }

    public void deleteFromLefSubtree(K key, E data, int height) {
        List<KdNode<E,T,K>> lFound = this.find(key);
        KdNode<E,T,K> foundNode = null;
        for (KdNode<E,T,K> node : lFound) {
            if (data.isSame(node.getData())) {

            }
        }
    }

    private class MyInteger {
        private int intVal;
        MyInteger(int val) { this.intVal = val; }

        public int getVal() {
            return this.intVal;
        }

        public void setVal(int val) {
            this.intVal = val;
        }
    }

    private class NodeToProcess {
        final int height;
        final KdNode<E,T,K> nodeToProcess;

        NodeToProcess(KdNode<E,T,K> node, int height) {
            this.height = height;
            this.nodeToProcess = node;
        }
    }

    /**
     * Tries to search in (sub)tree for the first occurrence of node with data, that are searched for.
     * @param key key to found
     * @param startingNode node(subtree) from which will be searched node with data on the way down
     * @return node with searched data | null if there's no node with wanted data in the given subtree
     */
    private KdNode<E,T,K> findNodeWithKey(K key, KdNode<E,T,K> startingNode, MyInteger currentHeight) {
        if (startingNode == null)
            return null;

        KdNode<E,T,K> currentNode = startingNode;
        int height = currentHeight.getVal();
        int cmp = Integer.MIN_VALUE;
        int dim = Integer.MIN_VALUE;

        // (k1 <= node.k1: go left) ; (k1 > node.k1: go right)
        // height <0,h>
        while (currentNode != null && currentNode.canExistInSubtree(key)) {
            dim = (height++ % this.k) + 1;
            cmp = currentNode.compareTo(key, dim);
            if (cmp == 0) {
                currentHeight.setVal(height - 1); // send back where you have ended
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
        }

        return null; // out of the cycle there's no more node that meets criteria for equality
    }

    public void printRootMin() {
        KdNode<E,T,K> min = findMin(1,this.root.getLeftSon(),new MyInteger(1));
        System.out.println("  -  MIN to the root's LEFT subtree: " + (min == null ? "NULL" : min.getUsedKey()));

        min = findMin(1,this.root.getRightSon(),new MyInteger(1));
        System.out.println("  -  MIN to the root's RIGHT subtree: " + (min == null ? "NULL" : min.getUsedKey()));
    }

    public void printRootMax() {
        System.out.println(root);
        KdNode<E,T,K> max = findMax(1,this.root.getLeftSon(),new MyInteger(1));
        System.out.println("  +  MAX to the root's LEFT subtree: " + (max == null ? "NULL" : max.getUsedKey()));

        max = findMax(1,this.root.getRightSon(),new MyInteger(1));
        System.out.println("  +  MAX to the root's RIGHT subtree: " + (max == null ? "NULL" : max.getUsedKey()));
    }

    /**
     * By defined node, which represents subtree, searches for the first occurrence of node with maximum in wanted
     * dimension.
     * @param wantedDim specifies for which dimension is minimum searched
     * @param startingNode from which node (included) is minimum searched
     * @param currentHeight height of startingNode
     * @return first occurrence of node with found minimum
     */
    private KdNode<E,T,K> findMin(int wantedDim, KdNode<E,T,K> startingNode, MyInteger currentHeight) {
        return findExtreme(wantedDim, startingNode, currentHeight,
                (node1, node2) -> node1.compareTo(node2, wantedDim) == -1,
                (node) -> node.hasRightSon(),
                (node) -> node.getRightSon(),
                (node) -> node.getLeftSon());
    }

    /**
     * By defined node, which represents subtree, searches for the first occurrence of node with maximum in wanted
     * dimension.
     * @param wantedDim specifies for which dimension is maximum searched
     * @param startingNode from which node (included) is maximum searched
     * @param currentHeight height of startingNode
     * @return first occurrence of node with found maximum
     */
    private KdNode<E,T,K> findMax(int wantedDim, KdNode<E,T,K> startingNode, MyInteger currentHeight) {
        return findExtreme(wantedDim, startingNode, currentHeight,
                (node1, node2) -> node1.compareTo(node2, wantedDim) == 1,
                (node) -> node.hasLeftSon(),
                (node) -> node.getLeftSon(),
                (node) -> node.getRightSon());
    }

    /**
     * By defined node, which represents subtree, searches for the first occurrence of node with extreme in wanted
     * dimension. Extreme is defined by operations with nodes given in parameters.
     * @param wantedDim specifies for which dimension is extreme searched
     * @param startingNode from which node (included) is extreme searched
     * @param currentHeight height of startingNode
     * @param comparator defines if currently evaluated node should be lower, equal or greater than currently highest
     *                   found extreme
     * @param otherSonCheck defines if left's or right's son existence should be checked (as son of node, that has
     *                      dimension != wantedDim
     * @param otherSon defines if left or right son should be retrieved as other son in dimension != wantedDim
     * @param baseSon defines if left or right son should be retrieved as son in dimension = wantedDim
     * @return first occurrence of node with found extreme
     */
    private KdNode<E,T,K> findExtreme(int wantedDim, KdNode<E,T,K> startingNode, MyInteger currentHeight,
                                  INodeComparison<E,T,K> comparator, INodeEvaluation<E,T,K> otherSonCheck,
                                  INodeRetrieving<E,T,K> otherSon, INodeRetrieving<E,T,K> baseSon) {
        KdNode<E,T,K> currentNode = startingNode;
        KdNode<E,T,K> nodeWithExtreme = startingNode;
        LinkedList<NodeToProcess> lNotProcessed = new LinkedList<NodeToProcess>();
        int dim;
        int height = currentHeight.getVal();

        while (currentNode != null) {
            dim = (height % this.k) + 1;
            if (comparator.compareNodes(currentNode, nodeWithExtreme)) {
                nodeWithExtreme = currentNode;
                currentHeight.setVal(height);
            }
//            System.out.println("height= " + height + "; node=" +currentNode.toString());
            if (dim != wantedDim && otherSonCheck.evaluateNode(currentNode))  // other dim, must search trough both sons
                lNotProcessed.addLast(new NodeToProcess(otherSon.retrieveNode(currentNode), height + 1));

            currentNode = baseSon.retrieveNode(currentNode);
            if (currentNode != null)
                height++;
            else if (!lNotProcessed.isEmpty()) {
                height = lNotProcessed.getLast().height;
                currentNode = lNotProcessed.removeLast().nodeToProcess;
            }
        }

        currentHeight.setVal(height);
        return nodeWithExtreme;
    }

    private void inOrderProcessing(INodeProcessing<E,T,K> operation, boolean printHierarchy) {
        ArrayList<Boolean> llb = new ArrayList<>();
        int level = 0;
        KdNode<E,T,K> current = this.root;
//        if (printHierarchy) printNode(level, current.getData().toString(), false, llb);
        if (printHierarchy) printNode(level, current.toString(), false, llb);
        boolean isLeftSubTreeProcessed = false;
        while (current != null) {
            if (!isLeftSubTreeProcessed) {
                if (current.hasLeftSon()) {
                    if (printHierarchy) {
                        llb.add(new Boolean(current.hasRightSon()));  // if current has both sons
                        level++;
                    }
                    current = current.getLeftSon();
//                    if (printHierarchy) printNode(level, current.getData().toString(), true, llb);
                    if (printHierarchy) printNode(level, current.toString(), true, llb);
                } else { // hasRightSon
                    if (operation != null) operation.processNode(current);
                    if (current.hasRightSon()) {
                        if (printHierarchy) {
                            level++;
                            llb.add(new Boolean(current.hasLeftSon()));  // if current has both sons
                        }
                        current = current.getRightSon();
//                        if (printHierarchy) printNode(level, current.getData().toString(), false, llb);
                        if (printHierarchy) printNode(level, current.toString(), false, llb);
                    }
                    else { // hasNoneSon
                        KdNode<E,T,K> parent = current.getParent();
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
                if (operation != null) operation.processNode(current);
               if (current.hasRightSon()) {
                   if (printHierarchy) {
                       level++;
                       llb.add(new Boolean(false));// left surely exists, because it was processed && after right son is no other son
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

    private boolean isRoot(KdNode<E,T,K> node) { return this.root == node; }

    private static void printNode(int level, String nodeRepr, boolean isLeftSon, ArrayList<Boolean> llb) {
        nodeRepr = nodeRepr == null ? "?" : nodeRepr;
        String nodeType = String.format("_>(%s):", (level == 0 ? "O" : (isLeftSon) ? "L" : "R"));   // Root | Left | Right
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
