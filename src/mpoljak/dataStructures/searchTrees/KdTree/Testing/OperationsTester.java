package mpoljak.dataStructures.searchTrees.KdTree.Testing;

import mpoljak.data.forTesting.Data2D;
import mpoljak.data.forTesting.Data4D;
import mpoljak.dataStructures.searchTrees.KdTree.KDTree;
import mpoljak.utilities.BisectionSearch;
import mpoljak.utilities.IdGenerator;

import java.util.*;

/**
 * Class for testing main functionalities of K-dimensional(k-d) tree structure.
 */
//public class OperationsTester<D extends ISame<D> & IKdComparable<D> > {
public class OperationsTester {

    private static final int VAL_SEED_IDX = 0;  // standardised index (within seed array) of seed for generating data
    private static final int PROB_SEED_IDX = 1; /* standardised index (within seed array) of seed for generating
                                                   probability. */
    private String customCharSet = "abcdefghijklmnopqrstuvwxyz0123456789"; // set of chars entering generation of string
    private final Random genR = new Random(new Random().nextInt());

    private static final int DEBUG_GEN_OBSERVED_SEED = -130083375;      // for testing:
    private static final int DEBUG_GEN_FOR_INSERTION_SEED = 1296616592; // for testing: SEARCH
    private static final int DEBUG_PROB_SEED = -1200112902;             // for testing: SEARCH
    private static final int DEBUG_DECISION_SEED = 1697875354;          // for testing:


    /**
     * @return set of chars entering generation of string. Example: customCharSet = "abcd1234". This means that string
     * value can be generated only from this set of chars.
     */
    public String getCustomCharSet() {
        return this.customCharSet;
    }

    /**
     * Sets new set of chars entering generation of string. Example: customCharSet = "abcd1234". This means that string
     * value can be generated only from this set of chars.
     *
     * @param customCharSet not empty set of chars
     */
    public void setCustomCharSet(String customCharSet) {
        if (customCharSet != null && !customCharSet.isEmpty())
            this.customCharSet = customCharSet;
    }

