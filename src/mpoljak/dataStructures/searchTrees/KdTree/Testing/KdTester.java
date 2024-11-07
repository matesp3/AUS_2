package mpoljak.dataStructures.searchTrees.KdTree.Testing;

import mpoljak.data.forTesting.Data2D;
import mpoljak.data.forTesting.Data4D;
import mpoljak.dataStructures.searchTrees.KdTree.IKdComparable;
import mpoljak.dataStructures.searchTrees.KdTree.ISame;
import mpoljak.dataStructures.searchTrees.KdTree.KDTree;
import mpoljak.utilities.BisectionSearch;
import mpoljak.utilities.DoubleComparator;
import mpoljak.utilities.IGeneratorId;
import mpoljak.utilities.IntegerIdGenerator;

import java.util.*;

/**
 * Class for testing main functionalities of K-dimensional(k-d) tree structure.
 */
//public class OperationsTester<D extends ISame<D> & IKdComparable<D> > {
public class KdTester<D extends ISame<D> & IKdComparable<D>, K extends Comparable<K>> {

    private static final int VAL_SEED_IDX = 0;  // standardised index (within seed array) of seed for generating data
    private static final int PROB_SEED_IDX = 1; /* standardised index (within seed array) of seed for generating
                                                   probability. */
    private static final int DEBUG_GEN_FOR_INSERTION_SEED = 1296616592; // for testing: SEARCH
    private static final int DEBUG_PROB_SEED = -1200112902;             // for testing: SEARCH
    private static final int DEBUG_OPERATION_SEED = 1697875354;         // for testing: OVERALL

    private final Random genR = new Random(new Random().nextInt());
    private final Comparator<D> comparator;
    private final IGeneratorId<K> idGenerator;
    private final IUniqueDataGenerator<D,K> dataGenerator;
    private final int dimensions;

