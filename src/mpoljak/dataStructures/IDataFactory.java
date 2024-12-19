package mpoljak.dataStructures;

/**
 * Abstract factory.
 */
public interface IDataFactory {
    /**
     *
     * @param params specified parameters used for creating instance of data type depending on <code>paramType</code>.
     * @return new instance of data type specified by parameters.
     */
    public ITableData createData(IParams params);

    /**
     * Creates default parameters instance which should be updated by client and then passed to method
     * <code>createData</code>.
     * @return instance of parameters regarding chosen <code>paramType</code>
     */
    public IParams createParams();
}
