package mpoljak.dataStructures.searchTrees.KdTree.Testing;

import mpoljak.data.forTesting.Data2D;
import mpoljak.dataStructures.searchTrees.KdTree.KDTree;
import mpoljak.utilities.BisectionSearch;
import mpoljak.utilities.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class for testing main functionalities of K-dimensional(k-d) tree structure.
 */
//public class OperationsTester<D extends ISame<D> & IKdComparable<D> > {
public class OperationsTesterOld {

    private static final int VAL_SEED_IDX = 0;  // standardised index (within seed array) of seed for generating data
    private static final int PROB_SEED_IDX = 1; /* standardised index (within seed array) of seed for generating
                                                   probability. */
    private String customCharSet = "abcdefghijklmnopqrstuvwxyz0123456789"; // set of chars entering generation of string
    private final Random genR = new Random(new Random().nextInt());

    private static final int DEBUG_GEN_FOR_INSERTION_SEED = -130083375; // for testing: search, delete

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
     * @param treeSize                initial amount of inserted elements to the tree before test is launched
     * @param deletionsCount          number of delete operations within single iteration. Elements to delete are either observed
     *                                duplicates or randomly chosen elements from k-d tree, in order to test as duplicates as
     *                                single-time occurring values.
     * @param observedDuplicatesCount count of generates keys which will be observed in the test and inserted duplicate
     *                                multiple times. Each key is randomly chosen using even distribution of probability
     * @param dupInsertProb           probability by which will be one of the observed duplicates from generated set inserted
     *                                into the k-d tree. Probability of choosing concrete key from set is evenly
     *                                distributed.
     * @param logLevel                scalability of details about actions logged on console. Number from set {0,1,..}, where higher
     *                                number means more information.
     */
    public boolean testDeleteFunctionality(int iterationsCount, int treeSize, int deletionsCount,
                                           int observedDuplicatesCount, double dupInsertProb, int logLevel, boolean debug) {
        if (treeSize < deletionsCount) {
            printError("TreeSize parameter must be greater or equal to deletionsCount");
            return false;
        }
        if (treeSize < observedDuplicatesCount) {
            printError("TreeSize parameter must be greater or equal to observedDuplicatesCount");
            return false;
        }

        boolean overallOk = true;
        int seedOfObserved = 0;
        int[] seeds = {0, 0};
        int decisionSeed = 0;

        for (int iteration = 1; iteration <= iterationsCount; iteration++) {
//        ---------------------------------------------
            if (logLevel >= 1)
                printIteration(iteration, "DELETE TEST");
//        -- 1. STEP: prepare data
            KDTree<Data2D, Data2D, Data2D> kdTree = new KDTree<>(4);
            ArrayList<ArrayList<Data2D>> lObserved = new ArrayList<>(observedDuplicatesCount);
//            seedOfObserved = generateObservedData(observedDuplicatesCount, lObserved, logLevel, debug);
            ArrayList<Data2D> lInserted = new ArrayList<>(treeSize);
            seeds = insertData(treeSize, kdTree, lObserved, lInserted, dupInsertProb, logLevel, debug);

            if (logLevel >= 3)
                printIterationOperation("SORTING lists for comparison...");
            ArrayList<Data2D> lRetrieved = new ArrayList<>(treeSize);
            getSortedTreeDataById(kdTree, lRetrieved);
            // IMPORTANT: by now, lInserted and lRetrieved are sorted ascending by ID and therefore must be equal lists
            if (logLevel >= 3) {
                printHeader("SORTED lists of inserted elements and retrieved elements from k-d tree:");
                printListElementsComparison(lInserted, lRetrieved);
            }
//        -- 2. STEP: remember how many elements were there before deletion
            final int originalSize = lRetrieved.size();
            if (treeSize != originalSize)
                throw new RuntimeException("K-d tree does not contain required number of elements! " +
                        "RequiredSize=" + treeSize + " | RealSize=" + originalSize);
//        -- 3. STEP: delete element
            ArrayList<Data2D> lDeleted = new ArrayList<>(deletionsCount);
            Random decisionGen = new Random();

            if (!debug)
//                decisionSeed = decisionGen.nextInt(); // TODO uncomm
                decisionSeed = genR.nextInt(); // TODO uncomm
            else
                decisionSeed = 1697875354;   // TODO rmv
            decisionGen.setSeed(decisionSeed);

            if (logLevel >= 3) {
                printIterationOperation("DELETING " + deletionsCount + " elements from K-d tree... " +
                        "[DECISION_SEED=" + decisionSeed + "]");
                printHeader("DELETED elements:");
            }
//            if (logLevel >= 3)
//                kdTree.printTree();
//            printInfo("DELETE test [GEN_SEED="+seedOfObserved+ "; VALUE_SEED="
//                    +seeds[VAL_SEED_IDX]+"; PROB_SEED="+seeds[PROB_SEED_IDX]+"; DECISION_SEED="+decisionSeed+"]");
            for (int i = 0; i < deletionsCount; i++) {
//                if (logLevel >= 3)
//                    System.out.println("{"+i+"} BEGIN count > "+ kdTree.size());
//        --- 3.1 STEP: find duplicate element in lRetrieved or randomly get some. Then remember index of element to del
//        --- 3.2 STEP: set null at index of deleted element
                Data2D keyToDelete = pickElement(lInserted, dupInsertProb, decisionGen, lObserved); // steps 3.1 & 3.2
//        --- 3.3 STEP: delete element from the tree
                Data2D res = kdTree.delete(keyToDelete, keyToDelete);
                if (res == null) {
                    throw new RuntimeException("NULLPIONTr. [GEN_SEED=" + seedOfObserved + "; VALUE_SEED=" +
                            seeds[VAL_SEED_IDX] + "; PROB_SEED=" + seeds[PROB_SEED_IDX] + "; DECISION_SEED=" + decisionSeed + "]");
                }
                lDeleted.add(res);
//                if (logLevel >= 3) {
//                    printListItem(i + 1, "deleted key=" + keyToDelete);
//                    System.out.println("{" + i + "}END count > " + kdTree.size());
//                    kdTree.printTree();
//                }
            }
//        -- 4. STEP: evaluation
            boolean ok = true;
//        --- 4.1 STEP: check size of tree, if new size is less only by amount of deleted elements
            int sizeAfterDeletions = kdTree.size();
            ok = (sizeAfterDeletions == originalSize - deletionsCount);
            if (!ok) {
                if (logLevel >= 2)
                    printError("Size of the k-d tree after " + deletionsCount + " deletions is different than " +
                            "expected. " + "Expected=" + (originalSize - deletionsCount) + ", Actual=" + sizeAfterDeletions);
            } else {
                if (logLevel >= 2) {
                    printInfo("Ok.. Size of the k-d tree after " + deletionsCount + " deletions is as expected.");
                    printIterationOperation("CHECKING IF DELETED NODES ARE NOT PRESENT IN TREE...");
                }
//            --- 4.2 STEP: test whether deleted data does not exist in k-d tree
                if (logLevel >= 3)
                    printHeader("Testing SEARCH of deleted elements - RESULTS:");
                for (int i = 0; i < lDeleted.size(); i++) {
                    boolean present = false;
                    List<Data2D> lFoundDuplicates = kdTree.findAll(lDeleted.get(i));
                    if (lFoundDuplicates != null) {
                        for (Data2D d : lFoundDuplicates) {
                            if (lDeleted.get(i).isSame(d)) {
                                present = true;
                            }
                        }
                    }
                    if (logLevel >= 3) {
                        if (present)
                            printError("Deleted element " + lDeleted.get(i) + " found in the tree after deletion");
                        else
                            printListItem(i + 1, "Ok.. Element " + lDeleted.get(i) + " is NOT PRESENT in" +
                                    " the tree.");
                    }
                    ok = ok && !present;
                }
//        --- 4.3 STEP: get new list of sorted k-d tree elements by their ID and compare it with lInserted list.
                if (ok) {
                    if (logLevel >= 2)
                        printIterationOperation("COMPARING EXISTENCE OF REMAINING (NOT DELETED) VALUES...");

                    ArrayList<Data2D> lRemaining = new ArrayList<>(sizeAfterDeletions);
                    for (int i = 0; i < lInserted.size(); i++) {    // preparation of lRemaining list
                        if (lInserted.get(i) != null)
                            lRemaining.add(lInserted.get(i));
                    }
                    lRetrieved.clear();
                    getSortedTreeDataById(kdTree, lRetrieved);

                    if (lRetrieved.size() == lRetrieved.size()) {
                        if (logLevel >= 3)
                            printHeader("RESULTS of comparing:");
                        boolean same = true;
                        for (int i = 0; i < lRetrieved.size(); i++) {
                            same = lRemaining.get(i).isSame(lRetrieved.get(i));
                            if (logLevel >= 3)
                                printListItem(i + 1, "test: SAME? " + (same ? "YES" : "NO!")
                                        + "  <<  " + lRetrieved.get(i) + "     VS      " + lRemaining.get(i));
                            ok = ok && same;
                        }
                        if (ok) { // last check. Test whether all not deleted observed duplicates are able to find
                            if (logLevel >= 2) {
                                printInfo("Ok.. All remaining elements exist in k-d tree.");
                                printIterationOperation("TESTING reachability of observed duplicate keys" +
                                        " after delete operations(if they aren't stuck on a wrong place)");
                            }
//            -- 5. STEP: check if all duplicates were able to find. If not, it is sign, that they are not on the place
//                        where they should be. They are not searchable.
//                            ---- Update list lObserved - some of the duplicate keys might have been deleted by random
//                                  picking, not from this dedicated list lObserved
                            ArrayList<Data2D> lRemainingDuplicates = new ArrayList<>();
                            this.updateRemainingDuplicates(lInserted, lObserved, lRemainingDuplicates);
                            ok = testReachabilityOfRemainingDuplicates(kdTree, lRemainingDuplicates, logLevel);
                            if (logLevel >= 2) {
                                if (ok)
                                    printInfo("All remaining duplicate keys were SUCCESSFULLY found in k-d tree.");
                                else
                                    printError("Not all remaining duplicate keys were found in k-d tree.");
                            }
                        }

                    } else {
                        ok = false;
                        if (logLevel >= 2)
                            printError("Amount of remaining data after insertion and retrieved data from tree are "
                                    + "different");
                    }
                }
            }
            if (logLevel >= 1)
                printIterationResult(ok, iteration, "DELETE test [GEN_SEED=" + seedOfObserved + "; VALUE_SEED="
                        + seeds[VAL_SEED_IDX] + "; PROB_SEED=" + seeds[PROB_SEED_IDX] + "; DECISION_SEED=" + decisionSeed + "]");
            overallOk = overallOk && ok;
            lDeleted.clear();
            lInserted.clear();
            lRetrieved.clear();
            lObserved.clear();
//        ---------------------------------------------
        }
        if (!overallOk) // temp added
            printOverallResult(overallOk, "DELETE test");
        return overallOk;
    }

