package mpoljak.dataStructures.searchTrees.KdTree;

import java.util.*;
import java.lang.Integer;

/** functional interface */
//interface INodeEvaluation<T extends ISimilar<?>, K extends IKdComparable<K> > {
interface INodeEvaluation<E extends T, T extends ISame<T>, K extends IKdComparable<K> > {
    boolean evaluateNode(KdNode<E,T,K> node);
}

/** functional interface */
//interface INodeComparison<T extends ISimilar<?>, K extends IKdComparable<K> > {
interface INodeComparison<E extends T, T extends ISame<T>, K extends IKdComparable<K> > {
    boolean compareNodes(KdNode<E,T,K> node, KdNode<E,T,K> nodeWithExtreme);
}

/** functional interface */
//interface INodeProcessing<T extends ISimilar<?>, K extends IKdComparable<K> > {
interface INodeProcessing<E extends T, T extends ISame<T>, K extends IKdComparable<K> > {
    void processNode(KdNode<E,T,K> node);
}

/** functional interface */
//interface INodeRetrieving<T extends ISimilar<?>, K extends IKdComparable<K> > {
interface INodeRetrieving<E extends T, T extends ISame<T>, K extends IKdComparable<K> > {
    KdNode<E,T,K> retrieveNode(KdNode<E,T,K> node);
}

public class KDTree<E extends T, T extends ISame<T>, K extends IKdComparable<K> > {
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
        if (this.root == null) {
            this.root = new KdNode<E,T,K>(null, null, null, data, key);
            return;
        }
//        * k1 <= node.k1: go left; if k1 > node.k1: go right
//        * height <0,h>, h+1 levels
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

    /**
     * Finds all nodes, whose key value is equal to wanted key value
     * @param key wanted key
     * @return nodes with wanted key value | null if nothing was found
     */
    public List<E> findAll(K key) {
        List<NodeToProcess> lFound = this.findDuplicates(key);
        if (lFound == null)
            return null;

        List<E> lToReturn = new LinkedList<>();
        for (NodeToProcess n : lFound)
            lToReturn.add(n.nodeToProcess.getData());
        return lToReturn;
    }

