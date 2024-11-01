package mpoljak.dataStructures.searchTrees.KdTree.Testing;

import mpoljak.data.forTesting.Data4D;
import mpoljak.dataStructures.searchTrees.KdTree.KDTree;
import mpoljak.utilities.IdGenerator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OperationsGenerator {
    private static final String MY_CHAR_SET = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int VAL_SEED_IDX = 0;  // standardised index (within seed array) of seed for generating data
    private static final int PROB_SEED_IDX = 1; /* standardised index (within seed array) of seed for generating
                                                   probability. */

    /**
     * Generates string of given length by using generator
     * @param length number of positions in generated string
     * @param generator generator of integer values with some seed
     * @return generated string
     */
    private static String nextString(int length, Random generator) {
        StringBuilder sb = new StringBuilder(length);
        int len = MY_CHAR_SET.length();
        for (int i = 0; i < length; i++) {
            sb.append(MY_CHAR_SET.charAt(generator.nextInt(len)));
        }
        return sb.toString();
    }

    public void doAlg() {
        int suspectedCount = 10;
        int dataCount = 100;
        double duplicateProb = 0.15;
        KDTree<Data4D, Data4D, Data4D> kdTree = new KDTree<>(4);
        ArrayList< ArrayList<Data4D> > lSuspected = new ArrayList<>(suspectedCount);
        int logLevel = 3;
        int seedOfSuspected = generateSuspectedData(suspectedCount, lSuspected, logLevel);
        int[] seeds = insertData(dataCount, kdTree, lSuspected, duplicateProb, logLevel);
    }


    /**
     * Tests whether k-d tree is able to find all elements in its structure with same key value.
     * @param observedDuplicatesCount number of generated keys, that will be multiple times inserted to the tree and
     *                                recorded in other structure with its all insertions in order to compare result
     *                                from k-d tree search
     * @param insertionCount total number of elements in k-d tree data structure
     * @param duplicateInsertProb probability, by which will be some duplicate key from observed set of duplicates
     *                            inserted to the k-d tree.
     * @param logLevel level of description printed on the console. Range <0;2>, where 0 is no information, 1 are main
     *                 actions and 2 are specific steps of actions and 3 are all insertions to the structure and search
     *                 results
     */
    public void testSearchFunctionality(int observedDuplicatesCount, int insertionCount, double duplicateInsertProb,
                                        int logLevel) {

        KDTree<Data4D, Data4D, Data4D> kdTree = new KDTree<>(4);
        ArrayList< ArrayList<Data4D> > lSuspected = new ArrayList<>(observedDuplicatesCount);

        int seedOfSuspected = generateSuspectedData(observedDuplicatesCount, lSuspected, logLevel);
        int[] seeds = insertData(insertionCount, kdTree, lSuspected, duplicateInsertProb, logLevel);

        boolean ok = true;
        for (int i = 0; i < lSuspected.size(); i++) {
            ok = ok && searchForDuplicate(i, logLevel, lSuspected, kdTree);
        }
        if (logLevel >= 1)
            System.out.println("\n      |_---> SEARCH test for duplicate keys " + (ok ? "SUCCESSFUL" : "FAILED") +
                    " <---_|");
    }
//    ------------------------------------------ P R I V A T E ----------------------------------------------------
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
            System.out.println("\n  * CREATED DUPLICATES: ");
            for (int i = 0; i < lDuplicates.get(idx).size(); i++) {
                System.out.println( (i + 1) + ". " + lDuplicates.get(idx).get(i));
            }
        }

        List<Data4D> lFound = kdTree.findAll(lDuplicates.get(idx).get(0));    // take one from the duplicates
        if (lFound == null)
            return false;
        lFound.sort(new Data4D.Data4DComparator());

        if (logLevel >= 3) {
//        FIND ALL DUPLICATES WITH THE KEY OF ELEMENT ON THE INDEX IN THE K-D TREE
            System.out.println("  * FOUND DUPLICATES IN THE K-D TREE: ");
            for (int i = 0; i < lFound.size(); i++) {
                System.out.println((i + 1) + ". " + lFound.get(i));
            }
        }
        if (lDuplicates.get(idx).size() != lFound.size()) {
            if (logLevel >= 2)
                System.out.println(" ! ERROR: Different count of created and found duplicates!");
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
                System.out.println(" (i) SUCCESS!       -   created and found duplicates are matching.");
            else
                System.out.println(" (i) TEST FAILED!   -   found duplicates are not equal to created.");
        }
        return allEqual;
    }

    /**
     * Inserts generated data into the k-d tree instance. With probability given through duplicateInsertionProbability
     * parameter takes randomly element of list of before created duplicates and inserts it instead of creating new data
     * @param dataCount quantity of elements, that will be inserted into the tree
     * @param kdTree instance where data will be inserted
     * @param lSuspected final list of keys, from which will duplicates be generated
     * @param duplicateInsertionProbability probability of inserting duplicate instead of creating random key
     * @param logLevel level of details printed on console
     * @return seeds {valueSeed, probabilityOfDuplicatesSeed} used for building k-d tree
     */
    private int[] insertData(int dataCount, KDTree<Data4D, Data4D, Data4D> kdTree, ArrayList<ArrayList<Data4D>>
                                                    lSuspected, double duplicateInsertionProbability, int logLevel) {
        Random seedGenerator = new Random();
        int seedForValGen = seedGenerator.nextInt();
        Random valGen = new Random(seedForValGen);
        int seedForProb = seedGenerator.nextInt();
        Random probGenerator = new Random(seedForProb);

        if (logLevel >= 1)
            System.out.println("\n * * * GENERATING " + dataCount + " VALUES... VALUE_SEED=" + seedForValGen +
                    "; PROBABILITY_SEED=" + seedForProb + " <<<\n");
        Data4D nextData;

        for (int i = 0; i < dataCount; i++) {
            if (probGenerator.nextDouble() < duplicateInsertionProbability) {   // duplicate element
                int nextIdx = probGenerator.nextInt(lSuspected.size());
                nextData = new Data4D(lSuspected.get(nextIdx).get(0), IdGenerator.getInstance().nextId());
                lSuspected.get(nextIdx).add(nextData);
                if (logLevel >= 3)
                    System.out.println("    => inserting DUPLICATE element " + nextData + "...");
            }
            else {  // new element
                nextData = new Data4D(valGen.nextDouble(), nextString(10, valGen),
                        valGen.nextInt(), valGen.nextInt(), IdGenerator.getInstance().nextId());
                if (logLevel >= 3)
                    System.out.println("    -> inserting NEW element " + nextData + "...");
            }
            kdTree.insert(nextData, nextData);
        }
        for (ArrayList<Data4D> keyDuplicates: lSuspected)
            kdTree.insert(keyDuplicates.get(0), keyDuplicates.get(0)); // original duplicates haven't been inserted yet

        int[] seeds = new int[2];
        seeds[VAL_SEED_IDX] = seedForValGen;
        seeds[PROB_SEED_IDX] = seedForProb;
        return seeds;
    }

    /**
     *
     * @param suspectedCount number of elements inserted to the list 'lSuspected'
     * @param lSuspected structure holding generated data
     * @return generated seed, by which were data generated
     */
    private static int generateSuspectedData(int suspectedCount, ArrayList< ArrayList<Data4D> > lSuspected, int logLvl){
        int seedForValGen = new Random().nextInt();
//        Random valGen = new Random();
        Random valGen = new Random(seedForValGen);
        if (logLvl >= 1)
            System.out.println("\n * * * GENERATING "+ suspectedCount + " suspected data for duplicates:");
        for (int i = 0; i < suspectedCount; i++) {
            Data4D nextData = new Data4D(valGen.nextDouble(), nextString(10, valGen),
                    valGen.nextInt(), valGen.nextInt(), IdGenerator.getInstance().nextId());
            if (logLvl >= 2)
                System.out.println( (i + 1) + ". new data: " + nextData);
            ArrayList<Data4D> list = new ArrayList<>();
            list.add(nextData);
            lSuspected.add(list);
        }
        return seedForValGen;
    }



}