    /**
     * @param kdTree               instance, from which remaining duplicate keys will be searched for
     * @param lRemainingDuplicates duplicate keys that have remained after deletion from k-d tree
     * @param logLevel             2 for detailed info about errors
     * @return false if at least one duplicate key is not reachable - could not be found in k-d tree. Else true.
     */
    private boolean testReachabilityOfRemainingDuplicates(KDTree<Data2D, Data2D, Data2D> kdTree,
                                                          ArrayList<Data2D> lRemainingDuplicates, int logLevel) {
        boolean allOk = true;
        if (lRemainingDuplicates.isEmpty())
            return true;
        else {
            for (Data2D dup : lRemainingDuplicates) {
                List<Data2D> lFound = kdTree.findAll(dup);
                if (lFound == null) {
                    if (logLevel >= 3) {
                        printError("Could not find any element with duplicate key in k-d tree for value=" + dup);
                    }
                    return false;
                }
                boolean found = false;
                for (Data2D f : lFound) {
                    if (dup.isSame(f)) {
                        found = true;
                        break;
                    }
                }
                allOk = allOk && found;
                if (!found && logLevel >= 3)
                    printError("Remaining duplicate key=" + dup + " couldn't be found in k-d tree");
            }
        }
        return allOk;
    }

    /**
     * Tests whether all inserted elements into the k-d tree are present in the structure. Every generated data instance
     * is inserted only once into the structure, so the test is to check if number of insertions is equal to number of
     * found elements in the tree using some type of order going through structure (here is used in-order node visiting)
     *
     * @param insertionsCount how many instances of data will be generated and inserted into the k-d tree
     * @param iterationsCount how many times will search test be executed
     */
    public void testInsertFunctionality(int insertionsCount, int iterationsCount, int logLevel) {
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
        printOverallResult(overallOk, "INSERTION TEST");
    }

