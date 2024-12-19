package mpoljak.dataStructures;

public interface ICloneableStruct<T> {
    /**
     * @return new instance of <code>T</code> type with copied attribute values.
     */
    public T cloneInstance();
}
