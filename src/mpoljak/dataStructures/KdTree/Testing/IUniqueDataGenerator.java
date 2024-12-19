package mpoljak.dataStructures.KdTree.Testing;

import java.util.Random;

public interface IUniqueDataGenerator<T, I extends Comparable<I>> {

    /**
     * Generates instance with set unique identifier and values of attributes are generated with passed generator
     * @param valueGenerator valueGenerator instance with set seed to generate data for all arbitrary attributes of instance
     * @param uniqueId identifier, by which this instance can be uniquely distinguished
     * @return new instance with generated attributes and unique id
     */
    public T generateInstance(Random valueGenerator, I uniqueId);

    /**
     * Makes copy of other instance, but sets passed unique identifier for new instance.
     * @param other does copy of other instance, but sets unique identifier instead of copying it from other
     * @param uniqueId assigned unique value, by which this new instance could be distinguished
     * @return instance with same attribute values except unique identifier, which has different value
     */
    public T copyConstruct(T other, I uniqueId);
}
