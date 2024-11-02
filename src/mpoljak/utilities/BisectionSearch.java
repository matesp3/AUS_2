package mpoljak.utilities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class BisectionSearch {
    /**
     * Founds index of searched key in list data structure.
     * @param arr sorted array, that can contain null values. Elements must be UNIQUE!
     * @param k wanted key
     * @param cmp comparator for given type
     * @return index of key in list | -1 if key doesn't exist in structure
     * @param <D> type of data with unique id
     */
    public static <D> int getIdx(ArrayList<D> arr, D k, Comparator<D> cmp) {
        int withoutChangeCount = 0;
        int i = -1;
        int a = 0;
        int b = arr.size();
        while (true) {
            i = (a+b) / 2;
            if (arr.get(i) == null) {
                boolean tryRight = false;
                int temp = i;
                while (temp > a && arr.get(temp) == null) // try going left from 'i'
                    temp--;
                if (arr.get(temp) == null)
                    tryRight = true;
                else {
                    int cmpRes = cmp.compare(arr.get(temp), k);
                    if (cmpRes == 0)
                        return temp;
                    if (temp == a) {
                        if (cmpRes > 0) // could not be less 'a' index
                            return -1;
                        tryRight = true;
                    } else {    // temp > a
                        if (cmpRes > 0)
                            b = temp;
                        else
                            tryRight = true;
                    }
                }
                if (tryRight) { // nothing to the left. Try right side of 'i'
                    temp = i;
                    while (temp < b && arr.get(temp) == null) // try going left
                        temp++;
                    if (temp == arr.size())
                        return -1;
                    int cmpRes = cmp.compare(arr.get(temp), k);
                    if (cmpRes == 0)
                        return temp;
                    if (temp == b)
                        return -1;
                    else {
                        if (cmpRes > 0)
                            return -1;
                        else
                            a = temp;
                    }
                }
            } else {
                if (cmp.compare(arr.get(i), k) == 0)
                    return i;
                else if (cmp.compare(arr.get(i), k) > 0)
                    b = i;
                else
                    a = i;
                if (b - a <= 1) {
                    if (withoutChangeCount >= 1)
                        return -1;
                    withoutChangeCount++;
                }
            }
        }
    }

    public static void main(String[] args) {
        Random intGenerator = new Random();
        Random decisionGenerator = new Random();
//        int decisionSeed = 344328779;
        int decisionSeed = intGenerator.nextInt();
        decisionGenerator.setSeed(decisionSeed);
//        int valSeed = 644605624;
        int valSeed = decisionGenerator.nextInt();
        intGenerator.setSeed(valSeed);

        int iterations = 100;
        int n = 1_000_000;
        int biggest = 0;
        int range = 10;
        boolean overallOk = true;

        for (int j = 1; j <= iterations; j++) {
            System.out.println("+-+-+-+-+-+-+-+  ITERATION["+j+"]  +-+-+-+-+-+-+-+-+");
            ArrayList<Integer> arr = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if (decisionGenerator.nextBoolean()) {
                    biggest += intGenerator.nextInt(range) + 1;
                    arr.add(biggest);
                } else
                    arr.add(null);
            }

            boolean ok1 = true;
            for (int i = 0; i < n; i++) {
                if (arr.get(i) != null) {
                    ok1 = ok1 && (i == getIdx(arr, arr.get(i), Integer::compare));
                }
            }
            System.out.println("TEST for searching for non-null values: " + (ok1 ? "PASSED" : "FAILED"));
            int result = 0;
            boolean ok2 = true;
            for (int i = 0; i < n; i++) {
                if (arr.get(i) == null) {
                    int a = i;
                    while (a > 0 && arr.get(a) == null)
                        a--;
                    int b = i;
                    while (b < arr.size() - 1 && arr.get(b) == null)
                        b++;
                    if (arr.get(b) != null && arr.get(a) != null) {
                        int diff = arr.get(b) - arr.get(a);
                        if (diff > 1) {
                            result = getIdx(arr, arr.get(a) +diff - 1, Integer::compare);
                            ok2 = ok2 && result == -1;
                        }
                    }
                }
            }
            System.out.println("TEST for searching for not present values: " + (ok2 ? "PASSED" : "FAILED"));
            overallOk = overallOk && ok1 && ok2;
        }
        System.out.println("\n  O V E R A L L    T E S T    R E S U L T:  " + (overallOk ? "passed" : "failed")  );
    }
}