    /**
     * Tests whether structure deletes nodes correctly and if remaining data in the structure after deletion are all
     * in right positions (that means, that they are searchable - need to meet condition that duplicate keys in the
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
            KDTree<Data2D, Data2D, Data2D> kdTree = new KDTree<>(2);
            ArrayList<Data2D> lObserved = new ArrayList<>(requiredTreeSize);
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
                Data2D dataToDelete = lObserved.get(forDeleting[0]); // get inserted element at random idx
                if (logLevel >= 2)
                    printInfo("Picked element for delete="+dataToDelete);
                lObserved.set(forDeleting[0], null);  // deleted elements are set as null in helper structure
//              search for all data same key as key of dataToDelete
                List<Data2D> lDupBeforeDelete = kdTree.findAll(dataToDelete);
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
                lDupBeforeDelete.sort(new Data2D.Data2DComparator());
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
                List<Data2D> lDupAfterDelete = kdTree.findAll(dataToDelete);
                if (lDupAfterDelete == null && lDupBeforeDelete.size() != 0)
                    throw new RuntimeException("Problem with duplicate. Should retrieve something, but returned null");
                if (lDupAfterDelete != null) {
                    lDupAfterDelete.sort(new Data2D.Data2DComparator());
//              check, whether originally retrieved duplicates without dataToDeleete instance are the same as retrieved
//              after delete operation of dataToDelete instance
                    if (lDupBeforeDelete.size() == lDupAfterDelete.size()) {
                        for (int i = 0; i < lDupBeforeDelete.size(); i++) {
                            if (!lDupBeforeDelete.get(i).isSame(lDupAfterDelete.get(i)))
                                throw new RuntimeException("Not same! Problem with duplicates. Compared '"
                                        + lDupBeforeDelete.get(i) + "'  to  '" + lDupAfterDelete.get(i) + "'");
                        }
                    } else {
                        throw new RuntimeException("Amount of retrieved key duplicates after deleted instance before delete is"
                                + "is different as before delete. BeforeDelete=" + lDupBeforeDelete + " , AfterDelete="
                                + lDupAfterDelete);
                    }
                }
            }
//          At the end, check if remaining elements are in helper structure and k-d tree the same
            ArrayList<Data2D> lRetrieved = new ArrayList<>(deletionsCount);
            getSortedTreeDataById(kdTree, lRetrieved);
            int hi = -1;
            for (int i = 0; i < lRetrieved.size(); i++) {
                Data2D fromKd = lRetrieved.get(i);
                Data2D fromHelper = null;
                do {
                    hi++;
                    fromHelper = lObserved.get(hi);
                } while (fromHelper == null && hi < lObserved.size());
                if (!fromHelper.isSame(fromKd))
                    throw new NoSuchElementException(i+"-th elements (in sorted order by id) are not the same!  -   "
                    +"fromHelperStructure="+fromHelper+"    VS      fromKdTree="+fromKd);
            }
        }

        if (logLevel >= 0)
            printOverallResult(overallOk, "DELETE test");
        return overallOk;
    }

    private int[] pickNotDeletedElement(ArrayList<Data2D> lEvidence, Random g, int alternativeStarter) {
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
            ArrayList<Data2D> lInserted = new ArrayList<>();
            ArrayList<Data2D> lRetrieved = new ArrayList<>();
            if (logLevel >= 1)
                printIteration(iteration, "INSERTION TEST");
            int seedForValGen = new Random().nextInt();
            Random valGen = new Random(seedForValGen);

            KDTree<Data2D, Data2D, Data2D> kdTree = new KDTree<>(4);
            Data2D nextData;

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

        ArrayList<Data4D> lInserted = new ArrayList<>(dataCount);
        KDTree<Data4D, Data4D, Data4D> kdTree = new KDTree<>(4);

        int seedForValGen = debug ? DEBUG_GEN_FOR_INSERTION_SEED : genR.nextInt();
        Random valGen = new Random(seedForValGen);

        int seedForProb = debug ? DEBUG_PROB_SEED : genR.nextInt();
        Random probGenerator = new Random(seedForProb);

        if (logLevel >= 1)
            printHeader("GENERATED " + dataCount + " values...");

        // v-- first cannot be generated as duplicate
        Data4D observedData = gen4D();
        Data4D nextData = observedData;
//        Data4D nextData = generateDataInstance(valGen);
        kdTree.insert(nextData, nextData);
        lInserted.add(nextData);
        int myDuplicateCount = 1;

        if (logLevel >= 3)
            printListItem(1,"    -> inserting "+String.format("%-9s", "NEW")+" element " + nextData + "...");
        // first is inserted, now I'll generate duplicate or new element (new element could also be duplicate)
        for (int i = 0; i < dataCount-1; i++) {
            if (probGenerator.nextDouble() < duplicateInsertionProbability) {   // duplicate element
                nextData = new Data4D(lInserted.get(genR.nextInt(lInserted.size())),
                        IdGenerator.getInstance().nextId()); // copy construct, but use new id
                if (logLevel >= 3)
                    printListItem(i+2, "    => inserting DUPLICATE element " + nextData + "...");
            } else {  // new element
//                nextData = generateDataInstance(valGen);
                nextData = gen4D();
                if (logLevel >= 3)
                    printListItem(i+2,"    -> inserting "+String.format("%-9s", "NEW")+" element " + nextData + "...");
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

            KDTree<Data2D, Data2D, Data2D> kdTree = new KDTree<>(2);
            ArrayList<Data2D> lObserved = new ArrayList<>(insertionCount);

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
            for (Data2D insertedElement : lObserved) {
                nr++;
                boolean found = false;
                List<Data2D> lFoundDuplicates = kdTree.findAll(insertedElement);
                if (lFoundDuplicates != null) {
                    for (Data2D duplicate : lFoundDuplicates) {
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

//    ------------------------------------------ P R I V A T E ----------------------------------------------------
    /**
     * Generates string of given length by using generator
     *
     * @param length    number of positions in generated string
     * @param generator generator of integer values with some seed
     * @return generated string
     */
    private String nextString(int length, Random generator) {
        StringBuilder sb = new StringBuilder(length);
        int len = this.customCharSet.length();
        for (int i = 0; i < length; i++) {
            sb.append(this.customCharSet.charAt(generator.nextInt(len)));
        }
        return sb.toString();
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
    private Data2D pickElement(ArrayList<Data2D> lAvailable, double probDupDelete, Random valGen,
                               ArrayList<ArrayList<Data2D>> lDuplicates) {
        int idxToDelete = -1;
        boolean picked = false;
        if (valGen.nextDouble() < probDupDelete) { // take some duplicate and find its index
            while (!picked && !lDuplicates.isEmpty()) {
                idxToDelete = valGen.nextInt(lDuplicates.size());
                int innerIdx = valGen.nextInt(lDuplicates.get(idxToDelete).size());
                Data2D keyToDelete = lDuplicates.get(idxToDelete).remove(innerIdx);
                if (lDuplicates.get(idxToDelete).isEmpty()) {
                    lDuplicates.remove(idxToDelete);
                }
                idxToDelete = BisectionSearch.getIdx(lAvailable, keyToDelete, new Data2D.Data2DComparator());
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
        Data2D deletedKey = lAvailable.get(idxToDelete);
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
    private void updateRemainingDuplicates(ArrayList<Data2D> lInserted, ArrayList<ArrayList<Data2D>> lObserved,
                                           ArrayList<Data2D> lRemainingDuplicates) {
        for (ArrayList<Data2D> listOfDuplicates : lObserved) {
            for (Data2D data : listOfDuplicates) {
                if (BisectionSearch.getIdx(lInserted, data, new Data2D.Data2DComparator()) >= 0)
                    lRemainingDuplicates.add(data);
            }
        }
    }

    private Data4D gen4D() {
        setCustomCharSet("abcd");
        Random r = new Random();
        return new Data4D(1.0*(r.nextInt()%20),
                nextString(1, r),
                r.nextInt(),
                1.0*(r.nextInt()%20),
                IdGenerator.getInstance().nextId());
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
    private int[] insertData(int dataCount, KDTree<Data2D, Data2D, Data2D> kdTree, ArrayList<Data2D>
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
        Data2D nextData = generateDataInstance(valGen);
        lInserted.add(nextData);
        kdTree.insert(nextData, nextData);
        if (logLevel >= 3)
            printListItem(1,"    -> inserting "+String.format("%-9s", "NEW")+" element " + nextData + "...");
        // first is inserted, now I'll generate duplicate or new element (new element could also be duplicate)
        for (int i = 0; i < dataCount-1; i++) {
            if (probGenerator.nextDouble() < duplicateInsertionProbability) {   // duplicate element
                int nextIdx = probGenerator.nextInt(lInserted.size());
                nextData = new Data2D(lInserted.get(nextIdx),
                                        IdGenerator.getInstance().nextId()); // copy construct, but use new id
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
    private Data2D generateDataInstance(Random valGen) {
//        setCustomCharSet("abcde");
        return new Data2D(
                IdGenerator.getInstance().nextId(),
                (1+(Math.abs(valGen.nextInt()) % 100_00)), // 1-3
                (1 +(Math.abs(valGen.nextInt() % 100_00))) // 1-3
        );
//        return new Data2D(valGen.nextDouble() * (valGen.nextInt() % 10),
//                nextString(5, valGen),
//                valGen.nextInt() % 100,
//                valGen.nextDouble() * (valGen.nextInt() % 10),
//                IdGenerator.getInstance().nextId());
//        return new Data2D(valGen.nextDouble() * valGen.nextInt(), nextString(10, valGen),
//                valGen.nextInt(), valGen.nextDouble() * valGen.nextInt(), IdGenerator.getInstance().nextId());
    }

    /**
     * Retrieves all elements from given k-d tree instance and returns them sorted in lData instance.
     */
    private static void getSortedTreeDataById(KDTree<Data2D, Data2D, Data2D> kdTree, ArrayList<Data2D> lData) {
        if (kdTree.isEmpty())
            return;
        KDTree<Data2D, Data2D, Data2D>.KdTreeLevelOrderIterator<Data2D, Data2D, Data2D> it = kdTree.levelOrderIterator();
//        KDTree<Data2D, Data2D, Data2D>.KdTreeInOrderIterator<Data2D, Data2D, Data2D> it = kdTree.inOrderIterator();
        while (it.hasNext()) {
            lData.add(it.next());
        }
        lData.sort(new Data2D.Data2DComparator());
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

    /**
     * CONDITION: both arrays must have same size.
     */
    private static void printListElementsComparison(ArrayList<Data2D> lInserted, ArrayList<Data2D> lRetrieved) {
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
        final int INSERT_TEST = 1;
        final int SEARCH_TEST = 2;
        final int DELETE_TEST = 3;
        final int SPECIFIC_DUPLICATE_TEST = 4;
        int chosenOperation = DELETE_TEST;
        OperationsTester ot = new OperationsTester();
        boolean debug = false;
        boolean allOk = true;
//        -----------------------------------------------------
        if (chosenOperation == INSERT_TEST) {
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
                            boolean ok = ot.testDeleteFunctionality(1, i, (int)d*i/10, p, -1, true);
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
        System.out.println("\n"+ (chosenOperation == INSERT_TEST ? "INSERT" : (chosenOperation == DELETE_TEST ? "DELETE" :
                        (chosenOperation == SEARCH_TEST ? "SEARCH" : (chosenOperation == SPECIFIC_DUPLICATE_TEST ?
                                "DUPLICATES" : "UNKNOWN")))) + " TEST - ALL passed: " + allOk +
                (debug ? " [DEBUG]" : ""));
    }
}

