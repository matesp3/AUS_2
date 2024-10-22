package mpoljak.dataStructures.searchTrees.KdTree;
import java.util.ArrayList;
import java.lang.Integer;
import java.util.List;

/** functional interface */
interface IOperation <T extends IKdComparable<T, K> & IKeySetChooseable, K extends Comparable<K> > {
    void doSomething(KdNode<T, K> node);
}

interface IComparison <T extends IKdComparable<T, K> & IKeySetChooseable, K extends Comparable<K> > {
    int compareTo(T data, T otherData, int dim);
}

public class KDTree<T extends IKdComparable<T, K> & IKeySetChooseable, K extends Comparable<K> > {

    public static final int ONLY_ONE_KEY_SET_AVAILABLE = -1;
    private static final int ROW_NODE_MAX = 30;
    private static final int PREFIX_LENGTH = 6;

    private final int k;    // dimension of tree, k is from {1,2,...,n}
    private final int keySetIdForTreeBuilding;

    private final IOperation<T, K> operationPrint = (node) -> {
        System.out.println("[" + node.toString() + "], ");
    };

    private final IComparison<T, K> operationCompare;

    private KdNode<T,K> root;


    /**
     * Use when there are at least two equivalent key sets in data by which should be data inserted into the tree.
     * @param k defines how many dimensions will tree work with
     * @param keySetIdForTreeBuilding by which key from object's key set will tree be built. Starts from 1.
     */
    public KDTree(int k, int keySetIdForTreeBuilding) {
        if ((keySetIdForTreeBuilding != ONLY_ONE_KEY_SET_AVAILABLE && keySetIdForTreeBuilding < 1) || k < 1)
            throw new IllegalArgumentException("Parameters 'k' and 'keyId' must be positive number greater than zero");

        this.k = k;
        this.keySetIdForTreeBuilding = keySetIdForTreeBuilding - 1;

        this.operationCompare = (keySetIdForTreeBuilding == ONLY_ONE_KEY_SET_AVAILABLE)
                ? ( (data, otherData, dim) -> data.compareTo(otherData, dim) )
                : ( (data, otherData, dim) -> data.compareTo(otherData, dim, this.keySetIdForTreeBuilding) );
    }

    /**
     * Use in case, inserted data has only one key set. No two equivalent key sets in data.
     * @param k defines how many dimensions will tree work with
     */
    public KDTree(int k) {
        this(k, ONLY_ONE_KEY_SET_AVAILABLE);
    }

    public void insert(T data) {
        /*
            * k1 <= node.k1: go left
            * if k1 > node.k1: go right
            * height <0,h>, h+1 levels
         */
        if (this.root == null) {
            this.root = new KdNode<T,K>(null, null, null, data, 1);
            return;
        }

        KdNode<T,K> currentNode = this.root;
        boolean inserted = false;
        int height = 0; // from 0, in order to start with dim = 1, which is the lowest acceptable number of dim
        int dim = Integer.MIN_VALUE;    // undefined
        int cmp = Integer.MIN_VALUE;    // undefined

        while (!inserted) {
            dim = (height % this.k) + 1;
//            cmp = data.compareTo(currentNode.getData(), dim);
//            cmp = data.compareTo(currentNode.getData(), dim, this.keySetIdForTreeBuilding);
            cmp = operationCompare.compareTo(data, currentNode.getData(), dim);
            if (cmp == -1 || cmp == 0) { // v------ go to the left subtree
                if (!currentNode.hasLeftSon()) {
                    KdNode<T,K> leafNode = new KdNode<T,K>(currentNode, null, null, data, dim);
                    currentNode.setLeftSon(leafNode);
                    inserted = true;
                }
                currentNode = currentNode.getLeftSon();
                height++;
            } else if (cmp == 1) {      // v------- go to the right subtree
                if (!currentNode.hasRightSon()) {
                    KdNode<T,K> leafNode = new KdNode<T,K>(currentNode, null, null, data, dim);
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

    public void find(T data) {
//        KdNode<T> currentNode = this.root;
        // k1 <= node.k1: go left
        // k1 > node.k1: go right
        // if level down => height++; else height--; height <0,h>
//        int height = 0; // from 0, in order to start with dim = 1, which is the lowest number of dim
//
//        while (currentNode != null) {
//
//            int dim = (height % this.k) + 1;
//            int cmp = data.compareTo(currentNode.getData(), dim);
//            if (cmp == -1 || cmp == 0) {
//                currentNode = currentNode.getLeftSon();
//                height++;
//            } else if (cmp == 1 ) {
//                currentNode = currentNode.getRightSon();
//                height++;
//            } else {
//                if (cmp == Error.INVALID_DIMENSION.getErrCode())
//                    throw new java.lang.IllegalArgumentException("Node.compareTo(): Not a valid dimension");
//                else if (cmp == Error.NULL_PARAMETER.getErrCode())
//                    throw new NullPointerException("Node.compareTo(): NULL argument!");
//            }
//        }
    }

    public void remove(T data) {}

    public void printTree() {
//        inOrderProcessing(operationPrint, false);    // in-order for 1-dimension
        inOrderProcessing(null, true);      // general hierarchical structure
    }

    private void inOrderProcessing(IOperation<T, K> operation, boolean printHierarchy) {
        ArrayList<Boolean> llb = new ArrayList<>();
        int level = 0;
        KdNode<T,K> current = this.root;
        if (printHierarchy) printNode(level, current.getData().toString(), false, llb);
        boolean isLeftSubTreeProcessed = false;
        while (current != null) {
            if (!isLeftSubTreeProcessed) {
                if (current.hasLeftSon()) {
                    if (printHierarchy) {
                        llb.add(current.hasRightSon());  // if current has both sons
                        level++;
                    }
                    current = current.getLeftSon();
                    if (printHierarchy) printNode(level, current.getData().toString(), true, llb);

                } else { // hasRightSon
                    if (operation != null) operation.doSomething(current);
                    if (current.hasRightSon()) {
                        if (printHierarchy) {
                            level++;
                            llb.add(current.hasLeftSon());  // if current has both sons
                        }
                        current = current.getRightSon();
                        if (printHierarchy) printNode(level, current.getData().toString(), false, llb);

                    }
                    else { // hasNoneSon
                        KdNode<T,K> parent = current.getParent();
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
                   if (printHierarchy) printNode(level, current.getData().toString(), false, llb);
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
                   if (current.getParent() == null) // this could be true only for returning back to the root from
                                                    // right subtree
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

    private boolean isRoot(KdNode<T,K> node) { return this.root == node; }

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
            String alterPrefix = (prefix.length() == 0) ? ""
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
