package mpoljak.dataStructures;

public interface IPredicate<T> {
    public boolean evaluate(T evaluated);
    public void addSubPredicate(IPredicate<T> predicate);
    public void removeSubPredicate(IPredicate<T> predicate);
}