    public KdTester(Comparator<D> comparator, IGeneratorId<K> idGenerator,
                    IUniqueDataGenerator<D, K> dataGenerator, int dimensionsCount) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        if (idGenerator == null) {
            throw new IllegalArgumentException("IDGenerator cannot be null");
        }
        if (dataGenerator == null) {
            throw new IllegalArgumentException("DataGenerator cannot be null");
        }
        if (dimensionsCount < 1) {
            throw new IllegalArgumentException("Dimensions count cannot be less than 1");
        }
        this.comparator = comparator;
        this.idGenerator = idGenerator;
        this.dataGenerator = dataGenerator;
        this.dimensions = dimensionsCount;
    }

    /**
     * Tests whether structure deletes nodes correctly and if remaining data in the structure after deletion are all
     * in right positions that means, that they are searchable - need to meet condition that duplicate keys in the
     * right subtree of the key that replaced deleted node will be reinserted correctly to the k-d tree.
     *
     * @param iterationsCount         how many times will delete test be executed
     * @param requiredTreeSize        initial amount of inserted elements to the tree before test is launched
     * @param deletionsCount          number of delete operations within single iteration. Elements to delete are either
     *                                observed duplicates or randomly chosen elements from k-d tree, in order to test as
     *                                duplicates as single-time occurring values.
     * @param dupInsertProb           probability by which will be created key as a duplicate of randomly chosen element
     *                                from already inserted keys, instead of generating new key.
     * @param logLevel                scalability of details about actions logged on console. Number from set
     *                                {-1,0,1,...}, where higher number means more information.
     */
    public boolean testDeleteFunctionality(int iterationsCount, int requiredTreeSize, int deletionsCount,
                                           double dupInsertProb, int logLevel, boolean debug) {
        if (requiredTreeSize < deletionsCount) {
            printError("TreeSize parameter must be greater or equal to deletionsCount");
            return false;
        }

        boolean overallOk = true;
        int seedOfObserved = 0;
        int[] seeds = {0, 0};
        int decisionSeed = 0;

        for (int iteration = 1; iteration <= iterationsCount; iteration++) {
//        ---------------------------------------------
            int[] forDeleting = {-1,0}; // [0] is index of element to delete from lObserved, [1] is alternative
            // starter position
            if (logLevel >= 1)
                printIteration(iteration, "DELETE TEST");
//        -- 1. STEP: prepare data
            KDTree<D, D, D> kdTree = new KDTree<>(this.dimensions);
            ArrayList<D> lObserved = new ArrayList<>(requiredTreeSize);
            seeds = insertData(requiredTreeSize, kdTree, lObserved, dupInsertProb, logLevel, debug);

//        -- 2. STEP: remember how many elements were there before deletion
            final int originalSize = kdTree.size();
            if (requiredTreeSize != originalSize)
                throw new RuntimeException("K-d tree does not contain required number of elements! " +
                        "RequiredSize=" + requiredTreeSize + " | RealSize=" + originalSize);

            for (int delNr = 1; delNr <= deletionsCount; delNr++) {
//              do evidence of deleted element(s)
//                int idxOfDeleted = genR.nextInt(lObserved.size());
                if (logLevel >= 2)
                    printIterationOperation("PICKING element to delete...");
                forDeleting = pickNotDeletedElement(lObserved, genR, forDeleting[1]);
                D dataToDelete = lObserved.get(forDeleting[0]); // get inserted element at random idx
                if (logLevel >= 2)
                    printInfo("Picked element for delete="+dataToDelete);
                lObserved.set(forDeleting[0], null);  // deleted elements are set as null in helper structure
//              search for all data same key as key of dataToDelete
                List<D> lDupBeforeDelete = kdTree.findAll(dataToDelete);
                if (lDupBeforeDelete == null)
                    throw new NullPointerException("K-d tree returned any duplicate for dataToDelete=" + dataToDelete);
//              remove dataToDelete instance from list of duplicates
                for (int i = 0; i < lDupBeforeDelete.size(); i++) {
                    if (lDupBeforeDelete.get(i).isSame(dataToDelete)) {
                        lDupBeforeDelete.remove(i);
                        break;
                    }
                }
//              sort duplicates by their unique id
                if (logLevel >= 2)
                    printIterationOperation("SORTING BEFORE delete..");
                lDupBeforeDelete.sort(this.comparator);
//              delete element
                if (logLevel >= 2)
                    printIterationOperation("DELETING element "+dataToDelete+" ...");
                kdTree.delete(dataToDelete, dataToDelete);
                int actualSize = kdTree.size();
                if (logLevel >= 2)
                    printInfo("Actual tree size="+actualSize+" . Must be="+(originalSize - delNr));
                if (actualSize != (originalSize - delNr))
                    throw new RuntimeException("Size after delete different than expected. Expected=" + (originalSize - delNr)
                            + " , Actual=" + actualSize);
//              search for duplicates of dataToDelete again
                if (logLevel >= 2)
                    printIterationOperation("Searching for all remaining duplicates from k-d tree..");
                List<D> lDupAfterDelete = kdTree.findAll(dataToDelete);
                if (lDupAfterDelete == null && !lDupBeforeDelete.isEmpty())
                    throw new RuntimeException("Problem with duplicate. Should retrieve something, but returned null");
                if (logLevel >= 2)
                    printIterationOperation("Comparing duplicates before VS after deletion..");
                if (lDupAfterDelete != null) {
                    lDupAfterDelete.sort(this.comparator);
//              check, whether originally retrieved duplicates without dataToDelete instance are the same as retrieved
//              after delete operation of dataToDelete instance
                    if (lDupBeforeDelete.size() == lDupAfterDelete.size()) {
                        for (int i = 0; i < lDupBeforeDelete.size(); i++) {
                            if (!lDupBeforeDelete.get(i).isSame(lDupAfterDelete.get(i)))
                                throw new RuntimeException("Not same! Problem with duplicates. Compared '"
                                        + lDupBeforeDelete.get(i) + "'  to  '" + lDupAfterDelete.get(i) + "'");
                        }
                        if (logLevel >= 2)
                            printInfo("Ok.. All duplicates are same.");
                    } else {
                        throw new RuntimeException("Amount of retrieved key duplicates after deleted instance before delete is"
                                + "is different as before delete. BeforeDelete=" + lDupBeforeDelete + " , AfterDelete="
                                + lDupAfterDelete);
                    }
                }
            }
            if (logLevel >= 2) {
                printIterationOperation("Checking whether all remained data are present in k-d tree..");
            }
//          At the end, check if remaining elements are in helper structure and k-d tree the same
            ArrayList<D> lRetrieved = new ArrayList<>(deletionsCount);
            getSortedTreeDataById(kdTree, lRetrieved);
            int hi = -1;
            for (int i = 0; i < lRetrieved.size(); i++) {
                D fromKd = lRetrieved.get(i);
                D fromHelper = null;
                do {
                    hi++;
                    fromHelper = lObserved.get(hi);
                } while (fromHelper == null && hi < lObserved.size());
                if (!fromHelper.isSame(fromKd))
                    throw new NoSuchElementException(i+"-th elements (in sorted order by id) are not the same!  -   "
                            +"fromHelperStructure="+fromHelper+"    VS      fromKdTree="+fromKd);
            }
            if (logLevel >= 2)
                printInfo("..Ok delete test passed");
        }

        if (logLevel >= 0)
            printOverallResult(overallOk, "DELETE test");
        return overallOk;
    }

    private int[] pickNotDeletedElement(ArrayList<D> lEvidence, Random g, int alternativeStarter) {
        int[] res = new int[2];
        int idxToDelete = -1;
        for (int i = 0; i < 10; i++) {
            idxToDelete = g.nextInt(lEvidence.size());
            if (lEvidence.get(idxToDelete) == null)
                idxToDelete = -1;
            else {
                res[0] = idxToDelete;
                res[1] = alternativeStarter;
                break;
            }
        }
        if (idxToDelete == -1) {
            for (int i = alternativeStarter; i < lEvidence.size(); i++) {
                if (lEvidence.get(i) != null) {
                    res[0] = i;
                    res[1] = i;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * Tests whether all inserted elements into the k-d tree are present in the structure. Every generated data instance
     * is inserted only once into the structure, so the test is to check if number of insertions is equal to number of
     * found elements in the tree using some type of order going through structure (here is used in-order node visiting)
     *
     * @param insertionsCount how many instances of data will be generated and inserted into the k-d tree
     * @param iterationsCount how many times will search test be executed
     */
    public boolean testInsertFunctionality(int insertionsCount, int iterationsCount, int logLevel) {
        boolean overallOk = true;
        for (int iteration = 1; iteration <= iterationsCount; iteration++) {
            ArrayList<D> lInserted = new ArrayList<>();
            ArrayList<D> lRetrieved = new ArrayList<>();
            if (logLevel >= 1)
                printIteration(iteration, "INSERTION TEST");
            int seedForValGen = new Random().nextInt();
            Random valGen = new Random(seedForValGen);

            KDTree<D, D, D> kdTree = new KDTree<>(this.dimensions);
            D nextData;

            if (logLevel >= 3)
                printIterationOperation("INSERTING ELEMENTS:");

            for (int i = 0; i < insertionsCount; i++) {
                nextData = generateDataInstance(valGen);
                kdTree.insert(nextData, nextData);
                lInserted.add(nextData);
                if (logLevel >= 3)
                    printListItem(i + 1, nextData.toString());
            }
            getSortedTreeDataById(kdTree, lRetrieved);
            if (logLevel >= 1) {
                printInfo("Inserted " + lInserted.size() + " instances generated with SEED=" + seedForValGen);
                printInfo("Found " + lRetrieved.size() + " elements in the k-d tree.");
            }
//            ----- evaluation
            boolean ok = true;
            if (lInserted.size() == lRetrieved.size()) {
                for (int i = 0; i < lInserted.size(); i++) {
                    if (logLevel >= 3)
                        printListItem(i + 1, "Comparison: " + lInserted.get(i) + "    VS      "
                                + lRetrieved.get(i));
                    if (!lInserted.get(i).isSame(lRetrieved.get(i))) {
                        if (logLevel >= 2) {
                            printError("Found difference. Inserted element=" + lInserted.get(i) +
                                    " != retrieved element=" + lRetrieved.get(i));
                        }
                        ok = false;
                    }
                }
                if (logLevel >= 2)
                    printInfo("Inserted and retrieved elements from k-d tree are same and sizes are same also.");
            } else {
                ok = false;
                if (logLevel >= 2) {
                    printError("Size of k-d tree is DIFFERENT as number of inserted elements.");
                }
            }
            overallOk = overallOk && ok;
//            ------ end of evaluation
            if (logLevel >= 1) {
                printIterationResult(ok, 1, "Insertion Test");
            }
        }
        if (logLevel >= 0)
            printOverallResult(overallOk, "INSERTION TEST");
        return overallOk;
    }

    public void testFindingAllDuplicates(int dataCount, double duplicateInsertionProbability, int logLevel, boolean debug) {

        ArrayList<D> lInserted = new ArrayList<>(dataCount);
        KDTree<D, D, D> kdTree = new KDTree<>(this.dimensions);

        int seedForValGen = debug ? DEBUG_GEN_FOR_INSERTION_SEED : genR.nextInt();
        Random valGen = new Random(seedForValGen);

        int seedForProb = debug ? DEBUG_PROB_SEED : genR.nextInt();
        Random probGenerator = new Random(seedForProb);

        if (logLevel >= 1)
            printHeader("GENERATED " + dataCount + " values...");

        // v-- first cannot be generated as duplicate
        D observedData = generateDataInstance(valGen);
        D nextData = observedData;
//        D nextData = generateDataInstance(valGen);
        kdTree.insert(nextData, nextData);
        lInserted.add(nextData);
        int myDuplicateCount = 1;

        if (logLevel >= 3)
            printListItem(1,"    -> inserting "+String.format("%-9s", "NEW")+" element " + nextData + "...");
        // first is inserted, now I'll generate duplicate or new element (new element could also be duplicate)
        for (int i = 0; i < dataCount-1; i++) {
            if (probGenerator.nextDouble() < duplicateInsertionProbability) {   // duplicate element
                nextData = lInserted.get(genR.nextInt(lInserted.size())); // copy construct, but use new id
                nextData = dataGenerator.copyConstruct(nextData, this.idGenerator.nextId());
                if (logLevel >= 3)
                    printListItem(i+2, "    => inserting DUPLICATE element " + nextData + "...");
            } else {  // new element
                nextData = generateDataInstance(valGen);
                if (logLevel >= 3)
                    printListItem(i+2,"    -> inserting "+String.format("%-9s", "NEW")
                                                +" element " + nextData + "...");
            }
            lInserted.add(nextData);
            kdTree.insert(nextData, nextData);
            if (nextData.isSameKey(observedData))
                myDuplicateCount++;
        }
        printInfo("\nReal duplicates count for value="+observedData+" is: "+myDuplicateCount);
        kdTree.printTree();
    }

    /**
     * Tests whether k-d tree is able to find all elements in its structure with same key value.
     *
     * @param iterationsCount    how many times will search test be executed
     * @param insertionCount     wanted size of k-d tree
     * @param dupProbability     probability, by which will be generated duplicate key from yet inserted elements.
     * @param logLevel           level of description printed on the console. From 0, higher number means more detailed
     *                           logs. {0,3}
     */
    public boolean testSearchFunctionality(int iterationsCount, int insertionCount,
                                           double dupProbability, int logLevel, boolean debug) {
        boolean overallOk = true;

        for (int iteration = 1; iteration <= iterationsCount; iteration++) {
            if (logLevel >= 1)
                printIteration(iteration, "SEARCH TEST");

            KDTree<D, D, D> kdTree = new KDTree<>(this.dimensions);
            ArrayList<D> lObserved = new ArrayList<>(insertionCount);

            boolean iterationOk = true;
            if (logLevel >= 2)
                printIterationOperation("INSERTING "+insertionCount+" elements to k-d tree:");

            int[] seeds = insertData(insertionCount, kdTree, lObserved, dupProbability, logLevel, debug);
            if (insertionCount != kdTree.size()) {
                iterationOk = false;
                if (logLevel >= 2)
                    printError("K-d tree after "+insertionCount+" insertions has NOT WANTED size.");
            } else {
                if (logLevel >= 2)
                    printInfo("K-d tree after "+insertionCount+" insertions HAS wanted size.");
            }
            if (logLevel >= 2)
                printIterationOperation("CHECKING EXISTENCE of inserted elements inside the k-d tree...");
            if (logLevel >= 3)
                printHeader("RESULTS:");
            int nr = 0;
            for (D insertedElement : lObserved) {
                nr++;
                boolean found = false;
                List<D> lFoundDuplicates = kdTree.findAll(insertedElement);
                if (lFoundDuplicates != null) {
                    for (D duplicate : lFoundDuplicates) {
                        if (duplicate.isSame(insertedElement)) {
                            found = true;
                            break;
                        }
                    }
                }
                iterationOk = iterationOk && found;
                if (logLevel >= 3) {
                    if (found)
                        printListItem(nr, "Inserted element "+insertedElement+" was FOUND in tree.");
                    else
                        printError("Inserted element "+insertedElement+" NOT found in k-d tree.");
                }
            }
            overallOk = overallOk && iterationOk;
        }
        if (logLevel >= 0)
            printOverallResult(overallOk, "SEARCH TEST");
        return overallOk;
    }


    public boolean doOverallTest(double insProb, double searchProb, double delProb, int operationsCount, int logLevel,
                                 boolean debug) {
        if (DoubleComparator.getInstance().compare( insProb+searchProb+delProb, 1, 0.02) != 0) {
            if (logLevel >= -1)
                printError("Sum of probabilities must be 1! Actual is "+(insProb+searchProb+delProb));
            return false;
        }
        boolean overallOk = true;

        int operationSeed = debug ? DEBUG_OPERATION_SEED : genR.nextInt();
        Random opGen = new Random(operationSeed);
        int valueSeed = debug ? DEBUG_GEN_FOR_INSERTION_SEED : genR.nextInt();
        Random valGen = new Random(valueSeed);

        final double searchBound = insProb + searchProb;
        DoubleComparator comp = DoubleComparator.getInstance();
        comp.setEpsilon(0.001);

        ArrayList<D> lObserved = new ArrayList<>(operationsCount);
        KDTree<D, D, D> kdTree = new KDTree<>(this.dimensions);

        for (int op = 1; op <= operationsCount; op++) { // do n operations
            if (logLevel >= 2)
                printIteration(op, "OPERATIONS TEST");
            double chosenOperation = opGen.nextDouble();
            boolean operationOk = true;

            if (comp.compare(chosenOperation, insProb) < 1) {           //  chosenOperation <= insProb
                D nextData = generateDataInstance(valGen);
                if (logLevel >= 1)
                    printIterationOperation("INSERTING new value = "+nextData);
                lObserved.add(nextData);
                kdTree.insert(nextData, nextData);
                // check whether size of k-d tree is greater just by one
                int actualTreeSize = kdTree.size();
                operationOk = (actualTreeSize == lObserved.size());
                if (logLevel >= 2) {
                    if (operationOk)
                        printInfo("Size after insertion is as expected: "+actualTreeSize);
                    else
                        printError("Size after insertion is NOT as expected. Actual="+actualTreeSize
                                +" , Expected"+lObserved.size());
                }
            }
            else if (comp.compare(chosenOperation, searchBound) < 1) {  // chosenOperation <= searchBound
                if (lObserved.isEmpty()) {
                    if (logLevel >= 1)
                        printInfo("Could not execute search, because observed structure is empty.");
                } else {
                    int findIdx = valGen.nextInt(lObserved.size());
                    D suspectedData = lObserved.get(findIdx);
                    if (logLevel >= 1)
                        printIterationOperation("SEARCHING for duplicates of element = " + suspectedData+"..");
                    // find all duplicates in list
                    ArrayList<D> lObservedDup = getAllDuplicatesFromHelperStruct(suspectedData, lObserved);
                    // find all duplicates in k-d tree
                    List<D> lFoundDuplicates = kdTree.findAll(suspectedData);
                    // size of both structures must be equal
                    operationOk = (lObservedDup.isEmpty())
                            ? (lFoundDuplicates == null) : (lFoundDuplicates.size() == lObservedDup.size());
                    // sort them and compare them
                    if (operationOk && lFoundDuplicates != null) {
                        lObservedDup.sort(this.comparator);
                        lFoundDuplicates.sort(this.comparator);
                        for (int el = 0; el < lObservedDup.size(); el++) {
                            if (!lObservedDup.get(el).isSame(lFoundDuplicates.get(el))) {
                                operationOk = false;
                                if (logLevel >= 2)
                                    printError("Retrieved duplicates of element's key " + suspectedData
                                            + "are different!");
                                break;
                            }
                        }
                        if (operationOk && logLevel >= 2)
                            printInfo(".. Ok. All found duplicates are same as observed.");
                    } else
                        printError("Retrieved amount of duplicates for data " + suspectedData + "is different than" +
                                "expected. Actual=" + lObservedDup.size() + " , Expected=" + (lFoundDuplicates == null ? 0 :
                                lFoundDuplicates.size())
                        );
                }
            }
            else {                                                      // chosenOperation <= 1
                if (lObserved.isEmpty()) {
                    if (logLevel >= 1)
                        printInfo("Could not execute delete, because observed structure is empty.");
                } else {
                    int deleteIdx = valGen.nextInt(lObserved.size());
                    D dataToDelete = lObserved.get(deleteIdx);
                    if (logLevel >= 1)
                        printIterationOperation("DELETING element = " + dataToDelete+"..");
                    // delete
                    lObserved.set(deleteIdx, lObserved.get(lObserved.size() - 1)); // move to the delete position last element
                    lObserved.remove(lObserved.size() - 1);     // remove last element. This is optimization.
                    kdTree.delete(dataToDelete, dataToDelete);
                    // get new size and compare with expected
                    int actualTreeSize = kdTree.size();
                    operationOk = (actualTreeSize == lObserved.size());

                    if (logLevel >= 2) {
                        if (operationOk)
                            printInfo("Size after delete is as expected: "+actualTreeSize);
                        else
                            printError("Size after delete is NOT as expected. Actual="+actualTreeSize
                                    +" , Expected="+lObserved.size());
                    }
                }
            }
            if (logLevel >= 1)
                printIterationResult(operationOk, op, "Operations test");
            overallOk = overallOk && operationOk;
        }
        // at the end, iterate list and tree as sorted lists and compare their elements which must be the same!
        ArrayList<D> lRemainingInTree = new ArrayList<>(lObserved.size());
        lObserved.sort(this.comparator);
        getSortedTreeDataById(kdTree, lRemainingInTree);
        overallOk = overallOk && (lObserved.size() == lRemainingInTree.size());
        if (overallOk) {
            for (int i = 0; i < lObserved.size(); i++) {
                if (!lObserved.get(i).isSame(lRemainingInTree.get(i))) {
                    overallOk = false;
                    if (logLevel >= 1)
                        printError("Final comparing of remaining data in helper structure and k-d tree are " +
                                "DIFFERENT! Details: Comparing data from helper struct="+lObserved.get(i)
                                +"   VS  kd-tree="+lRemainingInTree.get(i));
                    break;
                }
            }
        } else {
            if (logLevel >= 1)
                printError("Amount of observed elements in helper structure and k-d tree are DIFFERENT!");
        }

        if (!overallOk)
            printHeader("Used seeds: [operations_seed= "+operationSeed+", value_seed="+valueSeed+"]");
        return overallOk;
    }

//    ------------------------------------------ P R I V A T E ----------------------------------------------------

    private ArrayList<D> getAllDuplicatesFromHelperStruct(D dataKey, ArrayList<D> lObserved) {
        ArrayList<D> duplicates = new ArrayList<>();
        for (D element : lObserved) {
            if (element.isSameKey(dataKey))
                duplicates.add(element);
        }
        return duplicates;
    }

    /**
     * By using given parameters picks one key from lAvailable, sets it as null in list and returns it for deletion.
     *
     * @param lAvailable    list of keys from which is chosen one to be deleted. Internally is set as null, if picked for
     *                      deletion.
     * @param probDupDelete probability by which will be deleted duplicate key from set of observed duplicates
     * @param valGen        generator of decisions
     * @param lDuplicates   set of observed duplicate keys
     * @return key to be deleted from k-d tree
     */
    private D pickElement(ArrayList<D> lAvailable, double probDupDelete, Random valGen,
                               ArrayList<ArrayList<D>> lDuplicates) {
        int idxToDelete = -1;
        boolean picked = false;
        if (valGen.nextDouble() < probDupDelete) { // take some duplicate and find its index
            while (!picked && !lDuplicates.isEmpty()) {
                idxToDelete = valGen.nextInt(lDuplicates.size());
                int innerIdx = valGen.nextInt(lDuplicates.get(idxToDelete).size());
                D keyToDelete = lDuplicates.get(idxToDelete).remove(innerIdx);
                if (lDuplicates.get(idxToDelete).isEmpty()) {
                    lDuplicates.remove(idxToDelete);
                }
                idxToDelete = BisectionSearch.getIdx(lAvailable, keyToDelete, this.comparator);
                picked = idxToDelete != -1;
            }
        }
        if (!picked) {          // just take some element
            idxToDelete = valGen.nextInt(lAvailable.size());
            idxToDelete = lAvailable.get(idxToDelete) == null ? -1 : idxToDelete;
            while (idxToDelete == -1) {
                idxToDelete = valGen.nextInt(lAvailable.size());
                idxToDelete = lAvailable.get(idxToDelete) == null ? -1 : idxToDelete;
            }
        }
        D deletedKey = lAvailable.get(idxToDelete);
        lAvailable.set(idxToDelete, null); // mark that this element was used for delete operation
        return deletedKey;
    }

    /**
     * Retrieves all duplicate values from lObserved that were inserted and not delete (set as null) from k-d tree.
     *
     * @param lInserted            inserted and still not deleted values in k-d tree
     * @param lObserved            set of generated duplicate values to be observed
     * @param lRemainingDuplicates not deleted duplicates found in lInserted
     */
    private void updateRemainingDuplicates(ArrayList<D> lInserted, ArrayList<ArrayList<D>> lObserved,
                                           ArrayList<D> lRemainingDuplicates) {
        for (ArrayList<D> listOfDuplicates : lObserved) {
            for (D data : listOfDuplicates) {
                if (BisectionSearch.getIdx(lInserted, data, this.comparator) >= 0)
                    lRemainingDuplicates.add(data);
            }
        }
    }

    /**
     * Inserts generated data into the k-d tree instance. With probability given through duplicateInsertionProbability
     * parameter inserts duplicate key from already inserted dataset.
     *
     * @param dataCount                     quantity of elements, that will be inserted into the tree
     * @param kdTree                        instance where data will be inserted
     * @param lInserted                     generated data are appended to this list
     * @param duplicateInsertionProbability probability of inserting duplicate instead of creating random key
     * @param logLevel                      level of details printed on console {0,1,3}. Higher number for more information.
     * @return seeds {valueSeed, probabilityOfDuplicatesSeed} used for building k-d tree
     */
    private int[] insertData(int dataCount, KDTree<D, D, D> kdTree, ArrayList<D>
            lInserted, double duplicateInsertionProbability, int logLevel, boolean debug) {

        if (lInserted == null)
            return null;
        else
            lInserted.ensureCapacity(dataCount);

        int seedForValGen = debug ? DEBUG_GEN_FOR_INSERTION_SEED : genR.nextInt();
        Random valGen = new Random(seedForValGen);

        int seedForProb = debug ? DEBUG_PROB_SEED : genR.nextInt();
        Random probGenerator = new Random(seedForProb);

        if (logLevel >= 1)
            printHeader("GENERATED " + dataCount + " values...");

        // v-- first cannot be generated as duplicate
        D nextData = generateDataInstance(valGen);
        lInserted.add(nextData);
        kdTree.insert(nextData, nextData);
        if (logLevel >= 3)
            printListItem(1,"    -> inserting "+String.format("%-9s", "NEW")+" element " + nextData + "...");
        // first is inserted, now I'll generate duplicate or new element (new element could also be duplicate)
        for (int i = 0; i < dataCount-1; i++) {
            if (probGenerator.nextDouble() < duplicateInsertionProbability) {   // duplicate element
                int nextIdx = probGenerator.nextInt(lInserted.size());
                nextData = this.dataGenerator.copyConstruct(lInserted.get(nextIdx), this.idGenerator.nextId()); // copy construct, but use new id
                if (logLevel >= 3)
                    printListItem(i+2, "    => inserting DUPLICATE element " + nextData + "...");
            } else {  // new element
                nextData = generateDataInstance(valGen);
                if (logLevel >= 3)
                    printListItem(i+2,"    -> inserting "+String.format("%-9s", "NEW")+" element " + nextData + "...");
            }
            lInserted.add(nextData);
            kdTree.insert(nextData, nextData);
        }
        int[] seeds = new int[2];
        seeds[VAL_SEED_IDX] = seedForValGen;
        seeds[PROB_SEED_IDX] = seedForProb;
        return seeds;
    }

    /**
     * Randomly generates instance using valGen.
     *
     * @param valGen generator of values with set seed
     * @return new instance
     */
    private D generateDataInstance(Random valGen) {
            return this.dataGenerator.generateInstance(valGen, this.idGenerator.nextId());
    }

    /**
     * Retrieves all elements from given k-d tree instance, appends them to lData instance and sorts all in
     * lData instance.
     */
    private void getSortedTreeDataById(KDTree<D, D, D> kdTree, ArrayList<D> lData) {
        if (kdTree.isEmpty())
            return;
        KDTree<D, D, D>.KdTreeLevelOrderIterator<D, D, D> it = kdTree.levelOrderIterator();
//        KDTree<D, D, D>.KdTreeInOrderIterator<D, D, D> it = kdTree.inOrderIterator();
        while (it.hasNext()) {
            lData.add(it.next());
        }
        lData.sort(this.comparator);
    }

//    ------------------------------ L O G G I N G - helper methods ------------------------------------

    private static void printIteration(int iterationNr, String iterationOfWhat) {
        String str = "ITERATION[" + iterationNr + "] of " + iterationOfWhat;
        System.out.println("\n+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+   " + str + "   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
    }

    private static void printIterationOperation(String operationInfo) {
        System.out.println("\n * * * " + operationInfo);
    }

    private static void printIterationResult(boolean passed, int iterationNr, String testName) {
        System.out.println("      |_---> " + testName + " ITERATION[" + iterationNr + "]:   " +
                (passed ? "success" : "failed"));
    }

    private static void printInfo(String info) {
        System.out.println(" (i)    -   " + info);
    }

    private static void printHeader(String headerInfo) {
        System.out.println(" # " + headerInfo);
    }

    private static void printListItem(int itemNr, String itemInfo) {
        System.out.printf("   %d. %s%n", itemNr, itemInfo);
    }

    private static void printError(String errInfo) {
        System.out.println(" (!) ERROR  -   " + errInfo);
    }

    private static void printOverallResult(boolean passed, String testName) {
        System.out.println("\n            .___________________________________________________________________________.");
        System.out.println("            |                   " + String.format("%-50s", testName + " | RESULT: " +
                (passed ? "passed" : "failed")) + "      |");
        System.out.println("            +___________________________________________________________________________+");
    }

    private static String getTestName(int chosenOperation) {
        switch (chosenOperation) {
            case 0:
                return "OVERALL";
            case 1:
                return "INSERT";
            case 2:
                return "SEARCH";
            case 3:
                return "DELETE";
            case 4:
                return "SPECIFIC_DUPLICATE";
            default:
                return "UNKNOWN";
        }
    }

    /**
     * CONDITION: both arrays must have same size.
     */
    private void printListElementsComparison(ArrayList<D> lInserted, ArrayList<D> lRetrieved) {
        for (int i = 0; i < lInserted.size(); i++)
            printListItem(i + 1, "Comparison of: " + lInserted.get(i) + "     VS      " + lRetrieved.get(i));
    }
    //     ==============================================================================================
    //    / / / / / / / / / / / / / / / / > M  A  I  N <  / / / / / / / / / / / / / / / / / / / / / / / /
    //     ==============================================================================================
    /**
     * Used to trigger testing of k-d tree functionalities
     */
    public static void main(String[] args) {

        final int OVERALL_TEST              = 0;
        final int INSERT_TEST               = 1;
        final int SEARCH_TEST               = 2;
        final int DELETE_TEST               = 3;
        final int SPECIFIC_DUPLICATE_TEST   = 4;

        int chosenOperation = OVERALL_TEST;
        KdTester<Data2D, Integer> ot = new KdTester<Data2D, Integer>( // TODO GENERATOR of Strings
                new Data2D.Data2DComparator(),                        // TODO 2 add description of doAllTest
                IntegerIdGenerator.getInstance(),
                new Data2D.Data2DGenerator(),
                2
        );
//        KdTester<Data4D, Integer> ot = new KdTester<Data4D, Integer>(
//                new Data4D.Data4DComparator(),
//                IntegerIdGenerator.getInstance(),
//                new Data4D.Data4DGenerator(),
//                4
//        );
        boolean debug = false;
        boolean allOk = true;
//        -----------------------------------------------------
        if (chosenOperation == OVERALL_TEST) {
            if (!debug) {
                for (int i = 10; i < 100; i += 10) {
                    for (int s = 10; s < (100 - i); s += 10) {
                        int d = (100 - i - s);
                        System.out.println(String.format("\ninsert=%d%% , search=%d%% , delete=%d%%", i, s, d));
                        boolean testOk = ot.doOverallTest(i / 100.0, s / 100.0, d / 100.0, 20000, 0, false);
                        System.out.println("    |_-> test: " + (testOk ? "passed" : "failed"));
                        allOk = allOk && testOk;
                    }
                }
            } else {
                allOk = ot.doOverallTest(0.4, 0.2, 0.4, 100000, 0, false);
            }
        }
//        -----------------------------------------------------
        else if (chosenOperation == INSERT_TEST) {
            if (!debug) {
                for (int i = 1; i <= 100000; i*=10) {       // treeSize
                    System.out.println("\n>---  for params: i=" + i + "  ---v");
                    boolean ok = ot.testInsertFunctionality(1, i, -1);
                    System.out.println("..." + (ok ? "SUCCESS" : "FAILED"));
                    allOk = allOk && ok;
                }
            } else {
                int dbg_i = 50000;
                boolean ok = ot.testInsertFunctionality(1, dbg_i, -1);
            }
        }
//        -----------------------------------------------------
        else if (chosenOperation == SEARCH_TEST) {
            if (!debug) {
                for (int i = 1; i <= 100000; i*=10) {           // treeSize
                    for (double p = 0.09; p < 1.0; p += 0.1) {  // probability of generating duplicate
                        System.out.println("\n>---  for params: i=" + i + ", p=" + p + "  ---v");
                        boolean ok = ot.testSearchFunctionality(1, i, p, -1, false);
                        System.out.println("..." + (ok ? "SUCCESS" : "FAILED"));
                        allOk = allOk && ok;
                    }
                }
            } else {
                int dbg_i = 50000;
                double dbg_p = 0.09;
                ot.testSearchFunctionality(1, dbg_i, dbg_p, 1, true);
            }
        }
//        -----------------------------------------------------------------------------------
        else if (chosenOperation == DELETE_TEST) {  // passed for size after delete. Need to check, whether duplicates
            // are on the reachable position
            if (!debug) {
//                for (int i = 10; i <= 100000; i*=10) {               // treeSize
//                    for (double d = 0.01; d < 11; d+=3.33) {
                for (int i = 10000; i <= 100000; i*=10) {               // treeSize
                    for (double d = 3.34; d < 11; d+=3.33) {              // d*10 = percentage of deleted elements
                        for (double p = 0.01; p <= 0.35; p += 0.32) {      // probability of creating duplicate
                            System.out.println("\n v---  for params: i=" + i + ", deleted="+d*i/10+", p="+p+"  <---");
                            boolean ok = ot.testDeleteFunctionality(1, i, (int)d*i/10, p, 1, true);
                            System.out.println("..." + (ok ? "SUCCESS" : "FAILED"));
                            allOk = allOk && ok;
                        }
                    }
                }
            } else {
                int dbg_i = 100;
                int dbg_d = 10;
                double dbg_p = 0.01;
                ot.testDeleteFunctionality(1, dbg_i, dbg_d * dbg_i / 10, dbg_p, -1, true);
            }
        }
        else if (chosenOperation == SPECIFIC_DUPLICATE_TEST) {
            ot.testFindingAllDuplicates(200,0.2, 3,debug);
        }
//        -----------------------------------------------------------------------------------
        /*                                          R E S U L T                                                    */
        System.out.println("\n - - - - - - - - - - - - - - - - - - - -\n    "
                +getTestName(chosenOperation)+" TEST - ALL passed: "+allOk+(debug ? " [DEBUG]" : ""));
    }
}

