package mpoljak.App;

import mpoljak.dataStructures.searchTrees.KdTree.Testing.OperationsTester;
import mpoljak.dataStructures.searchTrees.KdTree.Testing.MiniTester;
import mpoljak.utilities.DoubleComparator;

import java.util.ArrayList;
import java.util.Comparator;

public class App {

    public static void main(String[] args) {
        OperationsTester ot = new OperationsTester();
//        ot.testSearchFunctionality(10, 10,100, 0.25, 3); // --ok
//        ot.testInsertFunctionality(200000, 200,1);   // --ok
//        ot.testDeleteFunctionality(3, 10, 5, 3,0.22, 3);
//        int[] arr = new int[100_000_00];
//        for (int i = 0; i < arr.length; i++) {
//            arr[i] = i + 1;
//        }
//        int[] arr = {1,2,8,9,17,18,27,44};
//        getIdx(arr, 3);
//        boolean goOn = true;
//        int k;
//        for (k = 0; k < arr.length; k++) {
//            if (arr[k] != arr[getIdx(arr, arr[k])]) {
//                System.out.println("FAIL: element i=" + k + ", arr[i]=" + arr[k] + ", found idx=" + getIdx(arr, arr[k]));
//            }
//        }
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(1);  // 0
        arr.add(2);  // 1
        arr.add(null);  // 2
        arr.add(null);  // 3
        arr.add(17); // 4
        arr.add(null); // 5
        arr.add(null); // 6
        arr.add(42); // 7
        arr.add(44); // 8
//        System.out.println(getIdxD(arr, 44, Integer::compare));
    }

    private static int getIdx(int[] arr, int k) {
        int withoutChangeCount = 0;
        int i = -1;
        int a = 0;
        int b = arr.length;
        while (true) {
            i = (a+b) / 2;
            if (arr[i] == k)
                return i;
            else if (arr[i] > k)
                b = i;
            else
                a = i;
            if (b-a <= 1) {
                if (withoutChangeCount >= 1)
                    return -1;
                withoutChangeCount++;
            }
        }
    }
}
