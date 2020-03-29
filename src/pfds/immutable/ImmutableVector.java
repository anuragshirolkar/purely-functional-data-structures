package pfds.immutable;

import java.util.stream.Stream;

/**
 * The immutable implementation of the Vector.
 * All the mutating methods return the mutated copy of the original vector.
 * Uses the Radix balanced tree under the hood to avoid making full copy of the
 * vector for updating.
 * @param <T> the type of elements in this vector
 */
public class ImmutableVector<T> {

    private final RadixNode<T> tree;

    /**
     * Default constructor. Provides an empty vector.
     */
    public ImmutableVector() {
        tree = RadixNode.of();
    }

    private ImmutableVector(RadixNode<T> tree) {
        this.tree = tree;
    }

    /**
     * @param <T> the type of the elements
     * @return the empty vector
     */
    public static <T> ImmutableVector<T> of() {
        return new ImmutableVector<>();
    }

    /**
     * @param <T> the type of the elements
     * @param v1 the element to be added
     * @return the singleton vector
     */
    public static <T> ImmutableVector<T> of(T v1) {
        return new ImmutableVector<T>().append(v1);
    }

    /**
     * @param index the position of the element to be retrieved
     * @return the element at the index
     */
    public T get(int index) {
        return tree.get(index);
    }

    /**
     * @param index the position of the element to be updated
     * @param value the new value of the element
     * @return the updated vector
     */
    public ImmutableVector<T> set(int index, T value) {
        return new ImmutableVector<>(tree.set(index, value));
    }

    /**
     * @param value the element to be appended
     * @return the new vector
     */
    public ImmutableVector<T> append(T value) {
        return new ImmutableVector<>(tree.appendWithOverflow(value));
    }

    /**
     * @return the number of elements in the vector
     */
    public int size() {
        return (int)tree.size();
    }

    /**
     * @return the {@code Stream} copy of the vector
     */
    public Stream<T> stream() {
        return tree.stream();
    }
}