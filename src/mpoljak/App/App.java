package mpoljak.App;

import mpoljak.data.GPS;
import mpoljak.data.forTesting.MyCoupleInt;
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
        tester.testVillages();
//        tester.printMaxForRoot();
//        tester.testSearchOfGPS(100, 10, 25);
//        tester.debugSearchOfGPS(100,10, 25, 1188562444, -884246284, 1129907258);
//        tester.testManualInsertion();
//        tester.testRandomInsert(2,2);
//--------------------------------------------------------------
    }
}
