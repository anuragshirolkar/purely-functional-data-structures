package pfds.immutable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Constants {
    static final int BRANCHING_FACTOR_LOG = 5;
    static final int BRANCHING_FACTOR = (int)Utils.pow(2L, BRANCHING_FACTOR_LOG);
}

abstract class RadixNode<T> {
    abstract long size();
    abstract int level();
    abstract long maxCapacity();
    abstract RadixNode<T> append(T elem);
    abstract T get(int index);
    abstract RadixNode<T> set(int index, T value);
    abstract Stream<T> stream();

    RadixNode<T> appendWithOverflow(T elem) {
        if (size() == maxCapacity()) {
            return new RadixBranchNode<T>(Utils.arrayListOf(this)).append(elem);
        }
        return append(elem);
    }

    static <T> RadixNode<T> of() {
        return makeEmptyWithLevels(0);
    }

    static <T> RadixNode<T> makeEmptyWithLevels(int level) {
        if (level == 0) {
            return new RadixValueNode<T>(new ArrayList<T>());
        }
        return new RadixBranchNode<T>(Utils.arrayListOf(makeEmptyWithLevels(level-1)));
    }
}

class RadixBranchNode<T> extends RadixNode<T> {
    private List<RadixNode<T>> children;
    private long size;
    private int level;

    RadixBranchNode(List<RadixNode<T>> nodes) {
        children = nodes;
        List<Integer> childrenLevels = children.stream().map(RadixNode::level).distinct().collect(Collectors.toList());
        if (childrenLevels.size() != 1) {
            throw new AssertionError("Children with distinct levels.");
        }
        level = childrenLevels.get(0)+1;
        size = children.stream().map(RadixNode::size).reduce(Long::sum).orElse(0L);
    }

    @Override
    long size() {
        return size;
    }

    @Override
    int level() {
        return level;
    }

    @Override
    long maxCapacity() {
        return Utils.pow((long) Constants.BRANCHING_FACTOR, level + 1);
    }

    @Override
    RadixBranchNode<T> append(T elem) {
        if (size() == maxCapacity()) {
            throw new AssertionError("Tree is full.");
        }
        RadixNode<T> lastChild = children.get(children.size()-1);
        if (lastChild.size() != lastChild.maxCapacity()) {
            RadixNode<T> newLastChild = lastChild.append(elem);
            List<RadixNode<T>> newChildren = new ArrayList<RadixNode<T>>(children);
            newChildren.set(newChildren.size()-1, newLastChild);
            return new RadixBranchNode<T>(newChildren);
        }
        RadixNode<T> newLastChild = (RadixNode<T>) RadixNode.makeEmptyWithLevels(level - 1).append(elem);
        List<RadixNode<T>> newChildren = new ArrayList<RadixNode<T>>(children);
        newChildren.add(newLastChild);
        return new RadixBranchNode<T>(newChildren);
    }

    private int getChildIndex(int index) {
        return (index>>(Constants.BRANCHING_FACTOR_LOG*level))&(Constants.BRANCHING_FACTOR-1);

    }

    @Override
    T get(int index) {
        return children.get(getChildIndex(index)).get(index);
    }

    @Override
    RadixNode<T> set(int index, T value) {
        return children.get(getChildIndex(index)).set(index, value);
    }

    @Override
    Stream<T> stream() {
        return children.stream()
        .flatMap(child -> child.stream());
    }


}

class RadixValueNode<T> extends RadixNode<T> {
    private List<T> values;

    RadixValueNode(List<T> values) {
        this.values = values;
    }

    T getValue(int ind) {
        return values.get(ind);
    }

    @Override
    long size() {
        return values.size();
    }

    @Override
    int level() {
        return 0;
    }

    @Override
    long maxCapacity() {
        return (long) Constants.BRANCHING_FACTOR;
    }

    @Override
    RadixNode<T> append(T elem) {
        if (size() == maxCapacity()) {
            throw new AssertionError("Tree is full.");
        }
        List<T> newValues = new ArrayList<T>(values);
        newValues.add(elem);
        return new RadixValueNode<T>(newValues);
    }

    private int getChildIndex(int index) {
        return index & (Constants.BRANCHING_FACTOR-1);
    }

    @Override
    T get(int index) {
        return values.get(getChildIndex(index));
    }

    @Override
    RadixNode<T> set(int index, T value) {
        List<T> newValues = new ArrayList<>(values);
        newValues.set(getChildIndex(index), value);
        return new RadixValueNode<>(newValues);
    }

    @Override
    Stream<T> stream() {
        return values.stream();
    }
}