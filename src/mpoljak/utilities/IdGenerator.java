package mpoljak.utilities;

public class IdGenerator {
    private static IdGenerator instance = null;

    public static IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }
        return instance;
    }

    private int nextId;

    private IdGenerator() { this.nextId = 0; }

    public int nextId() {
        nextId++;
        return this.nextId;
    }
}
