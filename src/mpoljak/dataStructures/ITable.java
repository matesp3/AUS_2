package mpoljak.dataStructures;

import mpoljak.dataStructures.KdTree.ISame;

import java.util.List;

public interface ITable<E extends T, T extends ISame<T>, K extends ITableKey> extends IFilterIterable<E>, ICloneable<ITable<E,T,K>> {
    public void insert(K key, E data);

    /**
     *
     * @param key unique or secondary
     * @param data is ignored if structure does not work with secondary keys
     * @return value that fits key, and if key is secondary then if it also fits data comparator
     */
    public E find(K key, E data);

    public List<E> findAll(K key);

    public E delete(K key, E data);
}
