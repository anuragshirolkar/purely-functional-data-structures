package pfds.immutable;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Immutable implementation of the linked ImmutableList.
 */
public interface ImmutableList<T> {

  public Optional<T> head();
  public ImmutableList<T> tail();
  public long size();
  public ImmutableList<T> prepend(T elem);
  public ImmutableList<T> append(T elem);
  public ImmutableList<T> insertAt(T elem, long position);
  public ImmutableList<T> deleteAt(long position);
  public Stream<T> stream();

  public static <T> ImmutableList<T> of() {
    return new Empty<T>();
  }

  public static <T> ImmutableList<T> of(T elem) {
    return new NonEmpty<T>(elem);
  }
}

class Empty<T> implements ImmutableList<T> {

  @Override
  public Optional<T> head() {
    return Optional.empty();
  }

  @Override
  public ImmutableList<T> tail() {
    return new Empty<T>();
  }

  @Override
  public long size() {
    return 0;
  }

  @Override
  public ImmutableList<T> prepend(T elem) {
    return ImmutableList.of(elem);
  }

  @Override
  public ImmutableList<T> append(T elem) {
    return ImmutableList.of(elem);
  }

  @Override
  public ImmutableList<T> insertAt(T elem, long position) {
    return ImmutableList.of(elem);
  }

  @Override
  public ImmutableList<T> deleteAt(long position) {
    return new Empty<T>();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Empty<?>) {
      return true;
    }
    return false;
  }

  @Override
  public Stream<T> stream() {
    return Stream.of();
  }

}

class NonEmpty<T> implements ImmutableList<T> {
  private T head;
  private ImmutableList<T> tail;
  private long length;

  NonEmpty(T h) {
    this.head = h;
    this.tail = ImmutableList.of();
    this.length = 1;
  }

  private NonEmpty(T h, ImmutableList<T> t) {
    this.head = h;
    this.tail = t;
    this.length = t.size()+1;
  }

  @Override
  public Optional<T> head() {
    return Optional.of(head);
  }

  @Override
  public ImmutableList<T> tail() {
    return tail;
  }

  @Override
  public long size() {
    return length;
  }

  @Override
  public ImmutableList<T> prepend(T elem) {
    return new NonEmpty<T>(elem, this);
  }

  @Override
  public ImmutableList<T> append(T elem) {
    return new NonEmpty<T>(head, tail.append(elem));
  }

  @Override
  public ImmutableList<T> insertAt(T elem, long position) {
    if (position == 0) {
      return append(elem);
    }
    return new NonEmpty<T>(head, tail.insertAt(elem, position-1));
  }

  @Override
  public ImmutableList<T> deleteAt(long position) {
    if (position == 0) {
      return tail;
    }
    return new NonEmpty<T>(head, tail.deleteAt(position-1));
  }

  @Override
  public Stream<T> stream() {
    return Stream.concat(Stream.of(head), tail.stream());
  }
}

