package mpoljak.utilities;

public class IntegerIdGenerator implements IGeneratorId<Integer> {
    private static IntegerIdGenerator instance = null;

    public static IntegerIdGenerator getInstance() {
        if (instance == null) {
            instance = new IntegerIdGenerator();
        }
        return instance;
    }

    private int nextId;

    private IntegerIdGenerator() { this.nextId = 0; }

    public Integer nextId() {
        nextId++;
        return this.nextId;
    }
}