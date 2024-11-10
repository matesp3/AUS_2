package mpoljak.utilities;

public class StringIdGenerator implements IGeneratorId<String> {
    private static StringIdGenerator instance = null;

    public static StringIdGenerator getInstance() {
        if (instance == null) {
            instance = new StringIdGenerator(10);
        }
        return instance;
    }
    private static final char[] charSet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s',
            't','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9'};
    private final int[] aIdxCombinations;
    private final char[] aChars;
    private final int firstBeyondIdx;   // -1 defines max index value, which can items of aIdx contains
    private boolean lastIdxAssigned;

    private StringIdGenerator(int idLength) {
        if (idLength < 1)
            throw new IllegalArgumentException("Length of generated ID must be greater than 0");
        this.aChars = new char[idLength];
        System.arraycopy(charSet, 0, this.aChars, 0, idLength); // use firs 'idLength' chars
        this.aIdxCombinations = new int[idLength];
        this.firstBeyondIdx = aChars.length;
        this.lastIdxAssigned = false;
    }

    /**
     * Adjusts properly idx combinations, if index at level 0 has reached maximum index
     * @param level
     * @return true, if operation added has been executed. False, if it was able no more.
     */
    private boolean addInHigherLevel(int level) {
        int ci = level;
        while (ci < this.firstBeyondIdx && this.aIdxCombinations[ci] == this.firstBeyondIdx - 1) // while at current level is max index reached
            ci++;
        if (ci == this.firstBeyondIdx) // if all levels have reached maximum
            return false;
        for (int i = 0; i < ci; i++) {
            this.aIdxCombinations[i] = 0;  // reset indexes
        }
        this.aIdxCombinations[ci] = this.aIdxCombinations[ci] + 1;
        return true;
    }

    public String composeId() {
//        System.out.println();
//        System.out.print(this.aChars[ this.aIdxs[0] ]);
        StringBuilder sb = new StringBuilder(this.aChars.length);
//        for (int i = 1; i < this.aIdxs.length; i++) {
        for (int i = 0; i < this.aIdxCombinations.length; i++) {
//            System.out.print("i["+i+"] = "+this.aIdxs[i]+" , ");
//            System.out.print("_"+this.aChars[ this.aIdxs[i] ]);
            sb.append(this.aChars[ this.aIdxCombinations[i] ]);    // retrieved index into char array for current index combination
        }
        return sb.toString();
    }

    public void test() {
//        boolean shouldEnd = false;
//        int innerIdx = 0;
////        while (!shouldEnd) {
////            this.printIndexes();
////            if (this.aIdxs[innerIdx] == this.firstBeyondIdx) {
////                shouldEnd = !this.addInHigherLevel(1);
////                System.out.println();
////            }
////            this.aIdxs[innerIdx] = this.aIdxs[innerIdx]+1;
////        }
//        do {
//            this.composeId();
//            this.aIdxCombinations[0] = this.aIdxCombinations[0]+1;
//            if (this.aIdxCombinations[0] == this.firstBeyondIdx) {
//                shouldEnd = !this.addInHigherLevel(1);
////                System.out.println();
//            }
//        } while (!shouldEnd);
    }

    /**
     *
     * @return unique string combination with defined string size. Else null, if all possible unique values have
     * been requested.
     */
    @Override
    public String nextId() {    // something like iterator
        if (this.lastIdxAssigned)
            return null;
        String newId = this.composeId();
        this.aIdxCombinations[0] = this.aIdxCombinations[0] + 1;
        if (this.aIdxCombinations[0] == this.firstBeyondIdx) {
            this.lastIdxAssigned = !this.addInHigherLevel(1);
        }
        return newId;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20000; i++) {
            System.out.println(i+". - "+StringIdGenerator.getInstance().nextId());
        }

    }
}