    public E delete(K key, E data) {
//        List<NodeToProcess> lFoundDuplicates = this.findDuplicates(key);
        //TODO AK OBSAHUJE STROM IBA KOREN A TEN CHCEM ODSTRANIT, JE TO OSETRENE?
        NodeToProcess nodeToProcess = this.findUnique(key, data);
        if (nodeToProcess == null)
            return null;

        E deletedData = nodeToProcess.nodeToProcess.getData();

        KdNode<E,T,K> vForRepl = nodeToProcess.nodeToProcess; // node to delete
        MyInteger heightRef = new MyInteger(nodeToProcess.height);

        if (vForRepl.hasNoneSons()) {
            if (vForRepl == this.root)
                this.root = null;
            else
                vForRepl.getParent().removeChild(vForRepl);
            return deletedData;
        }
        NodeToProcess hole = null;  // copy of substitute node in its original place
        boolean orderingState = false;  // whe duplicates are ready and node is already deleted
        LinkedList<NodeToProcess> lToDelete = new LinkedList<>();
        List<KdNode<E,T,K>> lToReinsert = new LinkedList<>(); //nodes deleted from right subtree that need to be reins.
        KdNode<E,T,K> vSubstitute = null;
        int wantedDim, heightOfReplaced;

        do {
            if (vForRepl.hasLeftSon()) {   // if there's possibility going left, go left
                wantedDim = (heightRef.intVal % this.k) + 1;   // by which dim it is compared at height of node to delete
                heightOfReplaced = heightRef.intVal();
                heightRef.setVal(heightRef.intVal() + 1);   // going to the level of left/right son

                vSubstitute = findMax(wantedDim, vForRepl.getLeftSon(), heightRef);
                hole = new NodeToProcess(new KdNode<>(vSubstitute), heightRef.intVal()); // remember values
                // ^--<- need to remember substitute's relationships
//                -------------------------------------BEG
                doReplacementFromLeftSubtree(vForRepl, vSubstitute, hole);
                updateHeightIfNeeded(vSubstitute, heightOfReplaced, lToDelete); // possibly came here after deleting from right sub
                //  UPDATE OF NODE TO PROCESS
                heightRef.setVal(hole.height);
                vForRepl = hole.nodeToProcess;
//                -------------------------------------END
            } else if (vForRepl.hasRightSon()) {  // v.hasOnlyRightSon
                wantedDim = (heightRef.intVal % this.k) + 1;   // by which dim it is compared at height of node to delete
                heightOfReplaced = heightRef.intVal();
                heightRef.setVal(heightRef.intVal() + 1);   // going to the level of left/right son

                vSubstitute = findMin(wantedDim, vForRepl.getRightSon(), heightRef);
                hole = new NodeToProcess(new KdNode<>(vSubstitute), heightRef.intVal()); // remember values
                // ^--<- need to remember substitute's relationships
//                    if (!orderingState) { // it is pre-ordering state, // commented 03.11
                    this.findAllEqualInDim(vForRepl, heightOfReplaced, wantedDim, vSubstitute, lToDelete);
//                    }
//                -------------------------------------BEG
                doReplacementFromRightSubtree(vForRepl, vSubstitute, hole);
                if (orderingState) { // must do update, if vSubstitute is to replace node in other place
                    updateHeightIfNeeded(vSubstitute, heightOfReplaced, lToDelete);
                }
                // here won't be updateOfHeight, because this vSubstitute is excluded from insertion to lToDelete
//                    if (!vForRepl.hasNoneSons()) // continue replacing nodes  todo zak. 03,11
                if (!hole.nodeToProcess.hasNoneSons()) // continue replacing nodes  todo pridane. 03,11
                    orderingState = true;
//                }
                //  UPDATE OF NODE TO PROCESS
                heightRef.setVal(hole.height);
                vForRepl = hole.nodeToProcess;
//                -------------------------------------END
            } else {    // vForRep.hasNoneSons = true
//                if (vForRepl.getParent() == null)
//                    throw new RuntimeException("hlbka: "+heightRef.intVal+", "+ this.size()+",hasNoneSons:"+vForRepl.hasNoneSons());
                vForRepl.getParent().removeChild(vForRepl); // deleting relationships of helpful node with its parent
                vForRepl.setParent(null);
                NodeToProcess ntp = lToDelete.isEmpty() ? null : lToDelete.removeLast();
                if (ntp == null) {
                    vForRepl = null;
                } else {
                    vForRepl = ntp.nodeToProcess;
                    heightRef.setVal(ntp.height);
                    if (vForRepl.hasNoRelationships())
                        System.out.println("V="+vForRepl+", height="+heightRef.intVal);
                }
                if (vForRepl != null)
                    lToReinsert.add(vForRepl);
                orderingState = false;
            }
        } while (vForRepl != null);
//        } while (vForRepl != null && !vForRepl.hasNoneSons());

        for (KdNode<E,T,K> n : lToReinsert) {
            this.insert(n.getUsedKey(), n.getData());
        }
        return deletedData;
    }

    /**
     * @return true if tree has not root node.
     */
    public boolean isEmpty() {
        return this.root == null;
    }

    public int size() {
        if (this.root == null)
            return 0;
        int elementsCount = 0;
        KdTreeInOrderIterator<E,T,K> iterator = this.inOrderIterator();
        while (iterator.hasNext()) {
            iterator.getNextNode();
            elementsCount++;
        }
//        this.inOrderProcessing((node) -> this.elementsCount++, false);
        return elementsCount;
    }

    public KdTreeInOrderIterator<E,T,K> inOrderIterator() {
        return new KdTreeInOrderIterator<E,T,K>(this.root);
    }

    public KdTreeLevelOrderIterator<E,T,K> levelOrderIterator() {
        return new KdTreeLevelOrderIterator<E,T,K>(this.root);
    }

