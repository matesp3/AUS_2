package mpoljak.dataStructures;

import java.util.Iterator;

public interface IFilterIterable<T> extends Iterable<T> {
    public Iterator<T> iterator(IPredicate<T> filter);
}
