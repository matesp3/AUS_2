package mpoljak.utilities;

public class IntIdGenerator {
    private static int nextId = 1;
    public static int nextId() {
        return nextId++;
    }
}