    public class KdTreeInOrderIterator<D extends B, B extends ISame<B>, Q extends IKdComparable<Q>>
            implements Iterator<D> {

        private KdNode<D,B,Q> currentNode;
        private boolean isLeftSubTreeProcessed;

        private KdTreeInOrderIterator(KdNode<D,B,Q> treeRoot) {
            if (treeRoot == null)
                throw new NullPointerException("Cannot make iterator for not existing tree structure");
            this.currentNode = treeRoot;
            this.isLeftSubTreeProcessed = false;
            this.getNextNode(); // prepare first in-order node
        }

        @Override
        public boolean hasNext() {
            return this.currentNode != null;
        }

        @Override
        public D next() {
            D currentData = this.currentNode == null ? null : this.currentNode.getData();
            this.getNextNode(); // prepare next node
            return currentData;
        }

        private void getNextNode() {
            while (this.currentNode != null) {
                if (!this.isLeftSubTreeProcessed) {
                    while (this.currentNode.hasLeftSon()) {
                        this.currentNode = this.currentNode.getLeftSon();
                    }
                    this.isLeftSubTreeProcessed = true;
                    return;
                }
                else { // left subtree is processed
                    if (this.currentNode.hasRightSon()) {
                        this.currentNode = this.currentNode.getRightSon();
                        this.isLeftSubTreeProcessed = false;  // continue processing left subtree of right son
                    }
                    else {
                        while (this.currentNode.hasParent() &&
                                this.currentNode.getParent().isRightSon(this.currentNode)) {
                            this.currentNode = this.currentNode.getParent();
                        }
                        this.currentNode = this.currentNode.getParent();
                        return;
                    }
                }
            }
        }
    }

    public class KdTreeLevelOrderIterator<D extends B, B extends ISame<B>, Q extends IKdComparable<Q>>
            implements Iterator<D> {
        private LinkedList< KdNode<D,B,Q> > notProcessed;
        private KdNode<D,B,Q> currentNode;

        private KdTreeLevelOrderIterator(KdNode<D,B,Q> rootNode) {
            if (rootNode == null)
                throw new NullPointerException("Cannot make iterator for not existing tree structure");
            this.currentNode = null;
            this.notProcessed = new LinkedList<>();
            this.notProcessed.addLast(rootNode);
            this.getNextNode();
        }

        @Override
        public boolean hasNext() {
            return !this.notProcessed.isEmpty() || this.currentNode != null;
        }

        @Override
        public D next() {
            D currentData = this.currentNode == null ? null : this.currentNode.getData();
            this.getNextNode(); // prepare next data
            return currentData;
        }

        private void getNextNode () {
            if (this.notProcessed.isEmpty()) {
                this.currentNode = null;
                return;
            }
            this.currentNode = this.notProcessed.removeFirst();
            if (this.currentNode.hasLeftSon())
                this.notProcessed.addLast(this.currentNode.getLeftSon());
            if (this.currentNode.hasRightSon())
                this.notProcessed.addLast(this.currentNode.getRightSon());
        }
    }

//  ------------------------------------- V I S U A L I Z A T I O N --------------------------------------------------
    public void printTree() {
//        inOrderProcessing(operationPrint, false);    // in-order for 1-dimension
        this.inOrderProcessing(null, true);      // general hierarchical structure
    }

//    public void printRootMin() {
//        KdNode<E,T,K> min = findMin(1,this.root.getLeftSon(),new MyInteger(1));
//        System.out.println("  -  MIN to the root's LEFT subtree: " + (min == null ? "NULL" : min.getUsedKey()));
//
//        min = findMin(1,this.root.getRightSon(),new MyInteger(1));
//        System.out.println("  -  MIN to the root's RIGHT subtree: " + (min == null ? "NULL" : min.getUsedKey()));
//    }
//
//    public void printRootMax() {
//        System.out.println(root);
//        KdNode<E,T,K> max = findMax(1,this.root.getLeftSon(),new MyInteger(1));
//        System.out.println("  +  MAX to the root's LEFT subtree: " + (max == null ? "NULL" : max.getUsedKey()));
//
//        max = findMax(1,this.root.getRightSon(),new MyInteger(1));
//        System.out.println("  +  MAX to the root's RIGHT subtree: " + (max == null ? "NULL" : max.getUsedKey()));
//    }

// -------------------------------------------- P R I V A T E -------------------------------------------------------
    private class MyInteger {
        private int intVal;
        MyInteger(int val) { this.intVal = val; }

        public int intVal() {
            return this.intVal;
        }

        public void setVal(int val) {
            this.intVal = val;
        }
    }

    private class NodeToProcess {
        int height;
        final KdNode<E,T,K> nodeToProcess;

        NodeToProcess(KdNode<E,T,K> node, int height) {
            this.height = height;
            this.nodeToProcess = node;
        }
    }

