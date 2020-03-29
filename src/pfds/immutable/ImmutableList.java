package pfds.immutable;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * The immutable implementation of the linked List.
 * All the mutating methods return the mutated copy of the list.
 * @param <T> the type of elements in this list
 */
public interface ImmutableList<T> {

  /**
   * @return the first element of the list
   */
  public Optional<T> head();

  /**
   * @return the sublist from the 2nd element onwards
   */
  public ImmutableList<T> tail();

  /**
   * @return the size of the list.
   */
  public long size();

  /**
   * @param elem the element to be added
   * @return the new list adding elem to the beginning
   */
  public ImmutableList<T> prepend(T elem);

  /**
   * @param elem the element to be added
   * @return the new list adding elem at the end
   */
  public ImmutableList<T> append(T elem);

  /**
   * @param elem the element to be inserted
   * @param position the position to insert the element at
   * @return the new list with elem at position
   */
  public ImmutableList<T> insertAt(T elem, long position);

  /**
   * @param position the position to remove the element from
   * @return the new list with the element from position removed
   */
  public ImmutableList<T> deleteAt(long position);

  /**
   * @return the {@link Stream} copy of the list
   */
  public Stream<T> stream();

  /**
   * @param <T> the type of the elements in the list
   * @return the empty list
   */
  public static <T> ImmutableList<T> of() {
    return new Empty<T>();
  }

  /**
   * @param <T> the type of the elements in the list
   * @param elem the element to put in the list
   * @return the singleton list
   */
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

