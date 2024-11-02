package mpoljak.dataStructures.searchTrees.KdTree.Testing;

import mpoljak.data.forTesting.Data4D;
import mpoljak.dataStructures.searchTrees.KdTree.IKdComparable;
import mpoljak.dataStructures.searchTrees.KdTree.KDTree;
import mpoljak.utilities.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class for testing main functionalities of K-dimensional(k-d) tree structure.
 */
//public class OperationsTester<D extends ISame<D> & IKdComparable<D> > {
public class OperationsTester {

    private static final int VAL_SEED_IDX = 0;  // standardised index (within seed array) of seed for generating data
    private static final int PROB_SEED_IDX = 1; /* standardised index (within seed array) of seed for generating
                                                   probability. */
    private String customCharSet = "abcdefghijklmnopqrstuvwxyz0123456789"; // set of chars entering generation of string

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
     * @param customCharSet not empty set of chars
     */
    public void setCustomCharSet(String customCharSet) {
        if (customCharSet != null && !customCharSet.isEmpty())
            this.customCharSet = customCharSet;
    }

    public void doAlg() {
        int suspectedCount = 10;
        int dataCount = 100;
        double duplicateProb = 0.15;
        KDTree<Data4D, Data4D, Data4D> kdTree = new KDTree<>(4);
        ArrayList< ArrayList<Data4D> > lSuspected = new ArrayList<>(suspectedCount);
        int logLevel = 3;
        int seedOfSuspected = generateObservedData(suspectedCount, lSuspected, logLevel);
        int[] seeds = insertData(dataCount, kdTree, lSuspected, null, duplicateProb, logLevel);
    }

    /**
     * Tests whether structure deletes nodes correctly and if remaining data in the structure after deletion are all
     * in right positions (that means, that they are searchable - need to meet condition that duplicate keys in the
     * right subtree of the key that replaced deleted node will be reinserted correctly to the k-d tree.
     * @param iterationsCount how many times will delete test be executed
     * @param treeSize initial amount of inserted elements to the tree before test is launched
     * @param deletionsCount number of delete operations within single iteration. Elements to delete are either observed
     *                       duplicates or randomly chosen elements from k-d tree, in order to test as duplicates as
     *                       single-time occurring values.
     * @param observedDuplicatesCount count of generates keys which will be observed in the test and inserted duplicate
     *                                multiple times. Each key is randomly chosen using even distribution of probability
     * @param dupInsertProb probability by which will be one of the observed duplicates from generated set inserted
     *                                      into the k-d tree. Probability of choosing concrete key from set is evenly
     *                                      distributed.
     * @param logLevel scalability of details about actions logged on console. Number from set {0,1,..}, where higher
     *                 number means more information.
     */
    public void testDeleteFunctionality(int iterationsCount, int treeSize, int deletionsCount,
                                        int observedDuplicatesCount, double dupInsertProb, int logLevel) {
        if (logLevel >= 1)
            printIteration(1, "DELETE TEST");
//        -- 1. STEP: prepare data
        KDTree<Data4D, Data4D, Data4D> kdTree = new KDTree<>(4);
        ArrayList<ArrayList<Data4D>> lObserved = new ArrayList<>(observedDuplicatesCount);
        int seedOfObserved = generateObservedData(observedDuplicatesCount, lObserved, logLevel);

        ArrayList<Data4D> lInserted = new ArrayList<>(treeSize);

        int[] seeds = insertData(treeSize, kdTree, lObserved, lInserted, dupInsertProb, logLevel);

        if (logLevel >= 3)
            printIterationOperation("SORTING LISTS for comparison...");
        ArrayList<Data4D> lRetrieved = new ArrayList<>(treeSize);
        getSortedTreeDataById(kdTree, lRetrieved);
        // IMPORTANT: by now, lInserted and lRetrieved are sorted ascending by ID and therefore must be equal lists
        if (logLevel >= 3) {
            printHeader("SORTED lists of inserted elements and retrieved elements from k-d tree:");
            printListElementsComparison(lInserted, lRetrieved);
        }
//        -- 2. STEP: remember how many elements were there before deletion
        final int originalSize = lRetrieved.size();
        if (treeSize != originalSize)
            throw new RuntimeException("K-d tree does not contain required number of elements! RequiredSize="+treeSize+
                    " | RealSize="+originalSize);
//        -- 3. STEP: delete element
        if (logLevel >= 3) {
            printIterationOperation("DELETING "+deletionsCount+" ELEMENTS from K-d tree...");
            printHeader("DELETED elements:");
        }
        for (int i = 0; i < deletionsCount; i++) {

        }
//        --- 3.1 STEP: find duplicate element in lRetrieved or randomly get some. Then remember index of element to del.
//        --- 3.2 STEP: delete element from the tree
//        --- 3.3 STEP: set null at index of deleted element
//        -- 4. STEP: evaluation
//        --- 4.1 STEP: check size of tree, if its less only by amount of deleted elements
//        --- 4.2 STEP: get new list of sorted k-d tree elements by their ID and compare it with lInserted list.
        boolean ok = true;
//        -- 5. STEP: check if all duplicates were able to find. If not, it is sign, that they are not on the place\
//                    where they should be. They are not searchable.
    }

