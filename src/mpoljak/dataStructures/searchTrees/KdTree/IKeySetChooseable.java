package mpoljak.dataStructures.searchTrees.KdTree;

/**
 * This interface defines feature of object types which have more than one key set composed of the same type and number
 * of key attributes (key set), and therefore it can be
 * chosen by which key set should be the type compared.
 */
public interface IKeySetChooseable {
    /** Toggles actually compared key to the next one defined in some kind of order.
      * If actually compared key was the last in the order, the first in the order is chosen.*/
    public void toggleComparedKeySet();
    /** Sets new actually compared key by its id which is defined within some kind of the internal key order. */
    public void setComparedKeySet(int key);
    /** Informs about number of available equivalent keys to choose from. Always should be higher than 1. */
    public int getKeySetsCount();
    /** Returns id of key set, which is currently chosen. */
    public int getCurrentKeySet();
    /** Gives info about all available keys with their id within some key order */
    public String getKeySetsDescription();
}
