package mpoljak.dataStructures;

import java.util.ArrayList;
import java.util.List;

public class CompositePredicateAnd<T> extends Predicate<T> {
    List<IPredicate<T>> lPredicates;

    public CompositePredicateAnd() {
        lPredicates = new ArrayList<IPredicate<T>>();
    }

    @Override
    public boolean evaluate(T evaluated) {
        boolean result = true;
        for (IPredicate<T> predicate : lPredicates) {
            result &= predicate.evaluate(evaluated);
            if (!result)
                return false;
        }
        return result;
    }

    @Override
    public void addSubPredicate(IPredicate<T> predicate) {
        if (predicate != null) {
            lPredicates.add(predicate);
        }
    }

    @Override
    public void removeSubPredicate(IPredicate<T> predicate) {
        lPredicates.remove(predicate);
    }
}
