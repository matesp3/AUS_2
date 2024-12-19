package mpoljak.dataStructures;

public interface ICloneable<T> {
    /**
     * @return new instance of <code>T</code> type with copied attribute values.
     */
    public T cloneInstance();
}
