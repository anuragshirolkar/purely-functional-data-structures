package pfds.immutable;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;

public class ImmutableListTest {

  @Test
  public void testEmpty() throws Exception {
    ImmutableList<Long> empty = ImmutableList.of();

    assertEquals(empty.head(), Optional.empty());
    assertEquals(empty.size(), 0);
    assertEquals(empty.tail().size(), 0);
    assertEquals(empty.prepend(1L).head(), Optional.of(1L));
  }

  @Test
  public void testNonEmpty() throws Exception {
    ImmutableList<Long> singleton = ImmutableList.of(3L);

    assertEquals(singleton.head(), Optional.of(3L));
    assertEquals(singleton.size(), 1);
    assertEquals(singleton.tail().size(), 0);
    assertEquals(singleton.deleteAt(0).size(), 0);

    ImmutableList<Long> big = ImmutableList.of(5L).prepend(4L);

    assertEquals(big.deleteAt(0).head(), Optional.of(5L));
    assertEquals(big.deleteAt(1).head(), Optional.of(4L));
  }
}