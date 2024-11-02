package mpoljak.App;

import mpoljak.dataStructures.searchTrees.KdTree.Testing.OperationsTester;
import mpoljak.dataStructures.searchTrees.KdTree.Testing.MiniTester;
import mpoljak.utilities.DoubleComparator;

public class App {

    public static void main(String[] args) {
        OperationsTester ot = new OperationsTester();
//        ot.testSearchFunctionality(10, 10,100, 0.25, 3); // --ok
        ot.testInsertFunctionality(200000, 200,1);   // --ok
    }
}
