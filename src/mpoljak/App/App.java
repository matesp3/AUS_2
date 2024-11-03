package mpoljak.App;

import mpoljak.dataStructures.searchTrees.KdTree.Testing.OperationsTester;
import mpoljak.dataStructures.searchTrees.KdTree.Testing.MiniTester;
import mpoljak.utilities.BisectionSearch;
import mpoljak.utilities.DoubleComparator;

import java.util.ArrayList;
import java.util.Comparator;

public class App {

    public static void main(String[] args) {
        OperationsTester ot = new OperationsTester();
//        ot.testSearchFunctionality(10, 10,100, 0.25, 3); // --ok
//        ot.testInsertFunctionality(200000, 200,1);   // --ok
//        ot.testDeleteFunctionality(1, 1000, 30, 10,0.22, 2);

//        ot.testDeleteFunctionality(1, 25, 10, 2,0.22, 3); // todo uncomment
//        ot.testDeleteFunctionality(1000, 15, 5, 7,0.22, 1, false); // found
        for (int i = 20; i < 10000000; i*=5) {
            ot.testDeleteFunctionality(100, i, i/4, i/5,0.50, 0, false); // found
        }

    }


}
