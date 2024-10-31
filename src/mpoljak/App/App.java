package mpoljak.App;

import mpoljak.data.GPS;
import mpoljak.data.forTesting.Data4D;
import mpoljak.data.forTesting.MyCoupleInt;
import mpoljak.dataStructures.searchTrees.KdTree.KDTree;
import mpoljak.dataStructures.searchTrees.KdTree.Testing.Tester;
import mpoljak.utilities.DoubleComparator;

public class App {

    public static void main(String[] args) {
        DoubleComparator.getInstance().setEpsilon(0.00001);
        Tester tester = new Tester();
//        tester.testFindingExtremes(1000000, -1, -1, -1);
//        tester.testFindingExtremes(1000000, 570413914, -151831802, -165047230);
//        tester.testFindingExtremes(1_000_000, -1, -1, -1);
//        tester.testFindingExtremes(10000, -671960820, 1489149674, 1031476793);
//        tester.testVillages();
//        tester.printMaxForRoot();
//        tester.testSearchOfGPS(100, 10, 25);
//        tester.debugSearchOfGPS(100,10, 25, 1188562444, -884246284, 1129907258);
//        tester.testManualInsertion();
//        tester.testRandomInsert(2,2);
//--------------------------------------------------------------


        Data4D d1 = new Data4D(5.2, "v", 5, 5.4);
        Data4D d2 = new Data4D(8.6, "avdca", -5, 55.4);
        Data4D d3 = new Data4D(-5.2, "sdvasa", 5, -0.4);
        Data4D d4 = new Data4D(16.2, "csadb", -24, 23.4);
        Data4D d5 = new Data4D(7.2, "cryw", 5, -5.4);
        Data4D d6 = new Data4D(-5.2, "cymuy", 18, 31.4);
        Data4D d7 = new Data4D(5.2, "kc fsa", -5, 5.4);
//        System.out.println(d1.compareTo(d2,1));
//        System.out.println(d1.compareTo(d2,2));
//        System.out.println(d1.compareTo(d2,3));
//        System.out.println(d1.compareTo(d2,4));


        KDTree<Data4D, Data4D, Data4D> kdTree = new KDTree<Data4D, Data4D, Data4D>(4);

        kdTree.insert(d1,d1);
        kdTree.insert(d2,d2);
        kdTree.insert(d3,d3);
        kdTree.insert(d4,d4);
        kdTree.insert(d5,d5);
        kdTree.insert(d6,d6);
        kdTree.insert(d7,d7);

        kdTree.printTree();
    }
}
