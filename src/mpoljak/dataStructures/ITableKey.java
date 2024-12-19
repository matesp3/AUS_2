package mpoljak.dataStructures;

public interface ITableKey {
    /**
     *
     * @return <code>true</code> if it's primary key, <code>false</code> if it's secondary key.
     */
    public boolean isUnique();

    public boolean equalsTo(ITableKey other);
}
