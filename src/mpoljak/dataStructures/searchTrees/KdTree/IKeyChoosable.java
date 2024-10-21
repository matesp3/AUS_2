package mpoljak.dataStructures.searchTrees.KdTree;

/**
 * This interface defines feature of object types which have more than one key of the same type, and therefore it can be
 * chosen by which instance of key should be the object compared.
 */
public interface IKeyChoosable {
    /** Toggles actually compared key to the next one defined in some kind of order.
      * If actually compared key was the last in the order, the first in the order is chosen.*/
    public void toggleComparedKey();
    /** Sets new actually compared key by its id which is defined within some kind of the internal key order. */
    public void setComparedKey(int key);
    /** Informs about number of available equivalent keys to choose from. Always should be higher than 1. */
    public int getKeysCount();
    /** Gives info about all available keys with their id within some key order */
    public String getKeysDescription();
}