    private void updateHeightIfNeeded(KdNode<E,T,K> movedNode, int newHeight, LinkedList<NodeToProcess> lNodes) {
        for (NodeToProcess n : lNodes) {
            if (n.nodeToProcess == movedNode)
                n.height = newHeight;
        }
    }

    /**
     * Finds all nodes which are equal for key in given dimension. If found node already exists in list, it won't be
     * added to the list again, but if its height has changed, it will be updated in existing instance of NodeToProcess.
     * @param startingNode node, where search starts
     * @param nodeToExclude node whose key will be used to find nodes by given dim. This node won't be added to lFound
     *                      list.
     * @param height is height of starting node
     * @param dim by which dimension will be equality checked
     * @param lFound adds found elements to this list. The instance of list must already exist, else the method
     *              execution ends.
     */
//    private void findAllEqualInDim(KdNode<E,T,K> startingNode, MyInteger startingHeight, int dim, KdNode<E,T,K> nodeToExclude, LinkedList<NodeToProcess> lFound) {
    private void findAllEqualInDim(KdNode<E,T,K> startingNode, int height, int dim, KdNode<E,T,K> nodeToExclude,
                                   LinkedList<NodeToProcess> lFound) {
        if (nodeToExclude == null)
            return;
        K key = nodeToExclude.getUsedKey();
        KdNode<E,T,K> currentNode = startingNode;
        LinkedList<NodeToProcess> lNotProcessed = new LinkedList<>();

        while (currentNode != null) {
            if (currentNode.compareTo(key, dim) == 0 && currentNode != nodeToExclude) {
                // check whether this instance has already been inserted into the list. No duplicate references allowed.
                boolean isAlreadyIn = false;
                for (NodeToProcess n : lFound) {
                    if (n.nodeToProcess == currentNode) {
//                        n.height = height;
                        isAlreadyIn = true;
                        break;
                    }
                }
                if (!isAlreadyIn)
                    lFound.addLast(new NodeToProcess(currentNode, height));
            }
            if (currentNode.hasRightSon()) {
                lNotProcessed.addLast(new NodeToProcess(currentNode.getRightSon(), height + 1));
            }

            currentNode = currentNode.getLeftSon();
            height++;
            if (currentNode == null && !lNotProcessed.isEmpty()) {
                NodeToProcess ntp = lNotProcessed.removeLast();
                currentNode = ntp.nodeToProcess;
                height = ntp.height;
            }
        }
    }

    /**
     * Finds node with unique combination of key and data
     * @param key wanted key
     * @param data wanted data
     * @return wrapping type for wanted node with its height in the tree
     */
    private NodeToProcess findUnique(K key, E data) {
        List<NodeToProcess> lFound = this.findDuplicates(key);
        return this.findUnique(data, lFound);
    }

    /**
     * Tries to find data with unique data within given list of key duplicates
     * @param data wanted data
     * @param lDuplicates nodes with same key value
     * @return wrapped unique node | null
     */
    private NodeToProcess findUnique(E data, List<NodeToProcess> lDuplicates) {
        if (lDuplicates == null)
            return null;

        NodeToProcess foundNode = null;
        for (NodeToProcess v : lDuplicates) {
            if (data.isSame(v.nodeToProcess.getData())) {
                foundNode = v;
                break;
            }
        }
        return foundNode;
    }