    /**
     * Tests whether k-d tree is able to find all elements in its structure with same key value.
     *
     * @param iterationsCount         how many times will search test be executed
     * @param observedDuplicatesCount number of generated keys, that could be inserted multiple times to the tree and
     *                                recorded in other structure with its all insertions in order to compare result
     *                                from k-d tree search. Each key is randomly chosen from generated set using even
     *                                distribution of probability.
     * @param insertionCount          total number of elements in k-d tree data structure
     * @param duplicateInsertProb     probability, by which will be some(chosen with even distribution of probability)
     *                                duplicate key from observed set of duplicates inserted to the k-d tree.
     * @param logLevel                level of description printed on the console. Range <0;2>, where 0 is no information, 1 are main
     *                                actions and 2 are specific steps of actions and 3 are all insertions to the structure and search
     *                                results
     */
    public void testSearchFunctionality(int iterationsCount, int observedDuplicatesCount, int insertionCount,
                                        double duplicateInsertProb, int logLevel, boolean debug) {
        boolean overallOk = true;

        for (int iteration = 1; iteration <= iterationsCount; iteration++) {
            if (logLevel >= 1)
                printIteration(iteration, "SEARCH TEST");

            KDTree<Data2D, Data2D, Data2D> kdTree = new KDTree<>(4);
            ArrayList<ArrayList<Data2D>> lObserved = new ArrayList<>(observedDuplicatesCount);

//            int seedOfObserved = generateObservedData(observedDuplicatesCount, lObserved, logLevel, debug);
            int[] seeds = insertData(insertionCount, kdTree, lObserved, null, duplicateInsertProb, logLevel,
                    debug);
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

    /**
     * @param idx         offset in list lDuplicates
     * @param logLevel    level of details printed on console
     * @param lDuplicates prepared duplicate keys used to be searched for in k-d tree
     * @param kdTree      instance on which will be search executed
     * @return true - if the amount of found elements in k-d tree is the same as the amount of elements in lDuplicates
     * on the given index and if the unique ids of elements in both structures (for found elements and for given index
     * in lDuplicates) are the same. Else false.
     */
    private boolean searchForDuplicate(int idx, int logLevel, ArrayList<ArrayList<Data2D>> lDuplicates,
                                       KDTree<Data2D, Data2D, Data2D> kdTree) {
        if (logLevel >= 3) {
            //        PRINT REMEMBER DUPLICATES FOR GIVEN INDEX
            printHeader("CREATED DUPLICATES:");
            for (int i = 0; i < lDuplicates.get(idx).size(); i++) {
                printListItem(i + 1, lDuplicates.get(idx).get(i).toString());
            }
        }

        List<Data2D> lFound = kdTree.findAll(lDuplicates.get(idx).get(0));    // take one from the duplicates
        if (lFound == null)
            return false;
        lFound.sort(new Data2D.Data2DComparator());

        if (logLevel >= 3) {
//        FIND ALL DUPLICATES WITH THE KEY OF ELEMENT ON THE INDEX IN THE K-D TREE
            printHeader("FOUND DUPLICATES IN THE K-D TREE:");
            for (int i = 0; i < lFound.size(); i++) {
                printListItem(i + 1, lFound.get(i).toString());
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
     *
     * @param dataCount                     quantity of elements, that will be inserted into the tree
     * @param kdTree                        instance where data will be inserted
     * @param lObserved                     final list of keys, from which will duplicates be generated
     * @param lInserted                     if not null, inserts all elements that have been inserted into the k-d tree. Element are sorted
     *                                      ascending by unique assigned id.
     * @param duplicateInsertionProbability probability of inserting duplicate instead of creating random key
     * @param logLevel                      level of details printed on console {0,1,2,3}. Higher number for more information.
     * @return seeds {valueSeed, probabilityOfDuplicatesSeed} used for building k-d tree
     */
    private int[] insertData(int dataCount, KDTree<Data2D, Data2D, Data2D> kdTree, ArrayList<ArrayList<Data2D>>
            lObserved, ArrayList<Data2D> lInserted,
                             double duplicateInsertionProbability, int logLevel, boolean debug) {
        Random seedGenerator = new Random();
        int seedForValGen;
        if (!debug)
//            seedForValGen = seedGenerator.nextInt();  // TODO uncomm
            seedForValGen = genR.nextInt();  // TODO uncomm
        else
            seedForValGen = 1296616592;    // TODO rmv
        Random valGen = new Random(seedForValGen);

        int seedForProb;
        if (!debug)
//            seedForProb = seedGenerator.nextInt();// TODO uncomm
            seedForProb = genR.nextInt();// TODO uncomm
        else
            seedForProb = -1200112902; // TODO rmv
        Random probGenerator = new Random(seedForProb);

        if (logLevel >= 1)
            printIterationOperation("GENERATING " + dataCount + " VALUES... VALUE_SEED=" + seedForValGen +
                    "; PROBABILITY_SEED=" + seedForProb);

        for (ArrayList<Data2D> keyDuplicates : lObserved) {
            if (logLevel >= 3)
                System.out.println("    => inserting NEW element " + keyDuplicates.get(0) + "...");
            kdTree.insert(keyDuplicates.get(0), keyDuplicates.get(0)); // original duplicates haven't been inserted yet
            if (lInserted != null) {
                lInserted.add(keyDuplicates.get(0));
            }
        }
        if (lInserted != null)
            lInserted.sort(new Data2D.Data2DComparator()); // must be sorted by unique id
        Data2D nextData;
        dataCount = dataCount - lObserved.size();   /* because I need to insert duplicates for the first time and don't
                                                       want to affect number of inserted elements*/
        for (int i = 0; i < dataCount; i++) {
            if (probGenerator.nextDouble() < duplicateInsertionProbability) {   // duplicate element
                int nextIdx = probGenerator.nextInt(lObserved.size());
                nextData = new Data2D(lObserved.get(nextIdx).get(0), IdGenerator.getInstance().nextId());
                lObserved.get(nextIdx).add(nextData);   // remember newly create duplicate
                if (logLevel >= 3)
                    System.out.println("    => inserting DUPLICATE element " + nextData + "...");
            } else {  // new element
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
     *
     * @param observedCount number of elements inserted to the list 'lObserved'
     * @param lObserved     structure holding generated data
     * @param logLvl        {4}
     * @return generated seed, by which were data generated
     */
    private int generateObservedData(int observedCount, ArrayList<Data2D> lObserved, int logLvl, boolean
            debug) {
        int seedForValGen;
        if (!debug)
//            seedForValGen = new Random().nextInt(); // TODO by this
            seedForValGen = genR.nextInt(); // TODO by this
        else
            seedForValGen = DEBUG_GEN_FOR_INSERTION_SEED;   // TODO rmv
//        Random valGen = new Random();
        Random valGen = new Random(seedForValGen);
        if (logLvl >= 4)
            printIterationOperation("GENERATING " + observedCount + " observed data for duplicates " +
                    "[GEN_FOR_INSERTION_SEED=" + seedForValGen + "]");
        lObserved.clear();
        for (int i = 0; i < observedCount; i++) {
            Data2D nextData = generateDataInstance(valGen);
            if (logLvl >= 4)
                printListItem(i + 1, "new data: " + nextData);
            lObserved.add(nextData);
        }
        return seedForValGen;
    }

    private int generateObservedDataOld(int observedCount, ArrayList<ArrayList<Data2D>> lObserved, int logLvl, boolean
            debug) {
        int seedForValGen;
        if (!debug)
//            seedForValGen = new Random().nextInt(); // TODO by this
            seedForValGen = genR.nextInt(); // TODO by this
        else
            seedForValGen = DEBUG_GEN_FOR_INSERTION_SEED;   // TODO rmv
//        Random valGen = new Random();
        Random valGen = new Random(seedForValGen);
        if (logLvl >= 4)
            printIterationOperation("GENERATING " + observedCount + " observed data for duplicates " +
                    "[GEN_FOR_INSERTION_SEED=" + seedForValGen + "]");
        for (int i = 0; i < observedCount; i++) {
            Data2D nextData = generateDataInstance(valGen);
            if (logLvl >= 3)
                printListItem(i + 1, "new data: " + nextData);
            ArrayList<Data2D> list = new ArrayList<>();
            list.add(nextData);
            lObserved.add(list);
        }
        return seedForValGen;
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
                (1+(Math.abs(valGen.nextInt()) % 3)), // 1-3
                (1 +(Math.abs(valGen.nextInt() % 3))) // 1-3
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

    //    / / / / / / / / / / / / / / / / > M  A  I  N <  / / / / / / / / / / / / / / / / / / / / / / / /
    public static void main(String[] args) {
        OperationsTesterOld ot = new OperationsTesterOld();
//        -----------------------------------------------------
        int dupCount = 15;
        ArrayList<Data2D> lObserved = new ArrayList<>(dupCount);
        System.out.println("SEED: " + ot.generateObservedData(dupCount, lObserved, 4, true));
//        int best_i = 50000;
//        int best_d = 1;
//        double best_p =0.09;
//        boolean debug = false;
//        if (!debug) {
//            boolean allOk = true;
////        for (int i = 5; i <= 10; i++) {
//            for (int i = 10; i <= 12; i++) {
//                for (int d = 1; d < 10; d++) {
//                    for (double p = 0.09; p < 1.0; p += 0.1) {
//                        ot.testSearchFunctionality(1, (i/d) ,i, p, 1, false); // --ok
////                        boolean oki = ot.testDeleteFunctionality(1, i, i / d, i / 3, p, 0, true); // found
////                        if (!oki)
////                            System.out.println("for params: i=" + i + ", d=" + d + ", i/d= " + (i / d) + ", p=" + p + " ---v");
////                    allOk = allOk && oki;
//                    }
//                }
//            }
//        } else {
////        System.out.println("i="+best_i+", d="+best_d+", i/d= "+(best_i/best_d)+", p="+best_p);
//            ot.testDeleteFunctionality(1, best_i, best_i / best_d, best_i / 3, best_p, 3, true);
//        }
//    }
    }
}
