package pfds.immutable;

import java.util.stream.Stream;

public class ImmutableVector<T> {

    private final RadixNode<T> tree;

    public ImmutableVector() {
        tree = RadixNode.of();
    }

    private ImmutableVector(RadixNode<T> tree) {
        this.tree = tree;
    }

    public T get(int index) {
        return tree.get(index);
    }

    public ImmutableVector<T> set(int index, T value) {
        return new ImmutableVector<>(tree.set(index, value));
    }

    public ImmutableVector<T> append(T value) {
        return new ImmutableVector<>(tree.appendWithOverflow(value));
    }

    public int size() {
        return (int)tree.size();
    }

    public Stream<T> stream() {
        return tree.stream();
    }

    public static <T> ImmutableVector<T> of() {
        return new ImmutableVector<>();
    }

    public static <T> ImmutableVector<T> of(T v1) {
        return new ImmutableVector<T>().append(v1);
    }
}