package mpoljak.dataStructures.searchTrees.KdTree.Testing;

import mpoljak.data.forTesting.Data4D;
import mpoljak.dataStructures.searchTrees.KdTree.KDTree;

import java.util.Random;

public class OperationsGenerator {
    private static final String MY_CHAR_SET = "abcdefghijklmnopqrstuvwxyz0123456789";

    public void doAlg() {
        KDTree<Data4D, Data4D, Data4D> kdTree = new KDTree<>(4);
        generateData(10, kdTree);
        kdTree.printTree();
    }

    private void generateData(int dataCount, KDTree<Data4D, Data4D, Data4D> kdTree) {
        int seedForValGen = new Random().nextInt();
        Random valGen = new Random(seedForValGen);
        System.out.println("       >>> GENERATING VALUES... USED SEED=" + seedForValGen + " <<<");
        for (int i = 0; i < dataCount; i++) {
            Data4D nextData = new Data4D(valGen.nextDouble(), nextString(10, valGen),
                                         valGen.nextInt(), valGen.nextInt());
            kdTree.insert(nextData, nextData);
        }
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
