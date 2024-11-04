package mpoljak.App;

import mpoljak.dataStructures.searchTrees.KdTree.Testing.OperationsTesterOld;

public class App {

    public static void main(String[] args) {
        OperationsTesterOld ot = new OperationsTesterOld();
        ot.testSearchFunctionality(10, 10,100, 0.25, 1, false); // --ok
//        ot.testInsertFunctionality(200000, 200,1);   // --ok
//        ot.testDeleteFunctionality(1, 1000, 30, 10,0.22, 2);

//        ot.testDeleteFunctionality(1, 25, 10, 2,0.22, 3); // todo uncomment
//        ot.testDeleteFunctionality(1000, 15, 5, 7,0.22, 1, false); // found
        int best_i = 50000;
        int best_d = 1;
        double best_p =0.09;
        boolean debug = true;
        if (!debug) {
            boolean allOk = true;
//        for (int i = 5; i <= 10; i++) {
            for (int i = 10; i <= 1000000; i*=10) {
                for (int d = 1; d < 10; d++) {
                    for (double p = 0.09; p < 1.0; p += 0.1) {
//                        try {
                            boolean oki = ot.testDeleteFunctionality(1, i, i / d, i / 3, p, 0, true); // found
                            if (!oki)
                                System.out.println("for params: i=" + i + ", d=" + d + ", i/d= " + (i / d) + ", p=" + p + " ---v");
//                        } catch (RuntimeException r) {
////                    if  (!oki)
//                            System.out.println("for params: i=" + i + ", d=" + d + ", i/d= " + (i / d) + ", p=" + p + " ---v");
//                        }
//                    allOk = allOk && oki;

                    }
                }
//            ot.testDeleteFunctionality(100, i, i/4, i/5,0.50, 0, false); // found
            }
        } else {
//        System.out.println("\n\nbez failu: "+allOk);
//        System.out.println("i="+best_i+", d="+best_d+", i/d= "+(best_i/best_d)+", p="+best_p);
//            ot.testDeleteFunctionality(1, best_i, best_i / best_d, best_i / 3, best_p, 3, true);
//            ot.testDeleteFunctionality(1, best_i, best_i-1, best_i / 3, best_p, 3, true);
        }
    }


}
