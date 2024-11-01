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

    public void doAlg() {
        int suspectedCount = 10;
        int dataCount = 10_000;
        double duplicateProb = 0.35;
        KDTree<Data4D, Data4D, Data4D> kdTree = new KDTree<>(4);
        ArrayList< ArrayList<Data4D> > lSuspected = new ArrayList<>(suspectedCount);
        int seedOfSuspected = generateSuspectedData(suspectedCount, lSuspected);
        ArrayList<Integer> lQuantities = new ArrayList<>(suspectedCount);
        for (int i = 0; i < suspectedCount; i++)
            lQuantities.add(1);
        int[] seeds = insertData(dataCount, kdTree, lSuspected, duplicateProb, true);
//        kdTree.printTree();
//        ------------------------------ data inserted
        testSearchFunctionality( lSuspected, kdTree);
    }

    private void testSearchFunctionality(ArrayList< ArrayList<Data4D> > lSuspected,
                                         KDTree<Data4D, Data4D, Data4D> kdTree) {
        boolean ok = true;
        for (int i = 0; i < lSuspected.size(); i++) {
            System.out.println();
            ok = ok && searchForDuplicate(i, true, lSuspected, kdTree);
        }
        System.out.println("\n      |_---> SEARCH test " + (ok ? "SUCCESSFUL" : "FAILED") + " <---_|");
    }

    private boolean searchForDuplicate(int idx, boolean logDetails, ArrayList< ArrayList<Data4D> > lDuplicates,
                                       KDTree<Data4D, Data4D, Data4D> kdTree) {
        if (logDetails) {
//        PRINT REMEMBER DUPLICATES FOR GIVEN INDEX
        System.out.println("  * CREATED DUPLICATES: ");
        for (int i = 0; i < lDuplicates.get(idx).size(); i++) {
            System.out.println( (i + 1) + ". " + lDuplicates.get(idx).get(i));
        }
        }

        List<Data4D> lFound = kdTree.findAll(lDuplicates.get(idx).get(0));    // take one from the duplicates
        if (lFound == null)
            return false;
        lFound.sort(new Data4D.Data4DComparator());

        if (logDetails) {
//        FIND ALL DUPLICATES WITH THE KEY OF ELEMENT ON THE INDEX IN THE K-D TREE
            System.out.println("  * FOUND DUPLICATES IN THE K-D TREE: ");
            for (int i = 0; i < lFound.size(); i++) {
                System.out.println((i + 1) + ". " + lFound.get(i));
            }
        }
        if (lDuplicates.get(idx).size() != lFound.size()) {
            if (logDetails)
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
        if (logDetails) {
            if (allEqual)
                System.out.println(" (i) SUCCESS!       -   created and found duplicates are matching.");
            else
                System.out.println(" (i) TEST FAILED!   -   found duplicates are not equal to created.");
        }
        return allEqual;
    }

    private int[] insertData(int dataCount, KDTree<Data4D, Data4D, Data4D> kdTree, ArrayList<ArrayList<Data4D>>
                                                    lSuspected, double duplicateInsertionProbability, boolean log) {
        Random seedGenerator = new Random();
        int seedForValGen = seedGenerator.nextInt();
        Random valGen = new Random(seedForValGen);
        int seedForProb = seedGenerator.nextInt();
        Random probGenerator = new Random(seedForProb);

        if (log)
            System.out.println("       >>> GENERATING " + dataCount + " VALUES... VALUE_SEED=" + seedForValGen +
                    "; PROBABILITY_SEED=" + seedForProb + " <<<\n");
        Data4D nextData;

        for (int i = 0; i < dataCount; i++) {
            if (probGenerator.nextDouble() < duplicateInsertionProbability) {   // duplicate element
                int nextIdx = probGenerator.nextInt(lSuspected.size());
                nextData = new Data4D(lSuspected.get(nextIdx).get(0), IdGenerator.getInstance().nextId());
                lSuspected.get(nextIdx).add(nextData);
                if (log)
                    System.out.println("            => inserting DUPLICATE element " + nextData + "...");
            }
            else {  // new element
                nextData = new Data4D(valGen.nextDouble(), nextString(10, valGen),
                        valGen.nextInt(), valGen.nextInt(), IdGenerator.getInstance().nextId());
                if (log)
                    System.out.println("            -> inserting NEW element " + nextData + "...");
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
    private static int generateSuspectedData(int suspectedCount, ArrayList< ArrayList<Data4D> > lSuspected) {
        int seedForValGen = new Random().nextInt();
        Random valGen = new Random(seedForValGen);

        for (int i = 0; i < suspectedCount; i++) {
            Data4D nextData = new Data4D(valGen.nextDouble(), nextString(10, valGen),
                    valGen.nextInt(), valGen.nextInt(), IdGenerator.getInstance().nextId());
            ArrayList<Data4D> list = new ArrayList<>();
            list.add(nextData);
            lSuspected.add(list);
        }
        return seedForValGen;
    }

    public static String nextString(int length, Random generator) {
        StringBuilder sb = new StringBuilder(length);
        int len = MY_CHAR_SET.length();
        for (int i = 0; i < length; i++) {
            sb.append(MY_CHAR_SET.charAt(generator.nextInt(len)));
        }
        return sb.toString();
    }

}
