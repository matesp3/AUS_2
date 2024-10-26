package mpoljak.App;

import mpoljak.data.GPS;
import mpoljak.dataStructures.searchTrees.KdTree.Testing.Tester;
import mpoljak.utilities.DoubleComparator;

public class App {

    public static void main(String[] args) {
        DoubleComparator.getInstance().setEpsilon(0.0001);
        Tester tester = new Tester();
        tester.printMaxForRoot();
//        tester.testSearchOfGPS(100, 10, 25);
//        tester.debugSearchOfGPS(100,10, 25, 1188562444, -884246284, 1129907258);
//        tester.testManualInsertion();
//        tester.testRandomInsert(2,2);
//--------------------------------------------------------------
    }
}
