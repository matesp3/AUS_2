package mpoljak.App.Logic;

/**
 * Enables object of type <code>T</code> to be represented in CSV file as single line representation of instance's state
 * @param <T> type that is stored in csv file
 */
public interface ICsvFormattable<T> {
    /**
     * Created representation of <code>T</code>'s instance within single row, where attributes are separated with
     * <code>delimiter</code>
     * @param delimiter character for delimiting attributes of type's instance
     * @param delimiterReplacement is applied for String attributes. If a String attribute contains character that's
     *                             same as <code>delimiter</code> it's replaced by this value
     * @param blankSpaceReplacement if some string attribute is null or blank, its value is represented in csv as
     *                              value of <code>blankSpaceReplacement</code>. If some string attribute has equal
     *                              value to <code>blankSpaceReplacement</code>, this value will be duplicated.
     * @return row representation of instance of type <code>T</code>.
     */
    public String toCsvLine(char delimiter, String delimiterReplacement, String blankSpaceReplacement);

    /**
     * Creates instance of <code>T</code> from string <code>csvLine</code> representation.
     * @param csvLine line from csv file that represents some instance of type <code>T</code>
     * @param delimiter character that separates instance's attributes in <code>csvLine</code>
     * @param delimiterReplacement is used for string attributes. All substrings in string attributes whose value is
     *                             equal to <code>delimiterReplacement</code> are replaced with character <code>
     *                                 delimiter</code>
     * @param blankSpaceReplacement if some string attribute has value equal
     *                               to value composed as (<code>blankSpaceReplacement</code>+
     *                               <code>blankSpaceReplacement</code>), this value will be reduced to just one value of
     *                              <code>blankSpaceReplacement</code>.
     * @return new instance of <code>T</code>
     */
    public T fromCsvLine(String csvLine, char delimiter, String delimiterReplacement, String blankSpaceReplacement);
}