    /**
     * Finds all nodes, whose key value is equal to wanted key value
     * @param key wanted key
     * @return nodes with wanted key value
     */
    private List<NodeToProcess> findDuplicates(K key) {
        MyInteger height = new MyInteger(0);
        KdNode<E,T,K> resultNode = findNodeWithKey(key, this.root, height);

        if (resultNode == null)
            return null;
        List<NodeToProcess> foundNodes = new ArrayList<>();
        do {
            foundNodes.add(new NodeToProcess(resultNode, height.intVal()));
            height.setVal(height.intVal() + 1); // because i'm passing left son as parameter, so height has changed
            resultNode = findNodeWithKey(key, resultNode.getLeftSon(), height);
        } while (resultNode != null);

        return foundNodes;
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
        int height = currentHeight.intVal();
        int cmp = Integer.MIN_VALUE;
        int dim = Integer.MIN_VALUE;

        // (k1 <= node.k1: go left) ; (k1 > node.k1: go right)
        // height <0,h>
//        while (currentNode != null && currentNode.canExistInSubtree(key)) { // TODO docasne zakomentovane
        while (currentNode != null) {
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

    /**
     * By defined node, which represents subtree, searches for the first occurrence of node with maximum in wanted
     * dimension.
     * @param wantedDim specifies for which dimension is minimum searched
     * @param startingNode from which node (included) is minimum searched
     * @param currentHeight height of startingNode
     * @return first occurrence of node with found minimum and currentHeight instance updated to height of found node
     */
    private KdNode<E,T,K> findMin(int wantedDim, KdNode<E,T,K> startingNode, MyInteger currentHeight) {
        return findExtreme(wantedDim, startingNode, currentHeight,
                (node1, node2) -> node1.compareTo(node2, wantedDim) < 0,
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
     * @return first occurrence of node with found maximum and currentHeight instance updated to height of found node
     */
    private KdNode<E,T,K> findMax(int wantedDim, KdNode<E,T,K> startingNode, MyInteger currentHeight) {
        return findExtreme(wantedDim, startingNode, currentHeight,
                (node1, node2) -> node1.compareTo(node2, wantedDim) > 0,
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
     * @return first occurrence of node with found extreme and in currentHeight instance updated to height of found node
     */
    private KdNode<E,T,K> findExtreme(int wantedDim, KdNode<E,T,K> startingNode, MyInteger currentHeight,
                                  INodeComparison<E,T,K> comparator, INodeEvaluation<E,T,K> otherSonCheck,
                                  INodeRetrieving<E,T,K> otherSon, INodeRetrieving<E,T,K> baseSon) {
        KdNode<E,T,K> currentNode = startingNode;
        KdNode<E,T,K> nodeWithExtreme = startingNode;
        LinkedList<NodeToProcess> lNotProcessed = new LinkedList<NodeToProcess>();
        int dim;
        int height = currentHeight.intVal();

        while (currentNode != null) {
            dim = (height % this.k) + 1;
            if (comparator.compareNodes(currentNode, nodeWithExtreme)) {    // can be evaluated for any level of tree
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
//        currentHeight.setVal(height);
        return nodeWithExtreme;
    }

    private void doReplacementFromLeftSubtree(KdNode<E,T,K> vForRepl, KdNode<E,T,K> vSubstitute, NodeToProcess hole) {
        if (vForRepl == vSubstitute.getParent() || vForRepl.isLeftSon(vSubstitute)) {  // DIRECT RELATIONSHIP between deleted and substituted node
            // LEFT SON OF REPLACED
            hole.nodeToProcess.setParent(vSubstitute);  // do not change hole's sons
            vSubstitute.setLeftSon(hole.nodeToProcess);
            vForRepl.setLeftSon(null);
            // RIGHT SON OF REPLACED
            repairRightSonRelationshipsInLeftSubtree(vForRepl, vSubstitute);
            // PARENT OF REPLACED
            doReplacementWithParent(vForRepl, vSubstitute);
        } else {  // NO DIRECT RELATIONSHIP between deleted and substituted node
            // MAKING HOLE AS SON OF SUBSTITUTE
            if (vSubstitute.getParent().isLeftSon(vSubstitute))
                vSubstitute.getParent().setLeftSon(hole.nodeToProcess);
            else if (vSubstitute.getParent().isRightSon(vSubstitute))
                vSubstitute.getParent().setRightSon(hole.nodeToProcess);
            else
                throw new NoSuchElementException("ERROR:Inner node is not son of its parent. Fix needed");
            // LEFT SON OF REPLACED
            vForRepl.getLeftSon().setParent(vSubstitute);
            vSubstitute.setLeftSon(vForRepl.getLeftSon());
            vForRepl.setLeftSon(null);
            // RIGHT SON OF REPLACED
            repairRightSonRelationshipsInLeftSubtree(vForRepl, vSubstitute);
            // PARENT OF REPLACED
            doReplacementWithParent(vForRepl, vSubstitute);
        }
    }

    private void doReplacementFromRightSubtree(KdNode<E,T,K> vForRepl, KdNode<E,T,K> vSubstitute, NodeToProcess hole) {
        if (vForRepl == vSubstitute.getParent() || vForRepl.isRightSon(vSubstitute)) {// DIRECT RELATIONSHIP between deleted and substituted node
            // RIGHT SON OF REPLACED
            hole.nodeToProcess.setParent(vSubstitute);  // do not change hole's sons
            vSubstitute.setRightSon(hole.nodeToProcess);
            vForRepl.setRightSon(null);
            // LEFT SON OF REPLACED
            repairLeftSonRelationshipsInRightSubtree(vForRepl, vSubstitute);
            // PARENT OF REPLACED
            doReplacementWithParent(vForRepl, vSubstitute);
        } else {  // NO DIRECT RELATIONSHIP between deleted and substituted node
            // MAKING HOLE AS SON OF SUBSTITUTE
            if (vSubstitute.getParent().isLeftSon(vSubstitute))
                vSubstitute.getParent().setLeftSon(hole.nodeToProcess);
            else if (vSubstitute.getParent().isRightSon(vSubstitute))
                vSubstitute.getParent().setRightSon(hole.nodeToProcess);
            else
                throw new NoSuchElementException("ERROR:Inner node is not son of its parent. Fix needed");
            // RIGHT SON OF REPLACED
            vForRepl.getRightSon().setParent(vSubstitute);
            vSubstitute.setRightSon(vForRepl.getRightSon());
            vForRepl.setRightSon(null);
            // LEFT SON OF REPLACED
            repairLeftSonRelationshipsInRightSubtree(vForRepl, vSubstitute);
            // PARENT OF REPLACED
            doReplacementWithParent(vForRepl, vSubstitute);
        }
    }

    /**
     * Assigns references of parent of node to be deleted to substituting node.
     * @param vForRepl node that is going to be deleted
     * @param vSubstitute node that will replace node in place of deleted node
     */
    private void doReplacementWithParent(KdNode<E,T,K> vForRepl, KdNode<E,T,K> vSubstitute) {
        if (isRoot(vForRepl)) {
            setRoot(vSubstitute);
            vSubstitute.setParent(null);
        } else {
            if (vForRepl.getParent().isLeftSon(vForRepl))
                vForRepl.getParent().setLeftSon(vSubstitute);
            else if (vForRepl.getParent().isRightSon(vForRepl))
                vForRepl.getParent().setRightSon(vSubstitute);
            else
                throw new NoSuchElementException("ERROR:Inner node is not son of its parent. Fix needed");
            vSubstitute.setParent(vForRepl.getParent());
            vForRepl.setParent(null);
        }
    }

    /**
     * Assigns references of right son of node to be deleted to substituting node.
     * @param vForRepl node that is going to be deleted
     * @param vSubstitute node that will replace node in place of deleted node
     */
    private void repairRightSonRelationshipsInLeftSubtree(KdNode<E,T,K> vForRepl, KdNode<E,T,K> vSubstitute) {
        if (vForRepl.hasRightSon()) {
            vForRepl.getRightSon().setParent(vSubstitute);
            vSubstitute.setRightSon(vForRepl.getRightSon());
            vForRepl.setRightSon(null);
        } else {
            vSubstitute.setRightSon(null);
        }
    }

    /**
     * Assigns references of left son of node to be deleted to substituting node.
     * @param vForRepl node that is going to be deleted
     * @param vSubstitute node that will replace node in place of deleted node
     */
    private void repairLeftSonRelationshipsInRightSubtree(KdNode<E,T,K> vForRepl, KdNode<E,T,K> vSubstitute) {
        if (vForRepl.hasLeftSon()) {
            vForRepl.getLeftSon().setParent(vSubstitute);
            vSubstitute.setLeftSon(vForRepl.getLeftSon());
            vForRepl.setLeftSon(null);
        } else {
            vSubstitute.setLeftSon(null);
        }
    }

    private void inOrderProcessing(INodeProcessing<E,T,K> operation, boolean printHierarchy) {
        ArrayList<Boolean> llb = new ArrayList<>();
        int level = 0;
        KdNode<E,T,K> current = this.root;
        if (current == null) {
            System.out.println("Nothing to process! K-d tree is empty.");
            return;
        }
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

    private void setRoot(KdNode<E,T,K> node) { this.root = node; }

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