    private void pickElement(ArrayList<Data4D> lAvailable, double probDupDelete) {

    }

    /**
     * Tests whether all inserted elements into the k-d tree are present in the structure. Every generated data instance
     * is inserted only once into the structure, so the test is to check if number of insertions is equal to number of
     * found elements in the tree using some type of order going through structure (here is used in-order node visiting)
     * @param insertionsCount how many instances of data will be generated and inserted into the k-d tree
     * @param iterationsCount how many times will search test be executed
     */
    public void testInsertFunctionality(int insertionsCount, int iterationsCount, int logLevel) {
        boolean overallOk = true;
        for (int iteration = 1; iteration <= iterationsCount; iteration++) {
            ArrayList<Data4D> lInserted = new ArrayList<>();
            ArrayList<Data4D> lRetrieved = new ArrayList<>();
            if (logLevel >= 1)
                printIteration(iteration, "INSERTION TEST");
            int seedForValGen = new Random().nextInt();
            Random valGen = new Random(seedForValGen);

            KDTree<Data4D, Data4D, Data4D> kdTree = new KDTree<>(4);
            Data4D nextData;

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
                printInfo("Found "+lRetrieved.size()+" elements in the k-d tree.");
            }
//            ----- evaluation
            boolean ok = true;
            if (lInserted.size() == lRetrieved.size()) {
                for (int i = 0; i < lInserted.size(); i++) {
                    if (logLevel >= 3)
                        printListItem(i+1, "Comparison: "+lInserted.get(i)+"    VS      "
                                +lRetrieved.get(i));
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
        printOverallResult(overallOk, "INSERTION TEST");
    }

    /**
     * Tests whether k-d tree is able to find all elements in its structure with same key value.
     * @param iterationsCount how many times will search test be executed
     * @param observedDuplicatesCount number of generated keys, that will be multiple times inserted to the tree and
     *                                recorded in other structure with its all insertions in order to compare result
     *                                from k-d tree search. Each key is randomly chosen from generated set using even
     *                                distribution of probability.
     * @param insertionCount total number of elements in k-d tree data structure
     * @param duplicateInsertProb probability, by which will be some duplicate key from observed set of duplicates
     *                            inserted to the k-d tree.
     * @param logLevel level of description printed on the console. Range <0;2>, where 0 is no information, 1 are main
     *                 actions and 2 are specific steps of actions and 3 are all insertions to the structure and search
     *                 results
     */
    public void testSearchFunctionality(int iterationsCount, int observedDuplicatesCount, int insertionCount,
                                        double duplicateInsertProb, int logLevel) {
        boolean overallOk = true;

        for (int iteration = 1; iteration <= iterationsCount ; iteration++) {
            if (logLevel >= 1)
                printIteration(iteration, "SEARCH TEST");

            KDTree<Data4D, Data4D, Data4D> kdTree = new KDTree<>(4);
            ArrayList<ArrayList<Data4D>> lObserved = new ArrayList<>(observedDuplicatesCount);

            int seedOfObserved = generateObservedData(observedDuplicatesCount, lObserved, logLevel);
            int[] seeds = insertData(insertionCount, kdTree, lObserved, null, duplicateInsertProb, logLevel);
            boolean ok = true;
            for (int i = 0; i < lObserved.size(); i++) {
                ok = ok && searchForDuplicate(i, logLevel, lObserved, kdTree);
            }
            if (logLevel >= 1)
                printIterationResult(ok, iteration, "SEARCH test for duplicate keys");
//        System.out.println("REAL NUMBER OF ELEMENTS in tree: " + kdTree.size());
            overallOk = overallOk && ok;
        }
        printOverallResult(overallOk, "SEARCH TEST");
    }

//    ------------------------------------------ P R I V A T E ----------------------------------------------------
    /**
     * Generates string of given length by using generator
     * @param length number of positions in generated string
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
     *
     * @param idx offset in list lDuplicates
     * @param logLevel level of details printed on console
     * @param lDuplicates prepared duplicate keys used to be searched for in k-d tree
     * @param kdTree instance on which will be search executed
     * @return true - if the amount of found elements in k-d tree is the same as the amount of elements in lDuplicates
     * on the given index and if the unique ids of elements in both structures (for found elements and for given index
     * in lDuplicates) are the same. Else false.
     */
    private boolean searchForDuplicate(int idx, int logLevel, ArrayList< ArrayList<Data4D> > lDuplicates,
                                       KDTree<Data4D, Data4D, Data4D> kdTree) {
        if (logLevel >= 3) {
    //        PRINT REMEMBER DUPLICATES FOR GIVEN INDEX
            printHeader("CREATED DUPLICATES:");
            for (int i = 0; i < lDuplicates.get(idx).size(); i++) {
                printListItem(i+1, lDuplicates.get(idx).get(i).toString());
            }
        }

        List<Data4D> lFound = kdTree.findAll(lDuplicates.get(idx).get(0));    // take one from the duplicates
        if (lFound == null)
            return false;
        lFound.sort(new Data4D.Data4DComparator());

        if (logLevel >= 3) {
//        FIND ALL DUPLICATES WITH THE KEY OF ELEMENT ON THE INDEX IN THE K-D TREE
            printHeader("FOUND DUPLICATES IN THE K-D TREE:");
            for (int i = 0; i < lFound.size(); i++) {
                printListItem(i+1, lFound.get(i).toString());
            }
        }
        if (lDuplicates.get(idx).size() != lFound.size()) {
            if (logLevel >= 2)
                printError("Different count of created and found duplicates!");
            return false;
        }
        boolean allEqual = true;
        for (int i = 0; i < lFound.size(); i++) {
            if (lFound.get(i).getId() != lDuplicates.get(idx).get(i).getId()) {
                allEqual = false;
                break;
            }
        }
        if (logLevel >= 2) {
            if (allEqual)
                printInfo("SUCCESS!       -   created and found duplicates are matching.");
            else
                printInfo("TEST FAILED!   -   found duplicates are not equal to created.");
        }
        return allEqual;
    }

    /**
     * Inserts generated data into the k-d tree instance. With probability given through duplicateInsertionProbability
     * parameter takes randomly element (probability for choosing element is evenly distributed) from list of before
     * created duplicates and inserts it instead of creating new data
     * @param dataCount quantity of elements, that will be inserted into the tree
     * @param kdTree instance where data will be inserted
     * @param lObserved final list of keys, from which will duplicates be generated
     * @param lInserted if not null, inserts all elements that have been inserted into the k-d tree. Element are sorted
     *                  ascending by unique assigned id.
     * @param duplicateInsertionProbability probability of inserting duplicate instead of creating random key
     * @param logLevel level of details printed on console {0,1,2,3}. Higher number for more information.
     * @return seeds {valueSeed, probabilityOfDuplicatesSeed} used for building k-d tree
     */
    private int[] insertData(int dataCount, KDTree<Data4D, Data4D, Data4D> kdTree, ArrayList<ArrayList<Data4D>>
                                                    lObserved, ArrayList<Data4D> lInserted,
                                                    double duplicateInsertionProbability, int logLevel) {
        Random seedGenerator = new Random();
        int seedForValGen = seedGenerator.nextInt();
        Random valGen = new Random(seedForValGen);
        int seedForProb = seedGenerator.nextInt();
        Random probGenerator = new Random(seedForProb);

        if (logLevel >= 1)
            printIterationOperation("GENERATING "+dataCount+" VALUES... VALUE_SEED="+seedForValGen +
                    "; PROBABILITY_SEED=" + seedForProb);

        for (ArrayList<Data4D> keyDuplicates : lObserved) {
            if (logLevel >= 3)
                System.out.println("    => inserting NEW element " + keyDuplicates.get(0) + "...");
            kdTree.insert(keyDuplicates.get(0), keyDuplicates.get(0)); // original duplicates haven't been inserted yet
            if (lInserted != null) {
                lInserted.add(keyDuplicates.get(0));
            }
        }
        if (lInserted != null)
            lInserted.sort(new Data4D.Data4DComparator()); // must be sorted by unique id
        Data4D nextData;
        dataCount = dataCount - lObserved.size();   /* because I need to insert duplicates for the first time and don't
                                                       want to affect number of inserted elements*/
        for (int i = 0; i < dataCount; i++) {
            if (probGenerator.nextDouble() < duplicateInsertionProbability) {   // duplicate element
                int nextIdx = probGenerator.nextInt(lObserved.size());
                nextData = new Data4D(lObserved.get(nextIdx).get(0), IdGenerator.getInstance().nextId());
                lObserved.get(nextIdx).add(nextData);   // remember newly create duplicate
                if (logLevel >= 3)
                    System.out.println("    => inserting DUPLICATE element " + nextData + "...");
            }
            else {  // new element
                nextData = generateDataInstance(valGen);
                if (logLevel >= 3)
                    System.out.println("    -> inserting NEW element " + nextData + "...");
            }
            kdTree.insert(nextData, nextData);
            if (lInserted != null)
                lInserted.add(nextData);
        }
        int[] seeds = new int[2];
        seeds[VAL_SEED_IDX] = seedForValGen;
        seeds[PROB_SEED_IDX] = seedForProb;
        return seeds;
    }

    /**
     * Randomly generates keys. Each nested ArrayList structure will initially contain only first element on index 0.
     * @param observedCount number of elements inserted to the list 'lObserved'
     * @param lObserved structure holding generated data
     * @param logLvl {0,1,2}
     * @return generated seed, by which were data generated
     */
    private int generateObservedData(int observedCount, ArrayList< ArrayList<Data4D> > lObserved, int logLvl){
        int seedForValGen = new Random().nextInt();
//        Random valGen = new Random();
        Random valGen = new Random(seedForValGen);
        if (logLvl >= 1)
            printIterationOperation("GENERATING "+observedCount+" observed data for duplicates");
        for (int i = 0; i < observedCount; i++) {
            Data4D nextData = generateDataInstance(valGen);
            if (logLvl >= 2)
                printListItem(i+1, "new data: " + nextData);
            ArrayList<Data4D> list = new ArrayList<>();
            list.add(nextData);
            lObserved.add(list);
        }
        return seedForValGen;
    }

    /**
     * Randomly generates instance using valGen.
     * @param valGen generator of values with set seed
     * @return new instance
     */
    private Data4D generateDataInstance(Random valGen) {
        return new Data4D(valGen.nextDouble() * valGen.nextInt(), nextString(10, valGen),
                valGen.nextInt(), valGen.nextDouble() * valGen.nextInt(), IdGenerator.getInstance().nextId());
    }

    private static void getSortedTreeDataById(KDTree<Data4D, Data4D, Data4D> kdTree, ArrayList<Data4D> lData) {
        KDTree<Data4D, Data4D, Data4D>.KdTreeLevelOrderIterator<Data4D, Data4D, Data4D> it = kdTree.levelOrderIterator();
//        KDTree<Data4D, Data4D, Data4D>.KdTreeInOrderIterator<Data4D, Data4D, Data4D> it = kdTree.inOrderIterator();
        while (it.hasNext()) {
            lData.add(it.next());
        }
        lData.sort(new Data4D.Data4DComparator());
    }

//    ------------------------------ L O G G I N G - helper methods ------------------------------------

    private static void printIteration(int iterationNr, String iterationOfWhat) {
        String str = "ITERATION[" + iterationNr + "] of " + iterationOfWhat;
        System.out.println("\n+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+   "+str+"   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
    }

    private static void printIterationOperation(String operationInfo) {
        System.out.println(" * * * " + operationInfo);
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

    /** CONDITION: both arrays must have same size. */
    private static void printListElementsComparison(ArrayList<Data4D> lInserted, ArrayList<Data4D> lRetrieved) {
        for (int i = 0; i < lInserted.size(); i++)
            printListItem(i+1, "Comparison of: "+lInserted.get(i)+"     VS      "+lRetrieved.get(i));
    }
}
