package mpoljak.dataStructures;

public abstract class Predicate<T> implements IPredicate<T> {

    @Override
    public boolean evaluate(T evaluated) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void addSubPredicate(IPredicate<T> predicate) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void removeSubPredicate(IPredicate<T> predicate) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
